package cn.atecut.dao;


import cn.atecut.bean.User;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * @Description
 * @Date 21:24 2020/2/24
 */
@Repository
public class KbDao {
    private static Logger logger = LogManager.getLogger(KbDao.class);

    private List<Cookie> cookieList;

    private static OkHttpClient client;


    public KbDao() {

        cookieList = null;

        client = new OkHttpClient
                .Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                        List<Cookie> temp = new ArrayList<>();
                        for (Cookie cookie : list) {
                            for (Cookie cookie1 : cookieList) {
                                if (cookie.name().equals(cookie1.name())) {
                                    temp.add(cookie1);
                                }
                            }
                        }
                        cookieList.removeAll(temp);
                        cookieList.addAll(list);
                    }

                    @NotNull
                    @Override
                    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                        return cookieList;
                    }
                })
                .build();

    }

    public void setCookieList(List<Cookie> cookieList) {
        this.cookieList = cookieList;
    }

    public String getKbByUser(User user, JSONObject object) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create("data\t{\"XN\":\"2019\",\"XQ\":\"2\",\"ZC\":3,\"SJBZ\":\"1\"}", JSON);

        Request request = new Request.Builder()
                .url("https://ehall.ecut.edu.cn/psfw/sys/xgpspubapp/indexmenu/getAppConfig.do?appId=5395950742020172&appName=pswdkbapp&v=0030821013792767693")
                .get()
                .build();

        Response response = client.newCall(request).execute();

        request = new Request.Builder()
                .url("https://ehall.ecut.edu.cn/psfw/sys/pswdkbapp/wdkbcx/getWdkbxx.do")
                .post(requestBody)
                .build();
        response = client.newCall(request).execute();
        return response.body().string();
    }

}
