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
 * Load courses for a given professor and year and season
 * @param yearSeason
 * @param professor
 */
function ajaxLoadCoursesForSeason(yearSeason, professor) {
	$.ajax({
		url : ROOT_PATH + "/courses/" + yearSeason + "/professor/" + professor
	}).then(function(data) {
		populateCourses(data);
	});
}

function ajaxLoadProfessors() {
	$.ajax({
		url : ROOT_PATH + "/user/professor/"
	}).then(function(data) {
		populateProfessorsSelect(data);
	});
}

function ajaxShowCourse(courseEntry) {
	if (courseEntry && courseEntry.id) {
		$.ajax({
			headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
			type: "GET",
			url: ROOT_PATH + "/courses/" + courseEntry.id + "/student/", 
			success: 
				function(students){
					populateCourse(students, courseEntry);
				}
		});
	}
}

function ajaxUpdateGradesForCourse(courseEntryId, studentGrades) {
	if (courseEntryId && studentGrades) {
		$.ajax({
			headers: {
		        'Content-Type': 'application/json; charset=utf-8' 
		    },
			type: "PUT",
			url: ROOT_PATH + "/courses/" + courseEntryId + "/updateGrades/", 
			//dataType: 'json',
			data: studentGrades,
			success: 
				function(data){
					alert("grades updated");
				},
			error: function(errorThrown){
		        alert("error: " + errorThrown);
			}
		});
	}
}