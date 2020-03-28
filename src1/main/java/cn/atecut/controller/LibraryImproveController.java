package cn.atecut.controller;

import cn.atecut.bean.User;
import cn.atecut.bean.po.Student;
import cn.atecut.bean.vo.BookVO;
import cn.atecut.bean.vo.BooksInfo;
import cn.atecut.bean.vo.BorrowBookVO;
import cn.atecut.result.CodeMsg;
import cn.atecut.bean.pojo.Result;
import cn.atecut.service.LibraryService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import okhttp3.Cookie;
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
@Api(value = "增强版图书馆接口", tags = "增强版图书馆接口")
public class LibraryImproveController {

    @Autowired
    LibraryService libraryService;

    @PostMapping("/borrowedBooks/get")
    @ApiOperation(value = "获得学生借阅信息", notes = "获得学生借阅信息")
    public Result<List<BorrowBookVO>> getStuBorrowBookInfo(@RequestBody Student student){
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

    @GetMapping(value = "/BookBorrowInfo/{marcNo}")
    @ApiOperation(value = "根据marcNo条形码获得馆藏信息", notes = "根据marcNo条形码获得馆藏信息")
    public Result<List<BookVO.BookBorrowInfo>> getBookDetailByNo(
           @ApiParam(value = "marcNo", example = "53716638667671457a6f4863776950753333756e63673d3d", required = true)
           @PathVariable("marcNo") String marcNo){

        List<BookVO.BookBorrowInfo> borrowBookVOS;

        try {
            borrowBookVOS = libraryService.getBooksNumByMarc(marcNo);
        } catch (NoSuchMethodException | ScriptException | IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        if(borrowBookVOS != null){
            return Result.success(borrowBookVOS);
        }
        return Result.error(CodeMsg.PARAM_ERROR);
    }
    @GetMapping(value = "book/{title}/{pageCount}")
    @ApiOperation(value = "根据title查询图书信息", notes = "根据title查询图书信息")
    public Result<JSONObject> getBooksByTitle(
            @ApiParam(value = "标题", example = "活着", required = true)
            @PathVariable("title") String title,
            @ApiParam(value = "页码", example = "1", required = true)
            @PathVariable("pageCount") int pageCount,
            @ApiParam(value = "相关度依据", example = "relevance")
            @RequestParam(value = "sortField", required=false) String sortField,
            @ApiParam(value = "排序顺序", example = "desc")
            @RequestParam(value = "sortType", required=false) String sortType) throws NoSuchMethodException, ScriptException, IOException {

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

        String result = "";
        try {
            result = libraryService.getBooksByTitle(requestJson.toString());

        } catch (NoSuchMethodException | ScriptException | IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        if(result != null){
            return Result.success(JSONObject.parseObject(result));
        }
        return Result.error(CodeMsg.PARAM_ERROR);
    }
}
