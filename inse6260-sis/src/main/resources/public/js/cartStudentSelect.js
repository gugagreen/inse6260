
/**
 * Whenever #student_select changes, populates #currentStudent with student username and loads student cart 
 */
function handleModifyStudent() {
	$("#student_select").change(function() {
		$('#currentStudent').val(this.value);
		ajaxLoadCartForStudent(this.value);
		// FIXME - re-populate courses to adjust add button's function
		// populateCourses(
	});
}

function populateCart(data) {
	$('#cart').empty();
	var table = drawCartTable(data);
	$('#cart').append(table);
}

function drawCartTable(data) {
	var table = $('<table>');
	table.append(drawCartHeader());	

	for (var i = 0; i < data.length; i++) {
		table.append(drawCartRow(data[i]));
    }
	return table;
}

function drawCartHeader() {
	var headers = ['Id', 'Course', 'Grade', 'Status'];
	var link = "";
	return createRow(headers, true, link);
}

function drawCartRow(rowData) {
	var values = [rowData.id, rowData.courseEntry.course.code, rowData.grade, rowData.status];
	var link = $('<button>', {
					text: 'Delete',
					id: 'btn_del_' + rowData.id,
					click: function() {
						ajaxDeleteCourseForStudent(getCurrentStudent(), rowData.courseEntry.id)
					}
				}
			);
	return createRow(values, false, link);
}