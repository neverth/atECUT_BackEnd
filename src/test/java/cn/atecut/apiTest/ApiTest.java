package cn.atecut.apiTest;


import okhttp3.*;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;


public class ApiTest {
    static AtomicLong times = new AtomicLong(0);

    static AtomicLong right = new AtomicLong(0);

    @Test
    public void loadTest() throws InterruptedException {

        OkHttpClient client = new OkHttpClient
                .Builder()
                .build();

        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                while(true){
                    times.incrementAndGet();
                    Request request = new Request.Builder()
                            .url("https://www.nevertheless.fun/atecut/restfulApi/library/book/东华理工/6")
                            .get()
                            .build();
                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                    } catch (Exception e) {
                        System.exit(0);
                    }
                    if (response.code() == 200){
                        right.incrementAndGet();
                    }
                    try {
                        Thread.sleep(new Random().nextInt(5000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " "  + response.code() + " " + times.get() + "  " + right.get());

                }

            }, "thread" + i);
            thread.start();
        }
        Thread.sleep(10000000);
    }
}
