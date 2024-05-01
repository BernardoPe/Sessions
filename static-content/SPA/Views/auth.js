import {div, form, button, p} from "../WebDSL/web_dsl.js";
import {errorMessage, formInputField} from "./Models/models.js";

/**
 * Creates the HTML login page view.
 *
 * For details on the login operation see [authLogin]{@link authLogin}
 */
function loginView() {
	return (
		div({class: "form__group fade-up"},
			form({onsubmit: "authLogin(event)"},
                formInputField(
                    "token",
                    "Token",
                    "password",
                    "Token"
				),
                errorMessage("error-token", ""),
				button({type: "submit"}, "Login")
			),
		)
	)
}

/**
 * Creates the HTML register page view.
 *
 * For details on the register operation see [authRegister]{@link authRegister}
 */
function registerView() {
	return (
		div(null,
			div({class: "form__group fade-up", id: "registerForm"},
				form({onsubmit: "authRegister(event)"},
                    formInputField(
                        "username",
                        "Username",
                        "text",
                        "Username"
					),
                    errorMessage("error-username", ""),
                    formInputField(
                        "email",
                        "Email",
                        "text",
                        "Email"
					),
                    errorMessage("error-email", ""),
					button({type: "submit"}, "Register")
				),
			),
			div({class: "subtitle", id: "token-info", style: "display: none"},
				p({id: "token"}, ""),
                button({type: "submit", onclick: "window.location.href = '#home'"}, "Go to home page")
			)
		)
	)
}

export {loginView, registerView}