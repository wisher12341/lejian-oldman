var saveUrl;
var wid;
$(document).ready(function(){
    $(".chosen-select").chosen();
    loadWorkerEnumInfo();
    wid=getQueryVariable("id");
    if(wid!=null){
        $(".ibox-title h5").html("编辑服务人员");
        loadWorkerInfo(wid);
        saveUrl="/worker/edit"
    }else{
        saveUrl="/worker/add";
    }
});


function loadWorkerInfo(wid) {
    $.ajax({
        url: "/worker/getWorkerByWid",
        data :JSON.stringify({
            "workerId":wid
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        sync:true,
        success: function (result) {
            var data = result.workerVo;
            $("[name]").each(function () {
                var field = $(this).attr("name");
                var value =eval("data."+field);
                var tagType=$(this).prop("tagName");
                if(tagType==="INPUT"){
                    $(this).val(value);
                }
                if(tagType==="SELECT"){
                    $.each($(this).find("option"), function() {
                        if ($(this).html() == value) {
                            $(this).attr("selected", "selected");
                            if(($(this).attr("class")+"").indexOf("chosen-select")!==-1){
                                $(this).trigger("chosen:updated");
                            }
                        }
                    });
                }
            });
        }
    });
}


/**
 * 加载枚举值
 * @param oid
 */
function loadWorkerEnumInfo() {
    $.ajax({
        url: "/enum/workerAdd",
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        sync:true,
        success: function (result) {
            for(var key in result.workerType){
                var option="<option value='"+key+"'>"+result.workerType[key]+"</option>";
                $("select[name='type']").append(option)
            }
            for(var key in result.sex){
                var option="<option value='"+key+"'>"+result.sex[key]+"</option>";
                $("select[name='sex']").append(option)
            }
            for(var key in result.education){
                var option="<option value='"+key+"'>"+result.education[key]+"</option>";
                $("select[name='education']").append(option)
            }
        }
    });
}


function submitWorker() {
    var param={};
    $("[name]").each(function () {
        if ($(this).val()!== null && $(this).val().length>0) {
        var condition = "param." + $(this).attr("name") + "='" + $(this).val()+"'";
        eval(condition);
        }
    });
    if(wid!=null){
        param.id=wid;
    }

    $.ajax({
        url: saveUrl,
        data :JSON.stringify({
            "workerParam":param
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