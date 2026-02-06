package com.tourlesjours.calendar.planner;

import com.tourlesjours.calendar.planner.jpa.PlannerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannerDto {

    private int no;
    private String owner_id;

    /*
    사용자가 다른 사용자에게 일정을 공유할 경우 사용할 수 있는 필드.
     (ex. gildong -> gilsoon 에게 일정을 공유한 경우)
     gildong 기준 no = 1, owner_id = gildong
     gilsoon 기준 no = 2, owner_id = gilsoon 이지만
     gildong, gilsoon 모두 ori_ 데이터는
     ori_no = 1, ori_owner_id = gildong 이 됨.
    */
    private int ori_no;
    private String ori_owner_id;

    private int year;
    private int month;
    private int date;
    private String title;
    private String body;
    private String img_name;
    private String reg_date;
    private String mod_date;

    public PlannerEntity toEntity() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return PlannerEntity.builder()
                .planNo(no)
                .planOwnerId(owner_id)
                .planOriNo(ori_no)
                .planOriOwnerId(ori_owner_id)
                .planYear(year)
                .planMonth(month)
                .planDate(date)
                .planTitle(title)
                .planBody(body)
                .planImgName(img_name)
                .planRegDate(reg_date != null ? LocalDateTime.parse(reg_date, formatter) : null)
                .planModDate(mod_date != null ? LocalDateTime.parse(mod_date, formatter) : null)
                .build();
    }
}
