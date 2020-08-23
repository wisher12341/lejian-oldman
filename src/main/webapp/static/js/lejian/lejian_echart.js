var color=["#c23531","rgb(108, 189, 252)"];


/**
 *
 * @param title
 * @param data {“key”:xx,"value":{“key”:xx,"value":xx}}
 * @param obj
 * @param callback
 */
function createBarChartWithLegend(title,data,obj,callback) {
    var bar= echarts.init(obj);
    var yAxis_data=[];
    var series_data=[];
    var legend_data=[];
    var i=0;
    for(key1 in data){
        yAxis_data[i]=key1;
        var j=0;
        for(key2 in data[key1]){
            if(series_data[j]==null || series_data[j].length<=0){
                series_data[j]=[];
            }
            series_data[j][i]=data[key1][key2];
            if(i==0){
                legend_data[j]=key2;
            }
            j++;
        }
        i++
    }
    // console.info(JSON.stringify(legend_data));
    // console.info(JSON.stringify(series_data));
    var series=[];
    for(var k=0;k<series_data.length;k++){
        series[k]={
            name:legend_data[k],
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
                    fontSize: 26
                }
            },
            data: series_data[k]
        }
    }

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
        legend:{
            right: 10,
            top: 3,
            bottom: 20,
            textStyle: { //图例文字的样式
                color: '#fff',
                fontSize: 12
            },
            data:legend_data
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
            triggerEvent:true,
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
        series: series,
        color:color
    };
    bar.off('click');

    bar.on('click', function (params) {
        if(params.componentType =="yAxis" || params.componentType =="xAxis" ) {
            var name = params.value;
            callback(name);
        }

    });
    bar.setOption(option,true);
}

/**
 *
 * @param title
 * @param data {"key":xx,"value":xx}
 * @param obj
 * @param callback
 */
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
            triggerEvent:true,
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
    bar.off('click');
    bar.on('click', function (params) {
        if(params.componentType =="yAxis" || params.componentType =="xAxis" ) {
            var name = params.value;
            callback(name);
        }

    });
    bar.setOption(option,true);
}

function createPieChart(title,data,obj,callback){
    var legend= {
        itemWidth: 10,  // 设置大小
        itemHeight: 10,
        itemGap: 5, // 设置间距
        icon: "circle", //设置形状
        right: 10,
        top: 3,
        bottom: 20,
        orient: 'vertical',
        textStyle: { //图例文字的样式
            color: '#fff',
            fontSize: 10
        }
    };
    createPieChartWithLegend(title,data,obj,callback,legend)
}

function createPieChartWithLegend(title,data,obj,callback,lengend) {
    var legend_data=[];
    var series_data=[];
    var j=-1;
    for(var i=0;i<data.length;i++){
        // if(i%2===0){
        //     j++;
        //     legend_data[j]=[];
        // }
        legend_data[i]=data[i].key;
        series_data[i]={name:data[i].key,value:data[i].value};
    }


    var pie= echarts.init(obj);
    lengend.data=legend_data;
    var option = {
        title: {
            text:title,
            textStyle:{
                color:'#fff',
                fontSize:12,
                fontWeight:'normal'
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: lengend,
        series: [
            {
                name: '人数',
                type: 'pie',
                radius: '55%',
                center: ['45%', '50%'],
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

    pie.off('click');
    pie.on('click', function (params) {
        var name = params.name;
        console.info(name);

        callback(title,name);

    });

    pie.setOption(option,true);

}