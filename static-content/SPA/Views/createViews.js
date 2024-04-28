import {button, div, form} from "../WebDSL/web_dsl.js";
import {dateTimeInput, errorMessage, formInputField, formInputWithSearchResults, genresInput} from "./models.js";

function gameCreateView() {
    return div({class: "form__group"},
        form({id: "gameCreationForm", method: "POST"},
            formInputField(
                "game_name",
                "game_name",
                "text",
                "Game name"
            ),
            errorMessage("err_message-game", "Game name must be at least 3 characters long"),

            formInputField(
                "developer_name",
                "developer",
                "text",
                "Developer name"
            ),
            errorMessage("err_message-developer", "Developer name must be at least 3 characters long"),

            genresInput(),
            errorMessage("err_message-genres", "Select at least one genre"),
            button({type: "submit"}, "Create Game"),
        ),
    );
}

function sessionCreateView() {
    return div({class: "form__group"},
        form({id: "sessionCreationForm", method: "POST"},
            formInputField(
                "capacity",
                "capacity",
                "number",
                "Session capacity"
            ),
            errorMessage("err_message-capacity", "Capacity must be a number"),
            dateTimeInput(),
            errorMessage("err_message-date", "Invalid date to create a session"),
            formInputWithSearchResults(
                "game_name",
                "games",
                "text",
                "Game name"
            ),
            errorMessage("err_message-game", "Game name must be at least 3 characters long"),

            button({type: "submit"}, "Create Session"),
        ),
    );
}

export {gameCreateView, sessionCreateView};