var dataList, BUNAME;

$(document).ready(function () {
    BUNAME = CryptoJS.AES.decrypt(sessionStorage.getItem('BUSINESS_UNIT'), "EMA").toString(CryptoJS.enc.Utf8);

    buid();
    document.getElementById('bcTitle').innerHTML = CryptoJS.AES.decrypt(sessionStorage.getItem('BUSINESS_UNIT'), "EMA").toString(CryptoJS.enc.Utf8);
    document.getElementById('CREATED_BY').value = parseInt(CryptoJS.AES.decrypt(sessionStorage.getItem('USERID'), "EMA").toString(CryptoJS.enc.Utf8));
    document.getElementById('EDIT_CREATED_BY').value = parseInt(CryptoJS.AES.decrypt(sessionStorage.getItem('USERID'), "EMA").toString(CryptoJS.enc.Utf8));
    getData();
    AddBUList();
    AddUserList();
    EditBUList();
    EditUserList();


    $('#ADD_EMAIL_ADDRESS').multiselect({
        enableClickableOptGroups: true,
        enableCollapsibleOptGroups: true,
        includeSelectAllOption: true,
        dropRight: true,

        enableCaseInsensitiveFiltering: true,
        onChange: function (option, checked, select) {
            document.getElementById("EMAIL_VAL").innerHTML = "";
            var email_val = document.getElementById("ADD_EMAIL_ADDRESS").value;
            if (email_val === null || email_val === "") {
                document.getElementById("EMAIL_VAL").innerHTML = "Please select an item in the list.";
                // document,getElementById("ADD_EMAIL_ADDRESS").style.borderColor="red"
                // document.getElementById("ADD_EMAIL_ADDRESS").style.border="1px solid black";

            }

        }
    });
    $('#addBtnClear').click(function () {
        document.getElementById("EMAIL_VAL").innerHTML = "";
    });
    $('#EDIT_EMAIL_ADDRESS').multiselect({
        enableClickableOptGroups: true,
        enableCollapsibleOptGroups: true,
        includeSelectAllOption: true,
        dropRight: true,
        enableCaseInsensitiveFiltering: true,
        onChange: function (option, checked, select) {
            document.getElementById("EDIT_EMAIL_VAL").innerHTML = "";
            var email_val = document.getElementById("EDIT_EMAIL_ADDRESS").value;
            if (email_val === null || email_val === "") {
                document.getElementById("EDIT_EMAIL_VAL").innerHTML = "Please select an item in the list.";
                // document,getElementById("ADD_EMAIL_ADDRESS").style.borderColor="red"
                // document.getElementById("ADD_EMAIL_ADDRESS").style.border="1px solid black";

            }

        }
    });
    $('#editBtnClear').click(function () {
        document.getElementById("EDIT_EMAIL_VAL").innerHTML = "";
    });




    $(function () {
        $('#START_DATE_D').datetimepicker({
            format: 'YYYY-MM-DD'
        });
        $('#END_DATE_D').datetimepicker({
            format: 'YYYY-MM-DD'
        });
    });
    $(function () {
        $('#ADD_START_DATE_D').datetimepicker({
            format: 'DD-MM-YYYY',
            minDate: moment()
        });
        $('#ADD_END_DATE_D').datetimepicker({
            format: 'DD-MM-YYYY',
            useCurrent: false


        });
        $("#ADD_START_DATE_D").on("dp.change", function (e) {
            document.getElementById("ADD_END_DATE").disabled = false;
            $('#ADD_END_DATE_D').data("DateTimePicker").minDate(moment(e.date).add(1, 'days'));
        });
    });
    // jQuery(window).bind("resize", function () {
    //     // var browserZoomLevel = Math.round(window.devicePixelRatio * 100);.
    //     console.log(jQuery(window).innerHeight() * .71 - 120)
    //     jQuery("#mappinggrid").jqGrid('setGridHeight', jQuery(window).innerHeight() * .71 - 120)


    // }).trigger("resize");
});

