
var areaCountry,areaTown,areaVillage,areaCustomOne;
var homeServiceData,equipData,workerData;
var workerBeyond="";

/**
 * 当前展示的行政区域 级别
 */
var currentArea;


function createStaticCount(sync) {
    $.ajax({
        url: "/main/getMainStaticData",
        type: 'post',
        dataType: 'json',
        data :JSON.stringify({
            "oldmanSearchParam":{
                "areaCustomOne":areaCustomOne,
                "areaCountry":areaCountry,
                "areaTown":areaTown,
                "areaVillage":areaVillage
            },
            "birthdayLike":new Date().Format("MM-dd")
        }),
        sync:sync,
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            homeServiceData=result.homeServiceMap;
            equipData=result.equipMap;
            workerData=result.workerMap;
            $("#homeServiceNum").html(result.homeServiceCount);
            $("#equipNum").html(result.equipCount);
            $("#workerNum").html(result.workerCount);
            $("#birthdayNum").html(result.birthdayCount);

        }
    });
    // console.info("createStaticCount");

}

/**
 * 行政区域划分的各服务总人数
 */
function createAdminNumber() {
    $.ajax({
        url: "/oldman/getOldmanAreaGroupCount",
        type: 'post',
        dataType: 'json',
        data :JSON.stringify({
            "areaCountry":areaCountry,
            "areaTown":areaTown,
            "areaVillage":areaVillage
        }),
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            // console.info(JSON.stringify(result));
            var map = result.sortData;
            $("#totalNumber").html(result.sumCount);
            for(var i=0;i<map.length;i++){
                // var li="<li class='adminNumber' onclick=selectAreaCustomOne('"+map[i].first+"',this)><div class='row'>" +
                //     "<div class='col-xs-1'></div><div class='col-xs-7'><span class='word' style='font-size: large;display: inline;'>"+map[i].first+"</span></div>" +
                //     "<div class='col-xs-2' style='text-align: right'><span class='word num' style='display: inline'>"+map[i].second+"</span></div><div class='col-xs-2'></div></div></li>";
                var li="<li class='adminNumber' onclick=selectArea('"+map[i].first+"',this)>" +
                    "<div style='width: 70%;padding-left: 15%;display: inline-block'><span class='word' style='font-size: large;display: inline;'>"+map[i].first+"</span></div>" +
                    "<div style='width: 30%;display: inline-block'><span class='word num' style='display: inline'>"+map[i].second+"</span></div></li>";

                $("#adminNumber").append(li);
            }
        }
    });

}

function oldmanInfoReturn() {
    $(".fiveceng").hide();
    $(".thirdceng").show();
    $(".fourceng").show();
}

function selectArea(name,obj) {
    if(currentArea==="areaCountry"){
        areaTown=name;
    }
    if(currentArea==="areaTown"){
        areaVillage=name;
    }
    if(currentArea==="areaVillage"){
        areaCustomOne=name;
    }
    createStaticCount(false);
    pollOldmanStatus(false);
    createOldmanChart(false);

    if(name==null){
        var allOverlay = map.getOverlays();
        for(var i = 0;i<allOverlay.length;i++) {
            if(allOverlay[i].id !=undefined && allOverlay[i].id!=null && allOverlay[i].type!="RED"){
                allOverlay[i].setAnimation(null);
            }
        }
        map.panTo(new BMap.Point(lng,lat));
    }else{
        $.ajax({
            url: "/location/getLocationByArea",
            type: 'post',
            dataType: 'json',
            data :JSON.stringify({
                "areaCustomOne":areaCustomOne,
                "areaCountry":areaCountry,
                "areaTown":areaTown,
                "areaVillage":areaVillage
            }),
            async:false,
            contentType: "application/json;charset=UTF-8",
            success: function (result) {
                var data=result.locationVoList;
                var allOverlay = map.getOverlays();
                var positions=[];
                var m=0;
                for(var i = 0;i<allOverlay.length;i++) {
                    if(allOverlay[i].id !=undefined && allOverlay[i].id!=null && allOverlay[i].type!="RED"){
                        allOverlay[i].setAnimation(null);
                        for(var j=0;j<data.length;j++){
                            if(data[j].id==allOverlay[i].id){
                                allOverlay[i].setAnimation(BMAP_ANIMATION_BOUNCE);
                                if(m<50){
                                    positions[m++]=allOverlay[i].getPosition();
                                }
                            }
                        }
                    }
                }
                if(positions.length>0){
                    // console.info(positions.length);
                    // console.info(JSON.stringify(positions));
                    map.panTo(map.getViewport(positions).center);
                }
            }
        });
    }

    $(".adminNumber").css("background-color","");
    if(obj!=null){
        $(obj).css("background-color","#5c5c35");
    }

    $(".fiveceng").hide();
    $(".sixceng").hide();
    $(".secondceng #secondSecond").hide();
    $(".fourceng").show();
    $(".thirdceng").show();
    $(".secondceng #secondFirst").show();
}

