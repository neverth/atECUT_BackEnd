package cn.atecut.unti;

import okhttp3.Cookie;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/*
 * @author NeverTh
 * @description 持久化cookies类
 * @date 11:38 2019/11/25
 */

public class SerializableOkHttpCookies implements Serializable {

    private transient final List<Cookie> cookies;

    private transient List<Cookie> clientCookies;

    public SerializableOkHttpCookies(List<Cookie> cookies) {
        this.cookies = cookies;
        this.clientCookies = new ArrayList<>();
    }

    public List<Cookie> getCookies() {
        List<Cookie> bestCookies = cookies;
        if (!clientCookies.isEmpty()) {
            bestCookies = clientCookies;
        }
        return bestCookies;
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        for (Cookie item :
                cookies) {
            out.writeObject(item.name());
            out.writeObject(item.value());
            out.writeLong(item.expiresAt());
            out.writeObject(item.domain());
            out.writeObject(item.path());
            out.writeBoolean(item.secure());
            out.writeBoolean(item.httpOnly());
            out.writeBoolean(item.hostOnly());
            out.writeBoolean(item.persistent());
        }
    }

    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        try {
            while(true) {
                String name = (String) in.readObject();
                String value = (String) in.readObject();
                long expiresAt = in.readLong();
                String domain = (String) in.readObject();
                String path = (String) in.readObject();
                boolean secure = in.readBoolean();
                boolean httpOnly = in.readBoolean();
                boolean hostOnly = in.readBoolean();
                boolean persistent = in.readBoolean();
                Cookie.Builder builder = new Cookie.Builder();
                builder = builder.name(name);
                builder = builder.value(value);
                builder = builder.expiresAt(expiresAt);
                builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
                builder = builder.path(path);
                builder = secure ? builder.secure() : builder;
                builder = httpOnly ? builder.httpOnly() : builder;
                Cookie cookie = builder.build();
                clientCookies.add(cookie);
            }
        }
        catch(EOFException e) {

        }
        finally {

        }
    }
}
