package cn.atecut.dao;


import cn.atecut.bean.po.UserCookiePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author NeverTh
 */

@Repository
public interface UserCookieDao extends JpaRepository<UserCookiePO, Long>, JpaSpecificationExecutor<UserCookiePO> {

    @Query(value="select * from cookies where user_number = ?", nativeQuery=true)
    List<UserCookiePO> selectByUserNum(String userName);

    @Query(value="select * from cookies where user_number = ? and type = ?", nativeQuery=true)
    List<UserCookiePO> selectByUserNumAndType(String userName, String type);

}
