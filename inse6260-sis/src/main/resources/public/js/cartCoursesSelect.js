

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
	table.append(drawCoursesHeader());	

	for (var i = 0; i < data.length; i++) {
		table.append(drawCoursesRow(data[i]));
    }
	return table;
}

function drawCoursesHeader() {
	var headers = ['Id','Enrolled', 'Size', 'Code', 'Name', 'Credits', 'Professor', 'Days', 'Start Time - End Time', 'Start Date - End Date'];
	var link = "";
	return createRow(headers, true, link);
}

function drawCoursesRow(rowData) {
	var values = [rowData.id,'FIXME', rowData.size, rowData.course.code, rowData.course.name, rowData.course.credits, rowData.professor.username, rowData.dates.weekDays, rowData.dates.startTime +'-'+ rowData.dates.endTime, formatDate(rowData.dates.startDate)+" - "+formatDate(rowData.dates.endDate)];
	var link = $('<button>', {
					text: 'Add',
					id: 'btn_add_' + rowData.id,
					click: function() {ajaxAddCourseForStudent(getCurrentStudent(), rowData.id)}
				}
			);
	return createRow(values, false, link);
}