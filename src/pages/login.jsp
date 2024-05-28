
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login - ETQ Monitoring Application</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" type="image/png" href="../images/favicon-32x32.png" sizes="32x32" />
<link rel="icon" type="image/png" href="../images/favicon-16x16.png" sizes="16x16" />
<link href="../css/jquery-ui.css" rel="stylesheet">
<link href="../css/redmond-jquery-ui.css" rel="stylesheet">
<link href="../css/ui.jqgrid.css" rel="stylesheet">
<link href="../css/bootstrap.min.css" rel="stylesheet">
<link href="../css/bootstrap-datetimepicker.min.css" rel="stylesheet">
<link href="../css/font-awesome.css" rel="stylesheet">
<link href="../css/pixie-apt.css" rel="stylesheet">
<link href="../css/pixie-main.css" rel="stylesheet">
<script src="../js/pixie_js/respond.min.js" type="text/javascript"></script>
<script src="../js/pixie_js/modernizr-custom.js" type="text/javascript"></script>
<script src="../js/pixie_js/jquery-3.2.1.js" type="text/javascript"></script>
<script src="../js/pixie_js/popper.min.js" type="text/javascript"></script>
<script src="../js/pixie_js/jquery-ui.js" type="text/javascript"></script>
<script src="../js/pixie_js/jquery.jqGrid.min.js" type="text/javascript"></script>
<script src="../js/pixie_js/grid.locale-en.js" type="text/javascript"></script>
<script src="../js/pixie_js/bootstrap.min.js" type="text/javascript"></script>
<script src="../js/pixie_js/moment.min.js" type="text/javascript"></script>
<script src="../js/pixie_js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
<!-- <script type="text/javascript" src="../js/customJS/login.js"></script> -->
<script type="text/javascript" src="../js/pixie_js/aes.js"> </script>
<script src="../js/adal/adal.js" type="text/javascript"></script>
<script src="../js/adal/app.js" type="text/javascript"></script>
  
</head>
<body>
    <div class="wrapper">
     <div class="loader-box login-loader-box" style="display: none;">
		      <div class="spinner-border text-primary" role="status">
		     	<span class="sr-only">Loading...</span>
		     </div> 
		</div>
        <section class="login-section">
            <div class="login-form">
                <div class="logo-box">
                    <img src="/images/olympus-logo-white.png" alt="olympus-logo-white">
                    <h4 style="font-weight: bold">ETQ Monitoring Application</h4>
<!--                     <h4 style="font-weight: bold" id="instance_Name">Test</h4> -->
                     <h4 style="font-weight: bold" id="instance_Name">Development</h4>
                </div>
                <form action="/businessCenterPop-up" method="POST">
                    <div class="form-group">
                        <input type="text" class="form-control" id="Username" name="username" placeholder="Username">
                    </div>
                   <!--  <div class="form-group">
                        <input type="password" class="form-control" id="Password" name="password" placeholder="Password">
                    </div> -->
<!--                     <a class="btn common-btn" id="rsdLogin"  title="Sign In" href='javascript:void(0);'>Sign In</a>	  -->
                       <button style="font-weight: bold"  type="submit" class="btn common-btn" id="rsdLogin">Sign In</button>	<br></br>
                       <h6 style="font-weight: bold; text-align: center;" id="version">V 1</h6>			
                </form>
            </div>
        </section>
    </div>

</body>

</html>