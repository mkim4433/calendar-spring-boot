// 일정 등록
async function fetchWritePlan(year, month, date, title, body, file) {

    let reqFormData = new FormData();
    reqFormData.append("year", year);
    reqFormData.append("month", month);
    reqFormData.append("date", date);
    reqFormData.append("title", title);
    reqFormData.append("body", body);
    reqFormData.append("file", file);

    try {
        let response = await fetch("/planner/plan", {
            method: "POST",
            body: reqFormData
        });

        let data = await response.json();
        if (!data || data.result < 1) {
            console.log("fetchWritePlan FAILED.. ", data);
            alert("일정 등록에 문제가 발생했습니다.");
        } else {
            console.log("fetchWritePlan SUCCESS!! ", data);
            alert("일정이 정상적으로 등록되었습니다.");
            removeCalendarRow();
            addCalendarRow();
        }

    } catch (error) {
        console.log("fetchWritePlan ERORR!! ", error);
        alert("일정 등록에 문제가 발생했습니다.");

    } finally {
        console.log("fetchWritePlan finally completed. CLOSE MODAL.")
        hideWritePlanView();
    }
}