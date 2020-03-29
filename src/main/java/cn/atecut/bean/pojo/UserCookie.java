package cn.atecut.bean.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
public class UserCookie {

    private long id;

    private String userNumber;

    private String userCookies;

    private String  type;

    private Timestamp creatTime;

    private int version;
}
