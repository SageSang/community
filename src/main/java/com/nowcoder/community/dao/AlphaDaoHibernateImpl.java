package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * ClassName: AlphaDaoHibernateImpl
 * Package: com.nowcoder.community.dao
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/25 19:02
 * @Version 1.0
 */
//@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao{

    //@Override
    public String select() {
        return "Hibernate";
    }
}
