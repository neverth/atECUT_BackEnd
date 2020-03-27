package cn.atecut.dao;

import kong.unirest.Cookies;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class test {

    @Test
    public void test() throws IOException {
        OkHttpClient build = new OkHttpClient
                .Builder()
                .build();

        Request request = new Request.Builder()
                .url("https://authserver.ecut.edu.cn/authserver/login")
                .get()
                .build();

        Response response = build.newCall(request).execute();

        Headers headers = response.headers();

        List<String> values = headers.values("Set-Cookie");
        System.out.println(values);


        System.out.println();
    }
}
