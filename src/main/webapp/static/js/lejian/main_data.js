var adminNumber=[{"name":"XX（生产）队","number":"1,000"},
    {"name":"XX（生产）队","number":"1,000"},{"name":"XX（生产）队","number":"1,000"},
    {"name":"XX（生产）队","number":"1,000"},{"name":"XX（生产）队","number":"1,000"},
    {"name":"XX（生产）队","number":"1,000"},{"name":"XX（生产）队","number":"1,000"},
    {"name":"XX（生产）队","number":"1,000"},{"name":"XX（生产）队","number":"1,000"}];
var serviceOldman=[{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":2},{"name":"测试老人1","oid":3},{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":1},{"name":"测试老人1","oid":1}];
$(document).ready(function(){
    createAdminNumber(adminNumber);
    createOldman(serviceOldman);
    createWorker(serviceOldman);
    createHomeServiceBar();
    createEquipBar();
    createWarnBar();
    createWorkerBar();
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

function createHomeServiceBar() {
    createHeightAndWidthFromSourceDoc("home","homeService",0.7,0.8);
    var data=[{"key":"长护险服务人数","value":2000},{"key":"居家养老人数","value":1000},{"key":"家庭服务人数","value":500}];
    createBarChart(null,data,document.getElementById('homeService'),oldmanSelect);
}

function createEquipBar() {
    createHeightAndWidthFromSourceDoc("equip","equipC",0.7,0.8);
    var data=[{"key":"关怀系统","value":2000},{"key":"想家宝","value":1000},{"key":"摄像头","value":500}];
    createBarChart(null,data,document.getElementById('equipC'),oldmanSelect);
}

function createWarnBar() {
    createHeightAndWidthFromSourceDoc("warn","warnList",0.7,0.8);
    var data=[{"key":"紧急报警","value":2000},{"key":"行为报警","value":1000},{"key":"规律报警","value":500}];
    createBarChart(null,data,document.getElementById('warnList'),oldmanSelect);
}

function createWorkerBar() {
    createHeightAndWidthFromSourceDoc("worker","workerCList",0.7,0.8);
    var data=[{"key":"长护险","value":2000},{"key":"居家养老","value":1000},{"key":"送餐","value":500},{"key":"医疗","value":500}];
    createBarChart(null,data,document.getElementById('workerCList'),oldmanSelect);
}

function oldmanSelect(name) {
    $("#thirdOldmanName").html(name);
    $("#thirdOldmanNum").html(1000);
}


function createOldmanChart() {
    createHeightAndWidthFromSourceDoc("oldmanChart","oldmanAge",1,0.22);
    createHeightAndWidthFromSourceDoc("oldmanChart","oldmanSex",1,0.22);
    createHeightAndWidthFromSourceDoc("oldmanChart","oldmanHuji",1,0.22);
    createHeightAndWidthFromSourceDoc("oldmanChart","oldmanJia",1,0.22);

    var sex=[{"key":"男","value":2000},{"key":"女","value":1000}];
    var age=[{key:"60-70",value:100},{key:"71-80",value:100},{key:"81-90",value:100},{key:"90-",value:100}];
    var huji=[{key:"本地",value:100},{key:"外地",value:100},{key:"人户分离",value:100}];
    var jia=[{key:"纯老",value:100},{key:"独居",value:100},{key:"失独",value:100},{key:"孤老",value:100},{key:"一老养一老",value:100},{key:"三支人员",value:100},{key:"其他",value:100}];


    createPieChart("男女分布",sex,document.getElementById('oldmanAge'),oldmanSelect);
    createPieChart("年龄分布",age,document.getElementById('oldmanSex'),oldmanSelect);
    createPieChart("户籍分布",huji,document.getElementById('oldmanHuji'),oldmanSelect);
    createPieChart("家庭结构分布",jia,document.getElementById('oldmanJia'),oldmanSelect);

}


/**
 * 获取老人
 */
function createOldman(data) {
    for(var i=0;i<data.length;i++){
        var li="<li onclick='oldmanInfo("+data[i].oid+")'><span class='word' style='font-size: larger;'>"+data[i].name+"</span></li>";
        $("#oldmanList").append(li);
    }
}

/**
 * 获取服务人员
 */
function createWorker(data) {
    for(var i=0;i<data.length;i++){
        var li="<li onclick='oldmanInfo("+data[i].oid+")'><span class='word' style='font-size: larger;'>"+data[i].name+"</span></li>";
        $("#workerNameList").append(li);
    }
}