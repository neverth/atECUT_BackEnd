package cn.atecut.dao;

import cn.atecut.bean.po.Student;
import cn.atecut.bean.pojo.Fields;
import cn.atecut.bean.pojo.UserCookie;
import cn.atecut.util.CookiesUtil;
import cn.atecut.util.RequestUtil;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author NeverTh
 */
@Repository
public class JwDao {
    private static Logger logger = LogManager.getLogger(JwDao.class);

    private OkHttpClient client = RequestUtil.getOkHttpInstanceNotRedirect();

    private AtomicInteger depth = new AtomicInteger(0);

    @Autowired
    private UserCookieImplDao userCookieImplDao;

    private String getEhallAppCookie(Student student, String type, boolean needNewCookie) throws NoSuchMethodException, ScriptException, IOException {

//        depth.incrementAndGet();
//        if (depth.get() > 2){
//            return null;
//        }

        String appConfigUrl = "";
        String  authServerUrl = "https://authserver.ecut.edu.cn/authserver/login?service=https%3A%2F%2Fehall.ecut.edu.cn%3A443%2Fpsfw%2Fsys%2Fpswdkbapp%2F*default%2Findex.do";
        if (type.equals(Fields.EHALL_KB)){
            appConfigUrl = "https://ehall.ecut.edu.cn/psfw/sys/xgpspubapp/indexmenu/getAppConfig.do?appId=5395950742020172&appName=pswdkbapp";

        }else if(type.equals(Fields.EHALL_SCORE)){
            appConfigUrl = "https://ehall.ecut.edu.cn/psfw/sys/xgpspubapp/indexmenu/getAppConfig.do?appId=5393288982814459&appName=pscjcxapp";
        }

        UserCookie authServerCookie = null;

        if(needNewCookie){
            authServerCookie = userCookieImplDao.getOkCookieByUserNumAndType(student, Fields.AUTHSERVER);

        }else{
            authServerCookie = userCookieImplDao.getCookieByUserNumAndTypeNoCheck(student, Fields.AUTHSERVER);
        }
        if (authServerCookie == null){
            authServerCookie = userCookieImplDao.getOkCookieByUserNumAndType(student, Fields.AUTHSERVER);
        }

        CookiesUtil cookiesUtil = new CookiesUtil();
        try {
            Request request = new Request.Builder()
                    .url(authServerUrl)
                    .addHeader("cookie", authServerCookie.getUserCookies())
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            String location = response.headers().get("Location");

            for (int i = 0; i < 10; i++) {

                if (location == null){ break; }

                request = new Request.Builder()
                        .url(location)
                        .addHeader("cookie", cookiesUtil.toString())
                        .get()
                        .build();

                response = client.newCall(request).execute();
                cookiesUtil.add(response.headers().values("Set-Cookie"));
                location = response.headers().get("Location");
            }

            request = new Request.Builder()
                    .url(appConfigUrl)
                    .addHeader("cookie", cookiesUtil.toString())
                    .post(RequestBody.create("",
                            MediaType.get("application/json; charset=utf-8")))
                    .build();
            response = client.newCall(request).execute();
            cookiesUtil.add(response.headers().values("Set-Cookie"));
            if (!cookiesUtil.toString().contains("_WEU")){
                throw new Exception("cookies无效");
            }

        }catch (Exception e){
            return getEhallAppCookie(student, type, true);
        }
        return cookiesUtil.toString();

    }

    public String getEhallKbData(Student student, String reqData) throws NoSuchMethodException, ScriptException, IOException {
        String ehallKbCookie = getEhallAppCookie(student, Fields.EHALL_KB, false);

        RequestBody requestBody = RequestBody.create( "data=" + reqData,
                MediaType.get("application/x-www-form-urlencoded; charset=UTF-8"));

        Request request = new Request.Builder()
                .url("https://ehall.ecut.edu.cn/psfw/sys/pswdkbapp/wdkbcx/getWdkbxx.do")
                .addHeader("cookie", ehallKbCookie)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    public String getEhallScoreData(Student student, String reqData) throws NoSuchMethodException, ScriptException, IOException {
        String ehallScoreCookie = getEhallAppCookie(student, Fields.EHALL_SCORE, false);

        RequestBody requestBody = RequestBody.create(reqData,
                MediaType.get("application/x-www-form-urlencoded; charset=UTF-8"));

        Request request = new Request.Builder()
                .url("https://ehall.ecut.edu.cn/psfw/sys/pscjcxapp/modules/cjcx/cxxscj.do")
                .addHeader("cookie", ehallScoreCookie)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    public static void main(String[] args) {

    }

}
