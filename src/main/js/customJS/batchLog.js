var dataList, BUNAME;



$(document).ready(function () {
	document.getElementById('bcTitle').innerHTML = CryptoJS.AES.decrypt(sessionStorage.getItem('BUSINESS_UNIT'), "EMA").toString(CryptoJS.enc.Utf8);
	getData();
	BUNAME = CryptoJS.AES.decrypt(sessionStorage.getItem('BUSINESS_UNIT'), "EMA").toString(CryptoJS.enc.Utf8);

	buid();
	interfaceNameList();
	LogList();
	parentChildList();
	interfaceType();
	// jQuery(window).bind("resize", function () {
	// 	// var browserZoomLevel = Math.round(window.devicePixelRatio * 100);.
	// 	console.log(jQuery(window).innerHeight() * .71 - 120)
	// 	jQuery("#batchloggrid").jqGrid('setGridHeight', jQuery(window).innerHeight() * .71 - 120)


	// }).trigger("resize");

});

$(function () {
	$('#START_DATE_D').datetimepicker({
		format: 'YYYY-MM-DD',
		useCurrent: false
	});
	$('#END_DATE_D').datetimepicker({
		format: 'YYYY-MM-DD',
		useCurrent: false
	});

});

function getData() {
	jQuery('#batchloggrid').jqGrid('clearGridData');
	$("#batchloggrid").closest(".ui-jqgrid").find('.loading').show();
	var URL = "/oekg/batchlog/getBLData";
	$.ajax({
		type: "GET",
		url: URL,
		success: function (response) {
			$("#batchloggrid").closest(".ui-jqgrid").find('.loading').hide();
			dataList = response.Data;
			console.log(response);
			console.log(dataList);
			$("#batchloggrid").jqGrid('GridUnload');
			loadDataToGrid(dataList);
			jQuery('#batchloggrid').jqGrid('setGridParam', {
				data: dataList
			});
			jQuery('#batchloggrid').trigger('reloadGrid');
		}
	}

	);
};

function interfaceNameList() {
	var URL = "/oekg/batchlog/getInterfaceList";
	$.ajax({
		type: "GET",
		url: URL,
		success: function (response) {
			dataList = response.Data;
			$.each(dataList, function (index, value) {
				$('#INTERFACE_NAME').append('<option value="' + value.INTERFACE_ID + '">' + value.INTERFACE_NAME + '</option>');
			});
		},
	});

}
function buid() {

	var URL = "/oekg/etqMonitor/getbuid";

	var BU_NAME_W = BUNAME


	//Calling REST API
	$.post(URL, {
		BUSSINESS_UNIT_SITE: BU_NAME_W

	},
		function (response) {
			console.log("Success function")
		}

	);
};
function LogList() {
	var URL = "/oekg/batchlog/getLogList";
	$.ajax({
		type: "GET",
		url: URL,
		success: function (response) {
			dataList = response.Data;
			$.each(dataList, function (index, value) {
				$('#LOG_STATUS').append('<option value="' + value.LOG_STATUS_ID + '">' + value.LOG_STATUS_NAME + '</option>');
			});
		},
	});

}

function parentChildList() {
	var URL = "/oekg/batchlog/getLogList";
	$.ajax({
		type: "GET",
		url: URL,
		success: function (response) {
			dataList = response.Data;
			$.each(dataList, function (index, value) {
				$('#PARENT_CHILD').append('<option value="' + value.LOG_STATUS_ID + '">' + value.LOG_STATUS_NAME + '</option>');
			});
		},
	});

}

function interfaceType() {
	var URL = "/oekg/batchlog/getInterfaceTypeList";
	$.ajax({
		type: "GET",
		url: URL,
		success: function (response) {
			dataList = response.Data;
			$.each(dataList, function (index, value) {
				$('#INTERFACE_TYPE').append('<option value="' + value.INTERFACE_TYPE_ID + '">' + value.INTERFACE_TYPE + '</option>');
			});
		},
	});

}

function searchgetData() {

	jQuery('#batchloggrid').jqGrid('clearGridData');
	$("#batchloggrid").closest(".ui-jqgrid").find('.loading').show();
	var URL = "/oekg/batchlog/getSearchBLData";

	// var conversionStart;

	// var conversionEnd;
	var INTERFACE_NMAE_W = $('#INTERFACE_NAME').val();
	var INTERFACE_TYPE_W = $('#INTERFACE_TYPE').val();
	var LOG_STATUS_W = $('#LOG_STATUS').val();
	var START_DATE_W = $('#START_DATE').val();
	var END_DATE_W = $('#END_DATE').val();
	// IS ACTIVE



	// Calling REST API
	$.post(URL, {

		REQ_MODULE: INTERFACE_NMAE_W,
		REQ_FOR: INTERFACE_TYPE_W,
		STATUS: LOG_STATUS_W,
		START_DATE: START_DATE_W,
		END_DATE: END_DATE_W,


	}, function (response) {
		$("#batchloggrid").closest(".ui-jqgrid").find('.loading').hide();
		console.log(response);
		dataList = response.Data;
		console.log(dataList);
		$("#batchloggrid").jqGrid('GridUnload');
		loadDataToGrid(dataList);
		jQuery('#batchloggrid').jqGrid('setGridParam', {
			data: dataList
		});
		jQuery('#batchloggrid').trigger('reloadGrid');
	}
	);
};