function secondReturn() {
    $("#secondSecond").hide();
    $(".fiveceng").hide();
    $("#secondFirst").show();
    $(".thirdceng").show();
    $(".fourceng").show();
}

function sixReturn() {
    $(".sixceng").hide();
    $(".thirdceng").show();
    $(".fourceng").show();
    $(".secondceng").show();
}

function birthdayOldman() {
    $("#birthdayOldmanList").html("");

    $.ajax({
        url: "/oldman/getBirthdayOldman",
        type: 'post',
        dataType: 'json',
        data :JSON.stringify({
            "date":new Date().Format("MM-dd"),
            "oldmanSearchParam": {
                "areaCustomOne":areaCustomOne,
                "areaCountry":areaCountry,
                "areaTown":areaTown,
                "areaVillage":areaVillage
            },
        }),
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var oldmanList = result.oldmanVoList;
            for(var i=0;i<oldmanList.length;i++){
                var li="<li onclick='oldmanInfo("+oldmanList[i].oid+")'>" +
                    "<div class='bbbb' style='width: 30%;'><span class='word' style='font-size: larger;'>"+oldmanList[i].oid+"</span></div>" +
                    "<div class='bbbb' style='width: 30%;'><span class='word' style='font-size: larger;'>"+oldmanList[i].name+"</span></div>" +
                    "<div class='bbbb' style='width: 25%;'><span class='word' style='font-size: larger;'>"+oldmanList[i].age+"</span></div>" +
                    "<div class='bbbb' style='width: 10%;'><span class='word' style='font-size: larger;'>"+oldmanList[i].sex+"</span></div></li>";
                $("#birthdayOldmanList").append(li);
            }
            $("#birthdayOldmanNumber").html(oldmanList.length);
            $(".secondceng").hide();
            $(".thirdceng").hide();
            $(".fourceng").hide();
            $(".sixceng").show();
        }
    });

}


function secondceng(obj,type) {

    var oldmanSearchParam={
        "pageParam": {
            "pageNo": 0,
            "pageSize": 50
        },
        "oldmanSearchParam": {
            "areaCustomOne":areaCustomOne,
            "areaCountry":areaCountry,
            "areaTown":areaTown,
            "areaVillage":areaVillage
        },
        "needCount": false
    };
    oldmanSearchParam.oldmanSearchParam.areaCustomOne=areaCustomOne;
    var data={};
    if(type==1){
        oldmanSearchParam.oldmanSearchParam.homeService=true;
        data=homeServiceData;
    }
    if(type==2){
        oldmanSearchParam.oldmanSearchParam.equip=true;
        data=equipData;
    }
    if(type==4){
        data=workerData;
    }

    var chartData=[];
    var i=0;
    for(var key in data)  {
        chartData[i++]={"key":key,"value":data[key]};
    }

    createHeightAndWidthFromSourceDoc("sencondAll","chart",0.7,0.8);
    if(type==4) {
        createBarChart(null,chartData,document.getElementById('chart'),selectWorkerType);
        createWorker({"beyond":workerBeyond},"manList",true);
        $("#manName").html("工作人员");
    }else{
        createBarChart(null,chartData,document.getElementById('chart'),selectManType);
        createOldman(oldmanSearchParam, "manList",true);
        $("#manName").html("老人名单");
    }
    $("#secondcengName").html($(obj).children().eq(0).html());
    $("#secondcengNum").html($(obj).children().eq(1).html());
    $("#secondFirst").hide();
    $(".thirdceng").hide();
    $(".fourceng").hide();
    $("#secondSecond").show();
    $(".fiveceng").show();
}

function selectWorkerType(name) {
    $("#manName").html("工作人员："+name);
    if(name=="送餐"){
        createWorker({"type":1,"beyond":workerBeyond},"manList",true);
    }else if(name=="长护险"){
        createWorker({"type":2,"beyond":workerBeyond},"manList",true);
    }else if(name=="医疗"){
        createWorker({"type":3,"beyond":workerBeyond},"manList",true);
    }else if(name=="居家养老"){
        createWorker({"type":4,"beyond":workerBeyond},"manList",true);
    }
}

