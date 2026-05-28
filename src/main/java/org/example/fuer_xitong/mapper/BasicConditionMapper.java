package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.fuer_xitong.pojo.dto.BasicConditionDTO;

import java.util.List;

@Mapper
public interface BasicConditionMapper {

    /**
     * 新增基础条件
     *
     * @param dto 基础条件参数
     */
    void insertBasicCondition(@Param("dto")BasicConditionDTO dto);

    /**
     * 根据创建人工号查询基础条件
     */
    BasicConditionDTO selectBasicConditionByCreateBy(@Param("createBy") String createBy);

    List<BasicConditionDTO> selectByKeshi(String keshi);

}