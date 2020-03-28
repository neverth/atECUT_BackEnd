package cn.atecut.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author NeverTh
 */
@Data
@ApiModel(value = "书籍实体")
public class BookVO {

    @Data
    @ApiModel(value = "书籍可借信息实体")
    public static class BookBorrowInfo{
        @ApiModelProperty(value = "图书id码")
        String indexNum;
        @ApiModelProperty(value = "barcode")
        String barcode;
        @ApiModelProperty(value = "馆藏地")
        String holdings;
        @ApiModelProperty(value = "可借状态")
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
}
