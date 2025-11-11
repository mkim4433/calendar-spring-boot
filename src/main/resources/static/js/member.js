// 회원가입 유효성 검사
function signupForm() {

    let form = document.signup_form;
    if (form.id.value === '') {
        alert("INPUT NEW MEMBER ID!!")
        form.id.focus();

    } else if (form.pw.value === '') {
        alert("INPUT NEW MEMBER PW!!")
        form.pw.focus();

    } else if (form.mail.value === '') {
        alert("INPUT NEW MEMBER MAIL!!")
        form.mail.focus();

    } else if (form.phone.value === '') {
        alert("INPUT NEW MEMBER PHONE!!")
        form.phone.focus();

    } else {
        form.submit();
    }
}