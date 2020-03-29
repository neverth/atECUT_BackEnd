package cn.atecut.task;


import cn.atecut.bean.User;
import cn.atecut.controller.LibraryController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class CookiesMonitor {

    private static Logger logger = LogManager.getLogger(CookiesMonitor.class);

    @Autowired
    private LibraryController libraryController;

    @Scheduled(cron = "0 */29 * * * ?")
    public void refreshCookiesTask(){
        logger.debug("模拟用户请求开始，当cookies无效时刷新cookies");
        libraryController.commonGetUserCookies(
                        new User("201720180702", "ly19980911"));

    }
}
