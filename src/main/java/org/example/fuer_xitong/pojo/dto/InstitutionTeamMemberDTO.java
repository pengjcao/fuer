package org.example.fuer_xitong.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class InstitutionTeamMemberDTO {

    /** 机构成员工号（唯一标识） */

    private String institutionMemberId;

    /** 机构ID */

    private String institutionId;

    /** 姓名 */

    private String name;

    /** 任职（多选） */

    private List<String> positions;

    /** 简历文件 */
    private MultipartFile resumeFile;

    /** GCP证书文件 */
    private MultipartFile gcpFile;

    /** 执业证书文件（可选） */
    private MultipartFile licenseFile;
}
