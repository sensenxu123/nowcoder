package com.sensenxu.entity;

import lombok.Data;

/**
 * 封装关于分页的信息
 */

public class Page {
    //当前页码
    private int current = 1;
    //显示上限
    private int limit = 10;
    //数据总数（用于计算总页数）
    private int rows;
    //查询路径（用于复用分页路径）
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current >= 1){
            this.current = current;
        }

    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit >= 1 && limit <=20){
            this.limit = limit;
        }

    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows >= 0 ){
            this.rows = rows;
        }

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     * @return current*limit - current
     */
    public int getOffset(){
        return (current - 1)*limit;
    }

    /**
     * 获取总行数
     * @return
     */
    public int getTotal(){
        if(rows % limit == 0) return rows / limit;
        else return rows / limit + 1;
    }
    /**
     * 获取起始页 当前页往前数两页
     * @return
     */
    public int getFrom(){
        int from = current - 2;
        return from < 1 ? 1: from;
    }
    /**
     * 获取结束页 当前页往后数两页
     * @return
     */
    public int getTo(){
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }
}
