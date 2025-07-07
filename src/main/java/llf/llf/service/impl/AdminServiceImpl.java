package llf.llf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import llf.llf.mapper.AdminMapper;
import llf.llf.pojo.Admin;
import llf.llf.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<Admin> selectAll() {
        return adminMapper.selectAll();
    }

    @Override
    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }


    @Override
    public int add(Admin admin) {
        return adminMapper.add(admin);
    }

    @Override
    public int update(Admin admin) {
        return adminMapper.update(admin);
    }

    @Override
    public int deleteById(Integer id) {
        return adminMapper.deleteById(id);
    }
}
