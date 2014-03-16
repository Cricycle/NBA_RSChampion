WITH RECURSIVE rschamp(team_name, day) AS (
        SELECT CAST(team_name AS VARCHAR(32)), CAST('2013 Oct 28' AS DATE) AS day
        FROM nba_champions
        WHERE year = 2013
    UNION ALL
        SELECT team_name, day FROM (
            SELECT CAST(CASE
                WHEN team_name = losing_team THEN winning_team
                WHEN current_date > day + '1 day'::INTERVAL THEN team_name
                ELSE 'failure'
            END AS varchar(32)) AS team_name, CAST(day + '1 day'::INTERVAL AS DATE) AS day
            FROM rschamp LEFT JOIN nba_game_data
                ON CAST(rschamp.day + '1 day'::INTERVAL AS date) = game_date
                AND team_name = losing_team
        ) AS main_query
        WHERE team_name <> 'failure'
)
SELECT * FROM rschamp;