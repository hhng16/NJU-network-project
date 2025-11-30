package com.th.marketgod.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.marketgod.entity.Comment;
import com.th.marketgod.entity.Goods;
import com.th.marketgod.service.CommentService;
import com.th.marketgod.service.GoodsService;
import com.th.marketgod.service.ReservationService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author demo
 * @since 2025-10-30
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ReservationService reservationService;

//    @PostMapping("/save")
//    public boolean save(@RequestBody Comment comment) {
//        commentService.save(comment);
//        Goods g = goodsService.getById(comment.getGoodsid());
//        double currentSum = g.getRateSum();
//
//        currentSum += comment.getRate();
//        Integer currentCount = g.getRateCount();
//        currentCount = currentCount + 1;
//
//        double avg = currentSum / currentCount;
//        //System.out.println(avg);
//        BigDecimal bd = BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
//        double newAvg = bd.doubleValue();
//
//        g.setRateSum(currentSum);
//        g.setRateCount(currentCount);
//        g.setRateAvg(newAvg);
//
//        boolean updated = goodsService.updateById(g);
//
//        return true;
//    }
@PostMapping("/save")
public Result save(@RequestBody Comment comment) {
    try {
        //检查是否预订
        Long userId = comment.getUserid();
        Integer goodsId = comment.getGoodsid();
        boolean hasPermission = reservationService.checkCommentPermission(userId, goodsId);
        if (!hasPermission) {
            return Result.fail();
        }
        // 检查是否已存在该用户对商品的评论
        Comment existing = commentService.getOne(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getUserid, comment.getUserid())
                .eq(Comment::getGoodsid, comment.getGoodsid()));


        if (existing != null) {

            System.out.println("已存在该用户对商品的评论");
            return Result.fail();
        }

        // 保存评论
        boolean saved = commentService.save(comment);

        if (saved) {
            // 更新商品评分统计
            System.out.println("成功半！");
            Goods g = goodsService.getById(comment.getGoodsid());
            double currentSum = g.getRateSum();
            currentSum += comment.getRate();
            Integer currentCount = g.getRateCount() + 1;
            double avg = currentSum / currentCount;
            BigDecimal bd = BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
            double newAvg = bd.doubleValue();
            System.out.println("成功！");
            // 继续执行后续逻辑...
            return Result.suc();
        } else {
            System.out.println("死");
            return Result.fail();
        }
    } catch (Exception e) {
        System.out.println("驾崩");
        return Result.fail();
    }
}

    @PostMapping("/mod")
    public Result mod(@RequestBody Comment comment) {
        try {
            Goods g = goodsService.getById(comment.getGoodsid());
            double currentSum = g.getRateSum();

            // 获取原始评论
            Comment orig_c = commentService.getOne(
                    new QueryWrapper<Comment>()
                            .eq("userid", comment.getUserid())
                            .eq("goodsid", comment.getGoodsid())
            );

            if (orig_c == null) {
                return Result.fail();
            }

            currentSum -= orig_c.getRate();
            currentSum += comment.getRate();

            double avg = currentSum / g.getRateCount();
            BigDecimal bd = BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
            double newAvg = bd.doubleValue();

            g.setRateSum(currentSum);
            g.setRateAvg(newAvg);

            // 更新商品评分
            boolean goodsUpdated = goodsService.updateById(g);

            // 使用条件构造器更新评论
            boolean commentUpdated = commentService.update(
                    comment,
                    new UpdateWrapper<Comment>()
                            .eq("userid", comment.getUserid())
                            .eq("goodsid", comment.getGoodsid())
            );

            return commentUpdated ? Result.suc(comment) : Result.fail();

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
    @PostMapping("/savemod")
    public Result savemod(@RequestBody Comment comment) {
        try {
            // 检查评论是否存在
            Comment existing = commentService.getOne(new LambdaQueryWrapper<Comment>()
                    .eq(Comment::getUserid, comment.getUserid())
                    .eq(Comment::getGoodsid, comment.getGoodsid()));

            if (existing != null) {
                // 更新现有评论 - 需要更新商品评分统计
                Goods g = goodsService.getById(comment.getGoodsid());
                if (g != null) {
                    double currentSum = g.getRateSum();
                    // 减去原来的评分，加上新的评分
                    currentSum = currentSum - existing.getRate() + comment.getRate();

                    // 计算新的平均分
                    double avg = currentSum / g.getRateCount();
                    BigDecimal bd = BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
                    double newAvg = bd.doubleValue();

                    // 更新商品评分统计
                    g.setRateSum(currentSum);
                    g.setRateAvg(newAvg);
                    goodsService.updateById(g);
                }

                // 更新评论
                boolean updated = commentService.update(comment,
                        new LambdaQueryWrapper<Comment>()
                                .eq(Comment::getUserid, comment.getUserid())
                                .eq(Comment::getGoodsid, comment.getGoodsid()));
                return updated ? Result.suc("评论更新成功") : Result.fail();
            } else {
                // 创建新评论 - 与save方法逻辑相同
                boolean saved = commentService.save(comment);
                if (saved) {
                    Goods g = goodsService.getById(comment.getGoodsid());
                    if (g != null) {
                        double currentSum = g.getRateSum() + comment.getRate();
                        Integer currentCount = g.getRateCount() + 1;
                        double avg = currentSum / currentCount;
                        BigDecimal bd = BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
                        double newAvg = bd.doubleValue();

                        g.setRateSum(currentSum);
                        g.setRateCount(currentCount);
                        g.setRateAvg(newAvg);
                        goodsService.updateById(g);
                    }
                    return Result.suc("评论保存成功");
                } else {
                    return Result.fail();
                }
            }
        } catch (Exception e) {
            return Result.fail();
        }
    }


    @GetMapping("/delete")
    public Result delete(@RequestParam Integer userid, @RequestParam Integer goodsid) {
        Comment comment = commentService.getOne(
                new QueryWrapper<Comment>()
                        .eq("userid", userid)
                        .eq("goodsid", goodsid)
        );

        Goods g = goodsService.getById(goodsid);
        if (g != null) {
            double newSum = g.getRateSum() - comment.getRate();
            int newCount = g.getRateCount() - 1;

            double newAvg = 0;
            if (newCount > 0) {
                newAvg = newSum / newCount;
                BigDecimal bd = BigDecimal.valueOf(newAvg).setScale(2, RoundingMode.HALF_UP);
                newAvg = bd.doubleValue();
            }

            g.setRateSum(newSum);
            g.setRateCount(newCount);
            g.setRateAvg(newAvg);

            goodsService.updateById(g);
        }

        // 删除评论
        boolean removed = commentService.remove(
                new QueryWrapper<Comment>()
                        .eq("userid", userid)
                        .eq("goodsid", goodsid)
        );

        return removed ? Result.suc() : Result.fail();
    }

    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QuerryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        Integer goodsid = (Integer) param.get("goodsid");
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        if(goodsid== null){
//            System.out.println(goodsid+"那一天的忧郁忧郁起来");
//            System.out.println(goodsid+"那一天的忧郁忧郁起来");
//        }
        if (goodsid != null && goodsid > 0) {
            lambdaQueryWrapper.eq(Comment::getGoodsid, goodsid);
        }
        Page<Comment> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage result = commentService.pagec(page, lambdaQueryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }

}
