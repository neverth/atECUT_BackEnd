package cn.atecut.service;

import cn.atecut.dao.DoubanDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DoubanService {

    @Autowired
    DoubanDao doubanDao;

    private Logger logger = LogManager.getLogger(DoubanService.class);

    public String getBookInfoByIsbn(String isbn) throws IOException {
        return doubanDao.getBookInfoByIsbn(isbn);
    }
}