function RoleList() {
    var URL = "/oekg/mapping/getRoleList";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {

            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#ROLE_ID').append('<option value="' + value.ROLE_ID + '">' + value.ROLE + '</option>');
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
function AddRoleList() {
    var URL = "/oekg/mapping/getRoleList";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {

            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#ADD_ROLE_ID').append('<option value="' + value.ROLE_ID + '">' + value.ROLE + '</option>');
            });
        },
    });

}

function EditRoleList() {
    var URL = "/oekg/mapping/getRoleList";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {

            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#EDIT_ROLE').append('<option value="' + value.ROLE_ID + '">' + value.ROLE + '</option>');
            });
        },
    });

}

function AddUserList() {
    var URL = "/oekg/mapping/getEmailAddressList";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {

            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#ADD_EMAIL_ADDRESS').append('<option value="' + value.USER_ID + '">' + value.EMAIL_ADDRESS + '</option>');

            });

            $('#ADD_EMAIL_ADDRESS').multiselect('rebuild');
        },
    });

}

function EditUserList() {
    var URL = "/oekg/mapping/getEmailAddressList";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {

            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#EDIT_EMAIL_ADDRESS').append('<option value="' + value.USER_ID + '">' + value.EMAIL_ADDRESS + '</option>');
            });
            $('#EDIT_EMAIL_ADDRESS').multiselect('rebuild');
        },
    });

}

function AddBUList() {
    var URL = "/oekg/mapping/getMappingBUList";
    var st_email = CryptoJS.AES.decrypt(sessionStorage.getItem('EMAILADDRESS'), "EMA").toString(CryptoJS.enc.Utf8);
    $.post(URL, {

        EMAILADDRESS: st_email

    }, function (response) {
        dataList = response.Data;
        console.log(dataList)
        $.each(dataList, function (index, value) {
            $('#ADD_BUSINESS_UNIT').append('<option value="' + value.BUSINESS_UNIT_SITE_ID + '">' + value.BUSINESS_UNIT_SITE + '</option>');
        });
    }

    );


}

function EditBUList() {
    var URL = "/oekg/mapping/getMappingBUList";

    var st_email = CryptoJS.AES.decrypt(sessionStorage.getItem('EMAILADDRESS'), "EMA").toString(CryptoJS.enc.Utf8);
    $.post(URL, {

        EMAILADDRESS: st_email

    }, function (response) {
        dataList = response.Data;
        $.each(dataList, function (index, value) {
            $('#EDIT_BUSINESS_UNIT').append('<option value="' + value.BUSINESS_UNIT_SITE_ID + '">' + value.BUSINESS_UNIT_SITE + '</option>');
        });
    }
    );


}

function getData() {
    jQuery('#mappinggrid').jqGrid('clearGridData');
    $("#mappinggrid").closest(".ui-jqgrid").find('.loading').show();
    var URL = "/oekg/mapping/getAllMapingData";

    $.ajax({
        type: "GET",
        url: URL,

        success: function (response) {
            $("#mappinggrid").closest(".ui-jqgrid").find('.loading').hide();
            dataList = response.Data;

            $("#mappinggrid").jqGrid('GridUnload');
            loadDataToGrid(dataList);
            jQuery('#mappinggrid').jqGrid('setGridParam', {
                data: dataList
            });
            jQuery('#mappinggrid').trigger('reloadGrid');
        }
    }

    );
};

