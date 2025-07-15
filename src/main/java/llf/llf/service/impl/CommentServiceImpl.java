package llf.llf.service.impl;

import llf.llf.mapper.CommentMapper;
import llf.llf.pojo.Comment;
import llf.llf.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> selectAll() {
        return commentMapper.selectAll();
    }

    @Override
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    @Override
    public List<Comment> selectByPostId(Integer postId) {
        return commentMapper.selectByPostId(postId);
    }

    @Override
    public List<Comment> selectByUserId(Integer userId) {
        return commentMapper.selectByUserId(userId);
    }

    @Override
    public List<Comment> selectByParentId(Integer parentId) {
        return commentMapper.selectByParentId(parentId);
    }

    @Override
    public int add(Comment comment) {
        return commentMapper.add(comment);
    }

    @Override
    public int update(Comment comment) {
        return commentMapper.update(comment);
    }

    @Override
    public int deleteById(Integer id) {
        return commentMapper.deleteById(id);
    }

    @Override
    public int deleteByPostId(Integer postId) {
        return commentMapper.deleteByPostId(postId);
    }
}
