drop table if exists sessions_players;
drop table if exists sessions;
drop table if exists games;
drop table if exists players;
drop table if exists tokens;

CREATE TABLE games (
   id SERIAL PRIMARY KEY,
   name VARCHAR(60) UNIQUE NOT NULL,
   developer VARCHAR(40) NOT NULL,
   genres VARCHAR(40)[] NOT NULL
   CONSTRAINT valid_genres CHECK (ARRAY['Action', 'Adventure', 'RPG', 'Turn-Based', 'Shooter']::VARCHAR(40)[] @> genres)
);

create table players (
     id serial primary key,
     name varchar(60) unique not null,
     email varchar(40) unique not null check (email like '%_@_%.__%'),
     password_hash varchar(60) unique not null
);

create table tokens
(
    token          VARCHAR(256) primary key,
    player_id      int references players (id) on delete cascade not null,
    timeCreation   timestamp                                     not null,
    timeExpiration timestamp                                     not null
);


CREATE TABLE sessions (
      id SERIAL PRIMARY KEY,
      game_id INT REFERENCES games(id) ON DELETE CASCADE NOT NULL, -- 1 to N relationship between games and sessions
      capacity INT NOT NULL CHECK (capacity > 0 AND capacity < 101),
      date TIMESTAMP NOT NULL CHECK (date > CURRENT_TIMESTAMP)
);


/**
  * N to N relationship between sessions and players must be represented by a separate table
 */
CREATE TABLE sessions_players (
      session_id INT REFERENCES sessions(id) ON DELETE CASCADE,
      player_id INT REFERENCES players(id) ON DELETE CASCADE,
      PRIMARY KEY (session_id, player_id)
);


-- Trigger for sessions_players table
CREATE OR REPLACE FUNCTION check_on_player_add() RETURNS TRIGGER AS $$

DECLARE
    session_date TIMESTAMP;
BEGIN
    SELECT date INTO session_date FROM sessions WHERE id = NEW.session_id;
    IF session_date < CURRENT_TIMESTAMP THEN
        RAISE EXCEPTION 'Session is closed';
    END IF;

    PERFORM * FROM sessions_players WHERE session_id = NEW.session_id FOR UPDATE;
    IF (SELECT COUNT(*) FROM sessions_players WHERE session_id = NEW.session_id) + 1 > (SELECT capacity FROM sessions WHERE id = NEW.session_id) THEN
        RAISE EXCEPTION 'Session Full';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_on_player_add_trigger BEFORE INSERT ON sessions_players
    FOR EACH ROW EXECUTE PROCEDURE check_on_player_add();

-- Trigger for sessions table
CREATE OR REPLACE FUNCTION check_capacity_on_session_update() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.capacity < (SELECT COUNT(*) FROM sessions_players WHERE session_id = NEW.id) THEN
        RAISE EXCEPTION 'Capacity cannot be less than the number of players in the session';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_capacity_on_session_update_trigger BEFORE UPDATE ON sessions
    FOR EACH ROW EXECUTE PROCEDURE check_capacity_on_session_update();

SELECT * FROM sessions_players where session_id = 1;

