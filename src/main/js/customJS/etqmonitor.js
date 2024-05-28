var dataList, dataCusList, dataAddRefList, dataLength, BUName, BuId, BUNAME;
var INTER_NAME;
function showLoader() {
	$(".loader-box").css('display', 'block');
}

function hideLoader() {
	$(".loader-box").css('display', 'none');
}
$(document).ready(function () {
    BUNAME = CryptoJS.AES.decrypt(sessionStorage.getItem('BUSINESS_UNIT'), "EMA").toString(CryptoJS.enc.Utf8);
    buid();
    document.getElementById('CREATED_BY').value = parseInt(CryptoJS.AES.decrypt(sessionStorage.getItem('USERID'), "EMA").toString(CryptoJS.enc.Utf8));
    document.getElementById('bcTitle').innerHTML = CryptoJS.AES.decrypt(sessionStorage.getItem('BUSINESS_UNIT'), "EMA").toString(CryptoJS.enc.Utf8);
    requestStatus();
    getCompData();


    if ($(".whole-tab").length > 0) {
        $('.whole-tab').easyResponsiveTabs({
            type: 'default', //Types: default, vertical, accordion
            width: 'auto', //auto or any width like 600px
            fit: true, // 100% fit in a container
            tabidentify: 'hor_1', // The tab groups identifier
        });
    }
    $(function () {
        $('#COM_REQUEST_DATE_FROM_D').datetimepicker({
            format: 'YYYY-MM-DD'
        });
        $('#COM_REQUEST_DATE_TO_D').datetimepicker({
            format: 'YYYY-MM-DD'
        });
    });

    $(function () {
        $('#CUS_REQUEST_DATE_FROM_D').datetimepicker({
            format: 'YYYY-MM-DD'
        });
        $('#CUS_REQUEST_DATE_TO_D').datetimepicker({
            format: 'YYYY-MM-DD'
        });
    });

    $(function () {
        $('#ADR_REQUEST_DATE_FROM_D').datetimepicker({
            format: 'YYYY-MM-DD'
        });
        $('#ADR_REQUEST_DATE_TO_D').datetimepicker({
            format: 'YYYY-MM-DD'
        });
    });
    $("#UPLOAD_REFERENCE_NUMBER").change(function () {
        var ref = document.getElementById("UPLOAD_REFERENCE_NUMBER").value
        console.log(ref)
        if (ref == "") {
            document.getElementById("UPLOAD_REFERENCE_NUMBER").style.setProperty('border', '1px solid red', '');
            document.getElementById("UPLOAD_REFERENCE_NUMBER_ERROR").innerHTML = "Please fill out this field."
        }
        else {
            document.getElementById("UPLOAD_REFERENCE_NUMBER").style.removeProperty('border', '1px solid red', '');
            document.getElementById("UPLOAD_REFERENCE_NUMBER_ERROR").innerHTML = ""
        }


    });
    $("#UPLOAD_COMPLAINT_NUMBER").change(function () {
        var comp = document.getElementById("UPLOAD_COMPLAINT_NUMBER").value
        console.log(comp)
        if (comp == "") {

            document.getElementById("UPLOAD_COMPLAINT_NUMBER").style.setProperty('border', '1px solid red', '');
            document.getElementById("UPLOAD_COMPLAINT_NUMBER_ERROR").innerHTML = "Please fill out this field."
        }
        else {
            document.getElementById("UPLOAD_COMPLAINT_NUMBER").style.removeProperty('border', '1px solid red', '');
            document.getElementById("UPLOAD_COMPLAINT_NUMBER_ERROR").innerHTML = ""
        }



    });
    document.getElementById("uploadcompbtn").disabled;
    document.getElementById("uploadcompbtn").style.setProperty('cursor', 'not-allowed')
    $('#formFile').bind('change', function () {
        if (this.files[0].size == 0) {
            document.getElementById("uploadcompbtn").disabled;
            document.getElementById("uploadcompbtn").style.setProperty('cursor', 'not-allowed')
            document.getElementById("UPLOAD_COMP_SIZE").innerHTML = ""
        }
        if (this.files[0].size >= 10485760) {
            document.getElementById("uploadcompbtn").disabled;
            document.getElementById("uploadcompbtn").style.setProperty('cursor', 'not-allowed')
            document.getElementById("UPLOAD_COMP_SIZE").innerHTML = " * File size must be less than 10 MB. "
        } else {
            document.getElementById("uploadcompbtn").removeAttribute('disabled');
            document.getElementById("uploadcompbtn").style.removeProperty('cursor', 'not-allowed')
            document.getElementById("UPLOAD_COMP_SIZE").innerHTML = ""
            alert("less than 10 MB")
        }


    });

    // jQuery(window).bind("resize", function () {
    //     // var browserZoomLevel = Math.round(window.devicePixelRatio * 100);.
    //     jQuery("#compjqGrid").jqGrid('setGridHeight', jQuery(window).innerHeight() * .66 - 120)



    // }).trigger("resize");



    // jQuery(window).bind("resize", function () {
    //     // var browserZoomLevel = Math.round(window.devicePixelRatio * 100);.
    //     jQuery("#addRefjqGrid").jqGrid('setGridHeight', jQuery(window).innerHeight() * .66 - 120)



    // }).trigger("resize");
    // jQuery(window).bind("resize", function () {
    //     // var browserZoomLevel = Math.round(window.devicePixelRatio * 100);.
    //     jQuery("#cusjqGrid").jqGrid('setGridHeight', jQuery(window).innerHeight() * .66 - 120)



    // }).trigger("resize");

});
// function uploadcomp(){
//     URL = "/oekg/etqMonitor/uploadcomplaintfile";

//     var FILE_W = $('#formFile').val();
//    console.log(FILE_W);

//     $.post(URL, {

//         UPLOAD_FILE: FILE_W,
//     }

