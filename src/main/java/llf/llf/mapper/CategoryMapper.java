package llf.llf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import llf.llf.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    
    List<Category> selectAll();
    
    Category selectById(@Param("id") Integer id);
    
    Category selectByName(@Param("name") String name);
    
    int add(Category category);
    
    int update(Category category);
    
    int deleteById(@Param("id") Integer id);
}
