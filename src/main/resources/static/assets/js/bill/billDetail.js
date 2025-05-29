const url = window.location.pathname;
const id = url.split('/').pop();

localStorage.setItem('billId', id);

let billId = localStorage.getItem('billId')
const modal = document.getElementById("confirmModalStatus");
const cancelModal = document.getElementById('cancelModalStatus');
const historyModal = document.getElementById('historyModalStatus');
const btn = document.getElementById("changeStatusButton");
const closeBtn = document.getElementsByClassName("closexacnhan")[0];
const closeBtnhuy = document.getElementsByClassName("closehuy")[0];
const closeBtnhistory = document.getElementsByClassName("closehistory")[0];
const confirmButton = document.getElementById("confirmButton");
const btnCancel = document.getElementById('cancelBill');
const btnhistory = document.getElementById('historyBill');
const confirmationReason = document.getElementById("confirmationReason");
const cancelReason = document.getElementById("cancelmationReason");
const cancelButton = document.getElementById("cancelButton");

let actionDescription = null;

btn.onclick = function () {
    modal.style.display = "block";
}
closeBtn.onclick = function () {
    modal.style.display = "none";
}
btnCancel.onclick = function () {
    cancelModal.style.display = 'block';
}
closeBtnhuy.onclick = function () {
    cancelModal.style.display = "none";
}
btnhistory.onclick = function () {
    historyModal.style.display = 'block';
}
closeBtnhistory.onclick = function () {
    historyModal.style.display = "none";
}

