package cn.atecut.unti;

import cn.atecut.bean.po.Student;
import cn.atecut.bean.pojo.UserCookie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.Objects;

/**
 * @author NeverTh
 */
public class NewJwCookieOp {
    private volatile static NewJwCookieOp instance;

    private Logger logger = LogManager.getLogger(NewJwCookieOp.class);

    private NewJwCookieOp() { }

    public static NewJwCookieOp getInstance() {
        if (instance == null) {
            synchronized (NewJwCookieOp.class) {
                if (instance == null) {
                    instance = new NewJwCookieOp();
                }
            }
        }
        return instance;
    }

    public String getUserValidCookies(Student student, String authServerCookie, String webVpn1Cookie)
            throws IOException, NullPointerException{

        OkHttpClient client = RequestUtil.getOkHttpInstanceNotRedirect();

        Request request = new Request.Builder()
                .url("https://172-20-130-13.webvpn1.ecit.cn/jwglxt/xtgl/index_cxYhxxIndex.html")
                .addHeader("cookie", authServerCookie
                        + webVpn1Cookie)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String jSession = null;
        if (response.headers().get("Set-Cookie") != null){
            jSession = response.headers().get("Set-Cookie").split(";")[0] + ";";
        }

        request = new Request.Builder()
                .url("https://172-20-130-13.webvpn1.ecit.cn/sso/jziotlogin")
                .addHeader("cookie", authServerCookie
                        + webVpn1Cookie + jSession)
                .get()
                .build();

        response = client.newCall(request).execute();
        String location = response.headers().get("Location");


        for (int i = 0; i < 10; i++) {

            if (response.headers().get("Set-Cookie") != null){
                if (response.headers().get("Set-Cookie").contains("JSESSIONID")){
                    jSession += response.headers().get("Set-Cookie").split(";")[0] + ";";
                }
            }

            if(location == null){
                break;
            }

            request = new Request.Builder()
                    .url(location)
                    .addHeader("cookie", authServerCookie
                            + webVpn1Cookie + jSession)
                    .get()
                    .build();

            response = client.newCall(request).execute();
            location = response.headers().get("Location");
        }
        return jSession + webVpn1Cookie;
    }

    public static boolean isUserCookieOk(UserCookie cookie) throws IOException {
        OkHttpClient client = RequestUtil.getOkHttpInstanceNotRedirect();

        Request request = new Request.Builder()
                .url("https://172-20-130-13.webvpn1.ecit.cn/jwglxt/xtgl/index_cxNews.html")
                .addHeader("cookie", cookie.getUserCookies())
                .get()
                .build();
        Response response = client.newCall(request).execute();

        return Objects.requireNonNull(response.body()).string().contains("通知");
    }

    public static void main(String[] args) throws NoSuchMethodException, ScriptException, IOException {
        LibraryCookieOp libraryCookieOp = LibraryCookieOp.getInstance();
//        libraryCookieOp.getUserValidCookies(new Student("201720180702", "ly19980911"));
    }
}
