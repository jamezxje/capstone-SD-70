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
function formatVND(value) {
    const parts = value.toString().split(".");
    const formattedWhole = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    if (parts.length > 1 && parts[1] === "00") {
        return formattedWhole;
    }
    return parts.length > 1 ? formattedWhole + "." + parts[1] : formattedWhole;
}

fetchAllProvince()
function fetchAllProvince() {
    axios.get(urlProvince, {
        headers: {
            token: tokenApiGHN,
        }
    })
        .then(response => {
            const proviences = response.data.data;
            const selectElement = document.getElementById('provinceSelect');
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
            const districtSelect = document.getElementById('districtSelect');
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
            const wardSelect = document.getElementById('wardSelect');
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
document.getElementById('provinceSelect').addEventListener('change', function () {

    const provinceId = this.value;
    const selectOption = this.options[this.selectedIndex];
    provinceName = selectOption.textContent || selectOption.innerText;
    console.log("Check prodvince id", provinceId);
    // if (provinceName === "Chọn") {
    //     document.getElementById("provinceSelectError").style.display = "block";
    //     document.getElementById("provinceSelectError").innerText = "Vui lòng chọn tỉnh/thành phố!";
    //     return false;
    // } else {
    //     document.getElementById("provinceSelectError").style.display = "none";
    //
    // }
    if (provinceId) {
        fetchProvinceDistricts(provinceId);
        const districtID = document.getElementById('districtSelect').value;
        const wardSelectID = document.getElementById('wardSelect').value;
        fetchMoneyShip(districtID, wardSelectID, 1);
    } else {
        const districtSelect = document.getElementById('districtSelect');
        districtSelect.innerHTML = '<option value="">Chọn</option>';
    }
});

document.getElementById('districtSelect').addEventListener('change', function () {
    const districtID = this.value;
    const selectOption = this.options[this.selectedIndex];
    districtName = selectOption.textContent || selectOption.innerText;
    console.log("Check distric", districtID);

    if (districtID) {
        fetchProvinceWard(districtID);
    } else {
        const wardSelect = document.getElementById('wardSelect');
        wardSelect.innerHTML = '<option value="">Chọn</option>';
    }
})
let fullAddress = null;
document.getElementById('wardSelect').addEventListener('change', function () {
    const wardCode = this.value;
    const selectOption = this.options[this.selectedIndex]
    wardName = selectOption.textContent || selectOption.innerText;

    console.log("Check ward", wardCode)
    const districtID = document.getElementById('districtSelect').value;
    if (districtID && wardCode) {
        fetchMoneyShip(districtID, wardCode, 1);
        fetchDayShip(districtID, wardCode);
        fetchAllAddress();
    }
    else {
        console.log(" NO id dis and ward")
    }

})

function fetchAllAddress() {
    const addressValue = document.getElementById('addressValue').value;
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

document.getElementById('addressValue').addEventListener('input', () => {
    const addressValue = document.getElementById('addressValue').value;
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
let totalShipLocal = 0;
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
                const wardSelect = document.getElementById("wardSelect").value;
                console.log('CHeck ward fetchh money', wardSelect)
                    document.getElementById('shipping').innerText = formatVND(totalShip.total) + " VND";
                    const totalPayShip = totalShip.total + totalShipAndItem;
                    document.getElementById('span-totalPayMent').innerText = formatVND(totalPayShip) + ' VND';
                    document.getElementById('image-ghn').style.display = 'block';
                // priceAmountBillAndShipNoVoucher = totalBill + totalShipLocal - voucherValueLocal;
                // document.getElementById('total-amount').innerText = formatVND(priceAmountBillAndShipNoVoucher) + "đ";
                // document.getElementById('amount').innerText = formatVND(priceAmountBillAndShipNoVoucher);
                // document.getElementById('input-payment').value = formatVND(priceAmountBillAndShipNoVoucher)
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
            document.getElementById('confirm-day').innerText = formattedDate;
        })
        .catch(error => {
            console.log("erorr day ship", error);
        })
}

let totalItem = 0;
let totalShipAndItem = 0;
let itemDiscountLocal = 0;
document.addEventListener("DOMContentLoaded", function () {
    function formatCurrency(number) {
        return number.toLocaleString("vi-VN") + " VND";
    }

    const rawData = localStorage.getItem("selectedProductsForCheckout");

    if (rawData) {
        const data = JSON.parse(rawData);
        const tbody = document.getElementById("checkout-body");

        data.forEach((item, index) => {
            const row = document.createElement("tr");
            const total = item.price * item.quantity;
totalItem += total;
            row.innerHTML = `
        <td>${index + 1}</td>
        <td>${item.name}</td>
        <td>${item.size?.name || ''}</td>
        <td>${item.color?.name || ''}</td>
        <td>${item.quantity}</td>
        <td>${formatCurrency(item.price)}</td>
        <td>${formatCurrency(total)}</td>
      `;

            tbody.appendChild(row);
        });
    }
    const voucherValueRaw = localStorage.getItem('voucherValueNoLogin');
    const voucherValueNoLogin = parseInt(voucherValueRaw) || 0;
    itemDiscountLocal = voucherValueNoLogin;
    document.getElementById('span-voucherValue').innerText = formatVND(voucherValueNoLogin) + " VND";
    document.getElementById('span-totalItem').innerText = formatCurrency(totalItem)
    const totalPayment = totalItem - voucherValueNoLogin
    totalShipAndItem = totalPayment;
    document.getElementById('span-totalPayMent').innerText = formatVND(totalPayment) + ' VND';
});

document.getElementById('cod-btn').addEventListener('click', function (event) {
    event.preventDefault(); // Ngăn submit mặc định nếu là form

    const isNameValid = checkName();
    const isPhoneValid = checkPhone();
    const isEmailValid = checkEmail();
    const isProvinceValid = checkProvince();
    const isDistrictValid = checkDistrict();
    const isWardValid = checkWard();
    const isAddressValid = checkFullAddress();

    if (!isNameValid || !isPhoneValid || !isEmailValid || !isProvinceValid || !isDistrictValid || !isWardValid || !isAddressValid) {
        toastr.options.positionClass = 'toast-top-right';
        toastr.error('Vui lòng nhập đủ thông tin!');
        return;
    }

    Swal.fire({
        title: 'Xác nhận',
        text: 'Bạn có xác nhận đặt hàng không?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            const userName = document.getElementById('nameCustomer').value;
            const phoneNumber = document.getElementById('numberPhoneCustomer').value;
            const email = document.getElementById('emailCustomer').value;
            const idVoucher = localStorage.getItem('voucherIDNoLogin');
            const note = document.getElementById('note').value;
            const selectedProductsForCheckout = JSON.parse(localStorage.getItem("selectedProductsForCheckout")) || [];
            const billDetail = selectedProductsForCheckout.map(item => ({
                idProductDetail: item.idProductLocal,
                price: item.price,
                quantity: item.quantity
            }));

            const bill = {
                userName: userName,
                phoneNumber: phoneNumber,
                email: email,
                address: fullAddress,
                shipDate: dateShip,
                moneyShip: totalShipLocal,
                itemDiscount: itemDiscountLocal,
                totalMoney: totalShipAndItem,
                billDetail: billDetail,
                paymentMethod: "paymentReceive",
                idVoucher: idVoucher,
                afterPrice: totalShipAndItem + totalShipLocal,
                idUser: 13,
                note: note
            };

            console.log("Check bill", bill);

            axios.post(`/createBill-Customer`, bill)
                .then(response => {
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công',
                        text: 'Đặt hàng thành công!'
                    });
                    const selectedProducts = JSON.parse(localStorage.getItem('selectedProductsForCheckout')) || [];
                    const cart = JSON.parse(localStorage.getItem('noLoginCart')) || [];
                    selectedProducts.forEach(purchasedItem => {
                        const indexInCart = cart.findIndex(cartItem =>
                            cartItem.idProductLocal === purchasedItem.idProductLocal &&
                            cartItem.color.id === purchasedItem.color.id &&
                            cartItem.size.id === purchasedItem.size.id
                        );

                        if (indexInCart !== -1) {
                            const cartItem = cart[indexInCart];
                            if (purchasedItem.quantity < cartItem.quantity) {
                                cartItem.quantity -= purchasedItem.quantity;
                            } else {
                                cart.splice(indexInCart, 1);
                            }
                        }
                    });
                    localStorage.setItem('noLoginCart', JSON.stringify(cart));
                    localStorage.removeItem('selectedProductsForCheckout');
                    localStorage.removeItem('voucherIDNoLogin');
                    localStorage.removeItem('voucherValueNoLogin');
                    setTimeout(() => {
                        window.location.href = '/';
                    }, 2000);
                })
                .catch(error => {
                    console.error("Lỗi đặt hàng:", error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Thất bại',
                        text: 'Đặt hàng thất bại. Vui lòng thử lại.'
                    });
                });
        }
    });

});

