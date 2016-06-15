/**
 * setup dynamic elements after page is loaded
 */
$(document).ready(function() {
	// load course dates
	ajaxLoadCourseDates();
	// set current professor
	loadCurrentProfessor();
	// handle modify season
	handleModifySeason();
	// handle modify professor
	handleModifyProfessor();
});

function populateCourseDatesSelect(data) {
	var select = $('#term_select');
	for (var i = 0; i < data.length; i++) {
		select.append($("<option />").val(data[i]).text(data[i]));
	}
}

function loadCurrentProfessor() {
	if (isProfessor()) {
		var professorId = $("#username").val();
		$('#currentProfessor').val(professorId);
	} else if (isAdmin()) {
		$('#professorSelectDiv').show();
		ajaxLoadProfessors();
	}
}

function populateProfessorsSelect(data) {
	var select = $('#professor_select');
	for (var i = 0; i < data.length; i++) {
		select.append($("<option />").val(data[i].username).text(data[i].username));
	}
}
