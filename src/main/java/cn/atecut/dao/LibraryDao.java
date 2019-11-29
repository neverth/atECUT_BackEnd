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

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 华
 */

@Repository
public class LibraryDao {

    private static Logger logger = LogManager.getLogger(LibraryDao.class);

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
        boolean flag = false;

        if (maxErrorTimes > 3){
            return false;
        }

        if (cookieList == null){
            logger.debug("从磁盘获取用户cookies");
            SerializableOkHttpCookies serializableOkHttpCookies = new SerializableOkHttpCookies(null);
            String path = System.getProperties().getProperty("user.home")
                    + File.separator + ".atecut" + File.separator + "cookies" + File.separator
                    + user.getNumber() + "cookies";
            try{
                FileInputStream fis = new FileInputStream(path);
                ObjectInputStream ois = new ObjectInputStream(fis);
                serializableOkHttpCookies.readObject(ois);
                fis.close();
                ois.close();
            }catch (IOException | ClassNotFoundException e){
                logger.debug(e.getMessage());
                logger.debug("从磁盘获取cookies失败");
                error = 1;
            }
            cookieList = serializableOkHttpCookies.getCookies();
        }


        Request request = new Request.Builder()
                .url("https://172-20-135-5-8080.webvpn1.ecit.cn/opac/ajax_item.php?marc_no")
                .get()
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            logger.debug("cookies有效性检查失败");
            error = 1;
        }

        String htmlBody = null;
        try {
            htmlBody =  Objects.requireNonNull(response.body()).string();
        } catch (NullPointerException | IOException e) {
            error = 1;
        }
        try {
            flag = htmlBody.contains("书刊状态");
            flag = true;
        } catch (NullPointerException e) {
            logger.debug("cookies有效性检查失败，htmlBody中不包含 “书刊状态”");
            error = 1;
        }

        if (!flag || error == 1){
            logger.debug("cookies错误或失效，重新获取cookies " + maxErrorTimes);
            WebVpnOneOp webVpnOneOp = WebVpnOneOp.getInstance();
            webVpnOneOp.userLogin(user);
            maxErrorTimes ++;
            flag = getCookies(user);
        }
        return flag;
    }



    public BooksInfo getBooksByTitle(User user, JSONObject object){

        if(!getCookies(user)){
            return null;
        }
        logger.debug("用户cookies有效");

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


    public List<BookInfo.BookNum> getBooksNumByMarc(User user, String marcNo){

        if(!getCookies(user)){
            return null;
        }

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
        return (List<BookInfo.BookNum>)processingData(2, response);
    }

    private Object processingData(int type, Response ... responses){
        logger.debug("正在处理type为" + type + "responses");
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
                logger.debug("转换为json失败");
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
