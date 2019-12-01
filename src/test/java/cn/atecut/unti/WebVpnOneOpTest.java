package cn.atecut.unti;


import cn.atecut.bean.User;
import cn.atecut.dao.LibraryDao;
import com.alibaba.fastjson.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class WebVpnOneOpTest {
    @Test
    public void userLoginTest() throws IOException {
//        ApplicationContext act =
//                new ClassPathXmlApplicationContext("classpath:spring/spring-mvc.xml");
        WebVpnOneOp a = WebVpnOneOp.getInstance();
        System.out.println(a.userLogin(new User("201720180702", "ly19980911")));

    }
    @Test
    public void getBooksByTitleTest(){
        LibraryDao dao = new LibraryDao();


        JSONObject requestJson = new JSONObject();
        requestJson.put("filters", new JSONArray());
        requestJson.put("first", true);
        requestJson.put("limiter", new JSONArray());
        requestJson.put("locale", "");
        requestJson.put("pageCount", 1);
        requestJson.put("pageSize", 20);
        requestJson.put("sortField", "relevance");
        requestJson.put("sortType", "desc");
        // fashjson竟然还不支持流式计算
        JSONArray a = new JSONArray();
        JSONObject b = new JSONObject();
        JSONArray c = new JSONArray();
        JSONObject d = new JSONObject();

        a.add(b);
        b.put("fieldList", c);
        c.add(d);
        d.put("fieldCode", "");
        d.put("fieldValue", "spring");

        requestJson.put("searchWords", a);
        System.out.println(requestJson.toJSONString());



//        String result =  dao.getBooksByTitle(
//                new User("201720180702", "ly19980911"),
//                requestJson);

//        JSONObject resultJson = new JSONObject(result);
//
//        JSONObject respJson = new JSONObject();
//        System.out.println(resultJson.toString());
    }
    @Test
    public void htmljx(){
        String source = "<!-- 图书明细 右侧的相关资源自定义链接 -->\n" +
                "<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"5\" cellspacing=\"0\" id=\"item\">\n" +
                "\t  \t          \t          <tr align=\"left\" class=\"greytext1\">\n" +
                "\t            <td >索书号</td>\n" +
                "\t            <td >条码号</td>\n" +
                "\t            <td >年卷期</td>\n" +
                "\t            <td >校区—馆藏地</td>\n" +
                "\t            <td >书刊状态</td>\n" +
                "\t         </tr>\n" +
                "\t          \t          <tr align=\"left\" class=\"whitetext\" >\n" +
                "\t            <td  width=\"10%\" >TP312JA/4020 </td>\n" +
                "\t            <td  width=\"15%\" >0476122</td>\n" +
                "\t            <td  width=\"10%\" >&nbsp;</td>\n" +
                "\t            <td  width=\"25%\" title=\"南昌：905\"><img src=\"../tpl/images/place_marker.gif\" />南昌—南昌：905  </td>\n" +
                "\t            <td  width=\"20%\" ><font color=green>可借</font></td>\n" +
                "\t          </tr>\n" +
                "\t          \t          <tr align=\"left\" class=\"whitetext\" >\n" +
                "\t            <td  width=\"10%\" >TP312JA/4020 </td>\n" +
                "\t            <td  width=\"15%\" >0476123</td>\n" +
                "\t            <td  width=\"10%\" >&nbsp;</td>\n" +
                "\t            <td  width=\"25%\" title=\"基础图书阅览室(3楼)\"><img src=\"../tpl/images/place_marker.gif\" />抚州—基础图书阅览室(3楼)  </td>\n" +
                "\t            <td  width=\"20%\" ><font color=green>可借</font></td>\n" +
                "\t          </tr>\n" +
                "\t          \t          <tr align=\"left\" class=\"whitetext\" >\n" +
                "\t            <td  width=\"10%\" >TP312JA/4020 </td>\n" +
                "\t            <td  width=\"15%\" >0476124</td>\n" +
                "\t            <td  width=\"10%\" >&nbsp;</td>\n" +
                "\t            <td  width=\"25%\" title=\"基础图书阅览室(3楼)\"><img src=\"../tpl/images/place_marker.gif\" />抚州—基础图书阅览室(3楼)  </td>\n" +
                "\t            <td  width=\"20%\" ><font color=green>可借</font></td>\n" +
                "\t          </tr>\n" +
                "\t          \t          <tr align=\"left\" class=\"whitetext\" >\n" +
                "\t            <td  width=\"10%\" >TP312JA/4020 </td>\n" +
                "\t            <td  width=\"15%\" >0476125</td>\n" +
                "\t            <td  width=\"10%\" >&nbsp;</td>\n" +
                "\t            <td  width=\"25%\" title=\"基础图书阅览室(3楼)\"><img src=\"../tpl/images/place_marker.gif\" />抚州—基础图书阅览室(3楼)  </td>\n" +
                "\t            <td  width=\"20%\" ><font color=green>可借</font></td>\n" +
                "\t          </tr>\n" +
                "\t          \t          <tr align=\"left\" class=\"whitetext\" >\n" +
                "\t            <td  width=\"10%\" >TP312JA/4020 </td>\n" +
                "\t            <td  width=\"15%\" >0476126</td>\n" +
                "\t            <td  width=\"10%\" >&nbsp;</td>\n" +
                "\t            <td  width=\"25%\" title=\"基础图书阅览室(3楼)\"><img src=\"../tpl/images/place_marker.gif\" />抚州—基础图书阅览室(3楼)  </td>\n" +
                "\t            <td  width=\"20%\" ><font color=green>可借</font></td>\n" +
                "\t          </tr>\n" +
                "\t          \t\t\t\t  \n" +
                "\t          \t          </table>";

        Document doc = Jsoup.parse(source);

        Elements trEles = doc.getElementById("item").getElementsByTag("tr");

        int len = trEles.size();
        for (int i = 1; i < trEles.size(); i++) {
            trEles.get(i).select("td").forEach((item) -> {
                String xxx =  item.text();
            });
        }


    }
    @Test
    public void testUserPath(){
        String path = System.getProperties().getProperty("user.home")
                + File.separator + ".atecut" + File.separator + "cookies" + File.separator
                + "20cookies";
        System.out.println(path);
        File file = new File(path);
        try {
            if (!file.exists()) {
                if(!file.getParentFile().getParentFile().exists()){
                    file.getParentFile().getParentFile().mkdir();
                }
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdir();
                }
                file.createNewFile();
//                if (!file.getParentFile().getParentFile().exists()) {
//                    file.getParentFile().getParentFile().mkdir();
//                    if (file.getParentFile().getParentFile().exists()){
//                        if(!file.getParentFile().exists()){
//                            file.getParentFile().mkdir();
//                            if(file.getParentFile().exists()){
//                                file.createNewFile();
//                            }
//                        }
//                    }
//                }
                if(file.exists()){
                    System.out.println("1324");
                }

            }
        }catch (Exception e){

        }
    }
    @Test
    public void CookiesMonitorTest(){

    }

}