//     );
// }
async function uploadFile() {
    getBuId();
    let formData = new FormData();
    formData.append("formFile", formFile.files[0]);
    let response = await fetch('/oekg/etqMonitor/uploadcomplaintfile', {
        method: "POST",
        body: formData
    });
}
function buid() {
    showLoader();
   
    var URL = "/oekg/etqMonitor/getbuid";

    var BU_NAME_W = BUNAME
  

    //Calling REST API
    $.post(URL, {
        BUSSINESS_UNIT_SITE: BU_NAME_W
        
    },
        function (response) {
            console.log("Success function")
            hideLoader();
        }

    );
};
function getBuId() {
    var URL = "/oekg/etqMonitor/getbuid";
    BUName = CryptoJS.AES.decrypt(sessionStorage.getItem('BUSINESS_UNIT'), "EMA").toString(CryptoJS.enc.Utf8);
    console.log("I_ID" + INTERFACE_DOCUMENT_ID_W);
    $.post(URL, {

        BUSSINESS_UNIT_SITE: BUName


    }, function (response) {
        var BUID = response.BUID;
        document.getElementById("BUSINESS_UNIT_SITE_ID").value = BUID;

    }

    );
}
function removemsg() {
    document.getElementById("UPLOAD_COMP_SIZE").innerHTML = "";
    document.getElementById("uploadcompbtn").setAttribute('disabled', '');
    document.getElementById("uploadcompbtn").style.setProperty('cursor', 'not-allowed')
}



function uploadformreset() {
    document.getElementById("UPLOAD_COMPLAINT_NUMBER").style.removeProperty('border', '1px solid red', '');
    document.getElementById("UPLOAD_COMPLAINT_NUMBER_ERROR").innerHTML = ""
    document.getElementById("UPLOAD_REFERENCE_NUMBER").style.removeProperty('border', '1px solid red', '');
    document.getElementById("UPLOAD_REFERENCE_NUMBER_ERROR").innerHTML = ""
}

function externalActions() {
    var action = document.getElementById("EXTERNAL_ACTION").value;

    console.log(action);

    if (action == "GENERATE_REPORT") {
        var fName = document.getElementById("COMP_FORM");
        fName.action = "/oekg/etqMonitor/excelview";
        fName.method = "GET";
        fName.submit();
        action = "null";
    }
    if (action == "UPDATE_ERROR_LOG") {
        $("#uploadcompmodal").modal('show');
        loadDataToUploadGrid();
        getUploadData();
    }
    if (action == "UPLOAD_COMPLAINTS") {
        $("#uploadcomplaintfile").modal('show');

    }
}
function hidecol(rowid) {

    var URL = "/oekg/etqMonitor/getActivityData";
    var currentRowData = $("#compjqGrid").jqGrid("getRowData", rowid);
    var INTERFACE_DOCUMENT_ID_W = currentRowData.INTERFACE_DOCUMENT_ID;
    var BATCH_ID_W = currentRowData.BATCH_ID;
    console.log("I_ID" + INTERFACE_DOCUMENT_ID_W);
    $.post(URL, {
        // CONFIG_ID: CONFIG_ID_W,
        INTERFACE_DOCUMENT_ID: INTERFACE_DOCUMENT_ID_W,
        BATCH_ID: BATCH_ID_W

    }, function (response) {
        console.log(response.Data.length)
        dataLength = response.Data.length;
        dataActivityList = response.Data;
        console.log(dataActivityList)
        $("#compjqGrid").jqGrid('GridUnload');
        loadDataToCompGrid(dataActivityList);
        jQuery('#compjqGrid').jqGrid('setGridParam', {
            data: dataActivityList
        });
        jQuery("#compjqGrid").jqGrid('hideCol', ["BATCH_ID", "ACTIVITY_NUMBER"]);
        jQuery("#compjqGrid").jqGrid('showCol', ["ACTIVITY_DETAILS"]);
        jQuery('#compjqGrid').trigger('reloadGrid');

    }

    );
}

function hidecolCust(rowid) {

    var URL = "/oekg/etqMonitor/getContactData";
    var currentRowData = $("#cusjqGrid").jqGrid("getRowData", rowid);
    var INTERFACE_DOCUMENT_ID_W = currentRowData.INTERFACE_DOCUMENT_ID
    var BATCH_ID_W = currentRowData.BATCH_ID;
    console.log("I_ID" + INTERFACE_DOCUMENT_ID_W);
    $.post(URL, {
        // CONFIG_ID: CONFIG_ID_W,
        INTERFACE_DOCUMENT_ID: INTERFACE_DOCUMENT_ID_W,
        BATCH_ID: BATCH_ID_W

    }, function (response) {
        console.log(response)
        dataLength = response.Data.length;
        dataActivityList = response.Data;
        console.log(dataActivityList)
        $("#cusjqGrid").jqGrid('GridUnload');
        loadDataToCusGrid(dataActivityList);
        jQuery('#cusjqGrid').jqGrid('setGridParam', {
            data: dataActivityList
        });
        jQuery("#cusjqGrid").jqGrid('hideCol', ["BATCH_ID", "ACTIVITY_NUMBER", "CONTACT_NUMBER"]);
        jQuery("#cusjqGrid").jqGrid('showCol', ["CONTACT_DETAILS"]);
        jQuery('#cusjqGrid').trigger('reloadGrid');

    }

    );
}

function getDataLength(rowid) {

    var URL = "/oekg/etqMonitor/getActivityData";
    var currentRowData = $("#compjqGrid").jqGrid("getRowData", rowid);
    console
    var INTERFACE_DOCUMENT_ID_W = currentRowData.INTERFACE_DOCUMENT_ID
    console.log("I_ID" + INTERFACE_DOCUMENT_ID_W);
    $.post(URL, {
        // CONFIG_ID: CONFIG_ID_W,
        INTERFACE_DOCUMENT_ID: INTERFACE_DOCUMENT_ID_W

    }, function (response) {
        console.log(response.Data.length);
        return response.Data.length;
    }

    );
}

function getUploadData() {
    jQuery('#uploadjqGrid').jqGrid('clearGridData');
    $("#uploadjqGrid").closest(".ui-jqgrid").find('.loading').show();
    var URL = "/oekg/etqMonitor/getAllErrorLogData";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {
            $("#uploadjqGrid").closest(".ui-jqgrid").find('.loading').hide();
            dataList = response.Data;
            $("#uploadjqGrid").jqGrid('GridUnload');
            loadDataToUploadGrid(dataList);
            jQuery('#uploadjqGrid').jqGrid('setGridParam', {
                data: dataList
            });
            jQuery('#uploadjqGrid').trigger('reloadGrid');
        }
    }

    );
};
function getCompData() {
    // $('#COM_UPLOAD_STATUS').val('4'); 
    showLoader();
    INTER_NAME = 'COMPLAINT';
    document.getElementById('COM_UPLOAD_STATUS').value = '4';
    jQuery('#compjqGrid').jqGrid('clearGridData');
    $("#compjqGrid").closest(".ui-jqgrid").find('.loading').show();
    var URL = "/oekg/etqMonitor/getAllCompData";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {
            $("#compjqGrid").closest(".ui-jqgrid").find('.loading').hide();
            dataList = response.Data;
            $("#compjqGrid").jqGrid('GridUnload');
            loadDataToCompGrid(dataList);
            jQuery('#compjqGrid').jqGrid('setGridParam', {
                data: dataList
            });
            jQuery('#compjqGrid').trigger('reloadGrid');
            hideLoader();
        }
    }

    );
};

