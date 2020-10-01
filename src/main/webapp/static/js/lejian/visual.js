$(document).ready(function(){
    getConfigData();
});


function getConfigData() {
    $.ajax({
        url: "/config/getMainConfigData",
        type: 'post',
        dataType: 'json',
        sync:true,
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            for(key in result.map){
                $("input[name="+key+"]").val(result.map[key]);
            }
        }
    });
}

function save() {
    var param={};
    $("[name]").each(function () {
        var condition = "param." + $(this).attr("name") + "='" + $(this).val()+"'";
        eval(condition);
    });

    $.ajax({
        url: "/config/saveVarConfigData",
        data :JSON.stringify({
            "map":param
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