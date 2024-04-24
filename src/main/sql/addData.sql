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