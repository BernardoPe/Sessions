import {button, div, form} from "../WebDSL/web_dsl.js";
import {dateTimeInput, errorMessage, formInputField, genresInput} from "./models.js";

function gameCreateView() {
    return div({class: "form__group"},
        form({id: "gameCreationForm", method: "POST"},
            formInputField(
                "game_name",
                "game_name",
                "text",
                "Game name",
                "Enter the name of the game",
                true,
            ),
            errorMessage("err_message-game", "Game name must be at least 3 characters long"),

            formInputField(
                "developer",
                "developer",
                "text",
                "Developer name",
                "Enter the name of the developer",
                true,
            ),
            errorMessage("err_message-developer", "Developer name must be at least 3 characters long"),

            genresInput(),
            button({type: "submit"}, "Create Game"),
        ),
    );
}

function sessionCreateView() {
    return div(
        form({id: "sessionCreationForm", method: "POST"},
            formInputField(
                "game_name",
                "game_name",
                "text",
                "Game name",
                "Enter the name of the game",
                true,
            ),
            errorMessage("err_message-game", "Game name must be at least 3 characters long"),

            formInputField(
                "capacity",
                "capacity",
                "number",
                "Capacity of the session",
                "Enter the capacity of the session",
                true,
            ),
            errorMessage("err_message-game", "Game name must be at least 3 characters long"),

            dateTimeInput(),
            errorMessage("error-date", "Invalid date to create a session"),

            button({type: "submit"}, "Create Session"),
        ),
    );
}

export {gameCreateView, sessionCreateView};