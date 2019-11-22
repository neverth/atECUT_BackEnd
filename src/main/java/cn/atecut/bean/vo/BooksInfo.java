package cn.atecut.bean.vo;

import cn.atecut.bean.BookInfo;
import lombok.Data;

import java.util.List;

/**
 * @author 华
 */
@Data
public class BooksInfo {
    int total;
    List<BookInfo> bookInfos;
}