cancelButton.onclick = function () {
    const reason = cancelReason.value.trim();

    // Regex: chỉ cho phép chữ cái và khoảng trắng (có dấu cũng được)
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

confirmButton.onclick = function () {
    const reason = confirmationReason.value.trim();
    const isValidChars = /^[\p{L}\s]+$/u.test(reason);

    if (reason.length >= 5 && isValidChars) {
        modal.style.display = "none";
        actionDescription = reason;
        confirmationReason.value = "";
        changeStatus();
        // location.reload();

    } else if (!isValidChars) {
        showToast("Lý do không được chứa số hoặc ký tự đặc biệt.");
    } else {
        showToast("Vui lòng nhập tối thiểu 5 ký tự.");
    }

};

function showSuccessToast(message) {
    const toast = document.getElementById("toast-success");
    toast.textContent = message;
    toast.style.display = "block";
    toast.style.opacity = "1";

    setTimeout(() => {
        toast.style.opacity = "0";
        setTimeout(() => {
            toast.style.display = "none";
        }, 500);
    }, 3000);
}

function showToast(message) {
    const toast = document.getElementById("toast");
    toast.textContent = message;
    toast.style.display = "block";
    toast.style.opacity = "1";

    setTimeout(() => {
        toast.style.opacity = "0";
        setTimeout(() => {
            toast.style.display = "none";
        }, 500); // chờ hiệu ứng mờ xong mới ẩn
    }, 3000); // hiện trong 3s
}

document.getElementById('btn-changeInfor').addEventListener('click', function () {
    // Lấy các giá trị từ input/select
    const customerName = document.getElementById("customerNameModal").value.trim();
    const phoneNumber = document.getElementById("phoneNumberModal").value.trim();
    const city = document.getElementById("citySelectModal").value;
    const district = document.getElementById("districtSelectModal").value;
    const ward = document.getElementById("wardSelectModal").value;
    const detailAddress = document.getElementById("detailAddressModal").value.trim();

    // Validate
    const phoneRegex = /^0\d{9}$/;

    if (customerName === "") {
        showToast("Tên khách hàng không được để trống.");
        return;
    }

    const nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;

    if (!nameRegex.test(customerName)) {
        showToast("Tên khách hàng không được chứa ký tự đặc biệt hoặc số.");
        return;
    }

    if (!phoneRegex.test(phoneNumber)) {
        showToast("Số điện thoại không hợp lệ (phải có 10 chữ số và bắt đầu bằng 0).");
        return;
    }

    if (city === "") {
        showToast("Vui lòng chọn thành phố.");
        return;
    }

    if (district === "") {
        showToast("Vui lòng chọn huyện.");
        return;
    }

    if (ward === "") {
        showToast("Vui lòng chọn xã.");
        return;
    }

    if (detailAddress === "") {
        showToast("Vui lòng nhập địa chỉ chi tiết.");
        return;
    }

    updateInforBill(billId);
    const modal = document.getElementById("changeAddressModal");
    modal.style.display = "none";

    showSuccessToast("Thay đổi thông tin khách hàng thành công.");
});


function changeStatus() {
    const employeeId = 1;
    axios.put(`http://localhost:8080/change-status/${billId}?idEmployee=${employeeId}`, {
        actionDescription: actionDescription
    })
        .then(response => {
            const newStatus = response.data.status;
            if (newStatus === "XAC_NHAN") {
                printBill();
            }
            console.log("New Status: ", newStatus);
            updateTimelineStatus(newStatus);
            getInforBill(billId);
            showSuccessToast("Chuyển trạng thái thành công!");
        })
        .catch(error => {
            console.error("Error changing status", error);
            if (error.response && error.response.data) {
                const errorMessage = error.response.data.message || "Đã có lỗi xảy ra!";
                console.log('Check error', errorMessage)
                // alert(`Lỗi: ${errorMessage}`);
                showToast(errorMessage)
                // toastr.options.positionClass = 'toast-top-right'
                // toastr.error(errorMessage);
            } else {
                // Trường hợp không có phản hồi rõ ràng
                alert("Lỗi không xác định. Vui lòng thử lại sau.");
            }

        });
}

function cancelBill() {
    const employeeId = 1;
    axios.put(`http://localhost:8080/cancel-bill/${billId}?idEmployee=${employeeId}`, {
        actionDescription: actionDescription
    })
        .then(response => {
            const newStatus = response.data.status;
            console.log("New Status: ", newStatus);
            updateTimelineStatus(newStatus);
            getInforBill(billId);
            showSuccessToast("Hủy thành công!");
        })
        .catch(error => {
            console.error("Error changing status", error);
        });
}

function updateTimelineStatus(status) {
    switch (status) {
        case "CHO_XAC_NHAN":
            if (document.getElementById("waiting-confirmation").style.display === "none") {
                document.getElementById("waiting-confirmation").style.display = "block";
            }
            break;
        case "XAC_NHAN":
            if (document.getElementById("confirmed").style.display === "none") {
                document.getElementById("confirmed").style.display = "block";
                document.getElementById('changeInfor').style.display = 'none';
            }
            printBill();
            break;
        case "CHO_VAN_CHUYEN":
            if (document.getElementById("waiting-shipping").style.display === "none") {
                document.getElementById("waiting-shipping").style.display = "block";
                document.getElementById('changeInfor').style.display = 'none';
            }
            break;
        case "VAN_CHUYEN":
            if (document.getElementById("shipping").style.display === "none") {
                document.getElementById("shipping").style.display = "block";
                document.getElementById('cancelBill').style.display = 'none'
                document.getElementById('changeInfor').style.display = 'none';
            }
            break;
        case "DA_THANH_TOAN":
            if (document.getElementById("paid").style.display === "none") {
                document.getElementById("paid").style.display = "block";
                document.getElementById('changeInfor').style.display = 'none';
            }
            break;
        case "THANH_CONG":
            if (document.getElementById("completed").style.display === "none") {
                document.getElementById("completed").style.display = "block";
                document.getElementById('changeStatusButton').style.display = 'none'
                document.getElementById('changeInfor').style.display = 'none';
            }
            break;
        case "DA_HUY" :
            if (document.getElementById("cancel").style.display === "none") {
                document.getElementById('cancel').style.display = 'block';
                document.getElementById('changeStatusButton').style.display = 'none'
                document.getElementById('cancelBill').style.display = 'none'
                document.getElementById('changeInfor').style.display = 'none'
            }
        default:
            console.error("Unknown status", status);
    }
}

function getTimeStatus(billid) {
    axios.get(`http://localhost:8080/getStatus-history?id=${billid}`)
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
            document.getElementById('changeInfor').style.display = 'none'
            return "confirmed";
        case "CHO_VAN_CHUYEN":
            document.getElementById('changeInfor').style.display = 'none'

            return "waiting-shipping";
        case "VAN_CHUYEN":
            document.getElementById('changeInfor').style.display = 'none'

            return "shipping";
        case "DA_THANH_TOAN":
            document.getElementById('changeInfor').style.display = 'none'

            return "paid";
        case "THANH_CONG":
            document.getElementById('changeStatusButton').style.display = 'none'
            document.getElementById('cancelBill').style.display = 'none'
            document.getElementById('changeInfor').style.display = 'none'

            return "completed";
        case "DA_HUY":
            document.getElementById('changeStatusButton').style.display = 'none'
            document.getElementById('cancelBill').style.display = 'none'
            document.getElementById('changeInfor').style.display = 'none'
            return "cancel";
        default:
            return null;
    }
}

