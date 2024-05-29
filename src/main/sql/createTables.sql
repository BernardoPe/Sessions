drop table if exists sessions_players;
drop table if exists sessions;
drop table if exists games;
drop table if exists players;

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


create or replace procedure removeOldTokens()
language plpgsql
as $$
begin
    delete from tokens where timeExpiration < current_timestamp;
end;
$$;


create trigger removeOldTokensTrigger
before insert or update or delete on tokens
for each row
execute procedure removeOldTokens();