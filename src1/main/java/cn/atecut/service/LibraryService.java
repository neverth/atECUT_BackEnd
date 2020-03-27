package cn.atecut.service;

import cn.atecut.bean.BookInfo;
import cn.atecut.bean.User;
import cn.atecut.bean.po.Student;
import cn.atecut.bean.pojo.Fields;
import cn.atecut.bean.pojo.UserCookie;
import cn.atecut.bean.vo.BooksInfo;
import cn.atecut.bean.vo.BorrowBookVO;
import cn.atecut.dao.*;
import cn.atecut.unti.RequestUtil;
import cn.atecut.unti.WebVpnOneOp;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.*;

/**
 * @author liyang
 */
@Service
public class LibraryService {

    @Autowired
    LibraryImproveDao libraryImproveDao;

    @Autowired
    UserCookieImplDao userCookieImplDao;

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

    public List<BorrowBookVO> getStuBorrowBookInfo(Student student) throws NoSuchMethodException, ScriptException, IOException {

        OkHttpClient client = RequestUtil.getOkHttpInstanceNotRedirect();


        Request request = new Request.Builder()
                .url("https://172-20-135-5-8080.webvpn1.ecit.cn/reader/book_lst.php")
                .addHeader("cookie", webVpn1Cookie.getUserCookies()
                        + phpSession)
                .get()
                .build();

        Response response = client.newCall(request).execute();

        String htmlBody =  Objects.requireNonNull(response.body()).string();

        Document doc = Jsoup.parse(htmlBody);
        Element formElement = doc.getElementById("mylib_content");
        Elements tableElements = formElement.getElementsByTag("table");
        Elements trs = tableElements.get(0).getElementsByTag("tr");


        ArrayList<String> borrowInfo = new ArrayList<>();
        for (Element tr : trs) {
            Elements tds = tr.getElementsByTag("td");
            for (Element td : tds){
                borrowInfo.add(td.text());
            }
        }

        List<BorrowBookVO> result = new ArrayList<>();
        for (int i = 1; i < trs.size(); i++) {
            result.add(new BorrowBookVO(
                    borrowInfo.get(i * 8),
                    borrowInfo.get(i * 8 + 1),
                    borrowInfo.get(i * 8 + 2),
                    borrowInfo.get(i * 8 + 3),
                    borrowInfo.get(i * 8 + 4),
                    borrowInfo.get(i * 8 + 5)
            ));
        }
        return result;
    }
}