function searchgetData() {
    jQuery('#mappinggrid').jqGrid('clearGridData');
    $("#mappinggrid").closest(".ui-jqgrid").find('.loading').show();
    var URL = "/oekg/mapping/getSearchMappingData";


    var ROLE_ID_W = $('#ROLE_ID').val();
    var USER_NAME_W = $('#USER_NAME').val();
    var START_DATE_W = $('#START_DATE').val();
    var END_DATE_W = $('#END_DATE').val();

    var IS_ACTIVE_W;
    var IS_ACTIVE_YES = $('#SEARCH_IS_ACTIVE').prop('checked');
    if (IS_ACTIVE_YES == true) {
        IS_ACTIVE_W = 'Y';
    } else {
        IS_ACTIVE_W = 'N';
    }

    // Calling REST API
    $.post(URL, {
        ROLE_ID: ROLE_ID_W,
        USER_NAME: USER_NAME_W,
        START_DATE: START_DATE_W,
        END_DATE: END_DATE_W,
        IS_ACTIVE: IS_ACTIVE_W

    }, function (response) {
        $("#mappinggrid").closest(".ui-jqgrid").find('.loading').hide();
        dataList = response.Data;
        // $("#mappinggrid").jqGrid('')
        $("#mappinggrid").jqGrid('GridUnload');
        loadDataToGrid(dataList);
        jQuery('#mappinggrid').jqGrid('setGridParam', {
            data: dataList
        });
        jQuery('#mappinggrid').trigger('reloadGrid');
    }

    );
};

