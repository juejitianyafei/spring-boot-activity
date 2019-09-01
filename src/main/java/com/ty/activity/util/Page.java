package com.ty.activity.util;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Page<T> implements Serializable {

    //起始位置
    private int start;
    //行数
    private int pagesize;
    // 页数（第几页）
    private int currentpage;
    // 查询数据库里面对应的数据有多少条
    private long total;// 从数据库查处的总记录数

    private List<T> rows;

    /****
     *
     * @param currentpage
     * @param pagesize
     */
    public void setCurrentpage(int currentpage,int pagesize) {
        this.start = (currentpage-1)*pagesize;

    }

    /****
     *
     * @param currentpage    当前页
     * @param pagesize    每页显示多少条
     */
    public Page(int currentpage,int pagesize) {
        this.pagesize = pagesize;
        this.currentpage = currentpage;
        //计算当前页和数据库查询起始值以及总页数
        setCurrentpage(currentpage, pagesize);
    }


}
