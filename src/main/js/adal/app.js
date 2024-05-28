//Local
//var clientId = 'ef7097cb-8b5f-4616-8da5-96e7b1cad6e7';
//var tenantId = 'fb6e8f10-9fbb-4dc7-a480-e56cb51bd661';

//Ketan Details
var clientId = 'd9a44915-1ff3-49c7-a47f-5e48aa3288a1';
var tenantId = 'e6d7ba17-4378-433f-96fa-a48c4590ca82';

//PRD
//var clientId = '9becb296-8560-4e3f-8ea2-a3c3b370546e';
//var tenantId = 'e6d7ba17-4378-433f-96fa-a48c4590ca82';

var authContext = new AuthenticationContext({
	clientId : clientId,
	tenantId : tenantId,
//	postLogoutRedirectUri :'https://10.160.113.176:8443/RSD/'
//	postLogoutRedirectUri :'https://rsd-td.azurewebsites.net/RSD/'
//	postLogoutRedirectUri :'https://rsd.olympus.com/RSD/'
	postLogoutRedirectUri :'https://rsd-dev.azurewebsites.net/RSD/'
//	postLogoutRedirectUri :	'http://localhost:8181/RSD/'
		
});

// Make an AJAX request to the Microsoft Graph API and print the response as
// JSON.
/*var getCurrentUser = function(access_token) {
	alert(access_token);
	var xhr = new XMLHttpRequest();
	xhr.open('GET', 'https://graph.microsoft.com/v1.0/me', true);
	xhr.setRequestHeader('Authorization', 'Bearer ' + access_token);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			sessionStorage.setItem("User_Info", xhr.responseText);
			alert(xhr.responseText);
		} else {
			// TODO: Do something with the error (or non-200 responses)
			document.getElementById('api_response').textContent = 'ERROR:\n\n'
					+ xhr.responseText;
		}
	};
	xhr.send();
}*/
if (authContext.isCallback(window.location.hash)) {
	authContext.handleWindowCallback();
	var err = authContext.getLoginError();
	if (err) {
		// TODO: Handle errors signing in and getting tokens
	/*	document.getElementById('api_response').textContent = 'ERROR:\n\n'+ err;*/
		console.log(err);
	}

} else {

	// If logged in, get access token and make an API request
	var user = authContext.getCachedUser();
	if (user) {
/*		sessionStorage.setItem("USER_NAME",user.userName);
		sessionStorage.setItem("USER_GIVEN_NAME",user.profile.given_name);
		sessionStorage.setItem("USER_FAMILY_NAME",user.profile.family_name);
*/		
		var cipher_pass=sessionStorage.getItem("loginSec");
		var encrypted_userRole = sessionStorage.getItem("userRole");
		var userRole = CryptoJS.AES.decrypt(encrypted_userRole, cipher_pass).toString(CryptoJS.enc.Utf8);
		if (null!=userRole) {
//			if ('Admin'==userRole) {
//				window.location.href = "/RSD/UserMaster";
//			}
//			if ('Uploader'==userRole)
//			{
//				window.location.href="/RSD/BusinessCenterPop-up";
//			}
//			if ('Approver'==userRole)
//			{
//				window.location.href="/RSD/BusinessCenterPop-up";
//			}
//			if ('Reader'==userRole)
//			{
//				window.location.href="/RSD/BusinessCenterPop-up";
//			}
			if(sessionStorage.getItem("InitialUserName").toLowerCase()==user.userName.toLowerCase())
            {
                if ('Admin'==userRole) {
                    window.location.href = "/RSD/UserMaster";
                }
                if ('Uploader'==userRole)
                {
                    window.location.href="/RSD/BusinessCenterPop-up";
                }
                if ('Approver'==userRole)
                {
                    window.location.href="/RSD/BusinessCenterPop-up";
                }
                if ('Reader'==userRole)
                {
                    window.location.href="/RSD/BusinessCenterPop-up";
                }
            }
            else
            {
                alert('Initial Username is not matached with AD login.');
                sessionStorage.setItem("InitialUserName", "");
              
                sessionStorage.clear();
                authContext.logOut();
            }
			
		}
		else{
			window.location.href = '/RSD';
		}
	} else {
		/*document.getElementById('username').textContent = 'Not signed in.';*/
	}
}