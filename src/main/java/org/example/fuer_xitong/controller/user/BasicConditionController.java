package org.example.fuer_xitong.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.pojo.dto.BasicConditionDTO;
import org.example.fuer_xitong.service.BasicConditionService;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/basicCondition")
@Slf4j
public class BasicConditionController {
    @Autowired
    private  BasicConditionService basicConditionService;

    @PostMapping(value = "/report", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result reportBasicConditionJson(@RequestBody BasicConditionDTO dto) {
        return reportBasicCondition(dto);
    }

    @PostMapping(value = "/report", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result reportBasicConditionForm(@ModelAttribute BasicConditionDTO dto) {
        return reportBasicCondition(dto);
    }

    private Result reportBasicCondition(BasicConditionDTO dto) {
        basicConditionService.reportBasicCondition(dto);
        return Result.success("基础条件填报成功");
    }


    /**
     * 查询当前登录人基础条件详情
     */
    @GetMapping("/detail")
    public Result<List<BasicConditionDTO>> getBasicConditionDetail(
            @RequestParam String keshi) {

        List<BasicConditionDTO> list = basicConditionService.getBasicConditionDetail(keshi);
        return Result.success(list);
    }

    @DeleteMapping("/delete")
    public Result deleteBasicCondition(@RequestParam Long id) {
        basicConditionService.deleteBasicCondition(id);
        return Result.success("基础条件记录删除成功");
    }
}
