var dataList;
var userID;
var userName;
var password;
var loginSec;
var userRole;
var userDisplayName;

function showLoader() {
	$(".loader-box").css('display', 'block');
}

function hideLoader() {
	$(".loader-box").css('display', 'none');
}


/*
 * 
 * Login Functions and validation
 * 
 */

//window.onload=getInstanceName;
/*
 * 

window.addEventListener('DOMContentLoaded', (event) => {
	getInstanceName();
});

function getInstanceName(){
	$.ajax({
//		url : "/RSD/rest/RSDLoginDataController/getInstanceName?m=" + Math.random(),
		url : "/RSD/rest/RSDLoginDataController/getInstanceName",
//		data : "",
		type : 'GET',
		dataType : 'json', 
		cache: true,
		success : function(response) {
				 document.getElementById("instanceName").innerHTML= response.InstanceName;
		},
		error : function(request, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});
	
}
 */
$(document).on("keydown", "form", function(event) { 
	if (event.key == "Enter"){
		chkAuthenticate();
	}
    return event.key != "Enter";
});

$(document).on("keyup", "form", function(event) { 
	 userName = $("#Username").val();
	    sessionStorage.setItem("InitialUserName", userName);
//	    console.log(sessionStorage.getItem("InitialUserName"));
});

function chkAuthenticate(){
	
	showLoader();
	try {
		userName = $("#Username").val();
		if (!userName) {
			alert('Please enter Username');
		}
		if (userName) {
			var fd = new FormData();
			fd.append('username', userName);
			$.ajax({
				url : "rest/RSDLoginDataController/getLoginData",
				data : fd,
				type : 'POST',
				dataType : 'json',
				cache : false,
				processData : false,
				contentType : false,
				success : function(response) {
					if (response.CODE == 1) {
						dataList = response.isEtQUser;
						userDisplayName= response.userDisplayName;
						userID = response.userID;
						loginSec = response.loginSec;
						userRole = response.userRole;
						SaveData();
						authContext.login();
						hideLoader();
					} else {
						dataList = [];
						userID = [];
						userRole = [];
						hideLoader();
						alert(response.MESSAGE);
                        sessionStorage.setItem("InitialUserName", "");
					}
//					dataList = response.isEtQUser;
//					userID = response.userID;
//					loginSec = response.loginSec;
//					userRole = response.userRole;
//					if (dataList == "Success") {
//						SaveData();
//						authContext.login();
//					} else {
//						alert('Please enter valid UserName')
//					}
					// }
				},
				error : function(request, textStatus, errorThrown) {
					hideLoader();
					console.log(errorThrown);
				}
			});
		}
	} catch (e) {
//		alert('Error: ' + e);
		hideLoader();
		console.log(e);

	}
}
/*
 * This function will execute Login validation process
 * 
 */
$(function() {
	$('#rsdLogin').click(function() {
		chkAuthenticate();
	});
});

function SaveData() {
	var encrypted_userName = CryptoJS.AES.encrypt(userName, loginSec).toString();
	var encrypted_userID = CryptoJS.AES.encrypt(userID, loginSec).toString();
	var encrypted_userRole = CryptoJS.AES.encrypt(userRole, loginSec).toString();
	var encrypted_userDisplayName = CryptoJS.AES.encrypt(userDisplayName, loginSec).toString();
	
	sessionStorage.setItem('UserDisplayName', encrypted_userDisplayName);
	sessionStorage.setItem('Username', encrypted_userName);
	sessionStorage.setItem('userID', encrypted_userID);
	sessionStorage.setItem('userRole', encrypted_userRole);
	sessionStorage.setItem('loginSec', loginSec);
}