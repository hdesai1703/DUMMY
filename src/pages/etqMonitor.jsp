<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
    <!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
    <!DOCTYPE html>
    <html>

    <head>
        <title>EMA-ETQ Monitor</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="shortcut icon" href="#" />
        <link rel="icon" type="image/png" href="../images/favicon-32x32.png" sizes="32x32" />
        <link rel="icon" type="image/png" href="../images/favicon-16x16.png" sizes="16x16" />

        <link rel="stylesheet" href="../css/jquery-ui.css">
        <link rel="stylesheet" href="../css/redmond-jquery-ui.css">
        <link rel="stylesheet" href="../css/ui.jqgrid.css">
        <link rel="stylesheet" href="../css/bootstrap.min.css">
        <link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" href="../css/bootstrap-multiselect.css">
        <link rel="stylesheet" href="../css/easy-responsive-tabs.css">
        <link rel="stylesheet" href="../css/font-awesome.css">
        <link rel="stylesheet" href="../css/pixie-apt.css">
        <link rel="stylesheet" href="../css/pixie-main.css">
        <link rel="stylesheet" href="../css/pixie-media.css">

        <script type="text/javascript" src="../js/pixie_js/respond.min.js"></script>
        <script type="text/javascript" src="../js/pixie_js/modernizr-custom.js"></script>
        <script type="text/javascript" src="../js/pixie_js/jquery-3.2.1.js"></script>
        <script type="text/javascript" src="../js/pixie_js/popper.min.js"></script>
        <script type="text/javascript" src="../js/pixie_js/easyResponsiveTabs.js"></script>
        <script type="text/javascript" src="../js/pixie_js/jquery-ui.js"></script>
        <script type="text/javascript" src="../js/pixie_js/jquery.jqGrid.min.js"></script>
        <script type="text/javascript" src="../js/pixie_js/grid.locale-en.js"></script>
        <script type="text/javascript" src="../js/pixie_js/bootstrap.min.js"></script>
        <script type="text/javascript" src="../js/pixie_js/moment.min.js"></script>
        <script type="text/javascript" src="../js/pixie_js/bootstrap-datetimepicker.min.js"></script>
        <script type="text/javascript" src="../js/pixie_js/bootstrap-multiselect.js"></script>
        <script type="text/javascript" src="../js/pixie_js/aes.js">
        </script>
        <script type="text/javascript" src="../js/pixie_js/bootbox.js"></script>
        <script type="text/javascript" src="../js/customJS/etqmonitor.js"></script>
        <script
            src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>



        <style>
            .ui-jqgrid .ui-jqgrid-labels th.ui-th-column {
                background-color: #0F1F8A;
                color: #fff;
                font-size: 11px;
            }

            .has-error input,
            .has-error textarea {
                border-color: #dc3545;
            }

            .has-error .form-control.form-control-sm {
                border: 1px solid red;
            }

            .has-error .selectpicker.form-control {
                border: 1px solid red;
            }

            .has-error .form-control {
                border: 1px solid red;
            }

            .has-error .form-control.form-control-sm.common-multiselect {
                border: 1px solid red;
            }

            .has-error li {
                font-size: 12px;
                padding: 4px 0 0;
                color: red;
            }
        </style>
    </head>

    <body>
        <div class="wrapper">
            <jsp:include page="/pages/menu.jsp" />
            <!-- <div class="loader-box">
                <div class="spinner-border text-primary" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </div> -->
            <div class="content-page">
                <div class="content">
                    <div class="container-fluid">
                        <div class="top-box clearfix">
                            <div class="page-main-title">
                                <h5>Business Center : <b><span id="bcTitle"></span></b></h5>
                            </div>
                        </div>
                        <div class="whole-tab">
                            <ul class="resp-tabs-list hor_1 clearfix">
                                <li id="COMPLAINT" onclick="getCompData()">Complaint</li>
                                <li id="CUSTOMER" onclick="getCusData()">Customer</li>
                                <li id="ADDITIONAL_REFERENCE" onclick="getAddRefData()">Additional Reference</li>

                            </ul>
                            <div class="resp-tabs-container hor_1">
                                <div class="box-1">
                                    <div class="file-upload-box">
                                        <!-- <div class="inner-title">
                                            <h4>Search By Product</h4>
                                        </div> -->

                                        <form id="COMP_FORM" name="COMP_MAIN_FORM">
                                            <div class="row ">
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="inputPassword" class="col-sm-5 col-form-label"
                                                            style="padding-left: 3px;">Reference Number
                                                        </label>
                                                        <div class="col-sm-7">
                                                            <input type="text" class="form-control form-control-sm"
                                                                id="COM_REFERENCE_NUMBER" name="REFERENCE_NUMBER">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label class="col-sm-5 col-form-label"
                                                            style="padding-left: 3px;">Upload status
                                                        </label>
                                                        <div class="col-sm-7">
                                                            <select class="form-control form-control-sm"
                                                                id="COM_UPLOAD_STATUS" name="ETQ_UPLOAD_STATUS"
                                                                style="height: 31px; font-size: small;">

                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="rsid" class="col-sm-4 col-form-label"
                                                            style="padding-left: 3px;">Debug
                                                            level
                                                            <span style="color: red;">&#42</span>
                                                        </label>
                                                        <div class="col-sm-7">
                                                            <select class="form-control form-control-sm"
                                                                id="COM_DEBUG_LEVEL" name="DEBUG_LEVEL"
                                                                style="height: 31px; font-size: small;" disabled>
                                                                <option value="1" id="1">1</option>
                                                                <option value="2" id="2">2</option>
                                                            </select>
                                                            <div class="help-block with-errors"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="enddate" class="col-sm-5 col-form-label"
                                                            style="padding-left: 3px;">Request
                                                            Date From</label>
                                                        <div class="col-sm-7">
                                                            <div class='input-group date' id='COM_REQUEST_DATE_FROM_D'>
                                                                <input type='text' class="form-control form-control-sm"
                                                                    id="COM_REQUEST_DATE_FROM"
                                                                    name="REQUEST_DATE_FROM" />
                                                                <span class="input-group-addon">
                                                                    <span class="fa fa-calendar"></span>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="enddate" class="col-sm-5 col-form-label"
                                                            style="padding-left: 3px;">Request
                                                            Date To</label>
                                                        <div class="col-sm-7">
                                                            <div class='input-group date' id='COM_REQUEST_DATE_TO_D'>
                                                                <input type='text' class="form-control form-control-sm"
                                                                    id="COM_REQUEST_DATE_TO" name="REQUEST_DATE_TO" />
                                                                <span class="input-group-addon">
                                                                    <span class="fa fa-calendar"></span>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="inputPassword" class="col-sm-4 col-form-label"
                                                            style="padding-left: 3px;">Batch </label>
                                                        <div class="col-sm-7">
                                                            <input type="text" class="form-control form-control-sm"
                                                                id="COM_BATCH" name="BATCH_ID">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row pb-2 mb-0">
                                                <div class="col-sm-4 pb-0 mb-0">
                                                    <div class="form-group row pb-0 mb-0">
                                                        <div class="form-group mb-0 pb-0" id="COM_configButton">
                                                            <button type="reset" class="btn common-btn" id="btnClear"
                                                                style="margin-bottom: 0px;">Clear</button>
                                                            <button type="button" class="btn common-btn"
                                                                id="searchConfig" style="margin-bottom: 0px;"
                                                                onclick="searchgetData()">Search</button>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4 pb-0 mb-0">
                                                    <div class="form-group row pb-0 mb-0">
                                                        <label for="rsid" class="col-sm-5 pb-0 mb-0 col-form-label"
                                                            style="padding-left: 3px; padding-bottom: 0px;">External
                                                            Actions
                                                        </label>
                                                        <div class="col-sm-7 pb-0 mb-0">
                                                            <select class="form-control form-control-sm"
                                                                id="EXTERNAL_ACTION" name="EXTERNAL_ACTION"
                                                                style="height: 31px; font-size: small;">
                                                                <option value="" id="" style="font-size: 12px;">
                                                                    Select Action
                                                                </option>
                                                                <option value="GENERATE_REPORT" id="COM_GENERATE_REPORT"
                                                                    style="font-size: 12px;">
                                                                    Generate
                                                                    report</option>
                                                                <option value="UPDATE_ERROR_LOG"
                                                                    id="COM_UPDATE_ERROR_LOG" style="font-size: 12px;">
                                                                    Update
                                                                    Error Log</option>
                                                                <!-- <option value="REVIEW_DUPLICATE_RECORDS"
                                                                    id="COM_REVIEW_DUPLICATE_RECORDS" style="font-size: 12px;">Review Duplicate
                                                                    Records
                                                                </option> -->
                                                                <!-- <option value="UPLOAD_COMPLAINTS"
                                                                    id="COM_UPLOAD_COMPLAINTS" style="font-size: 12px;">
                                                                    Upload
                                                                    Complaint</option> -->
                                                            </select>
                                                            <div class="help-block with-errors"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-2 pb-0">
                                                    <div class="form-group mb-0 pb-0" id="ACTION_BUTTON_COMPLAINT">
                                                        <button type="button" class="btn common-btn"
                                                            style="margin-bottom: 0px;" id="btnSubmit"
                                                            onclick="externalActions()">Submit</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                        <!-- <form action="/excelview" method="GET">
                                            <button type="submit">Excel View</button>
                                        </form>     -->

                                    </div>
                                    <div class="common-jqGrid " style="margin-top: 13px;">
                                        <!-- <div class="add-btn-box">
                                            <a href="javascript:void(0);" id="reProcessPMS" rel="tooltip"
                                                data-placement="left" title="Re Process">
                                                <i class="fa fa-repeat" aria-hidden="true"></i>
                                            </a>
                                        </div> -->
                                        <table id="compjqGrid"></table>
                                        <div id="compjqGridPager"></div>
                                    </div>
                                </div>


                                <!-- <div class="common-jqGrid">
                                        <div class="add-btn-box">
                                            <a href="javascript:void(0);" id="reProcessRSS" rel="tooltip"
                                                data-placement="left" title="Re Process">
                                                <i class="fa fa-repeat" aria-hidden="true"></i>
                                            </a>
                                        </div>
                                        <table id="rssjqGrid"></table>
                                        <div id="jqGridPagerRss"></div>
                                    </div> -->

                                <div class="box-2">
                                    <div class="file-upload-box">
                                        <!-- <div class="inner-title">
                                            <h4>Search By Product</h4>
                                        </div> -->

                                        <form action="">
                                            <div class="row">
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="inputPassword" class="col-sm-5 col-form-label"
                                                            style="padding-left: 3px;">Customer A/C No
                                                        </label>
                                                        <div class="col-sm-7">
                                                            <input type="text" class="form-control form-control-sm"
                                                                id="CUS_COMPLAINT_ACCOUNT_NUMBER"
                                                                name="REFERENCE_NUMBER">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class=" col-sm-4">
                                                    <div class="form-group row">
                                                        <label class="col-sm-5 col-form-label"
                                                            style="padding-left: 3px;">Upload status
                                                        </label>
                                                        <div class="col-sm-7">
                                                            <select class="form-control form-control-sm"
                                                                id="CUS_UPLOAD_STATUS" name="ETQ_UPLOAD_STATUS"
                                                                style="height: 31px; font-size: small;">

                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="rsid" class="col-sm-4 col-form-label"
                                                            style="padding-left: 3px;">Debug
                                                            level
                                                            <span style="color: red;">&#42</span>
                                                        </label>
                                                        <div class="col-sm-7">
                                                            <select class="form-control form-control-sm"
                                                                id="CUS_DEBUG_LEVEL" name="DEBUG_LEVEL"
                                                                style="height: 31px; font-size: small;" disabled>
                                                                <option value="1" id="1">1</option>
                                                                <option value="2" id="2">2</option>
                                                            </select>
                                                            <div class="help-block with-errors"></div>
                                                        </div>
                                                        <!-- <label for="rsid" class="col-form-label">Request for
                                                            <span style="color: red;">&#42</span>
                                                        </label>
                                                        <div class="col-sm-7">
                                                            <select class="form-control form-control-sm"
                                                                id="REQUEST_FOR" name="REQUEST_FOR"
                                                                style="height: 31px; font-size: small;" disabled>
                                                                <option value="" id="">LATEST</option>
                                                            </select>
                                                            <div class="help-block with-errors"></div>
                                                        </div> -->

                                                    </div>
                                                </div>


                                            </div>
                                            <div class="row">
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="enddate" class="col-sm-5 col-form-label"
                                                            style="padding-left: 3px;">Request
                                                            Date From</label>
                                                        <div class="col-sm-7">
                                                            <div class='input-group date' id='CUS_REQUEST_DATE_FROM_D'>
                                                                <input type='text' class="form-control form-control-sm"
                                                                    id="CUS_REQUEST_DATE_FROM"
                                                                    name="REQUEST_DATE_FROM" />
                                                                <span class="input-group-addon">
                                                                    <span class="fa fa-calendar"></span>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="enddate" class="col-sm-5 col-form-label"
                                                            style="padding-left: 3px;">Request
                                                            Date To</label>
                                                        <div class="col-sm-7">
                                                            <div class='input-group date' id='CUS_REQUEST_DATE_TO_D'>
                                                                <input type='text' class="form-control form-control-sm"
                                                                    id="CUS_REQUEST_DATE_TO" name="REQUEST_DATE_TO" />
                                                                <span class="input-group-addon">
                                                                    <span class="fa fa-calendar"></span>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="inputPassword" class="col-sm-4 col-form-label"
                                                            style="padding-left: 3px;">Batch </label>
                                                        <div class="col-sm-7">
                                                            <input type="text" class="form-control form-control-sm"
                                                                id="CUS_BATCH" name="BATCH_ID">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row pb-2 mb-0">
                                                <div class="col-sm-4 pb-0 mb-0">
                                                    <div class="form-group row pb-0 mb-0">
                                                        <div class="form-group mb-0 pb-0" id="CUS_configButton">
                                                            <button type="reset" class="btn common-btn" id="btnClear"
                                                                style="margin-bottom: 0px;">Clear</button>
                                                            <button type="button" class="btn common-btn"
                                                                id="searchConfig" style="margin-bottom: 0px;"
                                                                onclick="searchgetCusData()">Search</button>
                                                        </div>
                                                    </div>
                                                </div>


                                            </div>

                                            <!-- <div class="col-md-4 mt-1 ml-2">
                                                        <div class="form-group" id="configButton">
                                                            <button type="reset" class="btn common-btn"
                                                                id="btnClear">Clear</button>
                                                            <button type="button" class="btn common-btn"
                                                                id="searchConfig"
                                                                onclick="searchgetData()">Search</button>
                                                            <button type="button" class="btn common-btn" id="Add"
                                                                data-toggle="modal"
                                                                data-target="#add-mapping-modal">Add</button>
                                                        </div>
                                                    </div> -->

                                        </form>


                                    </div>
                                    <div class="common-jqGrid" style="margin-top: 13px;">

                                        <table id="cusjqGrid"></table>
                                        <div id="cusjqGridPager"></div>
                                    </div>
                                </div>
                                <div class="box-3">
                                    <div class="file-upload-box">
                                        <!-- <div class="inner-title">
                                            <h4>Search By Product</h4>
                                        </div> -->

                                        <form action="">
                                            <div class="row">
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="inputPassword" class="col-sm-5 col-form-label"
                                                            style="padding-left: 3px;">SHESHI Number
                                                        </label>
                                                        <div class="col-sm-7">
                                                            <input type="text" class="form-control form-control-sm"
                                                                id="ADR_REFERENCE_NUMBER" name="REFERENCE_NUMBER">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label class="col-sm-5 col-form-label"
                                                            style="padding-left: 3px;">Upload status
                                                        </label>
                                                        <div class="col-sm-7">
                                                            <select class="form-control form-control-sm"
                                                                id="ADR_UPLOAD_STATUS" name="ETQ_UPLOAD_STATUS"
                                                                style="height: 31px; font-size: small;">

                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="rsid" class="col-sm-4 col-form-label"
                                                            style="padding-left: 3px;">Debug
                                                            level
                                                            <span style="color: red;">&#42</span>
                                                        </label>
                                                        <div class="col-sm-7">
                                                            <select class="form-control form-control-sm"
                                                                id="ADR_DEBUG_LEVEL" name="DEBUG_LEVEL"
                                                                style="height: 31px; font-size: small;" disabled>
                                                                <option value="1" id="1">1</option>
                                                                <option value="2" id="2">2</option>
                                                            </select>
                                                            <div class="help-block with-errors"></div>
                                                        </div>
                                                        <!-- <label for="rsid" class="col-form-label">Request for
                                                            <span style="color: red;">&#42</span>
                                                        </label>
                                                        <div class="col-sm-7">
                                                            <select class="form-control form-control-sm"
                                                                id="REQUEST_FOR" name="REQUEST_FOR"
                                                                style="height: 31px; font-size: small;" disabled>
                                                                <option value="" id="">LATEST</option>
                                                            </select>
                                                            <div class="help-block with-errors"></div>
                                                        </div> -->

                                                    </div>
                                                </div>


                                            </div>
                                            <div class="row">
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="enddate" class="col-sm-5 col-form-label"
                                                            style="padding-left: 3px;">Request
                                                            Date From</label>
                                                        <div class="col-sm-7">
                                                            <div class='input-group date' id='ADR_REQUEST_DATE_FROM_D'>
                                                                <input type='text' class="form-control form-control-sm"
                                                                    id="ADR_REQUEST_DATE_FROM"
                                                                    name="REQUEST_DATE_FROM" />
                                                                <span class="input-group-addon">
                                                                    <span class="fa fa-calendar"></span>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="enddate" class="col-sm-5 col-form-label"
                                                            style="padding-left: 3px;">Request
                                                            Date To</label>
                                                        <div class="col-sm-7">
                                                            <div class='input-group date' id='ADR_REQUEST_DATE_TO_D'>
                                                                <input type='text' class="form-control form-control-sm"
                                                                    id="ADR_REQUEST_DATE_TO" name="REQUEST_DATE_TO" />
                                                                <span class="input-group-addon">
                                                                    <span class="fa fa-calendar"></span>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="form-group row">
                                                        <label for="inputPassword" class="col-sm-4 col-form-label"
                                                            style="padding-left: 3px;">Batch </label>
                                                        <div class="col-sm-7">
                                                            <input type="text" class="form-control form-control-sm"
                                                                id="ADR_BATCH" name="BATCH_ID">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row pb-2 mb-0">
                                                <div class="col-sm-4 pb-0 mb-0">
                                                    <div class="form-group row pb-0 mb-0">
                                                        <div class="form-group mb-0 pb-0" id="ADR_configButton">
                                                            <button type="reset" class="btn common-btn" id="btnClear"
                                                                style="margin-bottom: 0px;">Clear</button>
                                                            <button type="button" class="btn common-btn"
                                                                id="searchConfig" style="margin-bottom: 0px;"
                                                                onclick="searchgetAddRefData()">Search</button>
                                                        </div>
                                                    </div>
                                                </div>


                                            </div>
                                        </form>


                                    </div>
                                    <div class="common-jqGrid" style="margin-top: 13px;">
                                        <!-- <div class="add-btn-box">
                                            <a href="javascript:void(0);" id="reProcessPMS" rel="tooltip"
                                                data-placement="left" title="Re Process">
                                                <i class="fa fa-repeat" aria-hidden="true"></i>
                                            </a>
                                        </div> -->
                                        <table id="addRefjqGrid"></table>
                                        <div id="addRefjqGridPager"></div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="footer text-center"
            style="background-color: #D2D8FF; margin-top: -15px; font-size: small; color: #0F1F8A; height: fit-content;">
            <span style="margin-left: 150px;">&#169; 2023 Copyright | Evident</span>
        </div>

        <div class="modal fade bd-example-modal-lg" id="uploadcompmodal" tabindex="-1" role="dialog"
            aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="static"
            style="background:rgba(0, 0, 0, 0.5) !important ;">
            <div class="modal-dialog modal-dialog-centered" role="document" style="max-width: 85%;">
                <div class="modal-content">
                    <div class="modal-header pt-2 pb-2">
                        <h5 class="modal-title" id="exampleModalLabel">Update Error Log</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="uploadform">
                            <input type="hidden" name="SCHEDULE_ID" id="REMOVE_SCHEDULE_ID">
                            <input type="hidden" name="INTERFACE_NAME" id="REMOVE_SCHEDULE_INTERFACE">
                            <div class="row ml-1">
                                <div class="col-sm-4">
                                    <div class="form-group row">
                                        <label for="inputPassword" class="col-sm-5 col-form-label"
                                            style="padding-left: 3px;">Reference Number <span
                                                style="color: red;">&#42</span>
                                        </label>
                                        <div class="col-sm-7">
                                            <input type="text" class="form-control form-control-sm"
                                                id="UPLOAD_REFERENCE_NUMBER" name="UPLOAD_REFERENCE_NUMBER" required>
                                            <div class="help-block with-errors"><span id="UPLOAD_REFERENCE_NUMBER_ERROR"
                                                    style="font-size: 12px;padding: 4px 0 0;color: red;"></span></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-4">
                                    <div class="form-group row">
                                        <label class="col-sm-5 col-form-label" style="padding-left: 3px;">Complaint
                                            Number <span style="color: red;">&#42</span>
                                        </label>
                                        <div class="col-sm-7">
                                            <input type="text" class="form-control form-control-sm"
                                                id="UPLOAD_COMPLAINT_NUMBER" name="UPLOAD_COMPLAINT_NUMBER" required>
                                            <div class="help-block with-errors"><span id="UPLOAD_COMPLAINT_NUMBER_ERROR"
                                                    style="font-size: 12px;padding: 4px 0 0;color: red;"></span></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group mb-0 pb-0 ml-5">
                                    &nbsp;<button type="reset" class="btn common-btn" style="margin-bottom: 0px;"
                                        id="addBtn" onclick="uploadformreset()">Clear</button>
                                    &nbsp;&nbsp;<button type="button" class="btn common-btn" id="addBtnupdate"
                                        style="margin-bottom: 0px;" onclick="uploadconfirm()">Update</button>
                                    &nbsp;&nbsp;<button type="reset" class="btn common-btn" id="addBtn"
                                        style="margin-bottom: 0px;" onclick="getUploadData()">Reset View</button>
                                </div>

                            </div>

                            <div class="common-jqGrid">
                                <table id="uploadjqGrid"></table>
                                <div id="uploadjqGridPager"></div>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade bd-example-modal-lg" id="uploadalertmodal" tabindex="-1" role="dialog"
            aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="static"
            style="background:rgba(0, 0, 0, 0.5) !important ;">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header pt-2 pb-2">
                        <h5 class="modal-title" id="exampleModalLabel">Alert</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form action="/scheduler" data-toggle="validator">
                            <input type="hidden" name="SCHEDULE_ID" id="REMOVE_SCHEDULE_ID">
                            <input type="hidden" name="INTERFACE_NAME" id="REMOVE_SCHEDULE_INTERFACE">
                            <p style="font-size:13px;" id="UPLOAD_CONFIRM_MSG"> </p>
                            <div class="form-group row mr-2 mt-1 mb-0" style="float: right">
                                <button type="reset" id="addBtnClear" class="btn common-btn" data-dismiss="modal"
                                    style="float: center ;">Cancel</button>
                                &nbsp;&nbsp;<button type="submit" id="addBtnClear" class="btn common-btn"
                                    data-dismiss="modal" style="float: center ;"
                                    onclick="uploadErrorData();">Ok</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

        </div>
        <div class="modal fade bd-example-modal-lg" id="uploadcomplaintfile" tabindex="-1" role="dialog"
            aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="static"
            style="background:rgba(0, 0, 0, 0.5) !important ;">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header pt-2 pb-2">
                        <h5 class="modal-title" id="exampleModalLabel">Upload Complaint</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form data-toggle="validator">
                            <input type="hidden" id="CREATED_BY" name="CREATED_BY">
                            <input type="hidden" id="BUSINESS_UNIT_SITE_ID" name="BUSINESS_UNIT_SITE_ID">
                            <label for="formFile" class="form-label">Select a file <span style="color: red;">&#42</span>
                            </label>
                            <input class="form-control form-control-sm" type="file" id="formFile" name="formFile"
                                accept="application/vnd.ms-excel , application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
                            <p style="font-size:13px; color: #dc3545;" id="UPLOAD_COMP_SIZE"> </p>
                            <div class="form-group row mr-2 mt-2 mb-0" style="float: right">
                                <button type="reset" id="addBtnClear" class="btn common-btn" style="float: center ;"
                                    onclick="removemsg()">Cancel</button>
                                &nbsp;&nbsp;<button type="submit" id="uploadcompbtn" class="btn common-btn"
                                    data-dismiss="modal" style="float: center ; cursor: not-allowed;" disabled
                                    onclick="uploadFile()">Ok</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

        </div>

    </body>

    </html>