const ROOT_PATH = "http://localhost:8080";

function ajaxLoadStudents() {
	$.ajax({
		url : ROOT_PATH + "/user/student/"
	}).then(function(data) {
		populateStudentsSelect(data);
	});
}

/**
 * Load transcript for a given student. 
 */
function ajaxLoadBalanceForStudent(studentId) {
	// http://localhost:8080/payment/student/student1
	$.ajax({
		url : ROOT_PATH + "/payment/student/" + studentId
	}).then(function(data) {
		populateBalance(data);
	});
}
