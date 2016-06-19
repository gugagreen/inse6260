
/**
 * Whenever #term_select changes, populates #allCourses div with existing courses for the selected season
 */
function handleModifySeason() {
	$("#term_select").change(function() {
		ajaxLoadCoursesForSeason(this.value);
	});
}

function populateCourses(data) {
	$('#allCourses').empty();
	var table = drawCoursesTable(data);
	$('#allCourses').append(table);
}

function drawCoursesTable(data) {
	var table = $('<table>');
	table.attr({ class: ["table-bordered"]});
	table.append(drawCoursesHeader());

	for (var i = 0; i < data.length; i++) {
		table.append(drawCoursesRow(data[i]));
	}
	return table;
}

function drawCoursesHeader() {
	var headers = [ 'Id', 'Size', 'Code', 'Name', 'Credits', 'Professor', 'Days', 'Time',
			'Date', 'Disc' ];
	var link = "";
	return createRow(headers, true, link);
}

function drawCoursesRow(rowData) {
	var values = [ rowData.id, rowData.size, rowData.course.code, rowData.course.name, rowData.course.credits,
			rowData.professor.username, rowData.dates.weekDays, rowData.dates.startTime + '-' + rowData.dates.endTime,
			formatDate(rowData.dates.startDate) + " - " + formatDate(rowData.dates.endDate), formatDate(rowData.dates.discDate) ];
	var link = $('<button>', {
		text : 'Add',
		id : 'btn_add_' + rowData.id,
		click : function() {
			ajaxAddCourseForStudent(getCurrentStudent(), rowData.id)
		}
	});
	return createRow(values, false, link);
}