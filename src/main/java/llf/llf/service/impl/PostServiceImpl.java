package llf.llf.service.impl;

import llf.llf.mapper.PostMapper;
import llf.llf.pojo.Post;
import llf.llf.service.PostService;
import llf.llf.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostLikeService postLikeService;

    @Override
    public List<Post> selectAll() {
        return postMapper.selectAll();
    }

    @Override
    public Post selectById(Integer id) {
        return postMapper.selectById(id);
    }

    @Override
    public Post selectByIdWithLikeStatusAndView(Integer id, Integer currentUserId) {
        // 增加阅读量
        postMapper.incrementViewCount(id);

        // 获取文章信息
        Post post = postMapper.selectById(id);

        if (post != null && currentUserId != null) {
            // 设置点赞状态
            post.setIsLiked(postLikeService.checkUserLike(id, currentUserId));
        } else if (post != null) {
            post.setIsLiked(false);
        }

        return post;
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

    @Override
    public List<Post> selectByTagId(Integer tagId) {
        return postMapper.selectByTagId(tagId);
    }

    @Override
    public List<Post> selectByTagIdWithPaging(Integer tagId, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        return postMapper.selectByTagIdWithPaging(tagId, offset, size);
    }

    @Override
    public int countPostsByTagId(Integer tagId) {
        return postMapper.countPostsByTagId(tagId);
    }

    @Override
    public List<Post> searchPosts(String keyword) {
        return postMapper.searchPosts(keyword);
    }

    @Override
    public List<Post> selectByCategoryIdWithPaging(Integer categoryId, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        return postMapper.selectByCategoryIdWithPaging(categoryId, offset, size);
    }

    @Override
    public int countPostsByCategoryId(Integer categoryId) {
        return postMapper.countPostsByCategoryId(categoryId);
    }

    @Override
    public List<Post> selectPublishedPostsWithLikeStatus(Integer page, Integer size, Integer currentUserId) {
        Integer offset = (page - 1) * size;
        List<Post> posts = postMapper.selectPublishedPosts(offset, size);

        if (currentUserId != null && !posts.isEmpty()) {
            // 获取所有文章ID
            List<Integer> postIds = posts.stream()
                    .map(Post::getId)
                    .collect(Collectors.toList());

            // 获取用户已点赞的文章ID列表
            List<Integer> likedPostIds = postLikeService.getUserLikedPostIds(currentUserId, postIds);

            // 设置点赞状态
            posts.forEach(post -> {
                post.setIsLiked(likedPostIds.contains(post.getId()));
            });
        } else {
            // 未登录用户，所有文章都设为未点赞
            posts.forEach(post -> post.setIsLiked(false));
        }

        return posts;
    }
}