function formatRawDateToVN(rawDate) {
    const dateObj = new Date(
        rawDate[0],
        rawDate[1],
        rawDate[2],
        rawDate[3],
        rawDate[4],
        rawDate[5],
        Math.floor(rawDate[6] / 1000000)
    );

    const day = dateObj.getDate().toString().padStart(2, '0');
    const month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
    const year = dateObj.getFullYear();

    return `${day}/${month}/${year}`; // dạng dd/MM/yyyy
}

let nameCustomer = null, numberPhone = null, address = null, codeBill = null, createDate = null;

function getInforBill(billId) {
    axios.get(`http://localhost:8080/getInforBill/${billId}`)
        .then(response => {
            const data = response.data;
            console.log("Check data nhận được ", data)
            document.getElementById("billId").textContent = data.code || 'Không có dữ liệu';
            document.getElementById("status").textContent = getStatusText(data.status) || 'Không có dữ liệu';
            document.getElementById("type").textContent = data.type || 'Không có dữ liệu';
            document.getElementById("address").textContent = data.address || 'Không có dữ liệu';
            document.getElementById("note").textContent = data.note || 'Không có dữ liệu';
            document.getElementById("customerName").textContent = data.userName || 'Không có dữ liệu';
            document.getElementById("phoneNumber").textContent = data.phoneNumber || 'Không có dữ liệu';
            document.getElementById("shipDate").textContent = data.shipDate ? formatDate1(data.shipDate) : 'Không có dữ liệu';
            console.log("CHeck ngay tao ", formatRawDateToVN(data.createDate))
            createDate = data.createDate;
            nameCustomer = data.userName;
            numberPhone = data.phoneNumber;
            address = data.address;
            codeBill = data.code;
            // printBill();
            getTotalPayMentCustomer(codeBill)
            if (data.status === "VAN_CHUYEN") {
                document.getElementById('cancelBill').style.display = 'none';
            } else if (data.status === "DA_THANH_TOAN") {
                document.getElementById('cancelBill').style.display = 'none';

            } else if (data.status === "THANH_CONG") {
                document.getElementById('cancelBill').style.display = 'none';

            } else {
                document.getElementById('cancelBill').style.display = 'block';
            }

            console.log("data name", nameCustomer)
        })
        .catch(error => {
            console.log("Error fetching bill info", error);
        });
}

function getStatusText(status) {
    switch (status) {
        case 'CHO_XAC_NHAN':
            return 'Chờ xác nhận';
        case 'CHO_VAN_CHUYEN':
            return 'Chờ vận chuyển';
        case 'VAN_CHUYEN':
            return 'Đang vận chuyển';
        case 'XAC_NHAN':
            return 'Đã xác nhận';
        case 'DA_THANH_TOAN':
            return 'Đã thanh toán';
        case 'THANH_CONG':
            return 'Hoàn thành';
        case 'TRA_HANG':
            return 'Trả hàng';
        case 'DA_HUY':
            return 'Hủy';
        default:
            return 'Không xác định';
    }
}

