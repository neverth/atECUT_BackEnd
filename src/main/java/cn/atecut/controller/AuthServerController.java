package cn.atecut.controller;


import cn.atecut.bean.User;
import cn.atecut.result.CodeMsg;
import cn.atecut.result.Result;
import cn.atecut.service.AuthServerCookiesService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import okhttp3.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/authServer")
@Api(value = "authServerCookies接口", tags = "authServerCookies接口")
public class AuthServerController {

    @Autowired
    AuthServerCookiesService authServerCookiesService;

    @PostMapping("/cookies/{username}")
    public Result getCookiesByUserName(@PathVariable("username") String username, @RequestBody String postParamsSt) {

        JSONObject  postParams = JSONObject.parseObject(postParamsSt);
        List<Cookie> userCookies;
        try {
            userCookies = authServerCookiesService.getUserCookies(
                    new User(username, postParams.getString("password")));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }

        HashMap<String, String> newCookies = new HashMap<>();

        for (Cookie _cookies: userCookies) {
            newCookies.put(_cookies.name(), _cookies.value());
        }

        return Result.success(newCookies);
    }
    @DeleteMapping("/cookies/{username}")
    public Result deleteCookies(@PathVariable("username") String username) {
        return null;
    }

}
