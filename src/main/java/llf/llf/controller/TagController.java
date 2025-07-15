package llf.llf.controller;

import llf.llf.common.Result;
import llf.llf.pojo.Tag;
import llf.llf.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    // 查询所有标签
    @GetMapping
    public Result<List<Tag>> getAllTags() {
        return Result.success(tagService.selectAll());
    }

    // 根据ID查询标签
    @GetMapping("/{id}")
    public Result<Tag> getTagById(@PathVariable Integer id) {
        return Result.success(tagService.selectById(id));
    }

    // 根据名称查询标签
    @GetMapping("/name/{name}")
    public Result<Tag> getTagByName(@PathVariable String name) {
        return Result.success(tagService.selectByName(name));
    }

    // 根据文章ID查询标签
    @GetMapping("/post/{postId}")
    public Result<List<Tag>> getTagsByPostId(@PathVariable Integer postId) {
        return Result.success(tagService.selectByPostId(postId));
    }

    // 新增标签
    @PostMapping("/add")
    public Result<Integer> createTag(@RequestBody Tag tag) {
        return Result.success(tagService.add(tag));
    }

    // 更新标签
    @PutMapping("/update")
    public Result<Integer> updateTag(@RequestBody Tag tag) {
        return Result.success(tagService.update(tag));
    }

    // 删除标签
    @DeleteMapping("/del/{id}")
    public Result<Integer> deleteTag(@PathVariable Integer id) {
        return Result.success(tagService.deleteById(id));
    }
}
