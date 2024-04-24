drop table if exists sessions_players;
drop table if exists sessions;
drop table if exists games;
drop table if exists players;

CREATE TABLE games (
   id SERIAL PRIMARY KEY,
   name VARCHAR(40) UNIQUE NOT NULL,
   developer VARCHAR(40) NOT NULL,
   genres VARCHAR(40)[] NOT NULL
   CONSTRAINT valid_genres CHECK (ARRAY['Action', 'Adventure', 'RPG', 'Strategy', 'Turn-Based']::VARCHAR(40)[] @> genres)
);

create table players (
    id serial primary key,
    name varchar(40) unique not null,
    email varchar(40) unique not null check (email like '%_@_%.__%'),
    token_hash int8 unique not null
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

--insert into players (name, email, token_hash) values ('John Doe', 'testemail@a.pt', 0);
--select * from players;
--select * from games;
select * from sessions;
--select * from sessions_players;

SELECT * FROM games WHERE name = 'demoGame225';

CREATE OR REPLACE PROCEDURE fill_games()
    LANGUAGE plpgsql
AS $$
BEGIN
    FOR i IN 1..100000 LOOP
            INSERT INTO games (name, developer, genres)
            VALUES ('demoGame' || i, 'demoDeveloper' || i, ARRAY['Action', 'Adventure', 'RPG']);
        END LOOP;
END;
$$;

CREATE OR REPLACE PROCEDURE fill_sessions()
    LANGUAGE plpgsql
AS $$
BEGIN
    FOR i IN 1..100000 LOOP
        INSERT INTO sessions (game_id, capacity, date)
        VALUES (i, 10, CURRENT_TIMESTAMP + (i || ' days')::INTERVAL);
    END LOOP;
END;
$$;

CALL fill_games();
CALL fill_sessions();


SELECT COUNT(*) FROM (SELECT * FROM sessions JOIN games ON sessions.game_id = games.id LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id LEFT JOIN players ON sessions_players.player_id = players.id ) as sessions WHERE session_id IN (SELECT session_id FROM sessions_players WHERE player_id = ?)


SELECT sessions.session_id as sid, sessions.game_id as gid, date, capacity, game.name as gname, genres, developer, players.id as pid, players.name as pname, email, token_hash
FROM (
         SELECT *
         FROM sessions
                  JOIN games ON sessions.game_id = games.id
                  LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id
                  LEFT JOIN players ON sessions_players.player_id = players.id
         WHERE session_id IN (SELECT session_id FROM sessions_players WHERE player_id = ?)
         ORDER BY session_id
         LIMIT ? OFFSET ?
     ) as sessions;

SELECT sessions.id as sid, sessions.game_id as gid, date, capacity, games.name as gname, genres, developer, players.id as pid, players.name as pname,email, token_hash FROM sessions JOIN games ON sessions.game_id = games.id LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id LEFT JOIN players ON sessions_players.player_id = players.id WHERE session_id IN (SELECT session_id FROM sessions_players WHERE player_id = ?)
ORDER BY sessions.id LIMIT ? OFFSET ?;


SELECT sessions.id as sid, sessions.game_id as gid, date, capacity, games.name as gname, genres, developer, players.id as pid, players.name as pname,email, token_hash FROM sessions JOIN games ON sessions.game_id = games.id LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id LEFT JOIN players ON sessions_players.player_id = players.id WHERE session_id IN (SELECT session_id FROM sessions_players WHERE player_id = ?) ORDER BY sid LIMIT ? OFFSET ?
SELECT sessions.id as sid, sessions.game_id as gid, date, capacity, games.name as gname, genres, developer, players.id as pid, players.name as pname,email, token_hash FROM sessions JOIN games ON sessions.game_id = games.id LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id LEFT JOIN players ON sessions_players.player_id = players.id ORDER BY sid LIMIT ('5'::int4) OFFSET ('0'::int4)


SELECT DISTINCT ON (sessions.id) sessions.id as sid, sessions.game_id as gid, date, capacity, games.name as gname, genres, developer, players.id as pid, players.name as pname, email, token_hash
FROM sessions
         JOIN games ON sessions.game_id = games.id
         LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id
         LEFT JOIN players ON sessions_players.player_id = players.id
ORDER BY sessions.id
LIMIT 5 OFFSET 0;


SELECT DISTINCT sessions.id  FROM sessions JOIN games ON sessions.game_id = games.id LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id LEFT JOIN players ON sessions_players.player_id = players.id WHERE session_id IN (SELECT session_id FROM sessions_players WHERE player_id = ('1'::int4)) ORDER BY sessions.id LIMIT ('5'::int4) OFFSET ('0'::int4)
SELECT COUNT(*) FROM (SELECT DISTINCT sessions.id FROM sessions JOIN games ON sessions.game_id = games.id LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id LEFT JOIN players ON sessions_players.player_id = players.id ) as sessions

SELECT DISTINCT sessions.id FROM sessions JOIN games ON sessions.game_id = games.id LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id LEFT JOIN players ON sessions_players.player_id = players.id WHERE date >= now() WHERE player_id = ? ORDER BY sessions.id LIMIT ? OFFSET ?
