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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liyang
 */
public class WebVpnOneOp {
    private volatile static WebVpnOneOp instance;

    private List<Cookie> cookies = new ArrayList<>();

    private WebVpnOneLoginInfo webVpnOneLoginInfo = new WebVpnOneLoginInfo();

    private OkHttpClient client;

    private Request request;

    private Response response;

    private Logger logger = LogManager.getLogger(WebVpnOneOp.class);

    private WebVpnOneOp() {
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
                        FileOutputStream fos = null;
                        try {
                            String path =
//                                    this.getClass().getResource("/").getPath()
                                    "D://" + user.getNumber() + "cookies";
                            fos = new FileOutputStream(path);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            serializableOkHttpCookies.writeObject(oos);
                            oos.close();
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @NotNull
                    @Override
                    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                        return cookies;
                    }
                })
                .build();

        request = new Request.Builder()
                .url("https://webvpn1.ecit.cn/users/sign_in")
                .get()
                .build();

        logger.debug("正在请求 https://webvpn1.ecit.cn/users/sign_in");
        String htmlBody = null;
        try {
            response = client.newCall(request).execute();
            htmlBody = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        Document doc = Jsoup.parse(htmlBody);
        Element formElement = doc.getElementById("login-form");
        Elements inputElements = formElement.getElementsByTag("input");
        for (Element inputElement : inputElements) {
            if (inputElement.attr("name").equals("authenticity_token")) {
                webVpnOneLoginInfo.setAuthenticity_token(inputElement.attr("value"));
            }
        }

        if("".equals(webVpnOneLoginInfo.getAuthenticity_token())){
            return false;
        }
        return true;
    }

    public String userLogin(User user){
        if(getLoginInfo(user)){

            RequestBody requestBody = new FormBody.Builder()
                    .add("authenticity_token", webVpnOneLoginInfo.getAuthenticity_token())
                    .add("commit", webVpnOneLoginInfo.getCommit())
                    .add("user[dymatice_code]", webVpnOneLoginInfo.getDymatice_code())
                    .add("user[password]", webVpnOneLoginInfo.getPassword())
                    .add("user[login]", webVpnOneLoginInfo.getUsername())
                    .add("utf8", webVpnOneLoginInfo.getUtf8())
                    .build();

            request = new Request.Builder()
                    .url("https://webvpn1.ecit.cn/users/sign_in")
                    .post(requestBody)
                    .build();

            try {
                response = client.newCall(request).execute();
                if (response.body().string().contains(user.getNumber())){
                    logger.debug(user.getNumber() + " 登录成功");
                    return user.getNumber() + "cookies";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        WebVpnOneOp a = WebVpnOneOp.getInstance();
        System.out.println(a.userLogin(new User("201720180702", "ly19980911")));
    }
}
