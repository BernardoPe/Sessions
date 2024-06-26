import {button, div, form} from "../../../WebDSL/web_dsl.js";
import {formInputField, formInputWithSearchResults} from "../../Models/Inputs/form.js";
import {errorMessage} from "../../Models/Errors/error.js";
import {dateTimeInput} from "../../Models/Inputs/date.js";
import {genresInput} from "../../Models/Inputs/genres.js";
import {submitFormCreateGame, submitFormCreateSession} from "../../../Scripts/formSubmit.js"

/**
 * Creates the view for the game creation form
 *
 * For details about the game creation operation, see [Game Creation]{@link submitFormCreateGame}
 */
function gameCreateView() {
    return div({class: "form__group fade-up"},
        form({onsubmit: (event) => { submitFormCreateGame(event) } },
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


/**
 * Creates the view for the session creation form
 *
 * For details about the session creation operation, see [Session Creation]{@link submitFormCreateSession}
 */
function sessionCreateView() {
    return div({class: "form__group fade-up"},
        form({onsubmit: (event) => { submitFormCreateSession(event) } },
            formInputField(
                "capacity",
                "capacity",
                "number",
                "Session capacity"
            ),
            errorMessage("err_message-capacity", "Capacity must be a number"),
            formInputWithSearchResults(
                "game_name",
                "games",
                "text",
                "Game name"
            ),
            errorMessage("err_message-game", "Game name must be at least 3 characters long"),
            dateTimeInput(),
            errorMessage("err_message-date", "Invalid date to create a session"),
            button({type: "submit"}, "Create Session"),
        ),
    );
}

export {gameCreateView, sessionCreateView};