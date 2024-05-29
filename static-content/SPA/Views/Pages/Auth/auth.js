import {div, form, button, p} from "../../../WebDSL/web_dsl.js";
import {formInputField} from "../../Models/Inputs/form.js";
import {errorMessage} from "../../Models/Errors/error.js";
import {authLogin, authRegister} from "../../../Scripts/auth.js";
/**
 * Creates the HTML login page view.
 *
 * For details on the login operation see [authLogin]{@link authLogin}
 */
function loginView() {
	return (
		div({class: "form__group fade-up"},
			form({onsubmit: (event) => { authLogin(event) }},
                formInputField(
                    "name",
                    "Name",
                    "text",
                    "Name"
				),
				errorMessage("error-name", ""),
				formInputField(
					"password",
					"Password",
					"password",
					"Password"
				),
                errorMessage("error-password", ""),
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
		div({class: "center"},
			div({class: "form__group fade-up", id: "registerForm"},
				form({onsubmit: (event) => { authRegister(event) }},
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
					formInputField(
						"password",
						"Password",
						"password",
						"Password"
					),
					errorMessage("error-password", ""),
					formInputField(
						"confirm-password",
						"Confirm Password",
						"password",
						"Confirm Password"
					),
					errorMessage("error-confirm-password", ""),
					button({type: "submit"}, "Register")
				),
			),
			div({class: "subtitle", id: "token-info", style: "display: none"},
				p({id: "token"}, ""),
                button({type: "submit", onclick: () => { window.location.href = '#home' } }, "Go to home page")
			)
		)
	)
}

export {loginView, registerView}