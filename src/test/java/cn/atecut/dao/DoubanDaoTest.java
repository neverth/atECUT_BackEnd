package cn.atecut.dao;

import org.junit.Test;

public class DoubanDaoTest {
    @Test
    public void test1(){
        DoubanDao doubanDao = new DoubanDao();
        doubanDao.getBookInfoByNum(doubanDao.getNumByISBN("978-7-302-15955-1"));

    }
    @Test
    public void test(){
        System.out.println(System.currentTimeMillis());
    }
}
