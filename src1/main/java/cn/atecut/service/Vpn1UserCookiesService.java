package cn.atecut.service;


import cn.atecut.bean.User;
import cn.atecut.bean.po.Vpn1UserCookies;
import cn.atecut.dao.Vpn1UserCookiesDao;
import cn.atecut.unti.SerializableOkHttpCookies;
import cn.atecut.unti.WebVpnOneOp;
import okhttp3.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class Vpn1UserCookiesService {

    @Autowired
    Vpn1UserCookiesDao vpn1UserCookiesDao;

    private static Logger logger = LogManager.getLogger(Vpn1UserCookiesService.class);

    public List<Cookie> getUserCookies(User user){
        return null;
    }

    public List<Cookie> getUserCookiesFromDataBase(User user){
        logger.debug("正在从数据库中获取用户"+ user.getNumber() + "cookies ");
        ArrayList<Vpn1UserCookies> userCookiesList =
                vpn1UserCookiesDao.selectUserCookiesByUser(user);

        if(userCookiesList.size() == 0){
            return new ArrayList<>();
        }

        Vpn1UserCookies userCookies = userCookiesList.get(0);
        for (Vpn1UserCookies vpn1UserCookies : userCookiesList) {
            if(userCookies.getCreatTime().getTime() < vpn1UserCookies.getCreatTime().getTime()){
                userCookies =  vpn1UserCookies;
            }
        }
        return SerializableOkHttpCookies.parseCookies(userCookies.getUserCookies());
    }

    public List<Cookie> getUserCookiesNew(User user) throws IOException {
        logger.debug("用户重新登录并获取cookies");
        return WebVpnOneOp.getInstance().getUserValidCookies(user);
    }

    public int putCookiesToDataBase(User user, List<Cookie> cookies){
        logger.debug("插入用户 "+ user.getNumber() + "cookies到数据库中");
        Vpn1UserCookies vpn1UserCookies = new Vpn1UserCookies();
        vpn1UserCookies.setUserNumber(user.getNumber());
        vpn1UserCookies.setUserCookies(SerializableOkHttpCookies.parseCookies(cookies));
        vpn1UserCookies.setCreatTime(new Timestamp(System.currentTimeMillis()));
        vpn1UserCookies.setVersion(1);
        return vpn1UserCookiesDao.insertUserCookies(vpn1UserCookies);
    }

    public int updateUserCookies(User user, List<Cookie> cookies, int version){
        logger.debug("更新用户 "+ user.getNumber() + "cookies到数据库中");
        Vpn1UserCookies vpn1UserCookies = new Vpn1UserCookies();
        vpn1UserCookies.setUserNumber(user.getNumber());
        vpn1UserCookies.setUserCookies(SerializableOkHttpCookies.parseCookies(cookies));
        vpn1UserCookies.setCreatTime(new Timestamp(System.currentTimeMillis()));
        vpn1UserCookies.setVersion(version);
        return vpn1UserCookiesDao.updateUserCookies(user, vpn1UserCookies);
    }
}
