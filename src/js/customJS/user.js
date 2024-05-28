var dataList ,BUNAME;
function showLoader() {
	$(".loader-box").css('display', 'block');
}

function hideLoader() {
	$(".loader-box").css('display', 'none');
}
$(document).ready(function () {

    BUNAME = CryptoJS.AES.decrypt(sessionStorage.getItem('BUSINESS_UNIT'), "EMA").toString(CryptoJS.enc.Utf8);
    document.getElementById('bcTitle').innerHTML = CryptoJS.AES.decrypt(sessionStorage.getItem('BUSINESS_UNIT'), "EMA").toString(CryptoJS.enc.Utf8);
    document.getElementById('CREATED_BY').value = parseInt(CryptoJS.AES.decrypt(sessionStorage.getItem('USERID'), "EMA").toString(CryptoJS.enc.Utf8));
    document.getElementById('EDIT_CREATED_BY').value = parseInt(CryptoJS.AES.decrypt(sessionStorage.getItem('USERID'), "EMA").toString(CryptoJS.enc.Utf8));
    getUserData();
    buid();
    
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
            minDate: moment(),
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
    $(function () {
        $('#PASSWORD_DIV').hide();

        $('#ADD_USER_TYPE').change(function () {
            if ($('#ADD_USER_TYPE').val() == 'NON-LDAP') {
                document.getElementById("ADD_PASSWORD").setAttribute('required', '')
                $('#PASSWORD_DIV').show();

            } else if ($('#ADD_USER_TYPE').val() == 'LDAP') {
                document.getElementById("ADD_PASSWORD").removeAttribute('required', '')
                document.getElementById("ADD_PASSWORD").value = '';
                $('#PASSWORD_DIV').hide();
            }
        });
    });
    $(function () {
        $('#EDIT_PASSWORD_DIV').hide();

        $('#EDIT_USER_TYPE').change(function () {
            if ($('#EDIT_USER_TYPE').val() == 'NON-LDAP') {
                document.getElementById("EDIT_PASSWORD").setAttribute('required', '')
                $('#EDIT_PASSWORD_DIV').show();

            } else if ($('#EDIT_USER_TYPE').val() == 'LDAP') {
                document.getElementById("EDIT_PASSWORD").removeAttribute('required', '')
                document.getElementById("EDIT_PASSWORD").value = '';
                $('#EDIT_PASSWORD_DIV').hide();
            }
        });
    });
    // jQuery(window).bind("resize", function () {
    //     // var browserZoomLevel = Math.round(window.devicePixelRatio * 100);.
    //     console.log(jQuery(window).innerHeight() * .71 - 120)
    //     jQuery("#usergrid").jqGrid('setGridHeight', jQuery(window).innerHeight() * .71 - 120)


    // }).trigger("resize");

});
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
function getUserData() {
    showLoader();
    jQuery('#usergrid').jqGrid('clearGridData');
    $("#usergrid").closest(".ui-jqgrid").find('.loading').show();
    var URL = "/oekg/user/getAllUserData";
    $.ajax({
        type: "GET",
        url: URL,
        success: function (response) {
            $("#usergrid").closest(".ui-jqgrid").find('.loading').hide();
            dataList = response.Data;
            $("#usergrid").jqGrid('GridUnload');
            loadDataToGrid(dataList);
            jQuery('#usergrid').jqGrid('setGridParam', {
                data: dataList
            });
            jQuery('#usergrid').trigger('reloadGrid');
            hideLoader();
        }
    }

    );
};

function searchgetData() {
    jQuery('#usergrid').jqGrid('clearGridData');
    $("#usergrid").closest(".ui-jqgrid").find('.loading').show();
    var URL = "/oekg/user/getSearchUserData";

    var USER_NAME_W = $('#USER_NAME').val();
    var START_DATE_W = $('#START_DATE').val();
    var END_DATE_W = $('#END_DATE').val();
    //IS ACTIVE
    var IS_ACTIVE_W;
    var IS_ACTIVE_YES = $('#SEARCH_IS_ACTIVE').prop('checked');
    if (IS_ACTIVE_YES == true) {
        IS_ACTIVE_W = 'Y';
    } else {
        IS_ACTIVE_W = 'N';
    }

    //Calling REST API
    $.post(URL, {
        USER_NAME: USER_NAME_W,
        START_DATE: START_DATE_W,
        END_DATE: END_DATE_W,
        IS_ACTIVE: IS_ACTIVE_W
    },
        function (response) {
            $("#usergrid").closest(".ui-jqgrid").find('.loading').hide();
            dataList = response.Data;
            $("#usergrid").jqGrid('GridUnload');
            loadDataToGrid(dataList);
            jQuery('#usergrid').jqGrid('setGridParam', {
                data: dataList
            });
            jQuery('#usergrid').trigger('reloadGrid');
        }

    );
};

