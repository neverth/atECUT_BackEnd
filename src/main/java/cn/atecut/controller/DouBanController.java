package cn.atecut.controller;


import cn.atecut.bean.pojo.Result;
import cn.atecut.result.CodeMsg;
import cn.atecut.service.DoubanService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NeverTh
 */
@RequestMapping("/douBan")
@RestController
@Api(value = "豆瓣图书接口", tags = "豆瓣图书接口")
public class DouBanController {

    @Autowired
    DoubanService doubanService;

    @GetMapping(value = "bookIsbn/{isbn}")
    @ApiOperation(value = "根据图书isbn获得图书的详细信息", notes = "根据图书isbn获得图书的详细信息")
    public Result<JSONObject> getBookInfoByIsbn(
            @ApiParam(value = "isbn", required = true, example = "9787532125944")
            @PathVariable String isbn
    ) {
        String douBanInfo;
        try {
            douBanInfo = doubanService.getBookInfoByIsbn(isbn);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        if(douBanInfo != null){
            return Result.success(JSONObject.parseObject(douBanInfo));
        }
        return Result.error(CodeMsg.PARAM_ERROR);
    }
}