function formatDate1(dateString) {
    const date = new Date(dateString);
    return `${date.getDate()}-${date.getMonth() + 1}-${date.getFullYear()}`;
}

getInforBill(billId)
getTimeStatus(billId)

document.querySelector(".btn-update").addEventListener("click", function () {
    const modal = document.getElementById("changeAddressModal");
    modal.style.display = "block";
    document.getElementById("customerNameModal").value = nameCustomer;
    document.getElementById("phoneNumberModal").value = numberPhone;

    const addressParts = address.split(',').reverse();

    const city = addressParts[0].trim();
    const district = addressParts[1].trim();
    const ward = addressParts[2].trim();
    const detailAddress = addressParts[3] ? addressParts[3].trim() : '';

    document.getElementById("citySelectModal").value = city;
    document.getElementById("districtSelectModal").value = district;
    document.getElementById("wardSelectModal").value = ward;
    document.getElementById("detailAddressModal").value = detailAddress;
    setSelectValue(document.getElementById("citySelectModal"), city);
    setSelectValue(document.getElementById("districtSelectModal"), district);
    setSelectValue(document.getElementById("wardSelectModal"), ward);
});

function setSelectValue(selectElement, value) {
    let exists = false;
    for (let option of selectElement.options) {
        if (option.value === value) {
            exists = true;
            break;
        }
    }

    if (!exists) {
        const newOption = document.createElement("option");
        newOption.value = value;
        newOption.text = value;
        selectElement.appendChild(newOption);
    }

    selectElement.value = value;
}

document.querySelector(".close-btn").addEventListener("click", function () {
    const modal = document.getElementById("changeAddressModal");
    modal.style.display = "none";
});

const tokenApiGHN = '7d67a984-b5fe-11ef-b166-4205c1d15e61';
const shopId = '5511482';
const clientId = '4574315';
const urlProvince = 'https://online-gateway.ghn.vn/shiip/public-api/master-data/province';
const urlDistricts = 'https://online-gateway.ghn.vn/shiip/public-api/master-data/district';
const urlWard = 'https://online-gateway.ghn.vn/shiip/public-api/master-data/ward';
const urlMoneyShip = 'https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee';
const urlDayShip = 'https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/leadtime';
let provinceName = null;
let districtName = null;
let wardName = null;
fetchAllProvince();

function fetchAllProvince() {
    axios.get(urlProvince, {
        headers: {
            token: tokenApiGHN,
        }
    })
        .then(response => {
            const proviences = response.data.data;
            const selectElement = document.getElementById('citySelectModal');
            proviences.forEach(province => {
                const option = document.createElement('option');
                option.value = province.ProvinceID;
                option.textContent = province.ProvinceName;
                selectElement.appendChild(option);

            })

            console.log(response.data);
        })
        .catch(error => {
            console.error('Có lỗi xảy ra:', error);
        });

}

function fetchProvinceDistricts(idProvince) {
    axios.get(urlDistricts, {
        params: {
            province_id: idProvince
        },
        headers: {
            token: tokenApiGHN,
        }

    })
        .then(response => {
            const districts = response.data.data;
            const districtSelect = document.getElementById('districtSelectModal');
            districtSelect.innerHTML = '<option value="">Chọn</option>';
            districts.forEach(district => {
                const option = document.createElement('option');
                option.value = district.DistrictID;
                option.textContent = district.DistrictName;
                districtSelect.appendChild(option);
            })

            if (idDistrictChose) {
                document.getElementById('districtSelect').value = idDistrictChose
                console.log("Check ", idDistrictChose)
            }

            console.log("huyện", response.data)
        })
        .catch(error => {
            console.log("erorr", error)
        })

}

