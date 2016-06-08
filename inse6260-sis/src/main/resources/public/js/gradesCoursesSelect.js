

/**
 * Whenever #term_select changes, populates #allCourses div with existing courses for the selected season
 */
function handleModifySeason() {
	$("#term_select").change(function() {
		if (getCurrentProfessor() !== "") {
			ajaxLoadCoursesForSeason(this.value, getCurrentProfessor());
		}
	});
}

function handleModifyProfessor() {
	$("#professor_select").change(function() {
		$('#currentProfessor').val(this.value);
		// check if term is not null
		if ($("#term_select").val()) {
			ajaxLoadCoursesForSeason($("#term_select").val(), this.value);
		}
		
	});
}

function populateCourses(data) {
	$('#allCourses').empty();
	var table = drawCoursesTable(data);
	$('#allCourses').append(table);
}

function drawCoursesTable(data) {
	var table = $('<table>');
	table.append(drawCoursesHeader());	

	for (var i = 0; i < data.length; i++) {
		table.append(drawCoursesRow(data[i]));
    }
	return table;
}

function drawCoursesHeader() {
	var headers = ['Id', 'Size', 'Code', 'Name', 'Credits', 'Professor'];
	var link = "";
	return createRow(headers, true, link);
}

function drawCoursesRow(rowData) {
	var values = [rowData.id, rowData.size, rowData.course.code, rowData.course.name, rowData.course.credits, rowData.professor.username];
	var link = $('<button>', {
					text: 'Open',
					id: 'btn_open_' + rowData.id,
					click: function() {ajaxShowCourse(rowData)}
				}
			);
	return createRow(values, false, link);
}