package cn.atecut.service;


import cn.atecut.bean.BookInfo;
import cn.atecut.bean.vo.BooksInfo;
import cn.atecut.dao.LibraryImproveDao;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


/**
 * @author NeverTh
 */

@Service
public class LibraryImproveService {

    @Autowired
    LibraryImproveDao libraryImproveDao;

    public BooksInfo getBooksByTitle(List<Cookie> cookies,
                                     JSONObject requestJson) throws IOException {
        libraryImproveDao.setCookieList(cookies);
        return libraryImproveDao.getBooksByTitle(requestJson);

    }

    public List<BookInfo.BookNum> getBookDetailByNo(List<Cookie> cookies, String marcNo) throws IOException {
        libraryImproveDao.setCookieList(cookies);
        return libraryImproveDao.getBooksNumByMarc(marcNo);
    }

    public boolean isCookiesValid(List<Cookie> cookies){
        libraryImproveDao.setCookieList(cookies);
        return libraryImproveDao.isCookiesValid();
    }
}
