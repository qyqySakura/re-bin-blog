package llf.llf.service.impl;

import llf.llf.mapper.TagMapper;
import llf.llf.pojo.Tag;
import llf.llf.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> selectAll() {
        return tagMapper.selectAll();
    }

    @Override
    public Tag selectById(Integer id) {
        return tagMapper.selectById(id);
    }

    @Override
    public Tag selectByName(String name) {
        return tagMapper.selectByName(name);
    }

    @Override
    public int add(Tag tag) {
        return tagMapper.add(tag);
    }

    @Override
    public int update(Tag tag) {
        return tagMapper.update(tag);
    }

    @Override
    public int deleteById(Integer id) {
        return tagMapper.deleteById(id);
    }

    @Override
    public List<Tag> selectByPostId(Integer postId) {
        return tagMapper.selectByPostId(postId);
    }

    @Override
    @Transactional
    public void setPostTags(Integer postId, List<Integer> tagIds) {
        // 先删除文章的所有标签
        tagMapper.deletePostTags(postId);

        // 再添加新的标签
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Integer tagId : tagIds) {
                tagMapper.addPostTag(postId, tagId);
            }
        }
    }

    @Override
    public List<Tag> selectAllWithPostCount() {
        return tagMapper.selectAllWithPostCount();
    }
}
