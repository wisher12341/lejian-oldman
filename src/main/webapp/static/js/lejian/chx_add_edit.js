var saveUrl;
var cid;
$(document).ready(function(){
    cid=getQueryVariable("id");
    if(cid!=null){
        $(".ibox-title h5").html("编辑长护险");
        loadChxInfo(cid);
        saveUrl="/chx/edit"
    }else{
        saveUrl="/chx/add";
    }
});


function loadChxInfo(cid) {
    $.ajax({
        url: "/chx/getChxById",
        data :JSON.stringify({
            "id":cid
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        sync:true,
        success: function (result) {
            var data = result.chxVo;
            $("[name]").each(function () {
                var field = $(this).attr("name");
                var value =eval("data."+field);
                var tagType=$(this).prop("tagName");
                if(tagType==="INPUT"){
                    $(this).val(value);
                }
            });
        }
    });
}




function submitChx() {
    var param={};
    $("[name]").each(function () {
        if ($(this).val()!== null && $(this).val().length>0) {
        var condition = "param." + $(this).attr("name") + "='" + $(this).val()+"'";
        eval(condition);
        }
    });
    if(cid!=null){
        param.id=cid;
    }

    $.ajax({
        url: saveUrl,
        data :JSON.stringify({
            "chxParam":param
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