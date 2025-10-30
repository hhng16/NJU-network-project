package com.th.marketgod.entity;

import java.math.BigDecimal;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2025-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Goods对象", description="")
public class Goods implements Serializable {
    @TableField(exist = false)
    private String authorName;

    @TableField(exist = false)
    private String authorNumber;


    // 保持原有的 authorid getter/setter
    public Integer getAuthorid() {
        return authorid;
    }
    public void setAuthorid(Integer authorid) {
        this.authorid = authorid;
    }

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "货物名称")
    private String goodsname;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "库存")
    private Integer storage;


    @ApiModelProperty(value = "作者id")
    private int authorid;

    @ApiModelProperty(value = "标签")
    private Integer tag;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "总评分")
    private Double rateSum;

    @ApiModelProperty(value = "评分数")
    private Integer rateCount;

    @ApiModelProperty(value = "评分")
    private Double rateAvg;

}
