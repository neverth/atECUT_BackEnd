package cn.atecut.dao;

import cn.atecut.bean.BookInfo;
import cn.atecut.bean.vo.BooksInfo;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Repository
public class LibraryImproveDao {

    private static Logger logger = LogManager.getLogger(LibraryImproveDao.class);

    private List<Cookie> cookieList;

    private static OkHttpClient client;

    public LibraryImproveDao() {
        client = new OkHttpClient
                .Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) { }

                    @NotNull
                    @Override
                    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                        return cookieList;
                    }
                })
                .build();
    }

    public void setCookieList(List<Cookie> cookieList) {
        this.cookieList = cookieList;
    }

    public BooksInfo getBooksByTitle(JSONObject object) throws IOException, NullPointerException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(object.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://172-20-135-5-8080.webvpn1.ecit.cn/opac/ajax_search_adv.php")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        return (BooksInfo)processingData(1, response);
    }

    public List<BookInfo.BookNum> getBooksNumByMarc(String marcNo) throws IOException {
        Request request = new Request.Builder()
                .url("https://172-20-135-5-8080.webvpn1.ecit.cn/opac/ajax_item.php?marc_no=" + marcNo)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return (List<BookInfo.BookNum>)processingData(2, response);
    }

    public boolean isCookiesValid(){
        Request request = new Request.Builder()
                .url("https://172-20-135-5-8080.webvpn1.ecit.cn/opac/ajax_item.php?marc_no")
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            String htmlBody =  Objects.requireNonNull(response.body()).string();
            logger.info("cookies" + (htmlBody.contains("书刊状态") ? "有效" : "无效")+ htmlBody.substring(0, 30).trim());
            return htmlBody.contains("书刊状态");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            logger.debug("cookies有效性检查失败");
        }
        return false;
    }

    private Object processingData(int type, Response ... responses) throws IOException, NullPointerException {
        if(type == 1){
            if (responses.length != 1){
                return null;
            }
            BooksInfo booksInfo = new BooksInfo();
            BookInfo bookInfo = new BookInfo();
            List<BookInfo> bookInfoList = new ArrayList<>();


            JSONObject source = null;
            String a = Objects.requireNonNull(responses[0].body()).string();
            logger.info(a.substring(0, 50));
            source = JSONObject.parseObject(a);

            booksInfo.setTotal(source.getInteger("total"));
            source.getJSONArray("content").forEach((item) ->{
                bookInfoList.add(JSONObject.parseObject(item.toString(), BookInfo.class));
            });

            booksInfo.setBookInfos(bookInfoList);

            return booksInfo;
        }
        if(type == 2){

            if (responses.length != 1){
                return null;
            }
            List<BookInfo.BookNum> bookNumList = new ArrayList<>();

            String source = Objects.requireNonNull(responses[0].body()).string();

            Document doc = Jsoup.parse(source);
            Elements trEles = doc.getElementById("item").getElementsByTag("tr");

            for (int i = 1; i < trEles.size(); i++) {
                BookInfo.BookNum bookNum = new BookInfo.BookNum();
                Elements tdEles = trEles.get(i).select("td");

                bookNum.setIndexNum(tdEles.get(0).text());
                bookNum.setBarcode(tdEles.get(1).text());
                bookNum.setHoldings(tdEles.get(3).text());
                bookNum.setState(tdEles.get(4).text());

                bookNumList.add(bookNum);
            }
            return bookNumList;
        }
        return null;
    }
}
