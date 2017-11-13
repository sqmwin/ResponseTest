function changeCode() {
    var img = document.getElementById("code");
    img.src = "/servlet/verificationcodeservlet?" + new Date().getTime();
}