
CREATE TABLE IF NOT EXISTS nba_teams (
	team_name		VARCHAR(32) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS nba_champions (
	team_name		VARCHAR(32) REFERENCES nba_teams(team_name),
	year			INT
);

CREATE TABLE IF NOT EXISTS nba_game_data (
	home_team		VARCHAR(32) NOT NULL,
	away_team		VARCHAR(32) NOT NULL,
	home_score		INT 		NOT NULL,
	away_score		INT 		NOT NULL,
	game_date		DATE 		NOT NULL,

	CONSTRAINT play_diff_team CHECK (home_team <> away_team),
	CONSTRAINT FK_home_team FOREIGN KEY(home_team) REFERENCES nba_teams(team_name),
	CONSTRAINT FK_away_team FOREIGN KEY(away_team) REFERENCES nba_teams(team_name),
	CONSTRAINT PK_one_game_a_day PRIMARY KEY(game_date, home_team)
);

	