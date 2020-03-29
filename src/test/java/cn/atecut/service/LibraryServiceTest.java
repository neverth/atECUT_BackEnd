package cn.atecut.service;


import cn.atecut.bean.po.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.script.ScriptException;
import java.io.IOException;

@SpringBootTest
public class LibraryServiceTest {

    @Autowired
    LibraryService libraryService;

    @Test
    public void getStuBorrowBookInfoTest() throws NoSuchMethodException, ScriptException, IOException {
        libraryService.getStuBorrowBookInfo(new Student("201720180702", "ly19980911"));
    }
}
