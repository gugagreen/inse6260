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

function createRow(rowData, isHeader, link) {
	var row = $('<tr>');
	var celDef = (isHeader) ? ('<th>'): ('<td>');
	for (var i = 0; i < rowData.length; i++) {
		var cel = $(celDef).text(rowData[i]);
		row.append(cel);
	}
	
	var celLink = $(celDef).html(link);
	row.append(celLink);
	
	return row;
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


function getCurrentProfessor() {
	return $("#currentProfessor").val();
}

function populateProfessorsSelect(data) {
	var select = $('#professor_select');
	for (var i = 0; i < data.length; i++) {
		select.append($("<option />").val(data[i].username).text(data[i].username));
	}
}
