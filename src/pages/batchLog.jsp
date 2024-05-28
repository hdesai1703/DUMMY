<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
	<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
	<!DOCTYPE html>
	<html lang="en">

	<head>
		<title>EMA-Batch Log</title>
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
		<script type="text/javascript" src="../js/customJS/batchLog.js"></script>

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
					<div class="top-box clearfix">
						<div class="page-main-title">
							<h5>Business Center : <b><span id="bcTitle"></span></b></h5>
						</div>
					</div>
					
					
					<div class="file-upload-box mt-1">
						<form action="">
							<div class="form-group row mt-1">
								<label for="rsid" class="col-form-label" style="width: 80px;">Interface</label>
								<div class="col-sm-2">
									<select class="form-control" id="INTERFACE_NAME"
										name="REQ_MODULE" style="font-size: small;height: 38px;" required>
										<option value="">Select </option>
									</select>
								</div>
								<label for="rsid" class="col-form-label" style="width: 100px;">Interface Type</label>
								<div class="col-sm-2">
									<select class="form-control" id="INTERFACE_TYPE" style="font-size: small;height: 38px;"
										name="REQ_FOR" required>
										<option value="">Select</option>
									</select>
								</div>
								<label for="rsid" class="col-form-label" style="width: 100px;">Log Status</label>
								<div class="col-sm-2">
									<select class="form-control" id="LOG_STATUS" name="STATUS"
									style="font-size: small;height: 38px;" required>										
									</select>
								</div>
							</div>

							<div class="form-group row mt-2">
								<label for="startdate" class="col-form-label" style="width: 80px">Start Date</label>
								<div class="col-sm-2">
									<div class='input-group date' id='START_DATE_D'>
										<input type='text' class="form-control " id="START_DATE"
											name="START_DATE" />
										<span class="input-group-addon">
											<span class="fa fa-calendar"></span>
										</span>
									</div>
								</div>
								<label for="enddate" class="col-form-label" style="width: 100px">End
									Date</label>
								<div class="col-sm-2">
									<div class='input-group date' id='END_DATE_D'>
										<input type='text' class="form-control " id="END_DATE"
											name="END_DATE" />
										<span class="input-group-addon">
											<span class="fa fa-calendar"></span>
										</span>
									</div>
								</div>
								<!-- <label for="rsid" class="col-form-label" style="width: 100px;">Parent/Child</label>
								<div class="col-sm-2">
									<select class="form-control " id="PARENT_CHILD" style="font-size: small;height: 38px;"
										name="PHASE_DESIGN_NAME" required>
										<option value="0">Select</option>
									</select>
								</div>-->
								<div class="form-group" id="configButton">
									<button type="reset" class="btn common-btn" id="btnClear">Clear</button>
									<button type="button" class="btn common-btn" id="searchConfig"
										onclick="searchgetData()">Search</button>
								</div>
							</div>


						</form>
					</div>

					<div class="common-jqGrid">
						<table id="batchloggrid"></table>
						<div id="batchlogPager"></div>
					</div>

				</div>
			</div>
		</div>
		<div class="footer text-center "
			style="background-color: #D2D8FF; margin-top: -15px; font-size: small; color: #0F1F8A; height: fit-content;">
			<span style="margin-left: 150px;">&#169; 2023 Copyright | Evident</span>
		</div>
	</body>

	</html>