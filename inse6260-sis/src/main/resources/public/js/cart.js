$(document).ready(function() {
	$("#term_select").change(function() {
		alert( "Handler for .change() called: " +  this.value);
	});
	$.ajax({
		url : "http://localhost:8080/courses"
	}).then(function(data) {
		drawTable(data);
	});
});


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
