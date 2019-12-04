package cn.atecut.bean.model;


import lombok.Data;

import java.sql.Timestamp;

/*
 * @author NeverTh
 * @description //TODO
 * @date 16:52 2019/12/3
 */

@Data
public class Vpn1UserCookies {
    private int id;
    private String userNumber;
    private String userCookies;
    private Timestamp creatTime;
    private int version;
}
