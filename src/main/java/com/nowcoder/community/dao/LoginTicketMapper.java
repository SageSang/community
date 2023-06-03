package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * ClassName: LoginTicketMapper
 * Package: com.nowcoder.community.dao
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/28 16:16
 * @Version 1.0
 */
@Mapper
// @Deprecated：不推荐使用了
@Deprecated
public interface LoginTicketMapper {

    @Insert("""
            insert into login_ticket (user_id, ticket, status, expired)
            values(#{userId},#{ticket},#{status},#{expired})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select("""
            select id,user_id,ticket,status,expired
            from login_ticket where ticket = #{ticket}
            """)
    LoginTicket selectByTicket(String ticket);

    @Update("""
            <script>
            update login_ticket set status = #{status} where ticket=#{ticket}
            <if test="ticket!=null">
            and 1 = 1
            </if>
            </script>
            """)
    int updateStatus(String ticket, int status);

}
