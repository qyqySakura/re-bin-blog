package llf.llf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import llf.llf.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    
    List<Tag> selectAll();
    
    Tag selectById(@Param("id") Integer id);
    
    Tag selectByName(@Param("name") String name);
    
    int add(Tag tag);
    
    int update(Tag tag);
    
    int deleteById(@Param("id") Integer id);
    
    // 根据文章ID查询标签
    List<Tag> selectByPostId(@Param("postId") Integer postId);
    
    // 为文章添加标签
    int addPostTag(@Param("postId") Integer postId, @Param("tagId") Integer tagId);
    
    // 删除文章的所有标签
    int deletePostTags(@Param("postId") Integer postId);
}
