package cn.atecut.dao;


import cn.atecut.bean.model.WebVpn1UserCookies;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * @author NeverTh
 * @description //TODO
 * @date 16:20 2019/12/3
 */

@Repository
public interface Vpn1UserCookiesDao {

    public List<WebVpn1UserCookies> selectAllUserCookies();
}
