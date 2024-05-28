var Username;
var userRole;
var userID;
var UserDisplayName;

$(document).ready(function () {
	$(".menu-btn").click(function () {
		$(this).toggleClass("active");
		$('body').toggleClass('menu-box-open');
	});
	for (var i = 0; i < document.links.length; i++) {
		if (document.URL.includes(document.links[i].href) ) {
			document.links[i].className = " active";
		}
	}
});

$(function () {
	$("[rel='tooltip']").tooltip();
})

jQuery(document).ready(function ($) {
	$('#mMenuUl').hide();
	$('#mUser').hide();
	$('#mMapping').hide();
	$('#mEtqmonitor').hide();
	$('#mScheduler').hide();
	$('#mBatchlog').hide();

	document.getElementById("logedInName").innerHTML = CryptoJS.AES.decrypt(sessionStorage.getItem('USERNAME'), "EMA").toString(CryptoJS.enc.Utf8);
	userRole = CryptoJS.AES.decrypt(sessionStorage.getItem('ROLE'), "EMA").toString(CryptoJS.enc.Utf8);
	console.log(userRole);

	if (userRole == 'Admin') {
		$('#mMenuUl').show();
		$('#mUser').show();
		$('#mMapping').show();
		$('#mEtqmonitor').show();
		$('#mScheduler').show();
		$('#mBatchlog').show();
	}
	if (userRole == 'Reader') {
		$('#mMenuUl').show();
		$('#mUser').hide();
		$('#mMapping').hide();
		$('#mEtqmonitor').show();
		$('#mScheduler').hide();
		$('#mBatchlog').show();
	}
	document.addEventListener("click", function (event) {
		if (event.target.id !== "NEW_PASSWORD") {
			document.getElementById('result').innerHTML = "";
			$('#result').removeClass();

		}
	});
	$('#NEW_PASSWORD').keyup(function () {
		document.getElementById("required").innerHTML = ""
	})
	$('#PASSWORD').keyup(function () {
		document.getElementById("required").innerHTML = ""
	})
	$('#CONFIRM_PASSWORD').keyup(function () {
		document.getElementById("required").innerHTML = ""
	})
	$('#NEW_PASSWORD').keyup(function () {
		$('#result').html(checkStrength($('#NEW_PASSWORD').val()))
	})
	

	/*
	 * cipher_pass=sessionStorage.getItem("loginSec"); var encrypted_userName =
	 * sessionStorage.getItem("Username"); var encrypted_userDisplayName =
	 * sessionStorage.getItem("UserDisplayName"); var encrypted_userID =
	 * sessionStorage.getItem("userID"); if (null==encrypted_userName) {
	 * window.location.href = '/RSD'; }
	 * 
	 * Username = CryptoJS.AES.decrypt(encrypted_userName,
	 * cipher_pass).toString(CryptoJS.enc.Utf8); UserDisplayName =
	 * CryptoJS.AES.decrypt(encrypted_userDisplayName,
	 * cipher_pass).toString(CryptoJS.enc.Utf8); userID =
	 * CryptoJS.AES.decrypt(encrypted_userID,
	 * cipher_pass).toString(CryptoJS.enc.Utf8);
	 * document.getElementById("logedInName").textContent = UserDisplayName;
	 * 
	 * var encrypted_userRole = sessionStorage.getItem("userRole"); userRole =
	 * CryptoJS.AES.decrypt(encrypted_userRole,
	 * cipher_pass).toString(CryptoJS.enc.Utf8); //Admin if (userRole=='Admin'){
	 * $('#mUser').show(); $('#mGroupDetail').show();
	 * $('#mBusinessCenter').show(); $('#mUserMapping').show();
	 * $('#mGroupMapping').show(); $('#mConfiguration').show();
	 * $('#mAppConfig').show(); $('#mValidationType').show(); } //Uploader if
	 * (userRole=='Uploader'){ $('#mBLog').show(); $('#mUploadData').show();
	 * $('#RespBc').show(); } //Approver else if (userRole=='Approver'){
	 * $('#mNotification').show(); $('#mBLog').show(); $('#mUploadData').show();
	 * $('#RespBc').show(); } //Reader else if (userRole=='Reader'){
	 * $('#mBLog').show(); $('#RespBc').show(); }
	 * 
	 * $('#mMenuUl').show();
	 */
});