function generateVnpTxnRef() {
    const number = Math.floor(Math.random() * 1000000);
    return number.toString().padStart(6, '0');
}
document.getElementById('vnpay-btn').addEventListener('click' , function (event) {
    const isNameValid = checkName();
    const isPhoneValid = checkPhone();
    const isEmailValid = checkEmail();
    const isProvinceValid = checkProvince();
    const isDistrictValid = checkDistrict();
    const isWardValid = checkWard();
    const isAddressValid = checkFullAddress();
    if (!isNameValid || !isPhoneValid || !isEmailValid || !isProvinceValid || !isDistrictValid || !isWardValid || !isAddressValid) {
        event.preventDefault();
        toastr.error('Vui lòng nhập đủ thông tin!');
        return;
    }
const vnpay_amount = totalShipLocal !== 0 ? totalShipAndItem + totalShipLocal : totalItem || 0;
localStorage.setItem('totalPayment' , vnpay_amount);
localStorage.setItem('totalPrice' , totalItem)
    const payModel = {
        vnp_Amount: vnpay_amount,
        vnp_OrderInfo: "Thanh toán khách khong dang nhap",
        vnp_OrderType: "other",
        vnp_TxnRef: generateVnpTxnRef() ,
        userType: "GUEST"
    };
    const nameCustomer = document.getElementById('nameCustomer').value;
    const phoneCustomer = document.getElementById('numberPhoneCustomer').value;
    const email = document.getElementById('emailCustomer').value;
    const note = document.getElementById('note').value;
    const selectedProductsForCheckout = JSON.parse(localStorage.getItem("selectedProductsForCheckout")) || [];
    const billDetail = selectedProductsForCheckout.map(item => ({
        idProductDetail: item.idProductLocal,
        price: item.price,
        quantity: item.quantity
    }));
    const idVoucher = localStorage.getItem('voucherIDNoLogin')
    let idCustomerPay = 13;
    localStorage.setItem('idUserNoLogin', idCustomerPay);
    localStorage.setItem('userNameNoLogin', nameCustomer);
    localStorage.setItem('noteNoLogin',note);
    localStorage.setItem('phoneNumberNoLogin', phoneCustomer);
    localStorage.setItem('emailNoLogin', email);
    localStorage.setItem('itemDiscountNoLogin', itemDiscountLocal);
    localStorage.setItem('totalItemNoLogin', totalItem);
    localStorage.setItem('moneyShipNoLogin', totalShipLocal);
    localStorage.setItem('billDetailNoLogin', JSON.stringify(billDetail));
    localStorage.setItem('addressNoLogin', fullAddress);
    localStorage.setItem('shipDateNoLogin', dateShip);
    localStorage.setItem('voucherIdNoLogin', idVoucher);
    localStorage.setItem('afterPriceNoLogin', totalShipAndItem + totalShipLocal);
    axios.post('http://localhost:8080/payment-vnpay', payModel)
        .then(response => {
            console.log("Payment URL:", response.data);
            if (response.data) {
                window.location.href = response.data;
            } else {
                console.error("Không nhận được URL thanh toán từ API");
            }
        })
        .catch(error => {
            console.error("Error in payment API:", error);
        });
})

