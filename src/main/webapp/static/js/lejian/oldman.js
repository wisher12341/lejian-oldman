var table;
$(document).ready(function(){
    loadOldmanEnumInfo();
    table =$(".dataTables-example").dataTable(
        {
            "sPaginationType": "full_numbers",
            "bPaginite": true,
            "bInfo": true,
            "bSort": false,
            "bFilter": false, //搜索栏
            "bStateSave": true,
            "bProcessing": true, //加载数据时显示正在加载信息
            "bServerSide": true, //指定从服务器端获取数据
            "columns":[{},{
                data:"oid"
            },{
                data:"name"
            },{
                data:"areaVillage"
            },{
                data:"areaCustomOne"
            },{
                data:"sex"
            },{
                data:"birthday"
            },{
                data:"idCard"
            }
            ],
            "columnDefs": [
                // 列样式
                {
                    "targets": [0], // 目标列位置，下标从0开始
                    "data": "oid", // 数据列名
                    "render": function(data, type, full) { // 返回自定义内容
                        return"<input type='checkbox' name='id' value='"+data+"'/>";
                    }
                },
                // 增加一列，包括删除和修改，同时将我们需要传递的数据传递到链接中
                {
                    "targets": [8], // 目标列位置，下标从0开始
                    "data": "oid", // 数据列名
                    "render": function(data, type, full) { // 返回自定义内容
                        return "<button class='btn btn-primary' onclick=newPage('"+data+"','人员详情信息','/oldmanInfo?oid="+data+"')>查看</button>" +
                            "<button class='btn btn-primary' onclick=newPage('"+data+"','编辑','/oldmanEdit?oid="+data+"')>编辑</button>" +
                            "<button class='btn btn-primary' onclick=deleteOldman('"+data+"')>删除</button>";
                    }
                }
            ],
            "sAjaxSource": "/oldman/getOldmanByPage",//这个是请求的地址
            "fnServerData": retrieveData
        });
    function retrieveData(url, aoData, fnCallback) {
        $.ajax({
            url: url,//这个就是请求地址对应sAjaxSource
            data :JSON.stringify({
                "pageParam": {
                    "pageNo": aoData.iDisplayStart / aoData.iDisplayLength,
                    "pageSize": aoData.iDisplayLength
                },
                "oldmanSearchParam":{
                    "areaCountry":$("input[name='areaCountry']").val(),
                    "areaTown":$("input[name='areaTown']").val(),
                    "areaVillage":$("input[name='areaVillage']").val(),
                    "areaCustomOne":$("input[name='areaCustomOne']").val(),
                    "name":$("input[name='name']").val(),
                    "idCard":$("input[name='idCard']").val(),
                    "serviceStatus":$("select[name='serviceStatus']").val(),
                    "createTimeStart":$("input[name='createTimeStart']").val()!=""?$("input[name='createTimeStart']").val()+" 00:00:00":$("input[name='createTimeStart']").val(),
                    "createTimeEnd":$("input[name='createTimeEnd']").val()!=""?$("input[name='createTimeEnd']").val()+" 00:00:00":$("input[name='createTimeEnd']").val()

                }
            }),
            type: 'post',
            dataType: 'json',
            contentType: "application/json;charset=UTF-8",
            success: function (result) {
                var data ={
                    "iTotalRecords":result.count,
                    "iTotalDisplayRecords":result.count,
                    "aaData":result.oldmanVoList
                };
                fnCallback(data);//把返回的数据传给这个方法就可以了,datatable会自动绑定数据的
            },
            error:function(XMLHttpRequest, textStatus, errorThrown) {
                // alert("status:"+XMLHttpRequest.status+",readyState:"+XMLHttpRequest.readyState+",textStatus:"+textStatus);
            }
        });
    }


    $('#search').click(function () {
        table.fnFilter();
    });

    var oTable=$("#editable").dataTable();
    oTable.$("td").editable("",{
        "callback":function(sValue,y){var aPos=oTable.fnGetPosition(this);oTable.fnUpdate(sValue,aPos[0],aPos[1])},
        "submitdata":function(value,settings){return{"row_id":this.parentNode.getAttribute("id"),
            "column":oTable.fnGetPosition(this)[2]}},"width":"90%","height":"100%"});

});

function deleteOldman(oid) {
    $.ajax({
        url: "/oldman/delete",//这个就是请求地址对应sAjaxSource
        data: JSON.stringify({"oid": oid}),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            table.fnFilter();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            // alert("status:"+XMLHttpRequest.status+",readyState:"+XMLHttpRequest.readyState+",textStatus:"+textStatus);
        }
    });
}


function searchReset() {
    $("#searchDiv input[type!='button']").val("");
    $("#searchDiv select option:first").prop("selected", 'selected');
    table.fnFilter();
}

/**
 * 加载枚举值
 * @param oid
 */
function loadOldmanEnumInfo() {
    $.ajax({
        url: "/enum/oldmanAdd",
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",

        sync:true,
        success: function (result) {
            for(var key in result.serviceStatus){
                var option="<option value='"+key+"'>"+result.serviceStatus[key]+"</option>";
                $("select[name='serviceStatus']").append(option)
            }
        }
    });
}

function exportOldman() {
    $.ajax({
        url: "/oldman/exportExcel",
        data :JSON.stringify({
                "areaCountry":$("input[name='areaCountry']").val(),
                "areaTown":$("input[name='areaTown']").val(),
                "areaVillage":$("input[name='areaVillage']").val(),
                "areaCustomOne":$("input[name='areaCustomOne']").val(),
                "name":$("input[name='name']").val(),
                "idCard":$("input[name='idCard']").val(),
                "serviceStatus":$("select[name='serviceStatus']").val(),
                "createTimeStart":$("input[name='createTimeStart']").val()!=""?$("input[name='createTimeStart']").val()+" 00:00:00":$("input[name='createTimeStart']").val(),
                "createTimeEnd":$("input[name='createTimeEnd']").val()!=""?$("input[name='createTimeEnd']").val()+" 00:00:00":$("input[name='createTimeEnd']").val()
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        sync:true,
        success: function (result) {
        }
    });
}