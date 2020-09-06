$(document).ready(function(){
    $(".chosen-select").chosen();
    loadOldmanEnumInfo();
});

function loadOldmanEnumInfo() {
    $.ajax({
        url: "/enum/oldmanAdd",
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
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
            for(var key in result.familyType){
                var option="<option value='"+key+"'>"+result.familyType[key]+"</option>";
                $("select[name='familyType']").append(option)
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
    console.info(JSON.stringify(param));

    $.ajax({
        url: "/oldman/add",
        data :JSON.stringify({
            "oldmanParam":param
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            if(result.success!==null){
                alert("添加成功");
                location.reload();
            }else{
                alert("添加失败");
            }
        }
    });
}