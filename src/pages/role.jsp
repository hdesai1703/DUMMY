<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
	<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
	<!DOCTYPE html>
	<html lang="en">

	<head>
		<title>EMA-Role</title>
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
		<script type="text/javascript" src="../js/customJS/role1.js"></script>
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

			.has-error .selectpicker.form-control {
				border: 1px solid red;
			}

			.has-error .form-control {
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
     </div> -->
		</div>
		<div class="content-page">
			<div class="content">
				<div class="container-fluid">
					
					<div class="file-upload-box mt-1">
						<form action="">
							<div class="form-group row mt-1">
								<label for="role" class="col-form-label" style="width: 100px">Role</label>
								<div class="col-sm-2">
									<input type="text" class="form-control" name="ROLE" id="ROLE">
								</div>
								<label for="startdate" class="col-form-label" style="width: 100px">Start Date</label>
								<div class="col-sm-2">
									<div class='input-group date' id='START_DATE_D'>
										<input type='text' class="form-control" id="START_DATE" name="START_DATE" />
										<span class="input-group-addon">
											<span class="fa fa-calendar"></span>
										</span>
									</div>
								</div>

							</div>
							<div class="form-group row mt-3">
								<label for="enddate" class="col-form-label" style="width: 100px">End
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
								<div class="col-md-4 mt-2 ml-2">
									<div class="form-group" id="configButton">
										<button type="reset" class="btn common-btn" id="btnClear">Clear</button>
										<button type="button" class="btn common-btn" id="searchConfig"
											onclick="searchgetData()">Search</button>
										<button type="button" class="btn common-btn" id="Add" data-toggle="modal"
											data-target="#add-role-modal">Add</button>
									</div>
								</div>
							</div>
						</form>
					</div>

					<div class="common-jqGrid">
						<table id="rolegrid"></table>
						<div id="rolePager"></div>
					</div>
				</div>
			</div>
		
		</div>
		<div class="footer text-center " style="background-color: #D2D8FF; margin-top: -15px; font-size: small; color: #0F1F8A; height: fit-content;"   >
			<span style="margin-left: 150px;">&#169; 2022 Copyright | Olympus</span>
		</div>

		<!-- Add Role -->
		<div id="add-role-modal" class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog"
			data-backdrop="static" data-keyboard="false" aria-labelledby="myLargeModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-lg modal-dialog-centered" style="max-width: 60%;">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="exampleModalLabel">Role Details</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form action="/addRole" method="post" data-toggle="validator">
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group row">
										&nbsp; <label for="rsid" class="col-sm col-form-label"
											style="max-width: 124px;">Role
											 <span style="color: red;">&#42</span>
										</label>
										<div class="col-sm-4">
											<input type="text" class="form-control form-control-sm " name="ROLE"
												id="ROLE"  required>
												<div class="help-block with-errors"></div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group row">
										&nbsp; <label for="exampleFormControlTextarea1" class="col-sm col-form-label"
											style="max-width: 126px;">Description</label>
										<div class="col-sm-10">
											<textarea class="form-control form-control-sm" name="DESCRIPTION"
												id="DESCRIPTION" rows="3"></textarea>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group row">
										&nbsp; <label for="rsid" class="col-sm col-form-label" style="max-width: 126px;">Start
											Date <span style="color: red;">&#42</span>
										</label>
										<div class="col-sm-8">
											<div class='input-group date' id='ADD_START_DATE_D'>
												<input type='text' class="form-control form-control-sm" id="ADD_START_DATE" name="START_DATE" required />
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
										&nbsp; <label for="rsid" class="col-sm col-form-label" style="max-width: 125.4px;">End
											Date </label>
										<div class="col-sm-8">
											<div class='input-group date' id='ADD_END_DATE_D'>
												<input type='text' disabled="disabled" class="form-control form-control-sm" id="ADD_END_DATE" name="END_DATE" />
												<span class="input-group-addon">
													<span class="fa fa-calendar"></span>
												</span>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="form-group row">
								&nbsp;<label for="checkbox" class="col-sm col-form-label" style="max-width: 85px;">Is
									Active</label>
								<input type="checkbox" value='Y'  id="isactive"
									name="IS_ACTIVE" />
								<input type="hidden" value='N'  id="isactivehidden"
									name="IS_ACTIVE" />
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
		<div id="edit-role-modal" class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog"
			data-backdrop="static" data-keyboard="false" aria-labelledby="myLargeModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-lg modal-dialog-centered" style="max-width: 60%;">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="exampleModalLabel">Role Details</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form action="/updateRole" method="post" data-toggle="validator">
							<input type="hidden" id="EDIT_ROLE_ID" name="ROLE_ID">
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group row">
										&nbsp; <label for="rsid" class="col-sm col-form-label"
											style="max-width: 124px;">Role
											<span style="color: red;">&#42</span>
										</label>
										<div class="col-sm-4">
											<input type="text" class="form-control form-control-sm " name="ROLE"
												id="EDIT_ROLE"  required>
												<div class="help-block with-errors"></div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group row">
										&nbsp; <label for="exampleFormControlTextarea1" class="col-sm col-form-label"
											style="max-width: 126px;">Description</label>
										<div class="col-sm-10">
											<textarea class="form-control form-control-sm" name="DESCRIPTION"
												id="EDIT_DESCRIPTION" rows="3"></textarea>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group row">
										&nbsp; <label for="rsid" class="col-sm col-form-label" style="max-width: 126px;">Start
											Date <span style="color: red;">&#42</span>
										</label>
										<div class="col-sm-8">
											<div class='input-group date' id='EDIT_START_DATE_D'>
												<input type='text' class="form-control form-control-sm" id="EDIT_START_DATE" name="START_DATE" required />
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
										&nbsp; <label for="rsid" class="col-sm col-form-label" style="max-width: 125.4px;">End
											Date </label>
										<div class="col-sm-8">
											<div class='input-group date' id='EDIT_END_DATE_D'>
												<input type='text' class="form-control form-control-sm" id="EDIT_END_DATE" name="END_DATE" />
												<span class="input-group-addon">
													<span class="fa fa-calendar"></span>
												</span>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="form-group row">
								&nbsp;
								<label for="rsid" class="col-sm col-form-label" style="max-width: 85px;">Is
									Active </label>
								<input type="checkbox" value='Y'  id="EDIT_IS_ACTIVE"
									name="IS_ACTIVE" />
								<input type="hidden" value='N'  id="EDIT_IS_ACTIVE_HIDDEN"
									name="IS_ACTIVE" />
							</div>

							<div class="form-group row mr-5 mb-0" style="float: right">
								<button type="submit" class="btn common-btn" id="addBtnEdit"
									onclick="editCheckBox()">Apply Changes</button>
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