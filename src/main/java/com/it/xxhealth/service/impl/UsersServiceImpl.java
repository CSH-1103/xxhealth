package com.it.xxhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.xxhealth.entity.Users;
import com.it.xxhealth.service.UsersService;
import com.it.xxhealth.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author Ljz
* @description 针对表【users(用户表)】的数据库操作Service实现
* @createDate 2026-11-04 09:47:06
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

    @Override
    public Users getUsersByUserIdByPass(Users users) {
        return null;
    }

    @Override
    public Users getUsersById(Users users) {
        return null;
    }

    @Override
    public int saveUsers(Users users) {
        return 0;
    }
}




