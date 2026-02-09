package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.*;
import org.example.fuer_xitong.pojo.dto.NoticeGroupCreateDTO;

import java.util.List;


@Mapper
public interface NoticeGroupMapper {

    /**
     * 新建分组
     */
    // 插入分组

    @Insert("""
        INSERT INTO notice_group (group_name, creator_id)
        VALUES (#{groupName}, #{creatorId})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "groupId")
    int insertGroup(NoticeGroupCreateDTO dto);



    // 插入分组用户
    @Insert("""
        INSERT INTO notice_group_user (group_id, user_id)
        VALUES (#{groupId}, #{userId})
    """)
    int insertGroupUser(@Param("groupId") Integer groupId,
                        @Param("userId") String userId);



    /*查询所有分组*/
    @Select("SELECT id AS groupId, group_name AS groupName, creator_id AS creatorId FROM notice_group")
    List<NoticeGroupCreateDTO> selectAllGroups();




    /**
     * 删除分组下的所有用户
     */
    @Delete("DELETE FROM notice_group_user WHERE group_id = #{groupId}")
    int deleteUsersByGroupId(Integer groupId);

    /**
     * 删除分组本身
     */
    @Delete("DELETE FROM notice_group WHERE id = #{groupId}")
    int deleteById(Integer groupId);

    /*查询该分组下所有的用户*/
    @Select("SELECT user_id FROM notice_group_user WHERE group_id = #{groupId}")
    List<String> selectUserIdsByGroupId(Integer groupId);



}
