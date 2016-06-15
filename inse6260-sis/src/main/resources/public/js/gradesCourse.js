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
	var link = $('<select/>').attr({
		id : 'grade_' + rowData.username
	});
	var data = ['NOT_SET', 'A_PLUS', 'A', 'A_MINUS', 'B_PLUS', 'B', 'B_MINUS', 'C_PLUS', 'C', 'C_MINUS', 'F'];
	for (var val in data) {
		$('<option />', {value: data[val], text: data[val]}).appendTo(link);
	}
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
		var gradeSelect = $("#grade_" + student.username);
		// if there is a current grade, add to select, otherwise its is 'NOT_SET'
		if (currentGrade) {
			gradeSelect.val(currentGrade);
		} else {
			gradeSelect.val('NOT_SET');
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
	var allGrades = $( "select[id^='grade_']" );
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
