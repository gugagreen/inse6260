/**
 * setup dynamic elements after page is loaded
 */
$(document).ready(function() {
	// load student
	loadCurrentStudent();
	// handle modify student
	handleModifyStudent();
});

function loadCurrentStudent() {
	if (isStudent()) {
		var studentId = $("#username").val();
		$('#currentStudent').val(studentId);
		ajaxLoadBalanceForStudent(getCurrentStudent());
	} else if (isAdmin()) {
		$('#studentSelectDiv').show();
		ajaxLoadStudents();
	}
}

/**
 * Whenever #student_select changes, populates #currentStudent with student username and loads student balance 
 */
function handleModifyStudent() {
	$("#student_select").change(function() {
		$('#currentStudent').val(this.value);
		ajaxLoadBalanceForStudent(this.value);
	});
}

function populateStudentsSelect(data) {
	var select = $('#student_select');
	for (var i = 0; i < data.length; i++) {
		select.append($("<option />").val(data[i].username).text(data[i].username + " - " + data[i].origin));
	}
}

function populateBalance(balance) {
	$('#balance').empty();
	$('#balance').append(drawBalanceTitle(balance));
	
	if (balance.debts && balance.debts.length > 0) {
		var debtsTable = drawDebtsTable(balance.debts);
		$('#balance').append($('<h4>').append("Debts"));
		$('#balance').append(debtsTable);
	} else {
		$('#balance').append($('<h4>').append("No debts found!"));
	}
	
	if (balance.payments && balance.payments.length > 0) {
		var paymentsTable = drawPaymentsTable(balance.payments);
		$('#balance').append($('<h4>').append("Payments"));
		$('#balance').append(paymentsTable);
	} else {
		$('#balance').append($('<h4>').append("No payments found!"));
	}
}

function drawBalanceTitle(balance) {
	var paragraph = $('<h3>');
	paragraph.append("Total balance: " + balance.total);
	return paragraph;
}

function drawDebtsTable(debts) {
	var table = $('<table>');
	table.attr({ class: ["table-bordered"]});
	table.append(drawDebtsHeader());	

	for (var i = 0; i < debts.length; i++) {
		table.append(drawDebtsRow(debts[i]));
    }
	return table;
}

function drawDebtsHeader() {
	var headers = ['Code', 'Name', 'Credits', 'Season', 'End Date', 'Base Cost'];
	return createRow(headers, true, null);
}

function drawDebtsRow(rowData) {
	var course = rowData.courseEntry.course;
	var courseDates = rowData.courseEntry.dates;
	var values = [course.code, course.name, course.credits, courseDates.season, formatDate(courseDates.endDate), rowData.courseEntry.baseCost];
	return createRow(values, false, null);
}

function drawPaymentsTable(payments) {
	var table = $('<table>');
	table.attr({ class: ["table-bordered"]});
	table.append(drawPaymentsHeader());	

	for (var i = 0; i < payments.length; i++) {
		table.append(drawPaymentsRow(payments[i]));
    }
	return table;
}

function drawPaymentsHeader() {
	var headers = ['Id', 'Date', 'Value'];
	return createRow(headers, true, null);
}

function drawPaymentsRow(rowData) {
	var values = [rowData.id, formatDate(rowData.date), rowData.value];
	return createRow(values, false, null);
}

function addPayment() {
	var value = $('#paymentValue').val();
	if (Number(value) < 0.01 || Number(value) > 5000) {
		showErrorMessage("Value should be betwee [0.01, 5000.00]");
		$('#paymentValue').val('');
	} else {
		var studentId = getCurrentStudent();
		if (studentId) {
			ajaxAddCourseForStudent(studentId, value);
		} else {
			showErrorMessage("No student selected");
		}
	}
}

