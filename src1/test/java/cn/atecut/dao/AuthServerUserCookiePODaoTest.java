package cn.atecut.dao;


import cn.atecut.bean.User;
import cn.atecut.bean.po.AuthServerCookies;
import cn.atecut.unti.SerializableOkHttpCookies;
import okhttp3.Cookie;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@MapperScan("cn.atecut.dao")
public class AuthServerUserCookiePODaoTest {

    @Autowired
    AuthServerCookiesDao  authServerCookiesDao;

    @Test
    public void selectAllUserCookiesTest(){
        ArrayList<AuthServerCookies> a =
                authServerCookiesDao.selectAllUserCookies();
        System.out.println(a);
    }

    @Test
    public void selectUserCookiesByUserTest(){
        ArrayList<AuthServerCookies> a =
                authServerCookiesDao.selectUserCookiesByUser(new User("201720180702", ""));
        List<Cookie> b = SerializableOkHttpCookies.parseCookies(a.get(0).getUserCookies());
        System.out.println(a);
    }

    @Test
    public void insertUserCookiesTest(){
        AuthServerCookies vpn1UserCookies = new AuthServerCookies();
        vpn1UserCookies.setUserNumber("201720180702");
        vpn1UserCookies.setUserCookies("test1");
        vpn1UserCookies.setCreatTime(new Timestamp(System.currentTimeMillis()));
        vpn1UserCookies.setVersion(1);
        authServerCookiesDao.insertUserCookies(vpn1UserCookies);
    }

    @Test
    public void deleteUserCookiesTest(){
        AuthServerCookies vpn1UserCookies = new AuthServerCookies();
        vpn1UserCookies.setUserNumber("201720180702");
        vpn1UserCookies.setUserCookies("test");
        vpn1UserCookies.setCreatTime(new Timestamp(System.currentTimeMillis()));
        vpn1UserCookies.setVersion(1);
        authServerCookiesDao.deleteUserCookies(vpn1UserCookies);
    }

    @Test
    public void updateUserCookiesTest(){
        AuthServerCookies vpn1UserCookies = new AuthServerCookies();
        vpn1UserCookies.setUserCookies("nimeide1");
        vpn1UserCookies.setCreatTime(new Timestamp(System.currentTimeMillis()));
        vpn1UserCookies.setVersion(1);
        authServerCookiesDao.updateUserCookies(new User("test", "") ,vpn1UserCookies);
    }
}