function checkName() {
    const nameCustomer = document.getElementById("nameCustomer").value;
    const nameCustomerError = document.getElementById("nameCustomerError");

    if (!nameCustomer) {
        nameCustomerError.innerText = "Vui lòng nhập tên";
        nameCustomerError.style.display = "block";
        return false;
    } else if (nameCustomer.length < 3) {
        nameCustomerError.innerText = "Vui lòng nhập tên trên 3 ký tự"
        nameCustomerError.style.display = "block";
        return false;
    } else {
        nameCustomerError.innerText = "";
        nameCustomerError.style.display = "none";
        return true;
    }
}

function checkPhone() {
    const phoneCustomer = document.getElementById("numberPhoneCustomer").value;
    const numberPhoneCustomerError = document.getElementById("numberPhoneCustomerError");
    const phoneRegex = /^(?:\+84|84|0)\d{9}$/;
    if (!phoneCustomer) {
        numberPhoneCustomerError.innerText = "Vui lòng nhập số điện thoại";
        numberPhoneCustomerError.style.display = "block";
        return false;
    } else if (!phoneRegex.test(phoneCustomer)) {
        numberPhoneCustomerError.innerText = "Vui lòng đúng định dạng số điện thoại";
        numberPhoneCustomerError.style.display = "block";
        return false;
    } else {
        numberPhoneCustomerError.innerText = "";
        numberPhoneCustomerError.style.display = "none";
        return true;
    }
}

