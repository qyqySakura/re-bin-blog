package llf.llf.service;

import llf.llf.pojo.Category;
import java.util.List;

public interface CategoryService {
    
    List<Category> selectAll();
    
    Category selectById(Integer id);
    
    Category selectByName(String name);
    
    int add(Category category);
    
    int update(Category category);
    
    int deleteById(Integer id);
}
