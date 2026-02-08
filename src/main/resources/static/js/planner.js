// 플래너 진입 시 오늘에 해당하는 달력 표시.
// 사용자가 년, 월을 선택하면 해당하는 년, 월의 달력을 표시.
// 이전 월, 다음 월 버튼을 클릭하면 해당 월 달력 표시.

let current_year;
let current_month;
let current_date;
let current_day;

function setCurrentCalendar(year, month, date, day) {
    current_year = year;
    current_month = month;
    current_date = date;
    current_day = day;
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

    setCurrentCalendar(today_year, today_month, today_date, today_day);

    setYearAndMonthSelectUI();

    addCalendarRow();
    fetchCurrentMonthPlans();

    initEvents();

});

function setYearAndMonthSelectUI() {
    document.querySelector("#section_wrap select[name='p_year']").value = current_year;
    document.querySelector("#section_wrap select[name='p_month']").value = current_month + 1;
}

function addCalendarRow() {

    // 선택 월의 첫날을 구한 다음, date, day 를 구함.
    let first = new Date(current_year, current_month, 1);
    // let firstDate = first.getDate();
    let firstDay = first.getDay();

    // 선택 월의 마지막날을 구한 다음, date 를 구함.
    let last = new Date(current_year, current_month + 1, 0);
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

    // 'click' 이벤트 핸들러
    document.addEventListener("click", function (e) {
        // 달력에서 이전 월 버튼 클릭 시
        if (e.target.matches("#section_wrap .btn_pre")) {
            handleChangeMonth("prev");
        }

        // 달력에서 다음 월 버튼 클릭 시
        if (e.target.matches("#section_wrap .btn_next")) {
            handleChangeMonth("next");
        }

        // 달력에서 일정 쓰기 버튼 클릭 시
        if (e.target.matches("#section_wrap a.write")) {

            let year = current_year;
            let month = current_month + 1;
            let date = e.target.parentElement.previousSibling.textContent;
            // let date = dateDiv ? dateDiv.textContent : "";

            showWritePlanView(year, month, date);
        }

        // 일정 등록 모달에서 등록 버튼 클릭 시
        if (e.target.matches("#write_plan input[value='WRITE']")) {

            console.log(" WRITE btn clicked");
            let year = document.querySelector("#write_plan select[name='wp_year']").value;
            let month = document.querySelector("#write_plan select[name='wp_month']").value;
            let date = document.querySelector("#write_plan select[name='wp_date']").value;

            let title = document.querySelector("#write_plan input[name='p_title']").value;
            let body = document.querySelector("#write_plan input[name='p_body']").value;
            let file = document.querySelector("#write_plan input[name='p_file']").value;

            // 입력값 유효성 체크
            if (title === "") {
                alert("Input plan title!");
                document.querySelector("#write_plan input[name='p_title']").focus();
            } else if (body === "") {
                alert("Input plan body!");
                document.querySelector("#write_plan input[name='p_body']").focus();
            } else if (file === "") {
                alert("Select image file!");
                document.querySelector("#write_plan input[name='p_file']").focus();
            } else {
                let inputFile = document.querySelector("#write_plan input[name='p_file']");
                let files = inputFile.files;

                fetchWritePlan(year, month, date, title, body, files[0]);

                // 등록한 일정이 달력에서 보여야 됨.
                // 등록한 일자로 상세 조회?
            }
        }

        // @todo 모달 초기화 기능 구현
        // 일정 등록 모달에서 초기화 버튼 클릭 시


        // 일정 등록 모달에서 취소 버튼 클릭 시
        if (e.target.matches("#write_plan input[value='CANCEL']")) {
            hideWritePlanView();
        }
    })

    // 'change' 이벤트 핸들러
    document.addEventListener("change", function (e) {
        // 달력에서 년도, 월 select 변경 이벤트
        if (e.target.matches("#section_wrap select[name='p_year']") || e.target.matches("#section_wrap select[name='p_month']")) {
            setMonthBySelectChanged();
        }

        // 일정 등록 모달에서 년도 변경 시
        if (e.target.matches("#write_plan select[name='wp_year']")) {

            let year = e.target.value;
            let month = document.querySelector("#write_plan select[name='wp_month']").value;

            setDateSelectOptions(year, month, "wp_date");

        }

        // 일정 등록 모달에서 월 변경 시
        if (e.target.matches("#write_plan select[name='wp_month']")) {

            let year = document.querySelector("#write_plan select[name='wp_year']").value;
            let month = e.target.value;

            setDateSelectOptions(year, month, "wp_date");

        }

    })
}

function handleChangeMonth(type) {

    let yearSelect = document.querySelector("#section_wrap select[name='p_year']");
    let monthSelect = document.querySelector("#section_wrap select[name='p_month']");

    let temp_year = current_year;
    let temp_month = type === "prev" ? current_month - 1 : current_month + 1;


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
    setCurrentCalendar(
        tempCalendar.getFullYear(),
        tempCalendar.getMonth(),
        tempCalendar.getDate(),
        tempCalendar.getDay()
    );
    setYearAndMonthSelectUI();
    removeCalendarRow();
    addCalendarRow();
    fetchCurrentMonthPlans();
}

function removeCalendarRow() {

    let tbody = document.querySelector("#table_calendar tbody");
    tbody.innerHTML = "";
}

function setMonthBySelectChanged() {

    let temp_year = document.querySelector("select[name='p_year']").value;
    let temp_month = document.querySelector("select[name='p_month']").value - 1; // 0~11

    let tempCalendar = new Date(temp_year, temp_month, 1);
    setCurrentCalendar(
        tempCalendar.getFullYear(),
        tempCalendar.getMonth(),
        tempCalendar.getDate(),
        tempCalendar.getDay()
    );
    removeCalendarRow();
    addCalendarRow();
    fetchCurrentMonthPlans();
}

function showWritePlanView(year, month, date) {

    document.querySelector("#write_plan select[name='wp_year']").value = year;
    document.querySelector("#write_plan select[name='wp_month']").value = month;

    setDateSelectOptions(year, month, "wp_date");
    document.querySelector("#write_plan select[name='wp_date']").value = date;

    document.querySelector("#write_plan").style.display = "block";
}

function hideWritePlanView(year, month, date) {

    document.querySelector("#write_plan input[name='p_title']").value = "";
    document.querySelector("#write_plan input[name='p_body']").value = "";
    document.querySelector("#write_plan input[name='p_file']").value = "";

    document.getElementById("write_plan").style.display = "none";
}

// 년, 월 변경 시 해당 월에 맞게 일 셀렉트 옵션을 재구성.
function setDateSelectOptions(year, month, selectName) {

    let last = new Date(year, month, 0);

    let selectElement = document.querySelector(`select[name='${selectName}']`);

    selectElement.innerHTML = "";

    for (let i = 1; i <= last.getDate(); i++) {
        let option = document.createElement("option");
        option.value = i;
        option.textContent = i;
        selectElement.appendChild(option);
    }
}