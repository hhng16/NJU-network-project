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

    @PostMapping("/save")
    public boolean save(@RequestBody Comment comment) {
        commentService.save(comment);
        Goods g = goodsService.getById(comment.getGoodsid());
        double currentSum = g.getRateSum();

        currentSum += comment.getRate();
        Integer currentCount = g.getRateCount();
        currentCount = currentCount + 1;

        double avg = currentSum / currentCount;
        //System.out.println(avg);
        BigDecimal bd = BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
        double newAvg = bd.doubleValue();

        g.setRateSum(currentSum);
        g.setRateCount(currentCount);
        g.setRateAvg(newAvg);

        boolean updated = goodsService.updateById(g);

        return true;
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
    public boolean savemod(@RequestBody Comment comment) {
        return commentService.saveOrUpdate(comment);
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
