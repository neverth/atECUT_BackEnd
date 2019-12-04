package cn.atecut.dao;


import cn.atecut.unti.SerializableOkHttpCookies;
import okhttp3.Cookie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:spring/spring*.xml"})
public class LibraryDaoTest {

    @Autowired
    LibraryDao libraryDao;

    @Test
    public void parseCookiesTest() throws IOException, ClassNotFoundException {
        SerializableOkHttpCookies serializableOkHttpCookies = new SerializableOkHttpCookies(null);
        String path = System.getProperties().getProperty("user.home")
                + File.separator + ".atecut" + File.separator + "cookies" + File.separator
                 + "201720180702cookies";
        try{
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            serializableOkHttpCookies.readObject(ois);
            fis.close();
            ois.close();
        }catch (IOException | ClassNotFoundException e){

        }
        List<Cookie> cookieList = serializableOkHttpCookies.getCookies();
        System.out.println(cookieList.toString().length());

        String temp = SerializableOkHttpCookies.parseCookies(cookieList);
        System.out.println(temp);
        System.out.println(temp.length());
        List<Cookie> cookieList1 = SerializableOkHttpCookies.parseCookies(temp);
        System.out.println(cookieList1.toString().length());

    }
}
