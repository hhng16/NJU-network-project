package com.th.marketgod.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.marketgod.entity.Reservation;
import com.th.marketgod.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@Api(tags = "商品预订与退订接口")
public class ReservationController {
    @Resource
    private ReservationService reservationService;
    @PostMapping("/reserve")
    @ApiOperation("商品预订")
    public Result reserve(
            @RequestParam Long userId,
            @RequestParam Integer goodsId,
            @RequestParam Integer reserveNum
    ) {
        return reservationService.reserveGoods(userId, goodsId, reserveNum);
    }
    @PostMapping("/cancel")
    @ApiOperation("商品退订")
    public Result cancel(
            @RequestParam Long userId,
            @RequestParam Integer goodsId
    ) {
        return reservationService.cancelReservation(userId, goodsId);
    }
    @GetMapping("/check-comment-perm")
    @ApiOperation("校验用户评论权限")
    public Result checkCommentPermission(
            @RequestParam Long userId,
            @RequestParam Integer goodsId
    ) {
        boolean hasPermission = reservationService.checkCommentPermission(userId, goodsId);
        return Result.suc(hasPermission);
    }
    @GetMapping("/page")
    @ApiOperation("分页查询预订记录")
    public Result pageReserve(
            @RequestParam long current,
            @RequestParam long size,
            Wrapper<Reservation> wrapper
    ) {
        IPage<Reservation> page = new Page<>(current, size);
        IPage<Reservation> reservePage = reservationService.pageReserve(page, wrapper);
        return Result.suc(reservePage.getRecords(), reservePage.getTotal());
    }
}
