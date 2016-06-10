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
					alert("Password Changed!");
				}else{
					alert(data);
				};
			}
	});
}