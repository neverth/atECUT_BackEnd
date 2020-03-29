package cn.atecut.bean.po;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * @author NeverTh
 */
@Data
@Entity
@Table(name="cookies")
@NoArgsConstructor
@AllArgsConstructor

public class UserCookiePO {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="user_number")
    private String userNumber;

    @Column(name="user_cookies")
    private String userCookies;

    @Column(name="type")
    private String  type;

    @Column(name="creat_time")
    private Timestamp creatTime;

    @Column(name="version")
    private int version;

    public UserCookiePO(String userNumber, String userCookies, String type){
        this.userNumber = userNumber;
        this.userCookies = userCookies;
        this.type = type;
        this.version = 1;
        this.creatTime = new Timestamp(System.currentTimeMillis());
    }
}
