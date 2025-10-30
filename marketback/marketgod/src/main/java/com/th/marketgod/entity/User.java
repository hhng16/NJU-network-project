package com.th.marketgod.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Blob;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.BlobTypeHandler;

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
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    @TableField(typeHandler = BlobTypeHandler.class)

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "账号")
    private String no;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "余额")
    private Integer account;

    @ApiModelProperty(value = "角色 0超级管理员 1管理员 2用户")
    private Integer roleid;

    @ApiModelProperty(value = "头像")
    private String image;

    @ApiModelProperty(value = "联系方式")
    private String number;

}
