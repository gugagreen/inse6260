function populateCourse(students, courseEntry) {
	$('#courseDiv').empty();
	$('#courseDiv').append(drawCourseTitle(courseEntry));
	var table = drawStudentsTable(students);
	$('#courseDiv').append(table);
	loadCurrentGrades(students, courseEntry);
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
	var values = [ rowData.username ];
	var link = $('<input/>').attr({
		type : 'text',
		id : 'grade_' + rowData.username
	});
	return createRow(values, false, link);
}

function loadCurrentGrades(students, courseEntry) {
	for (var i = 0; i < students.length; i++) {
		var student = students[i];
		var currentGrade;
		// look for current grade in student academicRecords
		for (var j = 0; j < student.academicRecords.length; j++) {
			var record = student.academicRecords[j];
			if (record.courseEntry.id === courseEntry.id) {
				currentGrade = record.grade;
				break;
			}
		}
		// if there is a current grade, add to input
		if (currentGrade) {
			var gradeInput = $("#grade_" + student.username);
			gradeInput.val(currentGrade);
		}
	}
}

function drawGradesButton(courseEntry) {
	var gradesButton = $('<button>', {
		text : 'Update Grades',
		id : 'btn_update_grades_' + courseEntry.id,
		click : function() {
			studentGrades = buildStudentGradesJson();
			ajaxUpdateGradesForCourse(courseEntry.id, studentGrades);
		}
	});
	return gradesButton;
}

function buildStudentGradesJson() {
	var gradesJson = "[";
	var allGrades = $( "input[id^='grade_']" );
	for (var i = 0; i < allGrades.length; i++) {
		currentGrade = allGrades[i];
		gradesJson += JSON.stringify({"studentUsername":currentGrade.id.replace('grade_',''),"grade":currentGrade.value});
		if (i < allGrades.length -1) {
			gradesJson += ",";
		}
	}
	gradesJson += "]";
	return gradesJson;
}
