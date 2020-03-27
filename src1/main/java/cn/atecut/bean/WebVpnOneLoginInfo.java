package cn.atecut.bean;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WebVpnOneLoginInfo {
    String utf8;
    String authenticity_token;
    String username;
    String password;
    String dymatice_code;
    String commit;
}
