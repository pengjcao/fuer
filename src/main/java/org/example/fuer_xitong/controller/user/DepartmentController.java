package org.example.fuer_xitong.controller.user;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.pojo.dto.DepartmentCreateDTO;
import org.example.fuer_xitong.pojo.dto.DeptProfessionalGroupCreateDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.DepartmentVO;
import org.example.fuer_xitong.pojo.vo.DeptProfessionalGroupVO;
import org.example.fuer_xitong.service.DepartmentService;
import org.example.fuer_xitong.utils.Result;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/keshi_zhaunyezu")
@Slf4j
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

//    @Autowired
//    private DepartmentProfessionalGroupService departmentProfessionalGroupService;

    /**
     * 管理员创建科室
     */
    @PostMapping("/creatkeshi")
    public Result create(@RequestBody  DepartmentCreateDTO dto) {
         String id = BaseContext.getCurrentId();
         Integer role=BaseContext.getCurrentRole();

        departmentService.createDepartment(dto, id, role);
        return Result.success("创建科室成功");
    }

    @GetMapping("/listkeshi")
    public Result<List<DepartmentVO>> list() {
        return Result.success(departmentService.listDepartments());
    }


    /**
     * 新建科室专业组
     */
    @PostMapping("/create-professional-group")
    public Result create(@RequestBody DeptProfessionalGroupCreateDTO dto) {

        // 基础参数校验
        if (dto.getDepartmentId() == null) {
            return Result.error("科室ID不能为空");
        }
        if (dto.getKeshi() == null || dto.getKeshi().trim().isEmpty()) {
            return Result.error("科室名称不能为空");
        }
        if (dto.getGroupPath() == null || dto.getGroupPath().trim().isEmpty()) {
            return Result.error("专业组不能为空");
        }

        departmentService.createGroup(dto);
        return Result.success();
    }


    /**
     * 查询科室下的专业组
     */
    @GetMapping("/list-professional-group")
    public Result<List<DeptProfessionalGroupVO>> list(
            @RequestParam Integer departmentId
    ) {

        if (departmentId == null) {
            return Result.error("科室ID不能为空");
        }

        return Result.success(
                departmentService.listGroupByDepartment(departmentId)
        );
    }



}