package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.BasicConditionDTO;

import java.util.List;

public interface BasicConditionService {

    /**
     * 基础条件填报
     *
     * @param dto 基础条件参数
     */
    void reportBasicCondition(BasicConditionDTO dto);
    /**
     * 查询当前登录人基础条件详情
     *
     * @return 基础条件信息
     */
    List<BasicConditionDTO> getBasicConditionDetail(String keshi);
}