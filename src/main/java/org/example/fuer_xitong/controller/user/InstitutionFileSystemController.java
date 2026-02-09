package org.example.fuer_xitong.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.fuer_xitong.mapper.InstitutionSystemFileHistoryMapper;
import org.example.fuer_xitong.pojo.dto.InstitutionFileSystemCreateDTO;
import org.example.fuer_xitong.pojo.vo.InstitutionFileSystemVO;
import org.example.fuer_xitong.pojo.vo.InstitutionSystemFileHistoryVO;
import org.example.fuer_xitong.pojo.vo.InstitutionSystemFileVO;
import org.example.fuer_xitong.service.InstitutionFileSystemService;
import org.example.fuer_xitong.service.InstitutionSystemFileService;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user/institution-file-system")
@RequiredArgsConstructor
public class InstitutionFileSystemController {
    @Autowired
    private InstitutionFileSystemService institutionFileSystemService;

    @Autowired
    private InstitutionSystemFileService institutionSystemFileService;

    /**
     * 新建文件体系
     */
    @PostMapping("/create")
    public Result create(@RequestBody InstitutionFileSystemCreateDTO dto,@RequestParam(required = false) String keshi,@RequestParam(required = false, name = "groupPath") String groupPath) {

        institutionFileSystemService.create(dto,keshi,groupPath);
        return Result.success("成功创建文件体系");
    }

    /**
     * 文件体系列表
     */
    @GetMapping("/list")
    public Result list( @RequestParam(required = false) String keshi,@RequestParam(required = false, name = "groupPath") String groupPath) {
        List<InstitutionFileSystemVO> list =
                institutionFileSystemService.list(keshi,groupPath);
        return Result.success(list);
    }


    @PostMapping("/uploadfile")
    public Result uploadFiles(
            @RequestParam("systemId") Long systemId,
            @RequestParam("files") MultipartFile[] files,@RequestParam(required = false) String keshi,
            @RequestParam(required = false, name = "groupPath") String groupPath) {

        institutionSystemFileService.uploadFiles(systemId, files ,keshi,groupPath);
        return Result.success();
    }

    /**
     * 查询某个文件体系下的文件列表
     */
    @GetMapping("/query-by-system")
    public Result<List<InstitutionSystemFileVO>> queryBySystem(
            @RequestParam Long systemId,
            @RequestParam(required = false) String keshi,
            @RequestParam(required = false, name = "groupPath") String groupPath) {

        return Result.success(
                institutionSystemFileService.queryBySystemId(systemId,keshi,groupPath)
        );
    }

    /**
     * 覆盖上传文件
     */
    @PostMapping("/overwrite")
    public Result<String> overwrite(
            @RequestParam Long fileId,
            @RequestParam MultipartFile file,
            @RequestParam(required = false) String remark) {
        institutionSystemFileService.overwriteFile(fileId, file, remark);
        return Result.success("文件更改成功");
    }


    /**
     * 查询某个文件的历史记录
     */
    @GetMapping("/file-history")
    public Result<List<InstitutionSystemFileHistoryVO>> history(
            @RequestParam Long fileId) {

        List<InstitutionSystemFileHistoryVO> list =
                institutionSystemFileService.queryFileHistory(fileId);

        return Result.success(list);
    }

    /*删除文件体系*/
    @DeleteMapping("/system/delete")
    public Result delete(@RequestParam Long systemId) {
        institutionFileSystemService.deleteSystem(systemId);
        return Result.success("文件体系删除成功");
    }

    /**
     * 删除单个文件
     */
    @DeleteMapping("/file/delete")
    public Result deleteFile(@RequestParam Long fileId) {
        institutionSystemFileService.deleteFile(fileId);
        return Result.success("单个文件删除成功");
    }

    /**
     * 使文件失效
     */
    @PostMapping("/file/invalidate")
    public Result invalidateFile(@RequestParam Long fileId) {
        institutionSystemFileService.invalidateFile(fileId);
        return Result.success("文件已失效");
    }

}
