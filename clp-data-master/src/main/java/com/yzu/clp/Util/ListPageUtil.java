package com.yzu.clp.Util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页工具类
 * @param <T>
 */
public class ListPageUtil<T> {
    private List<T> data;

    /** 上一页 */
    private int lastPage;

    /** 当前页 */
    private int page;

    /** 下一页 */
    private int nextPage;
//
    /** 每页条数 */
    private int limit;

    /** 总页数 */
    private int totalPage;

    /** 总数据条数 */
    private int totalCount;

    public ListPageUtil(List<T> data, int page, int limit) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("data must be not empty!");
        }

        this.data = data;
        this.limit = limit;
        this.page = page;
        this.totalCount = data.size();
        this.totalPage = (totalCount + limit - 1) / limit;
        this.lastPage = page-1>1? page-1:1;
        this.nextPage = page>=totalPage? totalPage: page + 1;

    }


    /**
     * 得到分页后的数据
     * @return 分页后结果
     */
//    public List<T> getPagedLst() {
//        int fromIndex = (nowPage - 1) * limit;
//        if (fromIndex >= data.size()) {
//            return Collections.emptyList();//空数组
//        }
//        if(fromIndex<0){
//            return Collections.emptyList();//空数组
//        }
//        int toIndex = nowPage * limit;
//        if (toIndex >= data.size()) {
//            toIndex = data.size();
//        }
//        return data.subList(fromIndex, toIndex);
//    }

    public int getLimit() {
        return limit;
    }

    public List<T> getData() {
        int fromIndex = (page - 1) * limit;
        if (fromIndex >= data.size()) {
            return Collections.emptyList();//空数组
        }
        if(fromIndex<0){
            return Collections.emptyList();//空数组
        }
        int toIndex = page * limit;
        if (toIndex >= data.size()) {
            toIndex = data.size();
        }
        return data.subList(fromIndex, toIndex);
    }
    public int getLastPage() {
        return lastPage;
    }

    public int getPage() {
        return page;
    }

    public int getNextPage() {
        return nextPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }
}


/**
 * 使用例
 */
//if(moniAnnexPageInfoList.size() != 0){
//        ListPageUtil listPageUtil = new ListPageUtil(moniAnnexPageInfoList,page,limit);
//        Map<String, Object> pageDataMap = new HashMap<>();
//        pageDataMap.put("dataTotal", listPageUtil.getTotalCount());
//        pageDataMap.put("pages", listPageUtil.getTotalPage());
//        List<?> dataList = listPageUtil.getData();
//        pageDataMap.put("dataList", dataList);
//        result.setMsg("获取成功！");
//        result.setStatus(0);
//        result.setData(pageDataMap);
//        }else{
//        result.setMsg("获取成功！但数据为空！");
//        result.setStatus(0);
//        result.setData(null);
//        }
