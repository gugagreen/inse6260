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
function ajaxLoadTrasncriptForStudent(studentId) {
	// http://localhost:8080/user/student/student1/transcript
	$.ajax({
		url : ROOT_PATH + "/user/student/" + studentId + "/transcript"
	}).then(function(data) {
		populateTranscript(data);
	});
}