function fetchProvinceWard(idDistrict) {
    axios.get(urlWard, {
        params: {
            district_id: idDistrict
        },
        headers: {
            token: tokenApiGHN
        }
    })
        .then(response => {
            const wards = response.data.data;
            const wardSelect = document.getElementById('wardSelectModal');
            wardSelect.innerHTML = '<option value="">Chọn</option>';
            wards.forEach(ward => {
                const option = document.createElement('option');
                option.value = ward.WardCode;
                option.textContent = ward.WardName;
                wardName = ward.WardName;
                wardSelect.appendChild(option);
            })
            if (idWardCodeChose) {
                document.getElementById('wardSelect').value = idWardCodeChose
                fetchAllAddress();
                console.log("Check ", idWardCodeChose)

            }
            console.log("Xax", response.data);
        })
        .catch(error => {
            console.log("error", error)
        })

}

document.getElementById('citySelectModal').addEventListener('change', function () {

    const provinceId = this.value;
    const selectOption = this.options[this.selectedIndex];
    provinceName = selectOption.textContent || selectOption.innerText;
    console.log("Check prodvince id", provinceId);

    if (provinceId) {
        fetchProvinceDistricts(provinceId);
    } else {
        const districtSelect = document.getElementById('districtSelectModal');
        districtSelect.innerHTML = '<option value="">Chọn</option>';
    }
});

document.getElementById('districtSelectModal').addEventListener('change', function () {
    const districtID = this.value;
    const selectOption = this.options[this.selectedIndex];
    districtName = selectOption.textContent || selectOption.innerText;
    console.log("Check distric", districtID);

    if (districtID) {
        fetchProvinceWard(districtID);
    } else {
        const wardSelect = document.getElementById('wardSelectModal');
        wardSelect.innerHTML = '<option value="">Chọn</option>';
    }
})
let fullAddress = null;
document.getElementById('wardSelectModal').addEventListener('change', function () {
    const wardCode = this.value;
    const selectOption = this.options[this.selectedIndex]
    wardName = selectOption.textContent || selectOption.innerText;

    console.log("Check ward", wardCode)
    const districtID = document.getElementById('districtSelectModal').value;
    if (districtID && wardCode) {
        fetchMoneyShip(districtID, wardCode, 1);
        fetchDayShip(districtID, wardCode);
        fetchAllAddress();
        document.getElementById('shipform').style.display = 'block';
    } else {
        console.log(" NO id dis and ward")
    }
})

let totalShipLocal = 0;
let priceAmountBillAndShipNoVoucher = 0;

function fetchMoneyShip(to_id_district, to_code_ward, quantity) {
    let quantityProducts = 0;
    if (quantity == "" || quantity == null || quantity == undefined) {
        quantityProducts = 1;
    } else {
        quantityProducts = quantity;
    }
    axios.get(urlMoneyShip, {
        params: {
            insurance_value: "",
            coupon: "",
            service_type_id: 2,
            from_district_id: 1492,
            from_ward_code: "1A0501",
            to_district_id: to_id_district,
            to_ward_code: to_code_ward,
            height: 50,
            length: 20,
            weight: 200,
            width: 20,
        },
        headers: {
            token: tokenApiGHN,
            shop_id: shopId,
        }
    })
        .then(response => {
            const totalShip = response.data.data;
            if (totalShip && totalShip.total !== undefined) {
                console.log("Tiền ship:", totalShip.total);
                totalShipLocal = totalShip.total;
                document.getElementById('totalShip').value = formatVND(totalShip.total) + "đ";
            } else {
                console.log("Không có giá trị total.");
            }
        })
        .catch(error => {
            console.log("Lỗi khi lấy tiền ship: ", error.response ? error.response.data : error);
        });

}

let dateShip = null;

