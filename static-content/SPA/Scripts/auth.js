
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
	const password = document.getElementById('password').value
	const confirmPassword = document.getElementById('password2').value

	const registerErr = document.getElementById('error-register')

	if (name === "") {
		registerErr.style.display = "block"
		registerErr.innerHTML = "Please enter a name"
		return
	}

	if (email === "") {
		registerErr.style.display = "block"
		registerErr.innerHTML = "Please enter an email"
		return
	}

	if (password === "") {
		registerErr.style.display = "block"
		registerErr.innerHTML = "Please enter a password"
		return
	}

	if (password !== confirmPassword) {
		registerErr.style.display = "block"
		registerErr.innerHTML = "Passwords do not match"
		return
	}

	fetch('/players', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({name, email, password})
	}).then(res => res.ok ? res.json() : Promise.reject(res))
		.then(data => {
			document.getElementById('login').style.display = "none"
			document.getElementById('register').style.display = "none"
			document.getElementById('logout').style.display = "inline-block"
			window.location.href = "#home"
			const player = {pid: data.pid, name: name, email: email}
			sessionStorage.setItem('user', JSON.stringify(player))
			return data
		})
		.catch(err => {
			err.json().then(errBody => {
				registerErr.innerHTML = errBody.errorCause
				registerErr.style.display = "block"
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
	const name = document.getElementById('name').value
	const password = document.getElementById('password').value

	const loginErr = document.getElementById('error-login')

	loginErr.style.display = "none"

	if (name === "") {
		loginErr.innerHTML = "Please enter a name"
		loginErr.style.display = "block"
		return
	}

	if (password === "") {
		loginErr.innerHTML = "Please enter a password"
		loginErr.style.display = "block"
		return
	}

	fetch('/players/login', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({name, password})
	})
		.then(res => res.ok ? res.json() : Promise.reject(res))
		.then(data => {
			document.getElementById('login').style.display = "none"
			document.getElementById('register').style.display = "none"
			document.getElementById('logout').style.display = "inline-block"
			const player = {pid: data.pid, name: name}
			sessionStorage.setItem('user', JSON.stringify(player))
			window.location.href = "#home"
			return data
		}).catch(() => {
			loginErr.innerHTML = "Invalid credentials"
			loginErr.style.display = "block"
		})
}


async function tryAuth() {
	await fetch('/auth', {
		method: 'POST',
		credentials: 'same-origin',
	})
		.then(res => res.ok? res.json() : Promise.reject(res))
		.then(data => {
			document.getElementById('logout').style.display = "inline-block"
			sessionStorage.setItem('user', JSON.stringify(data))
			return data
		}).catch(() => {
			document.getElementById('login').style.display = "inline-block"
			document.getElementById('register').style.display = "inline-block"
			return null
	})
}

/**
 * Handles the logout of a player
 *
 * When a user logs out, they are redirected to the home page and lose all access to authenticated features.
 */
function authLogout() {
	fetch('/logout', {
		method: 'GET',
		credentials: 'same-origin',
	}).then(res => {
		if (!res.ok) {
			return Promise.reject(res)
		}
	})
	sessionStorage.clear()
	document.getElementById('login').style.display = "inline-block"
	document.getElementById('register').style.display = "inline-block"
	document.getElementById('logout').style.display = "none"
	window.location.href = "#home"
}

/**
 * Returns the data of player currently logged in
 *
 * Stored data includes the player's name, email, and ID.
 *
 * If no player is logged in, the function returns null.
 *
 */
function getPlayerData() {
	return JSON.parse(sessionStorage.getItem('user'))
}

export { authRegister, getPlayerData, authLogin, authLogout, tryAuth }
