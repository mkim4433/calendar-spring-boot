// 플래너 진입 시 오늘에 해당하는 달력 표시.
// 사용자가 년, 월을 선택하면 해당하는 년, 월의 달력을 표시.
// 이전 월, 다음 월 버튼을 클릭하면 해당 월 달력 표시.

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

    initEvents();

});

function setYearAndMonthSelectUI() {
    document.querySelector("#section_wrap select[name='p_year']").value = selected_year;
    document.querySelector("#section_wrap select[name='p_month']").value = selected_month + 1;
}

function addCalendarRow() {

    // 선택 월의 첫날을 구한 다음, date, day 를 구함.
    let first = new Date(selected_year, selected_month, 1);
    // let firstDate = first.getDate();
    let firstDay = first.getDay();

    // 선택 월의 마지막날을 구한 다음, date 를 구함.
    let last = new Date(selected_year, selected_month + 1, 0);
    let lastDate = last.getDate();

    // 1주일 7일(1줄) * 최대 6줄 = 42
    // 42개짜리 배열을 만들고 요일과 일이 맞게 날짜를 넣고, 그외에는 0을 집어넣음.
    let dates = [];
    let dateCount = 1;
    for (let i = 0; i < 42; i++) {
        if (i < firstDay || dateCount > lastDate) {
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

function initEvents() {

    // 이전 월 버튼 클릭 이벤트
    document.querySelector("#section_wrap .btn_pre").addEventListener("click", function () {
        // setPreMonth();
        handleChangeMonth("prev");
    })

    // 다음 월 버튼 클릭 이벤트
    document.querySelector("#section_wrap .btn_next").addEventListener("click", function () {
        // setNextMonth();
        handleChangeMonth("next");
    })

    // 년도 선택 이벤트
    document.querySelector("#section_wrap select[name='p_year']").addEventListener("change", function () {
        setMonthBySelectChanged();
    })

    // 월 선택 이벤트
    document.querySelector("#section_wrap select[name='p_month']").addEventListener("change", function () {
        setMonthBySelectChanged();
    })
}

function handleChangeMonth(type) {

    let yearSelect = document.querySelector("#section_wrap select[name='p_year']");
    let monthSelect = document.querySelector("#section_wrap select[name='p_month']");

    let temp_year = selected_year;
    let temp_month = type === "prev" ? selected_month - 1 : selected_month + 1;


    if (type === "prev") {

        if (yearSelect.value === "2024" && monthSelect.value === "1") {
            alert("2024년 1월 이전은 설정할 수 없습니다.");
            return false;
        }

        if (temp_month < 0) {
            temp_year -= 1;
            temp_month = 11;
        }
    }

    if (type === "next") {

        if (yearSelect.value === "2030" && monthSelect.value === "12") {
            alert("2030년 12월 이후는 설정할 수 없습니다.");
            return false;
        }

        if (temp_month > 11) {
            temp_year += 1;
            temp_month = 0;
        }
    }

    let tempCalendar = new Date(temp_year, temp_month, 1);
    setSelectedCalendar(
        tempCalendar.getFullYear(),
        tempCalendar.getMonth(),
        tempCalendar.getDate(),
        tempCalendar.getDay()
    );
    setYearAndMonthSelectUI();
    removeCalendarRow();
    addCalendarRow();
}

function removeCalendarRow() {

    let tbody = document.querySelector("#table_calendar tbody");
    tbody.innerHTML = "";
}

function setMonthBySelectChanged() {

    let temp_year = document.querySelector("select[name='p_year']").value;
    let temp_month = document.querySelector("select[name='p_month']").value - 1; // 0~11

    let tempCalendar = new Date(temp_year, temp_month, 1);
    setSelectedCalendar(
        tempCalendar.getFullYear(),
        tempCalendar.getMonth(),
        tempCalendar.getDate(),
        tempCalendar.getDay()
    );
    removeCalendarRow();
    addCalendarRow();
}

