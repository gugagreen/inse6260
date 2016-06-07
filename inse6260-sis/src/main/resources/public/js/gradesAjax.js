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