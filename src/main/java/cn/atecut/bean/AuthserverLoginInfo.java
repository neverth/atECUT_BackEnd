package cn.atecut.bean;

import lombok.Data;

@Data
public class AuthserverLoginInfo {
    private String lt;
    private String dllt;
    private String execution;
    private String _eventId;
    private String rmShown;
    private String pwdDefaultEncryptSalt;
    private String rememberMe;
}