package cn.atecut.dao;


import cn.atecut.bean.User;
import cn.atecut.bean.po.AuthServerCookies;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * @author NeverTh
 */
@Repository
public interface AuthServerCookiesDao {

    ArrayList<AuthServerCookies> selectAllUserCookies();

    ArrayList<AuthServerCookies> selectUserCookiesByUser(@Param("user") User user);

    int insertUserCookies(@Param("authServerCookies")AuthServerCookies authServerCookies);

    int deleteUserCookies(@Param("authServerCookies")AuthServerCookies authServerCookies);

    int updateUserCookies(@Param("user")User user,
                          @Param("authServerCookies")AuthServerCookies authServerCookies);

}