function requestStatus() {
    showLoader();
    var URL = "/oekg/etqMonitor/getRequestStatus";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {

            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#COM_UPLOAD_STATUS').append('<option value="' + value.REQUEST_STATUS_ID + '">' + value.REQUEST_STATUS + '</option>');
                $('#CUS_UPLOAD_STATUS').append('<option value="' + value.REQUEST_STATUS_ID + '">' + value.REQUEST_STATUS + '</option>');
                $('#ADR_UPLOAD_STATUS').append('<option value="' + value.REQUEST_STATUS_ID + '">' + value.REQUEST_STATUS + '</option>');
                hideLoader();
            });
        },
        
    });

}

function getCusData() {
    document.getElementById('CUS_UPLOAD_STATUS').value = '4';
    INTER_NAME = 'CUSTOMER';
    jQuery('#cusjqGrid').jqGrid('clearGridData');
    $("#cusjqGrid").closest(".ui-jqgrid").find('.loading').show();
    var URL = "/oekg/etqMonitor/getAllCusData";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {
            $("#cusjqGrid").closest(".ui-jqgrid").find('.loading').hide();
            dataCusList = response.Data;
            console.log(dataCusList);
            $("#cusjqGrid").jqGrid('GridUnload');
            loadDataToCusGrid(dataCusList);
            $('#cusjqGrid').trigger('reloadGrid');
            jQuery('#cusjqGrid').jqGrid('setGridParam', {
                data: dataCusList
            });
            jQuery('#cusjqGrid').trigger('reloadGrid');
        }
    }

    );
};

function getAddRefData() {
    document.getElementById('ADR_UPLOAD_STATUS').value = '4';
    INTER_NAME = 'ADDITIONAL REFERENCE';
    jQuery('#addRefjqGrid').jqGrid('clearGridData');
    $("#addRefjqGrid").closest(".ui-jqgrid").find('.loading').show();
    var URL = "/oekg/etqMonitor/getAllAddRefData";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {
            $("#addRefjqGrid").closest(".ui-jqgrid").find('.loading').hide();
            dataAddRefList = response.Data;
            $("#addRefjqGrid").jqGrid('GridUnload');
            loadDataToAddRefGrid(dataAddRefList);
            $('#addRefjqGrid').trigger('reloadGrid');
            jQuery('#addRefjqGrid').jqGrid('setGridParam', {
                data: dataAddRefList
            });
            jQuery('#addRefjqGrid').trigger('reloadGrid');
        }
    }

    );
};

function searchgetData() {
    showLoader();
    console.log(INTER_NAME);
    var URL;
    var upload_status = document.getElementById('COM_UPLOAD_STATUS').value;
    console.log(upload_status);
    if (upload_status === '1') {
        URL = "/oekg/etqMonitor/getPendingCompData";
    } else {
        URL = "/oekg/etqMonitor/getSearchCompData";
    }
    jQuery('#compjqGrid').jqGrid('clearGridData');
    $("#compjqGrid").closest(".ui-jqgrid").find('.loading').show();
    console.log(URL);
    var INTERFACE_NAME_W = INTER_NAME
    var REFERENCE_NUMBER_W = $('#COM_REFERENCE_NUMBER').val();
    var ETQ_UPLOAD_STATUS_W = $('#COM_UPLOAD_STATUS').val();
    var DEBUG_LEVEL_W = $('#COM_DEBUG_LEVEL').val();
    var REQUEST_DATE_FROM_W = $('#COM_REQUEST_DATE_FROM').val();
    var REQUEST_DATE_TO_W = $('#COM_REQUEST_DATE_TO').val();
    var BATCH_ID_W = $('#COM_BATCH').val();
    if (BATCH_ID_W === "") {
        BATCH_ID_W = 0;
    }
    // Calling REST API
    $.post(URL, {
        // CONFIG_ID: CONFIG_ID_W,

        REFERENCE_NUMBER: REFERENCE_NUMBER_W,
        ETQ_UPLOAD_STATUS: ETQ_UPLOAD_STATUS_W,
        DEBUG_LEVEL: DEBUG_LEVEL_W,
        REQUEST_DATE_FROM: REQUEST_DATE_FROM_W,
        REQUEST_DATE_TO: REQUEST_DATE_TO_W,
        BATCH_ID: BATCH_ID_W,
        INTERFACE_NAME: INTERFACE_NAME_W

    }, function (response) {
        $("#compjqGrid").closest(".ui-jqgrid").find('.loading').hide();
        dataList = response.Data;
        $("#compjqGrid").jqGrid('GridUnload');
        loadDataToCompGrid(dataList);
        jQuery('#compjqGrid').jqGrid('setGridParam', {
            data: dataList
        });
        jQuery('#compjqGrid').trigger('reloadGrid');
        hideLoader();
    }

    );
};

function uploadconfirm() {
    var REFERENCE_NUMBER_W = $('#UPLOAD_REFERENCE_NUMBER').val();
    var COMPLAINT_NUMBER_W = $('#UPLOAD_COMPLAINT_NUMBER').val();

    if (REFERENCE_NUMBER_W == "") {
        document.getElementById("UPLOAD_REFERENCE_NUMBER").style.setProperty('border', '1px solid red', '');
        document.getElementById("UPLOAD_REFERENCE_NUMBER_ERROR").innerHTML = "Please fill out this field."
    }
    else {
        document.getElementById("UPLOAD_REFERENCE_NUMBER").style.removeProperty('border', '1px solid red', '');
        document.getElementById("UPLOAD_REFERENCE_NUMBER_ERROR").innerHTML = ""
    }
    if (COMPLAINT_NUMBER_W == "") {
        document.getElementById("UPLOAD_COMPLAINT_NUMBER").style.setProperty('border', '1px solid red', '');
        document.getElementById("UPLOAD_COMPLAINT_NUMBER_ERROR").innerHTML = "Please fill out this field."
    }
    else {
        document.getElementById("UPLOAD_COMPLAINT_NUMBER").style.removeProperty('border', '1px solid red', '');
        document.getElementById("UPLOAD_COMPLAINT_NUMBER_ERROR").innerHTML = ""
    }
    if (REFERENCE_NUMBER_W != "" && COMPLAINT_NUMBER_W != "") {
        document.getElementById("UPLOAD_CONFIRM_MSG").innerHTML = "Do you want to reprocess the System Source Number: <b>" + REFERENCE_NUMBER_W + "</b> with Complaint Number: <b>" + COMPLAINT_NUMBER_W + "</b>";
        $("#uploadalertmodal").modal('show');
    }


}


