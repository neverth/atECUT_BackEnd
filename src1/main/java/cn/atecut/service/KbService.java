package cn.atecut.service;


import cn.atecut.bean.User;
import cn.atecut.dao.AuthServerCookiesDao;
import cn.atecut.dao.KbDao;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class KbService {

    private KbDao kbDao;

    private AuthServerCookiesDao authServerCookiesDao;

    private Logger logger = LogManager.getLogger(KbService.class);

    @Autowired
    public void setKbDao(KbDao kbDao) {
        this.kbDao = kbDao;
    }

    @Autowired
    public void setAuthServerCookiesDao(AuthServerCookiesDao authServerCookiesDao) {
        this.authServerCookiesDao = authServerCookiesDao;
    }

    public String getKbByUser(List<Cookie> cookies, User user) throws IOException {
        JSONObject requestJson = new JSONObject();
        kbDao.setCookieList(cookies);
        return kbDao.getKbByUser(user, requestJson);
    }
}
