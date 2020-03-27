package cn.atecut.service;

import cn.atecut.bean.BookInfo;
import cn.atecut.bean.User;
import cn.atecut.bean.po.BorrowBookPO;
import cn.atecut.bean.po.Student;
import cn.atecut.bean.pojo.Fields;
import cn.atecut.bean.pojo.UserCookie;
import cn.atecut.bean.vo.BooksInfo;
import cn.atecut.bean.vo.BorrowBookVO;
import cn.atecut.dao.*;
import cn.atecut.unti.RequestUtil;
import cn.atecut.unti.WebVpnOneOp;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
}
