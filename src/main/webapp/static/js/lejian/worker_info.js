$(document).ready(function(){
    var wid=getQueryVariable("id");
    loadWorkerInfo(wid);
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
        success: function (result) {
            var data = result.workerVo;
            $("[dataField]").each(function () {
                var field = $(this).attr("dataField");
                var value =eval("data."+field);
                $(this).text(value);
            });
        }
    });
}
