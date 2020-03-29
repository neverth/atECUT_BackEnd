package cn.atecut.bean.po;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author NeverTh
 */
@Data
@AllArgsConstructor
public class BorrowBookPO {

    String marcRecNo;

    String titleAndAu;

    String borrowTime;

    String returnTime;

    String renewQuantity;

    String holdings;
}
