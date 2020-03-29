package cn.atecut.bean.po;

import cn.atecut.bean.BookInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author NeverTh
 */
@Data

public class BookPO {

    @Data
    public static class BookBorrowInfo{
        String indexNum;
        String barcode;
        String holdings;
        String state;
    }

    String author;
    String callNo;
    String docTypeName;
    String isbn;
    String marcRecNo;
    String num;
    String pubYear;
    String publisher;
    String title;
    List<BookInfo.BookNum> bookNums;
}
