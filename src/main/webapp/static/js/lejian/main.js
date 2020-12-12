$(document).ready(function() {
    createChart();
});

/**
 * 生成可视化数据
 * 长护险到期人数
 */
function createChart() {
    $.ajax({
        url: "/chx/deadlineCount",//这个就是请求地址对应sAjaxSource
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var count=0;
            if(result.count!=null){
                count=result.count;
            }
            var dflt = {
                min: 0,
                max: count===0?1:count*2,
                donut: true,
                gaugeWidthScale: 0.6,
                counter: true,
                hideInnerShadow: true
            };
            var gg1 = new JustGage({
                id: 'gg1',
                value: count,
                defaults: dflt
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            // alert("status:"+XMLHttpRequest.status+",readyState:"+XMLHttpRequest.readyState+",textStatus:"+textStatus);
        }
    });
}