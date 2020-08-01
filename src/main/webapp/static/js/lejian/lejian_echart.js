function createBarChart(title,data,obj,callback) {
    var bar= echarts.init(obj);
    var yAxis_data=[];
    var num=[];
    for(var i=0;i<data.length;i++){
        yAxis_data[i]=data[i].key;
        num[i]={name:data[i].key,value:data[i].value};
    }

    var series={
        type: 'bar',
        stack: '总量',
        label: {
            normal: {
                show: true,
                position: 'insideLeft',
                textStyle: {
                    fontSize: 14
                }
            }
        },
        itemStyle: {
            normal: {
                color: '#6cbdfc',
                fontSize: 26
            }
        },
        data: num
    };

    var option = {
        title: {
            text:title,
            textStyle:{
                color:'#fff',
                fontSize:14,
                fontWeight:'normal'
            },
            x:'0%',
            y:'0%',
        },
        tooltip:{
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid:  {
            top:'25%',
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            show:false
        },
        yAxis: {
            type:'category',
            data: yAxis_data,
            boundaryGap:false,
            axisTick:{
                show:false
            },
            // y 轴线
            axisLine:{
                show:false,

            },
            // 分割线设置
            splitLine:{
                show:false,  //显示分割线
            },
            axisLabel:{
                textStyle: {
                    color: '#fff',
                    fontSize:12,
                    interval: 0,
                }
            }
        },
        series: series
    };

    bar.on('click', function (params) {
        var name = params.name;
        callback(name);

    });
    bar.setOption(option);
}


function createPieChart(title,data,obj,callback) {
    var legend_data=[];
    var series_data=[];
    // var j=-1;
    for(var i=0;i<data.length;i++){
        // if(i%2===0){
        //     j++;
        //     legend_data[j]=[];
        // }
        legend_data[i]=data[i].key;
        series_data[i]={name:data[i].key,value:data[i].value};
    }
    // var legend=[];
    // for(var i=0;i<legend_data.length;i++){
    //     legend[i]={
    //         icon: "circle", //设置形状
    //         itemWidth: 5,  // 设置大小
    //         itemHeight: 5,
    //         itemGap: 5, // 设置间距
    //         orient: 'horizontal',
    //         x : ((i+1)*5)+"%",
    //         y : ((i+1)*10)+"%",
    //         align: 'left',
    //         textStyle: { //图例文字的样式
    //             color: '#fff',
    //             fontSize: 10
    //         },
    //         data: legend_data[i]
    //     }
    // }


    var pie= echarts.init(obj);
    var option = {
        title: {
            text:title,
            textStyle:{
                color:'#fff',
                fontSize:14,
                fontWeight:'normal'
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
            itemWidth: 5,  // 设置大小
            itemHeight: 5,
            itemGap: 5, // 设置间距
            icon: "circle", //设置形状

            orient: 'vertical',
            align: 'right',
            textStyle: { //图例文字的样式
                color: '#fff',
                fontSize: 10
            },
            data: legend_data[i]
        },
        series: [
            {
                name: '人数',
                type: 'pie',
                radius: '55%',
                center: ['28%', '45%'],
                data: series_data,
                //去掉指示线
                label: {
                    normal: {
                        position: 'inner',
                        show : false
                    }
                },
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    pie.setOption(option);

}