package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.vo.InstitutionSystemFileHistoryVO;
import org.example.fuer_xitong.pojo.vo.InstitutionSystemFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InstitutionSystemFileService {
    void uploadFiles(Long systemId, MultipartFile[] files,String keshi,String Grouppath);

    /**
     * 根据文件体系ID查询文件
     */
    List<InstitutionSystemFileVO> queryBySystemId(Long systemId,String keshi,String Grouppath);


    /**
     * 覆盖上传文件（当前文件 → 历史）
     */
    void overwriteFile(Long fileId, MultipartFile file, String remark);


    /**
     * 查询文件历史记录
     */
    List<InstitutionSystemFileHistoryVO> queryFileHistory(Long fileId);

    /**
     * 删除单个文件
     */
    void deleteFile(Long fileId);


    /**
     * 让某个文件失效
     */
    void invalidateFile(Long fileId);
}