function uploadErrorData() {

    var REFERENCE_NUMBER_W = $('#UPLOAD_REFERENCE_NUMBER').val();
    var COMPLAINT_NUMBER_W = $('#UPLOAD_COMPLAINT_NUMBER').val();

    URL = "/oekg/etqMonitor/updateErrorLogData";

    jQuery('#uploadjqGrid').jqGrid('clearGridData');
    $("#uploadjqGrid").closest(".ui-jqgrid").find('.loading').show();
    console.log(URL);

    var REFERENCE_NUMBER_W = $('#UPLOAD_REFERENCE_NUMBER').val();
    var COMPLAINT_NUMBER_W = $('#UPLOAD_COMPLAINT_NUMBER').val();

    // Calling REST API
    $.post(URL, {
        // CONFIG_ID: CONFIG_ID_W,

        REFERENCE_NUMBER: REFERENCE_NUMBER_W,
        COMPLAINT_NUMBER: COMPLAINT_NUMBER_W,


    }, function (response) {
        $("#uploadjqGrid").closest(".ui-jqgrid").find('.loading').hide();
        dataList = response.Data;
        $("#uploadjqGrid").jqGrid('GridUnload');
        loadDataToUploadGrid(dataList);
        jQuery('#uploadjqGrid').jqGrid('setGridParam', {
            data: dataList
        });
        jQuery('#uploadjqGrid').trigger('reloadGrid');
    }

    );

};

function searchgetCusData() {
    showLoader();
    jQuery('#cusjqGrid').jqGrid('clearGridData');
    $("#cusjqGrid").closest(".ui-jqgrid").find('.loading').show();
    var URL;
    var upload_status = document.getElementById('CUS_UPLOAD_STATUS').value;
    console.log(upload_status);
    if (upload_status === '1') {
        URL = "/oekg/etqMonitor/getPendingCompData";
    } else {
        URL = "/oekg/etqMonitor/getSearchCusData";
    }

    var INTERFACE_NAME_W = INTER_NAME;
    var REFERENCE_NUMBER_W = $('#CUS_COMPLAINT_ACCOUNT_NUMBER').val();
    var ETQ_UPLOAD_STATUS_W = $('#CUS_UPLOAD_STATUS').val();
    var DEBUG_LEVEL_W = $('#CUS_DEBUG_LEVEL').val();
    var REQUEST_DATE_FROM_W = $('#CUS_REQUEST_DATE_FROM').val();
    var REQUEST_DATE_TO_W = $('#CUS_REQUEST_DATE_TO').val();
    var BATCH_ID_W = $('#CUS_BATCH').val();
    if (BATCH_ID_W === "") {
        BATCH_ID_W = 0;
    }
    // Calling REST API
    $.post(URL, {
        // CONFIG_ID: CONFIG_ID_W,

        REFERENCE_NUMBER: REFERENCE_NUMBER_W,
        ETQ_UPLOAD_STATUS: ETQ_UPLOAD_STATUS_W,
        DEBUG_LEVEL: DEBUG_LEVEL_W,
        REQUEST_DATE_FROM: REQUEST_DATE_FROM_W,
        REQUEST_DATE_TO: REQUEST_DATE_TO_W,
        BATCH_ID: BATCH_ID_W,
        INTERFACE_NAME: INTERFACE_NAME_W

    }, function (response) {
        $("#cusjqGrid").closest(".ui-jqgrid").find('.loading').hide();
        dataCusList = response.Data;
        $("#cusjqGrid").jqGrid('GridUnload');
        loadDataToCusGrid(dataCusList);
        jQuery('#cusjqGrid').jqGrid('setGridParam', {
            data: dataCusList
        });
        jQuery('#cusjqGrid').trigger('reloadGrid');
        hideLoader();
    }

    );
};

function searchgetAddRefData() {
    jQuery('#addRefjqGrid').jqGrid('clearGridData');
    $("#addRefjqGrid").closest(".ui-jqgrid").find('.loading').show();
    var URL;
    var upload_status = document.getElementById('ADR_UPLOAD_STATUS').value;
    console.log(upload_status);
    if (upload_status === '1') {
        URL = "/oekg/etqMonitor/getPendingCompData";
    } else {
        URL = "/oekg/etqMonitor/getSearchAddRefData";
    }

    var INTERFACE_NAME_W = INTER_NAME;
    var REFERENCE_NUMBER_W = $('#ADR_REFERENCE_NUMBER').val();
    var ETQ_UPLOAD_STATUS_W = $('#ADR_UPLOAD_STATUS').val();
    var DEBUG_LEVEL_W = $('#ADR_DEBUG_LEVEL').val();
    var REQUEST_DATE_FROM_W = $('#ADR_REQUEST_DATE_FROM').val();
    var REQUEST_DATE_TO_W = $('#ADR_REQUEST_DATE_TO').val();
    var BATCH_ID_W = $('#ADR_BATCH').val();
    if (BATCH_ID_W === "") {
        BATCH_ID_W = 0;
    }
    // Calling REST API
    $.post(URL, {
        // CONFIG_ID: CONFIG_ID_W,        
        REFERENCE_NUMBER: REFERENCE_NUMBER_W,
        ETQ_UPLOAD_STATUS: ETQ_UPLOAD_STATUS_W,
        DEBUG_LEVEL: DEBUG_LEVEL_W,
        REQUEST_DATE_FROM: REQUEST_DATE_FROM_W,
        REQUEST_DATE_TO: REQUEST_DATE_TO_W,
        BATCH_ID: BATCH_ID_W,
        INTERFACE_NAME: INTERFACE_NAME_W

    }, function (response) {
        $("#addRefjqGrid").closest(".ui-jqgrid").find('.loading').hide();
        dataAddRefList = response.Data;
        $("#addRefjqGrid").jqGrid('GridUnload');
        loadDataToAddRefGrid(dataAddRefList);
        jQuery('#addRefjqGrid').jqGrid('setGridParam', {
            data: dataAddRefList
        });
        jQuery('#addRefjqGrid').trigger('reloadGrid');
    }

    );
};

