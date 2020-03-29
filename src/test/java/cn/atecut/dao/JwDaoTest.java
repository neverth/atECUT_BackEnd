package cn.atecut.dao;

import cn.atecut.bean.po.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.script.ScriptException;
import java.io.IOException;

@SpringBootTest
public class JwDaoTest {
    @Autowired
    JwDao jwDao;

    @Test
    public void getEhallKbData() throws NoSuchMethodException, ScriptException, IOException {
        jwDao.getEhallKbData(new Student("201720180702", "ly19980911"),
                "%7B%22XN%22%3A%222019%22%2C%22XQ%22%3A%222%22%2C%22ZC%22%3A7%2C%22SJBZ%22%3A%221%22%7D");
    }
}
