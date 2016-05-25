$(document).ready(function() {
	$.ajax({
		url : "http://localhost:8080/courseDates"
	}).then(function(data) {
		populateCourseDatesSelect(data);
	});
	$("#term_select").change(function() {
		alert( "Handler for .change() called: " +  this.value);
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

function drawHeader() {
	var row = $('<tr>');
	var cel1 = $('<th>').text('Id');
	var cel2 = $('<th>').text('Size');
	row.append(cel1);
	row.append(cel2);
	return row;
}

function drawRow(rowData) {
	var row = $('<tr>');
	var cel1 = $('<td>').text(rowData.id);
	var cel2 = $('<td>').text(rowData.size);
	row.append(cel1);
	row.append(cel2);
	return row;
}
