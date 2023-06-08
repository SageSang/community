package com.nowcoder.community.dao.elasticsearch;

import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * ClassName: DiscussPostRepository
 * Package: com.nowcoder.community.dao.elasticsearch
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/7 20:28
 * @Version 1.0
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
