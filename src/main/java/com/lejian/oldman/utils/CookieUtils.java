package com.lejian.oldman.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public final class CookieUtils {


    public static String getCookie(HttpServletRequest request,String key){
        for(Cookie cookie:request.getCookies()){
            if(cookie.getName().equals(key)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
