package com.th.marketgod.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value="Comment对象", description="")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userid;

    private Integer goodsid;

    private Integer rate;

    private String message;

    private LocalDateTime createdAt;

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String goodsname;

}
