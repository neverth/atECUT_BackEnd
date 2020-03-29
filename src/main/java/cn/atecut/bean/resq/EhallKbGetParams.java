package cn.atecut.bean.resq;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author NeverTh
 */

@Data

/*
 * // ApiModel里面不能用 ‘/’
 */

@ApiModel(value = "jw.ehall.kb.get 请求参数实体")
public class EhallKbGetParams{
    @ApiModelProperty(value = "学生学号", required = true, example = "20172018****")
    @JsonProperty(value = "number")
    @NotNull
    private String number;

    @ApiModelProperty(value = "学生密码", required = true, example = "password")
    @JsonProperty(value = "password")
    @NotNull
    private String password;

    @ApiModelProperty(value = "学年", required = true, example = "2019")
    @JsonProperty(value = "XN")
    @NotNull(message = "参数错误")
    private String XN;

    @ApiModelProperty(value = "学期", required = true, example = "2")
    @JsonProperty(value = "XQ")
    @NotNull(message = "参数错误")
    private String XQ;

    @ApiModelProperty(value = "周次", required = true, example = "4")
    @JsonProperty(value = "ZC")
    @NotNull(message = "参数错误")
    private String ZC;

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("XN", this.XN);
        jsonObject.put("XQ", this.XQ);
        jsonObject.put("ZC", this.ZC);
        jsonObject.put("SJBZ", "1");
        return jsonObject.toString();
    }
}
