const idBac = localStorage.getItem('codeCustomer');
const modal = document.getElementById("confirmModalStatus");
const cancelModal = document.getElementById('cancelModalStatus');
const closeBtnhuy = document.getElementsByClassName("closehuy")[0];
const cancelReason = document.getElementById("cancelmationReason");
const btnCancel = document.getElementById('cancelBill');
const cancelButton = document.getElementById("cancelButton");
btnCancel.onclick = function () {
    cancelModal.style.display = 'block';
}
closeBtnhuy.onclick = function () {
    cancelModal.style.display = "none";
}

cancelButton.onclick = function () {
    const reason = cancelReason.value.trim();
    const isValidChars = /^[\p{L}\s]+$/u.test(reason);
    if (reason.length >= 20 && isValidChars) {
        cancelModal.style.display = 'none';
        actionDescription = reason;
        cancelReason.value = '';
        cancelBill();
        location.reload();
    } else if (!isValidChars) {
        showToast("Lý do không được chứa số hoặc ký tự đặc biệt.");
    } else {
        showToast("Vui lòng nhập ít nhất 20 ký tự.");
    }
};
function getTimeStatus(code) {
    axios.get(`http://localhost:8080/getStatus-history-customer?code=${code}`)
        .then(response => {
            console.log("Full response data: ", response.data);

            if (Array.isArray(response.data) && response.data.length > 0) {
                const statusList = response.data;

                const timelineItems = document.querySelectorAll('.timeline-item');
                timelineItems.forEach(item => item.style.display = 'none');

                statusList.forEach(item => {
                    const status = item.status;
                    const createDate = item.createDate;
                    const iconElement = getStatusIcon(status);
                    const formattedDate = formatDate(createDate);

                    updateTimeline(status, iconElement, formattedDate);
                });
            } else {
                console.error("Không có dữ liệu lịch sử hóa đơn.");
            }
        })
        .catch(error => {
            console.error("Error", error);
        });
}
function updateTimeline(status, icon, date) {
    const statusElement = document.getElementById(getStatusId(status));
    if (statusElement) {
        statusElement.style.display = "block";
        const dateDiv = statusElement.querySelector('.date');
        dateDiv.textContent = date;
    }
}
function getStatusIcon(status) {
    switch (status) {
        case "CHO_XAC_NHAN":
            return "📜";
        case "XAC_NHAN":
            return "✅";
        case "CHO_VAN_CHUYEN":
            return "🚚";
        case "VAN_CHUYEN":
            return "🚛";
        case "DA_THANH_TOAN":
            return "💳";
        case "THANH_CONG":
            return "🎉";
        case "DA_HUY":
            return "❌";
        default:
            return "❓";
    }
}
function formatDate(dateString) {
    const date = new Date(dateString);
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();

    return `${hours}:${minutes}:${seconds} ${day}-${month}-${year}`;
}
function getStatusId(status) {
    switch (status) {
        case "CHO_XAC_NHAN":
            return "waiting-confirmation";
        case "XAC_NHAN":
            return "confirmed";
        case "CHO_VAN_CHUYEN":
            return "waiting-shipping";
        case "VAN_CHUYEN":
            return "shipping";
        case "DA_THANH_TOAN":
            return "paid";
        case "THANH_CONG":
            return "completed";
        case "DA_HUY":
            return "cancel";
        default:
            return null;
    }
}
function getStatusText(status) {
    switch(status) {
        case 'CHO_XAC_NHAN': return 'Chờ xác nhận';
        case 'CHO_VAN_CHUYEN': return 'Chờ vận chuyển';
        case 'VAN_CHUYEN': return 'Đang vận chuyển';
        case 'XAC_NHAN': return 'Đã xác nhận';
        case 'DA_THANH_TOAN': return 'Đã thanh toán';
        case 'THANH_CONG': return 'Hoàn thành';
        case 'TRA_HANG': return 'Trả hàng';
        case 'DA_HUY': return 'Hủy';
        default: return 'Không xác định';
    }
}
function formatDate1(dateString) {
    const date = new Date(dateString);
    return `${date.getDate()}-${date.getMonth() + 1}-${date.getFullYear()}`;
}

function getInforBill(code) {
    axios.get(`http://localhost:8080/getInforBillCustomer/${code}`)
        .then(response => {
            const data = response.data;
            console.log("Check data" , data)
            document.getElementById("billId").textContent = data.code || 'Không có dữ liệu';
            document.getElementById("status").textContent = getStatusText(data.status) || 'Không có dữ liệu';
            document.getElementById("type").textContent = data.type || 'Không có dữ liệu';
            document.getElementById("address").textContent = data.address || 'Không có dữ liệu';
            document.getElementById("note").textContent = data.note || 'Không có dữ liệu';
            document.getElementById("customerName").textContent = data.userName || 'Không có dữ liệu';
            document.getElementById("phoneNumber").textContent = data.phoneNumber || 'Không có dữ liệu';
            document.getElementById("shipDate").textContent = data.shipDate ? formatDate1(data.shipDate) : 'Không có dữ liệu';
            if (data.status === "VAN_CHUYEN") {
                document.getElementById('cancelBill').style.display = 'none';
            } else if (data.status === "DA_THANH_TOAN") {
                document.getElementById('cancelBill').style.display = 'none';

            } else if (data.status === "THANH_CONG") {
                document.getElementById('cancelBill').style.display = 'none';

            }  else if (data.status === "DA_HUY") {
        document.getElementById('cancelBill').style.display = 'none';

    }
            else {
                document.getElementById('cancelBill').style.display = 'block';
            }
        })
        .catch(error => {
            console.log("Error fetching bill info", error);
        });
}
function formatCurrency(number) {
    return number.toLocaleString('vi-VN') + ' VND';
}

