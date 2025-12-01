package com.th.marketgod.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.th.marketgod.controller.Result;
import com.th.marketgod.entity.Goods;
import com.th.marketgod.entity.Reservation;
import com.th.marketgod.entity.User;
import com.th.marketgod.mapper.GoodsMapper;
import com.th.marketgod.mapper.ReservationMapper;
import com.th.marketgod.mapper.UserMapper;
import com.th.marketgod.service.ReservationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author demo
 * @since 2025-10-30
 */
@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService {


}