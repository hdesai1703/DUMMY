<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!DOCTYPE html>

<html>

<head>
    <script type="text/javascript" src="../js/adal/adal.js"></script>
    <script type="text/javascript" src="../js/pixie_js/aes.js"> </script>
    <script type="text/javascript" src="../js/customJS/menu.js"> </script>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/png" href="../images/favicon-32x32.png" sizes="32x32" />
    <link rel="icon" type="image/png" href="../images/favicon-16x16.png" sizes="16x16" />



</head>
<style>
    #register .short {

        color: #FF0000;
        font-size: small;
    }

    #register .weak {

        color: orange;
        font-size: small;
    }

    #register .good {

        color: #2D98F3;
        font-size: small;
    }

    #register .strong {

        color: limegreen;
        font-size: small;
    }
</style>

<body>

    <div class="wrapper">
        <header>
            <div class="custom-container clearfix">
                <div class="left-box">
                    <div class="logo">
                        <img src="../images/olympus-logo-white.png" alt="olympus-logo-white"
                            style="background-color: whitesmoke; padding: 5px;">
                    </div>
                    <div class="menu-box"><a href="javascript:void(0);" class="menu-btn"><img
                                src="../images/menu-white.png" alt="menu-white">
                        </a></div>
                </div>
                <div class="center-title-box">
                    <h4>ETQ Monitoring Application</h4>
                </div>
                <ul class="nav navbar-nav navbar-right right-box">
                    <div class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <img src="../images/user-profile.png" alt="user-profile"><span id="logedInName"></span></a>
                        <div class="dropdown-menu">
                            <!--                            <li><a href="javascript:void(0);" title="System Settings"><i class="pixie-apt-setting"></i><span>System Settings</span></a></li>
                            -->
                            <li><a href="javascript:void(0);" title="Change Password" data-toggle="modal"
                                    data-target="#password-configuration-modal"><i
                                        class="pixie-apt-pass"></i><span>Change Password</span></a></li>
                            <li><a id="RespBc" href="/user/businessCenterPop-up" title="Logout"><i
                                        class="rsd-business-center"></i><span>Change BC</span></a></li>
                            <li><a href="/user/logout"><i class="pixie-apt-logout"
                                        onclick="doAADLogOut()"></i><span>Logout</span></a></li>
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
                                <li><a id="RespBc" href="javascript:changebc()" title="Logout"><i
                                            class="rsd-business-center"></i><span>Change BC</span></a></li>
                                <li><a href="javascript:doAADLogOut()" title="Logout"><i
                                            class="pixie-apt-logout"></i><span>Logout</span></a></li>
                            </div>
                        </div>
                    </ul>
                </div>
                <div class="bottom-box">
                    <div class="menu-box"><a href="javascript:void(0);" class="menu-btn"><img src="../images/menu.png"
                                alt="menu">
                        </a></div>
                    <div class="center-title-box">
                        <h4>ETQ MONITORING APPLICATION</h4>
                    </div>
                </div>
            </div>
        </header>
        <div class="left-side-menu">
            <div class="menu-box">
                <ul id="mMenuUl" style="display:none;">
                    <li><a id="mUser" href="/oekg/user/user" title="user" class="menu"><i
                                class="rsd-user-master"></i><span>User</span></a></li>
                    <li><a id="mMapping" href="/oekg/mapping/mapping" title="Mapping" class="menu"><i
                                class="rsd-notification"></i><span>User Role Mapping</span></a>
                    </li>
                    <li><a id="mEtqmonitor" href="/oekg/etqMonitor/etqMonitor" title="Etqmonitor" class="menu"><i
                                class="rsd-db-configuration"></i><span>ETQ Monitoring</span></a></li>
                    <li><a id="mScheduler" href="/oekg/scheduler/scheduler" title="Scheduler"><i
                                class="rsd-group-master" class="menu"></i><span>Scheduler</span></a></li>
                    <li><a id="mBatchlog" href="/oekg/batchlog/batchLog" title="Batchlog" class="menu"><i
                                class="rsd-file-upload"></i><span>Batchlog</span></a></li>

                </ul>
            </div>
        </div>

    </div>
    <div id="password-configuration-modal" class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog"
        data-backdrop="static" data-keyboard="false" aria-labelledby="myLargeModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg " style="max-width: 40%;margin-top: 11rem;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Change Password</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form data-toggle="validator" id="register"
                        oninput='CONFIRM_PASSWORD.setCustomValidity(CONFIRM_PASSWORD.value != NEW_PASSWORD.value ? "The Password does not match" : "")'>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group row">
                                    <label class="col-sm col-form-label" style="max-width: 160px;">Old Password
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <input type="password" class="form-control form-control-sm " id="PASSWORD"
                                            name="PASSWORD" required>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group row">
                                    <label class="col-sm col-form-label" style="max-width: 160px;">New Password
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <input type="password" class="form-control form-control-sm " id="NEW_PASSWORD"
                                            name="NEW_PASSWORD" required>
                                        <span id="result"></span>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group row">
                                    <label class="col-sm col-form-label" style="max-width: 160px;">Confirm Password
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <input type="password" class="form-control form-control-sm "
                                            id="CONFIRM_PASSWORD" name="CONFIRM_PASSWORD" required>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group row" style="justify-content: center;">
                         <span id="required" style="font-size: small ; color: #FF0000; "></span>
                        </div>

                        <div class="form-group row" style="justify-content: center;">
                            <button type="button" class="btn common-btn" id="addBtnEdit"
                                onclick="changepassword()">Save</button> &nbsp; &nbsp;
                            <button type="reset" class="btn common-btn" id="addBtnClear">Clear</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade bd-example-modal-lg" id="passwordfailed" tabindex="-1" role="dialog"
        aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="static"
        style="background:rgba(0, 0, 0, 0.5) !important ;">
        <div class="modal-dialog " style="max-width: 35%; margin-top: 15rem;">
            <div class="modal-content">
                <div class="modal-header pt-2 pb-2">
                    <h5 class="modal-title" id="exampleModalLabel">Alert</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form data-toggle="validator">
                        <p style="font-size:15px;">Sorry!! your Current password was wrong please try again </p>
                        <div class="form-group row mr-5 mt-1 mb-0" style="float: right">
                            <button type="button" id="addBtnClear" class="btn common-btn" data-dismiss="modal"
                                style="float: right ;">Ok</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade bd-example-modal-lg" id="passwordsuccess" tabindex="-1" role="dialog"
        aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="static"
        style="background:rgba(0, 0, 0, 0.5) !important ;">
        <div class="modal-dialog " style="max-width: 35%; margin-top: 15rem;">
            <div class="modal-content">
                <div class="modal-header pt-2 pb-2">
                    <h5 class="modal-title" id="exampleModalLabel">Alert</h5>
                    <!-- <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button> -->
                </div>
                <div class="modal-body">
                    <form data-toggle="validator">
                        <p style="font-size:15px;">Your password was changed successfully Please click here to <a
                                href="/user/logout"><span>Logout!!</span></a></p>
                        <div class="form-group row mr-5 mt-1 mb-0" style="float: right">
                            <!-- <a href="/user/logout"><i class="pixie-apt-logout"
                                onclick="doAADLogOut()"></i><span>Logout</span></a>
                            <a href="/user/user/logout" class="btn common-btn" role="button" data-dismiss="modal">Logout</a> -->
                            <!-- <button type="submit" id="addBtnClear" class="btn common-btn" data-dismiss="modal"
                                style="float: right ;">Logout</button> -->
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</body>

</html>