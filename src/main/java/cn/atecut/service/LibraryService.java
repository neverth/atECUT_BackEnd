package cn.atecut.service;

import cn.atecut.bean.BookInfo;
import cn.atecut.bean.User;
import cn.atecut.bean.vo.BooksInfo;
import cn.atecut.dao.LibraryDao;
import cn.atecut.unti.WebVpnOneOp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.alibaba.fastjson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author liyang
 */
@Service
public class LibraryService {

    @Autowired
    private LibraryDao libraryDao;

    private Logger logger = LogManager.getLogger(LibraryService.class);

    public String getBooksByTitle(User user, String title) throws IOException {

        JSONObject requestJson = new JSONObject();
        requestJson.put("filters", new JSONArray());
        requestJson.put("first", true);
        requestJson.put("limiter", new JSONArray());
        requestJson.put("locale", "");
        requestJson.put("pageCount", 1);
        requestJson.put("pageSize", 20);
        requestJson.put("sortField", "relevance");
        requestJson.put("sortType", "desc");

        // fashjson竟然还不支持流式计算
        JSONArray a = new JSONArray();
        JSONObject b = new JSONObject();
        JSONArray c = new JSONArray();
        JSONObject d = new JSONObject();

        a.add(b);
        b.put("fieldList", c);
        c.add(d);
        d.put("fieldCode", "");
        d.put("fieldValue", title);

        requestJson.put("searchWords", a);

        int i = 0;
        while(i < 5){
            BooksInfo result = libraryDao.getBooksByTitle(user, requestJson);
            if(result == null){
                logger.debug("getBooksByTitle失败，正在重试第 " + i+1 + " 次");
                WebVpnOneOp webVpnOneOp = WebVpnOneOp.getInstance();
                webVpnOneOp.userLogin(user);
            }
            i++;
            if(result != null){
                JSONObject respJson = new JSONObject();
                respJson.put("status", 200);
                respJson.put("message", "success");
                respJson.put("data", result);
                return respJson.toJSONString();
            }
        }

        return null;
    }
}
