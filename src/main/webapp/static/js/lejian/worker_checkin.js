var map;
$(document).ready(function(){
    $.ajax({
        url: "/oldman/getOldmanByOid",
        data :JSON.stringify({
            "oid":getQueryVariable("oid")
        }),
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            var oldman =result.oldmanVo;
            $("#oid").text(oldman.oid);
            $("#oldmanname").text(oldman.name);
            checkIn();
        }
    });

});

/**
 * 手机访问 定位是准确的
 * PC上不准确
 */
function checkIn() {
    var geolocation = new BMap.Geolocation();
    geolocation.getCurrentPosition(function(r){
        if(this.getStatus() == BMAP_STATUS_SUCCESS){
            $.ajax({
                url: "/worker/checkin",
                data :JSON.stringify({
                    "lng":r.point.lng,
                    "lat":r.point.lat,
                    "oid":getQueryVariable("oid")
                }),
                type: 'post',
                dataType: 'json',
                contentType: "application/json;charset=UTF-8",
                success: function (result) {
                    // console.info(JSON.stringify(result));
                    if(result.errorCode==null){
                        $("#result").text("签到成功");
                    }else{
                        $("#result").text("签到失败："+result.errMsg);
                    }
                    $("#load-spinner").css("display","none");
                    var lng =r.point.lng;
                    var lat =r.point.lat;
                    map = new BMap.Map("map",{enableMapClick:false});
                    map.centerAndZoom(new BMap.Point(lng,lat), 15);
                    map.enableScrollWheelZoom(true);
                    map.enablePinchToZoom(true);
                    var marker = new BMap.Marker(new BMap.Point(lng, lat), {
                        icon: new BMap.Icon("/static/img/mapGreen.png", new BMap.Size(48, 48))
                    });  // 创建标注
                    marker.setAnimation(BMAP_ANIMATION_BOUNCE);
                    map.addOverlay(marker);
                }
            });
            console.log("当前位置经度为:"+r.point.lng+"纬度为:"+r.point.lat);
        } else {
            console.log('无法定位到您的当前位置，导航失败，请手动输入您的当前位置！'+this.getStatus());
        }
    },{enableHighAccuracy: true});

}

