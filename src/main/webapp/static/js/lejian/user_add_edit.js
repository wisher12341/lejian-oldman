var saveUrl;
var uid;
$(document).ready(function(){
    loadUserEnumInfo();
    uid=getQueryVariable("uid");
    if(uid!=null){
        $(".ibox-title h5").html("编辑账号");
        loadUserInfo(uid);
        saveUrl="/user/edit"
    }else{
        $(".ibox-title h5").html("添加账号");
        saveUrl="/user/add";
    }
});


function loadUserInfo(uid) {
    $.ajax({
        url: "/user/getUserByUid",
        data :JSON.stringify({
            "uid":uid
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var data = result.userVo;
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
                        }
                    });
                }
            });
            roleChange($("[name='role']"));
        }
    });
}

function loadUserEnumInfo() {
    $.ajax({
        url: "/enum/user",
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        sync:true,
        success: function (result) {
            for(var key in result.roleType){
                var option="<option value='"+key+"'>"+result.roleType[key]+"</option>";
                $("select[name='role']").append(option)
            }
        }
    });
}



function submitUser() {
    var param={};
    $("[name]").each(function () {
        if ($(this).val()!== null && $(this).val().length>0) {
        var condition = "param." + $(this).attr("name") + "='" + $(this).val()+"'";
        eval(condition);
        }
    });
    if(uid!=null){
        param.id=uid;
    }

    $.ajax({
        url: saveUrl,
        data :JSON.stringify({
            "userParam":param
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            console.info(result.success);
            if(result.success!==null && result.success!==undefined){
                alert("保存成功");
                location.reload();
            }else{
                alert("保存失败");
            }
        }
    });
}

function roleChange(obj) {
    //服务人员
    if($(obj).val()==="2"){
        $("#bindWorker").show();
    }else{
        $("#bindWorker").hide();
    }
}