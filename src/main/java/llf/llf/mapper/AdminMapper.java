package llf.llf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import llf.llf.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author sakura
* @description 针对表【admin】的数据库操作Mapper
* @createDate 2025-07-07 15:59:09
* @Entity llf.domain.Admin
*/

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    List<Admin> selectAll();

    Admin selectById();

    int add(Admin  admin);

    int update(Admin  admin);

    int deleteById();
}




