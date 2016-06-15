$(document).ready(function() {
	$("#changePassword").click(savePass);
});

function savePass() {
	var user = $("#username").val();
	var pass = $("#pass").val();
	var newPass = $("#newPass").val();
	var passConfirm = $("#passConfirm").val();
	if (pass === "" || newPass === "" || passConfirm === "") {
		showErrorMessage("Please Fill All Required Fields");
		return

	}
	if (!(newPass === passConfirm)) {
		$("#error").show();
		$('input[type="password"],textarea').val('');
		return

	} else {
		$("#error").hide();
		AjaxChangePass(user, pass, newPass);
		$('input[type="password"],textarea').val('');
	}
};