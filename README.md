This project will mine data from the web about the select NBA regular season.

It will then allow/display who the Regular Season Champion has been over the
course of the season.

The definition of Regular Season Champion is taken from:
http://grantland.com/the-triangle/introducing-the-nba-regular-season-championship-belt/

============================
To use this project in the intended way:

You'll need to have a postgres server set up locally, though I suppose you could manipulate the code to connect to a remote database.
You can download here: http://www.postgresql.org/download/

This download includes PgAdminIII, which you need to use to create a new login role: nbaMiner, with pazsword nbaMiner.
You can change those things if you want.  Just make sure to always be careful with pazswords.

On your local postgres server, set up a database called 'NBA', make sure nbaMiner has the correct permissions.

Once that is done, you'll need to run the .sql scripts to set up the tables.

Steps:
Run table_creation.sql -- creates tables, and triggers on nba_game_data
Run NBAMain loadRegular 1946 -- loads regular season + playoff game data into nba_game_data, trigger loads team names into nba_teams
Run NBAMain loadChamps -- loads champ data into nba_champions
Run generate_regular_start.sql -- generates the start date for the regular season based off of the nba_game_data, makes nba_regular_start table
Run fn_find_rschamp.sql -- creates the function find_rschamp(Date), which returns the team which is the regular season champ on the given date.