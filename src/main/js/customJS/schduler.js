var dataList;
var scheduler_msg;
var edit_scheduler_msg, BUNAME,BUID_RES;
$(document).ready(function () {
    BUNAME = CryptoJS.AES.decrypt(sessionStorage.getItem('BUSINESS_UNIT'), "EMA").toString(CryptoJS.enc.Utf8);

    buid();
    document.getElementById('bcTitle').innerHTML = CryptoJS.AES.decrypt(sessionStorage.getItem('BUSINESS_UNIT'), "EMA").toString(CryptoJS.enc.Utf8);
    document.getElementById('CREATED_BY').value = parseInt(CryptoJS.AES.decrypt(sessionStorage.getItem('USERID'), "EMA").toString(CryptoJS.enc.Utf8));
    document.getElementById('EDIT_CREATED_BY').value = parseInt(CryptoJS.AES.decrypt(sessionStorage.getItem('USERID'), "EMA").toString(CryptoJS.enc.Utf8));
    document.getElementById('REMOVE_CREATED_BY').value = parseInt(CryptoJS.AES.decrypt(sessionStorage.getItem('USERID'), "EMA").toString(CryptoJS.enc.Utf8));

    getData();
    // AddBUList();
    // AddUserList();
    schedulerTypeList();
    AddSchedulerTypeList();
    EditSchedulerTypeList();
    interfaceNameList();
    addInterfaceNameList();
    editInterfaceNameList();
    range();
    editRange();
    addInterfaceType();
    editInterfaceType();
    // EditBUList();
    // EditUserList();
    // loadDataToGrid();    

    $(function () {
        document.getElementById("ADD_INTERFACE_NAME").setAttribute('disabled', '')

        $('#ADD_BUSINESS_UNIT').change(function () {
            if (document.getElementById("ADD_INTERFACE_NAME").value == "") {
                document.getElementById("ADD_INTERFACE_NAME").setAttribute('disabled', '')
            }
            document.getElementById("ADD_INTERFACE_NAME").removeAttribute('disabled', '')
        });
    });

    $(function () {
        $('#SCH_DIV').hide();

        $('#ADD_EVENT_TYPE').change(function () {
            if ($('#ADD_EVENT_TYPE').val() == 'Schedule') {
                document.getElementById("SCH_SCHEDULE_TYPE").setAttribute('required', '')
                document.getElementById("SCH_START_DATE").setAttribute('required', '')
                document.getElementById("SCH_START_TIME").setAttribute('required', '')
                $('#SCH_DIV').show();

            } else {
                document.getElementById("SCH_SCHEDULE_TYPE").removeAttribute('required', '')
                document.getElementById("SCH_START_DATE").removeAttribute('required', '')
                document.getElementById("SCH_START_TIME").removeAttribute('required', '')
                $('#SCH_DIV').hide();
            }
        });

        $('#EDIT_EVENT_TYPE').change(function () {
            if ($('#EDIT_EVENT_TYPE').val() == 'Schedule') {
                document.getElementById("EDIT_SCHEDULE_TYPE").setAttribute('required', '')
                document.getElementById("EDIT_START_DATE").setAttribute('required', '')
                document.getElementById("EDIT_START_TIME").setAttribute('required', '')
                $('#EDIT_SCH_DIV').show();

            } else {
                document.getElementById("EDIT_SCHEDULE_TYPE").removeAttribute('required', '')
                document.getElementById("EDIT_START_DATE").removeAttribute('required', '')
                document.getElementById("EDIT_START_TIME").removeAttribute('required', '')
                $('#EDIT_SCH_DIV').hide();
            }
        });
    });

    $("#ADD_INTERFACE_NAME").change(function () {
        var BC_name = document.getElementById("ADD_BUSINESS_UNIT").value;
        var inter_name = document.getElementById("ADD_INTERFACE_NAME").value;
        var EVENT_TYPE_W = document.getElementById("ADD_EVENT_TYPE").value;

        var INTERFACE_SELECTED_ITEMS = document.getElementById("ADD_INTERFACE_TYPE");
        if (inter_name === 'CUSTOMER' || inter_name === 'ADDITIONAL REFERENCE') {
            INTERFACE_SELECTED_ITEMS.remove(2);
            INTERFACE_SELECTED_ITEMS.remove(2);
        } else if (inter_name === 'COMPLAINT') {
            $("#ADD_INTERFACE_TYPE")
                .empty()
                .append('<option value="">Select Interface Type</option>');
            addInterfaceType();
        }

        var URL = "/oekg/scheduler/getExistSchData"
        $.post(URL, {
            BUSINESS_UNIT_SITE_ID: BC_name,
            INTERFACE_NAME: inter_name,
            EVENT_TYPE: EVENT_TYPE_W
        }, function (response) {
            dataList = response.Data;
            scheduler_msg = response.MESSAGE;
        }
        );
    });

    $("#EDIT_INTERFACE_NAME").change(function () {
        var BC_name = document.getElementById("EDIT_BUSINESS_UNIT").value;
        var inter_name = document.getElementById("EDIT_INTERFACE_NAME").value;
        var INTERFACE_SELECTED_ITEMS = document.getElementById("EDIT_INTERFACE_TYPE");
        if (inter_name === 'CUSTOMER' || inter_name === 'ADDITIONAL REFERENCE') {
            INTERFACE_SELECTED_ITEMS.remove(2);
            INTERFACE_SELECTED_ITEMS.remove(2);
        } else if (inter_name === 'COMPLAINT') {
            $("#EDIT_INTERFACE_TYPE")
                .empty()
                .append('<option value="">Select Interface Type</option>');
            editInterfaceType();
        }

        var URL = "/oekg/scheduler/getExistSchData"
        $.post(URL, {
            BUSINESS_UNIT_SITE_ID: BC_name,
            INTERFACE_NAME: inter_name,
        }, function (response) {
            dataList = response.Data;
            edit_scheduler_msg = response.MESSAGE;
            console.log(edit_scheduler_msg);
        }
        );
    });

    $(function () {
        $('#SCH_DAILY').hide();
        $('#SCH_MONTHLY').hide();
        $('#SCH_WEEKLY').hide();
        $('#SCH_SCHEDULE_TYPE').change(function () {
            $('#SCH_DAILY').hide();
            $('#SCH_MONTHLY').hide();
            $('#SCH_WEEKLY').hide();
            if ($('#SCH_SCHEDULE_TYPE').val() == 'Daily') {
                $('#SCH_DAILY').show();
            } else if ($('#SCH_SCHEDULE_TYPE').val() == 'Monthly') {
                $('#SCH_MONTHLY').show();
            } else if ($('#SCH_SCHEDULE_TYPE').val() == 'Weekly') {
                $('#SCH_WEEKLY').show();
            }
        });
    });

    // $(function () {
    //     $('#EDIT_SCH_DAILY').hide();
    //     $('#EDIT_SCH_MONTHLY').hide();
    //     $('#EDIT_SCH_WEEKLY').hide();
    //     $('#EDIT_SCH_SCHEDULE_TYPE').change(function () {
    //         $('#EDIT_SCH_DAILY').hide();
    //         $('#EDIT_SCH_MONTHLY').hide();
    //         $('#EDIT_SCH_WEEKLY').hide();
    //         if ($('#EDIT_SCHEDULE_TYPE').val() == 'Daily') {
    //             $('#EDIT_SCH_DAILY').show();
    //         } else if ($('#EDIT_SCHEDULE_TYPE').val() == 'Monthly') {
    //             $('#EDIT_SCH_MONTHLY').show();
    //         } else if ($('#EDIT_SCHEDULE_TYPE').val() == 'Weekly') {
    //             $('#EDIT_SCH_WEEKLY').show();
    //         }
    //     });
    // });

    $(function () {
        $('#START_DATE_D').datetimepicker({
            format: 'DD-MM-YYYY',
            minDate: moment()
        });
        $('#END_DATE_D').datetimepicker({
            format: 'DD-MM-YYYY',
            useCurrent: false
        });
        $("#SCH_START_TIME_D").datetimepicker({
            format: 'HH:mm'
        });
        // $("#SCH_START_DATE_D").on("dp.change", function (e) {
        //     document.getElementById("SCH_END_DATE").disabled = false;
        //     $('#SCH_END_DATE_D').data("DateTimePicker").minDate(moment(e.date).add(1, 'days'));
        // });
    });
    $('#SCH_START_DATE_D').datetimepicker({
        format: 'DD-MM-YYYY',
        minDate: moment()

    });
    $('#SCH_END_DATE_D').datetimepicker({
        format: 'DD-MM-YYYY',
        useCurrent: false


    });
    $("#EDIT_START_TIME_D").datetimepicker({
        format: 'HH:mm'
    });
    // jQuery(window).bind("resize", function () {
    //     // var browserZoomLevel = Math.round(window.devicePixelRatio * 100);.
    //     console.log(jQuery(window).innerHeight() * .71 - 120)
    //     jQuery("#schGrid").jqGrid('setGridHeight', jQuery(window).innerHeight() * .71 - 120)


    // }).trigger("resize");

});



