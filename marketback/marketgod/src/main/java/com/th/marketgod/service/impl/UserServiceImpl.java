package com.th.marketgod.service.impl;

import com.th.marketgod.entity.User;
import com.th.marketgod.mapper.UserMapper;
import com.th.marketgod.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author demo
 * @since 2025-07-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
