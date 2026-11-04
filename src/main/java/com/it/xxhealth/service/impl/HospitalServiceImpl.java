package com.it.xxhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.xxhealth.entity.Hospital;
import com.it.xxhealth.service.HospitalService;
import com.it.xxhealth.mapper.HospitalMapper;
import org.springframework.stereotype.Service;

/**
* @author Ljz
* @description 针对表【hospital(医院信息表)】的数据库操作Service实现
* @createDate 2026-11-04 09:47:06
*/
@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital>
    implements HospitalService{

}