function range() {
    var array = [...Array(31
    ).keys()].map(i => i + 1);

    $.each(array, function (index, value) {
        $('#SCH_MONTH_DAY').append('<option value="' + value + '">' + value + '</option>');
    });
}

function editRange() {
    var array = [...Array(31
    ).keys()].map(i => i + 1);

    $.each(array, function (index, value) {
        $('#EDIT_MONTH_DAY').append('<option value="' + value + '">' + value + '</option>');
    });
}
function RoleList() {
    var URL = "/oekg/scheduler/getRoleList";
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
            BUID_RES = response.BUID;
            console.log("Success function")
           
            $('#ADD_BUSINESS_UNIT').append('<option value="' + BUID_RES + '">' + BUNAME + '</option>');
            $('#EDIT_BUSINESS_UNIT').append('<option value="' + BUID_RES + '">' + BUNAME + '</option>');
           
        }

    );
};
function AddRoleList() {
    var URL = "/oekg/scheduler/getRoleList";
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
    var URL = "/oekg/scheduler/getRoleList";
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
    var URL = "/oekg/scheduler/getEmailAddressList";
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
    var URL = "/oekg/scheduler/getEmailAddressList";
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
    var URL = "/oekg/scheduler/getMappingBUList";
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
    var URL = "/oekg/scheduler/getMappingBUList";

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

function schedulerTypeList() {
    var URL = "/oekg/scheduler/getSchedulerTypeList";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {

            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#EVENT_TYPE').append('<option value="' + value.SCHEDULER_TYPE_VALUE + '">' + value.SCHEDULER_TYPE + '</option>');
            });
        },
    });

}

