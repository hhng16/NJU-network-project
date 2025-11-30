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

    @Resource
    private ReservationMapper reservationMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Override
    public IPage<Reservation> pageReserve(IPage<Reservation> page, Wrapper<Reservation> wrapper) {
        return reservationMapper.pageReserve(page, wrapper);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result reserveGoods(Integer userId, Integer goodsId, Integer reserveNum) {

        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail();
        }

        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) {
            return Result.fail();
        }

        if (reserveNum <= 0) {
            return Result.fail();
        }
        if (goods.getStorage() < reserveNum) {
            return Result.fail();
        }
        Reservation existing = reservationMapper.selectByUserIdAndGoodsId(userId, goodsId);
        if (existing != null) {
            return Result.fail();
        }
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setGoodsId(goodsId);
        reservation.setReserveNum(reserveNum);
        reservation.setCreatedAt(LocalDateTime.now());
        reservationMapper.insert(reservation);
        goods.setStorage(goods.getStorage() - reserveNum);
        goodsMapper.updateById(goods);
        User author = userMapper.selectById(goods.getAuthorid());
        String contactInfo = "联系方式：" + author.getNumber();

        return Result.suc(contactInfo);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result cancelReservation(Integer userId, Integer goodsId) {
        Reservation existing = reservationMapper.selectByUserIdAndGoodsId(userId, goodsId);
        if (existing == null) {
            return Result.fail();
        }
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) {
            return Result.fail();
        }
        QueryWrapper<Reservation> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("user_id", userId)
                .eq("goods_id", goodsId);
        reservationMapper.delete(deleteWrapper);
        goods.setStorage(goods.getStorage() + existing.getReserveNum());
        goodsMapper.updateById(goods);

        return Result.suc("退订成功");
    }
    @Override
    public boolean checkCommentPermission(Integer userId, Integer goodsId) {
        Reservation reservation = reservationMapper.selectByUserIdAndGoodsId(userId, goodsId);
        return reservation != null;
    }
}