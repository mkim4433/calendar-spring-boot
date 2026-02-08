package com.tourlesjours.calendar.planner;

import com.tourlesjours.calendar.member.jpa.MemberRepository;
import com.tourlesjours.calendar.planner.jpa.PlannerEntity;
import com.tourlesjours.calendar.planner.jpa.PlannerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PlannerService {

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
        if(savedPlannerEntity != null) {
            savedPlannerEntity.setPlanOriNo(savedPlannerEntity.getPlanNo());
            plannerRepository.save(savedPlannerEntity);

            result = 1;
        } else {
            result = 0;
        }

        resultMap.put("result", result);
        return resultMap;
    }
}
