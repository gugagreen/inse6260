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

function drawHeader() {
	var row = $('<tr>');
	var cel1 = $('<th>').text('Id');
	var cel2 = $('<th>').text('Size');
	var cel3 = $('<th>').text('Code');
	var cel4 = $('<th>').text('Name');
	var cel5 = $('<th>').text('Credits');
	var cel6 = $('<th>').text('Professor');
	row.append(cel1);
	row.append(cel2);
	row.append(cel3);
	row.append(cel4);
	row.append(cel5);
	row.append(cel6);
	return row;
}

function drawRow(rowData) {
	var row = $('<tr>');
	var cel1 = $('<td>').text(rowData.id);
	var cel2 = $('<td>').text(rowData.size);
	var cel3 = $('<td>').text(rowData.course.code);
	var cel4 = $('<td>').text(rowData.course.name);
	var cel5 = $('<td>').text(rowData.course.credits);
	var cel6 = $('<td>').text(rowData.professor.username);
	row.append(cel1);
	row.append(cel2);
	row.append(cel3);
	row.append(cel4);
	row.append(cel5);
	row.append(cel6);
	return row;
}
