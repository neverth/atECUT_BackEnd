package cn.atecut.dao;

import cn.atecut.unti.RequestUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Objects;

/**
 * @author liyang
 */
@Repository
public class DoubanDao {
    private static Logger logger = LogManager.getLogger(DoubanDao.class);

    private static OkHttpClient client;

    public DoubanDao() {
        client = RequestUtil.getOkHttpInstanceNotRedirect();
    }

    public String getNumByISBN(String isbn) throws IOException {

        Request request = new Request.Builder()
                .url("https://book.douban.com/isbn/" + isbn)
                .get()
                .build();

        String location = "";
        Response response = null;

        for (int i = 0; i < 10; i++) {
            response = client.newCall(request).execute();
            location = response.headers().get("Location");

            if(location == null){
                return null;
            }

            if (location.contains("https://book.douban.com/subject/")){
                break;
            }
            request = new Request.Builder()
                    .url(location)
                    .get()
                    .build();
        }

        return location.substring(location.indexOf("/subject/") + 9, location.length() - 1);
    }

    public String getBookInfoByNum(String number) throws IOException {

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

        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    public String getBookInfoByIsbn(String isbn) throws IOException {
        return getBookInfoByNum(getNumByISBN(isbn));
    }

    public static void main(String[] args) throws IOException {
        DoubanDao doubanDao = new DoubanDao();
        System.out.println(doubanDao.getBookInfoByIsbn("9787532125944"));
    }
}
