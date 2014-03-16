CREATE TABLE nba_teams (
	team_name		VARCHAR(63) PRIMARY KEY
);

CREATE TABLE nba_champions (
	team_name		VARCHAR(63) REFERENCES nba_teams(team_name),
	year			INT PRIMARY KEY
);

CREATE TABLE nba_game_data (
	winning_team	VARCHAR(63) NOT NULL,
	losing_team		VARCHAR(63) NOT NULL,
	winning_score	INT 		NOT NULL,
	losing_score	INT 		NOT NULL,
	game_date		DATE 		NOT NULL,
	game_loc        VARCHAR(63) NOT NULL,

	CONSTRAINT play_diff_team CHECK (winning_team <> losing_team),
	CONSTRAINT PK_one_game_a_day PRIMARY KEY(game_date, winning_team)
);

CREATE FUNCTION add_new_teams() RETURNS TRIGGER AS $new_teams$
    BEGIN
        -- get the team names that have been recently added
        -- then insert them into nba_teams
        INSERT INTO nba_teams(team_name)
        SELECT NEW.winning_team
        WHERE NOT EXISTS (SELECT * FROM nba_teams WHERE team_name = NEW.winning_team);

        INSERT INTO nba_teams(team_name)
        SELECT NEW.losing_team
        WHERE NOT EXISTS (SELECT * FROM nba_teams WHERE team_name = NEW.losing_team);

        RETURN NEW;
    END;
$new_teams$ LANGUAGE plpgsql;

CREATE TRIGGER trg_add_team_name AFTER INSERT ON nba_game_data
    FOR EACH ROW EXECUTE PROCEDURE add_new_teams();