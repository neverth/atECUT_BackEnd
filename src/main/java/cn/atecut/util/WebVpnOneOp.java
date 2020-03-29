package cn.atecut.util;

import cn.atecut.bean.User;
import cn.atecut.bean.pojo.UserCookie;
import lombok.Data;
import lombok.ToString;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author NeverTh
 * @description webVpn1登录选项
 * @date 11:39 2019/11/25
 */

public class WebVpnOneOp {

    private volatile static WebVpnOneOp instance;

    private List<Cookie> cookies = new ArrayList<>();

    private OkHttpClient client;

    private Logger logger = LogManager.getLogger(WebVpnOneOp.class);

    @Data
    @ToString
    public class WebVpnOneLoginInfo {
        String utf8;
        String authenticity_token;
        String username;
        String password;
        String dymatice_code;
        String commit;
    }

    private WebVpnOneOp() {
//        cookiesMonitor();
    }

    public static WebVpnOneOp getInstance() {
        if (instance == null) {
            synchronized (WebVpnOneOp.class) {
                if (instance == null) {
                    instance = new WebVpnOneOp();
                }
            }
        }
        return instance;
    }

    private WebVpnOneLoginInfo getLoginInfo(User user) throws IOException, NullPointerException {

        logger.debug("正在请求登录页面获取登录参数");

        WebVpnOneLoginInfo webVpnOneLoginInfo = new WebVpnOneLoginInfo();
        webVpnOneLoginInfo.setUtf8("✓");
        webVpnOneLoginInfo.setUsername(user.getNumber());
        webVpnOneLoginInfo.setPassword(user.getPassword());
        webVpnOneLoginInfo.setDymatice_code("unknown");
        webVpnOneLoginInfo.setCommit("登录+Login");

        client = new OkHttpClient
                .Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                        List<Cookie> temp = new ArrayList<>();
                        for (Cookie cookie : list) {
                            for (Cookie cookie1 : cookies) {
                                if (cookie.name().equals(cookie1.name())) {
                                    temp.add(cookie1);
                                }
                            }
                        }
                        cookies.removeAll(temp);
                        cookies.addAll(list);
                    }
                    @NotNull
                    @Override
                    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                        return cookies;
                    }
                })
                .build();

        Request request = new Request.Builder()
                .url("https://webvpn1.ecit.cn/users/sign_in")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String htmlBody = Objects.requireNonNull(response.body()).string();
        Document doc = Jsoup.parse(htmlBody);
        Element formElement = doc.getElementById("login-form");
        Elements inputElements = formElement.getElementsByTag("input");
        for (Element inputElement : inputElements) {
            if ("authenticity_token".equals(inputElement.attr("name"))) {
                webVpnOneLoginInfo.setAuthenticity_token(inputElement.attr("value"));
            }
        }
        return webVpnOneLoginInfo;
    }

    public List<Cookie> getUserValidCookies(User user) throws IOException, NullPointerException {

        logger.debug(user.getNumber() + "用户正在登陆并获取Cookies");

        cookies.clear();

        WebVpnOneLoginInfo webVpnOneLoginInfo = getLoginInfo(user);

        RequestBody requestBody = new FormBody.Builder()
                .add("authenticity_token", webVpnOneLoginInfo.getAuthenticity_token())
                .add("commit", webVpnOneLoginInfo.getCommit())
                .add("user[dymatice_code]", webVpnOneLoginInfo.getDymatice_code())
                .add("user[password]", webVpnOneLoginInfo.getPassword())
                .add("user[login]", webVpnOneLoginInfo.getUsername())
                .add("utf8", webVpnOneLoginInfo.getUtf8())
                .build();

        Request request = new Request.Builder()
                .url("https://webvpn1.ecit.cn/users/sign_in")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (Objects.requireNonNull(response.body()).string().contains(user.getNumber())) {
            logger.debug(user.getNumber() + " 登录成功，返回Cookies");

            ArrayList<Cookie> a = new ArrayList<>();
            for (Cookie cookie: cookies) {
                if ("_webvpn_key".equals(cookie.name())
                        || "webvpn_username".equals(cookie.name())){

                    a.add(cookie);
                }
            }
            return a;
        }
        return null;
    }

    public boolean UserSignOut(User user) throws IOException {

        RequestBody requestBody = new FormBody.Builder()
                .add("_method", "delete")
                .build();

        Request request = new Request.Builder()
                .url("https://webvpn1.ecit.cn/users/sign_out")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 302) {
                logger.debug(user.getNumber() + "已经注销");
                return true;
            }
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
        return false;
    }


    private void scheduleGetCookies() {
        logger.debug("开始执行定时刷新Cookies任务");
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(() -> {
            logger.debug("开始刷新Cookies");

            logger.debug("Cookies刷新失败");
        }, 55L, 55L, TimeUnit.MINUTES);
    }

    private void cookiesMonitor() {
        logger.debug("开始监控Cookies有效性");

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(() -> {

            Request request = new Request.Builder()
                    .url("https://172-20-135-5-8080.webvpn1.ecit.cn/opac/ajax_item.php?marc_no")
                    .get()
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (Exception e) {
                logger.debug(e.getMessage());
                logger.debug("Cookies可能无效");
            }

            if ((response != null ? response.code() : 0) == 200) {
                try {
                    logger.debug(response.body().string().contains("书刊状态") ?
                            "Cookies有效" : "Cookies无效");
                } catch (IOException e) {
                    logger.debug(e.getMessage());
                }
            }

        }, 30L, 30L, TimeUnit.MINUTES);
    }

    private void storeToDisk() {
        SerializableOkHttpCookies serializableOkHttpCookies
                = new SerializableOkHttpCookies(cookies);

        String path = System.getProperties().getProperty("user.home")
                + File.separator + ".atecut" + File.separator + "cookies" + File.separator
                + "cookies";
        File file = new File(path);
        try {
            if (!file.exists()) {
                if (!file.getParentFile().getParentFile().exists()) {
                    file.getParentFile().getParentFile().mkdir();
                }
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdir();
                }
                file.createNewFile();
            }
            if (!file.exists()) {
                logger.debug("cookies文件创建失败，系统退出");
                System.exit(0);
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            serializableOkHttpCookies.writeObject(oos);
            logger.debug("已经将用户"  + "cookies写入cookies文件");
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
            logger.debug("cookies文件创建失败，系统退出");
            System.exit(0);
        }
    }

    static public boolean isUserCookieOk(UserCookie userCookie) throws IOException {

        OkHttpClient client = RequestUtil.getOkHttpInstanceNotRedirect();

        Request request = new Request.Builder()
                .url("https://172-20-135-5-8080.webvpn1.ecit.cn/opac/ajax_item.php?marc_no")
                .addHeader("cookie", userCookie.getUserCookies())
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String htmlBody =  Objects.requireNonNull(response.body()).string();
        return htmlBody.contains("书刊状态");
    }
}
