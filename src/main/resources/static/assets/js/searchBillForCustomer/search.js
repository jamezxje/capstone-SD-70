document.getElementById('btn-search').addEventListener('click', function () {
    const codeCustomer = document.getElementById('maHoaDon').value.trim(); // Lấy giá trị tại thời điểm click

    console.log("Check mã hóa đơn:", codeCustomer);

    if (!codeCustomer) {
        alert("Vui lòng nhập mã hóa đơn.");
        return;
    }

    searchCodeForCustomer(codeCustomer);
});

function searchCodeForCustomer(code) {
    axios.get(`http://localhost:8080/search?code=${code}`)  // dùng backtick để tạo template string
        .then(response => {
            localStorage.setItem('codeCustomer', code);
            window.location.href = 'http://localhost:8080/searchBillCode'; // nhớ thêm http://
        })
        .catch(error => {
            if (error.response && error.response.status === 404) {
                toastr.options.positionClass = 'toast-top-right';
                toastr.error(error.response.data);
                return;
            } else {
                console.error("Lỗi khác: ", error);
                alert("Đã xảy ra lỗi. Vui lòng thử lại.");
            }
        });
}
