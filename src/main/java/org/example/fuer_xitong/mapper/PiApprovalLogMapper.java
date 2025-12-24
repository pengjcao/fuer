package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.fuer_xitong.pojo.vo.PiApprovalLogVO;

import java.util.List;

public interface PiApprovalLogMapper {

    /**
     * 查询某个研究者某条 PI 的审批日志
     */
    List<PiApprovalLogVO> selectLogsByPiId(@Param("piId") String piId);
}
