package cn.atecut.bean;


import lombok.Data;

import java.util.List;

/**
 * @author 李洋
 */
@Data
public class BookInfo {

    @Data
    public static class BookNum{
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
    List<BookNum> bookNums;
}


