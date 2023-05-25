package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: UserMapper
 * Package: com.nowcoder.community.dao
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/25 20:45
 * @Version 1.0
 */
@Mapper
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);

}
