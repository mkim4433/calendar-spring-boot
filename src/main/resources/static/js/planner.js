// 플래너 진입 시 오늘에 해당하는 달력 표출.
// 사용자가 년, 월을 선택하면 해당하는 년, 월의 달력을 표출.

let selected_year;
let selected_month;
let selected_date;
let selected_day;

function setSelectedCalendar(year, month, date, day) {
    selected_year = year;
    selected_month = month;
    selected_date = date;
    selected_day = day;
}

// 플래너 진입 시 오늘에 해당하는 달력 표출.
document.addEventListener("DOMContentLoaded", function () {
    console.log("Document Loaded!!");

    // 오늘 날짜
    let today = new Date();
    let today_year = today.getFullYear();
    let today_month = today.getMonth();     // 오늘 월 (0~11)
    let today_date = today.getDate();
    let today_day = today.getDay();         // 오늘 요일 (0~6, 0:일요일)

    setSelectedCalendar(today_year, today_month, today_date, today_day);

    setYearAndMonthSelectUI();

    addCalendarRow();

});

function setYearAndMonthSelectUI() {
    document.querySelector("#section_wrap select[name='p_year']").value = selected_year;
    document.querySelector("#section_wrap select[name='p_month']").value = selected_month + 1;
}

function addCalendarRow() {

    // 선택 월의 첫날을 구한 다음, date, day 를 구함.
    let first = new Date(selected_year, selected_month, 1);
    let firstDate = first.getDate();
    let firstDay = first.getDay();

    // 선택 월의 마지막날을 구한 다음, date 를 구함.
    let last = new Date(selected_year, selected_month + 1, 0);
    let lastDate = last.getDate();

    // 1주일 7일(1줄) * 최대 6줄 = 42
    // 42개짜리 배열을 만들고 요일과 일이 맞게 날짜를 넣고, 그외에는 0을 집어넣음.
    let dates = [];
    let dateCount = 1;
    for (let i = 0; i < 42; i++) {
        if (i < firstDay || i >= lastDate) {
            dates[i] = 0;
        } else {
            dates[i] = dateCount;
            dateCount++;
        }
    }

    // 달력 UI 생성
    let tableBody = document.querySelector("#table_calendar tbody");
    tableBody.innerHTML = "";   // 기존 내용 초기화

    let idx = 0;
    for (let i = 0; i < 6; i++) {
        if (i > 3 && dates[idx] === 0) return;

        let row = document.createElement("tr");

        for (let j = 0; j < 7; j++) {
            let cell = document.createElement("td");

            if (dates[idx] !== 0) {
                // 일자 cell div
                let dateDiv = document.createElement("div");
                dateDiv.className = "date";
                dateDiv.textContent = dates[idx];
                cell.appendChild(dateDiv);

                // 일정 등록 버튼 UI
                let writeDiv = document.createElement("div");
                let writeLink = document.createElement("a");
                writeLink.className = "write";
                writeLink.textContent = "write";
                writeLink.href = "#none";
                writeDiv.appendChild(writeLink);
                cell.appendChild(writeDiv);

                // 일정 목록 UI
                let planWrap = document.createElement("div");
                planWrap.className = "plan_wrap";
                planWrap.id = `date_${dates[idx]}`;

                let planList = document.createElement("ul");
                planList.className = "plan";
                planWrap.appendChild(planList);
                cell.appendChild(planWrap);
            }
            row.appendChild(cell);
            idx++;
        }
        tableBody.appendChild(row);
    }

}

