package cn.atecut.dao;


import cn.atecut.bean.po.UserCookiePO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserCookiePODaoTest {

    @Autowired
    UserCookieDao userCookieDao;

    @Test
    public void test(){
//        System.out.println(cookiesDao.selectByUserNum("1"));

//        System.out.println(userCookieDao.save(new UserCookiePO()));
    }

}
