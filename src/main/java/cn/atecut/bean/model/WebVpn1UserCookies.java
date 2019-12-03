package cn.atecut.bean.model;


import lombok.Data;

import java.sql.Time;

/*
 * @author NeverTh
 * @description //TODO
 * @date 16:52 2019/12/3
 */

@Data
public class WebVpn1UserCookies {
    private int id;
    private String userNumber;
    private String userCookies;
    private Time creatTime;
    private int version;
}
