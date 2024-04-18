import {div, h1, form, input, button, label, p, a} from "../WebDSL/web_dsl.js";

function loginView() {
	return (
		div({class: "form__group"},
			form({id: "loginForm", method: "POST"},
				div({class: "form__input"},
					input({type: "password", class: "form__field", id: "token", placeholder: "token"}),
					label("token", {class:"form__label"}, "Token"),
				),
				div({class: "error-message-container"},
					p({class: "error_message", id: "error-token"}, "")
				),
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
					div({class: "form__input"},
						input({type: "text", class: "form__field", id: "username", placeholder: "username"}),
						label("username", {class:"form__label"}, "Username"),
					),
					div ({class: "error-message-container"},
						p({class: "error_message", id: "error-username"}, "")
					),
					div({class: "form__input"},
						input({type: "text", class: "form__field", id: "email", placeholder: "email"}),
						label("password2", {class:"form__label"}, "Email"),
					),
					div ({class: "error-message-container"},
						p({class: "error_message", id: "error-email"}, "")
					),
					button({type: "submit"}, "Register")
				),
			),
			div({class: "subtitle", id: "token-info", style: "display: none"},
				p({id: "token"}, ""),
				a({href: "#home"}, null,"Go to home page")
			)
		)
	)
}

export {loginView, registerView}