function loadProducts(code) {
    axios.get(`http://localhost:8080/search-product-customer/${code}`)
        .then(response => {
            const productList = response.data;
            const tableBody = document.getElementById("product-list");

            tableBody.innerHTML = "";

            productList.forEach((product, index) => {
                const total = product.price * product.quantity;

                const row = document.createElement("tr");

                row.innerHTML = `
                    <td>${index + 1}</td>
                    <td>
                        <img src="${product.image || 'https://via.placeholder.com/70'}" 
                             alt="Ảnh sản phẩm" 
                             width="70" height="70"
                             style="object-fit: cover; border-radius: 6px;" />
                        <div><h6>${product.name}</h6></div>
                    </td>
                    <td>${product.size}</td>
                    <td>${product.color}</td>
                    <td>${product.quantity}</td>
                    <td>${formatCurrency(product.price)}</td>
                    <td>${formatCurrency(total)}</td>
                `;

                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error("Lỗi khi load sản phẩm:", error);
        });
}
function getInforBillPrice(code) {
    axios.get(`http://localhost:8080/getTotalBill/${code}`)
        .then(response => {
            const data = response.data;
            console.log("Check data bill price", data);

            if (Array.isArray(data) && data.length > 0) {
                const billInfo = data[0];

                const afterPrice = billInfo.afterPrice || 0;
                const moneyShip = billInfo.moneyShip || 0;
                const beforePrice = billInfo.beforePrice || 0;
                const discount = billInfo.discountPrice || 0;

                const totalPrice = afterPrice + moneyShip;


                document.getElementById("beforePrice").textContent = formatVND(beforePrice) || 'Không có dữ liệu';
                document.getElementById("afterPrice").textContent = formatVND(afterPrice) || 'Không có dữ liệu';
                document.getElementById("moneyShip").textContent = formatVND(moneyShip) || 'Không có dữ liệu';
                document.getElementById("discountPrice").textContent = formatVND(discount) || 'Không có dữ liệu';
                document.getElementById("discountPrice-1").textContent = formatVND(discount) || 'Không có dữ liệu';
                document.getElementById("totalPrice").textContent = formatVND(totalPrice) || 'Không có dữ liệu';
            } else {
                console.error("Không có dữ liệu hóa đơn.");
            }
        })
        .catch(error => {
            console.log("Error fetching bill info", error);
        });
}
function cancelBill () {
    const employeeId = 1;
    axios.put(`http://localhost:8080/cancel-bill-customer/${idBac}?idEmployee=${employeeId}`, {
        actionDescription: actionDescription
    })
        .then(response => {
            const newStatus = response.data.status;
            console.log("New Status: ", newStatus);
            updateTimelineStatus(newStatus);
            getInforBill(idBac)
        })
        .catch(error => {
            console.error("Error changing status", error);
        });
}

function updateTimelineStatus(status) {
    switch(status) {
        case "CHO_XAC_NHAN":
            if (document.getElementById("waiting-confirmation").style.display === "none") {
                document.getElementById("waiting-confirmation").style.display = "block";
            }
            break;
        case "XAC_NHAN":
            if (document.getElementById("confirmed").style.display === "none") {
                document.getElementById("confirmed").style.display = "block";
            }
            break;
        case "CHO_VAN_CHUYEN":
            if (document.getElementById("waiting-shipping").style.display === "none") {
                document.getElementById("waiting-shipping").style.display = "block";
            }
            break;
        case "VAN_CHUYEN":
            if (document.getElementById("shipping").style.display === "none") {
                document.getElementById("shipping").style.display = "block";
                document.getElementById('cancelBill').style.display = 'none'
            }
            break;
        case "DA_THANH_TOAN":
            if (document.getElementById("paid").style.display === "none") {
                document.getElementById("paid").style.display = "block";
                document.getElementById('cancelBill').style.display = 'none'
            }
            break;
        case "THANH_CONG":
            if (document.getElementById("completed").style.display === "none") {
                document.getElementById("completed").style.display = "block";
                document.getElementById('cancelBill').style.display = 'none'
            }
            break;
        case "DA_HUY" :
            if (document.getElementById("cancel").style.display === "none") {
                document.getElementById('cancel').style.display = 'block';
                document.getElementById('cancelBill').style.display = 'none'
            }
        default:
            console.error("Unknown status", status);
            cancelButton.style.display = "none";
    }
}
function formatVND(value) {
    const parts = value.toString().split(".");
    const formattedWhole = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    if (parts.length > 1 && parts[1] === "00") {
        return formattedWhole;
    }
    return parts.length > 1 ? formattedWhole + "." + parts[1] : formattedWhole;
}
getInforBillPrice(idBac);
loadProducts(idBac)
getInforBill(idBac);
getTimeStatus(idBac);
