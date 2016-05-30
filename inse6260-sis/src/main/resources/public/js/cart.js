$(document).ready(function() {
	$.ajax({
		url : "http://localhost:8080/courseDates"
	}).then(function(data) {
		populateCourseDatesSelect(data);
	});
	$("#term_select").change(function() {
		$.ajax({
			url : "http://localhost:8080/courses/" + this.value
		}).then(function(data) {
			$('#allCourses').empty();
			drawTable(data);
		});
	});
});

function populateCourseDatesSelect(data) {
	var select = $('#term_select');
	for (var i = 0; i < data.length; i++) {
		select.append($("<option />").val(data[i]).text(data[i]));
	}
}

function drawTable(data) {
	var table = $('<table>');
	table.append(drawHeader());	

	for (var i = 0; i < data.length; i++) {
		table.append(drawRow(data[i]));
    }
	
	$('#allCourses').append(table);
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

function drawHeader() {
	var headers = ['Id', 'Size', 'Code', 'Name', 'Credits', 'Professor'];
	var link = "";
	return createRow(headers, true, link);
}

function drawRow(rowData) {
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