function loadDataToGrid(dataList) {
	$("#batchloggrid")
		.jqGrid(
			{
				data: dataList,
				datatype: "local",
				colNames: ['BATCH ID', 'SCHEDULE ID',
					'EVENT', 'BUSINESS UNIT ID',
					'INTERFACE NAME', 'INTERFACE TYPE',
					'PARENT BATCH', 'STATUS', 'MESSAGE',
					'START_DATE', 'END_DATE', 'ATTRIBUTE1',
					'ATTRIBUTE2', 'ATTRIBUTE3', 'ATTRIBUTE4',
					'ATTRIBUTE5', 'ATTRIBUTE6', 'ATTRIBUTE7',
					'ATTRIBUTE8', 'ATTRIBUTE9', 'ATTRIBUTE10'],

				colModel: [
					{
						// name and index must be
						// same
						// must be similar to
						// console
						// must be similar to
						// console
						name: 'PROGRAM_ID',
						index: 'PROGRAM_ID',
						width: 60,
						sortable: false,
						align: 'center'
					},
					{
						name: 'SCHEDULER_ID',
						index: 'SCHEDULER_ID',
						width: 80,
						sortable: false,
						align: 'center'
					},
					{
						name: 'EVENT',
						index: 'EVENT',
						width: 50,
						sortable: false,
						align: 'center'
					},
					{
						name: 'BUSINESS_UNIT_ID',
						index: 'BUSINESS_UNIT_ID',
						width: 130,
						align: 'center',
						hidden: true
					},
					{
						name: 'REQ_MODULE',
						index: 'REQ_MODULE',
						width: 130,
						sortable: false,
						align: 'center'
					},
					{
						name: 'REQ_FOR',
						index: 'REQ_FOR',
						width: 80,
						hidden: true,
						sortable: false,
						align: 'center'
					},
					{
						name: 'PARENT_BATCH_ID',
						index: 'PARENT_BATCH_ID',
						width: 85,
						sortable: false,
						align: 'center'
					},
					{
						name: 'STATUS',
						index: 'STATUS',
						width: 70,
						hidden: false,
						sortable: false,
						align: 'center'
					},
					{
						name: 'STATUS_MESSAGE',
						index: 'STATUS_MESSAGE',
						width: 180,
						hidden: false,
						sortable: false,
						align: 'center'
					},
					{
						name: 'START_DATE',
						index: 'START_DATE',
						width: 110,
						sortable: false,
						align: 'center',
						formatter: "date",
						formatoptions: {
							"srcformat": "Y-m-d H:i:s",
							"newformat": "d-m-Y H:i:s",
						},
					},
					{
						name: 'END_DATE',
						index: 'END_DATE',
						width: 110,
						sortable: false,
						align: 'center',
						formatter: "date",
						formatoptions: {
							"srcformat": "Y-m-d H:i:s",
							"newformat": "d-m-Y H:i:s",
						},
					},
					{
						name: 'ATTRIBUTE1',
						index: 'ATTRIBUTE1',
						width: 150,
						sortable: false,
						hidden: true,
						align: 'center'
					},
					{
						name: 'ATTRIBUTE2',
						index: 'ATTRIBUTE2',
						width: 130,
						sortable: false,
						hidden: true
					},
					{
						name: 'ATTRIBUTE3',
						index: 'ATTRIBUTE3',
						width: 130,
						sortable: false,
						hidden: true
					},
					{
						name: 'ATTRIBUTE4',
						index: 'ATTRIBUTE4',
						width: 130,
						sortable: false,
						hidden: true
					},
					{
						name: 'ATTRIBUTE5',
						index: 'ATTRIBUTE5',
						width: 130,
						sortable: false,
						hidden: true
					},
					{
						name: 'ATTRIBUTE6',
						index: 'ATTRIBUTE6',
						width: 130,
						sortable: false,
						hidden: true
					},
					{
						name: 'ATTRIBUTE7',
						index: 'ATTRIBUTE7',
						width: 130,
						sortable: false,
						hidden: true
					},
					{
						name: 'ATTRIBUTE8',
						index: 'ATTRIBUTE8',
						width: 130,
						sortable: false,
						hidden: true
					},
					{
						name: 'ATTRIBUTE9',
						index: 'ATTRIBUTE9',
						width: 130,
						sortable: false,
						hidden: true
					},
					{
						name: 'ATTRIBUTE10',
						index: 'ATTRIBUTE10',
						width: 130,
						sortable: false,
						hidden: true
					}
				],
				viewrecords: true,
			    // rowNum: 15,
				loadonce: true,
				rowList: [10, 20, 30],
				pager: '#batchlogPager',
				height: 'auto',
				shrinkToFit: true,
				resizable: false,
				forceFit: true,
				autowidth: true,
				caption: "Batch Log",
				loadComplete: function () {
					if ($("#batchloggrid").getGridParam("records") == 0) {
						$("#batchloggrid").addRowData(
							$("#batchloggrid")
								.empty()
								.append('<tr><td style="text-align: center">No records to display</td></tr>')
						);
					}
				}
			});
	$("#batchloggrid").parents('div.ui-jqgrid-bdiv').css("max-height", "400px");
	jQuery("#batchloggrid").jqGrid('navGrid', '#batchlogPager', {
		search: true,
		edit: false,
		add: false,
		del: false
	}, {}, {}, {}, {
		multipleSearch: true,
		multipleGroup: false,
		showQuery: false,
		sopt: ['cn', 'eq'],
		defaultSearch: 'cn',
		caption: "Search Batch Log",
		Find: "Find Batch Log",
		drag: true,
		searchOnEnter: true,
		height: 260,
		width: 500
		// optDescriptions: {eq:'my eq', gt:'after', le:'on or before'}
	}

	);
}