function fetchDayShip(to_id_distrcit, to_code_ward) {
    console.log("gọi vào đây")
    axios.get(urlDayShip, {
        params: {
            from_district_id: 1492,
            from_ward_code: "1A0501",
            to_district_id: to_id_distrcit,
            to_ward_code: to_code_ward,
            service_id: 53320,
        },
        headers: {
            token: tokenApiGHN,
            shop_id: shopId,
        }
    })
        .then(response => {
            const dayShip = response.data.data;
            const leadTime = dayShip.leadtime;
            console.log("Data day", dayShip)
            const date = new Date(leadTime * 1000);
            const formattedDate = `${date.getDate().toString().padStart(2, '0')}/${(date.getMonth() + 1).toString().padStart(2, '0')}/${date.getFullYear()}`;
            dateShip = formattedDate;
            document.getElementById('confirm-day').value = formattedDate;

        })
        .catch(error => {
            console.log("erorr day ship", error);
        })
}

function formatVND(value) {
    const parts = value.toString().split(".");
    const formattedWhole = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    if (parts.length > 1 && parts[1] === "00") {
        return formattedWhole;
    }
    return parts.length > 1 ? formattedWhole + "." + parts[1] : formattedWhole;
}

function fetchAllAddress() {
    const addressValue = document.getElementById('detailAddressModal').value;
    console.log("Check pro", provinceName)
    console.log("Check dis", districtName)
    console.log("Check war", wardName)
    console.log('Check full ad', addressValue)
    if (provinceName && districtName && wardName) {
        const address = `${addressValue}, ${wardName}, ${districtName} , ${provinceName}`;
        fullAddress = address;
        console.log("Check địa chỉ đủ", fullAddress);
    } else {
        console.log("error adress")
    }
}

document.getElementById('detailAddressModal').addEventListener('input', () => {
    const addressValue = document.getElementById('detailAddressModal').value;
    console.log("Check pro", provinceName)
    console.log("Check dis", districtName)
    console.log("Check war", wardName)
    console.log('Check full ad', addressValue)
    if (provinceName && districtName && wardName) {
        const address = `${addressValue}, ${wardName}, ${districtName} , ${provinceName}`;
        fullAddress = address;
        console.log("Check địa chỉ đủ", fullAddress);
    } else {
        console.log("error adress")
    }
})

function updateInforBill(billId) {
    const nameCustomer = document.getElementById('customerNameModal').value;
    const phoneCustomer = document.getElementById('phoneNumberModal').value;
    const shipDate = document.getElementById('confirm-day').value;
    const moneyShip = document.getElementById('totalShip').value;
    let moneyShipFormatted = moneyShip.replace("đ", "").replace(",", "");
    const request = {
        customerName: nameCustomer,
        numberPhone: phoneCustomer,
        customerAddress: fullAddress,
        shipDate: shipDate,
        moneyShip: moneyShipFormatted
    }
    console.log("requst", request)
    axios.post(`/updateCustomer-bill/${billId}`, request)
        .then(response => {
            console.log("update thanh cong", response.data);
window.location.reload();
            getInforBill(billId)
        })
        .catch(error => {
            console.log("lỗi update", error);
        })
}

getProductForBill(billId)

function getProductForBill(billId) {
    axios.get(`/products/${billId}`)
        .then(response => {
            console.log("get product thành công", response.data);

            const products = response.data;
            const productListElement = document.getElementById('productList');
            productListElement.innerHTML = '';

            if (products.length === 0) {
                productListElement.innerHTML = '<li>Không có sản phẩm nào.</li>';
                return;
            }

            products.forEach((product, index) => {
                const li = document.createElement('li');
                li.textContent = `${index + 1}. ${product.name} - SL: ${product.quantity}`;
                productListElement.appendChild(li);
            });
        })
        .catch(error => {
            console.log("Lỗi lấy sản phẩm", error);
        });
}

let customerPaymentBill = null;

