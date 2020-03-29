package cn.atecut.util;


import okhttp3.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author NeverTh
 */
public class RequestUtil {

    private volatile static OkHttpClient okHttpClientInstanceNotRedirect;

    private volatile static OkHttpClient okHttpClientInstance;

    private Logger logger = LogManager.getLogger(RequestUtil.class);

    public static OkHttpClient getOkHttpInstanceNotRedirect() {
        if (okHttpClientInstanceNotRedirect == null) {
            synchronized (RequestUtil.class) {
                if (okHttpClientInstanceNotRedirect == null) {
                    okHttpClientInstanceNotRedirect = new OkHttpClient
                            .Builder()
                            .followRedirects(false)
                            .build();
                }
            }
        }
        return okHttpClientInstanceNotRedirect;
    }

    public static OkHttpClient getOkHttpInstance() {
        if (okHttpClientInstance == null) {
            synchronized (RequestUtil.class) {
                if (okHttpClientInstance == null) {
                    okHttpClientInstance = new OkHttpClient
                            .Builder()
                            .build();
                }
            }
        }
        return okHttpClientInstance;
    }
}
