package cn.atecut.service;

import cn.atecut.bean.User;
import cn.atecut.bean.po.AuthServerCookies;
import cn.atecut.dao.AuthServerCookiesDao;
import cn.atecut.util.AuthServerOp;
import cn.atecut.util.SerializableOkHttpCookies;
import okhttp3.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServerCookiesService {

    @Autowired
    AuthServerCookiesDao authServerCookiesDao;

    private static Logger logger = LogManager.getLogger(AuthServerCookiesService.class);

    public List<Cookie> getUserCookies(User user) throws NoSuchMethodException, ScriptException, IOException {
        List<Cookie> cookies = getUserCookiesFromDataBase(user);

        if(cookies == null || !AuthServerOp.isCookiesOk(cookies)){
            cookies = getUserCookiesNew(user);
            if (cookies != null){
                putCookiesToDataBase(user, cookies);
                return cookies;
            }
        }

        return cookies;
    }

    public List<Cookie> getUserCookiesFromDataBase(User user){
        logger.debug("正在从数据库中获取用户"+ user.getNumber() + "cookies ");
        ArrayList<AuthServerCookies> userCookiesList =
                authServerCookiesDao.selectUserCookiesByUser(user);

        if(userCookiesList.size() == 0){
            return new ArrayList<>();
        }

        AuthServerCookies userCookies = userCookiesList.get(0);
        for (AuthServerCookies vpn1UserCookies : userCookiesList) {
            if(userCookies.getCreatTime().getTime() < vpn1UserCookies.getCreatTime().getTime()){
                userCookies =  vpn1UserCookies;
            }
        }
        return SerializableOkHttpCookies.parseCookies(userCookies.getUserCookies());
    }

    public List<Cookie> getUserCookiesNew(User user) throws IOException, ScriptException, NoSuchMethodException {
        logger.debug("用户重新登录并获取cookies");
        return AuthServerOp.getInstance().getUserValidCookies(user);
    }

    public int putCookiesToDataBase(User user, List<Cookie> cookies){
        logger.debug("插入用户 "+ user.getNumber() + "cookies到数据库中");
        AuthServerCookies vpn1UserCookies = new AuthServerCookies();
        vpn1UserCookies.setUserNumber(user.getNumber());
        vpn1UserCookies.setUserCookies(SerializableOkHttpCookies.parseCookies(cookies));
        vpn1UserCookies.setCreatTime(new Timestamp(System.currentTimeMillis()));
        vpn1UserCookies.setVersion(1);
        return authServerCookiesDao.insertUserCookies(vpn1UserCookies);
    }

    public int updateUserCookies(User user, List<Cookie> cookies, int version){
        logger.debug("更新用户 "+ user.getNumber() + "cookies到数据库中");
        AuthServerCookies vpn1UserCookies = new AuthServerCookies();
        vpn1UserCookies.setUserNumber(user.getNumber());
        vpn1UserCookies.setUserCookies(SerializableOkHttpCookies.parseCookies(cookies));
        vpn1UserCookies.setCreatTime(new Timestamp(System.currentTimeMillis()));
        vpn1UserCookies.setVersion(version);
        return authServerCookiesDao.updateUserCookies(user, vpn1UserCookies);
    }

    public static void main(String[] args) {
        AuthServerCookiesService a = new AuthServerCookiesService();
        System.out.println(a.getUserCookiesFromDataBase(new User("201720180702", "")));
    }

}
