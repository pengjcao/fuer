package org.example.fuer_xitong.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.fuer_xitong.pojo.dto.InstitutionFileDTO;
import org.example.fuer_xitong.pojo.vo.InstitutionFileVO;
import org.example.fuer_xitong.pojo.vo.InstitutionTrialManagementFileVO;
import org.example.fuer_xitong.service.InstitutionFileService;
import org.example.fuer_xitong.service.InstitutionTeamMemberService;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user/institution")
@RequiredArgsConstructor
public class InstitutionFileController {



    @Autowired
    private InstitutionFileService institutionFileService;












    @PostMapping(
            value = "/filesaveall",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Result saveall(@ModelAttribute InstitutionFileDTO dto) {
        institutionFileService.saveOrUpdate(dto);
        return Result.success();
    }

    /**
     * 查询机构制度文件记录（表中只有一条）
     */
    @GetMapping("/fileselect")
    public Result<InstitutionFileVO> getInstitutionFile() {
        InstitutionFileVO vo = institutionFileService.getInstitutionFile();
        return Result.success(vo);
    }

    /**
     * 上传【临床试验管理制度】文件
     */
    @PostMapping(
            value = "/trialManagement/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Result uploadTrialManagement(
            @RequestParam("file") MultipartFile file
    ) {
        institutionFileService.uploadTrialManagementFile(file);
        return Result.success("上传成功");
    }

    /**
     * 查询【临床试验管理制度】历史记录列表
     */
    @GetMapping("/trialManagement/history")
    public Result<List<InstitutionTrialManagementFileVO>> listTrialManagementHistory(

    ) {
        int institutionFileId=1;
        List<InstitutionTrialManagementFileVO> list =
                institutionFileService.listTrialManagementHistory(institutionFileId);
        return Result.success(list);
    }

    
    /**
     * 上传【药物临床试验标准操作规程（SOP）】
     */
    @PostMapping(
            value = "/drugTrialSop/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Result uploadDrugTrialSop(
            @RequestParam("file") MultipartFile file
    ) {
        institutionFileService.uploadDrugTrialSopFile(file);
        return Result.success("上传成功");
    }





//    @GetMapping("/drugTrialSop/history")
//    public Result<List<InstitutionTrialManagementFileVO>> listTrialManagementHistory(
//
//    ) {
//        int institutionFileId=1;
//        List<InstitutionTrialManagementFileVO> list =
//                institutionFileService.listTrialManagementHistory(institutionFileId);
//        return Result.success(list);
//    }





}


