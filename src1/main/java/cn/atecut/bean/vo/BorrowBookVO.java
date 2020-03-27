package cn.atecut.bean.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author NeverTh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowBookVO {

    String marcRecNo;

    String titleAndAu;

    String borrowTime;

    String returnTime;

    String renewQuantity;

    String holdings;

}
