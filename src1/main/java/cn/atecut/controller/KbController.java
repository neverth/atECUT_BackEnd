package cn.atecut.controller;


import cn.atecut.bean.User;
import cn.atecut.service.AuthServerCookiesService;
import cn.atecut.service.KbService;
import okhttp3.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequestMapping("restfulApi/kb")
@Controller
public class KbController {

    @Autowired
    KbService kbService;

    @Autowired
    AuthServerCookiesService authServerCookiesService;

    @RequestMapping(value = "{userid}", produces = "application/json; charset=UTF-8",
            method = RequestMethod.GET)
    @ResponseBody
    public String handleGetBooksByTitleApi(@PathVariable String userid,
                                    HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) throws IOException, ScriptException, NoSuchMethodException {
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");

        List<Cookie> cookies =
                authServerCookiesService.getUserCookiesFromDataBase(new User(userid, " "));

        if (cookies.isEmpty()){
            cookies =
                    authServerCookiesService.getUserCookiesNew(new User(userid, "ly19980911"));
            authServerCookiesService.putCookiesToDataBase(new User(userid, "ly19980911"), cookies);
        }
        return kbService.getKbByUser(cookies, new User(userid, " "));
    }
}
