package com.nowcoder.community.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * ClassName: Page
 * Package: com.nowcoder.community.entity
 * Description:
 * 封装分页相关信息
 *
 * @Autuor Dongjie Sang
 * @Create 2023 /5/25 23:01
 * @Version 1.0
 */
@Data
public class Page {
    // 当前页码
    @Min(value = 1, message = "论坛页面最小为第一页")
    private int current = 1;
    // 显示上限
    @Min(value = 1, message = "分页最小为1！")
    @Max(value = 100, message = "分页最大为100！")
    private int limit = 10;
    // 数据总数（用于计算整数）
    @Min(value = 0, message = "数据总数不能为负！")
    private int rows;
    // 查询路径（用于复用分页链接）
    private String path;

    /**
     * 获取当前页的起始行
     *
     * @return the offset
     */
    public int getOffset() {
        // current * limit - limit
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     *
     * @return the total
     */
    public int getTotal() {
        // rows / limit [+1]
        if ((rows % limit) == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码
     *
     * @return the from
     */
    public int getFrom() {
        int from = current - 2;
        return Math.max(from, 1);
    }

    /**
     * 获取终止页码
     *
     * @return the to
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return Math.min(to, total);
    }
}
