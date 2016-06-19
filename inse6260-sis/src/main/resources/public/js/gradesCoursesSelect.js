

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
	table.attr({ class: ["table-bordered"]});
	table.append(drawCoursesHeader());	

	for (var i = 0; i < data.length; i++) {
		table.append(drawCoursesRow(data[i]));
    }
	return table;
}

function drawCoursesHeader() {
	var headers = ['Id', 'Size', 'Code', 'Name', 'Credits', 'Professor', 'Days', 'Start Time - End Time', 'Start Date','-', 'End Date'];
	var link = "";
	return createRow(headers, true, link);
}

function drawCoursesRow(rowData) {
	var values = [rowData.id, rowData.size, rowData.course.code, rowData.course.name, rowData.course.credits, rowData.professor.username, rowData.dates.weekDays, rowData.dates.startTime +'-'+ rowData.dates.endTime, formatDate(rowData.dates.startDate),"-",formatDate(rowData.dates.endDate)];
	var link = $('<button>', {
					text: 'Open',
					id: 'btn_open_' + rowData.id,
					click: function() {ajaxShowCourse(rowData)}
				}
			);
	return createRow(values, false, link);
}

function formatDate(date){
	var dateStamp = new Date(date);
	var yyyy = dateStamp.getFullYear().toString();
	var mm = (dateStamp.getMonth()+1).toString(); // getMonth() is zero-based
    var dd  = dateStamp.getDate().toString();
	return (mm[1]?mm:"0"+mm[0]) + "/" + (dd[1]?dd:"0"+dd[0]) +"/" + yyyy;
}