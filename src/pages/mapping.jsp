<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
	<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
	<!DOCTYPE html>
	<html lang="en">

	<head>
		<title>EMA-User Role Mapping</title>
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
		<script type="text/javascript" src="../js/customJS/mapping.js"></script>
		<script
			src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>
		
		


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
								<label for="inputPassword" class="col-form-label" style="width: 80px">Role </label>
								<div class="col-sm-2">
									<select class="form-control" id="ROLE_ID" name="ROLE_ID"
										style="height: 38px; font-size: small;" disabled>
										<option value="3" id="0">Reader</option>
									</select>
								</div>
								<label class="col-form-label" style="width: 130px">User Display Name</label>
								<div class="col-sm-2">
									<input type="text" class="form-control" id="USER_NAME" name="USER_NAME">
								</div>
							</div>
							<div class="form-group row mt-2">
								<label for="startdate" class="col-form-label" style="width: 80px">Start Date</label>
								<div class="col-sm-2">
									<div class='input-group date' id='START_DATE_D'>
										<input type='text' class="form-control" id="START_DATE" name="START_DATE" />
										<span class="input-group-addon">
											<span class="fa fa-calendar"></span>
										</span>
									</div>
								</div>
								<label for="enddate" class="col-form-label" style="width: 130px">End
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
											data-target="#add-mapping-modal">Add</button>
									</div>
								</div>

							</div>




						</form>
					</div>
					<div class="common-jqGrid">
						<table id="mappinggrid"></table>
						<div id="mappingPager"></div>
					</div>

				</div>

			</div>

		</div>
		<div class="footer text-center "
			style="background-color: #D2D8FF; margin-top: -15px; font-size: small; color: #0F1F8A; height: fit-content;">
			<span style="margin-left: 150px;">&#169; 2023 Copyright | Evident</span>
		</div>

		<!-- Add Mapping -->
		<div id="add-mapping-modal" class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog"
			data-backdrop="static" data-keyboard="false" aria-labelledby="myLargeModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-lg modal-dialog-centered" style="max-width: 78%;">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="exampleModalLabel">User Role Mapping</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form action="/oekg/mapping/addMapping" method="post" data-toggle="validator">
							<input type="hidden" id="CREATED_BY" name="CREATED_BY">
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group row">
										<label for="rsid" class="col-sm col-form-label" style="max-width: 126px;">Role
											<span style="color: red;">&#42</span>
										</label>
										<div class="col-sm-9">
											<select class="form-control form-control-sm" id="ADD_ROLE_ID" name="ROLE_ID"
												style="height: 38px; font-size: small;" required disabled>												
												<option value="3" id="3">Reader</option>
											</select>
											<div class="help-block with-errors"></div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group row">
										<label for="rsid" class="col-sm col-form-label" style="max-width: 126px;">User
											<span style="color: red;">&#42</span>
										</label>
										<div class="col-sm-9">
											<div class="multiselect-box single-select">
												<select class="common-multiselect" id="ADD_EMAIL_ADDRESS" name="USER_ID"
													style="font-size: small;" required>
													<option value="" id="">Select User</option>
												</select>
												<!-- <div class="help-block with-errors"></div> -->
												<span id="EMAIL_VAL" style="color:red ; font-size: 12px ; "></span>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group row">
										<label for="rsid" class="col-sm col-form-label"
											style="max-width: 126px;">Business
											Unit<span style="color: red;">&#42</span>
										</label>
										<div class="col-sm-9">
											<select class="form-control form-control-sm" id="ADD_BUSINESS_UNIT"
												name="BUSINESS_UNIT_SITE_ID" style="height: 38px; font-size: small;"
												required>
												<option value="" id="">Select BU</option>
											</select>
											<div class="help-block with-errors"></div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group row">
										<label for="rsid" class="col-sm col-form-label" style="max-width: 126px;">Start
											Date <span style="color: red;">&#42</span></label>
										<div class="col-sm-9">
											<div class='input-group date' id='ADD_START_DATE_D'>
												<input type='text' class="form-control form-control-sm"
													id="ADD_START_DATE" name="START_DATE" style="height: 38px"
													required />
												<span class="input-group-addon">
													<span class="fa fa-calendar"></span>
												</span>
											</div>
											<div class="help-block with-errors"></div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group row">
										<label for="rsid" class="col-sm col-form-label" style="max-width: 126px;">End
											Date </label>
										<div class="col-sm-9">
											<div class='input-group date' id='ADD_END_DATE_D'>
												<input type='text' disabled="disabled"
													class="form-control form-control-sm" id="ADD_END_DATE"
													name="END_DATE" style="height: 38px" />
												<span class="input-group-addon">
													<span class="fa fa-calendar"></span>
												</span>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group row">
										<label for="checkbox" class="col-sm col-form-label" style="max-width: 85px;">Is
											Active</label>
										<input type="checkbox" value='Y'  id="isactive"
											name="IS_ACTIVE" />
										<input type="hidden" value='N'  id="isactivehidden"
											name="IS_ACTIVE" />
									</div>
								</div>
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
		<!-- update Mapping -->
		<div id="update-mapping-modal" class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog"
			data-backdrop="static" data-keyboard="false" aria-labelledby="myLargeModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-lg modal-dialog-centered" style="max-width: 78%;">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="exampleModalLabel">User Role Mapping</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form action="/oekg/mapping/updateMapping" method="post" data-toggle="validator">
							<input type="hidden" id="EDIT_ROLE_USER_ID" name="ROLE_USER_ID">
							<input type="hidden" id="EDIT_CREATED_BY" name="CREATED_BY">
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group row">
										<label for="rsid" class="col-sm col-form-label" style="max-width: 126px;">Role
											<span style="color: red;">&#42</span>
										</label>
										<div class="col-sm-9">
											<select class="selectpicker form-control " id="EDIT_ROLE" name="ROLE_ID"
												style="height: 38px; font-size: small;" required disabled>
												<option value="3" id="3">Reader</option>
											</select>
											<div class="help-block with-errors"></div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group row">
										<label for="rsid" class="col-sm col-form-label" style="max-width: 126px;">User
											<span style="color: red;">&#42</span>
										</label>
										<div class="col-sm-9">
											<div class="multiselect-box single-select">
												<select class="common-multiselect" id="EDIT_EMAIL_ADDRESS"
													name="USER_ID" style="height: 38px; font-size: small;" required>
													<option value="" id="">Select User</option>
												</select>
												<span id="EDIT_EMAIL_VAL" style="color:red ; font-size: 12px ; "></span>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group row">
										<label for="rsid" class="col-sm col-form-label"
											style="max-width: 126px;">Business
											Unit<span style="color: red;">&#42</span>
										</label>
										<div class="col-sm-9">
											<select class="selectpicker form-control" id="EDIT_BUSINESS_UNIT"
												name="BUSINESS_UNIT_SITE_ID" style="height: 38px; font-size: small;"
												required>
												<option value="" id="">Select BU</option>
											</select>
											<div class="help-block with-errors"></div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group row">
										<label for="rsid" class="col-sm col-form-label" style="max-width: 126px;">Start
											Date <span style="color: red;">&#42</span></label>
										<div class="col-sm-9">
											<div class='input-group date' id='EDIT_START_DATE_D'>
												<input type='text' class="form-control form-control-sm"
													id="EDIT_START_DATE" name="START_DATE" style="height: 38px"
													required />
												<span class="input-group-addon">
													<span class="fa fa-calendar"></span>
												</span>
											</div>
											<div class="help-block with-errors"></div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group row">
										<label for="rsid" class="col-sm col-form-label" style="max-width: 126px;">End
											Date </label>
										<div class="col-sm-9">
											<div class='input-group date' id='EDIT_END_DATE_D'>
												<input type='text' class="form-control form-control-sm"
													id="EDIT_END_DATE" name="END_DATE" style="height: 38px" />
												<span class="input-group-addon">
													<span class="fa fa-calendar"></span>
												</span>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group row">
										<label for="checkbox" class="col-sm col-form-label" style="max-width: 85px;">Is
											Active</label>
										<input type="checkbox" value='Y'  id="EDIT_IS_ACTIVE"
											name="IS_ACTIVE" />
										<input type="hidden" value='N' 
											id="EDIT_IS_ACTIVE_HIDDEN" name="IS_ACTIVE" />
									</div>
								</div>
							</div>
							<div class="form-group row mr-5 mb-0" style="float: right">
								<button type="submit" class="btn common-btn" id="addBtnEdit"
									onclick="editCheckBox()">Apply Changes</button>
								&nbsp; &nbsp;
								<button type="reset" class="btn common-btn" id="editBtnClear">Clear</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

	</body>



	</html>