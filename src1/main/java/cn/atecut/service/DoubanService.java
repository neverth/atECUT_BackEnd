package cn.atecut.service;

import cn.atecut.dao.DoubanDao;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoubanService {

    @Autowired
    DoubanDao doubanDao;

    private Logger logger = LogManager.getLogger(DoubanService.class);

    public String getBookInfoByIsbn(String isbn){
        JSONObject respJson = new JSONObject();
        String result = doubanDao.getBookInfoByIsbn(isbn);

        if(result != null){
            respJson.put("status", 200);
            respJson.put("message", "success");
            respJson.put("data", JSONObject.parseObject(result));
        }

        return respJson.toJSONString();
    }
}