function selectManType(name) {
    var param={
        "pageParam": {
            "pageNo": 0,
            "pageSize": 50
        },
        "oldmanSearchParam": {
            "areaCustomOne":areaCustomOne,
            "areaCountry":areaCountry,
            "areaTown":areaTown,
            "areaVillage":areaVillage
        },
        "needCount": false
    };
    $("#manName").html("老人名单："+name);
    if(name=="家庭服务"){
        param.oldmanSearchParam.serviceType=2;
    }else if(name=="长护险"){
        param.oldmanSearchParam.serviceType=1;
    }else if(name=="居家养老服务"){
        param.oldmanSearchParam.serviceType=3;
    }else if(name=="关怀系统"){
        param.oldmanSearchParam.equipType="careGatewayId";
    }else if(name=="想家宝"){
        param.oldmanSearchParam.equipType="xjbId";
    }else if(name=="摄像头"){
        param.oldmanSearchParam.equipType="cameraId";
    }
    createOldman(param,"manList",true);

}

function selectAlarmType(name) {
    $("#manName").html("报警信息："+name);
    if(name=="行为预警"){
        createAlarmData(2,"manList",true);
    }else if(name=="规律异常预警"){
        createAlarmData(6,"manList",true);
    }else if(name=="光强预警"){
        createAlarmData(3,"manList",true);
    }else if(name=="温度预警"){
        createAlarmData(4,"manList",true);
    }else if(name=="紧急报警"){
        createAlarmData(1,"manList",true);
    }else if(name=="未归预警"){
        createAlarmData(5,"manList",true);
    }
}

function alarmClick(obj) {
    $.ajax({
        url: "/alarm/getAllTypeCountByArea",
        type: 'post',
        dataType: 'json',
        data :JSON.stringify({
            "areaCustomOne":areaCustomOne,
            "areaCountry":areaCountry,
            "areaTown":areaTown,
            "areaVillage":areaVillage
        }),
        async:false,
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var map=result.smap;
            $("#secondcengNum").html(result.sum);
            createHeightAndWidthFromSourceDoc("sencondAll","chart",0.7,0.8);
            createBarChartWithLegend(null,map,document.getElementById('chart'),selectAlarmType);
        }
    });
    createAlarmData(null,true);
    $("#manName").html("报警信息");
    $("#secondcengName").html($(obj).children().eq(0).html());
    $("#secondFirst").hide();
    $(".thirdceng").hide();
    $(".fourceng").hide();
    $("#secondSecond").show();
    $(".fiveceng").show();
}


function createAlarmData(type,clear) {
    $.ajax({
        url: "/alarm/getAlarmOldmanByPage",
        type: 'post',
        dataType: 'json',
        data :JSON.stringify({
            "pageParam":{
                "pageNo":0,
                "pageSize":100
            },
            "needOldmanInfo":true,
            "needCount":false,
            "alarmSearchParam":{
                "areaCustomOne":areaCustomOne,
                "areaCountry":areaCountry,
                "areaTown":areaTown,
                "areaVillage":areaVillage,
                "type":type
            }
        }),
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var data=result.alarmVoList;
            if(clear){
                $("#manList").html("");
            }
            for(var i=0;i<data.length;i++){
                var colorCss="";
                if(data[i].isHandle==false){
                    colorCss="color:red";
                }
                var li="<li onclick='oldmanInfo("+data[i].oldmanVo.oid+")'>" +
                    "<div class='bbbb' style='width: 30%;'><span class='word' style='font-size: larger;"+colorCss+"'>"+data[i].oldmanVo.oid+"</span></div>" +
                    "<div class='bbbb' style='width: 30%;'><span class='word' style='font-size: larger;"+colorCss+"'>"+data[i].oldmanVo.name+"</span></div>" +
                    "<div class='bbbb' style='width: 25%;'><span class='word' style='font-size: larger;"+colorCss+"'>"+data[i].oldmanVo.age+"</span></div>" +
                    "<div class='bbbb' style='width: 10%;'><span class='word' style='font-size: larger;"+colorCss+"'>"+data[i].oldmanVo.sex+"</span></div></li>";
                $("#manList").append(li);
            }
        }
    });
}

