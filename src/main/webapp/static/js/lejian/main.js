var map;
$(document).ready(function(){
    map = new BMap.Map("map");
    map.centerAndZoom(new BMap.Point(121.390421, 31.157395), 16);
    map.setMapStyle({style:'midnight'});
    map.enableScrollWheelZoom(true);

    $.ajax({
        url: "/location/getAllLocation",
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var positions = result.locationVoList;
            for(var i=0;i<positions.length;i++){
                createLocationMarker(positions[i],map);
            }
        }
    });

});

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
    marker.setTitle(position.desc);
    marker.disableMassClear();
    map.addOverlay(marker);

    marker.addEventListener("click", function(){
        var infoWindow = createInfoWindow(position);
        map.openInfoWindow(infoWindow, pt); //开启信息窗口
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
            var data = "<div class='container' style='width: 100%'>";
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
    $("#oldmanInfo").attr("src","/oldmanInfo?oid="+oid+"&contact=no");
    //todo 等数据加载完 再展示
    $("#oldmanInfo").css("display","block");
}

// 展示所有服务人员及其特定时间段内的位置
function showAllWorker() {
    $.ajax({
        url: "/worker/getWorkerByPage",
        type: 'post',
        data :JSON.stringify({
            "pageParam": {
                "pageNo": 0,
                "pageSize": 100
            },
            "startTime":"2020-01-01 00:00:00",
            "endTime":"2020-12-01 00:00:00",
            "location":true
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
                    '<div class="col-sm-6"><button class="btn btn-primary" onclick="showOneWorker('+worker.id+')" >路线</button><button class="btn btn-primary" onclick="openQQ('+worker.qq+')">语音</button></div>' +
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
            "startTime":"2020-01-01 00:00:00",
            "endTime":"2020-12-01 00:00:00",
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

