import {div, form, button, p} from "../WebDSL/web_dsl.js";
import {errorMessage, formInputField} from "./models.js";

function loginView() {
	return (
		div({class: "form__group"},
			form({id: "loginForm", method: "POST"},
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

function registerView() {
	return (
		div(null,
			div({class: "form__group", id: "registerForm"},
				form({id: "registerForm", method: "POST"},
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