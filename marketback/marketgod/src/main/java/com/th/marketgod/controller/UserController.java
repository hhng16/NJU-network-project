package com.th.marketgod.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.marketgod.entity.User;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.th.marketgod.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author demo
 * @since 2025-07-29
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private  UserService userService;

    @PostMapping("/save")
    public boolean save(@RequestBody User user) {
        return userService.save(user);
    }
    @PostMapping("/mod")
    public Result mod(@RequestBody User user) {
        return userService.updateById(user)? Result.suc(user) : Result.fail();
    }
    @PostMapping("/savemod")
    public boolean savemod(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        return userService.removeById(id)? Result.suc() : Result.fail();
    }
    @PostMapping("/listP")
    public List<User> listP(@RequestBody User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(user.getName())) {
            queryWrapper.like(User::getName, user.getName());
        }
        return userService.list(queryWrapper);
    }
    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QuerryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        Page<User> page = new Page(1,2);
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name)&&!name.equals("null")) {
            queryWrapper.like(User::getName, name);
        }
        IPage result = userService.page(page, queryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        List<User> list = userService.lambdaQuery()
                .eq(User::getNo, user.getNo())
                .eq(User::getPassword, user.getPassword())
                .list();

        return list.size() > 0 ? Result.suc(list.get(0)) : Result.fail();

    }
    @PostMapping("/cancel")
    public Result cancel(@RequestBody User user) {
        List<User> list = userService.lambdaQuery()
                .eq(User::getName, user.getName())
                .list();
        user.setId(list.get(0).getId());
        mod(user);
        return list.size() > 0 ? Result.suc(list.get(0)) : Result.fail();

    }
    @GetMapping("/findByName")
    public Result findByName(@RequestParam String name) {
        List list = userService.lambdaQuery().eq(User::getName, name).list();
        return !list.isEmpty() ? Result.suc(list):Result.fail();
    }
    @GetMapping("/findById")
    public String findById(@RequestParam int id) {
        List list = userService.lambdaQuery().eq(User::getId, id).list();
        User  user = (User) list.get(0);
        return !list.isEmpty() ? user.getName():null;
    }
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        return userService.save(user)? Result.suc() : Result.fail();
    }
    @PostMapping("/updateAvatar")
    public Map<String, Object> updateAvatar(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 根据id更新用户头像
            userService.updateById(user);
            result.put("code", 200);
            result.put("msg", "头像更新成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "头像更新失败: " + e.getMessage());
        }
        return result;
    }

}
