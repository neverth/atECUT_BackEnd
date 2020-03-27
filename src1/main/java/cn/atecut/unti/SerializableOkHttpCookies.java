package cn.atecut.unti;

import okhttp3.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

    private static  transient Logger logger = LogManager.getLogger(SerializableOkHttpCookies.class);

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
            // 持久化boolean时有一些问题，先留在这 TODO
//            out.writeBoolean(item.secure());
//            out.writeBoolean(item.httpOnly());
//            out.writeBoolean(item.hostOnly());
//            out.writeBoolean(item.persistent());
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
//                boolean secure = in.readBoolean();
//                boolean httpOnly = in.readBoolean();
//                boolean hostOnly = in.readBoolean();
//                boolean persistent = in.readBoolean();
                Cookie.Builder builder = new Cookie.Builder();
                builder = builder.name(name);
                builder = builder.value(value);
                builder = builder.expiresAt(expiresAt);
//                builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
                builder = builder.domain(domain);
                builder = builder.path(path);
//                builder = secure ? builder.secure() : builder;
//                builder = httpOnly ? builder.httpOnly() : builder;
                Cookie cookie = builder.build();
                clientCookies.add(cookie);
            }
        }
        catch(EOFException e) {
        }
        finally {
        }
    }

    static public List<Cookie> parseCookies(String serializableCookies){
        logger.info("正在解析serializableCookies为Cookies");

        SerializableOkHttpCookies serializableOkHttpCookies = new SerializableOkHttpCookies(null);
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(serializableCookies.getBytes(StandardCharsets.ISO_8859_1)));
            serializableOkHttpCookies.readObject(ois);
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
        return serializableOkHttpCookies.getCookies();
    }


    static public String parseCookies(List<Cookie> cookies) {
        logger.info("正在解析Cookies为serializableCookies");

        SerializableOkHttpCookies serializableOkHttpCookies = new SerializableOkHttpCookies(cookies);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            serializableOkHttpCookies.writeObject(oos);
        } catch (IOException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
        String res = null;
        try {
            res = bos.toString(String.valueOf(StandardCharsets.ISO_8859_1));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }
}
