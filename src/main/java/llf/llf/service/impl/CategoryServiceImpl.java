package llf.llf.service.impl;

import llf.llf.mapper.CategoryMapper;
import llf.llf.pojo.Category;
import llf.llf.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> selectAll() {
        return categoryMapper.selectAll();
    }

    @Override
    public Category selectById(Integer id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public Category selectByName(String name) {
        return categoryMapper.selectByName(name);
    }

    @Override
    public int add(Category category) {
        return categoryMapper.add(category);
    }

    @Override
    public int update(Category category) {
        return categoryMapper.update(category);
    }

    @Override
    public int deleteById(Integer id) {
        return categoryMapper.deleteById(id);
    }

    @Override
    public List<Category> selectAllWithPostCount() {
        return categoryMapper.selectAllWithPostCount();
    }
}
