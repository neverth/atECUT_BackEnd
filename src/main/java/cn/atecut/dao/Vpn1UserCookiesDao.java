package cn.atecut.dao;


import cn.atecut.bean.User;
import cn.atecut.bean.model.Vpn1UserCookies;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/*
 * @author NeverTh
 * @description //TODO
 * @date 16:20 2019/12/3
 */

@Repository
public interface Vpn1UserCookiesDao {

    ArrayList<Vpn1UserCookies> selectAllUserCookies();

    ArrayList<Vpn1UserCookies> selectUserCookiesByUser(@Param("user")User user);

    int insertUserCookies(@Param("vpn1UserCookies")Vpn1UserCookies vpn1UserCookies);

    int deleteUserCookies(@Param("vpn1UserCookies")Vpn1UserCookies vpn1UserCookies);

    int updateUserCookies(@Param("user")User user,
                          @Param("vpn1UserCookies")Vpn1UserCookies vpn1UserCookies);

}
