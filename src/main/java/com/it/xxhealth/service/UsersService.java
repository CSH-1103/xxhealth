package com.it.xxhealth.service;

import com.it.xxhealth.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Ljz
* @description 针对表【users(用户表)】的数据库操作Service
* @createDate 2026-11-04 09:47:06
*/
public interface UsersService extends IService<Users> {

    /**
     * 根据手机号码和密码进行登录验证证
     * @param users
     * @return
     */
    Users getUsersByUserIdByPass(Users users);

    /**
     * 根据手机号码进行是否已注册验证
     * @param users
     * @return
     */
    Users getUsersById(Users users);

    /**
     * 根据用户输入信息进行注册
     * @param users
     * @return
     */
    int saveUsers(Users users);

}
