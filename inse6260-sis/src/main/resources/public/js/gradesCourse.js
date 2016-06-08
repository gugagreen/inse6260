function populateCourse(students, courseEntry) {
	$('#courseDiv').empty();
	$('#courseDiv').append(drawCourseTitle(courseEntry));
	var table = drawStudentsTable(students);
	$('#courseDiv').append(table);
	$('#courseDiv').append(drawGradesButton(courseEntry));
}

function drawCourseTitle(courseEntry) {
	var paragraph = $('<p>');
	paragraph.append("Course: " + courseEntry.course.code);
	return paragraph;
}

function drawStudentsTable(data) {
	var table = $('<table>');
	table.append(drawStudentsHeader());

	for (var i = 0; i < data.length; i++) {
		table.append(drawStudentsRow(data[i]));
	}
	return table;
}

function drawStudentsHeader() {
	var headers = [ 'Student' ];
	var link = "Grade";
	return createRow(headers, true, link);
}

function drawStudentsRow(rowData) {
	// FIXME - check if grade already exists for student
	var values = [ rowData.username ];
	var link = $('<input/>').attr({
		type : 'text',
		id : 'grade_' + rowData.username
	});
	return createRow(values, false, link);
}

function drawGradesButton(courseEntry) {
	var gradesButton = $('<button>', {
		text : 'Update Grades',
		id : 'btn_update_grades_' + courseEntry.id,
		click : function() {
			// FIXME - rest call to update grades
			alert("not implemented");
			var allGrades = $( "input[id^='grade_']" );
			alert(allGrades.length);
			//ajaxDeleteCourseForStudent(getCurrentStudent(), rowData.courseEntry.id)
		}
	});
	return gradesButton;
}