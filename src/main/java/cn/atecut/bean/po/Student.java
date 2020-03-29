package cn.atecut.bean.po;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "学生信息实体")
public class Student {
    @ApiModelProperty(value = "学生学号", required = true, example = "20172018****")
    private String number;

    @ApiModelProperty(value = "学生密码", required = true, example = "password")
    private String password;
}
