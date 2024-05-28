var mydata = [{
        id: "1",
        invdate: "2007-10-01",
        name: "test",
        ship_via: "Shailesh",
        note: "Test Note",
        closed: "No",
        amount: "200.00",
        tax: "10.00",
        total: "210.00"
    },
    {
        id: "2",
        invdate: "2007-10-02",
        name: "test2",
        ship_via: "Shailesh",
        note: "Test Note",
        closed: "No",
        amount: "300.00",
        tax: "20.00",
        total: "320.00"
    },
    {
        id: "3",
        invdate: "2007-09-01",
        name: "test3",
        ship_via: "Vivek",
        note: "Test Note",
        closed: "No",
        amount: "400.00",
        tax: "30.00",
        total: "430.00"
    },
    {
        id: "4",
        invdate: "2007-10-04",
        name: "test",
        ship_via: "Shailesh",
        note: "Test Note",
        closed: "No",
        amount: "200.00",
        tax: "10.00",
        total: "210.00"
    },
    {
        id: "5",
        invdate: "2007-10-05",
        name: "test2",
        ship_via: "Shailesh",
        note: "Test Note",
        closed: "No",
        amount: "300.00",
        tax: "20.00",
        total: "320.00"
    },
    {
        id: "6",
        invdate: "2007-09-06",
        name: "test3",
        ship_via: "Shailesh",
        note: "Test Note",
        closed: "No",
        amount: "400.00",
        tax: "30.00",
        total: "430.00"
    },
    {
        id: "7",
        invdate: "2007-10-04",
        name: "test",
        ship_via: "Vivek",
        note: "Test Note",
        closed: "No",
        amount: "200.00",
        tax: "10.00",
        total: "210.00"
    },
    {
        id: "8",
        invdate: "2007-10-03",
        name: "test2",
        ship_via: "Shailesh",
        note: "Test Note",
        closed: "No",
        amount: "300.00",
        tax: "20.00",
        total: "320.00"
    },
    {
        id: "9",
        invdate: "2007-09-01",
        name: "test3",
        ship_via: "Disha",
        note: "Test Note",
        closed: "No",
        amount: "400.00",
        tax: "30.00",
        total: "430.00"
    },
    {
        id: "11",
        invdate: "2007-10-01",
        name: "test",
        ship_via: "Shailesh",
        note: "Test Note",
        closed: "No",
        amount: "200.00",
        tax: "10.00",
        total: "210.00"
    },
    {
        id: "12",
        invdate: "2007-10-02",
        name: "test2",
        ship_via: "Shailesh",
        note: "Test Note",
        closed: "No",
        amount: "300.00",
        tax: "20.00",
        total: "320.00"
    },
    {
        id: "13",
        invdate: "2007-09-01",
        name: "test3",
        ship_via: "Vivek",
        note: "Test Note",
        closed: "No",
        amount: "400.00",
        tax: "30.00",
        total: "430.00"
    },
    {
        id: "14",
        invdate: "2007-10-04",
        name: "test",
        ship_via: "Shailesh",
        note: "Test Note",
        closed: "No",
        amount: "200.00",
        tax: "10.00",
        total: "210.00"
    },
    {
        id: "15",
        invdate: "2007-10-05",
        name: "test2",
        ship_via: "Shailesh",
        note: "Test Note",
        closed: "No",
        amount: "300.00",
        tax: "20.00",
        total: "320.00"
    },
    {
        id: "16",
        invdate: "2007-09-06",
        name: "test3",
        ship_via: "Shailesh",
        note: "Test Note",
        closed: "No",
        amount: "400.00",
        tax: "30.00",
        total: "430.00"
    },
    {
        id: "17",
        invdate: "2007-10-04",
        name: "test",
        ship_via: "Vivek",
        note: "Test Note",
        closed: "No",
        amount: "200.00",
        tax: "10.00",
        total: "210.00"
    },
    {
        id: "18",
        invdate: "2007-10-03",
        name: "test2",
        ship_via: "Shailesh",
        note: "Test Note",
        closed: "No",
        amount: "300.00",
        tax: "20.00",
        total: "320.00"
    },
    {
        id: "19",
        invdate: "2007-09-01",
        name: "test3",
        ship_via: "Disha",
        note: "Test Note",
        closed: "No",
        amount: "400.00",
        tax: "30.00",
        total: "430.00"
    }
];

$(document).ready(function() {
    $("#jqGrid").jqGrid({
        datatype: "local",
        loadOnce: true,
        colNames: ['Inv No', 'Date', 'Client', 'Amount', 'Tax', 'Total', 'Closed', 'Ship via', 'Notes', 'Action'],
        colModel: [{
                name: 'id',
                index: 'id',
                width: 55,
                editable: false,
                editoptions: {
                    readonly: true,
                    size: 10
                }
            },
            {
                name: 'invdate',
                index: 'invdate',
                width: 80,
                editable: true,
                editoptions: {
                    size: 10
                }
            },
            {
                name: 'name',
                index: 'name',
                width: 90,
                editable: true,
                editoptions: {
                    size: 25
                }
            },
            {
                name: 'amount',
                index: 'amount',
                width: 60,
                align: "right",
                editable: true,
                editoptions: {
                    size: 10
                }
            },
            {
                name: 'tax',
                index: 'tax',
                width: 60,
                align: "right",
                editable: true,
                editoptions: {
                    size: 10
                }
            },
            {
                name: 'total',
                index: 'total',
                width: 60,
                align: "right",
                editable: true,
                editoptions: {
                    size: 10
                }
            },
            {
                name: 'closed',
                index: 'closed',
                width: 55,
                align: 'center',
                editable: true,
                edittype: "checkbox",
                editoptions: {
                    value: "Yes:No"
                }
            },
            {
                name: 'ship_via',
                index: 'ship_via',
                width: 70,
                editable: true,
                edittype: "select",
                editoptions: {
                    value: "SP:Shailesh;VP:Vivek;DP:Disha"
                }
            },
            {
                name: 'note',
                index: 'note',
                width: 100,
                sortable: false
            },
            {
                name: "action",
                formatter: "actions",
                formatoptions: {
                    editformbutton: true
                },
                width: 54,
                align: "center",
                fixed: true,
                hidedlg: true,
                resizable: false,
                sortable: false,
                search: false,
                editable: false,
                viewable: false
            }
        ],
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: '#jqGridPager',
        sortname: 'id',
        viewrecords: true,
        sortorder: "desc",
        caption: "Navigator Example",
        // editurl:"someurl.php",
        /*height: 210,
        width: 1200,*/
        shrinkToFit: false,
        resizable: false,
        forceFit: true,
        hidegrid: true,
        /*rowNum: 8,
        pager: "#" + pagerId,*/
        height: "100%",
        width: "100%",
        gridview: true
    });
    createRows();
    /*jQuery("#jqGrid").jqGrid('navGrid', '#jqGridPager', {}, //options
    	{
    		height: 280,
    		reloadAfterSubmit: false
    	}, // edit options
    	{
    		height: 280,
    		reloadAfterSubmit: false
    	}, // add options
    	{
    		reloadAfterSubmit: false
    	}, // del options
    	{
    		search: true
    	} // search options
    );*/
    jQuery("#jqGrid").jqGrid('navGrid', '#jqGridPager', {
        edit: false,
        edittitle: "Edit Post",
        width: 500,
        add: true,
        addtitle: "Add Post",
        width: 500,
        del: false,
        view: true,
        search: true
    });
});


function createRows() {
    for (var i = 0; i <= mydata.length; i++) {
        jQuery("#jqGrid").jqGrid('addRowData', i + 1, mydata[i]);
    }
}