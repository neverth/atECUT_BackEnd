package cn.atecut.dao;

import cn.atecut.bean.BookInfo;
import cn.atecut.bean.User;
import cn.atecut.bean.po.BorrowBookPO;
import cn.atecut.bean.po.Student;
import cn.atecut.bean.pojo.Fields;
import cn.atecut.bean.pojo.UserCookie;
import cn.atecut.bean.vo.BooksInfo;
import cn.atecut.bean.vo.BorrowBookVO;
import cn.atecut.unti.RequestUtil;
import cn.atecut.unti.SerializableOkHttpCookies;
import cn.atecut.unti.WebVpnOneOp;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 华
 */

@Repository
public class LibraryDao {

    private static Logger logger = LogManager.getLogger(LibraryDao.class);

    private OkHttpClient client = RequestUtil.getOkHttpInstanceNotRedirect();

    @Autowired
    private UserCookieImplDao userCookieImplDao;

    public List<BorrowBookPO> getBorrowBookInfo(Student student) throws NoSuchMethodException, ScriptException, IOException {

        UserCookie libraryCookie = userCookieImplDao.getCookieByUserNumAndTypeNoCheck(student, Fields.LIBRARY);

        if (libraryCookie == null){
            libraryCookie = userCookieImplDao.getOkCookieByUserNumAndType(student, Fields.LIBRARY);
        }

        String htmlBody = "";
        try{
            Request request = new Request.Builder()
                    .url("https://172-20-135-5-8080.webvpn1.ecit.cn/reader/book_lst.php")
                    .addHeader("cookie", libraryCookie.getUserCookies())
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            htmlBody =  Objects.requireNonNull(response.body()).string();

            if(!htmlBody.contains("注销") || response.code() != 200){
                throw new Exception("cookies无效");
            }

        }catch (Exception e){
            libraryCookie = userCookieImplDao.getOkCookieByUserNumAndType(student, Fields.LIBRARY);

            Request request = new Request.Builder()
                    .url("https://172-20-135-5-8080.webvpn1.ecit.cn/reader/book_lst.php")
                    .addHeader("cookie", libraryCookie.getUserCookies())
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            htmlBody =  Objects.requireNonNull(response.body()).string();
        }

        Document doc = Jsoup.parse(htmlBody);
        Element formElement = doc.getElementById("mylib_content");
        Elements tableElements = formElement.getElementsByTag("table");

        if (tableElements.size() == 0){
            return new ArrayList<>();
        }
        Elements trs = tableElements.get(0).getElementsByTag("tr");


        ArrayList<String> borrowInfo = new ArrayList<>();
        for (Element tr : trs) {
            Elements tds = tr.getElementsByTag("td");
            for (Element td : tds){
                borrowInfo.add(td.text());
            }
        }

        List<BorrowBookPO> result = new ArrayList<>();
        for (int i = 1; i < trs.size(); i++) {
            result.add(new BorrowBookPO(
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