function AddSchedulerTypeList() {
    var URL = "/oekg/scheduler/getSchedulerTypeList";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {

            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#ADD_EVENT_TYPE').append('<option value="' + value.SCHEDULER_TYPE_VALUE + '">' + value.SCHEDULER_TYPE + '</option>');
            });
        },
    });

}

function EditSchedulerTypeList() {
    var URL = "/oekg/scheduler/getSchedulerTypeList";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {

            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#EDIT_EVENT_TYPE').append('<option value="' + value.SCHEDULER_TYPE_VALUE + '">' + value.SCHEDULER_TYPE + '</option>');
            });
        },
    });

}

function interfaceNameList() {
    var URL = "/oekg/scheduler/getInterfaceList";
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

function addInterfaceNameList() {
    var URL = "/oekg/scheduler/getInterfaceList";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {

            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#ADD_INTERFACE_NAME').append('<option value="' + value.INTERFACE_ID + '">' + value.INTERFACE_NAME + '</option>');
            });
        },
    });

}

function editInterfaceNameList() {
    var URL = "/oekg/scheduler/getInterfaceList";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {

            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#EDIT_INTERFACE_NAME').append('<option value="' + value.INTERFACE_ID + '">' + value.INTERFACE_NAME + '</option>');
            });
        },
    });

}

