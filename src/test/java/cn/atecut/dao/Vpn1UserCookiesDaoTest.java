package cn.atecut.dao;

import cn.atecut.bean.User;
import cn.atecut.bean.model.Vpn1UserCookies;
import cn.atecut.unti.SerializableOkHttpCookies;
import okhttp3.Cookie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:spring/spring*.xml"})
public class Vpn1UserCookiesDaoTest {

    @Autowired
    Vpn1UserCookiesDao vpn1UserCookiesDao;

    @Test
    public void selectAllUserCookiesTest(){
        ArrayList<Vpn1UserCookies> a =
        vpn1UserCookiesDao.selectAllUserCookies();
        System.out.println(a);
    }

    @Test
    public void selectUserCookiesByUserTest(){
        ArrayList<Vpn1UserCookies> a =
                vpn1UserCookiesDao.selectUserCookiesByUser(new User("201720180702", ""));
        List<Cookie> b = SerializableOkHttpCookies.parseCookies(a.get(0).getUserCookies());
        System.out.println(a);
    }

    @Test
    public void insertUserCookiesTest(){
        Vpn1UserCookies vpn1UserCookies = new Vpn1UserCookies();
        vpn1UserCookies.setUserNumber("201720180702");
        vpn1UserCookies.setUserCookies("test");
        vpn1UserCookies.setCreatTime(new Timestamp(System.currentTimeMillis()));
        vpn1UserCookies.setVersion(1);
        vpn1UserCookiesDao.insertUserCookies(vpn1UserCookies);
    }

    @Test
    public void deleteUserCookiesTest(){
        Vpn1UserCookies vpn1UserCookies = new Vpn1UserCookies();
        vpn1UserCookies.setUserNumber("201720180702");
        vpn1UserCookies.setUserCookies("test");
        vpn1UserCookies.setCreatTime(new Timestamp(System.currentTimeMillis()));
        vpn1UserCookies.setVersion(1);
        vpn1UserCookiesDao.deleteUserCookies(vpn1UserCookies);
    }
    @Test
    public void updateUserCookiesTest(){
        Vpn1UserCookies vpn1UserCookies = new Vpn1UserCookies();
        vpn1UserCookies.setUserCookies("nimeide");
        vpn1UserCookies.setCreatTime(new Timestamp(System.currentTimeMillis()));
        vpn1UserCookies.setVersion(1);
        vpn1UserCookiesDao.updateUserCookies(new User("test", "") ,vpn1UserCookies);
    }
}
