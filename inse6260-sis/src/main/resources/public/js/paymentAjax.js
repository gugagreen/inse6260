const ROOT_PATH = "http://localhost:8080";

function ajaxLoadStudents() {
	$.ajax({
		url : ROOT_PATH + "/user/student/"
	}).then(function(data) {
		populateStudentsSelect(data);
	});
}

/**
 * Load balance for a given student. 
 */
function ajaxLoadBalanceForStudent(studentId) {
	// http://localhost:8080/payment/student/student1
	$.ajax({
		url : ROOT_PATH + "/payment/student/" + studentId
	}).then(function(data) {
		populateBalance(data);
	});
}

/**
 * Add a payment to a student
 * @param studentId
 * @param courseEntryId
 */
function ajaxAddCourseForStudent(studentId, value) {
	// "/payment/student/{username}/value/{value}"
	if (studentId !== "") {
		$.ajax({
			headers: { 
				'Content-Type': 'application/json; charset=utf-8' 
		    },
			type: "POST",
			url: ROOT_PATH + "/payment/student/" + studentId + "/value/" + value, 
			success: 
				function(result){
					showSuccessMessage("payment added");
					ajaxLoadBalanceForStudent(studentId);
				},
			error:
				function(errorMsg) {
					showErrorMessage(errorMsg.responseText);
				}
		});
	}
}