function addInterfaceType() {
    var URL = "/oekg/scheduler/getInterfaceTypeList";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {
            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#ADD_INTERFACE_TYPE').append('<option value="' + value.INTERFACE_TYPE_ID + '">' + value.INTERFACE_TYPE + '</option>');
            });
        },
    });

}

function editInterfaceType() {
    var URL = "/oekg/scheduler/getInterfaceTypeList";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {
            dataList = response.Data;
            $.each(dataList, function (index, value) {
                $('#EDIT_INTERFACE_TYPE').append('<option value="' + value.INTERFACE_TYPE_ID + '">' + value.INTERFACE_TYPE + '</option>');
            });
        },
    });

}

function getData() {
    jQuery('#schGrid').jqGrid('clearGridData');
    $("#schGrid").closest(".ui-jqgrid").find('.loading').show();
    var URL = "/oekg/scheduler/getAllSchedulerData";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {
            $("#schGrid").closest(".ui-jqgrid").find('.loading').hide();
            dataList = response.Data;

            $("#schGrid").jqGrid('GridUnload');
            loadDataToGrid(dataList);
            jQuery('#schGrid').jqGrid('setGridParam', {
                data: dataList
            });
            jQuery('#schGrid').trigger('reloadGrid');
        }
    }

    );
};

function searchgetData() {
    jQuery('#schGrid').jqGrid('clearGridData');
    $("#schGrid").closest(".ui-jqgrid").find('.loading').show();
    var URL = "/oekg/scheduler/getSearchSchedulerData";


    var EVENT_TYPE_W = $('#EVENT_TYPE').val();
    var INTERFACE_NAME_W = $('#INTERFACE_NAME').val();
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
        EVENT_TYPE: EVENT_TYPE_W,
        INTERFACE_NAME: INTERFACE_NAME_W,
        START_DATE: START_DATE_W,
        END_DATE: END_DATE_W,
        IS_ACTIVE: IS_ACTIVE_W

    }, function (response) {
        $("#schGrid").closest(".ui-jqgrid").find('.loading').hide();
        dataList = response.Data;
        $("#schGrid").jqGrid('GridUnload');
        loadDataToGrid(dataList);
        jQuery('#schGrid').jqGrid('setGridParam', {
            data: dataList
        });
        jQuery('#schGrid').trigger('reloadGrid');
    }

    );
};