function loadDataToGrid(dataList) {
    $("#mappinggrid")
        .jqGrid({
            data: dataList,
            datatype: "local",
            colNames: ['ROLE USER	ID', 'ROLE ID', 'ROLE', 'USER ID', 'USER DISPLAY NAME',
                'EMAIL ADDRESS', 'START DATE',
                'END DATE', 'CREATED_BY', 'CREATION_DATE',
                'LAST_UPDATED_BY', 'LAST_UPDATE_DATE', 'BUSINESS UNIT SITE ID',
                'BUSINESS UNIT SITE', 'ATTRIBUTE1', 'ATTRIBUTE2', 'ATTRIBUTE3',
                'ATTRIBUTE4', 'ATTRIBUTE5', 'ATTRIBUTE6',
                'ATTRIBUTE7', 'ATTRIBUTE8', 'ATTRIBUTE9',
                'ATTRIBUTE10', 'IS ACTIVE', 'ACTION'
            ],

            colModel: [{
                name: 'ROLE_USER_ID',
                index: 'ROLE_USER_ID',
                // width : 55
                hidden: true
            },
            {
                // name and index must be same
                // must be similar to console
                // must be similar to console
                name: 'ROLE_ID',
                index: 'ROLE_ID',
                width: 150,
                hidden: true
            },
            {
                name: 'ROLE',
                index: 'ROLE',
                width: 100

            },
            {
                name: 'USER_ID',
                index: 'USER_ID',
                width: 280,
                sortable: false,
                hidden: true
            },
            {
                name: 'USER_NAME',
                index: 'USER_NAME',
                // width : 350,
                sortable: false
            },
            {
                name: 'EMAIL_ADDRESS',
                index: 'EMAIL_ADDRESS',
                // width : 350,
                sortable: false
            },
            {
                name: 'START_DATE',
                index: 'START_DATE',
                width: 90,
                sortable: false,
                align: 'center',
                formatter: "date",
                formatoptions: {
                    "srcformat": "Y-m-d",
                    "newformat": "d-m-Y",
                },
            },
            {
                name: 'END_DATE',
                index: 'END_DATE',
                width: 90,
                sortable: false,
                align: 'center',
                formatter: "date",
                formatoptions: {
                    "srcformat": "Y-m-d",
                    "newformat": "d-m-Y",
                },
            },
            {
                name: 'CREATED_BY',
                index: 'CREATED_BY',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'CREATION_DATE',
                index: 'CREATION_DATE',
                formatter: "date",
                formatoptions: {
                    "srcformat": "Y-m-d",
                    "newformat": "d-m-Y",
                },
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'LAST_UPDATED_BY',
                index: 'LAST_UPDATED_BY',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'LAST_UPDATE_DATE',
                index: 'LAST_UPDATE_DATE',
                formatter: "date",
                formatoptions: {
                    "srcformat": "Y-m-d",
                    "newformat": "d-m-Y",
                },
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'BUSINESS_UNIT_SITE_ID',
                index: 'BUSINESS_UNIT_SITE_ID',
                // width : 350,
                hidden: true,
                sortable: false
            },
            {
                name: 'BUSINESS_UNIT_SITE',
                index: 'BUSINESS_UNIT_SITE',
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
                name: 'IS_ACTIVE',
                index: 'IS_ACTIVE',
                width: 70,
                sortable: false,
                align: 'center'
            },
            {
                name: 'Action',
                index: 'Action',
                search: false,
                sort: false,
                align: 'center',
                formatter: function (cellValue, option,
                    rowdata) {
                    return "<a href='javascript:void(0);' title='Edit' data-toggle='modal' data-target='#update-mapping-modal'><i class='fa fa-pencil' aria-hidden='true' onclick=editConfigurationRow('" +
                        option.rowId + "');></i></a>"
                },
                width: 70
            }
            ],
            //rowNum: 15,
            loadonce: false,
            rowList: [10, 20, 30],
            pager: '#mappingPager',
            viewrecords: true,
            height: 'auto',
            caption: "Role & User",
            loadComplete: function () {
                if ($("#mappinggrid").getGridParam("records") == 0) {
                    $("#mappinggrid").addRowData(
                        $("#mappinggrid")
                            .empty()
                            .append('<tr><td style="text-align: center">No records to display</td></tr>')
                    );
                }
            }
        });
    $("#mappinggrid").parents('div.ui-jqgrid-bdiv').css("max-height", "400px");
    jQuery("#mappinggrid").jqGrid('navGrid', '#mappingPager', {
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

    var email_val = document.getElementById("ADD_EMAIL_ADDRESS").value;
    if (email_val === null || email_val === "") {
        document.getElementById("EMAIL_VAL").innerHTML = "Please select an item in the list."
        $(".common-multiselect").css("border", "solid 1px blue");
    }



}

function editCheckBox() {
    if (document.getElementById("EDIT_IS_ACTIVE").checked) {
        document.getElementById('EDIT_IS_ACTIVE_HIDDEN').disabled = true;
    }
    var email_val = document.getElementById("EDIT_EMAIL_ADDRESS").value;
    if (email_val === null || email_val === "") {
        document.getElementById("EDIT_EMAIL_VAL").innerHTML = "Please select an item in the list."
        $(".common-multiselect").css("border", "solid 1px blue");
    }
}

function editConfigurationRow(rowid) {


    var currentRowData = $("#mappinggrid").jqGrid("getRowData", rowid);
    var parts = currentRowData.START_DATE.split('-');
    // Please pay attention to the month (parts[1]); JavaScript counts months from 0:
    // January - 0, February - 1, etc.
    var mydate = new Date(parts[2], parts[1] - 1, parts[0]);

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
    $('#EDIT_ROLE_USER_ID').val(currentRowData.ROLE_USER_ID);

    var BUID = currentRowData.BUSINESS_UNIT_SITE_ID;
    document.getElementById('EDIT_BUSINESS_UNIT').value = BUID;

    var RoleID = currentRowData.ROLE_ID;
    document.getElementById('EDIT_ROLE').value = RoleID;

    $('#EDIT_START_DATE').val(currentRowData.START_DATE);
    $('#EDIT_END_DATE').val(currentRowData.END_DATE);

    var EmailAdd = currentRowData.USER_ID;
    document.getElementById('EDIT_EMAIL_ADDRESS').value = EmailAdd;
    $("#EDIT_EMAIL_ADDRESS").multiselect("refresh");


    var IS_ACTIVE = currentRowData.IS_ACTIVE;
    if (IS_ACTIVE == 'Y') {
        $('#EDIT_IS_ACTIVE').attr("checked", true);
    } else if (IS_ACTIVE == 'N') {
        $('#EDIT_IS_ACTIVE').attr("checked", false);
    }

}