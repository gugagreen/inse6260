/**
 * setup dynamic elements after page is loaded
 */
$(document).ready(function() {
	// load student
	loadCurrentStudent();
	// handle modify student
	handleModifyStudent();
});

function loadCurrentStudent() {
	if (isStudent()) {
		var studentId = $("#username").val();
		$('#currentStudent').val(studentId);
		ajaxLoadTrasncriptForStudent(getCurrentStudent());
	} else if (isAdmin()) {
		$('#studentSelectDiv').show();
		ajaxLoadStudents();
	}
}

/**
 * Whenever #student_select changes, populates #currentStudent with student username and loads student cart 
 */
function handleModifyStudent() {
	$("#student_select").change(function() {
		$('#currentStudent').val(this.value);
		ajaxLoadTrasncriptForStudent(this.value);
	});
}

function populateStudentsSelect(data) {
	var select = $('#student_select');
	for (var i = 0; i < data.length; i++) {
		select.append($("<option />").val(data[i].username).text(data[i].username));
	}
}

function populateTranscript(transcript) {
	$('#transcript').empty();
	if (transcript.academicRecords && transcript.academicRecords.length > 0) {
		$('#transcript').append(drawTranscriptTitle(transcript));
		var table = drawTranscriptTable(transcript.academicRecords);
	} else {
		$('#transcript').append($('<p>').append("No finished courses for student"));
	}
	$('#transcript').append(table);
}

function drawTranscriptTitle(transcript) {
	var paragraph = $('<p>');
	paragraph.append("Student: " + transcript.studentUsername);
	paragraph.append('<p>');
	paragraph.append(" Cumulative Grade Point Average: " + transcript.gpa);
	return paragraph;
}

function drawTranscriptTable(academicRecords) {
	var table = $('<table>');
	table.append(drawTranscriptHeader());	

	for (var i = 0; i < academicRecords.length; i++) {
		table.append(drawTranscriptRow(academicRecords[i]));
    }
	return table;
}

function drawTranscriptHeader() {
	var headers = ['Id', 'Code', 'Name', 'Credits', 'Season', 'End Date', 'Grade'];
	return createRow(headers, true, null);
}

function drawTranscriptRow(rowData) {
	var course = rowData.courseEntry.course;
	var courseDates = rowData.courseEntry.dates;
	var values = [rowData.id, course.code, name, course.credits, courseDates.season, formatDate(courseDates.endDate), rowData.grade];
	return createRow(values, false, null);
}
