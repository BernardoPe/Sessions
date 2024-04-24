import {br, button, div, fieldset, form, input, label, legend} from "../WebDSL/web_dsl.js";

function gameCreateView() {
    return div({class: "form__group"},
        form({id: "gameCreationForm", method: "POST"},

            div({class: "form__input"},
                input({id: "game_name", class: "form__field", type: "text", placeholder: "Enter the name of the game"}),
                label("game_name", {class: "form__label", required: true}, "Game name"),
            ),

            div({class: "form__input"},
                input({
                    id: "developer_name",
                    class: "form__field",
                    type: "text",
                    placeholder: "Enter the name of the developer"
                }),
                label("developer_name", {class: "form__label", required: true}, "Developer name"),
            ),

            fieldset(null,
                legend(null, "Select Genres:"),
                input({id: "RPG", type: "checkbox", name: "genre", value: "RPG"}),
                label("RPG", null, "RPG"),
                br(null),
                input({id: "Adventure", type: "checkbox", name: "genre", value: "Adventure"}),
                label("Adventure", null, "Adventure"),
                br(null),
                input({id: "Shooter", type: "checkbox", name: "genre", value: "Shooter"}),
                label("Shooter", null, "Shooter"),
                br(null),
                input({id: "TurnBased", type: "checkbox", name: "genre", value: "Turn-Based"}),
                label("TurnBased", null, "TurnBased"),
                br(null),
                input({id: "Action", type: "checkbox", name: "genre", value: "Action"}),
                label("Action", null, "Action"),
                br(null),
            ),
            button({type: "submit"}, "Create Game"),
        ),
    );
}

function sessionCreateView() {
    return div(
        form({id: "sessionCreationForm", method: "POST"},
            div({class: "form__input"},
                input({
                    id: "capacity",
                    class: "form__field",
                    type: "number",
                    placeholder: "Enter the capacity of the session"
                }),
                label("capacity", {class: "form__label", required: true}, "Capacity of the session"),
            ),

            div({class: "form__input"},
                input({
                    id: "date",
                    type: "datetime-local",
                    class: "form__field datepicker",
                    placeholder: "yyyy-mm-dd",
                    name: "date",
                    value: ""
                }),
                label("date", {class: "form__label"}, "Date"),
            ),

            div({class: "form__input"},
                input({id: "game_name", class: "form__field", type: "text", placeholder: "Enter the name of the game"}),
                label("game_name", {class: "form__label", required: true}, "Game name"),
            ),

            button({type: "submit"}, "Create Session"),
        ),
    );
}

export {gameCreateView, sessionCreateView};