
function showSuccessMessage(message) {
	showModalMessage("Success", message);
}

function showErrorMessage(message) {
	showModalMessage("Error", message);
}

function showModalMessage(title, message) {
	$("#messageModal").find(".modal-title").html(title);
	$("#messageModal").find(".modal-body").html(message);
	$("#messageModal").modal("show");
}