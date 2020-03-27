package cn.atecut.dao;


import cn.atecut.bean.po.Student;
import cn.atecut.bean.pojo.Fields;
import cn.atecut.bean.pojo.UserCookie;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.script.ScriptException;
import java.io.IOException;

@SpringBootTest
@MapperScan("cn.atecut.dao")
public class UserCookieImplDaoTest {

    @Autowired
    UserCookieImplDao userCookieImplDao;

    @Test
    public void selectByUserNumAndTypeTest() throws NoSuchMethodException, ScriptException, IOException {
        UserCookie userCookie = userCookieImplDao.getOkCookieByUserNumAndType(
                new Student("201720180702", "ly19980911"), Fields.LIBRARY);
        System.out.println(userCookie);
    }

    @Test
    public void getCookieByUserNumAndTypeNoCheckTest() throws NoSuchMethodException, ScriptException, IOException {
        UserCookie userCookie = userCookieImplDao.getCookieByUserNumAndTypeNoCheck(
                new Student("201720180702", "ly19980911"), Fields.WEBVPN1);
        System.out.println(userCookie);
    }

}