function loadDataToGrid(dataList) {
    $("#usergrid")
        .jqGrid(
            {
                data: dataList,
                datatype: "local",
                colNames: ['USER ID', 'USER DISPLAY NAME', 'EMAIL ADDRESS',
                    'USER_TYPE', 'PASSWORD', 'START DATE', 'END DATE', 'IS ACTIVE ',
                    'CREATED BY', 'CREATION DATE', 'LAST UPDATED BY', 'LAST UPDATE DATE',
                    'ATTRIBUTE1', 'ATTRIBUTE2', 'ATTRIBUTE3', 'ATTRIBUTE4', 'ATTRIBUTE5',
                    'ATTRIBUTE6', 'ATTRIBUTE7', 'ATTRIBUTE8', 'ATTRIBUTE9', 'ATTRIBUTE10',
                    'ACTION'],

                colModel: [
                    {
                        name: 'USER_ID',
                        index: 'USER_ID',
                        // width : 55
                        hidden: true
                    },

                    {
                        // name and index must be
                        // same
                        // must be similar to
                        // console
                        // must be similar to
                        // console
                        name: 'USER_NAME',
                        index: 'USER_NAME',
                        width: 100
                    },

                    {
                        name: 'EMAIL_ADDRESS',
                        index: 'EMAIL_ADDRESS',
                        width: 120

                    },
                    {

                        name: 'USER_TYPE',
                        index: 'USER_TYPE',
                        width: 100,
                        hidden: true
                    },
                    {
                        name: 'PASSWORD',
                        index: 'PASSWORD',
                        width: 120,
                        hidden: true,


                    },

                    {
                        name: 'START_DATE',
                        index: 'START_DATE',
                        width: 90,
                        formatter: "date",
                        formatoptions: {
                            "srcformat": "Y-m-d",
                            "newformat": "d-m-Y",
                            userLocalTime: true
                        },
                        sortable: false,
                        align: 'center'
                    },
                    {
                        name: 'END_DATE',
                        index: 'END_DATE',
                        width: 90,
                        formatter: "date",
                        formatoptions: {
                            "srcformat": "Y-m-d",
                            "newformat": "d-m-Y",
                            userLocalTime: true
                        },
                        sortable: false,
                        align: 'center'
                    },
                    {
                        name: 'IS_ACTIVE',
                        index: 'IS_ACTIVE',
                        width: 70,
                        sortable: false,
                        align: 'center'
                    },
                    {
                        name: 'CREATED_BY',
                        index: 'CREATED_BY',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'CREATION_DATE',
                        index: 'CREATION_DATE',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'LAST_UPDATED_BY',
                        index: 'LAST_UPDATED_BY',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'LAST_UPDATE_DATE',
                        index: 'LAST_UPDATE_DATE',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'ATTRIBUTE1',
                        index: 'ATTRIBUTE1',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'ATTRIBUTE2',
                        index: 'ATTRIBUTE2',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'ATTRIBUTE3',
                        index: 'ATTRIBUTE3',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'ATTRIBUTE4',
                        index: 'ATTRIBUTE4',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'ATTRIBUTE5',
                        index: 'ATTRIBUTE5',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'ATTRIBUTE6',
                        index: 'ATTRIBUTE6',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'ATTRIBUTE7',
                        index: 'ATTRIBUTE7',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'ATTRIBUTE8',
                        index: 'ATTRIBUTE8',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'ATTRIBUTE9',
                        index: 'ATTRIBUTE9',
                        // width : 55
                        hidden: true
                    },
                    {
                        name: 'ATTRIBUTE10',
                        index: 'ATTRIBUTE10',
                        // width : 55
                        hidden: true
                    },

                    {
                        name: 'ACTION',
                        index: 'ACTION',
                        search: false,
                        sort: false,
                        align: 'center',
                        formatter: function (
                            cellValue, option,
                            rowdata) {
                            return "<a href='javascript:void(0);' title='Edit' data-toggle='modal' data-target='#edit-user-modal'><i class='fa fa-pencil' aria-hidden='true' onclick=editRoleRow('"
                                + option.rowId
                                + "');></i></a>"
                        },
                        width: 70
                    }],
                // rowNum: 12,
                loadonce: false,
                rowList: [10, 20, 30],
                pager: '#userPager',
                viewrecords: true,
                height: 'auto',
                caption: "User",
                scrollerbar: true,
                loadComplete: function () {
                    if ($("#usergrid").getGridParam("records") == 0) {
                        $("#usergrid").addRowData(
                            $("#usergrid")
                                .empty()
                                .append('<tr><td style="text-align: center">No records to display</td></tr>')
                        );
                    }
                }
            });
    $("#usergrid").parents('div.ui-jqgrid-bdiv').css("max-height", "400px");
    jQuery("#usergrid").jqGrid('navGrid', '#userPager', {
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
        caption: "Search User",
        Find: "Find User",
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

function editRoleRow(rowid) {

    var currentRowData = $("#usergrid").jqGrid("getRowData", rowid);
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


    if(currentRowData.IS_ACTIVE =='N'){
        $('#EDIT_PASSWORD_DIV').show();
    }
   
    $('#EDIT_USER_ID').val(currentRowData.USER_ID);
    $('#EDIT_USER_NAME').val(currentRowData.USER_NAME);
    $('#EDIT_EMAIL_ADDRESS').val(currentRowData.EMAIL_ADDRESS);
    $('#EDIT_START_DATE').val(currentRowData.START_DATE);
    $('#EDIT_END_DATE').val(currentRowData.END_DATE);
    $('#EDIT_USER_TYPE').val(currentRowData.USER_TYPE);
    // $('#EDIT_PASSWORD').val((currentRowData.PASSWORD));
    var IS_ACTIVE = currentRowData.IS_ACTIVE;
    if (IS_ACTIVE == 'Y') {
        $('#EDIT_IS_ACTIVE').attr("checked", true);
    } else if (IS_ACTIVE == 'N') {
        $('#EDIT_IS_ACTIVE').attr("checked", false);
    }

}
