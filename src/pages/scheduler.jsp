<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!DOCTYPE html>
<html lang="en">

<head>
    <title>EMA-Scheduler</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/png" href="../images/favicon-32x32.png" sizes="32x32" />
    <link rel="icon" type="image/png" href="../images/favicon-16x16.png" sizes="16x16" />
    <link rel="shortcut icon" href="#" />
    <link rel="stylesheet" href="../css/jquery-ui.css">
    <link rel="stylesheet" href="../css/redmond-jquery-ui.css">
    <link rel="stylesheet" href="../css/ui.jqgrid.css">
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="../css/font-awesome.css">
    <link rel="stylesheet" href="../css/pixie-apt.css">
    <link rel="stylesheet" href="../css/pixie-main.css">
    <link rel="stylesheet" href="../css/bootstrap-multiselect.css">
    <link rel="stylesheet" href="../css/pixie-media.css">

    <script type="text/javascript" src="../js/pixie_js/respond.min.js"></script>
    <script type="text/javascript" src="../js/pixie_js/modernizr-custom.js"></script>
    <script type="text/javascript" src="../js/pixie_js/jquery-3.2.1.js"></script>
    <script type="text/javascript" src="../js/pixie_js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="../js/pixie_js/popper.min.js"></script>
    <script type="text/javascript" src="../js/pixie_js/jquery-ui.js"></script>
    <script type="text/javascript" src="../js/pixie_js/jquery.jqGrid.min.js"></script>
    <script type="text/javascript" src="../js/pixie_js/grid.locale-en.js"></script>
    <script type="text/javascript" src="../js/pixie_js/bootstrap.min.js"></script>
    <script type="text/javascript" src="../js/pixie_js/bootstrap-multiselect.js"></script>

    <script type="text/javascript" src="../js/pixie_js/moment.min.js"></script>
    <script type="text/javascript" src="../js/pixie_js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="../js/pixie_js/aes.js">
    </script>
    <!-- <script>
            function getmsg(){
                var msg="[[${Error}]]";
                console.log("inside getmsg")
                console.log(msg)
                 return "123";
            }
        </script> -->
    <script type="text/javascript" src="../js/customJS/schduler.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>




    <style>
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
    </div>
    <div class="content-page">
        <div class="content">
            <div class="container-fluid">
                <div class="top-box clearfix">
                    <div class="page-main-title">
                        <h5>Business Center : <b><span id="bcTitle"></span></b></h5>
                    </div>
                </div>


                <div class="file-upload-box mt-1 ">
                    <form action="">
                        <div class="form-group row mt-1 ">
                            <label for="inputPassword" class="col-form-label" style="width: 120px">Schedule Type
                            </label>
                            <div class="col-sm-2">
                                <select class="form-control" id="EVENT_TYPE" name="EVENT_TYPE"
                                    style="height: 38px; font-size: small;">
                                    <option value="" id="">Select</option>
                                </select>
                            </div>
                            <label for="inputPassword" class="col-form-label" style="width: 80px">Interface </label>
                            <div class="col-sm-2">
                                <select class="form-control" id="INTERFACE_NAME" name="INTERFACE_NAME"
                                    style="height: 38px; font-size: small;">
                                    <option value="" id="">Select</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group row mt-2">
                            <label for="startdate" class="col-form-label" style="width: 120px">Start Date</label>
                            <div class="col-sm-2">
                                <div class='input-group date' id='START_DATE_D'>
                                    <input type='text' class="form-control" id="START_DATE" name="START_DATE" />
                                    <span class="input-group-addon">
                                        <span class="fa fa-calendar"></span>
                                    </span>
                                </div>
                            </div>
                            <label for="enddate" class="col-form-label" style="width: 80px">End
                                Date</label>
                            <div class="col-sm-2">
                                <div class='input-group date' id='END_DATE_D'>
                                    <input type='text' class="form-control" id="END_DATE" name="END_DATE" />
                                    <span class="input-group-addon">
                                        <span class="fa fa-calendar"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="form-check ml-1 mt-2">
                                <input class="form-check-input" type="hidden" value="N" name="IS_ACTIVE"
                                    id="SEARCH_IS_ACTIVE_HIDDEN">
                                <input class="form-check-input" type="checkbox" value="Y" name="IS_ACTIVE"
                                    id="SEARCH_IS_ACTIVE" checked>
                                <label class="form-check-label" for="defaultCheck1"> Is Active </label>
                            </div>
                            <div class="col-md-4 mt-1 ml-2">
                                <div class="form-group" id="configButton">
                                    <button type="reset" class="btn common-btn" id="btnClear">Clear</button>
                                    <button type="button" class="btn common-btn" id="searchConfig"
                                        onclick="searchgetData()">Search</button>
                                    <button type="button" class="btn common-btn" id="Add" data-toggle="modal"
                                        data-target="#add-sch-modal">Add</button>
                                </div>
                            </div>

                        </div>




                    </form>
                </div>
                <div class="common-jqGrid">
                    <table id="schGrid"></table>
                    <div id="schPager"></div>
                </div>

            </div>

        </div>

    </div>
    <div class="footer text-center "
        style="background-color: #D2D8FF; margin-top: -15px; font-size: small; color: #0F1F8A; height: fit-content;">
        <span style="margin-left: 150px;">&#169; 2023 Copyright | Evident</span>
    </div>

    <!-- Add Mapping -->
    <div id="add-sch-modal" class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog" data-backdrop="static"
        data-keyboard="false" aria-labelledby="myLargeModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered" style="max-width: 65%;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Scheduler</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form action="/oekg/scheduler/addSchedule" method="post" data-toggle="validator" id="ADD_SCH_FORM">
                        <input type="hidden" id="CREATED_BY" name="CREATED_BY">
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group row">
                                    <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">Type
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <select class="form-control form-control-sm" id="ADD_EVENT_TYPE"
                                            name="EVENT_TYPE" style="height: 38px; font-size: small;" required>
                                            <option value="" id="">Select</option>
                                        </select>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group row">
                                    <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">Location
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <select class="form-control form-control-sm" id="ADD_BUSINESS_UNIT"
                                            name="BUSINESS_UNIT_SITE_ID" style="height: 38px; font-size: small;"
                                            required>
                                            <option value="" id="">Select BU</option>
                                        </select>

                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group row">
                                    <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">Interface
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <select class="form-control form-control-sm" id="ADD_INTERFACE_NAME"
                                            name="INTERFACE_NAME" style="height: 38px; font-size: small;" required>
                                            <option value="" id="">Select</option>
                                        </select>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group row">
                                    <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">Interface
                                        Type
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <select class="form-control form-control-sm" id="ADD_INTERFACE_TYPE"
                                            name="INTERFACE_TYPE" style="height: 38px; font-size: small;">
                                            <option value="">Select Interface Type</option>
                                        </select>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group row">
                                    <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">No Of
                                        Batches
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <Input type="number" class="form-control" id="ADD_NO_OF_BATCHES"
                                            name="BATCH"></Input>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group row">
                                    <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">No of
                                        Records
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <Input type="number" class="form-control" id="ADD_NO_OF_RECORDS"
                                            name="NO_OF_RECORD"></Input>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="SCH_DIV">
                            <h6 style="color: #0F1F8A;font-weight: bold; text-align: center;">Schedule</h6>
                            <div class="row">
                                <div class="col-sm-6">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label"
                                            style="max-width: 130px;">Schedule Type
                                            <span style="color: red;">&#42</span>
                                        </label>
                                        <div class="col-sm-8">
                                            <select class="form-control form-control-sm" id="SCH_SCHEDULE_TYPE"
                                                name="SCHEDULE_TYPE" style="height: 38px; font-size: small;">
                                                <option value="" id="">Select</option>
                                                <option value="Daily" id="Schedule">Daily</option>
                                                <option value="Monthly" id="Immediate">Monthly</option>
                                                <option value="Weekly" id="Immediate">Weekly</option>
                                            </select>
                                            <div class="help-block with-errors"></div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-6" id="SCH_DAILY">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label" style="max-width: 260px;">Batch
                                            Execute After every Minutes
                                            <span style="color: red;">&#42</span>
                                        </label>
                                        <div class="col-sm-3">
                                            <Input type="text" class="form-control" id="SCH_BATCH_EXECUTE_ON"
                                                name="BATCH_EXECUTE_ON"></Input>
                                            <div class="help-block with-errors"></div>
                                        </div>
                                    </div>
                                </div>


                                <div class="col-sm-6" id="SCH_MONTHLY">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">Month
                                            Day
                                            <span style="color: red;">&#42</span>
                                        </label>
                                        <div class="col-sm-8">
                                            <select class="form-control form-control-sm" id="SCH_MONTH_DAY"
                                                name="MONTH_DAY" style="height: 38px; font-size: small;">
                                                <option value="0" id="0">Select</option>
                                            </select>

                                            <div class="help-block with-errors"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6" id="SCH_WEEKLY">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">Week
                                            Day
                                            <span style="color: red;">&#42</span>
                                        </label>

                                        <div class="form-check form-check-inline">
                                            <label class="form-check-label" for="inlineCheckbox1"
                                                style="font-size: x-small; margin-right: 5px;">Sun</label>
                                            <input class="form-check-input" type="checkbox" id="inlineCheckbox1"
                                                value="0" name="WEEK_DAY">
                                            <label class="form-check-label" for="inlineCheckbox2"
                                                style="font-size: x-small ; margin-right: 5px;">Mon</label>
                                            <input class="form-check-input" type="checkbox" id="inlineCheckbox2"
                                                value="1" name="WEEK_DAY">
                                            <label class="form-check-label" for="inlineCheckbox1"
                                                style="font-size: x-small ; margin-right: 5px;">Tue</label>
                                            <input class="form-check-input" type="checkbox" id="inlineCheckbox1"
                                                value="2" name="WEEK_DAY">
                                            <label class="form-check-label" for="inlineCheckbox2"
                                                style="font-size: x-small ; margin-right: 5px;">Wed</label>
                                            <input class="form-check-input" type="checkbox" id="inlineCheckbox2"
                                                value="3" name="WEEK_DAY">
                                            <label class="form-check-label" for="inlineCheckbox1"
                                                style="font-size: x-small ;margin-right: 5px;">Thu</label>
                                            <input class="form-check-input" type="checkbox" id="inlineCheckbox1"
                                                value="4" name="WEEK_DAY">
                                            <label class="form-check-label" for="inlineCheckbox2"
                                                style="font-size: x-small ;margin-right: 5px;">Fri</label>
                                            <input class="form-check-input" type="checkbox" id="inlineCheckbox2"
                                                value="5" name="WEEK_DAY">
                                            <label class="form-check-label" for="inlineCheckbox1"
                                                style="font-size: x-small ;margin-right: 5px;">Sat</label>
                                            <input class="form-check-input" type="checkbox" id="inlineCheckbox1"
                                                value="6" name="WEEK_DAY">
                                        </div>
                                    </div>

                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-6">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label"
                                            style="max-width: 130px;">Starting Date
                                            <span style="color: red;">&#42</span>
                                        </label>
                                        <div class="col-sm-8">
                                            <div class='input-group date' id='SCH_START_DATE_D'>
                                                <input type='text' class="form-control form-control-sm"
                                                    id="SCH_START_DATE" name="START_DATE" style="height: 38px" />
                                                <span class="input-group-addon">
                                                    <span class="fa fa-calendar"></span>
                                                </span>
                                            </div>
                                            <div class="help-block with-errors"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label"
                                            style="max-width: 130px;">Staring Time
                                            <span style="color: red;">&#42</span>
                                        </label>
                                        <div class="col-sm-8">
                                            <div class='input-group date' id='SCH_START_TIME_D'>
                                                <input type='text' class="form-control form-control-sm"
                                                    id="SCH_START_TIME" name="START_TIME" style="height: 38px" />
                                                <span class="input-group-addon">
                                                    <span class="fa fa-clock-o"></span>
                                                </span>
                                            </div>
                                            <div class="help-block with-errors"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <input type="hidden" name="END_DATE" id="SCH_END_DATE">
                            <!-- <div class="row">
                                    <div class="col-sm-6">
                                        <div class="form-group row">
                                            <label for="rsid" class="col-sm col-form-label"
                                                style="max-width: 130px;">End
                                                Date </label>
                                            <div class="col-sm-8">
                                                <div class='input-group date' id='SCH_END_DATE_D'>
                                                    <input type='text' class="form-control form-control-sm"
                                                        id="SCH_END_DATE" name="END_DATE" style="height: 38px" disabled=true/>
                                                    <span class="input-group-addon">
                                                        <span class="fa fa-calendar"></span>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div> -->
                        </div>
                        <div class="form-group row mr-5 mt-1 mb-0" style="float: right">
                            <button type="submit" class="btn common-btn" id="addBtn" onclick="checkBox()">Add</button>
                            &nbsp; &nbsp;
                            <button type="reset" class="btn common-btn" id="addBtnClear">Clear</button>
                        </div>
                    </form>
                </div>

            </div>
        </div>
    </div>
    </div>
    <!-- update Mapping -->
    <div id="update-sch-modal" class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog" data-backdrop="static"
        data-keyboard="false" aria-labelledby="myLargeModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered" style="max-width: 65%;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Edit Scheduler</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form action="/oekg/scheduler/updateSchedule" method="post" data-toggle="validator"
                        id="EDIT_SCH_FORM">
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group row">
                                    <input type="hidden" id="EDIT_SCHEDULE_ID" name="SCHEDULE_ID">
                                    <input type="hidden" id="EDIT_CREATED_BY" name="CREATED_BY">
                                    <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">Type
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <select class="form-control form-control-sm" id="EDIT_EVENT_TYPE"
                                            name="EVENT_TYPE" style="height: 38px; font-size: small;" required>
                                            <option value="" id="">Select</option>
                                            <option value="Schedule" id="">Schedule</option>
                                        </select>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group row">
                                    <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">Location
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <select class="form-control form-control-sm" id="EDIT_BUSINESS_UNIT"
                                            name="BUSINESS_UNIT_SITE_ID" style="height: 38px; font-size: small;"
                                            required>
                                            <option value="" id="">Select BU</option>
                                        </select>

                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group row">
                                    <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">Interface
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <select class="form-control form-control-sm" id="EDIT_INTERFACE_NAME"
                                            name="INTERFACE_NAME" style="height: 38px; font-size: small;" required>
                                            <option value="" id="">Select</option>
                                        </select>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group row">
                                    <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">Interface
                                        Type
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <select class="form-control form-control-sm" id="EDIT_INTERFACE_TYPE"
                                            name="INTERFACE_TYPE" style="height: 38px; font-size: small;" required>
                                            <option value="">Select Interface Type</option>
                                        </select>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group row">
                                    <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">No Of
                                        Batches
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <Input type="number" class="form-control" id="EDIT_NO_OF_BATCHES"
                                            name="BATCH"></Input>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group row">
                                    <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">No of
                                        Records
                                        <span style="color: red;">&#42</span>
                                    </label>
                                    <div class="col-sm-8">
                                        <Input type="number" class="form-control" id="EDIT_NO_OF_RECORDS"
                                            name="NO_OF_RECORD"></Input>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="EDIT_SCH_DIV">
                            <h6 style="color: #0F1F8A;font-weight: bold; text-align: center;">Schedule</h6>
                            <div class="row">
                                <div class="col-sm-6">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label"
                                            style="max-width: 130px;">Schedule Type
                                            <span style="color: red;">&#42</span>
                                        </label>
                                        <div class="col-sm-8">
                                            <select class="form-control form-control-sm" id="EDIT_SCHEDULE_TYPE"
                                                name="SCHEDULE_TYPE" style="height: 38px; font-size: small;">
                                                <option value="" id="">Select</option>
                                                <option value="Daily" id="DAILY_E">Daily</option>
                                                <option value="Monthly" id="MONTHLY_E">Monthly</option>
                                                <option value="Weekly" id="WEEKLY_E">Weekly</option>
                                            </select>
                                            <div class="help-block with-errors"></div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-6" id="EDIT_SCH_DAILY">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label" style="max-width: 260px;">Batch
                                            Execute After every Minutes
                                            <span style="color: red;">&#42</span>
                                        </label>
                                        <div class="col-sm-3">
                                            <Input type="text" class="form-control" id="EDIT_BATCH_EXECUTE_ON"
                                                name="BATCH_EXECUTE_ON"></Input>
                                            <div class="help-block with-errors"></div>
                                        </div>
                                    </div>
                                </div>


                                <div class="col-sm-6" id="EDIT_SCH_MONTHLY">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">Month
                                            Day
                                            <span style="color: red;">&#42</span>
                                        </label>
                                        <div class="col-sm-8">
                                            <select class="form-control form-control-sm" id="EDIT_MONTH_DAY"
                                                name="MONTH_DAY" style="height: 38px; font-size: small;">
                                                <option value="0" id="0">Select</option>
                                            </select>

                                            <div class="help-block with-errors"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6" id="EDIT_SCH_WEEKLY">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label" style="max-width: 130px;">Week
                                            Day
                                            <span style="color: red;">&#42</span>
                                        </label>

                                        <div class="form-check form-check-inline">
                                            <label class="form-check-label" for="inlineCheckbox1"
                                                style="font-size: x-small; margin-right: 5px;">Sun</label>
                                            <input class="form-check-input" type="checkbox" id="EDIT_WEEK_DAY0"
                                                value="0" name="WEEK_DAY">
                                            <label class="form-check-label" for="EDIT_WEEK_DAY"
                                                style="font-size: x-small ; margin-right: 5px;">Mon</label>
                                            <input class="form-check-input" type="checkbox" id="EDIT_WEEK_DAY1"
                                                value="1" name="WEEK_DAY">
                                            <label class="form-check-label" for="EDIT_WEEK_DAY"
                                                style="font-size: x-small ; margin-right: 5px;">Tue</label>
                                            <input class="form-check-input" type="checkbox" id="EDIT_WEEK_DAY2"
                                                value="2" name="WEEK_DAY">
                                            <label class="form-check-label" for="EDIT_WEEK_DAY"
                                                style="font-size: x-small ; margin-right: 5px;">Wed</label>
                                            <input class="form-check-input" type="checkbox" id="EDIT_WEEK_DAY3"
                                                value="3" name="WEEK_DAY">
                                            <label class="form-check-label" for="EDIT_WEEK_DAY"
                                                style="font-size: x-small ;margin-right: 5px;">Thu</label>
                                            <input class="form-check-input" type="checkbox" id="EDIT_WEEK_DAY4"
                                                value="4" name="WEEK_DAY">
                                            <label class="form-check-label" for="EDIT_WEEK_DAY"
                                                style="font-size: x-small ;margin-right: 5px;">Fri</label>
                                            <input class="form-check-input" type="checkbox" id="EDIT_WEEK_DAY5"
                                                value="5" name="WEEK_DAY">
                                            <label class="form-check-label" for="EDIT_WEEK_DAY"
                                                style="font-size: x-small ;margin-right: 5px;">Sat</label>
                                            <input class="form-check-input" type="checkbox" id="EDIT_WEEK_DAY6"
                                                value="6" name="WEEK_DAY">
                                        </div>
                                    </div>

                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-6">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label"
                                            style="max-width: 130px;">Starting Date
                                            <span style="color: red;">&#42</span>
                                        </label>
                                        <div class="col-sm-8">
                                            <div class='input-group date' id='EDIT_START_DATE_D'>
                                                <input type='text' class="form-control form-control-sm"
                                                    id="EDIT_START_DATE" name="START_DATE" style="height: 38px" />
                                                <span class="input-group-addon">
                                                    <span class="fa fa-calendar"></span>
                                                </span>
                                            </div>
                                            <div class="help-block with-errors"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label"
                                            style="max-width: 130px;">Staring Time
                                            <span style="color: red;">&#42</span>
                                        </label>
                                        <div class="col-sm-8">
                                            <div class='input-group date' id='EDIT_START_TIME_D'>
                                                <input type='text' class="form-control form-control-sm"
                                                    id="EDIT_START_TIME" name="START_TIME" style="height: 38px" />
                                                <span class="input-group-addon">
                                                    <span class="fa fa-clock-o"></span>
                                                </span>
                                            </div>
                                            <div class="help-block with-errors"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <input type="hidden" name="END_DATE" id="EDIT_END_DATE">
                            <!-- <div class="row">
                                <div class="col-sm-6">
                                    <div class="form-group row">
                                        <label for="rsid" class="col-sm col-form-label"
                                            style="max-width: 130px;">End
                                            Date </label>
                                        <div class="col-sm-8">
                                            <div class='input-group date' id='EDIT_END_DATE_D'>
                                                <input type='text' class="form-control form-control-sm"
                                                    id="EDIT_END_DATE" name="END_DATE" style="height: 38px"/>
                                                <span class="input-group-addon">
                                                    <span class="fa fa-calendar"></span>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div> -->
                        </div>
                        <div class="form-group row mr-5 mt-1 mb-0" style="float: right">
                            <button type="submit" onclick="editcheckbox()" class="btn common-btn" id="addBtnEdit">Apply
                                Changes</button>
                            &nbsp; &nbsp;
                            <button type="reset" class="btn common-btn" id="addBtnClear">Clear</button>
                        </div>
                    </form>
                </div>

            </div>
        </div>
    </div>
    <!-- #alertmodel -->
    <div class="modal fade bd-example-modal-lg" id="alertmodal" tabindex="-1" role="dialog"
        aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="none"
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
                    <form action="/oekg/scheduler/removeScheduler" method="post" data-toggle="validator">
                        <input type="hidden" id="REMOVE_CREATED_BY" name="CREATED_BY">
                        <input type="hidden" name="SCHEDULE_ID" id="REMOVE_SCHEDULE_ID">
                        <input type="hidden" name="INTERFACE_NAME" id="REMOVE_SCHEDULE_INTERFACE">
                        <p style="font-size:15px;">Do you want to stop this Scheduler ?</p>
                        <div class="form-group row mr-5 mt-1 mb-0" style="float: right">
                            <button type="submit" class="btn common-btn" id="addBtnEdit">Yes</button>
                            &nbsp; &nbsp;
                            <button id="addBtnClear" class="btn common-btn" data-dismiss="modal"
                                style="float: right ;">No</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade bd-example-modal-lg" id="schalertmodal" tabindex="-1" role="dialog"
        aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="none"
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
                    <form action="/oekg/scheduler/scheduler" data-toggle="validator">
                        <input type="hidden" name="SCHEDULE_ID" id="REMOVE_SCHEDULE_ID">
                        <input type="hidden" name="INTERFACE_NAME" id="REMOVE_SCHEDULE_INTERFACE">
                        <p style="font-size:15px;">Unable to add a scheduler for the interface since an active scheduler
                            is running.</p>
                        <div class="form-group row mr-5 mt-1 mb-0" style="float: right">
                            <button type="submit" id="addBtnClear" class="btn common-btn" data-dismiss="modal"
                                style="float: right ;">Ok</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>


</html>