package com.tourlesjours.calendar.planner;

import com.tourlesjours.calendar.member.jpa.MemberRepository;
import com.tourlesjours.calendar.planner.jpa.PlannerEntity;
import com.tourlesjours.calendar.planner.jpa.PlannerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlannerService {

    private static final int PLAN_MODIFY_FAIL = 0;
    private static final int PLAN_MODIFY_SUCCESS = 1;

    private final PlannerRepository plannerRepository;
    private final MemberRepository memberRepository;

    public PlannerService(PlannerRepository plannerRepository,
                          MemberRepository memberRepository) {
        this.plannerRepository = plannerRepository;
        this.memberRepository = memberRepository;
    }

    // 일정 등록
    @Transactional
    public Map<String, Object> writePlan(PlannerDto plannerDto) {

        Map<String, Object> resultMap = new HashMap<>();

        int result = 0;
        plannerDto.setOri_owner_id(plannerDto.getOwner_id());
        PlannerEntity savedPlannerEntity = plannerRepository.save(plannerDto.toEntity());
        if (savedPlannerEntity != null) {
            savedPlannerEntity.setPlanOriNo(savedPlannerEntity.getPlanNo());
            plannerRepository.save(savedPlannerEntity);

            result = 1;
        } else {
            result = 0;
        }

        resultMap.put("result", result);
        return resultMap;
    }

    // 일정 목록 조회
    public Map<String, Object> getPlans(Map<String, Object> params) {

        Map<String, Object> resultMap = new HashMap<>();

        List<PlannerEntity> entities = plannerRepository.findByPlanYearAndPlanMonthAndPlanOwnerId(
                Integer.parseInt(String.valueOf(params.get("year"))),
                Integer.parseInt(String.valueOf(params.get("month"))),
                String.valueOf(params.get("owner_id"))
        );

        List<PlannerDto> plans = entities.stream()
                .map(PlannerEntity::toDto)
                .collect(Collectors.toList());

        resultMap.put("plans", plans);

        return resultMap;
    }

    // 일정 상세 조회
    public Map<String, Object> getPlanDetail(Map<String, String> params) {

        Map<String, Object> resultMap = new HashMap<>();

        PlannerEntity entity = plannerRepository.findByPlanNo(Integer.parseInt(params.get("no")));
        PlannerDto planDto = entity.toDto();

        resultMap.put("plan", planDto);

        return resultMap;
    }

    // 일정 상세 수정
    @Transactional
    public Map<String, Object> modifyPlan(PlannerDto plannerDto) {

        Map<String, Object> resultMap = new HashMap<>();
        int result = PLAN_MODIFY_FAIL;

        PlannerEntity entity = plannerRepository.findByPlanNo(plannerDto.getNo());

        if (entity != null) {
            entity.setPlanYear(plannerDto.getYear());
            entity.setPlanMonth(plannerDto.getMonth());
            entity.setPlanDate(plannerDto.getDate());
            entity.setPlanTitle(plannerDto.getTitle());
            entity.setPlanBody(plannerDto.getBody());

            // 파일 있으면 파일 업데이트
            if (plannerDto.getImg_name() != null) {
                entity.setPlanImgName(plannerDto.getImg_name());
            }

            result = PLAN_MODIFY_SUCCESS;
        }

        resultMap.put("result", result);
        return resultMap;
    }
}
