package cn.atecut.bean.vo;

import cn.atecut.bean.BookInfo;
import lombok.Data;

import java.util.List;

/**
 * @author neverth
 */
@Data
public class BooksInfo {
    int total;
    List<BookInfo> bookInfos;
}
