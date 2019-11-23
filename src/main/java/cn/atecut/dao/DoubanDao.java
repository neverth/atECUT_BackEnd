package cn.atecut.dao;

import cn.atecut.unti.WebVpnOneOp;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liyang
 */
@Repository
public class DoubanDao {
    private static Logger logger = LogManager.getLogger(WebVpnOneOp.class);

    private static OkHttpClient client;

    private static int maxErrorTimes;

    private String number;


    public DoubanDao() {
        client = new OkHttpClient
                .Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) { }

                    @NotNull
                    @Override
                    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                        String urlStr = httpUrl.url().toString();
                        if(urlStr.contains("https://book.douban.com/subject/")){
                            number = urlStr.substring(urlStr.indexOf("/subject/") + 9, urlStr.length() - 1);

                            return null;
                        }
                        return new ArrayList<>();
                    }
                })
                .build();
    }

    public String getNumByISBN(String isbn){
        Request request = new Request.Builder()
                .url("https://book.douban.com/isbn/" + isbn)
                .get()
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return number;
        }

        return null;
    }

    public String  getBookInfoByNum(String number){
        Request request = new Request.Builder()
                .url("https://m.douban.com/rexxar/api/v2/book/" +
                        number + "?ck=&for_mobile=1")
                .get()
                .addHeader("Host", "m.douban.com")
                .addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
                .addHeader("Accept", "application/json,*/*")
                .addHeader("Referer", "https://m.douban.com/book/subject/" +
                        number + "/")
                .build();
        Response response = null;
        String result = null;
        try {
            response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getBookInfoByIsbn(String isbn){
        return getBookInfoByNum(getNumByISBN(isbn));
    }
}
