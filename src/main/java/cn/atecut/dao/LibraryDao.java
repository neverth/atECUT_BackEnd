package cn.atecut.dao;

import cn.atecut.bean.BookInfo;
import cn.atecut.bean.User;
import cn.atecut.bean.vo.BooksInfo;
import cn.atecut.unti.SerializableOkHttpCookies;
import cn.atecut.unti.WebVpnOneOp;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import com.alibaba.fastjson.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 华
 */

@Repository
public class LibraryDao {

    private static Logger logger = LogManager.getLogger(WebVpnOneOp.class);

    private List<Cookie> cookieList;

    private static OkHttpClient client;

    private static int maxErrorTimes;

    public LibraryDao() {

        cookieList = null;

        maxErrorTimes = 0;

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


    private boolean getCookies(User user){
        int error = 0;
        if (maxErrorTimes > 3){
            return false;
        }

        if (cookieList == null){

            FileInputStream fis = null;
            ObjectInputStream ois = null;
            SerializableOkHttpCookies serializableOkHttpCookies = new SerializableOkHttpCookies(null);

            try{
                fis = new FileInputStream("D:\\" + user.getNumber() + "cookies");
                ois = new ObjectInputStream(fis);
                serializableOkHttpCookies.readObject(ois);

                fis.close();
                ois.close();
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                error = 1;
            }

            cookieList = serializableOkHttpCookies.getCookies();
        }

        Request request = new Request.Builder()
                .url("https://172-20-135-5-8080.webvpn1.ecit.cn/opac/ajax_item.php" +
                        "?marc_no=454936706d4f366c495a444679792f49546f795262513d3d")
                .get()
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
            error = 1;

        }
        String htmlBody = null;
        try {
            htmlBody =  response.body().toString();

        } catch (Exception e) {
            e.printStackTrace();
            error = 1;

        }
        if (htmlBody == null || error == 1){
            WebVpnOneOp webVpnOneOp = WebVpnOneOp.getInstance();
            webVpnOneOp.userLogin(user);
            maxErrorTimes ++;
            getCookies(user);
        }

        return cookieList != null;
    }



    public BooksInfo getBooksByTitle(User user, JSONObject object){

        if(!getCookies(user)){
            return null;
        }

        MediaType JSON
                = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(object.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://172-20-135-5-8080.webvpn1.ecit.cn/opac/ajax_search_adv.php")
                .post(requestBody)
                .build();
        Response response = null;
        String htmlBody = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        BooksInfo booksInfo = (BooksInfo)processingData(1, response);

//        booksInfo.getBookInfos().forEach((item)->{
//            List<BookInfo.BookNum> bookNumList = new ArrayList<>();
//            bookNumList = (List<BookInfo.BookNum>)getBooksNumByMarc(item.getMarcRecNo());
//            item.setBookNums(bookNumList);
//        });

        return booksInfo;
    }


    private Object getBooksNumByMarc(String marcNo){
        Request request = new Request.Builder()
                .url("https://172-20-135-5-8080.webvpn1.ecit.cn/opac/ajax_item.php?marc_no=" + marcNo)
                .get()
                .build();
        Response response = null;
        String htmlBody = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return processingData(2, response);
    }

    private Object processingData(int type, Response ... responses){

        if(type == 1){
            if (responses.length != 1){
                return null;
            }

            BooksInfo booksInfo = new BooksInfo();
            BookInfo bookInfo = new BookInfo();
            List<BookInfo> bookInfoList = new ArrayList<>();


            JSONObject source = null;
            try {
                source = JSONObject.parseObject(responses[0].body().string());

            } catch (Exception e) {
                e.printStackTrace();
            }

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


            String source = null;
            try {
                source = responses[0].body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

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