var authContext = new AuthenticationContext({
	// clientId : 'ef7097cb-8b5f-4616-8da5-96e7b1cad6e7',
	clientId: 'd9a44915-1ff3-49c7-a47f-5e48aa3288a1',// KP
	// clientId : '9becb296-8560-4e3f-8ea2-a3c3b370546e',//PRD
	// postLogoutRedirectUri : 'https://10.160.113.176:8443/RSD/'
	// postLogoutRedirectUri :'https://rsd-td.azurewebsites.net/RSD/'
	// postLogoutRedirectUri :'https://rsd.olympus.com/RSD/'
	postLogoutRedirectUri: 'https://rsd-dev.azurewebsites.net/RSD/'
	// postLogoutRedirectUri : 'http://localhost:8181/RSD/'
});
function doAADLogOut() {
	console.log("inside logout bc")
	// authContext.logOut();

}

function changebc() {
	console.log("inside change bc")
	URL = "/oekg/user/changeBC";
	var BC_W = sessionStorage.getItem("BUSINESS_UNIT");
	var ROLE_W = sessionStorage.getItem("ROLE");
	$.post(URL, {
		BUSINESS_UNIT_SITE_ID: BC_W,
		ROLE: ROLE_W
	}, function (response) {

	}

	);
}
function changepassword() {

	if ($('#PASSWORD').val() != "" & $('#NEW_PASSWORD').val() != "" & $('#CONFIRM_PASSWORD').val != "") {
		if ($('#NEW_PASSWORD').val() == $('#CONFIRM_PASSWORD').val()) {
			console.log("inside chnage password")
			var URL = "/oekg/user/changepassword";

			var EMAIL_ADDRESS_W = CryptoJS.AES.decrypt(sessionStorage.getItem('EMAILADDRESS'), "EMA").toString(CryptoJS.enc.Utf8);
			var PASSWORD_W = $('#PASSWORD').val();
			var NEW_PASSWORD_W = $('#NEW_PASSWORD').val();
			var CONFIRM_PASSWORD_W = $('#CONFIRM_PASSWORD').val();
			//IS ACTIVE


			//Calling REST API
			$.post(URL, {
				EMAIL_ADDRESS: EMAIL_ADDRESS_W,
				PASSWORD: PASSWORD_W,
				NEW_PASSWORD: NEW_PASSWORD_W
			},
				function (response) {
					dataList = response.MSG;
					if (dataList == "currentpassword was wrong") {
						$("#passwordfailed").modal('show');
					}
					if (dataList == "Password was changed successfully") {
						$("#passwordsuccess").modal('show');
					}



				}

			);
		}
	}
	else {

		document.getElementById("required").innerHTML = "Please provide the required fields"
	}
}
function checkStrength(password) {
	var strength = 0
	if (password.length == 0) {
		$('#result').removeClass()
		return ''
	}
	if (password.length < 4) {
		$('#result').removeClass()
		$('#result').addClass('short')
		return 'Too short'
	}
	if (password.length > 5) strength += 1
	// If password contains both lower and uppercase characters, increase strength value.
	if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/)) strength += 1
	// If it has numbers and characters, increase strength value.
	if (password.match(/([a-zA-Z])/) && password.match(/([0-9])/)) strength += 1
	// If it has one special character, increase strength value.
	if (password.match(/([!,%,&,@,#,$,^,*,?,_,~])/)) strength += 1
	// If it has two special characters, increase strength value.
	if (password.match(/(.*[!,%,&,@,#,$,^,*,?,_,~].*[!,%,&,@,#,$,^,*,?,_,~])/)) strength += 1
	// Calculated strength value, we can return messages
	// If value is less than 2
	if (strength < 2) {
		$('#result').removeClass()
		$('#result').addClass('weak')
		return 'Weak'
	} else if (strength == 2) {
		$('#result').removeClass()
		$('#result').addClass('good')
		return 'Good'
	} else {
		$('#result').removeClass()
		$('#result').addClass('strong')
		return 'Strong'
	}

}
