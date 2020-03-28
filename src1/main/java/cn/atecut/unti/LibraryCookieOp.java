package cn.atecut.unti;

import cn.atecut.bean.po.Student;
import cn.atecut.bean.pojo.UserCookie;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.Objects;

/**
 * @author NeverTh
 */
public class LibraryCookieOp {

    private volatile static LibraryCookieOp instance;

    private Logger logger = LogManager.getLogger(LibraryCookieOp.class);

    private LibraryCookieOp() { }

    public static LibraryCookieOp getInstance() {
        if (instance == null) {
            synchronized (LibraryCookieOp.class) {
                if (instance == null) {
                    instance = new LibraryCookieOp();
                }
            }
        }
        return instance;
    }

    public String getUserValidCookies(Student student, String authServerCookie, String webVpn1Cookie)
            throws IOException, NullPointerException{

        OkHttpClient client = RequestUtil.getOkHttpInstanceNotRedirect();

        Request request = new Request.Builder()
                .url("https://172-20-135-5-8080.webvpn1.ecit.cn/reader/book_lst.php")
                .addHeader("cookie", webVpn1Cookie)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String location = response.headers().get("Location");
        String phpSession = response.headers().get("Set-Cookie").split(";")[0] + ";";

        for (int i = 0; i < 10; i++) {
            if(location == null){
                break;
            }
            String setCookie = response.headers().get("Set-Cookie");

            if(setCookie != null){
                phpSession = setCookie.split(";")[0] + ";";
            }

            request = new Request.Builder()
                    .url(location)
                    .addHeader("cookie", authServerCookie
                            + webVpn1Cookie + phpSession)
                    .get()
                    .build();

            response = client.newCall(request).execute();
            location = response.headers().get("Location");
        }
        return phpSession + webVpn1Cookie;
    }

    public static boolean isUserCookieOk(UserCookie cookie) throws IOException {
        OkHttpClient client = RequestUtil.getOkHttpInstanceNotRedirect();

        Request request = new Request.Builder()
                .url("https://172-20-135-5-8080.webvpn1.ecit.cn/reader/ajax_class_sort.php")
                .addHeader("cookie", cookie.getUserCookies())
                .get()
                .build();
        Response response = client.newCall(request).execute();

        return !Objects.requireNonNull(response.body()).string().contains("Not login");
    }

    public static void main(String[] args) throws NoSuchMethodException, ScriptException, IOException {
        LibraryCookieOp libraryCookieOp = LibraryCookieOp.getInstance();
//        libraryCookieOp.getUserValidCookies(new Student("201720180702", "ly19980911"));
    }
}