function loadDataToCompGrid(dataList) {
    $("#compjqGrid")
        .jqGrid({
            data: dataList,
            datatype: "local",
            colNames: ['DEBUG SEQ', 'DEBUG LEVEL', 'BATCH', 'COMPLAINT NUMBER',
                'REFERENCE NUMBER', 'SYSTEM SOURCE', 'REFERENCE NUM2',
                'SYSTEM SOURCE2', 'ETQ UPLOAD STATUS', 'UPLOAD STATUS', 'UPLOAD MESSAGE', 'PROCESS DATE',
                'INTERFACE DOCUMENT ID', 'INTERFACE NAME',
                'REQ FOR', 'COMPLAINT INTERFACE ID', 'BUSINESS UNIT ID',
                'ATTRIBUTE1', 'ATTRIBUTE2', 'ATTRIBUTE3',
                'ATTRIBUTE4', 'ATTRIBUTE5', 'ATTRIBUTE6',
                'ATTRIBUTE7', 'ATTRIBUTE8', 'ATTRIBUTE9',
                'ATTRIBUTE10', 'ACTIVITY NUMBER', 'ACTIVITY DETAILS'
            ],

            colModel: [{
                name: 'DEBUG_SEQ',
                index: 'DEBUG_SEQ',
                // width : 55
                hidden: true
            },
            {
                // name and index must be same
                // must be similar to console
                // must be similar to console
                name: 'DEBUG_LEVEL',
                index: 'DEBUG_LEVEL',
                width: 150,
                hidden: true
            },
            {
                name: 'BATCH_ID',
                index: 'BATCH_ID',
                // width : 350,
                width: 80,
                sortable: false
            },
            {
                name: 'ETQ$NUMBER',
                index: 'ETQ$NUMBER',
                // width : 350,
                width: 120,
                align: 'center',
                sortable: false,
                formatter: function (cellValue, option, rowdata) {
                    console.log(rowdata.UPLOAD_STATUS)
                    if (rowdata.UPLOAD_STATUS == "Success") {
                        return rowdata.ETQ$NUMBER
                    } else {

                        return '-';

                    }

                }
            },
            {
                name: 'REFERENCE_NUM',
                index: 'REFERENCE_NUM',
                // width : 350,
                width: 120,
                sortable: false
            },
            {
                name: 'SYSTEM_SOURCE',
                index: 'SYSTEM_SOURCE',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'REFERENCE_NUM2',
                index: 'REFERENCE_NUM2',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'SYSTEM_SOURCE2',
                index: 'SYSTEM_SOURCE2',
                // width : 350,
                hidden: true,
                sortable: false
            },

            {
                name: 'ETQ_UPLOAD_STATUS',
                index: 'ETQ_UPLOAD_STATUS',
                width: 100,
                hidden: true,
                sortable: false
            },
            {
                name: 'UPLOAD_STATUS',
                index: 'UPLOAD_STATUS',
                width: 90,
                align: 'center',
                sortable: false,
                formatter: function (cellValue, option, rowdata) {
                    if (rowdata.UPLOAD_STATUS === '1' || rowdata.UPLOAD_STATUS === '0' ||  rowdata.UPLOAD_STATUS == null) {
                        return "Pending"
                    } else {
                        return rowdata.UPLOAD_STATUS;
                    }

                }
            },
            {
                name: 'ETQ_UPLOAD_MESSAGE',
                index: 'ETQ_UPLOAD_MESSAGE',
                width: 280,
                sortable: false
            },
            {
                name: 'ETQ_UPLOADED_DATE',
                index: 'ETQ_UPLOADED_DATE',
                formatter: "date",
                align: 'center',
                formatoptions: {
                    srcformat: 'ISO8601Long',
                    newformat: 'd-m-y H:i:s'
                },
                width: 100
            },

            {
                name: 'INTERFACE_DOCUMENT_ID',
                index: 'INTERFACE_DOCUMENT_ID',
                // width : 350,
                hidden: true,
                sortable: false
            },

            {
                name: 'INTERFACE_NAME',
                index: 'INTERFACE_NAME',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'REQ_FOR',
                index: 'REQ_FOR',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'COMPLAINT_INTERFACE_ID',
                index: 'COMPLAINT_INTERFACE_ID',
                width: 165,
                hidden: true,
                sortable: false
            },

            {
                name: 'BUSINESS_UNIT_ID',
                index: 'BUSINESS_UNIT_ID',
                // width : 350,
                hidden: true,
                sortable: false
            },

            {
                name: 'ATTRIBUTE1',
                index: 'ATTRIBUTE1',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE2',
                index: 'ATTRIBUTE2',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE3',
                index: 'ATTRIBUTE3',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE4',
                index: 'ATTRIBUTE4',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE5',
                index: 'ATTRIBUTE5',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE6',
                index: 'ATTRIBUTE6',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE7',
                index: 'ATTRIBUTE7',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE8',
                index: 'ATTRIBUTE8',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE9',
                index: 'ATTRIBUTE9',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE10',
                index: 'ATTRIBUTE10',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ACTIVITY_NUMBER',
                index: 'ACTIVITY_NUMBER',
                width: 100,
                hidden: false,
                align: 'center',
                formatter: function (cellValue, option, rowdata) {
                    console.log(rowdata.UPLOAD_STATUS)
                    if (rowdata.ACTIVITY_NUMBER != null && rowdata.UPLOAD_STATUS == "Success" && !rowdata.ACTIVITY_NUMBER.includes("null")) {
                        return "<a href='javascript:void(0);' style='text-decoration: underline; color: #3366CC; font-weight: bold' onclick=hidecol('" +
                            option.rowId + "');>Activity Details</a>"
                    } else {

                        if (rowdata.UPLOAD_STATUS == "Pending" || rowdata.UPLOAD_STATUS == "ERROR") {
                            return '-';
                        } else {
                            return "No Activity"
                        }
                    }

                }
            },
            {
                name: 'ACTIVITY_DETAILS',
                index: 'ACTIVITY_DETAILS',
                search: false,
                sort: false,
                align: 'center',
                formatter: function (cellValue, option, rowdata) {
                    return rowdata.ACTIVITY_NUMBER
                },
                width: 100,
                hidden: true
            }

            ],
            viewrecords: true,
            // rowNum: 10,
            loadonce: true,
            rowList: [10, 20, 30],
            pager: '#compjqGridPager',
            height: 'auto',
            shrinkToFit: true,
            resizable: false,
            forceFit: true,
            autowidth: true,
            loadComplete: function () {
                if ($("#compjqGrid").getGridParam("records") == 0) {
                    $("#compjqGrid").addRowData(
                        $("#compjqGrid")
                            .empty()
                            .append('<tr><td style="text-align: center">No records to display</td></tr>')
                    );
                }
            }
            // shrinkToFit: true,
            // forceFit: true,
            // hidegrid: true,
            // width: '100%',
            // height: '100%'
        });
    $($("#compjqGrid")[0].grid.hDiv).find(".ui-jqgrid-labels th.ui-th-column")
        .unbind("mouseenter")
        .unbind("mouseleave");
    $("#compjqGrid").parents('div.ui-jqgrid-bdiv').css("max-height", "370px");
    jQuery("#compjqGrid").jqGrid('navGrid', '#compjqGridPager', {
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
        caption: "Search Configuration",
        Find: "Find Configuration",
        drag: true,
        searchOnEnter: true,
        height: 260,
        width: 500
        // optDescriptions: {eq:'my eq', gt:'after', le:'on or before'}
    }

    );
}

