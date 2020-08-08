var adminNumber=[{"name":"XX（生产）队","number":"1,000"},
    {"name":"XX（生产）队","number":"1,000"},{"name":"XX（生产）队","number":"1,000"},
    {"name":"XX（生产）队","number":"1,000"},{"name":"XX（生产）队","number":"1,000"},
    {"name":"XX（生产）队","number":"1,000"},{"name":"XX（生产）队","number":"1,000"},
    {"name":"XX（生产）队","number":"1,000"},{"name":"XX（生产）队","number":"1,000"}];
var serviceOldman=[{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":2},{"name":"测试老人1","oid":3},{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":1}];
var areaCustomOne=null;
$(document).ready(function(){
    createAdminNumber(adminNumber);
    createOldmanChart();
});

/**
 * 行政区域划分的各服务总人数
 */
function createAdminNumber(data) {
    for(var i=0;i<data.length;i++){
        var li="<li class='adminNumber'><span class='word num' style='display: inline'>"+data[i].number+"</span><span class='word' style='font-size: large;display: inline;margin-left: 5%'>"+data[i].name+"</span></li>";
        $("#adminNumber").append(li);
    }
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
    createOldman(serviceOldman,"birthdayOldmanList",true);
    $(".secondceng").hide();
    $(".thirdceng").hide();
    $(".fourceng").hide();
    $(".sixceng").show();
}

/**
 * sourceId 为null 获取父类的元素
 * @param sourceId
 * @param targetId
 * @param heightP
 * @param withP
 */
function createHeightAndWidthFromSourceDoc(sourceId,targetId,heightP,withP) {
    var doc;
    if(sourceId==null){
        doc=document.getElementById(targetId).parentNode;
    }else{
        doc=document.getElementById(sourceId);
    }

    var width = doc.offsetWidth*withP;
    var height = doc.offsetHeight*heightP;
    $("#"+targetId).css("width",width).css("height",height);

}

function secondceng(obj,type) {
    var oldmanSearchParam={
        "pageParam": {
            "pageNo": 0,
            "pageSize": 10
        },
        "oldmanSearchParam": {},
        "needCount": false
    };
    oldmanSearchParam.oldmanSearchParam.areaCustomOne=areaCustomOne;
    var data=[];
    if(type==1){
        data=getHomeServiceData();
    }
    if(type==2){
        oldmanSearchParam.oldmanSearchParam.equip=true;
        data=getEquipData();
    }
    if(type==3){
        data=getWarnData();
    }
    if(type==4){
        data=getWorkerData();
    }

    createHeightAndWidthFromSourceDoc("sencondAll","chart",0.7,0.8);
    createBarChart(null,data,document.getElementById('chart'),null);
    if(type==4) {
        createWorker(serviceOldman,"manList",true);
        $("#manName").html("工作人员");
    }else{
        createOldman(oldmanSearchParam, "manList",true);
        $("#manName").html("服务人员");
    }
    $("#secondcengName").html($(obj).children().eq(0).html());
    $("#secondcengNum").html($(obj).children().eq(1).html());
    $("#secondFirst").hide();
    $(".thirdceng").hide();
    $(".fourceng").hide();
    $("#secondSecond").show();
    $(".fiveceng").show();
}


function getHomeServiceData() {
    return [{"key":"长护险服务人数","value":2000},{"key":"居家养老人数","value":1000},{"key":"家庭服务人数","value":500}];
}

function getEquipData() {
    return data=[{"key":"关怀系统","value":2000},{"key":"想家宝","value":1000},{"key":"摄像头","value":500}];
}

function getWarnData() {
    return data=[{"key":"紧急报警","value":2000},{"key":"行为报警","value":1000},{"key":"规律报警","value":500}];
}

function getWorkerData() {
    return data=[{"key":"长护险","value":2000},{"key":"居家养老","value":1000},{"key":"送餐","value":500},{"key":"医疗","value":500}];
}



function createOldmanChart() {
    createHeightAndWidthFromSourceDoc("oldmanChart","oldmanAge",0.45,0.45);
    createHeightAndWidthFromSourceDoc("oldmanChart","oldmanSex",0.45,0.45);
    createHeightAndWidthFromSourceDoc("oldmanChart","oldmanHuji",0.45,0.45);
    createHeightAndWidthFromSourceDoc("oldmanChart","oldmanJia",0.45,0.45);

    var sex=[{"key":"男","value":2000},{"key":"女","value":1000}];
    var age=[{key:"60-70",value:100},{key:"71-80",value:100},{key:"81-90",value:100},{key:"90-",value:100}];
    var huji=[{key:"本地",value:100},{key:"外地",value:100},{key:"人户\n分离",value:100}];
    var jia=[{key:"纯老",value:100},{key:"独居",value:100},{key:"失独",value:100},{key:"孤老",value:100},{key:"一老养\n一老",value:100},{key:"三支\n人员",value:100},{key:"其他",value:100}];


    createPieChart("男女",sex,document.getElementById('oldmanAge'),null);
    createPieChart("年龄",age,document.getElementById('oldmanSex'),null);
    createPieChart("户籍",huji,document.getElementById('oldmanHuji'),null);
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
    createPieChartWithLegend("家庭结构",jia,document.getElementById('oldmanJia'),null,legend);

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
                var li="<li onclick='oldmanInfo("+oldmanList[i].oid+")'><span class='word' style='font-size: larger;'>"+oldmanList[i].name+"</span></li>";
                $("#"+id).append(li);
            }
        }
    });
}

/**
 * 获取老人
 */
function createWorker(data,id,clear) {
    if(clear){
        $("#"+id).html("");
    }
    for(var i=0;i<data.length;i++){
        var li="<li onclick='oldmanInfo("+data[i].oid+")'><span class='word' style='font-size: larger;'>"+data[i].name+"</span></li>";
        $("#"+id).append(li);
    }
}