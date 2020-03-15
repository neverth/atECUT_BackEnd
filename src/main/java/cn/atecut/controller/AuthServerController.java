package cn.atecut.controller;


import cn.atecut.bean.User;
import cn.atecut.result.CodeMsg;
import cn.atecut.result.Result;
import cn.atecut.service.AuthServerCookiesService;
import io.swagger.annotations.Api;
import okhttp3.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/authServer")
@Api(value = "authServerCookies接口", tags = "authServerCookies接口")
public class AuthServerController {

    @Autowired
    AuthServerCookiesService authServerCookiesService;

    @PostMapping("/cookies")
    public Result getCookiesByUserName(@RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                    HttpServletResponse httpServletResponse) {
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        List<Cookie> userCookies;
        try {
            userCookies = authServerCookiesService.getUserCookiesFromDataBase(
                    new User(username, password));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        if (userCookies != null){
            return Result.success(userCookies);
        }

        return Result.success();
    }
}