function loadDataToCusGrid(dataCusList) {
    console.log(dataCusList);
    $("#cusjqGrid")
        .jqGrid({
            data: dataCusList,
            datatype: "local",
            colNames: ['DEBUG SEQ', 'DEBUG LEVEL', 'BATCH', 'CUSTOMER NUMBER',
                'CUSTOMER A/C NUMBER', 'SYSTEM SOURCE', 'REFERENCE NUM2',
                'SYSTEM SOURCE2', 'ETQ UPLOAD STATUS', 'UPLOAD STATUS', 'UPLOAD MESSAGE', 'PROCESS DATE',
                'INTERFACE DOCUMENT ID', 'INTERFACE NAME',
                'REQ FOR', 'COMPLAINT INTERFACE ID', 'BUSINESS UNIT ID',
                'ATTRIBUTE1', 'ATTRIBUTE2', 'ATTRIBUTE3',
                'ATTRIBUTE4', 'ATTRIBUTE5', 'ATTRIBUTE6',
                'ATTRIBUTE7', 'ATTRIBUTE8', 'ATTRIBUTE9',
                'ATTRIBUTE10', 'CONTACT DETAILS', 'CONTACT DETAILS'
            ],

            colModel: [{
                name: 'DEBUG_SEQ',
                index: 'DEBUG_SEQ',
                // width : 55
                hidden: true
            },
            {
                // name and index must be same
                // must be similar to console
                // must be similar to console
                name: 'DEBUG_LEVEL',
                index: 'DEBUG_LEVEL',
                width: 150,
                hidden: true
            },
            {
                name: 'BATCH_ID',
                index: 'BATCH_ID',
                // width : 350,
                width: 80,
                sortable: false
            },
            {
                name: 'ETQ$NUMBER',
                index: 'ETQ$NUMBER',
                // width : 350,
                width: 120,
                align: 'center',
                sortable: false
            },
            {
                name: 'REFERENCE_NUM',
                index: 'REFERENCE_NUM',
                // width : 350,
                width: 130,
                sortable: false
            },
            {
                name: 'SYSTEM_SOURCE',
                index: 'SYSTEM_SOURCE',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'REFERENCE_NUM2',
                index: 'REFERENCE_NUM2',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'SYSTEM_SOURCE2',
                index: 'SYSTEM_SOURCE2',
                // width : 350,
                hidden: true,
                sortable: false
            },

            {
                name: 'ETQ_UPLOAD_STATUS',
                index: 'ETQ_UPLOAD_STATUS',
                width: 100,
                hidden: true,
                sortable: false
            },
            {
                name: 'UPLOAD_STATUS',
                index: 'UPLOAD_STATUS',
                width: 90,
                align: 'center',
                sortable: false,
                formatter: function (cellValue, option, rowdata) {
                    if (rowdata.UPLOAD_STATUS === '1' || rowdata.UPLOAD_STATUS === '0' ||  rowdata.UPLOAD_STATUS == null) {
                        return "Pending"
                    } else {
                        return rowdata.UPLOAD_STATUS;
                    }
                }
            },
            {
                name: 'ETQ_UPLOAD_MESSAGE',
                index: 'ETQ_UPLOAD_MESSAGE',
                width: 280,
                sortable: false
            },
            {
                name: 'ETQ_UPLOADED_DATE',
                index: 'ETQ_UPLOADED_DATE',
                formatter: "date",
                align: 'center',
                formatoptions: {
                    srcformat: 'ISO8601Long',
                    newformat: 'd-m-y H:i:s'
                },
                width: 100
            },

            {
                name: 'INTERFACE_DOCUMENT_ID',
                index: 'INTERFACE_DOCUMENT_ID',
                // width : 350,
                hidden: true,
                sortable: false
            },

            {
                name: 'INTERFACE_NAME',
                index: 'INTERFACE_NAME',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'REQ_FOR',
                index: 'REQ_FOR',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'COMPLAINT_INTERFACE_ID',
                index: 'COMPLAINT_INTERFACE_ID',
                width: 165,
                hidden: true,
                sortable: false
            },

            {
                name: 'BUSINESS_UNIT_ID',
                index: 'BUSINESS_UNIT_ID',
                // width : 350,
                hidden: true,
                sortable: false
            },

            {
                name: 'ATTRIBUTE1',
                index: 'ATTRIBUTE1',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE2',
                index: 'ATTRIBUTE2',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE3',
                index: 'ATTRIBUTE3',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE4',
                index: 'ATTRIBUTE4',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE5',
                index: 'ATTRIBUTE5',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE6',
                index: 'ATTRIBUTE6',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE7',
                index: 'ATTRIBUTE7',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE8',
                index: 'ATTRIBUTE8',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE9',
                index: 'ATTRIBUTE9',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE10',
                index: 'ATTRIBUTE10',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'CONTACT_NUMBER',
                index: 'CONTACT_NUMBER',
                width: 100,
                hidden: false,
                align: 'center',
                formatter: function (cellValue, option, rowdata) {
                    if (rowdata.CONTACT_NUMBER != null) {
                        return "<a href='javascript:void(0);' style='text-decoration: underline; color: #3366CC; font-weight: bold' onclick=hidecolCust('" +
                            option.rowId + "');>Get Contact</a>"
                    } else {
                        if (rowdata.UPLOAD_STATUS === '1') {
                            return '';
                        } else {
                            return "No Contact"
                        }
                    }

                }
            },
            {
                name: 'CONTACT_DETAILS',
                index: 'CONTACT_DETAILS',
                search: false,
                sort: false,
                align: 'center',
                formatter: function (cellValue, option, rowdata) {
                    return rowdata.CONTACT_NUMBER
                },
                width: 100,
                hidden: true
            }

            ],
            viewrecords: true,
            //rowNum: 10,
            loadonce: true,
            rowList: [10, 20, 30],
            pager: '#cusjqGridPager',
            height: 'auto',
            loadComplete: function () {
                if ($("#cusjqGrid").getGridParam("records") == 0) {
                    $("#cusjqGrid").addRowData(
                        $("#cusjqGrid")
                            .empty()
                            .append('<tr><td style="text-align: center">No records to display</td></tr>')
                    );
                }
            }
        });

    $($("#cusjqGrid")[0].grid.hDiv).find(".ui-jqgrid-labels th.ui-th-column")
        .unbind("mouseenter")
        .unbind("mouseleave");
    $("#cusjqGrid").parents('div.ui-jqgrid-bdiv').css("max-height", "370px");

    jQuery("#cusjqGrid").jqGrid('navGrid', '#cusjqGridPager', {
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
        caption: "Search Configuration",
        Find: "Find Configuration",
        drag: true,
        searchOnEnter: true,
        height: 260,
        width: 500
        // optDescriptions: {eq:'my eq', gt:'after', le:'on or before'}
    }

    );
}

