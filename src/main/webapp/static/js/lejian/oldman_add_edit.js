var saveUrl;
var oid;
$(document).ready(function(){
    $(".chosen-select").chosen();
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
});


/**
 * 加载所有地图坐标信息
 */
function loadAllLocationInfo() {
    $.ajax({
        url: "/location/getAllLocation",
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        sync:true,
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
                    if(field==="locationId"){
                        $.each($(this).find("option"), function() {
                            if ($(this).val() == value) {
                                $(this).attr("selected", "selected");
                                $(this).trigger("chosen:updated");
                                return false;
                            }
                        });
                    }else{
                        $.each($(this).find("option"), function() {
                            if ($(this).html() == value) {
                                $(this).attr("selected", "selected");
                                if(($(this).attr("class")+"").indexOf("chosen-select")!==-1){
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