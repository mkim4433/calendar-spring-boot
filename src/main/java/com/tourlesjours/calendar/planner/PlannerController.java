package com.tourlesjours.calendar.planner;

import com.tourlesjours.calendar.planner.util.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
}