function getTotalPayMentCustomer(billCode) {
    axios.get(`/getTotalBill/${billCode}`)
        .then(response => {
            console.log("Lấy dữ liệu thành công", response.data);

            const data = response.data;

            if (Array.isArray(data) && data.length > 0) {
                const billInfo = data[0];
                console.log("Discount:", billInfo.discountPrice);
                console.log("Ship:", billInfo.moneyShip);
                console.log("Tổng thanh toán:", billInfo.afterPrice);
                const afterPrice = billInfo.afterPrice;
                const moneyShip = billInfo.moneyShip;
                const totalCustomerPayMent = afterPrice;
                customerPaymentBill = totalCustomerPayMent;
                console.log('Check customerPayMentBill', customerPaymentBill)
                // Gán vào HTML nếu muốn
                document.getElementById('printTotalPayment').innerText = totalCustomerPayMent.toLocaleString('vi-VN') + 'đ';

            } else {
                console.log("Không có dữ liệu tổng hóa đơn.");
            }
        })
        .catch(error => {
            console.log("Lỗi khi lấy tổng tiền hóa đơn:", error);
        });
}

document.getElementById('checkprint').addEventListener('click', function () {
    printBill();
})

function printBill() {
    console.log("Chechk chạy vào")
    document.getElementById('printCode').innerText = codeBill || 'Chưa có mã';
    document.getElementById('printCreateDate').innerText = formatRawDateToVN(createDate);
    // document.getElementById('printStaff').innerText = billData.idUser || 'Nhân viên chưa có';
    document.getElementById('printNameCustomer').innerText = nameCustomer || 'Khách lẻ';
    document.getElementById('printNumberPhoneCustomer').innerText = numberPhone || '';
    document.getElementById('printAddress').innerText = address || '';
    console.log('Check total ', customerPaymentBill)


    printJS({
        printable: 'invoice-container',
        type: 'html', // Loại in là HTML
        // header: 'Hóa Đơn Thanh Toán',
        style: `
            /* Đảm bảo tất cả CSS in ấn được áp dụng */
            @media print {
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 0;
                }
                #invoice-container {
                    display: block !important;
                    padding: 20px;
                    width: 100%;
                    box-sizing: border-box;
                }
           

.section {
    padding: 15px 0;
    border-bottom: 1px dashed #aaa;
}

.invoice-header {
    text-align: center;
}

.invoice-header h2 {
    margin: 0;
}

.invoice-info-section {
    display: flex;
    justify-content: space-between;
}

.invoice-info-box {
    width: 48%;
}

.invoice-info-box h4 {
    margin-bottom: 10px;
}

.invoice-info-box p {
    margin: 5px 0;
}

.order-content-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
    margin-bottom: 10px;
    position: relative;
}

.order-content-header::before {
    content: "";
    position: absolute;
    top: 0;
    bottom: 0;
    left: 50%;
    width: 1px;
    border-left: 1px dashed #aaa;
    transform: translateX(-50%);
}

.product-list {
    list-style-type: decimal;
    padding-left: 20px;
}

.product-list li {
    margin: 5px 0;
}

.invoice-footer {
    display: flex;
    justify-content: space-between;
}

.invoice-info-section,
.invoice-footer {
    display: flex;
    justify-content: space-between;
    position: relative;
}

.invoice-info-section::before,
.invoice-footer::before {
    content: "";
    position: absolute;
    top: 0;
    bottom: 0;
    left: 50%;
    width: 1px;
    border-left: 1px dashed #aaa;
    transform: translateX(-50%);
}

.invoice-payment {
    width: 48%;
    font-weight: bold;
}

.invoice-signature {
    width: 48%;
    text-align: right;
}

.signature-line {
    margin-top: 60px;
    border-top: 1px dashed #000;
    width: 100%;
    padding-top: 5px;
    font-style: italic;
}


            }
        `
    });

    document.getElementById("invoice-container").style.display = 'none';
    setTimeout(() => {
        if (!document.hidden) {
            location.reload();
        }
    }, 1000);
}

