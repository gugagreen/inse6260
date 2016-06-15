const ROOT_PATH = "http://localhost:8080";

function AjaxChangePass(username, password, newPassword) {
	$.ajax({
		headers: { 
	        'Accept': 'text/plain',
	    },
		type: "POST",
		url: ROOT_PATH + "/changePassword/user/" + username + "/password/" + password + "/newPassword/" + newPassword, 
		success: 
			function(data){
				if(data==='OK'){
					showSuccessMessage("Password Changed!");
				}else{
					showErrorMessage(data);
				};
			},
		error: 
			function(errorMsg) {
				showErrorMessage(errorMsg.responseText);
			}
	});
}