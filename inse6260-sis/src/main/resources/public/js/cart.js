$(document).ready(function() {
	// load course dates
	$.ajax({
		url : "http://localhost:8080/courseDates"
	}).then(function(data) {
		populateCourseDatesSelect(data);
	});
	// handle modify season
	$("#term_select").change(function() {
		$.ajax({
			url : "http://localhost:8080/courses/" + this.value
		}).then(function(data) {
			$('#allCourses').empty();
			populateCourses(data);
		});
	});
	// load cart
	$.ajax({
		url : "http://localhost:8080/cart/student/" + $("#username").val()
	}).then(function(data) {
		populateCart(data);
	});
});

function populateCourseDatesSelect(data) {
	var select = $('#term_select');
	for (var i = 0; i < data.length; i++) {
		select.append($("<option />").val(data[i]).text(data[i]));
	}
}

function populateCourses(data) {
	var table = drawCoursesTable(data);
	$('#allCourses').append(table);
}

function populateCart(data) {
	var table = drawCartTable(data);
	$('#cart').append(table);
}

function createRow(rowData, isHeader, link) {
	var row = $('<tr>');
	var celDef = (isHeader) ? ('<th>'): ('<td>');
	for (var i = 0; i < rowData.length; i++) {
		var cel = $(celDef).text(rowData[i]);
		row.append(cel);
	}
	
	var celLink = $(celDef).html(link);
	row.append(celLink);
	
	return row;
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
					text: 'Add',
					id: 'btn_' + rowData.id,
					click: function() {
						// FIXME - switch alert with a post call to server
						alert('hello ' + rowData.course.code);
					}
				}
			);
	return createRow(values, false, link);
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
					id: 'btn_' + rowData.id,
					click: function() {
						// FIXME - switch alert with a post call to server
						alert('hello ' + rowData.courseEntry.course.code);
					}
				}
			);
	return createRow(values, false, link);
}
