package cn.atecut.bean.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author NeverTh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "借阅图书VO实体")
public class BorrowBookVO {

    @ApiModelProperty(value = "条形码")
    String marcRecNo;

    @ApiModelProperty(value = "题名/责任者")
    String titleAndAu;

    @ApiModelProperty(value = "借阅日期")
    String borrowTime;

    @ApiModelProperty(value = "应还日期")
    String returnTime;

    @ApiModelProperty(value = "续借量")
    String renewQuantity;

    @ApiModelProperty(value = "馆藏地")
    String holdings;

}
