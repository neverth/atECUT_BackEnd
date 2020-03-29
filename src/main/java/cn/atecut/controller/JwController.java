package cn.atecut.controller;


import cn.atecut.bean.po.Student;
import cn.atecut.bean.pojo.Result;
import cn.atecut.bean.resq.EhallKbGetParams;
import cn.atecut.result.CodeMsg;
import cn.atecut.service.JwService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.script.ScriptException;
import javax.validation.Valid;
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
    public Result<JSONObject> getEhallKbData(@Valid @RequestBody EhallKbGetParams ehallKbGetParams){
        String result;
        try {
            result = jwService.getEhallKbData(
                    new Student(ehallKbGetParams.getNumber(),
                            ehallKbGetParams.getPassword()),
                    ehallKbGetParams.toString());

        } catch (NoSuchMethodException | IOException | ScriptException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        if(result != null){
            return Result.success(JSONObject.parseObject(result));
        }
        return Result.error(CodeMsg.PARAM_ERROR);
    }
}
