SELECT game_date, CAST(extract(year from game_date) as INT) AS year INTO nba_regular_start
FROM
(
    SELECT game_date, ROW_NUMBER() OVER (PARTITION BY extract(year from game_date) ORDER BY game_date) AS rank
    FROM nba_game_data
    WHERE extract(month from game_date) > 6
) AS subq
WHERE rank = 1;