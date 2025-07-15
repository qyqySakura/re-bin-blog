package llf.llf.service;

import llf.llf.pojo.Tag;
import java.util.List;

public interface TagService {
    
    List<Tag> selectAll();
    
    Tag selectById(Integer id);
    
    Tag selectByName(String name);
    
    int add(Tag tag);
    
    int update(Tag tag);
    
    int deleteById(Integer id);
    
    // 根据文章ID查询标签
    List<Tag> selectByPostId(Integer postId);
    
    // 为文章设置标签
    void setPostTags(Integer postId, List<Integer> tagIds);
}
