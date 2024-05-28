<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
	<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
	<!DOCTYPE html>
	<html lang="en">

	<head>
		<title>EMA-User</title>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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

		<script type="text/javascript" src="../js/customJS/user.js"></script>
		<script
			src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>
		<style>
			div#searchhdfbox_configurationgrid.ui-jqdialog-titlebar.ui-widget-header.ui-corner-all.ui-helper-clearfix {
				height: 35px;
				padding-top: 4px;
				border-radius: 0px;
			}

			span.ui-jqdialog-title {
				font-size: 13px;
				font-weight: 500;
				padding-left: 165px;
				padding-top: 2px;
			}

			div#searchmodfbox_configurationgrid.ui-widget.ui-widget-content.ui-corner-all.ui-jqdialog.jqmID1 {
				padding-left: 0px;
				padding-right: 0px;
				padding-top: 0px;
				padding-bottom: 0px;
				border-radius: 10px;
			}

			/* a#fbox_configurationgrid_reset.fm-button.ui-state-default.ui-corner-all.fm-button-icon-left.ui-search,		
	a#fbox_configurationgrid_search.fm-button.ui-state-default.ui-corner-all.fm-button-icon-right.ui-search {
		background-color: blue;
	} */

			select,
			select.opts,
			input#jqg5.input-elm,
			input#jqg1.input-elm {
				width: 107px;
				height: 20px;
			}

			select.opsel {
				width: 109px;
				height: 20px;
			}

			input.add-rule.ui-add {
				margin-left: 5px;
				height: 20px;
				margin-top: 0px;
				margin-bottom: 0px;
				margin-left: 2px;
				margin-right: 0px;
				padding-top: 0px;
				padding-bottom: 0px;
			}

			.has-error input,
			.has-error textarea {
				border-color: #dc3545;
			}

			.has-error .form-control.form-control-sm {
				border: 1px solid red;
			}

			.has-error li {
				font-size: 12px;
				padding: 4px 0 0;
				color: red;
			}

			/* .ui-jqgrid .ui-jqgrid-htable .ui-jqgrid-btable {
				table-layout: auto;
			} */

			/* .ui-jqgrid .ui-jqgrid-bdiv {
				overflow-y: scroll;
				overflow-x: scroll;
				color: blue				
			} */

			/* .common-jqGrid .ui-th-column {		
				padding-right: 10px;
			} */
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


						<div class="file-upload-box mt-1">
							<form action="">
								<div class="form-group row mt-1">
									<label for="role" class="col-form-label" style="width: 120px">User Display
										Name</label>
									<div class="col-sm-2">
										<input type="text" class="form-control" name="USER_NAME" id="USER_NAME">
									</div>
									<label for="startdate" class="col-form-label" style="width: 100px">Start
										Date</label>
									<div class="col-sm-2">
										<div class='input-group date' id='START_DATE_D'>
											<input type='text' class="form-control" id="START_DATE" name="START_DATE" />
											<span class="input-group-addon">
												<span class="fa fa-calendar"></span>
											</span>
										</div>
									</div>

								</div>
								<div class="form-group row">
									<label for="enddate" class="col-form-label" style="width: 120px">End
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
												data-target="#add-user-modal">Add</button>
										</div>
									</div>
								</div>
							</form>
						</div>
						<div class="common-jqGrid">
							<table id="usergrid"></table>
							<div id="userPager"></div>
						</div>
					</div>
				</div>

			</div>
			<div class="footer text-center "
				style="background-color: #D2D8FF; margin-top: -15px; font-size: small; color: #0F1F8A; height: fit-content;">
				<span style="margin-left: 150px;">&#169; 2023 Copyright | Evident</span>
			</div>

			<!-- Add Role -->
			<div id="add-user-modal" class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog"
				data-backdrop="static" data-keyboard="false" aria-labelledby="myLargeModalLabel" aria-hidden="true">
				<div class="modal-dialog modal-lg modal-dialog-centered" style="max-width: 76%;">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="exampleModalLabel">User Details</h5>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<form action="/oekg/user/addUser" method="post" data-toggle="validator">
								<input type="hidden" id="CREATED_BY" name="CREATED_BY">
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group row">
											&nbsp; <label for="rsid" class="col-sm col-form-label"
												style="max-width: 160px;">User Display
												Name <span style="color: red;">&#42</span>
											</label>
											<div class="col-sm-8">
												<input type="text" class="form-control form-control-sm "
													name="USER_NAME" id="USER_NAME" required>
												<div class="help-block with-errors"></div>
											</div>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group row">
											<label for="exampleFormControlTextarea1" class="col-sm col-form-label"
												style="max-width: 126px;">Email Address<span
													style="color: red;">&#42</span></label>
											<div class="col-sm-8">
												<input type="email" class="form-control form-control-sm "
													name="EMAIL_ADDRESS" id="EMAIL_ADDRESS" required>
												<div class="help-block with-errors"></div>
											</div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group row">
											&nbsp; <label for="rsid" class="col-sm col-form-label"
												style="max-width: 160px;">Start
												Date <span style="color: red;">&#42</span>
											</label>
											<div class="col-sm-8">
												<div class='input-group date' id='ADD_START_DATE_D'>
													<input type='text' class="form-control form-control-sm"
														id="ADD_START_DATE" name="START_DATE" required>
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
												style="max-width: 125.4px;">End
												Date </label>
											<div class="col-sm-8">
												<div class='input-group date' id='ADD_END_DATE_D'>
													<input type='text' disabled="disabled"
														class="form-control form-control-sm" id="ADD_END_DATE"
														name="END_DATE" />
													<span class="input-group-addon">
														<span class="fa fa-calendar"></span>
													</span>
												</div>
											</div>
										</div>
									</div>

								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group row">
											&nbsp; <label for="rsid" class="col-sm col-form-label"
												style="max-width: 160px;">User Type
												<span style="color: red;">&#42</span>
											</label>
											<div class="col-sm-8">
												<select class="form-control form-control-sm" id="ADD_USER_TYPE"
													name="USER_TYPE" style="height: 31px; font-size: small;" required>
													<option value="" id="">Select User Type</option>
													<option value="LDAP" id="LDAP">LDAP</option>
													<option value="NON-LDAP" id="NON-LDAP">NON-LDAP</option>
												</select>
												<div class="help-block with-errors"></div>
											</div>
										</div>
									</div>
									<div class="col-sm-6" id="PASSWORD_DIV">
										<div class="form-group row">
											<label class="col-sm col-form-label" style="max-width: 125.4px;">Password
												<span style="color: red;">&#42</span></label>
											<div class="col-sm-8">
												<input type="password" class="form-control form-control-sm"
													name="PASSWORD" id="ADD_PASSWORD">
												<div class="help-block with-errors"></div>
											</div>
										</div>
									</div>

								</div>
								<div class="form-group row">
									&nbsp;<label for="checkbox" class="col-sm col-form-label"
										style="max-width: 85px;">Is
										Active</label>
									<input type="checkbox" value='Y' id="isactive" name="IS_ACTIVE" />
									<input type="hidden" value='N' id="isactivehidden" name="IS_ACTIVE" />
								</div>

								<div class="form-group row mr-5 mb-0" style="float: right">
									<button type="submit" class="btn common-btn" id="addBtnEdit"
										onclick="checkBox()">Add</button>
									&nbsp; &nbsp;
									<button type="reset" class="btn common-btn" id="addBtnClear">Clear</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>

			<!-- Edit Role -->
			<div id="edit-user-modal" class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog"
				data-backdrop="static" data-keyboard="false" aria-labelledby="myLargeModalLabel" aria-hidden="true">
				<div class="modal-dialog modal-lg modal-dialog-centered" style="max-width: 76%;">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="exampleModalLabel">User Details</h5>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<form action="/oekg/user/updateUser" method="post" data-toggle="validator">
								<input type="hidden" id="EDIT_CREATED_BY" name="CREATED_BY">
								<input type="hidden" id="EDIT_USER_ID" name="USER_ID">
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group row">
											&nbsp; <label for="rsid" class="col-sm col-form-label"
												style="max-width: 160px;">User Display
												Name <span style="color: red;">&#42</span>
											</label>
											<div class="col-sm-8">
												<input type="text" class="form-control form-control-sm "
													name="USER_NAME" id="EDIT_USER_NAME" required>
												<div class="help-block with-errors"></div>
											</div>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group row">
											<label for="exampleFormControlTextarea1" class="col-sm col-form-label"
												style="max-width: 126px;">Email Address<span
													style="color: red;">&#42</span></label>
											<div class="col-sm-8">
												<input type="email" class="form-control form-control-sm "
													name="EMAIL_ADDRESS" id="EDIT_EMAIL_ADDRESS" required>
												<div class="help-block with-errors"></div>
											</div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group row">
											&nbsp; <label for="rsid" class="col-sm col-form-label"
												style="max-width: 160px;">Start
												Date <span style="color: red;">&#42</span>
											</label>
											<div class="col-sm-8">
												<div class='input-group date' id='EDIT_START_DATE_D'>
													<input type='text' class="form-control form-control-sm"
														id="EDIT_START_DATE" name="START_DATE" required>
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
												style="max-width: 125.4px;">End
												Date </label>
											<div class="col-sm-8">
												<div class='input-group date' id='EDIT_END_DATE_D'>
													<input type='text' class="form-control form-control-sm"
														id="EDIT_END_DATE" name="END_DATE" />
													<span class="input-group-addon">
														<span class="fa fa-calendar"></span>
													</span>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group row">
											&nbsp; <label for="rsid" class="col-sm col-form-label"
												style="max-width: 160px;">User Type
												<span style="color: red;">&#42</span>
											</label>
											<div class="col-sm-8">
												<select class="form-control form-control-sm" id="EDIT_USER_TYPE"
													name="USER_TYPE" style="height: 31px; font-size: small;" required>
													<option value="" id="">Select User Type</option>
													<option value="LDAP" id="LDAP">LDAP</option>
													<option value="NON-LDAP" id="NON-LDAP">NON-LDAP</option>
												</select>
												<div class="help-block with-errors"></div>
											</div>
										</div>
									</div>
									<!-- <div class="col-sm-6" id="EDIT_PASSWORD_DIV">
									<div class="form-group row">
										<label class="col-sm col-form-label" style="max-width: 125.4px;">Password <span style="color: red;">&#42</span></label>
										<div class="col-sm-8">
											<input type="text" class="form-control form-control-sm" name="PASSWORD"
												id="EDIT_PASSWORD">
											<div class="help-block with-errors"></div>
										</div>
									</div>
								</div> -->

								</div>
								<div class="form-group row">
									&nbsp;
									<label for="rsid" class="col-sm col-form-label" style="max-width: 85px;">Is
										Active </label>
									<input type="checkbox" value='Y' id="EDIT_IS_ACTIVE" name="IS_ACTIVE" />
									<input type="hidden" value='N' id="EDIT_IS_ACTIVE_HIDDEN" name="IS_ACTIVE" />
								</div>

								<div class="form-group row mr-5 mb-0" style="float: right">
									<button type="submit" class="btn common-btn" id="addBtnEdit"
										onclick="editCheckBox(e)">Apply
										Changes</button>
									&nbsp; &nbsp;
									<button type="reset" class="btn common-btn" id="addBtnClear">Clear</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>





	</body>

	</html>