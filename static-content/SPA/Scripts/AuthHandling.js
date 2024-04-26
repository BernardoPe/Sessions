let user = null

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

function authLogout() {
	user = null
	document.getElementById('login').style.display = "inline-block"
	document.getElementById('register').style.display = "inline-block"
	document.getElementById('logout').style.display = "none"
	window.location.href = "#home"
}

function getPlayerData() {
	return user
}

export { authRegister, getPlayerData, authLogin, authLogout }
