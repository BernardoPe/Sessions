import {button, div, i} from "../../../WebDSL/web_dsl.js";

function confirmationDiv(action, divID) {

	const cancelButton = button({class: "confirmation__confirm"},
		i({class: "fas fa-times fa-2x red"})
	);

	const confirmButton = button({class: "confirmation__cancel"},
		i({class: "fas fa-check fa-2x green"})
	);

	cancelButton.addEventListener('click', () => cancelConfirmation(divID));
	confirmButton.addEventListener('click', () => confirmAction(divID, action));

	return div({class: "confirmation__buttons fade-in split" , id: "confirmation__buttons-" + divID},
		confirmButton,
		cancelButton,
	)

}
function cancelConfirmation(divID) {
	document.getElementById("confirmation__buttons-" + divID).remove()
	document.getElementById(divID).style.display = "block"
}

function confirmAction(divID, action) {
	document.querySelector(".confirmation__buttons").remove()
	action()
}


window.cancelConfirmation = cancelConfirmation
window.confirmAction = confirmAction



export {confirmationDiv}