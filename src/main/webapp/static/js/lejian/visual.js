var map;
var timestamp=new Date().getTime();
var careTimestamp=new Date().getTime();

var interval;
var time;

var lng,lat;
$(document).ready(function(){
    getConfigData();
});

function getConfigData() {
    $.ajax({
        url: "/visual/setting/getByPage",
        type: 'post',
        dataType: 'json',
        data :JSON.stringify({
            "pageParam": {
                "pageNo": 0,
                "pageSize": 10
            }
        }),
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var usedData;
            var data = result.visualSettingVoList;
            for(var i=0;i<data.length;i++){
                var highlight ="";

                if(data[i].isUsed==="是"){
                    usedData = data[i];
                    highlight="p12";
                }
                var text = data[i].areaVillage;
                if (text == null || text == ''){
                    text = data[i].areaTown;
                }
                if (text == null || text == ''){
                    text = data[i].areaCountry;
                }

                var label = $("<span class='label p11 "+highlight+"' onclick=changeVisualArea('"+data[i].id+"')>"+text+"</span>");
                $("#visualSetting").append(label);
            }

            lng=usedData.lng;
            lat=usedData.lat;
            createMap(usedData.lng,usedData.lat);

            areaCountry=usedData.areaCountry;
            areaTown=usedData.areaTown;
            areaVillage=usedData.areaVillage;
            areaCustomOne=usedData.areaCustomOne;

            if(areaCountry!==undefined &&areaCountry!==null && areaCountry!==""){
                currentArea="areaCountry";
                workerBeyond+=usedData.areaCountry+"-";
            }
            if(areaTown!==undefined &&areaTown!==null && areaTown!==""){
                currentArea="areaTown";
                workerBeyond+=usedData.areaTown+"-";
            }
            if(areaVillage!==undefined &&areaVillage!==null && areaVillage!==""){
                currentArea="areaVillage";
                workerBeyond+=usedData.areaVillage+"-";
            }
            if(areaCustomOne!==undefined && areaCustomOne!==null && areaCustomOne!==""){
                currentArea="areaCustomOne";
            }
            workerBeyond=workerBeyond.substr(0,workerBeyond.length-1);

            if(currentArea==="areaCountry"){
                $("#areaTitle2").html(areaCountry);
            }
            if(currentArea==="areaTown"){
                $("#areaTitle1").html(areaCountry);
                $("#areaTitle2").html(areaTown);
            }
            if (currentArea==="areaVillage"){
                $("#areaTitle1").html(areaCountry+areaTown);
                $("#areaTitle2").html(areaVillage);
            }
            pollOldmanStatus(true);
            createAdminNumber();
            createStaticCount(true);
            createOldmanChart(true);
        }
    });

}

function createMap(lng,lat) {
    map = new BMap.Map("map",{enableMapClick:false});
    map.centerAndZoom(new BMap.Point(lng,lat), 15);
    map.setMapStyle({style:'midnight'});
    map.enableScrollWheelZoom(true);
    map.enablePinchToZoom(true);
    var opts = {anchor: BMAP_ANCHOR_BOTTOM_RIGHT};
    map.addControl(new BMap.NavigationControl(opts));
    $.ajax({
        url: "/location/getAllLocationByConfig",
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var positions = result.locationVoList;
            for(var i=0;i<positions.length;i++){
                createLocationMarker(positions[i],map);
            }
            interval =self.setInterval("pollOldmanStatus()",60*1000);
            $('#timeIcon').html(new Date().Format('yyyy-MM-dd HH:mm:ss'));
            time=self.setInterval("$('#timeIcon').html(new Date().Format('yyyy-MM-dd HH:mm:ss'))",1000);
        }
    });
}

/**
 * 轮询老人状态
 */
function pollOldmanStatus(sync) {
    // console.info("start job");
    $.ajax({
        url: "/main/poll",
        type: 'post',
        dataType: 'json',
        data :JSON.stringify({
            "timestamp": timestamp,
            "careTimeStamp":careTimestamp,
            "oldmanSearchParam":{
                "areaCustomOne":areaCustomOne,
                "areaCountry":areaCountry,
                "areaTown":areaTown,
                "areaVillage":areaVillage
            }
        }),
        sync:sync,
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            $("#serviceNum").html(result.yellowOldmanCount);
            $("#historyServiceNum").html(result.workerCheckInCount);
            $("#warnNum").html(result.alarmCount);

            $("#homeServiceNum").html(result.homeServiceCount);
            $("#organServiceNum").html(result.organServiceCount);
            $("#rzzNum").html(result.rzzCount);
            $("#dbNum").html(result.dbCount);
            $("#workerNum").html(result.workerCount);
            $("#equipNum").html(result.equipCount);

            var positions = result.locationVoList;
            var allOverlay = map.getOverlays();
            var records= result.careAlarmRecordVoList;
            if(positions!=null && positions.length>0){
                timestamp=result.timestamp;
                for(var i=0;i<positions.length;i++){
                    var position=positions[i];
                    for(var j = 0;j<allOverlay.length;j++) {
                        //删除指定的楼
                        if(allOverlay[j].id !=undefined && allOverlay[j].id!=null){
                            var id=allOverlay[j].id;
                            if (id==position.id) {
                                position.positionX=allOverlay[j].getPosition().lng;
                                position.positionY=allOverlay[j].getPosition().lat;
                                map.removeOverlay(allOverlay[j]);
                                console.info(position.positionX+":"+position.positionY);
                                createLocationMarker(position,map);
                                break;
                            }
                        }
                    }
                }
            }
            if(records!=null && records.length>0){
                careTimestamp=result.careTimestamp;
                for(var i=0;i<records.length;i++){
                    var record=records[i];
                    var message = record.oldmanVo.name+"："+record.type;
                    showToast(message);
                }
            }
        }
    });
    // console.info("pollOldmanStatus");

}

