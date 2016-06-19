/**
 * Whenever #student_select changes, populates #currentStudent with student username and loads student cart
 */
function handleModifyStudent() {
	$("#student_select").change(function() {
		$('#currentStudent').val(this.value);
		ajaxLoadCartForStudent(this.value);
	});
}

function populateCart(data) {
	$('#cart').empty();
	var table = drawCartTable(data);
	$('#cart').append(table);
}

function drawCartTable(data) {
	var table = $('<table>');
	table.attr({ class: ["table-bordered"]});
	table.append(drawCartHeader());

	for (var i = 0; i < data.length; i++) {
		table.append(drawCartRow(data[i]));
	}
	return table;
}

function drawCartHeader() {
	var headers = [ 'Id', 'Course', 'Days', 'Time', 'Date', 'Disc', 'Grade', 'Status' ];
	var link = "";
	return createRow(headers, true, link);
}

function drawCartRow(rowData) {
	var dates = rowData.courseEntry.dates;

	var values = [ rowData.id, rowData.courseEntry.course.code, dates.weekDays, dates.startTime + '-' + dates.endTime,
			formatDate(dates.startDate) + " - " + formatDate(dates.endDate), formatDate(dates.discDate), rowData.grade, rowData.status ];
	var link = $('<button>', {
		text : 'Delete',
		id : 'btn_del_' + rowData.id,
		click : function() {
			ajaxDeleteCourseForStudent(getCurrentStudent(), rowData.courseEntry.id)
		}
	});
	return createRow(values, false, link);
}