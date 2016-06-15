const ROOT_PATH = "http://localhost:8080";

/**
 * Load course dates and populate #term_select combo box with possible seasons.
 */
function ajaxLoadCourseDates() {
	$.ajax({
		url : ROOT_PATH + "/courseDates"
	}).then(function(data) {
		populateCourseDatesSelect(data);
	});
}

/**
 * Load courses for a given year and season
 * @param yearSeason
 */
function ajaxLoadCoursesForSeason(yearSeason) {
	$.ajax({
		url : ROOT_PATH + "/courses/" + yearSeason
	}).then(function(data) {
		populateCourses(data);
	});
}

/**
 * Load cart for a given student. 
 */
function ajaxLoadCartForStudent(studentId) {
	$.ajax({
		url : ROOT_PATH + "/cart/student/" + studentId
	}).then(function(data) {
		populateCart(data);
	});
}

function ajaxAddCourseForStudent(studentId, courseEntryId) {
	if (studentId !== "") {
		$.ajax({
			headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
			type: "POST",
			url: ROOT_PATH + "/cart/student/" + studentId + "/courseEntry/" + courseEntryId, 
			success: 
				function(result){
					ajaxLoadCartForStudent(studentId);
				}
		});
	}
}

function ajaxDeleteCourseForStudent(studentId, courseEntryId) {
	if (studentId !== "") {
		$.ajax({
			headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
			type: "DELETE",
			url: ROOT_PATH + "/cart/student/" + studentId + "/courseEntry/" + courseEntryId, 
			success: 
				function(result){
					ajaxLoadCartForStudent(studentId);
				},
			error:
				function(errorMsg) {
					$("#errorModal").find(".modal-body").html(errorMsg.responseText);
					$("#errorModal").modal("show");
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