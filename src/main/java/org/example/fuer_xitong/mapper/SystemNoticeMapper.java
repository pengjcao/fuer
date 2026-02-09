package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.*;
import org.example.fuer_xitong.pojo.dto.SystemNoticePublishDTO;
import org.example.fuer_xitong.pojo.vo.SystemNoticeVO;

import java.util.List;

@Mapper
public interface SystemNoticeMapper {
    /** 插入通知主体（不含附件） */
    @Insert("""
        INSERT INTO system_notice
        (title, content, publisher_id, publisher_role,group_ids)
        VALUES
        (#{title}, #{content}, #{publisherId}, #{publisherRole}, #{groupIds})
    """)
    void insertNoticeMinimal(@Param("title") String title,
                             @Param("content") String content,
                             @Param("publisherId") String publisherId,
                             @Param("publisherRole") Integer publisherRole,
                             @Param("groupIds") String groupIds);


    /** 获取刚插入的自增ID */
    @Select("SELECT LAST_INSERT_ID()")
    Integer getLastInsertId();

    /** 回写附件路径 */
    @Update("""
        UPDATE system_notice
        SET attachment_paths = #{paths}
        WHERE notice_id = #{noticeId}
    """)
    void updateAttachmentPaths(@Param("noticeId") Integer noticeId,
                               @Param("paths") String attachmentPaths);



    List<SystemNoticeVO> selectAll();


    @Select("""
        SELECT
            notice_id        AS noticeId,
            title,
            content,
            attachment_paths AS attachmentPaths,
            publisher_id     AS publisherId,
            publisher_role   AS publisherRole,
            create_time      AS createTime
        FROM system_notice
        WHERE notice_id = #{noticeId}
          AND is_deleted = 0
    """)
    SystemNoticeVO selectById(@Param("noticeId") Integer noticeId);



    List<SystemNoticeVO> selectByUserGroups(@Param("userId") String userId);
}
