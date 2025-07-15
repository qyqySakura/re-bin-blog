package llf.llf.controller;

import llf.llf.common.Result;
import llf.llf.pojo.Category;
import llf.llf.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 查询所有分类
    @GetMapping
    public Result<List<Category>> getAllCategories() {
        return Result.success(categoryService.selectAll());
    }

    // 根据ID查询分类
    @GetMapping("/{id}")
    public Result<Category> getCategoryById(@PathVariable Integer id) {
        return Result.success(categoryService.selectById(id));
    }

    // 根据名称查询分类
    @GetMapping("/name/{name}")
    public Result<Category> getCategoryByName(@PathVariable String name) {
        return Result.success(categoryService.selectByName(name));
    }

    // 新增分类
    @PostMapping("/add")
    public Result<Integer> createCategory(@RequestBody Category category) {
        return Result.success(categoryService.add(category));
    }

    // 更新分类
    @PutMapping("/update")
    public Result<Integer> updateCategory(@RequestBody Category category) {
        return Result.success(categoryService.update(category));
    }

    // 删除分类
    @DeleteMapping("/del/{id}")
    public Result<Integer> deleteCategory(@PathVariable Integer id) {
        return Result.success(categoryService.deleteById(id));
    }
}
