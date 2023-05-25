package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * ClassName: AlphaService
 * Package: com.nowcoder.community.service
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/25 19:10
 * @Version 1.0
 */
@Service
//@Scope("prototype")
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("实例化AlphaService");
    }

    @PostConstruct
    public void init() {
        System.out.println("初始化AlphaService");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("销毁AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }
}
