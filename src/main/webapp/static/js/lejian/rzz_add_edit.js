var saveUrl;
var id;
$(document).ready(function(){
    id=getQueryVariable("id");
    loadRzzInfo();
    if(id!=null){
        $(".ibox-title h5").html("编辑认知症");
        loadInfo(id);
        saveUrl="/rzz/edit"
    }else{
        saveUrl="/rzz/add";
    }
});


function loadRzzInfo() {
    $.ajax({
        url: "/enum/rzzAdd",
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        sync:true,
        success: function (result) {
            for(var key in result.map){
                var option="<option value='"+key+"'>"+result.map[key]+"</option>";
                $("select[name='type']").append(option)
            }
        }
    });
}

function loadInfo(id) {
    $.ajax({
        url: "/rzz/get",
        type: 'post',
        data :JSON.stringify({
            "id":id
        }),
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        sync:true,
        success: function (result) {
            var data = result.rzzVoList[0];
            $("[name]").each(function () {
                var field = $(this).attr("name");
                var value;
                if (field == "type"){
                    value = data.typeRaw;
                }else{
                    value =eval("data."+field);
                }
                var tagType=$(this).prop("tagName");
                if(tagType==="INPUT"){
                    $(this).val(value);
                }
                if(tagType==="SELECT") {
                    $.each($(this).find("option"), function () {
                        if ($(this).val() == value) {
                            $(this).attr("selected", "selected");
                        }
                    });
                }
            });
        }
    });
}


function submitRzz() {
    var param={};
    $("[name]").each(function () {
        if ($(this).val()!== null && $(this).val().length>0) {
        var condition = "param." + $(this).attr("name") + "='" + $(this).val()+"'";
        eval(condition);
        }
    });

    $.ajax({
        url: saveUrl,
        data :JSON.stringify({
            "rzzParam":param
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