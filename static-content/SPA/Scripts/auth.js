/**
 * Represents the player currently logged in to the SPA
 *
 * Since the SPA does not send browser requests, cookies are not used to store the user's session.
 *
 * Instead, the user's data is stored in browser memory, and is lost when the browser is closed or refreshed.
 */
let user = null

window.authRegister = authRegister
window.authLogin = authLogin

/**
 * Handles the registration of a new player
 *
 * For the registration to be successful, the player must provide a name and an email,
 * each of which must be unique and have at 3-60 characters in length.
 *
 * If the registration is successful, the user is logged in and their token is displayed.
 *
 * If the registration is unsuccessful, the user is informed of the error.
 */
function authRegister(event) {

	event.preventDefault()
	const name = document.getElementById('username').value
	const email = document.getElementById('email').value
	const nameErr = document.getElementById('error-username')
	const emailErr = document.getElementById('error-email')

	nameErr.style.display = "none"
	emailErr.style.display = "none"

	if (name === "") {
		nameErr.innerHTML = "Please enter a name"
		nameErr.style.display = "block"
		if (email === "") {
			emailErr.innerHTML = "Please enter an email"
			emailErr.style.display = "block"
		}
		return
	}

	if (email === "") {
		emailErr.innerHTML = "Please enter an email"
		emailErr.style.display = "block"
		return
	}

	fetch('/players', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({ name, email })
	}).then(res => {
			res.json()
				.then(data => {
					if (res.ok) {
						user = data
						user.name = name
						user.email = email
						document.getElementById('login').style.display = "none"
						document.getElementById('register').style.display = "none"
						document.getElementById('logout').style.display = "inline-block"
						document.getElementById('registerForm').style.display = "none"
						document.getElementById('token-info').style.display = "block"
						document.getElementById('token').innerHTML = "Your token is: " + data.token
						return data
					}
					else {
						return Promise.reject(data)
					}
				})
				.catch(err => {
					if (err.errorCause.toLowerCase().includes("email")) {
						emailErr.innerHTML = err.errorCause
						emailErr.style.display = "block"
					}
					else {
						nameErr.innerHTML = err.errorCause
						nameErr.style.display = "block"
					}
				})
		})
}


/**
 * Handles the login of an existing player
 *
 * For the login to be successful, the player must provide a unique token that follows the UUID format.
 *
 * If the login is successful, the user is logged in and redirected to the home page.
 * @param event
 */
function authLogin(event) {

	event.preventDefault()
	const token = document.getElementById('token').value
	const tokenErr = document.getElementById('error-token')

	tokenErr.style.display = "none"

	if (token === "") {
		const tokenErr = document.getElementById('error-token')
		tokenErr.innerHTML = "Please enter a token"
		tokenErr.style.display = "block"
		return
	}

	fetch('/auth', {
		method: 'GET',
		headers: {
			'Authorization': 'Bearer ' + token,
		},
	})
		.then(res => res.ok? res.json() : Promise.reject(res))
		.then(data => {
			document.getElementById('login').style.display = "none"
			document.getElementById('register').style.display = "none"
			document.getElementById('logout').style.display = "inline-block"
			user = data
			user.token = token
			window.location.href = "#home"
			return data
		}).catch(err => {
			const tokenErr = document.getElementById('error-token')
			tokenErr.innerHTML = "Invalid token"
			tokenErr.style.display = "block"
		})
}


/**
 * Handles the logout of a player
 *
 * When a user logs out, they are redirected to the home page and lose all access to authenticated features.
 */
function authLogout() {
	user = null
	document.getElementById('login').style.display = "inline-block"
	document.getElementById('register').style.display = "inline-block"
	document.getElementById('logout').style.display = "none"
	window.location.href = "#home"
}

/**
 * Returns the data of player currently logged in
 *
 * If no player is logged in, the function returns null.
 *
 * For details on how the SPA handles user sessions, see [user]{@link user}
 */
function getPlayerData() {
	return user
}

export { authRegister, getPlayerData, authLogin, authLogout }