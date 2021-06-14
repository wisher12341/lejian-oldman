var workerId;
$(document).ready(function(){
    workerId=getQueryVariable("workerId");

    $.ajax({
        url: "/worker/getWorkerByWid",
        type: 'post',
        dataType: 'json',
        data :JSON.stringify({
            "workerId":workerId
        }),
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            $("input.workerName").val(result.workerVo.name);
        }
    });


    $('.oid').select2({
        ajax: {
            url: '/oldman/getOldmanByPage',

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
                    "oldmanSearchParam": {
                        "name": val.term
                    },
                    "needCount":false
                });

            },

            processResults: function (data) {
                var options = new Array();

                $(data.oldmanVoList).each(function (i, v) {
                    options.push({
                        id: v.oid,

                        text: v.name+"-"+v.sex

                    });

                });

                return {
                    results: options

                };

            }

        }

    });
});




function submitData() {
    var param={};
    $("[name]").each(function () {
        if ($(this).val()!== null && $(this).val().length>0) {
            var value = $(this).val();
            if ($(this).attr("type")==="datetime-local"){
                value = new Date(value).getTime();
            }
            var condition = "param." + $(this).attr("name") + "='" + value+"'";
            eval(condition);
        }
    });
    param.workerId=workerId;

    $.ajax({
        url: "/worker/dispatch/add",
        data :JSON.stringify(param),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            if(result.success!==null && result.success===true){
                alert("保存成功");
                location.reload();
            }else{
                alert("保存失败");
            }
        }
    });
}