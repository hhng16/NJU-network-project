package com.th.marketgod.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.marketgod.entity.Goods;
import com.th.marketgod.entity.User;
import com.th.marketgod.service.GoodsService;
import com.th.marketgod.service.UserService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author demo
 * @since 2025-07-29
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserService userService;


    @PostMapping("/save")
    public boolean save(@RequestBody Goods goods) {
        return goodsService.save(goods);
    }
    @PostMapping("/mod")
    public Result mod(@RequestBody Goods goods) {
        return goodsService.updateById(goods)? Result.suc(goods) : Result.fail();
    }
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods) {
        return goodsService.saveOrUpdate(goods)?Result.suc(goods) : Result.fail();
    }
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        return goodsService.removeById(id)? Result.suc() : Result.fail();
    }





    @GetMapping("/list")
    public Result list(){
        List list = goodsService.list();
        return Result.suc(list);
    }
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QuerryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("goodsname");
        Integer tagob=( Integer)param.get("tag");
        int tag = tagob != null ? tagob : 0;
        Integer authoridobj = (Integer)param.get("authorid");
        int authorid = authoridobj != null ? authoridobj : 0;
//        String author = (String) param.get("author");
//        Integer receiverObj = (Integer) param.get("receiver");
//        int receiver = receiverObj != null ? receiverObj : 0; // 默认值设为 0 或其他业务含义的默认值
        Page<Goods> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Goods::getGoodsname,name);
        }
//        if(StringUtils.isNotBlank(author) && !"null".equals(author)){
//            lambdaQueryWrapper.like(Goods::getAuthor,author);
//        }
        if(tag != 0){
            lambdaQueryWrapper.eq(Goods::getTag,tag);
        }
        if(authorid > 0){
            lambdaQueryWrapper.eq(Goods::getAuthorid,authorid);
        }
        IPage result = goodsService.page(page,lambdaQueryWrapper);
        List<Goods> records = result.getRecords();

        // --- 批量查询作者并映射 authorName（避免 N+1） ---
        Set<Integer> authorIds = records.stream()
                .map(Goods::getAuthorid)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (!authorIds.isEmpty()) {
            Set<Long> authorIdLongs = authorIds.stream().map(Integer::longValue).collect(Collectors.toSet());

            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.in(User::getId, authorIdLongs);
            List<User> users = userService.list(userWrapper);

            Map<Integer, String> idToName = users.stream()
                    .collect(Collectors.toMap(User::getId, User::getName));
            Map<Integer, String> idToNumber = users.stream()
                    .collect(Collectors.toMap(User::getId, User::getNumber));

            for (Goods g : records) {
                if (g.getAuthorid() != null) {
                    String authorName = idToName.get(g.getAuthorid());
                    String authorNumber = idToNumber.get(g.getAuthorid());
                    g.setAuthorName(authorName != null ? authorName : "未知");
                    g.setAuthorNumber(authorNumber);
                }
            }
        }

        return Result.suc(records, result.getTotal());
    }
}
