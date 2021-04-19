var saveUrl;
var id;
$(document).ready(function(){
    id=getQueryVariable("id");
    if(id!=null){
        $(".ibox-title h5").html("编辑");
        loadInfo(id);
        saveUrl="/location/edit"
    }else{
        $(".ibox-title h5").html("添加");
        saveUrl="/location/add";
    }
});


function loadInfo(id) {
    $.ajax({
        url: "/location/getById",
        data :JSON.stringify({
            "id":id
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var data = result.locationVoList[0];
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




function save() {
    var param={};
    $("[name]").each(function () {
        if ($(this).val()!== null && $(this).val().length>0) {
        var condition = "param." + $(this).attr("name") + "='" + $(this).val()+"'";
        eval(condition);
        }
    });
    if(id!=null){
        param.id=id;
    }

    $.ajax({
        url: saveUrl,
        data :JSON.stringify({
            "locationParam":param
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            if(result.success!==null && result.success!==undefined){
                alert("保存成功");
                location.reload();
            }else{
                alert("保存失败");
            }
        }
    });
}
