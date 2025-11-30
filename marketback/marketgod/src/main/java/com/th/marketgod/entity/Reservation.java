package com.th.marketgod.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author demo
 * @since 2025-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Reservation对象", description = "商品预订记录")
@TableName("reservation") // 确保与数据库表名一致
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("user_id")
    @ApiModelProperty(value = "预订用户ID")
    private Long userId;

    @TableField("goods_id")
    @ApiModelProperty(value = "预订商品ID")
    private Integer goodsId;

    @TableField("reserve_num")
    @ApiModelProperty(value = "预订数量", required = true, example = "1")
    private Integer reserveNum;

    @TableField("created_at")
    @ApiModelProperty(value = "预订创建时间")
    private LocalDateTime createdAt;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品名称（非数据库字段，仅用于前端展示）")
    private String goodsname;

    @TableField(exist = false)
    @ApiModelProperty(value = "预订用户昵称（非数据库字段，仅用于前端展示）")
    private String username;
}
