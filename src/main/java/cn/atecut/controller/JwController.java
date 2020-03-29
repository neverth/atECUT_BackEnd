package cn.atecut.controller;


import cn.atecut.bean.po.Student;
import cn.atecut.bean.pojo.Result;
import cn.atecut.result.CodeMsg;
import cn.atecut.service.JwService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * @author NeverTh
 */
@RequestMapping("/jw")
@RestController
@Api(value = "教务系统接口", tags = "教务系统接口")
public class JwController {

    @Autowired
    JwService jwService;

    @PostMapping("/ehall/kb/get")
    @ApiOperation(value = "获得学生ehall平台的课表", notes = "获得学生ehall平台的课表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学号", example = "20172018****", required = true),
            @ApiImplicitParam(value = "密码", example = "****", required = true),
            @ApiImplicitParam(value = "学年", example = "2019", required = true),
            @ApiImplicitParam(value = "学期", example = "2", required = true),
            @ApiImplicitParam(value = "周次", example = "4", required = true),
            @ApiImplicitParam(value = "SJBZ", example = "1", defaultValue = "1")
    })
    public Result<JSONObject> getEhallKbData(
            @RequestBody JSONObject jsonObject
    ){
        String result;
        try {
            result = jwService.getEhallKbData(
                    new Student(jsonObject.remove("username").toString(),
                            jsonObject.remove("password").toString()),
                    jsonObject.toString());

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