/**
 * 创建标注
 * @param position
 * @param map
 */
function createLocationMarker(position,map) {
    var pt = new BMap.Point(position.positionX, position.positionY);
    var myIcon;
    if(position.locationTypeEnum == "GREEN"){
        myIcon = new BMap.Icon("/static/img/mapGreen.png", new BMap.Size(32, 32));
    }else if(position.locationTypeEnum == "YELLOW"){
        myIcon = new BMap.Icon("/static/img/mapYellow.png", new BMap.Size(32, 32));
    }else{
        myIcon = new BMap.Icon("/static/img/mapRed.png", new BMap.Size(32, 32));
    }
    var marker = new BMap.Marker(pt, {
        icon: myIcon
    });  // 创建标注
    if(position.locationTypeEnum == "RED"){
        marker.setAnimation(BMAP_ANIMATION_BOUNCE);
    }
    marker.setTitle(position.desc);
    marker.disableMassClear();
    marker.id=position.id;
    marker.type=position.locationTypeEnum;
    map.addOverlay(marker);

    marker.addEventListener("click", function(){
        var infoWindow = createInfoWindow(position);
        map.openInfoWindow(infoWindow, pt); //开启信息窗口
        // 红灯处理
        if(position.locationTypeEnum == "RED"){
            //该楼对应的老人 报警都为已处理
            $.ajax({
                url: "/alarm/updateHandleByLocationId",
                type: 'post',
                dataType: 'json',
                data :JSON.stringify({
                    "locationId": marker.id,
                    "isHandle":1
                }),
                contentType: "application/json;charset=UTF-8",
                success: function (result) {}
            });
            //该楼所有老人变成 绿色
            $.ajax({
                url: "/oldman/updateStatusByLocationId",
                type: 'post',
                dataType: 'json',
                data :JSON.stringify({
                    "locationId": marker.id,
                    "status":1
                }),
                async:false,
                contentType: "application/json;charset=UTF-8",
                success: function (result) {
                    if(result.success==true){
                        map.removeOverlay(marker);
                        position.locationTypeEnum = "GREEN";
                        createLocationMarker(position,map);
                    }
                }
            });
        }
    });
}


/**
 * 创建信息窗口
 * @param position
 */
function createInfoWindow(position) {
    var infoWindow;
    $.ajax({
        url: "/oldman/getOldmanByLocationId",
        type: 'post',
        data :JSON.stringify({
            "locationId": position.id
        }),
        async:false,
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var opts = {
                width : 500,     // 信息窗口宽度
                height: 200,     // 信息窗口高度
                title : position.desc, // 信息窗口标题
                message:""
            };
            var data = "<div class='container' style='width: 100%;overflow-y: scroll' >";
            var oldmanList = result.oldmanVoList;
            for(var i=0;i<oldmanList.length;i++){
                var oldman = oldmanList[i];
                data += '<div class="row">' +
                    '<div class="col-sm-4">'+oldman.name+'</div>' +
                    '<div class="col-sm-2">'+oldman.status+'</div>' +
                    '<div class="col-sm-6"><button class="btn btn-primary" onclick=oldmanInfo('+oldman.oid+')>查看</button><button class="btn btn-primary" onclick="openVideo()">视频</button><a class="btn btn-primary" href="Webshell://hello">萤石云</a></div>' +
                    '</div>';
            }
            data+="</div>";
            infoWindow = new BMap.InfoWindow(data, opts);  // 创建信息窗口对象
        }
    });
    return infoWindow;
}

function oldmanInfo(oid) {
    $("#oldmanInfo").hide();
    $("#oldmanInfo").attr("src","/oldmanInfo?oid="+oid+"&contact=no");
    $("#oldmanInfo").load(function(){                             //  等iframe加载完毕
        $("#oldmanInfo").show();
    });
}

