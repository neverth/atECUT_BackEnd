package cn.atecut.dao;

import cn.atecut.bean.po.BookPO;
import cn.atecut.bean.po.BorrowBookPO;
import cn.atecut.bean.po.Student;
import cn.atecut.bean.pojo.Fields;
import cn.atecut.bean.pojo.UserCookie;
import cn.atecut.unti.RequestUtil;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author neverth
 */

@Repository
public class LibraryDao {

    private static Logger logger = LogManager.getLogger(LibraryDao.class);

    private OkHttpClient client = RequestUtil.getOkHttpInstanceNotRedirect();

    @Autowired
    private UserCookieImplDao userCookieImplDao;

    private String  getResponse(Student student, String url, String effectiveSign) throws NoSuchMethodException, ScriptException, IOException {

        UserCookie libraryCookie = userCookieImplDao.getCookieByUserNumAndTypeNoCheck(student, Fields.LIBRARY);

        if (libraryCookie == null){
            libraryCookie = userCookieImplDao.getOkCookieByUserNumAndType(student, Fields.LIBRARY);
        }

        String htmlBody = "";
        try{
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("cookie", libraryCookie.getUserCookies())
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            htmlBody =  Objects.requireNonNull(response.body()).string();

            if(!htmlBody.contains(effectiveSign) || response.code() != 200){
                throw new Exception("cookies无效");
            }

        }catch (Exception e){
            libraryCookie = userCookieImplDao.getOkCookieByUserNumAndType(student, Fields.LIBRARY);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("cookie", libraryCookie.getUserCookies())
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            htmlBody =  Objects.requireNonNull(response.body()).string();
        }
        return htmlBody;
    }

    private String  postResponse(Student student,
                                 String url,
                                 String effectiveSign,
                                 String postData,
                                 boolean needNewCookie) throws NoSuchMethodException, ScriptException, IOException {
        UserCookie libraryCookie = null;

        if(needNewCookie){
            libraryCookie = userCookieImplDao.getOkCookieByUserNumAndType(student, Fields.LIBRARY);

        }else{
            libraryCookie = userCookieImplDao.getCookieByUserNumAndTypeNoCheck(student, Fields.LIBRARY);

        }
        if (libraryCookie == null){
            libraryCookie = userCookieImplDao.getOkCookieByUserNumAndType(student, Fields.LIBRARY);
        }

        String htmlBody = "";
        try{
            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(postData, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("cookie", libraryCookie.getUserCookies())
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            htmlBody =  Objects.requireNonNull(response.body()).string();

            if(!htmlBody.contains(effectiveSign) || response.code() != 200){
                throw new Exception("cookies无效");
            }

        }catch (Exception e){
            return postResponse(student, url, effectiveSign, postData, true);
        }
        return htmlBody;
    }

    public List<BorrowBookPO> getBorrowBookInfo(Student student) throws NoSuchMethodException, ScriptException, IOException {

        String htmlBody = getResponse(student,
                "https://172-20-135-5-8080.webvpn1.ecit.cn/reader/book_lst.php",
                "注销");

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

    public List<BookPO.BookBorrowInfo> getBooksNumByMarc(String marcNo) throws IOException, NoSuchMethodException, ScriptException {
        String htmlBody = getResponse(new Student("201720180702", "ly19980911"),
                "https://172-20-135-5-8080.webvpn1.ecit.cn/opac/ajax_item.php?marc_no=" + marcNo,
                "图书明细");

        List<BookPO.BookBorrowInfo> BookBorrowInfos = new ArrayList<>();

        Document doc = Jsoup.parse(htmlBody);
        Elements trEles = doc.getElementById("item").getElementsByTag("tr");

        for (int i = 1; i < trEles.size(); i++) {
            BookPO.BookBorrowInfo bookNum = new BookPO.BookBorrowInfo();
            Elements tdEles = trEles.get(i).select("td");

            bookNum.setIndexNum(tdEles.get(0).text());
            bookNum.setBarcode(tdEles.get(1).text());
            bookNum.setHoldings(tdEles.get(3).text());
            bookNum.setState(tdEles.get(4).text());
            BookBorrowInfos.add(bookNum);
        }
        return BookBorrowInfos;
    }

    public String getBooksByTitle(String object) throws IOException, NullPointerException, NoSuchMethodException, ScriptException {

        String htmlBody = postResponse(new Student("201720180702", "ly19980911"),
                "https://172-20-135-5-8080.webvpn1.ecit.cn/opac/ajax_search_adv.php",
                "total",
                object,
                false);

        return htmlBody;
    }
}
