package cn.atecut.result;
import java.io.Serializable;

/*
 * @author NeverTh
 * @description //TODO
 * @date 16:59 2019/11/26
 */

public class CodeMsg implements Serializable {

    private int code;
    private String msg;

    public static CodeMsg SUCCESS = new CodeMsg(200, "success");
    public static CodeMsg ERROR = new CodeMsg(5001, "error");
    public static CodeMsg SERVER_ERROR = new CodeMsg(503, "服务端异常");
    public static CodeMsg NOT_EXIST_RECORD = new CodeMsg(500200, "不存在此纪录");
    public static CodeMsg SQL_ERROR = new CodeMsg(500300, "SQL语句异常");
    public static CodeMsg PARAM_ERROR = new CodeMsg(500400, "参数异常");
    public static CodeMsg DELETE_FAIL = new CodeMsg(500500, "删除失败");

    private CodeMsg() { }

    private CodeMsg( int code,String msg ) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
