package cn.atecut.service;

import cn.atecut.bean.po.BookPO;
import cn.atecut.bean.po.BorrowBookPO;
import cn.atecut.bean.po.Student;
import cn.atecut.bean.vo.BookVO;
import cn.atecut.bean.vo.BorrowBookVO;
import cn.atecut.dao.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.*;

/**
 * @author liyang
 */
@Service
public class LibraryService {

    @Autowired
    LibraryDao libraryDao;


    public List<BorrowBookVO> getStuBorrowBookInfo(Student student) throws NoSuchMethodException, ScriptException, IOException {

        List<BorrowBookPO> borrowBookInfo = libraryDao.getBorrowBookInfo(student);
        List<BorrowBookVO> borrowBookVOS = new ArrayList<>();

        for (BorrowBookPO book : borrowBookInfo) {
            BorrowBookVO borrowBookVO = new BorrowBookVO();
            BeanUtils.copyProperties(book, borrowBookVO);
            borrowBookVOS.add(borrowBookVO);
        }

        return borrowBookVOS;
    }

    public List<BookVO.BookBorrowInfo> getBooksNumByMarc(String mac) throws NoSuchMethodException, ScriptException, IOException {

        List<BookPO.BookBorrowInfo> booksNumByMarc = libraryDao.getBooksNumByMarc(mac);
        List<BookVO.BookBorrowInfo> borrowBookVOS = new ArrayList<>();

        for (BookPO.BookBorrowInfo book : booksNumByMarc) {
            BookVO.BookBorrowInfo borrowBookVO = new BookVO.BookBorrowInfo();
            BeanUtils.copyProperties(book, borrowBookVO);
            borrowBookVOS.add(borrowBookVO);
        }
        return borrowBookVOS;
    }

    public String getBooksByTitle(String reqData) throws IOException, NoSuchMethodException, ScriptException {
        return libraryDao.getBooksByTitle(reqData);
    }
}
