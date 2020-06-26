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

    /**
     * 创建标注
     * @param position
     * @param map
     */
    function createMarker(position,map) {
        var pt = new BMap.Point(position.positionX, position.positionY);
        var myIcon;
        if(position.locationTypeEnum == "GREEN"){
            myIcon = new BMap.Icon("../../static/img/a1.png", new BMap.Size(52, 26));
        }else if(position.locationTypeEnum == "YELLOW"){
            myIcon = new BMap.Icon("/static/img/a2.png", new BMap.Size(52, 26));
        }else{
            myIcon = new BMap.Icon("/static/img/a3.png", new BMap.Size(52, 26));

        }
        var marker = new BMap.Marker(pt, {
            icon: myIcon
        });  // 创建标注
        marker.setTitle(position.desc);
        map.addOverlay(marker);
    }
});

