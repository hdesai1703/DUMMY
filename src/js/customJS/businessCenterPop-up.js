/*This function is used to set loader*/
var userID ;
var BusinessList;
var userRole;
var userName;
var BcName;
var BcId;
var cipher_pass;

cipher_pass=sessionStorage.getItem("loginSec");
var encrypted_userID = sessionStorage.getItem("userID");
var encrypted_Username = sessionStorage.getItem("Username");
userID = CryptoJS.AES.decrypt(encrypted_userID, cipher_pass).toString(CryptoJS.enc.Utf8);
userName = CryptoJS.AES.decrypt(encrypted_Username, cipher_pass).toString(CryptoJS.enc.Utf8);
var encrypted_userRole = sessionStorage.getItem("userRole");
user_Role = CryptoJS.AES.decrypt(encrypted_userRole, cipher_pass).toString(CryptoJS.enc.Utf8);
if (user_Role =="Admin"){
	window.location.href = '/RSD/AuthorizationDenied';
}

function showLoader() {
	$(".loader-box").css('display', 'block');
}

function hideLoader() {
	$(".loader-box").css('display', 'none');
}

$(document).ready(function() {
	showLoader();
//		$(document).on("keyup", function (event) {
//		if (event.which == 13) {
//        	 $("#clicking").trigger('click');
//        }
//    });

	getBusinessCenterData();
});

$(document).on("keydown", "form", function(event) { 
	if (event.key == "Enter"){
		saveBusinessCenter()
	}
    return event.key != "Enter";
});

/*This function helps to get business center lov data*/
function getBusinessCenterData(){
	var fd = new FormData();
	fd.append('userID',userID);
	/* This post call used to get Business Center Details based on User for LOV */
	$.ajax({
        url : "../RSD/rest/BusinessCenterDataController/getBusinessCenterLovDetail?m=" + Math.random(),
        data : fd,
        type : 'POST',
        dataType : 'json',
        enctype : "multipart/form-data",
        processData : false,
        contentType : false,
        cache : false,
        success : function(response) {
        	if (response.CODE == 1) {
    			BusinessList = response.Data;			
    			$.each(BusinessList, function(index, value) {
    				 $('#exampleFormControlSelect1').append('<option value="' + value.BUSINESS_CENTER_ID_PK + '">' + value.BUSINESS_CENTER_NAME + '</option>');
               });
    			$('#exampleModalCenter').modal('show');
    			hideLoader();	
    		} else {
    			BusinessList=[];
    			alert(response.MESSAGE);
    			hideLoader();
    		}
        },
        error : function(request, textStatus, errorThrown) {
            hideLoader();
            console.log(errorThrown);
        }
    });

}

/**
 * @param BcId
 * @returns
 * 
 * This function use to get Business Center ID related to User name and based on that get User Role
 */
function getUserRolebasedOnBC(BcId,BcName){	
	showLoader();
	var fd = new FormData();
	fd.append('username',userName);
	fd.append('BcId',BcId);
	$.ajax({
		url : "rest/RSDLoginDataController/getUserRolebasedOnBC",
		type : 'POST',
		data:fd,
		dataType : 'json',
		cache : false,
		processData : false,
	    contentType : false,
		success : function(response) {
			if (response.CODE == 1) {
				var userRoleBC = response.userRole;
				var encrypted_userRoleBC = CryptoJS.AES.encrypt(userRoleBC, cipher_pass).toString();
				sessionStorage.setItem('userRole', encrypted_userRoleBC);
				var encrypted_userRole1 = sessionStorage.getItem("userRole");
				userRole = CryptoJS.AES.decrypt(encrypted_userRole1, cipher_pass).toString(CryptoJS.enc.Utf8);
				var encrypted_BcName = CryptoJS.AES.encrypt(BcName, cipher_pass).toString();
				var encrypted_BcId = CryptoJS.AES.encrypt(BcId, cipher_pass).toString();

				sessionStorage.setItem('BcName', encrypted_BcName);
				sessionStorage.setItem('BcId', encrypted_BcId);
				hideLoader();
				if (userRole=="Reader"){
					
					window.location.href = '/RSD/BatchLog'
				}
				else if (userRole=="Admin"){
					
					window.location.href = '/RSD/UserMaster'	
				}
				else if (userRole=="Uploader"){
					
					window.location.href = '/RSD/BatchLog'
				}
				else if (userRole=="Approver"){
					
					window.location.href = '/RSD/Notification'
				}
				if (null==userRole ||null==userName){
					
					window.location.href = '/RSD'
				}
				
			} else {				
				userRole = [];
				hideLoader();
			}
		}
	});
		
}

function saveBusinessCenter(){
	
	var idx_bc = document.getElementById("exampleFormControlSelect1");
	var bc_code = idx_bc.options[idx_bc.selectedIndex].value;
	
	if(bc_code == "0"){
	    alert("Select Business Center");
	    return false;
	}
	else{
		var BcName=$("#exampleFormControlSelect1 option:selected" ).text();
		BcId=$("#exampleFormControlSelect1" ).val();
		getUserRolebasedOnBC(BcId,BcName);	
		return true;
	}
}
//on save button click.
$(function() {	
	$('#savebtn').click(function() {
		saveBusinessCenter();
		
	});
});