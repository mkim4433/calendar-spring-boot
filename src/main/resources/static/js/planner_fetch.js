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
            alert("일정 등록에 문제가 발생했습니다.");
        } else {
            alert("일정이 정상적으로 등록되었습니다.");
            removeCalendarRow();
            addCalendarRow();
            fetchCurrentMonthPlans();
        }

    } catch (error) {
        console.log("fetchWritePlan ERORR!! ", error);
        alert("일정 등록에 문제가 발생했습니다.");

    } finally {
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

// 일정 상세 조회
async function fetchPlanDetail(no) {

    let queryString = new URLSearchParams({"no": no}).toString();

    try {

        let response = await fetch(`planner/plan?${queryString}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json", "charset": "utf-8"
            }
        });

        if (!response.ok) {
            throw new Error("Something went wrong");
        }

        let data = await response.json();

        showPlanDetailView(data.plan);

    } catch (error) {
        console.log("fetchPlanDetail COMM ERROR!!", error);
    }
}

// 일정 상세 수정
async function fetchModifyPlan(no, year, month, date, title, body, file) {

    const reqData = new FormData();
    reqData.append("no", no);
    reqData.append("year", year);
    reqData.append("month", month);
    reqData.append("date", date);
    reqData.append("title", title);
    reqData.append("body", body);

    if (file !== undefined) {
        reqData.append("file", file);
    }

    try {
        const response = await fetch(`/planner/plan/${no}`, {
            method: "PUT",
            body: reqData,
        });

        if (!response.ok) {
            throw new Error("Something went wrong");
        }

        const data = await response.json();
        if (!data || data.result < 1) {
            alert("일정 수정에 문제가 발생했습니다.");
        } else {
            alert("일정이 정상적으로 수정되었습니다.");
            removeCalendarRow();
            addCalendarRow();
            fetchCurrentMonthPlans();
        }

    } catch (error) {
        console.log("fetchModifyPlan COMM ERROR!!", error);
    } finally {
        hidePlanDetailView();
    }
}

// 일정 삭제
async function fetchDeletePlan(no) {

    try {
        const response = await fetch(`/planner/plan/${no}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json", "charset": "utf-8"
            }
        })

        if (!response.ok) {
            throw new Error("Network response was not ok.");
        }

        let data = await response.json();
        if (!data || data.result < 1) {
            alert("일정 삭제에 문제가 발생했습니다. 다시 시도해 주세요.");
        } else {
            alert("일정이 정상적으로 삭제되었습니다.");
            removeCalendarRow();
            addCalendarRow();
            fetchCurrentMonthPlans();
        }

    } catch (error) {
        console.log("fetchDeletePlan COMM ERROR!!", error);
        alert("일정 삭제 중 오류가 발생했습니다.");
    } finally {
        hidePlanDetailView();
    }
}
