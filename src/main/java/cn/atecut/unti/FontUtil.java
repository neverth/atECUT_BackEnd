package cn.atecut.unti;

import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author NeverTh
 */
public class FontUtil {

    public static String decodeUnicode(String source){
        // 定义正则表达式来搜索中文字符的转义符号
        final String s = "&#.*?;";
        Pattern compile = Pattern.compile(s);
        Matcher matcher = compile.matcher(source);
        // 循环搜索 并转换 替换
        while (matcher.find()) {
            String group = matcher.group();
            // 获得16进制的码
            String hexcode = "0" + group.replaceAll("(&#|;)", "");
            // 字符串形式的16进制码转成int并转成char 并替换到源串中
            source = source.replaceAll(group, (char) Integer.decode(hexcode).intValue() + "");
        }
        return source;
    }

    public static void main(String[] args) throws IOException, DocumentException {
        FontUtil.decodeUnicode("&#x6df1;&#x5165;&#x7406;&#x89e3;&#x004a;&#x0061;&#x0076;&#x0061;&#x865a;&#x62df;&#x673a;&#x003a;&#x004a;&#x0056;&#x004d;&#x9ad8;&#x7ea7;&#x7279;&#x6027;&#x4e0e;&#x6700;&#x4f73;&#x5b9e;&#x8df5;&#x002e;&#x0032;&#x7248;");
    }
}
