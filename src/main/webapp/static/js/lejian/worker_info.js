$(document).ready(function(){
    var wid=getQueryVariable("wid");
    loadOldmanInfo(wid);
    if(contact=="no"){
        $("#contact").css("display","none");
    }else{
        loadContactPeople();
    }
});

function loadOldmanInfo(wid) {
    $.ajax({
        url: "/worker/getWorkerByWid",
        data :JSON.stringify({
            "workerId":wid
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var data = result.workerVo;
            $("[dataField]").each(function () {
                var field = $(this).attr("dataField");
                var value =eval("data."+field);
                $(this).text(value);
            });
        }
    });
}

function loadContactPeople() {
    var table =$(".dataTables-example").dataTable(
        {
            "sPaginationType": "full_numbers",
            "bPaginite": false,
            "bInfo": false,
            'paging':false,
            'bLengthChange': false,
            "bSort": false,
            "bFilter": false, //搜索栏
            "bStateSave": true,
            "bProcessing": true, //加载数据时显示正在加载信息
            "bServerSide": true, //指定从服务器端获取数据
            "columns":[{
                data:"name"
            },{
                data:"phone"
            },{
                data:"relation"
            },{
                data:"address"
            }
            ],
            "sAjaxSource": "/contact/getContactManByOid",//这个是请求的地址
            "fnServerData": retrieveData
        });
}

function retrieveData(url, aoData, fnCallback) {
    $.ajax({
        url: url,//这个就是请求地址对应sAjaxSource
        data :JSON.stringify({
            "oid":window.location.search.split("=")[1]
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var data ={
                "aaData":result.contactManVoList
            };
            fnCallback(data);//把返回的数据传给这个方法就可以了,datatable会自动绑定数据的
        },
        error:function(XMLHttpRequest, textStatus, errorThrown) {
            // alert("status:"+XMLHttpRequest.status+",readyState:"+XMLHttpRequest.readyState+",textStatus:"+textStatus);
        }
    });
}