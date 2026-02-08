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

        if (!response.ok) {
            throw new Error("Something went wrong");
        }

        let data = await response.json();
        if (!data || data.result < 1) {
            console.log("fetchWritePlan FAILED.. ", data);
            alert("일정 등록에 문제가 발생했습니다.");
        } else {
            console.log("fetchWritePlan SUCCESS!! ", data);
            alert("일정이 정상적으로 등록되었습니다.");
            removeCalendarRow();
            addCalendarRow();
            fetchCurrentMonthPlans();
        }

    } catch (error) {
        console.log("fetchWritePlan ERORR!! ", error);
        alert("일정 등록에 문제가 발생했습니다.");

    } finally {
        console.log("fetchWritePlan finally completed. CLOSE MODAL.")
        hideWritePlanView();
    }
}

// 해당 월 일정 목록 조회
async function fetchCurrentMonthPlans() {

    let reqData = {
        year: current_year,
        month: current_month + 1
    }

    let queryString = new URLSearchParams(reqData).toString();

    try {
        let response = await fetch(`planner/plans?${queryString}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json", "charset": "utf-8"
            }
        })

        if (!response.ok) {
            throw new Error("Something went wrong");
        }

        let data = await response.json();
        let plans = data.plans;

        plans.forEach(planDto => {
            let appendTag = `<li><a class="title" href="#none" data-no="${planDto.no}">${planDto.title}</a></li>`;
            let targetElement = document.querySelector(`#date_${planDto.date} ul.plan`);
            if (targetElement) {
                targetElement.insertAdjacentHTML('beforeend', appendTag);
            }
        });

    } catch (error) {
        console.log("fetchCurrentMonthPlans COMM ERROR!!", error);
    }



}