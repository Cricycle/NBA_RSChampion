CREATE OR REPLACE FUNCTION find_rschamp(game_day Date) RETURNS VARCHAR(63) AS $$
DECLARE
    game_month integer; -- := CAST(extract(month from timestamp game_day) AS INT);
    game_year integer; -- := CAST(extract(year from timestamp game_day) AS INT);
    output varchar(63);
BEGIN
    SELECT CAST(extract(month from game_day) AS INT) INTO game_month;
    SELECT CAST(extract(year from game_day) AS INT) INTO game_year;

    WITH RECURSIVE rschamp(team_name, day) AS (
            SELECT CAST(team_name AS VARCHAR(63)), CAST(game_date - '1 day'::INTERVAL AS DATE) AS day
            FROM nba_champions INNER JOIN nba_regular_start
            ON nba_champions.year = (CASE WHEN game_month < 7 THEN game_year-1 ELSE game_year END)
            AND nba_champions.year = nba_regular_start.year
        UNION ALL
            SELECT team_name, day FROM (
                SELECT CAST(CASE
                    WHEN team_name = losing_team THEN winning_team
                    WHEN current_date > day + '1 day'::INTERVAL THEN team_name
                    ELSE 'failure'
                END AS VARCHAR(63)) AS team_name, CAST(day + '1 day'::INTERVAL AS DATE) AS day
                FROM rschamp LEFT JOIN nba_game_data
                    ON CAST(rschamp.day + '1 day'::INTERVAL AS date) = game_date
                    AND team_name = losing_team
            ) AS main_query
            WHERE team_name <> 'failure'
    )
    SELECT team_name INTO output FROM rschamp WHERE day = game_day;

    RETURN output;
END;
$$ LANGUAGE plpgsql