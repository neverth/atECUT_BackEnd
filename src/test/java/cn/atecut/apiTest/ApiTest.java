package cn.atecut.apiTest;


import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import javax.swing.event.CaretListener;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;


public class ApiTest {
    static AtomicLong tiems = new AtomicLong(0);


    @Test
    public void loadTest() throws InterruptedException {

        OkHttpClient client = new OkHttpClient
                .Builder()
                .build();


        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                while(true){

                    Request request = new Request.Builder()
                            .url("https://www.nevertheless.fun/atecut/restfulApi/library/book/spring/6")
                            .get()
                            .build();

                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                    } catch (Exception e) {
                        System.exit(0);
                    }
                    System.out.println(Thread.currentThread().getName() + " " +tiems);

                    tiems.incrementAndGet();

                    try {
                        Thread.sleep(new Random().nextInt(5000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }, "thread " + i);
            thread.start();
        }

        Thread.sleep(10000000);
    }
}
