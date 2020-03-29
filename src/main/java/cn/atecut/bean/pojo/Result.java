package cn.atecut.bean.pojo;

import cn.atecut.result.CodeMsg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author NeverTh
 */
@Data
public class Result<T> {

    @ApiModelProperty(value = "code")
    private int code;

    @ApiModelProperty(value = "描述")
    private String  message;

    @ApiModelProperty(value = "对象")
    private T data;

    private Result(CodeMsg codeMsg) {
        if(codeMsg != null) {
            this.code = codeMsg.getCode();
            this.message = codeMsg.getMsg();
            this.data = null;
        }
    }

    private Result(CodeMsg codeMsg, T data) {

        if(codeMsg != null) {
            this.code = codeMsg.getCode();
            this.message = codeMsg.getMsg();
        }

        this.data = data;
    }

    public static <T>Result<T> success(){
        return new Result<T>(CodeMsg.SUCCESS);
    }

    public static <T> Result<T> success(T data){
        return new Result<>(CodeMsg.SUCCESS, data);
    }

    public static <T> Result<T> success(CodeMsg codeMsg, T data){
        return new Result<>(codeMsg, data);
    }


    public static <T>Result<T> error() {
        return new Result<>(CodeMsg.ERROR);
    }

    public static <T>Result<T> error(CodeMsg msg) {
        return new Result<>(msg);
    }

}