function loadDataToAddRefGrid(dataAddRefList) {
    $("#addRefjqGrid")
        .jqGrid({
            data: dataAddRefList,
            datatype: "local",
            colNames: ['DEBUG SEQ', 'DEBUG LEVEL', 'BATCH', 'ETQ DOCUMENT NUMBER',
                'SHESHI NUMBER', 'SYSTEM SOURCE', 'REFERENCE NUM2',
                'SYSTEM SOURCE2', 'ETQ UPLOAD STATUS', 'UPLOAD STATUS', 'UPLOAD MESSAGE', 'PROCESS DATE',
                'INTERFACE DOCUMENT ID', 'INTERFACE NAME',
                'REQ FOR', 'COMPLAINT INTERFACE ID', 'BUSINESS UNIT ID',
                'ATTRIBUTE1', 'ATTRIBUTE2', 'ATTRIBUTE3',
                'ATTRIBUTE4', 'ATTRIBUTE5', 'ATTRIBUTE6',
                'ATTRIBUTE7', 'ATTRIBUTE8', 'ATTRIBUTE9',
                'ATTRIBUTE10'
            ],

            colModel: [{
                name: 'DEBUG_SEQ',
                index: 'DEBUG_SEQ',
                // width : 55
                hidden: true
            },
            {
                // name and index must be same
                // must be similar to console
                // must be similar to console
                name: 'DEBUG_LEVEL',
                index: 'DEBUG_LEVEL',
                width: 150,
                hidden: true
            },
            {
                name: 'BATCH_ID',
                index: 'BATCH_ID',
                // width : 350,
                width: 80,
                sortable: false
            },
            {
                name: 'ETQ$NUMBER',
                index: 'ETQ$NUMBER',
                // width : 350,
                width: 120,
                align: 'center',
                sortable: false
            },
            {
                name: 'REFERENCE_NUM',
                index: 'REFERENCE_NUM',
                // width : 350,
                width: 130,
                sortable: false
            },
            {
                name: 'SYSTEM_SOURCE',
                index: 'SYSTEM_SOURCE',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'REFERENCE_NUM2',
                index: 'REFERENCE_NUM2',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'SYSTEM_SOURCE2',
                index: 'SYSTEM_SOURCE2',
                // width : 350,
                hidden: true,
                sortable: false
            },

            {
                name: 'ETQ_UPLOAD_STATUS',
                index: 'ETQ_UPLOAD_STATUS',
                width: 100,
                hidden: true,
                sortable: false
            },
            {
                name: 'UPLOAD_STATUS',
                index: 'UPLOAD_STATUS',
                width: 90,
                align: 'center',
                sortable: false,
                formatter: function (cellValue, option, rowdata) {
                    if (rowdata.UPLOAD_STATUS === '1' || rowdata.UPLOAD_STATUS === '0' ||  rowdata.UPLOAD_STATUS == null) {
                        return "Pending"
                    } else {
                        return rowdata.UPLOAD_STATUS;
                    }

                }
            },
            {
                name: 'ETQ_UPLOAD_MESSAGE',
                index: 'ETQ_UPLOAD_MESSAGE',
                width: 280,
                sortable: false
            },
            {
                name: 'ETQ_UPLOADED_DATE',
                index: 'ETQ_UPLOADED_DATE',
                formatter: "date",
                align: 'center',
                formatoptions: {
                    srcformat: 'ISO8601Long',
                    newformat: 'd-m-y H:i:s'
                },
                width: 100
            },

            {
                name: 'INTERFACE_DOCUMENT_ID',
                index: 'INTERFACE_DOCUMENT_ID',
                // width : 350,
                hidden: true,
                sortable: false
            },

            {
                name: 'INTERFACE_NAME',
                index: 'INTERFACE_NAME',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'REQ_FOR',
                index: 'REQ_FOR',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'COMPLAINT_INTERFACE_ID',
                index: 'COMPLAINT_INTERFACE_ID',
                width: 165,
                hidden: true,
                sortable: false
            },

            {
                name: 'BUSINESS_UNIT_ID',
                index: 'BUSINESS_UNIT_ID',
                // width : 350,
                hidden: true,
                sortable: false
            },

            {
                name: 'ATTRIBUTE1',
                index: 'ATTRIBUTE1',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE2',
                index: 'ATTRIBUTE2',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE3',
                index: 'ATTRIBUTE3',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE4',
                index: 'ATTRIBUTE4',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE5',
                index: 'ATTRIBUTE5',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE6',
                index: 'ATTRIBUTE6',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE7',
                index: 'ATTRIBUTE7',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE8',
                index: 'ATTRIBUTE8',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE9',
                index: 'ATTRIBUTE9',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'ATTRIBUTE10',
                index: 'ATTRIBUTE10',
                // width : 350,
                hidden: true,
                sortable: false
            }
                // {
                //     name: 'ACTIVITY_DETAILS',
                //     index: 'ACTIVITY_DETAILS',
                //     search: false,
                //     sort: false,
                //     align: 'center',
                //     formatter: function(cellValue, option,
                //         rowdata) {
                //         return "<a href='javascript:void(0);' title='Edit' data-toggle='modal' data-target='#edit-configuration-modal'><i class='fa fa-pencil' aria-hidden='true' onclick=editConfigurationRow('" +
                //             option.rowId + "');></i></a>"
                //     },
                //     width: 100
                // },

            ],
            viewrecords: true,
            // rowNum: 10,
            loadonce: true,
            rowList: [10, 20, 30],
            pager: '#addRefjqGridPager',
            height: 'auto',
            loadComplete: function () {
                if ($("#addRefjqGrid").getGridParam("records") == 0) {
                    $("#addRefjqGrid").addRowData(
                        $("#addRefjqGrid")
                            .empty()
                            .append('<tr><td style="text-align: center">No records to display</td></tr>')
                    );
                }
            }
        });

    $("#addRefjqGrid").parents('div.ui-jqgrid-bdiv').css("max-height", "370px");
    $($("#addRefjqGrid")[0].grid.hDiv).find(".ui-jqgrid-labels th.ui-th-column")
        .unbind("mouseenter")
        .unbind("mouseleave");
    jQuery("#addRefjqGrid").jqGrid('navGrid', '#addRefjqGridPager', {
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
        caption: "Search Configuration",
        Find: "Find Configuration",
        drag: true,
        searchOnEnter: true,
        height: 260,
        width: 500
        // optDescriptions: {eq:'my eq', gt:'after', le:'on or before'}
    }

    );
}

