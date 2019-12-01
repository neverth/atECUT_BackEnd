package cn.atecut.unti;


/*
 * @author NeverTh
 * @description //TODO
 * @date 22:27 2019/11/30
 */

import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/*
 * @author NeverTh
 * @description //TODO
 * @date 23:02 2019/11/30
 */

public class CookiesMonitor {

    private long cookiesCreatTime;

    private boolean cookiesValid;

    private List<Cookie> cookies = new ArrayList<>();

    private Logger logger = LogManager.getLogger(CookiesMonitor.class);

    private OkHttpClient client;


    public CookiesMonitor(List<Cookie> cookies) {
        this.cookies = cookies;
        client =
                new OkHttpClient
                        .Builder()
                        .cookieJar(new CookieJar() {
                            @Override
                            public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) { }
                            @NotNull
                            @Override
                            public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                                return cookies;
                            }
                        })
                        .build();
    }

    private void timingMonitoring(){

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        service.scheduleAtFixedRate(()->{

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