function workerInfo(wid) {
    $("#oldmanInfo").hide();
    $("#oldmanInfo").attr("src","/workerInfo?wid="+wid);
    $("#oldmanInfo").load(function(){                             //  等iframe加载完毕
        $("#oldmanInfo").show();
    });
}

// 展示该行政单位所有服务人员及其特定时间段内的位置
function showAllWorker() {
    $.ajax({
        url: "/worker/getWorkerPositionByPageAndArea",
        type: 'post',
        data :JSON.stringify({
            "pageParam": {
                "pageNo": 0,
                "pageSize": 10
            },
            "startTime": new Date(new Date(new Date().toLocaleDateString()).getTime()).Format('yyyy-MM-dd HH:mm:ss'),
            "endTime":new Date(new Date(new Date().toLocaleDateString()).getTime()+24*60*60*1000-1).Format('yyyy-MM-dd HH:mm:ss')
        }),
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var data = "";
            var workerList = result.workerVoList;
            for(var i=0;i<workerList.length;i++){
                var worker = workerList[i];
                data += '<div class="row worker-highlight" id="row'+worker.id+'">' +
                    '<div class="col-sm-6" style="padding-top: 1%">'+worker.name+'</div>' +
                    '<div class="col-sm-6"><button class="btn btn-primary" onclick=workerInfo('+worker.id+')>查看</button><button class="btn btn-primary" onclick="showOneWorker('+worker.id+')" >路线</button><button class="btn btn-primary" onclick="openQQ('+worker.qq+')">语音</button></div>' +
                    '</div>';
                if(worker.positionList.length>0){
                    var location = worker.positionList[0];
                    var pt = new BMap.Point(location.lng, location.lat);
                    var myIcon = new BMap.Icon("/static/img/worker.png", new BMap.Size(32, 32));
                    var marker = new BMap.Marker(pt, {
                        icon: myIcon
                    });  // 创建标注
                    marker.setTitle(worker.name+"："+location.time);
                    marker.setZIndex(999);
                    map.addOverlay(marker);
                }
            }
            data+='<div class="row">' +
                '<div class="col-sm-5"></div><div class="col-sm-2"><i class="fa fa-chevron-up fa-lg" aria-hidden="true" onclick="hideWorkerList()"></i></div></div>';
            $("#workerList").html(data).show();
            $("#workerIcon").hide();
        }
    });
}

function showOneWorker(id) {
    $.ajax({
        url: "/worker/getWorkerPositionByTime",
        type: 'post',
        data :JSON.stringify({
            "startTime": new Date(new Date(new Date().toLocaleDateString()).getTime()).Format('yyyy-MM-dd HH:mm:ss'),
            "endTime":new Date(new Date(new Date().toLocaleDateString()).getTime()+24*60*60*1000-1).Format('yyyy-MM-dd HH:mm:ss'),
            "workerId":id
        }),
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            map.clearOverlays();
            var worker=result.workerVo;
            var pois =[];
            var j=0;
            for(var i=0;i<worker.positionList.length;i++){
                var location = worker.positionList[i];
                if(location.lng.length>0 && location.lat.length>0){
                    var pt = new BMap.Point(location.lng, location.lat);
                    var myIcon = new BMap.Icon("/static/img/worker.png", new BMap.Size(32, 32));
                    var marker = new BMap.Marker(pt, {
                        icon: myIcon
                    });  // 创建标注
                    marker.setTitle(location.time);
                    marker.setZIndex(999);
                    map.addOverlay(marker);
                    pois[j++]=new BMap.Point(location.lng, location.lat)
                }
            }

            var sy = new BMap.Symbol(BMap_Symbol_SHAPE_BACKWARD_OPEN_ARROW, {
                scale: 0.6,//图标缩放大小
                strokeColor:'#fff',//设置矢量图标的线填充颜色
                strokeWeight: '2',//设置线宽
            });
            var icons = new BMap.IconSequence(sy, '10', '30');
            // 创建polyline对象
            var polyline =new BMap.Polyline(pois, {
                enableEditing: false,//是否启用线编辑，默认为false
                enableClicking: true,//是否响应点击事件，默认为true
                icons:[icons],
                strokeWeight:'8',//折线的宽度，以像素为单位
                strokeOpacity: 0.8,//折线的透明度，取值范围0 - 1
                strokeColor:"#18a45b" //折线颜色
            });
            map.addOverlay(polyline);          //增加折线
            $(".worker-highlight").css("background-color","");
            $("#row"+id).css("background-color","#5c5c35");
        }
    });
}

function hideWorkerList() {
    map.clearOverlays();
    $("#workerList").hide();
    $("#workerIcon").show();
}

function openQQ(qq) {
    var url='tencent://Message/?uin='+qq;
    window.open(url);
}

function openVideo() {
    var url='https://cloud3.xiangjiabao.com:28058';
    window.open(url);
}

function openYsy() {
    window.location.href="Webshell://hello";
}

