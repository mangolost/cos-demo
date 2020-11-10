package com.mangolost.cos.common;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <T>
 */
public class MyPage<T> {

    private int pageNo = 1;

    private int pageSize = 10;

    private int totalPages = 1;

    private int totalCount = 0;

    private List<T> records = new ArrayList<>();

    public MyPage() {

    }

    public MyPage(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
