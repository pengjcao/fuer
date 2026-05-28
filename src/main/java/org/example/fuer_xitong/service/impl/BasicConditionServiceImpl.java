package org.example.fuer_xitong.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.mapper.BasicConditionMapper;
import org.example.fuer_xitong.mapper.UserMapper;
import org.example.fuer_xitong.pojo.dto.BasicConditionDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.service.BasicConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicConditionServiceImpl implements BasicConditionService {

    @Autowired
    private  BasicConditionMapper basicConditionMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void reportBasicCondition(BasicConditionDTO dto) {
        log.info("基础条件填报入参: {}", dto);

        String createBy = BaseContext.getCurrentId();
        dto.setCreateBy(createBy);

        basicConditionMapper.insertBasicCondition(dto);
    }


    @Override
    public List<BasicConditionDTO> getBasicConditionDetail(String keshi) {
        // 1. 获取当前登录用户
        String currentUserId = BaseContext.getCurrentId();

        // 2. 查询当前用户所属科室
        String currentKeshi = userMapper.selectKeshiByJobNumber(currentUserId);

        // 3. 权限判断
        if (!keshi.equals(currentKeshi) && !"科研处".equals(currentKeshi)) {
            throw new RuntimeException("无权限查看该科室数据");
        }

        // 4. 查询该科室数据
        return basicConditionMapper.selectByKeshi(keshi);
    }
}