package cn.atecut.controller;

import cn.atecut.bean.BookInfo;
import cn.atecut.bean.User;
import cn.atecut.bean.vo.BooksInfo;
import cn.atecut.result.Result;
import cn.atecut.service.LibraryImproveService;
import cn.atecut.service.Vpn1UserCookiesService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


/*
 * @author NeverTh
 * @description //TODO
 * @date 15:57 2019/12/4
 */

@RequestMapping("restfulApi/library")
@Controller
public class LibraryController {

    @Autowired
    private LibraryImproveService libraryImproveService;

    @Autowired
    private Vpn1UserCookiesService vpn1UserCookiesService;

    private static Logger logger = LogManager.getLogger(LibraryController.class);

    @RequestMapping(value = "book/{title}/{pageCount}",
            produces = "application/json; charset=UTF-8",
            method = RequestMethod.GET)
    public @ResponseBody
    Result getBooksByTitle(@PathVariable("title") String title,
                           @PathVariable("pageCount") int pageCount,
                           @RequestParam(value = "sortField", required=false) String sortField,
                           @RequestParam(value = "sortType", required=false) String sortType) {

        // fastJson竟然还不支持流式计算
        JSONObject requestJson = new JSONObject();
        JSONArray a = new JSONArray();
        JSONObject b = new JSONObject();
        JSONArray c = new JSONArray();
        JSONObject d = new JSONObject();
        a.add(b);
        b.put("fieldList", c);
        c.add(d);
        d.put("fieldCode", "");
        d.put("fieldValue", title);

        requestJson.put("searchWords", a);
        requestJson.put("filters", new JSONArray());
        requestJson.put("first", true);
        requestJson.put("limiter", new JSONArray());
        requestJson.put("locale", "");
        requestJson.put("pageCount", pageCount);
        requestJson.put("pageSize", 20);
        requestJson.put("sortField", "relevance");
        requestJson.put("sortType", "desc");

        if(sortField != null){
            requestJson.put("sortField", sortField);
        }
        if(sortType != null){
            requestJson.put("sortType", sortType);
        }

        List<Cookie> cookies = commonGetUserCookies(new User("201720180702", "ly19980911"));
        if (cookies == null){
            return Result.error("服务当前不可用，稍后试试");
        }
        BooksInfo booksInfo;
        try {
            booksInfo = libraryImproveService.getBooksByTitle(
                        cookies, requestJson);
            return Result.success(booksInfo);
        } catch (IOException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
            return Result.error("服务当前不可用，稍后试试");
        }
    }

    @RequestMapping(value = "bookDetail/{marcNo}",
            produces = "application/json; charset=UTF-8",
            method = RequestMethod.GET)
    public @ResponseBody
    Result getBookDetailByNo(@PathVariable("marcNo") String marcNo){

        List<Cookie> cookies = commonGetUserCookies(new User("201720180702", "ly19980911"));
        if (cookies == null){
            return Result.error("服务当前不可用，稍后试试");
        }

        List<BookInfo.BookNum> bookNumList;
        try {
            bookNumList = libraryImproveService.getBookDetailByNo(cookies, marcNo);
            return Result.success(bookNumList);
        } catch (IOException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
            return Result.error();
        }
    }

    public List<Cookie> commonGetUserCookies(User user){
        List<Cookie> cookies =
                vpn1UserCookiesService.getUserCookiesFromDataBase(user);

        if(!libraryImproveService.isCookiesValid(cookies)){
            List<Cookie> newCookies = null;

            try {
                newCookies = vpn1UserCookiesService.getUserCookiesNew(user);
                if(cookies.size() == 0){
                    if(vpn1UserCookiesService.putCookiesToDataBase(user, newCookies) > 0){
                        logger.debug("向数据库中插入cookies");
                    }else{
                        logger.debug("向数据库中插入cookies失败");
                    }
                }else{
                    if(vpn1UserCookiesService.updateUserCookies(
                            user, newCookies, 1) > 0){
                        logger.debug("数据库中用户cookies已经更新");
                    }else{
                        logger.debug("数据库中用户cookies更新失败");
                    }
                }
            } catch (Exception e) {
                logger.debug(e.getMessage());
                return null;
            }
            return newCookies;
        }
        return cookies;
    }
}
