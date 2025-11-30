package com.th.marketgod.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.th.marketgod.controller.Result;
import com.th.marketgod.entity.Reservation;

/**
 * <p>
 *
 * </p>
 *
 * @author demo
 * @since 2025-10-30
 */
public interface ReservationService extends IService<Reservation> {


    IPage<Reservation> pageReserve(IPage<Reservation> page, Wrapper<Reservation> wrapper);
    Result reserveGoods(Integer userId, Integer goodsId, Integer reserveNum);
    Result cancelReservation(Integer userId, Integer goodsId);
    boolean checkCommentPermission(Integer userId, Integer goodsId);
}
