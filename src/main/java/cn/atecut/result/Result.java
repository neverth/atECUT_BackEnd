package cn.atecut.result;


import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;

public class Result<T> extends HashMap<String, Object> implements Serializable {
    /**
     *  成功时候的调用
     * */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    /**
     *  成功时候的调用
     * */
    public static <T> Result<T> success(){
        return new Result<T>();
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }


    /**
     *  失败时候的调用
     * */
    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }

    public static Result error() {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常，请联系管理员");
    }

    public static Result unAuthorized() {
        return error(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), "");
    }

    public static Result error(String msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    public static Result error(int code, String msg) {
        return new Result(code, msg, false);
    }

    public Result (){
        put("status", 200);
        put("flag", true);
        put("msg", "success");
    }

    private Result(T data) {
        put("status", 200);
        put("message", "success");
        put("data", data);
    }

    private Result(int code, String msg) {
        put("status", code);
        put("message", msg);
    }

    private Result(int code, String msg, Boolean flag) {
        put("status", code);
        put("message", msg);
    }

    private Result(CodeMsg codeMsg) {
        if(codeMsg != null) {
            put("status", codeMsg.getCode());
            put("message", codeMsg.getMsg());
            put("flag", false);
        }
    }

}
