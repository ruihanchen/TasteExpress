package org.chendev.tasteexpress.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPaymentVO {

    private String nonceStr;

    private String paySign;

    private String timeStamp;

    private String signType;

    private String packageStr;
}