function checkBox() {
    if (document.getElementById("isactive").checked) {
        document.getElementById('isactivehidden').disabled = true;
    }
}

function editCheckBox() {
    if (document.getElementById("EDIT_IS_ACTIVE").checked) {
        document.getElementById('EDIT_IS_ACTIVE_HIDDEN').disabled = true;
    }
}

function editConfigurationRow(rowid) {


    var currentRowData = $("#compjqGrid").jqGrid("getRowData", rowid);
    var parts = currentRowData.START_DATE.split('-');
    // Please pay attention to the month (parts[1]); JavaScript counts months from 0:
    // January - 0, February - 1, etc.
    var mydate = new Date(parts[2], parts[1] - 1, parts[0]);
    console.log(mydate.toDateString());

    $(function () {

        var startPart = $("#EDIT_START_DATE").val().split('-');
        var selDate = new Date(startPart[2], startPart[1] - 1, startPart[0]);
        var curdate = new Date().setHours(0, 0, 0, 0);

        $('#EDIT_START_DATE_D').datetimepicker({
            format: 'DD-MM-YYYY',
            minDate: mydate

        });

        if (selDate.getTime() >= curdate) {
            $('#EDIT_END_DATE_D').datetimepicker({
                format: 'DD-MM-YYYY',
                useCurrent: false,
                minDate: moment(mydate).add(1, 'days')

            });
        } else {
            $('#EDIT_END_DATE_D').datetimepicker({
                format: 'DD-MM-YYYY',
                useCurrent: false,
                minDate: moment()
            });
        }

        $("#EDIT_START_DATE_D").on("dp.change", function (e) {
            if (selDate.getTime() >= curdate) {
                $('#EDIT_END_DATE_D').data("DateTimePicker").minDate(moment(e.date).add(1, 'days'));
            } else {
                $('#EDIT_END_DATE_D').data("DateTimePicker").minDate(moment());
            }
        });


    });
    $('#EDIT_CONFIG_ID').val(currentRowData.CONFIG_ID);
    $('#EDIT_CONFIG_NAME').val(currentRowData.CONFIG_NAME);
    $('#EDIT_CONFIG_TYPE').val(currentRowData.CONFIG_TYPE);
    $('#EDIT_CONFIG_DESC').val(currentRowData.CONFIG_DESC);
    $('#EDIT_CONFIG_VALUE1').val(currentRowData.CONFIG_VALUE1);
    $('#EDIT_CONFIG_VALUE2').val(currentRowData.CONFIG_VALUE2);
    $('#EDIT_CONFIG_VALUE3').val(currentRowData.CONFIG_VALUE3);
    $('#EDIT_CONFIG_VALUE4').val(currentRowData.CONFIG_VALUE4);
    $('#EDIT_CONFIG_VALUE5').val(currentRowData.CONFIG_VALUE5);
    $('#EDIT_START_DATE').val(currentRowData.START_DATE);
    $('#EDIT_END_DATE').val(currentRowData.END_DATE);

    var IS_ACTIVE = currentRowData.IS_ACTIVE;
    if (IS_ACTIVE == 'Y') {
        $('#EDIT_IS_ACTIVE').attr("checked", true);
    } else if (IS_ACTIVE == 'N') {
        $('#EDIT_IS_ACTIVE').attr("checked", false);
    }

}
function loadDataToUploadGrid(dataList) {
    $("#uploadjqGrid")
        .jqGrid({
            data: dataList,
            datatype: "local",
            colNames: ['REFERENCE NUMBER', 'COMPLAINT NUMBER',
                'UPLOAD MESSAGE'
            ],

            colModel: [

                {
                    name: 'System Source Reference Number',
                    index: 'System Source Reference Number',
                    // width : 350,
                    width: 120,
                    sortable: false
                },
                {
                    name: 'Complaint Number',
                    index: 'Complaint Number',
                    // width : 350,
                    width: 120,
                    align: 'center',
                    sortable: false
                },
                {
                    name: 'EtQ Upload Message',
                    index: 'EtQ Upload Message',
                    width: 280,
                    sortable: false
                },


            ],
            viewrecords: true,
            rowNum: 10,
            loadonce: true,
            rowList: [10, 20, 30],
            pager: '#uploadjqGridPager',
            height: 'auto',

            loadComplete: function () {
                if ($("#uploadjqGrid").getGridParam("records") == 0) {
                    $("#uploadjqGrid").addRowData(
                        $("#uploadjqGrid")
                            .empty()
                            .append('<tr><td style="text-align: center">No records to display</td></tr>')
                    );
                }
            }
            // shrinkToFit: true,
            // forceFit: true,
            // hidegrid: true,
            // width: '100%',
            // height: '100%'
        });
    $($("#uploadjqGrid")[0].grid.hDiv).find(".ui-jqgrid-labels th.ui-th-column")
        .unbind("mouseenter")
        .unbind("mouseleave");
    $("#uploadjqGrid").parents('div.ui-jqgrid-bdiv').css("max-height", "250px");
    jQuery("#uploadjqGrid").jqGrid('navGrid', '#uploadjqGridPager', {
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
        caption: "Search Configuration",
        Find: "Find Configuration",
        drag: true,
        searchOnEnter: true,
        height: 260,
        width: 500
        // optDescriptions: {eq:'my eq', gt:'after', le:'on or before'}
    }

    );
}