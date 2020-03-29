package cn.atecut.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NeverTh
 */
public class CookiesUtil {

    @Data
    @AllArgsConstructor
    public static class SimCookie {
        String name;
        String value;
        String path;

        public static boolean isSame(SimCookie a, SimCookie b){
            if (a.getName().equals(b.name) && a.getPath().equals(b.getPath())){
                return true;
            }
            return false;
        }
    }

    ArrayList<SimCookie> cookies;

    public CookiesUtil(){
        cookies = new ArrayList<>();
    }

    public boolean add(String cookieStr){
        String name = cookieStr.split("=")[0];
        String value = cookieStr.split(";")[0] + ";";
        String path = cookieStr.split("=")[2].split(";")[0];
        return cookies.add(new SimCookie(name, value, path));
    }

    public int add(List<String > cookieStrList){
        int count = 0;
        for (String cookieStr : cookieStrList){
            String name = cookieStr.split("=")[0];
            String value = (cookieStr.split(";")[0]).replace(name + "=", "");

            String path = "";
            int pathIndex = cookieStr.indexOf("path=");
            if (pathIndex != -1){
                pathIndex += "path=".length();
                String sub = cookieStr.substring(pathIndex);
                if (sub.contains(";")){
                    path = sub.split(";")[0];
                }else{
                    path = sub;
                }
            }

            SimCookie simCookie = new SimCookie(name, value, path);

            int index = -1;
            for (int i = 0; i < cookies.size(); i++) {
                if (SimCookie.isSame(cookies.get(i), simCookie)){
                    index = i;
                    break;
                }
            }
            if (index != -1){
                cookies.remove(index);
                cookies.add(simCookie);
            }else {
                cookies.add(simCookie);
                count ++;
            }

        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        for (SimCookie simCookie : cookies) {
            toString.append(simCookie.getName()).append("=").append(simCookie.getValue()).append(";");
        }
        return toString.toString();
    }

    public static void main(String[] args) {

    }
}
