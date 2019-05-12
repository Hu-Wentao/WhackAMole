package com.example.whackamole.adapter;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/5/12
 */
public class Record implements Comparable<Record> {
    private String recordAccount;   // 一条分数记录的 用户名
    private int recordScore;        // 一条分数记录的分数值

    public Record(String recordAccount, int recordScore) {
        this.recordAccount = recordAccount;
        this.recordScore = recordScore;
    }

    public String getRecordAccount() {
        return recordAccount;
    }

    public int getRecordScore() {
        return recordScore;
    }

    // 以 分数进行排序 // 用不上, 应该直接在SQL查询时进行排序
    @Override
    public int compareTo(Record o) {
        // 降序 排列
        return o.recordScore - this.recordScore;
    }
}
