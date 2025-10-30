package com.th.marketgod.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.marketgod.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author demo
 * @since 2025-10-30
 */
public interface CommentService extends IService<Comment> {

    IPage pagec(IPage<Comment> page, Wrapper wrapper);
}
