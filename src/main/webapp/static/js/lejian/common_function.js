function newPage(id,name,url) {

    var t=url,
        a="1"+id,
        i=name,
        n=!0;

    var jq=top.jQuery;
    if(void 0==t||0==$.trim(t).length)
        return!1;
    if(
        jq(".J_menuTab").each(
            function(){
                return $(this).data("id")==t?(
                        $(this).hasClass("active")||
                        (
                            $(this).addClass("active").siblings(".J_menuTab").removeClass("active"),
                                jq(".J_mainContent .J_iframe").each(function(){
                                    return $(this).data("id")==t?
                                        ($(this).show().siblings(".J_iframe").hide(),!1)
                                        :
                                        void 0}))
                            ,n=!1,!1)
                    : void 0
            })
            ,n
    ) {
        var s='<a href="javascript:;" class="active J_menuTab" data-id="'+t+'">'+i+' <i class="fa fa-times-circle"></i></a>';
        jq(".J_menuTab").removeClass("active");
        var r='<iframe class="J_iframe" name="iframe'+a+'" width="100%" height="100%" src="'+t+'" frameborder="0" data-id="'+t+'" seamless></iframe>';
        jq(".J_mainContent").find("iframe.J_iframe").hide().parents(".J_mainContent").append(r);
        jq(".J_mainContent iframe:visible").load(function(){layer.close(o)});
        jq(".J_menuTabs .page-tabs-content").append(s);
        e(jq(".J_menuTab.active"));
    }
    return!1
}



function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
    }
    return(false);
}