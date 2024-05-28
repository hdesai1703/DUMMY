<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="../js/adal/adal.js"></script>
<script type="text/javascript" src="../js/pixie_js/aes.js"> </script>
<!-- <script type="text/javascript" src="../js/customJS/menu.js"> </script> -->
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" type="image/png" href="../images/favicon-32x32.png" sizes="32x32" />
<link rel="icon" type="image/png" href="../images/favicon-16x16.png" sizes="16x16" />

</head>
<body>

 <div class="wrapper">
 		 	<header>
            <div class="custom-container clearfix">
                <div class="left-box">
                    <div class="logo">
                        <img src="/images/olympus-logo-white.png" alt="olympus-logo-white">
                    </div>
                    <div class="menu-box"><a href="javascript:void(0);" class="menu-btn"><img src="/images/menu-white.png" alt="menu-white">
                        </a></div>
                </div>
                <div class="center-title-box">
                    <h4>ETQ Monitoring Application</h4>
                </div>
                <ul class="nav navbar-nav navbar-right right-box">
                    <div class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <img src="/images/user-profile.png" alt="user-profile"><span id="logedInName"></span></a>
                        <div class="dropdown-menu">
<!--                            <li><a href="javascript:void(0);" title="System Settings"><i class="pixie-apt-setting"></i><span>System Settings</span></a></li>
                           <li><a href="javascript:void(0);" title="Change Password"><i class="pixie-apt-pass"></i><span>Change Password</span></a></li> -->
                           <li><a  id="RespBc" href="/BusinessCenterPop-up" title="Logout"><i class="rsd-business-center"></i><span>Change BC</span></a></li>
                           <li><a href="javascript:doAADLogOut()"><i class="pixie-apt-logout"></i><span>Logout</span></a></li>
                        </div>
                    </div>
                </ul>
            </div>
            <div class="mobile-header">
                <div class="top-box">
                    <div class="logo">
                        <img src="../images/olympus-logo-white.png" alt="olympus-logo-white">
                    </div>
                    <ul class="nav navbar-nav navbar-right right-box">
                        <div class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <img src="../images/user-profile.png" alt="user-profile"><span id="logedInName"></span>
                            </a>
                            <div class="dropdown-menu">
<!--                                 <li><a href="javascript:void(0);" title="System Settings"><i class="pixie-apt-setting"></i><span>System Settings</span></a></li>
                                <li><a href="javascript:void(0);" title="Change Password"><i class="pixie-apt-pass"></i><span>Change Password</span></a></li> -->
                                <li><a  id="RespBc" href="/BusinessCenterPop-up" title="Logout"><i class="rsd-business-center"></i><span>Change BC</span></a></li>
                                <li><a href="javascript:doAADLogOut()" title="Logout"><i class="pixie-apt-logout"></i><span>Logout</span></a></li>
                            </div>
                        </div>
                    </ul>
                </div>
                <div class="bottom-box">
                    <div class="menu-box"><a href="javascript:void(0);" class="menu-btn"><img src="../images/menu.png" alt="menu">
                        </a></div>
                    <div class="center-title-box">
                        <h4>ETQ MOnitoring Application</h4>
                    </div>
                </div>
            </div>
        </header>
 </div>       
</body>
<script>
    $(document).ready(function() {
        document.getElementById("logedInName").innerHTML=sessionStorage.getItem('USERNAME');
  });
</script>
</html>