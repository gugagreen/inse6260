const ROOT_PATH = "http://localhost:8080";

/**
 * setup dynamic elements after page is loaded
 */
$(document).ready(function() {
	// load course dates
	loadCourseDates();
	// handle modify season
	handleModifySeason();
	// load cart
	loadCart();
	// handle modify student
	handleModifyStudent();
});

/**
 * Load course dates and populate #term_select combo box with possible seasons.
 */
function loadCourseDates() {
	$.ajax({
		url : ROOT_PATH + "/courseDates"
	}).then(function(data) {
		populateCourseDatesSelect(data);
	});
}

/**
 * Whenever #term_select changes, populates #allCourses div with existing courses for the selected season
 */
function handleModifySeason() {
	$("#term_select").change(function() {
		$.ajax({
			url : ROOT_PATH + "/courses/" + this.value
		}).then(function(data) {
			populateCourses(data);
		});
	});
}

/**
 * Load cart for a student. If user is not a student it will prompt user for student selection.
 */
function loadCart() {
	if (isStudent()) {
		var studentId = $("#username").val();
		$('#currentStudent').val(studentId);
		loadCartForStudent(studentId);
	} else if (isAdmin()) {
		$('#studentSelectDiv').show();
		ajaxLoadStudents();
	}
}

/**
 * Whenever #student_select changes, populates #currentStudent with student username and loads student cart 
 */
function handleModifyStudent() {
	$("#student_select").change(function() {
		$('#currentStudent').val(this.value);
		loadCartForStudent(this.value);
		// FIXME - re-populate courses to adjust add button's function
		// populateCourses(
	});
}

function isStudent() {
	return hasRole("ROLE_STUDENT");
}

function isAdmin() {
	return hasRole("ROLE_ADMIN");
}

function hasRole(roleName) {
	var roles = $("#authorities").val();
	if (roles.indexOf(roleName) > -1) {
		return true;
	} else {
		return false;
	}
}

/**
 * Load cart for a given student. 
 */
function loadCartForStudent(studentId) {
	$.ajax({
		url : ROOT_PATH + "/cart/student/" + studentId
	}).then(function(data) {
		populateCart(data);
	});
}

function populateCourseDatesSelect(data) {
	var select = $('#term_select');
	for (var i = 0; i < data.length; i++) {
		select.append($("<option />").val(data[i]).text(data[i]));
	}
}

function populateCourses(data) {
	$('#allCourses').empty();
	var table = drawCoursesTable(data);
	$('#allCourses').append(table);
}

function populateCart(data) {
	$('#cart').empty();
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
					id: 'btn_add_' + rowData.id,
					click: function() {ajaxAddCourseForStudent(rowData)}
				}
			);
	return createRow(values, false, link);
}

function ajaxAddCourseForStudent(rowData) {
	if ($("#currentStudent").val() !== "") {
		$.ajax({
			headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
			type: "POST",
			url: ROOT_PATH + "/cart/student/" + $("#currentStudent").val() + "/courseEntry/" + rowData.id, 
			success: 
				function(result){
					loadCartForStudent($("#currentStudent").val());
				}
		});
	}
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
					click: function() {ajaxDeleteCourseForStudent(rowData)}
				}
			);
	return createRow(values, false, link);
}

function ajaxDeleteCourseForStudent(rowData) {
	if ($("#currentStudent").val() !== "") {
		alert("delete: [" + rowData.courseEntry.id + "] for : [" + $("#currentStudent").val() + "]");
		
		$.ajax({
			headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
			type: "DELETE",
			url: ROOT_PATH + "/cart/student/" + $("#currentStudent").val() + "/courseEntry/" + rowData.courseEntry.id, 
			success: 
				function(result){
					loadCartForStudent($("#currentStudent").val());
				}
		});
	}
}

function ajaxLoadStudents() {
	$.ajax({
		url : ROOT_PATH + "/user/student/"
	}).then(function(data) {
		populateStudentsSelect(data);
	});
}

function populateStudentsSelect(data) {
	var select = $('#student_select');
	for (var i = 0; i < data.length; i++) {
		select.append($("<option />").val(data[i].username).text(data[i].username));
	}
}
