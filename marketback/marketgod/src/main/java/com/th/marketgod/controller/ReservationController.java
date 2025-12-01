package com.th.marketgod.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.marketgod.entity.Reservation;
import com.th.marketgod.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@Api(tags = "商品预订与退订接口")
public class ReservationController {
    @Resource
    private ReservationService reservationService;
//    @PostMapping("/reserve")
//
//    @PostMapping("/cancel")
//    @GetMapping("/check-comment-perm")
//
//    @GetMapping("/page")
@PostMapping("/reserve")
private Result reserveGoods(@RequestBody Reservation reservation) {
    try {
        // 检查是否已存在该用户对商品的预订
        boolean exists = reservationService.exists(new QueryWrapper<Reservation>()
                .eq("user_id", reservation.getUserId())
                .eq("goods_id", reservation.getGoodsId()));

        if (exists) {
            // 更新现有预订
            boolean updated = reservationService.update(reservation,
                    new QueryWrapper<Reservation>()
                            .eq("user_id", reservation.getUserId())
                            .eq("goods_id", reservation.getGoodsId()));
            return updated ? Result.suc() : Result.fail();
        } else {
            // 创建新预订
            boolean saved = reservationService.save(reservation);
            return saved ? Result.suc() : Result.fail();
        }
    } catch (Exception e) {
        return Result.fail();
    }
}

    @PostMapping("/cancel")
    private Result cancelReservation(@RequestBody Reservation reservation) {
        Wrapper<Reservation> wrapper = new QueryWrapper<Reservation>()
                .eq("user_id", reservation.getUserId())
                .eq("goods_id", reservation.getGoodsId());

        return reservationService.remove(wrapper) ? Result.suc() : Result.fail();
    }
    @GetMapping("/check-comment-perm")
    private Result checkCommentPerm(@RequestParam Integer userId, @RequestParam Integer goodsId) {

        Wrapper<Reservation> wrapper = new QueryWrapper<Reservation>()
                .eq("user_id", userId)
                .eq("goods_id", goodsId);
        boolean exists = reservationService.exists(wrapper);

        return reservationService.count(wrapper) > 0 ? Result.suc() : Result.fail();
    }
}
