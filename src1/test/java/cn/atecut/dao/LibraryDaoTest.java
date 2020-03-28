package cn.atecut.dao;

import cn.atecut.bean.po.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.script.ScriptException;
import java.io.IOException;

@SpringBootTest
public class LibraryDaoTest {
    @Autowired
    LibraryDao libraryDao;

    @Test
    public void getBorrowBookInfoTest() throws NoSuchMethodException, ScriptException, IOException {
        libraryDao.getBorrowBookInfo(new Student("201720180702", "ly19980911"));
    }

    @Test
    public void getBooksNumByMarcTest() throws IOException, NoSuchMethodException, ScriptException {
        libraryDao.getBooksNumByMarc("53716638667671457a6f4863776950753333756e63673d3d");
    }
    @Test
    public void getBooksByTitleTest() throws IOException, NoSuchMethodException, ScriptException {
        libraryDao.getBooksByTitle("53716638667671457a6f4863776950753333756e63673d3d");
    }
}
