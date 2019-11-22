package cn.atecut.controller;

import cn.atecut.bean.User;
import cn.atecut.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @RequestMapping(value = "/getBookByTitle/{title}", produces = "application/json; charset=UTF-8")
    public @ResponseBody
    String handleGetBooksByTitleApi(@PathVariable("title") String title,
                                    HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        return libraryService.getBooksByTitle(new User("201720180702", "ly19980911"), title);
    }

}
