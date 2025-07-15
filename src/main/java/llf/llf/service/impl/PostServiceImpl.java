package llf.llf.service.impl;

import llf.llf.mapper.PostMapper;
import llf.llf.pojo.Post;
import llf.llf.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Override
    public List<Post> selectAll() {
        return postMapper.selectAll();
    }

    @Override
    public Post selectById(Integer id) {
        return postMapper.selectById(id);
    }

    @Override
    public List<Post> selectByUserId(Integer userId) {
        return postMapper.selectByUserId(userId);
    }

    @Override
    public List<Post> selectByCategoryId(Integer categoryId) {
        return postMapper.selectByCategoryId(categoryId);
    }

    @Override
    public List<Post> selectByStatus(Integer status) {
        return postMapper.selectByStatus(status);
    }

    @Override
    public int add(Post post) {
        return postMapper.add(post);
    }

    @Override
    public int update(Post post) {
        return postMapper.update(post);
    }

    @Override
    public int deleteById(Integer id) {
        return postMapper.deleteById(id);
    }

    @Override
    public List<Post> selectPublishedPosts(Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        return postMapper.selectPublishedPosts(offset, size);
    }

    @Override
    public int countPublishedPosts() {
        return postMapper.countPublishedPosts();
    }
}
