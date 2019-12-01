package cn.atecut.unti;

import cn.atecut.bean.User;
import cn.atecut.bean.WebVpnOneLoginInfo;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;


/*
 * @author NeverTh
 * @description webVpn1登录选项
 * @date 11:39 2019/11/25
 */

public class WebVpnOneOp {
    private volatile static WebVpnOneOp instance;

    private List<Cookie> cookies = new ArrayList<>();

    private WebVpnOneLoginInfo webVpnOneLoginInfo = new WebVpnOneLoginInfo();

    private OkHttpClient client;

    private long cookiesCreatTime;

    private Logger logger = LogManager.getLogger(WebVpnOneOp.class);

    public static boolean cookiesValid;


    private WebVpnOneOp() {
        scheduleGetCookies();
        cookiesMonitor();
    }

    public static WebVpnOneOp getInstance(){
        if (instance == null) {
            synchronized (WebVpnOneOp.class) {
                if (instance == null) {
                    instance = new WebVpnOneOp();
                }
            }
        }
        return instance;
    }

    private  boolean getLoginInfo(User user){
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
                        for(Cookie cookie : list){
                            for (Cookie cookie1 : cookies){
                                if (cookie.name().equals(cookie1.name())){
                                    temp.add(cookie1);
                                }
                            }
                        }
                        cookies.removeAll(temp);
                        cookies.addAll(list);

                        SerializableOkHttpCookies serializableOkHttpCookies
                                = new SerializableOkHttpCookies(cookies);

                        String path = System.getProperties().getProperty("user.home")
                                + File.separator + ".atecut" + File.separator + "cookies" + File.separator
                                + user.getNumber() + "cookies";
                        File file = new File(path);
                        try {
                            if(!file.exists()) {
                                if(!file.getParentFile().getParentFile().exists()){
                                    file.getParentFile().getParentFile().mkdir();
                                }
                                if(!file.getParentFile().exists()){
                                    file.getParentFile().mkdir();
                                }
                                file.createNewFile();
                            }
                            if(!file.exists()){
                                logger.debug("cookies文件创建失败，系统退出");
                                System.exit(0);
                            }
                            FileOutputStream fos = new FileOutputStream(file);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            serializableOkHttpCookies.writeObject(oos);
                            logger.debug("已经将用户" + user.getNumber() + "cookies写入cookies文件");
                            oos.close();
                            fos.close();
                        }catch (IOException e) {
                            e.printStackTrace();
                            logger.debug(e.getMessage());
                            logger.debug("cookies文件创建失败，系统退出");
                            System.exit(0);
                        }
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

        logger.debug("正在请求登录页面获取登录参数");

        String htmlBody = null;
        Response response = null;

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("请求登录页面失败");
            e.printStackTrace();
            return false;
        }

        try {
            htmlBody = Objects.requireNonNull(response.body()).string();
        } catch (IOException | NullPointerException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return false;
        }

        Document doc = Jsoup.parse(htmlBody);
        Element formElement = doc.getElementById("login-form");
        Elements inputElements = formElement.getElementsByTag("input");
        for (Element inputElement : inputElements) {
            if ("authenticity_token".equals(inputElement.attr("name"))) {
                webVpnOneLoginInfo.setAuthenticity_token(inputElement.attr("value"));
            }
        }

        logger.debug("登录页面参数解析完成");

        return webVpnOneLoginInfo.getAuthenticity_token() != null;
    }

    public String userLogin(User user){

        if (!getLoginInfo(user)) {
            return null;
        }

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

        logger.debug("请求登录页面登录");

        try {
            Response response = client.newCall(request).execute();
            if (Objects.requireNonNull(response.body()).string().contains(user.getNumber())){
                logger.debug(user.getNumber() + " 登录成功");
                cookiesCreatTime = System.currentTimeMillis();
                return user.getNumber() + "cookies";
            }
        } catch (IOException | NullPointerException e) {
            logger.debug("登录失败");
            logger.debug(e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public long getCookiesCreatTime() {
        return cookiesCreatTime;
    }

    private void scheduleGetCookies(){
        logger.debug("开始执行定时刷新Cookies任务");
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(()->{
            logger.debug("开始刷新Cookies");
            if(userLogin(new User("201720180702", "ly19980911")) != null){
                logger.debug("Cookies刷新成功");
                cookiesCreatTime = System.currentTimeMillis();
                return;
            }
            logger.debug("Cookies刷新失败");
        }, 55L, 55L, TimeUnit.MINUTES);
    }

    private void cookiesMonitor(){
        logger.debug("开始监控Cookies有效性");

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(()->{

            Request request = new Request.Builder()
                    .url("https://172-20-135-5-8080.webvpn1.ecit.cn/opac/ajax_item.php?marc_no")
                    .get()
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (Exception e) {
                cookiesValid = false;
                logger.debug(e.getMessage());
                logger.debug("Cookies可能无效");
            }

            if ((response != null ? response.code() : 0) == 200){
                try {
                    cookiesValid = response.body().string().contains("书刊状态");
                    logger.debug(cookiesValid ? "Cookies有效" : "Cookies无效");
                } catch (IOException e) {
                    cookiesValid = false;
                    logger.debug(e.getMessage());
                }
            }

        }, 1L, 5L, TimeUnit.MINUTES);
    }
}
