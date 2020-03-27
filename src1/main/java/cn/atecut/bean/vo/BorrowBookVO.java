package cn.atecut.bean.vo;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author NeverTh
 */
@Data
@AllArgsConstructor
public class BorrowBookVO {

    String marcRecNo;

    String titleAndAu;

    String borrowTime;

    String returnTime;

    String renewQuantity;

    String holdings;

}