function loadDataToGrid(dataList) {
    $("#schGrid")
        .jqGrid({
            data: dataList,
            datatype: "local",
            colNames: ['STOP', 'SCHEDULER ID', 'BUSINESS UNIT SITE ID',
                'INTERFACE NAME', 'INTERFACE TYPE',
                'EVENT TYPE', 'SCHEDULE TYPE', 'START TIME', 'BATCH EXECUTE ON',
                'STATUS', 'MONTH DAY', 'WEEK DAY', 'START DATE',
                'END DATE', 'CREATED_BY', 'CREATION_DATE',
                'LAST_UPDATED_BY', 'LAST_UPDATE_DATE', 'BATCH', 'NO_OF_RECORD',
                'ATTRIBUTE1', 'ATTRIBUTE2', 'ATTRIBUTE3',
                'ATTRIBUTE4', 'ATTRIBUTE5', 'ATTRIBUTE6',
                'ATTRIBUTE7', 'ATTRIBUTE8', 'ATTRIBUTE9',
                'ATTRIBUTE10', 'ACTION'
            ],

            colModel: [{
                // name and index must be same
                // must be similar to console
                // must be similar to console
                name: 'STOP',
                index: 'STOP',
                search: false,
                sort: false,
                align: 'center',
                formatter: function (cellValue, option,
                    rowdata) {
                    if (rowdata.EVENT_TYPE == 'Schedule' && rowdata.STATUS == "ACTIVE") {
                        return "<a href='javascript:void(0);' title='Edit'data-toggle='modal' data-target='#alertmodal'><i class='fa fa-hand-stop-o' aria-hidden='true' onclick=removeScheduler('" +
                            option.rowId + "');></i></a>"
                    } else if (rowdata.EVENT_TYPE == 'Immediate') {
                        return '';
                    }
                    else {
                        return '';
                    }

                },
                width: 50
            },
            {
                name: 'SCHEDULER_ID',
                index: 'SCHEDULER_ID',
                width: 120,
                align: 'center',
                hidden: false
            },

            {
                // name and index must be same
                // must be similar to console
                // must be similar to console
                name: 'BUSINESS_UNIT_SITE_ID',
                index: 'BUSINESS_UNIT_SITE_ID',
                width: 150,
                hidden: true
            },
            {
                name: 'INTERFACE_NAME',
                index: 'INTERFACE_NAME',
                width: 140

            },
            {
                name: 'INTERFACE_TYPE',
                index: 'INTERFACE_TYPE',
                width: 120,
                align: 'center',
                sortable: false,
                hidden: false
            },
            {
                name: 'EVENT_TYPE',
                index: 'EVENT_TYPE',
                align: 'center',
                width: 100,
                sortable: false
            },
            {
                name: 'SCHEDULE_TYPE',
                index: 'SCHEDULE_TYPE',
                // width : 350,
                sortable: false,
                hidden: true
            },
            {
                name: 'START_TIME',
                index: 'START_TIME',
                // width : 350,
                sortable: false,
                hidden: true
            },
            {
                name: 'BATCH_EXECUTE_ON',
                index: 'BATCH_EXECUTE_ON',
                // width : 350,
                sortable: false,
                hidden: true
            },
            {
                name: 'STATUS',
                index: 'STATUS',
                align: 'center',
                width: 80,
                sortable: false,
                hidden: false
            },
            {
                name: 'MONTH_DAY',
                index: 'MONTH_DAY',
                // width : 350,
                sortable: false,
                hidden: true
            },
            {
                name: 'WEEK_DAY',
                index: 'WEEK_DAY',
                // width : 350,
                sortable: false,
                hidden: true
            },
            {
                name: 'START_DATE',
                index: 'START_DATE',
                width: 150,
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
                width: 150,
                sortable: false,
                align: 'center',
                formatter: "date",
                formatoptions: {
                    "srcformat": "Y-m-d H:i:s",
                    "newformat": "d-m-Y H:i:s",
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
                name: 'BATCH',
                index: 'BATCH',
                // width : 350,
                hidden: true,
                sortable: false
            }, {
                name: 'NO_OF_RECORD',
                index: 'NO_OF_RECORD',
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
                name: 'Action',
                index: 'Action',
                search: false,
                sort: false,
                align: 'center',
                formatter: function (cellValue, option,
                    rowdata) {
                    if (rowdata.EVENT_TYPE == 'Schedule' && rowdata.STATUS == "ACTIVE") {
                        return "<a href='javascript:void(0);' title='Edit' data-toggle='modal' data-target='#update-sch-modal'><i class='fa fa-pencil' aria-hidden='true' onclick=editSchedulerRow('" +
                            option.rowId + "');></i></a>"
                    } else if (rowdata.EVENT_TYPE == 'Immediate') {
                        return '';
                    } else {
                        return '';
                    }

                },
                width: 70
            }
            ],
            // rowNum: 15,
            loadonce: false,
            rowList: [10, 20, 30],
            pager: '#schPager',
            viewrecords: true,
            height: 'auto',
            caption: "Schedule",
            loadComplete: function () {
                if ($("#schGrid").getGridParam("records") == 0) {
                    $("#schGrid").addRowData(
                        $("#schGrid")
                            .empty()
                            .append('<tr><td style="text-align: center">No records to display</td></tr>')
                    );
                }
            }
        });
    $("#schGrid").parents('div.ui-jqgrid-bdiv').css("max-height", "400px");
    jQuery("#schGrid").jqGrid('navGrid', '#schPager', {
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
        caption: "Search Scheduler",
        Find: "Find Scheduler",
        drag: true,
        searchOnEnter: true,
        height: 260,
        width: 500
        // optDescriptions: {eq:'my eq', gt:'after', le:'on or before'}
    }

    );
}

function editSchedulerRow(rowid) {

    var currentRowData = $("#schGrid").jqGrid("getRowData", rowid);
    var parts1 = currentRowData.START_DATE.split(' ');
    var parts = parts1[0].split('-');
    var mydate = new Date(parts[2], parts[1] - 1, parts[0]);

    var EVENT_TYPE_E = currentRowData.EVENT_TYPE
    if (EVENT_TYPE_E == 'Immediate') {
        $('#EDIT_SCH_DIV').hide();
    } else if (EVENT_TYPE_E == 'Schedule') {
        $('#EDIT_SCH_DIV').show();
    }

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

    $(function () {
        var storedFormValues = currentRowData.WEEK_DAY;
        $.each(storedFormValues.split(","), function (intIndex, objValue) {
            $("#EDIT_WEEK_DAY" + objValue).attr("checked", "true");
        });
    })

    if (currentRowData.EVENT_TYPE == 'Schedule') {

        $('#EDIT_SCH_DIV').show();

        if (currentRowData.SCHEDULE_TYPE == 'Daily') {
            $('#EDIT_SCH_MONTHLY').hide();
            $('#EDIT_SCH_WEEKLY').hide();
            $('#EDIT_SCH_DAILY').show();
        } else if (currentRowData.SCHEDULE_TYPE == 'Monthly') {
            $('#EDIT_SCH_WEEKLY').hide();
            $('#EDIT_SCH_DAILY').hide();
            $('#EDIT_SCH_MONTHLY').show();
        } else if (currentRowData.SCHEDULE_TYPE == 'Weekly') {
            console.log(currentRowData.SCHEDULE_TYPE);
            $('#EDIT_SCH_DAILY').hide();
            $('#EDIT_SCH_MONTHLY').hide();
            $('#EDIT_SCH_WEEKLY').show();
        }
    }

    $(function () {
        if (currentRowData.EVENT_TYPE == 'Schedule') {
            $('#EDIT_SCH_DIV').show();
            $('#EDIT_SCHEDULE_TYPE').change(function () {
                $('#EDIT_SCH_DAILY').hide();
                $('#EDIT_SCH_MONTHLY').hide();
                $('#EDIT_SCH_WEEKLY').hide();
                if ($('#EDIT_SCHEDULE_TYPE').val() == 'Daily') {
                    console.log("Daily")
                    $('#EDIT_SCH_DAILY').show();
                } else if ($('#EDIT_SCHEDULE_TYPE').val() == 'Monthly') {
                    console.log("Monthly")
                    $('#EDIT_SCH_MONTHLY').show();
                } else if ($('#EDIT_SCHEDULE_TYPE').val() == 'Weekly') {
                    console.log("Weekly")
                    $('#EDIT_SCH_WEEKLY').show();
                }
            });
        }
    });


    $('#EDIT_SCHEDULE_ID').val(currentRowData.SCHEDULER_ID);
    $('#EDIT_EVENT_TYPE').val(currentRowData.EVENT_TYPE);

    var BUID = currentRowData.BUSINESS_UNIT_SITE_ID;
    document.getElementById('EDIT_BUSINESS_UNIT').value = BUID;

    var month_day = currentRowData.MONTH_DAY;
    document.getElementById("EDIT_MONTH_DAY").value = month_day;

    console.log(currentRowData.INTERFACE_TYPE);

    $('#EDIT_INTERFACE_NAME').val(currentRowData.INTERFACE_NAME);
    $('#EDIT_INTERFACE_TYPE').val(currentRowData.INTERFACE_TYPE);
    $('#EDIT_NO_OF_BATCHES').val(currentRowData.BATCH);
    $('#EDIT_NO_OF_RECORDS').val(currentRowData.NO_OF_RECORD);
    $('#EDIT_SCHEDULE_TYPE').val(currentRowData.SCHEDULE_TYPE);
    $('#EDIT_BATCH_EXECUTE_ON').val(currentRowData.BATCH_EXECUTE_ON);
    $('#EDIT_WEEK DAY').val(currentRowData.WEEK_DAY);
    $('#EDIT_START_TIME').val(currentRowData.START_TIME);
    $('#EDIT_START_DATE').val(parts1[0]);
    $('#EDIT_END_DATE').val(currentRowData.END_DATE);
}

function checkBox() {
    console.log("checkBox");
    var array = [];
    var sch_val = document.getElementById("SCH_SCHEDULE_TYPE").value;
    if (sch_val === "Weekly") {
        $("input:checkbox[name=WEEK_DAY]:checked").each(function () {
            array.push($(this).val());
        });

        var chk_st = array.toString();
        document.getElementById("WEEK_DAY").innerHTML = chk_st;
    }
    $("#ADD_SCH_FORM").submit(function (event) {
        if (scheduler_msg === "Scheduler Data Exist") {
            console.log("inside prevent")
            $("#schalertmodal").modal('show');
            event.preventDefault();
        }
    });
    console.log("checkBox");
}

function editcheckbox() {
    // console.log("checkBox");
    // var array = [];
    // var sch_val = document.getElementById("SCH_SCHEDULE_TYPE").value;
    // if (sch_val === "Weekly") {
    //     $("input:checkbox[name=WEEK_DAY]:checked").each(function () {
    //         array.push($(this).val());
    //     });

    //     var chk_st = array.toString();
    //     document.getElementById("WEEK_DAY").innerHTML = chk_st;
    // }
    $("#EDIT_SCH_FORM").submit(function (event) {
        if (edit_scheduler_msg === "Scheduler Data Exist") {
            console.log("inside prevent")
            $("#schalertmodal").modal('show');
            event.preventDefault();
        }
    });
    // console.log("checkBox");
}
var sch_Id = null;
function removeScheduler(rowid) {
    console.log("inside remove schedule")
    var currentRowData = $("#schGrid").jqGrid("getRowData", rowid);
    $('#REMOVE_SCHEDULE_ID').val(currentRowData.SCHEDULER_ID);
    $('#REMOVE_SCHEDULE_INTERFACE').val(currentRowData.INTERFACE_NAME);
}


// function removesch(){
//     var URL = "/oekg/scheduler/removeScheduler";
//     var URL1 ="/getAllSchedulerData"

//     $.post(URL, {
//         SCHEDULE_ID: sch_Id,
//     });
//     jQuery('#schGrid').jqGrid('clearGridData');
//     var URL1 = "/getAllSchedulerData";
//     $.ajax({
//         type: "GET",
//         url: URL1,
//         success: function (response) {
//             dataList = response.Data;

//             $("#schGrid").jqGrid('GridUnload');
//             loadDataToGrid(dataList);
//             jQuery('#schGrid').jqGrid('setGridParam', {
//                 data: dataList
//             });
//             jQuery('#schGrid').trigger('reloadGrid');
//         }
//     }

//     );
