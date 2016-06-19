
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
	var label = $('<label for="search">Search by ID, Code, Name, and Professor</label>');
	var input = $('<input id="search" class="search" placeholder="Search">');

	
	$('#allCourses').append(label);
	$('#allCourses').append(input);	
	$('#allCourses').append(table);
	loadSearch();
}

function drawCoursesTable(data) {
	var table = $('<table>');
	table.attr({ class: ["table-bordered"]});
	table.append('<thead id="header"><tr><th>Id</th><th>Size</th><th>Code</th><th>Name</th><th>Credits</th><th>Professor</th><th>Days</th><th>Time</th><th>Date</th><th>Disc</th></tr>');
	
	var headers = [ 'Id', 'Size', 'Code', 'Name', 'Credits', 'Professor', 'Days', 'Time',
	    			'Date', 'Disc' ];
	table.append('<tbody class="list">');
	for (var i = 0; i < data.length; i++) {
		table.append(drawCoursesRow(data[i], headers));
	}
	return table;
}


function drawCoursesRow(rowData, headers) {
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
	return createCourseRow(values, headers, link);
}

function createCourseRow(rowData,headers, link) {
	var row = $('<tr>');
	var celDef = ('<td>');
	for (var i = 0; i < rowData.length; i++) {
		var cel = $(('<td class='+headers[i]+'>')).text(rowData[i]);
		row.append(cel);
	}
	
	if (link) {
		var celLink = $(celDef).html(link);
		row.append(celLink);
	}
	
	return row;
}

function loadSearch(){
	var options = {
			valueNames:['Id','Code', 'Name', 'Professor',]
	};
	
	var userList = new List('allCourses',options);
}