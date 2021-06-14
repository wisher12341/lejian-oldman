$(document).ready(function(){
    //希望每次打开都要默认为今天日期+00:00
    //默认时间
    var myDate = new Date(), Y = myDate.getFullYear(), M = myDate.getMonth() + 1, D = myDate.getDate();
    //处理月是一位的情况
    if((M + '').length == 1){
        M = '0' + (M + '');
    }
    //处理日是一位的情况
    if((D + '').length == 1){
        D = '0' + (D + '')
    }
    var curDay = Y + '-' + M + '-' + D;


    $(".startTime").val(curDay+"T00:00:00");
    $(".endTime").val(curDay+"T23:59");
    $.ajax({
        url: "/worker/getWorkerByAccount",
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            $(".workerName").val(result.workerVo.name);
            $(".workerId").val(result.workerVo.id);
            search();
        }
    });
});


function search() {
    $.ajax({
        url: "/worker/dispatch/getByPage",
        type: 'post',
        data:JSON.stringify({
            "pageParam":{
                "pageNo":0,
                "pageSize":100
            },
            "workerDispatchSearchParam":{
                "workerId":$(".workerId").val(),
                "startTime":new Date($(".startTime").val()).getTime(),
                "endTime":new Date($(".endTime").val()).getTime()
            }
        }),
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            $("tbody").html("");
            for(var i=0;i<result.workerDispatchVoList.length;i++){
                var data = result.workerDispatchVoList[i];
                var tr = $("<tr><td>"+data.startTime+"-"+data.endTime+"</td><td>"+data.oldmanName+"</td><td>"+data.status+"</td></tr>");
                $("tbody").append(tr);
            }
        }
    });
}



