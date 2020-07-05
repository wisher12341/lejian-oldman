$(document).ready(function(){
    var map = new BMap.Map("map");
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
                createMarker(positions[i],map);
            }
        }
    });

});

/**
 * 创建标注
 * @param position
 * @param map
 */
function createMarker(position,map) {
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
                width : 400,     // 信息窗口宽度
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
                    '<div class="col-sm-4">'+oldman.status+'</div>' +
                    '<div class="col-sm-4"><button class="btn btn-primary" onclick=oldmanInfo('+oldman.oid+')>查看</button><button class="btn btn-primary">视频</button></div>' +
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