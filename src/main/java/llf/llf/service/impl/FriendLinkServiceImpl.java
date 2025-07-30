package llf.llf.service.impl;

import llf.llf.mapper.FriendLinkMapper;
import llf.llf.pojo.FriendLink;
import llf.llf.service.FriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendLinkServiceImpl implements FriendLinkService {

    @Autowired
    private FriendLinkMapper friendLinkMapper;

    @Override
    public List<FriendLink> selectAll() {
        return friendLinkMapper.selectAll();
    }

    @Override
    public FriendLink selectById(Integer id) {
        return friendLinkMapper.selectById(id);
    }

    @Override
    public int add(FriendLink friendLink) {
        return friendLinkMapper.add(friendLink);
    }

    @Override
    public int update(FriendLink friendLink) {
        return friendLinkMapper.update(friendLink);
    }

    @Override
    public int deleteById(Integer id) {
        return friendLinkMapper.deleteById(id);
    }
}
