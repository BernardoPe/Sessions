
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
	const nameErr = document.getElementById('error-username')
	const emailErr = document.getElementById('error-email')
	const passErr = document.getElementById('error-password')

	nameErr.style.display = "none"
	emailErr.style.display = "none"
	passErr.style.display = "none"

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

	if (password === "") {
		passErr.innerHTML = "Please enter a password"
		passErr.style.display = "block"
		return
	}

	fetch('/players', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({name, email, password})
	}).then(res => {
			res.json()
				.then(data => {
					if (res.ok) {
						document.getElementById('login').style.display = "none"
						document.getElementById('register').style.display = "none"
						document.getElementById('logout').style.display = "inline-block"
						document.getElementById('registerForm').style.display = "none"
						document.getElementById('token-info').style.display = "block"
						document.getElementById('token').innerHTML = "Your token is: " + data.token
						const player = {pid: data.pid, name: name, email: email}
						sessionStorage.setItem('user', JSON.stringify(player))
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
	const name = document.getElementById('name').value
	const password = document.getElementById('password').value
	const nameErr = document.getElementById('error-name')
	const passErr = document.getElementById('error-password')

	nameErr.style.display = "none"
	passErr.style.display = "none"

	if (name === "") {
		nameErr.innerHTML = "Please enter a valid name"
		nameErr.style.display = "block"
	}

	if (password === "") {
		passErr.innerHTML = "Please enter a password"
		passErr.style.display = "block"
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
			document.getElementById('registerForm').style.display = "none"
			document.getElementById('token-info').style.display = "block"
			document.getElementById('token').innerHTML = "Your token is: " + data.token
			const player = {pid: data.pid, name: name}
			sessionStorage.setItem('userLogin', JSON.stringify(player)) // changed from user to userLogin for tests purposes
			window.location.href = "#home"
			return data
		}).catch(() => {
		if (err.errorCause.toLowerCase().includes("name")) {
			nameErr.innerHTML = err.errorCause
			nameErr.style.display = "block"
		} else {
			passErr.innerHTML = err.errorCause
			passErr.style.display = "block"
		}
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
