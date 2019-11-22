package cn.atecut.controller;

import cn.atecut.bean.User;
import cn.atecut.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequestMapping("restfulApi/library")
@Controller
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @RequestMapping(value = "book/{title}/{pageCount}", produces = "application/json; charset=UTF-8",
            method = RequestMethod.GET)
    public @ResponseBody
    String handleGetBooksByTitleApi(@PathVariable("title") String title,
                                    @PathVariable("pageCount") int pageCount,
                                    HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) {
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        return libraryService.getBooksByTitle(new User("201720180702", "ly19980911"), title, pageCount);
    }

    @RequestMapping(value = "bookDetail/{marcNo}",
            produces = "application/json; charset=UTF-8",
            method = RequestMethod.GET)
    public @ResponseBody
    String handleGetBooksByTitleApi(@PathVariable("marcNo") String marcNo,
                                    HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse){
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        return libraryService.getBookDetailByNo(new User("201720180702", "ly19980911"), marcNo);
    }

}