function checkProvince() {
    const provinceSelect = document.getElementById("provinceSelect").value;
    const provinceSelectError = document.getElementById("provinceSelectError");

    if (!provinceSelect) {
        provinceSelectError.innerText = "Vui lòng chọn Tỉnh/Thành phố";
        provinceSelectError.style.display = "block";
        return false;
    } else {
        provinceSelectError.innerText = "";
        provinceSelectError.style.display = "none";
        return true;
    }
}

function checkDistrict() {
    const districtSelect = document.getElementById("districtSelect").value;
    const districtSelectError = document.getElementById("districtSelectError");

    if (!districtSelect) {
        districtSelectError.innerText = "Vui lòng chọn Quận/Huyện";
        districtSelectError.style.display = "block";
        document.getElementById('shipping').innerText = "0 VND";
        document.getElementById('span-totalPayMent').innerText = formatVND(totalItem) + ' VND';
        return false;
    } else {
        districtSelectError.innerText = "";
        districtSelectError.style.display = "none";
        return true;
    }
}

function checkWard() {
    const wardSelect = document.getElementById("wardSelect").value;
    const wardSelectError = document.getElementById("wardSelectError");

    if (!wardSelect) {
        wardSelectError.innerText = "Vui lòng chọn Xã/Phường";
        wardSelectError.style.display = "block";
            document.getElementById('shipping').innerText = "0 VND";
            document.getElementById('span-totalPayMent').innerText = formatVND(totalItem) + ' VND';
        return false;
    } else {
        wardSelectError.innerText = "";
        wardSelectError.style.display = "none";
        return true;
    }
}
function checkFullAddress() {
    const addressValue = document.getElementById('addressValue').value;
    const addressValueError = document.getElementById('addressValueError');
    const nameRegex = /^[a-zA-Z\s]*$/;
    if (!addressValue) {
        addressValueError.innerText = 'Vui lòng nhập địa chỉ cụ thể';
        addressValueError.style.display = 'block';
        return false;
    } else if (addressValue.length < 10) {
        addressValueError.innerText = 'Vui lòng nhập tối thiểu 10 chữ';
        addressValueError.style.display = 'block';
        return false;
    } else {
        addressValueError.innerText = "";
        addressValueError.style.display = 'none';
        return true;
    }
}

function checkEmail(){
    const emailCustomer = document.getElementById("emailCustomer").value;
    const emailCustomerError = document.getElementById("emailCustomerError");
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailCustomer) {
        emailCustomerError.innerText = "Vui lòng nhập email";
        emailCustomerError.style.display = "block";
        return false;
    }else if (!emailRegex.test(emailCustomer)) {
        emailCustomerError.innerText = "Vui lòng nhập đúng định dạng email";
        emailCustomerError.style.display = "block";
        return false;
    }
    else {
        emailCustomerError.innerText = "";
        emailCustomerError.style.display = "none";
        return true;
    }
}

document.getElementById("provinceSelect").addEventListener('change', checkProvince);
document.getElementById("districtSelect").addEventListener('change', checkDistrict);
document.getElementById("wardSelect").addEventListener('change', checkWard);

document.getElementById("nameCustomer").addEventListener('input', checkName);
document.getElementById("numberPhoneCustomer").addEventListener('input', checkPhone);
document.getElementById("addressValue").addEventListener('input', checkFullAddress);

