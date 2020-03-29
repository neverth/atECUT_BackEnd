package cn.atecut.bean.resq;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author NeverTh
 */
@Data
@ApiModel(value = "jw.ehall.score.get 请求参数实体")
public class EhallScoreGetParams {

    @ApiModelProperty(value = "学生学号", required = true, example = "20172018****")
    @JsonProperty(value = "number")
    @NotNull
    private String number;

    @ApiModelProperty(value = "学生密码", required = true, example = "password")
    @JsonProperty(value = "password")
    @NotNull
    private String password;

    @ApiModelProperty(value = "被查询学号", required = true, example = "20172018****")
    @JsonProperty(value = "XSBH")
    @NotNull(message = "参数错误")
    private String XSBH;

    @ApiModelProperty(value = "学年", required = true, example = "2019")
    @JsonProperty(value = "XN")
    @NotNull(message = "参数错误")
    private String XN;

    @ApiModelProperty(value = "学期", required = true, example = "1")
    @JsonProperty(value = "XQ")
    @NotNull(message = "参数错误")
    private String XQ;

    @ApiModelProperty(value = "页大小", required = true, example = "10")
    @JsonProperty(value = "pageSize")
    @NotNull(message = "参数错误")
    private String pageSize;

    @ApiModelProperty(value = "第几页", required = true, example = "1")
    @JsonProperty(value = "pageNumber")
    @NotNull(message = "参数错误")
    private String pageNumber;

    @Override
    public String toString() {
        // XSBH=201720180702&XN=2019&XQ=1&pageSize=10&pageNumber=1;
        StringBuilder toString  = new StringBuilder();
        toString.append("XSBH").append("=").append(this.XSBH).append("&");
        toString.append("XN").append("=").append(this.XN).append("&");
        toString.append("XQ").append("=").append(this.XQ).append("&");
        toString.append("pageSize").append("=").append(this.pageSize).append("&");
        toString.append("pageNumber").append("=").append(this.pageNumber);
        return toString.toString();
    }
}
