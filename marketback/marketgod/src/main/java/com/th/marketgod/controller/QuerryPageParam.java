package com.th.marketgod.controller;

import lombok.Setter;

import java.util.HashMap;

public class QuerryPageParam {
    private int PAGE_SIZE = 20;
    private int PAGE_NUM = 1;
    private int pageSize=PAGE_SIZE;
    private int pageNum=PAGE_NUM;

    @Setter
    private HashMap param=new HashMap();

    public HashMap getParam() {
        return param;
    }


    public long getPageNum() {
        return pageNum;
    }

    public long getPageSize() {
        return pageSize;
    }

}
