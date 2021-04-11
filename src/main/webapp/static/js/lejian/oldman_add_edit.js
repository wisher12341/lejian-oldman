var saveUrl;
var oid;
$(document).ready(function(){
    $(".chosen-select").chosen();

    loadArea("上海","areaCountry");
    loadOldmanEnumInfo();
    loadAllLocationInfo();
    oid=getQueryVariable("oid");
    if(oid!=null){
        $(".ibox-title h5").html("编辑老人");
        loadOldmanInfo(oid);
        saveUrl="/oldman/edit"
    }else{
        saveUrl="/oldman/add";
    }


    $('.locationId').select2({
        ajax: {
            url: '/location/getLocationByPage',

            type: 'POST',

            dataType: 'json',
            contentType: "application/json;charset=UTF-8",

            placeholder: '直接选择或搜索选择',

            data: function (val) {
                return JSON.stringify({
                    "pageParam": {
                        "pageNo": 0,
                        "pageSize": 50
                    },
                    "locationSearchParam": {
                        "desc": val.term
                    }
                });

            },

            processResults: function (data) {
                var options = new Array();

                $(data.locationVoList).each(function (i, v) {
                    options.push({
                        id: v.id,

                        text: v.desc

                    });

                });

                return {
                    results: options

                };

            }

        }

    });
});

/**
 * 调用百度地图 加载行政单位
 * ak是 1881783686账号的
 */
function loadArea(key,doc) {
    var sub_admin =1;
    if (key==="上海"){
        sub_admin = 2;
    }
    $.ajax({
        url: 'http://api.map.baidu.com/api_region_search/v1/?keyword='+key+'&sub_admin='+sub_admin+'&ak=q26hMcHcdSmP4RMfDKuZYBo13FAmf4j3',
        type: 'get',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        sync:true,
        success: function (result) {
            var data;
            if (sub_admin==2){
                data = result.districts[0].districts[0].districts;
            }else{
                data = result.districts[0].districts;
            }
            for(var i=0;i<data.length;i++){
                var option = "<option value='" + data[i].name + "'>" + data[i].name + "</option>";
                $("select[name='"+doc+"']").append(option);
            }
            $("select[name='"+doc+"']").trigger("chosen:updated");
            /**
             * 动态添加的元素 后面js是获取不到的， 要绑定一个事件后面才能获取到
             */
            $("select[name='"+doc+"']").on("click","option",function(){});
        }
    });
}

/**
 * 加载所有地图坐标信息
 */
function loadAllLocationInfo() {
    $.ajax({
        url: "/location/search",
        type: 'post',
        dataType: 'json',
        data: JSON.stringify({
            "areaCustomOne": $("input[name='areaCustomOne']").val()
        }),
        async: false,
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var positions = result.locationVoList;
            for(var i=0;i<positions.length;i++){
                var option = "<option value='" + positions[i].id + "'>" + positions[i].desc + "</option>";
                $("select[name='locationId']").append(option);
            }
            $("select[name='locationId']").trigger("chosen:updated");
            /**
             * 动态添加的元素 后面js是获取不到的， 要绑定一个事件后面才能获取到
             */
            $("select[name='locationId']").on("click","option",function(){});
        }
    });

}

function loadOldmanInfo(oid) {
    $.ajax({
        url: "/oldman/getOldmanByOid",
        data :JSON.stringify({
            "oid":oid
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        sync:true,
        success: function (result) {
            var data = result.oldmanVo;
            $("[name]").each(function () {
                var field = $(this).attr("name");
                var value =eval("data."+field);
                var tagType=$(this).prop("tagName");
                if(tagType==="INPUT"){
                    $(this).val(value);
                }
                if(tagType==="SELECT"){
                    if (field === "areaTown"){
                        $(this).append("<option value='"+value+"'>"+value+"</option>");
                        $(this).trigger("chosen:updated");
                    }
                    if(field==="locationId"){
                        var obj = $("<option value='"+data.locationId+"' selected>"+data.locationDesc+"</option>");
                        $(this).append(obj)
                    }else{
                        $.each($(this).find("option"), function() {
                            if ($(this).html() == value) {
                                $(this).attr("selected", "selected");
                                var className = $(this).parent().attr("class")+"";
                                // console.info(className);
                                if(className.indexOf("chosen-select")!==-1){
                                    $(this).trigger("chosen:updated");
                                }
                            }
                        });
                    }
                }
            });
        }
    });
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
            for(var key in result.sex){
                var option="<option value='"+key+"'>"+result.sex[key]+"</option>";
                $("select[name='sex']").append(option)
            }
            for(var key in result.politics){
                var option="<option value='"+key+"'>"+result.politics[key]+"</option>";
                $("select[name='politics']").append(option)
            }
            for(var key in result.education){
                var option="<option value='"+key+"'>"+result.education[key]+"</option>";
                $("select[name='education']").append(option)
            }
            for(var key in result.householdType){
                var option="<option value='"+key+"'>"+result.householdType[key]+"</option>";
                $("select[name='householdType']").append(option)
            }
            for(var key in result.family){
                var option="<option value='"+key+"'>"+result.family[key]+"</option>";
                $("select[name='family']").append(option)
            }
            for(var key in result.serviceStatus){
                var option="<option value='"+key+"'>"+result.serviceStatus[key]+"</option>";
                $("select[name='serviceStatus']").append(option)
            }
        }
    });
}


function submitOldman() {
    var param={};
    $("[name]").each(function () {
        if ($(this).val()!== null && $(this).val().length>0) {
        var condition = "param." + $(this).attr("name") + "='" + $(this).val()+"'";
        eval(condition);
        }
    });
    if(oid!=null){
        param.oid=oid;
    }

    $.ajax({
        url: saveUrl,
        data :JSON.stringify({
            "oldmanParam":param
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            if(result.success!==null){
                alert("保存成功");
                location.reload();
            }else{
                alert("保存失败");
            }
        }
    });
}


function areaChange(obj,doc) {
    loadArea($(obj).val(),doc);

}