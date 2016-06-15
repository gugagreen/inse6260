

function isStudent() {
	return hasRole("ROLE_STUDENT");
}

function isProfessor() {
	return hasRole("ROLE_PROFESSOR");
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

function getCurrentStudent() {
	return $("#currentStudent").val();
}

function getCurrentProfessor() {
	return $("#currentProfessor").val();
}