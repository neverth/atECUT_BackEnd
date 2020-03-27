package cn.atecut.controller;

import cn.atecut.bean.po.Student;
import cn.atecut.bean.vo.BorrowBookVO;
import cn.atecut.result.CodeMsg;
import cn.atecut.result.Result;
import cn.atecut.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

/**
 * @author NeverTh
 */
@RequestMapping("/library")
@RestController
public class LibraryImproveController {

    @Autowired
    LibraryService libraryService;

    @PostMapping("/borrowedBooks/get")
    public Result getStuBorrowBookInfo(@RequestBody Student student){
        List<BorrowBookVO> borrowBookVOS;
        try {
            borrowBookVOS = libraryService.getStuBorrowBookInfo(student);

        } catch (NoSuchMethodException | ScriptException | IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        if(borrowBookVOS != null){
            return Result.success(borrowBookVOS);
        }
        return Result.error(CodeMsg.PARAM_ERROR);
    }
}
