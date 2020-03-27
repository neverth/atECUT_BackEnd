package cn.atecut.controller;


import cn.atecut.service.DoubanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("restfulApi/douBan")
@Controller
public class DoubanController {

    @Autowired
    DoubanService doubanService;

    @RequestMapping(value = "bookIsbn/{isbn}", produces = "application/json; charset=UTF-8",
            method = RequestMethod.GET)
    public @ResponseBody
    String handleGetBooksByTitleApi(@PathVariable String isbn,
                                    HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) {
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        return doubanService.getBookInfoByIsbn(isbn);
    }
}
