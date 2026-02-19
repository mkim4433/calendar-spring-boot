package com.tourlesjours.calendar.planner;

import com.tourlesjours.calendar.planner.util.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/planner")
public class PlannerController {

    private final PlannerService plannerService;
    private final UploadFileService uploadFileService;

    public PlannerController(PlannerService plannerService,
                             UploadFileService uploadFileService) {

        this.plannerService = plannerService;
        this.uploadFileService = uploadFileService;
    }

    @GetMapping({"", "/"})
    public String home() {
        log.info("Planner home()");

        return "planner/home";
    }

    // 일정 등록
    @PostMapping("/plan")
    public ResponseEntity<Map<String, Object>> writePlan(PlannerDto plannerDto,
                                                         @RequestParam("file") MultipartFile file,
                                                         Principal principal) {

        String loggedInId = principal.getName();

        String savedFileName = uploadFileService.upload(loggedInId, file);

        if (savedFileName != null) {
            plannerDto.setImg_name(savedFileName);
            plannerDto.setOwner_id(loggedInId);

            Map<String, Object> resultMap = plannerService.writePlan(plannerDto);

            return ResponseEntity.ok(resultMap);

        } else {

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("message", "File upload failed.");
            return ResponseEntity.badRequest().body(errorMap);
        }
    }

    // 해당 월 일정 목록 조회
    @GetMapping("/plans")
    public ResponseEntity<Map<String, Object>> getPlans(Principal principal,
                                                        @RequestParam Map<String, Object> params) {

        String loggedInId = principal.getName();
        params.put("owner_id", loggedInId);

        Map<String, Object> resultMap = plannerService.getPlans(params);

        return ResponseEntity.ok(resultMap);
    }

    // 일정 상세 조회
    @GetMapping("/plan")
    public ResponseEntity<Map<String, Object>> getPlanDetail(@RequestParam Map<String, String> params) {

        Map<String, Object> resultMap = plannerService.getPlanDetail(params);

        return ResponseEntity.ok(resultMap);
    }

    // 일정 상세 수정
    @PutMapping("/plan/{no}")
    public ResponseEntity<Map<String, Object>> modifyPlan(@PathVariable("no") int no,
                                                          PlannerDto plannerDto,
                                                          @RequestParam(value = "file", required = false) MultipartFile file,
                                                          Principal principal) {
        plannerDto.setNo(no);
        String loggedInId = principal.getName();

        // 이미지 수정 있을 시
        if (file != null) {
            String savedFileName = uploadFileService.upload(loggedInId, file);
            if (savedFileName != null) {
                plannerDto.setImg_name(savedFileName);
                plannerDto.setOwner_id(loggedInId);
                return ResponseEntity.ok(plannerService.modifyPlan(plannerDto));
            } else {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("message", "File upload failed.");
                return ResponseEntity.badRequest().body(errorMap);
            }
            // 이미지 수정 없을 시
        } else {
            return ResponseEntity.ok(plannerService.modifyPlan(plannerDto));
        }
    }

    // 일정 삭제
    @DeleteMapping("/plan/{no}")
    public ResponseEntity<Map<String, Object>> deletePlan(@PathVariable("no") int no) {

        Map<String, Object> result = plannerService.deletePlan(no);

        return ResponseEntity.ok(result);
    }
}
