package cn.atecut.unti;

import cn.atecut.bean.AuthserverLoginInfo;
import cn.atecut.bean.User;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuthServerOp {

    private volatile static AuthServerOp instance;

    private List<Cookie> cookies = new ArrayList<>();

    private OkHttpClient client;

    private Logger logger = LogManager.getLogger(AuthServerOp.class);

    private AuthServerOp() {
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
    }

    public static AuthServerOp getInstance() {
        if (instance == null) {
            synchronized (WebVpnOneOp.class) {
                if (instance == null) {
                    instance = new AuthServerOp();
                }
            }
        }
        return instance;
    }

    private AuthserverLoginInfo getLoginedCookies() throws IOException {
        cookies.clear();
        AuthserverLoginInfo loginInfo = new AuthserverLoginInfo();

        Request request = new Request.Builder()
                .url("https://authserver.ecut.edu.cn/authserver/login")
                .get()
                .build();

        Response response = client.newCall(request).execute();

        String htmlBody = response.body().string();

        Document doc = Jsoup.parse(htmlBody);
        Element formElement = doc.getElementById("casLoginForm");
        Elements inputElements = formElement.getElementsByTag("input");

        String pwdDefaultEncryptSalt = htmlBody
                .substring(htmlBody.indexOf("pwdDefaultEncryptSalt") + 25
                        , htmlBody.indexOf("pwdDefaultEncryptSalt") + 41);

        loginInfo.setPwdDefaultEncryptSalt(pwdDefaultEncryptSalt);
        loginInfo.setRememberMe("on");

        for (Element inputElement : inputElements) {
            if (inputElement.attr("name").equals("lt")) {
                loginInfo.setLt(inputElement.attr("value"));
            }
            if (inputElement.attr("name").equals("dllt")) {
                loginInfo.setDllt(inputElement.attr("value"));
            }
            if (inputElement.attr("name").equals("execution")) {
                loginInfo.setExecution(inputElement.attr("value"));
            }
            if (inputElement.attr("name").equals("_eventId")) {
                loginInfo.set_eventId(inputElement.attr("value"));
            }
            if (inputElement.attr("name").equals("rmShown")) {
                loginInfo.setRmShown(inputElement.attr("value"));
            }
        }
        return loginInfo;
    }

    /*
     * @Description 私有方法 直接利用js引擎给用户密码aes加密
     * @Date 19:53 2019/10/17
     */
    static private String encryptPassword(String pa1, String pwdDefaultEncryptSalt) throws IOException, ScriptException, NoSuchMethodException {
        ScriptEngine se = new ScriptEngineManager().getEngineByName("javascript");
        InputStream in = AuthServerOp.class.getClassLoader().getResourceAsStream("js/aes.js");

        File tmp = File.createTempFile("lzq", ".tmp");
        OutputStream os = new FileOutputStream(tmp);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        in.close();

        Reader reader = new FileReader(tmp);
        se.eval(reader);
        String res = null;
        if (se instanceof Invocable) {
            Invocable invoke = (Invocable) se;
            res = invoke.invokeFunction("encryptAES", pa1, pwdDefaultEncryptSalt).toString();
        }
        reader.close();
        return res;
    }

    /*
     * @Description 真正的登录方法
     * @Date 20:02 2019/10/17
     */
    public List<Cookie> getUserValidCookies(User user) throws IOException, ScriptException, NoSuchMethodException {

        AuthserverLoginInfo loginInfo = getLoginedCookies();

        String passwordAES = encryptPassword(user.getPassword(), loginInfo.getPwdDefaultEncryptSalt());

        RequestBody requestBody = new FormBody.Builder()
                .add("username", user.getNumber())
                .add("password", passwordAES)
                .add("lt", loginInfo.getLt())
                .add("dllt", loginInfo.getDllt())
                .add("execution", loginInfo.getExecution())
                .add("_eventId", loginInfo.get_eventId())
                .add("rmShown", loginInfo.getRmShown())
                .add("rememberMe", loginInfo.getRememberMe())
                .build();

        Request request = new Request.Builder()
                .url("https://authserver.ecut.edu.cn/authserver/login")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        String htmlBody = response.body().string();
        return cookies;
    }

    public static boolean isCookiesOk(List<Cookie> cookies){
        return true;
    }

    public static void main(String[] args) throws IOException, ScriptException, NoSuchMethodException {
        AuthServerOp a = AuthServerOp.getInstance();
        List<Cookie> cookies =
                a.getUserValidCookies(new User("201720180702", "ly19980911"));
        System.out.println(cookies);
    }
}
