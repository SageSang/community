package com.nowcoder.community.controller;

import com.nowcoder.community.service.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * ClassName: DataController
 * Package: com.nowcoder.community.controller
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/12 13:48
 * @Version 1.0
 */
@Controller
public class DataController {
    @Autowired
    private DateService dateService;

    /**
     * Gets data page.
     * 打开统计页面的方法
     *
     * @return the data page
     */
    @RequestMapping(value = "/data", method = {RequestMethod.GET, RequestMethod.POST})
    public String getDataPage() {
        return "site/admin/data";
    }

    /**
     * Gets uv.
     * 处理统计网站UV的请求
     *
     * @param start the start
     * @param end   the end
     * @return the uv
     */
    @PostMapping("/data/uv")
    public String getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                        Model model) {
        long uv = dateService.calculateUV(start, end);
        model.addAttribute("uvResult", uv);
        model.addAttribute("uvStartDate", start);
        model.addAttribute("uvEndDate", end);

        return "forward:/data";
    }

    /**
     * Gets dau.
     * 处理统计网站DAU的请求
     *
     * @param start the start
     * @param end   the end
     * @param model the model
     * @return the dau
     */
    @PostMapping("/data/dau")
    public String getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                         Model model) {
        long dau = dateService.calculateDAU(start, end);
        model.addAttribute("dauResult", dau);
        model.addAttribute("dauStartDate", start);
        model.addAttribute("dauEndDate", end);

        return "forward:/data";
    }
}
