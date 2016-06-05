/**
 * setup dynamic elements after page is loaded
 */
$(document).ready(function() {
	// load course dates
	ajaxLoadCourseDates();
	// handle modify season
	handleModifySeason();
	// load cart
	loadCart();
	// handle modify student
	handleModifyStudent();
});

/**
 * Load cart for a student. If user is not a student it will prompt user for student selection.
 */
function loadCart() {
	if (isStudent()) {
		var studentId = $("#username").val();
		$('#currentStudent').val(studentId);
		ajaxLoadCartForStudent(studentId);
	} else if (isAdmin()) {
		$('#studentSelectDiv').show();
		ajaxLoadStudents();
	}
}

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

function populateStudentsSelect(data) {
	var select = $('#student_select');
	for (var i = 0; i < data.length; i++) {
		select.append($("<option />").val(data[i].username).text(data[i].username));
	}
}

function getCurrentStudent() {
	return $("#currentStudent").val();
}