function createOldmanChart(sync) {

    $.ajax({
        url: "/main/getOldmanCount",
        type: 'post',
        dataType: 'json',
        data :JSON.stringify({
            "oldmanSearchParam": {
                "areaCustomOne":areaCustomOne,
                "areaCountry":areaCountry,
                "areaTown":areaTown,
                "areaVillage":areaVillage
            }
        }),
        sync:sync,
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            createHeightAndWidthFromSourceDoc("oldmanChart","oldmanAge",0.45,0.45);
            createHeightAndWidthFromSourceDoc("oldmanChart","oldmanSex",0.45,0.45);
            createHeightAndWidthFromSourceDoc("oldmanChart","oldmanHuji",0.45,0.45);
            createHeightAndWidthFromSourceDoc("oldmanChart","oldmanJia",0.45,0.45);

            var sex=[];
            var age=[];
            var hj=[];
            var jt=[];
            var i=0;
            for(var key in result.sexMap){
                sex[i]={"key":key,"value":result.sexMap[key]};
                i++;
            }

            i=0;
            for(var key in result.ageMap){
                age[i]={"key":key,"value":result.ageMap[key]};
                i++;
            }

            i=0;
            for(var key in result.hjMap){
                hj[i]={"key":key,"value":result.hjMap[key]};
                i++;
            }

            i=0;
            for(var key in result.jtMap){
                jt[i]={"key":key,"value":result.jtMap[key]};
                i++;
            }

            createPieChart("性别",sex,document.getElementById('oldmanAge'),oldmanInfoChartSelect);
            createPieChart("年龄",age,document.getElementById('oldmanSex'),oldmanInfoChartSelect);
            createPieChart("户籍",hj,document.getElementById('oldmanHuji'),oldmanInfoChartSelect);
            var legend= {
                itemWidth: 8,  // 设置大小
                itemHeight: 8,
                itemGap: 3, // 设置间距
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
            createPieChartWithLegend("家庭结构",jt,document.getElementById('oldmanJia'),oldmanInfoChartSelect,legend);
        }
    });
    // console.info("createOldmanChart");

}

function oldmanInfoChartSelect(title,value) {
    console.info("callback");
    var param={
        "pageParam": {
            "pageNo": 0,
            "pageSize": 50
        },
        "oldmanSearchParam": {
            "areaCustomOne":areaCustomOne,
            "areaCountry":areaCountry,
            "areaTown":areaTown,
            "areaVillage":areaVillage
        },
        "needCount": false
    };
    if(title=="性别"){
        param.oldmanSearchParam.sex=value;
    }else if(title=="年龄"){
        param.oldmanSearchParam.age=value;
    }else if(title=="户籍"){
        param.oldmanSearchParam.householdType=value;
    }else{
        param.oldmanSearchParam.familyType=value;
    }
    createOldman(param,"manList",true);
    $("#manName").html("老人名单："+value);
    $(".thirdceng").hide();
    $(".fourceng").hide();
    $(".fiveceng").show();
}

/**
 * 获取老人
 */
function createOldman(param,id,clear) {
    if(clear){
        $("#"+id).html("");
    }
    $.ajax({
        url: "/oldman/getOldmanByPage",
        type: 'post',
        dataType: 'json',
        data :JSON.stringify(param),
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var oldmanList = result.oldmanVoList;
            for(var i=0;i<oldmanList.length;i++){
                var li="<li onclick='oldmanInfo("+oldmanList[i].oid+")'>" +
                    "<div class='bbbb' style='width: 30%;'><span class='word' style='font-size: larger;'>"+oldmanList[i].oid+"</span></div>" +
                    "<div class='bbbb'style='width: 30%;'><span class='word' style='font-size: larger;'>"+oldmanList[i].name+"</span></div>" +
                    "<div class='bbbb'style='width: 25%;'><span class='word' style='font-size: larger;'>"+oldmanList[i].age+"</span></div>" +
                    "<div class='bbbb'style='width: 10%;'><span class='word' style='font-size: larger;'>"+oldmanList[i].sex+"</span></div></li>";
                $("#"+id).append(li);
            }
        }
    });
}

/**
 * 获取工作人员
 */
function createWorker(param,id,clear) {
    if(clear){
        $("#"+id).html("");
    }

    $.ajax({
        url: "/worker/getWorkerByPage",
        type: 'post',
        dataType: 'json',
        data :JSON.stringify({
            "pageParam": {
                "pageNo": 0,
                "pageSize": 50
            },
            "workerSearchParam":param
        }),
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var data = result.workerVoList;
            for(var i=0;i<data.length;i++){
                var li="<li onclick='workerInfo("+data[i].id+")'>" +
                    "<div class='bbbb' style='width: 30%;'><span class='word' style='font-size: larger;'>"+data[i].id+"</span></div>" +
                    "<div class='bbbb' style='width: 30%;'><span class='word' style='font-size: larger;'>"+data[i].name+"</span></div>" +
                    "<div class='bbbb' style='width: 25%;'><span class='word' style='font-size: larger;'>"+data[i].age+"</span></div>" +
                    "<div class='bbbb' style='width: 10%;'><span class='word' style='font-size: larger;'>"+data[i].sex+"</span></div></li>";
                $("#"+id).append(li);
            }
        }
    });

}