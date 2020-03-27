package cn.atecut.dao;


import cn.atecut.bean.User;
import cn.atecut.bean.po.Student;
import cn.atecut.bean.po.UserCookiePO;
import cn.atecut.bean.po.Vpn1UserCookies;
import cn.atecut.bean.pojo.Fields;
import cn.atecut.bean.pojo.UserCookie;
import cn.atecut.unti.AuthServerOp;
import cn.atecut.unti.LibraryCookieOp;
import cn.atecut.unti.WebVpnOneOp;
import lombok.val;
import okhttp3.Cookie;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

@Repository
public class UserCookieImplDao {

    @Autowired
    UserCookieDao userCookieDao;

    private UserCookie getCookieAndSave(Student student, String type) throws NoSuchMethodException, ScriptException, IOException {
        AuthServerOp authServerOp = AuthServerOp.getInstance();
        WebVpnOneOp webVpnOneOp = WebVpnOneOp.getInstance();
        LibraryCookieOp libraryCookieOp = LibraryCookieOp.getInstance();
        User user = new User();

        BeanUtils.copyProperties(student, user);

        List<Cookie> userCookies = null;
        String userCookiesStr = null;

        switch (type) {
            case Fields.AUTHSERVER:
                userCookies = authServerOp.getUserValidCookies(user);

                break;
            case Fields.WEBVPN1:
                userCookies = webVpnOneOp.getUserValidCookies(user);

                break;
            case Fields.LIBRARY:
                userCookiesStr = libraryCookieOp.getUserValidCookies(
                        student,
                        getOkCookieByUserNumAndType(student, Fields.AUTHSERVER).getUserCookies(),
                        getOkCookieByUserNumAndType(student, Fields.WEBVPN1).getUserCookies()
                );

                break;

            default:
                System.out.println();
        }

        if(userCookiesStr == null){

            if (userCookies == null){
                return null;
            }

            StringBuilder cookieSt = new StringBuilder();
            for (Cookie cookies : userCookies) {
                cookieSt.append(cookies.name()).append("=").append(cookies.value()).append(";");
            }
            userCookiesStr = cookieSt.toString();
        }

        UserCookie res = new UserCookie();

        BeanUtils.copyProperties(userCookieDao.save(
                new UserCookiePO(student.getNumber(), userCookiesStr, type)
        ), res);

        return res;
    }

    public UserCookie getCookieByUserNumAndTypeNoCheck(Student student, String type){
        List<UserCookiePO> userCookiePOS = userCookieDao.selectByUserNumAndType(student.getNumber(), type);

        if (!userCookiePOS.isEmpty()) {
            UserCookiePO userCookiePO = userCookiePOS.get(0);
            for (UserCookiePO userCookiePO1 : userCookiePOS) {
                if (userCookiePO1.getCreatTime().getTime() > userCookiePO.getCreatTime().getTime()) {
                    userCookiePO = userCookiePO1;
                }
            }
            UserCookie userCookie = new UserCookie();
            BeanUtils.copyProperties(userCookiePO, userCookie);
            return userCookie;
        }
        return null;
    }

    public UserCookie getOkCookieByUserNumAndType(Student student, String type) throws NoSuchMethodException, ScriptException, IOException {

        List<UserCookiePO> userCookiePOS = userCookieDao.selectByUserNumAndType(student.getNumber(), type);

        if (!userCookiePOS.isEmpty()) {
            UserCookiePO userCookiePO = userCookiePOS.get(0);
            for (UserCookiePO userCookiePO1 : userCookiePOS) {
                if (userCookiePO1.getCreatTime().getTime() > userCookiePO.getCreatTime().getTime()) {
                    userCookiePO = userCookiePO1;
                }
            }

            UserCookie userCookie = new UserCookie();
            BeanUtils.copyProperties(userCookiePO, userCookie);
            if (Fields.AUTHSERVER.equals(type)) {
                if (AuthServerOp.isUserCookieOk(userCookie)) {
                    return userCookie;
                }
            }else if (Fields.WEBVPN1.equals(type)){
                if (WebVpnOneOp.isUserCookieOk(userCookie)) {
                    return userCookie;
                }
            }else if (Fields.LIBRARY.equals(type)){
                if (LibraryCookieOp.isUserCookieOk(userCookie)) {
                    return userCookie;
                }
            }
        }

        return getCookieAndSave(student, type);
    }
}
