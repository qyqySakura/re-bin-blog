package llf.llf.service;

import llf.llf.pojo.FriendLink;
import java.util.List;

public interface FriendLinkService {
    
    List<FriendLink> selectAll();
    
    FriendLink selectById(Integer id);
    
    int add(FriendLink friendLink);
    
    int update(FriendLink friendLink);
    
    int deleteById(Integer id);
}
