package cn.atecut.service;

import cn.atecut.bean.po.Student;
import cn.atecut.dao.JwDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * @author NeverTh
 */
@Service
public class JwService {

    @Autowired
    JwDao jwDao;

    public String getEhallKbData(Student student, String reqData) throws NoSuchMethodException, ScriptException, IOException {
        return jwDao.getEhallKbData(student, reqData);
    }
}
