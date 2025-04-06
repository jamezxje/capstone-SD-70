let idBill = null;
let invoiceCount = 1;
const tabContainer = document.getElementById('tabContainer');
const tabContentContainer = document.getElementById('tabContentContainer');
const listBillId = [];
let invoiceCodeLocal = null;

function createInvoiceTab(invoiceNumber, invoiceData) {
    const allTabs = document.querySelectorAll('.tab');
    console.log("Check count tab", allTabs.length)
    const tab = document.createElement('div');
    tab.classList.add('tab');
    tab.innerHTML = `
        Hóa đơn ${invoiceNumber} 
        <button class="delete-btn" data-id="${invoiceData.id}">X</button>
    `;


    tab.setAttribute('data-tab', invoiceNumber);

    tab.setAttribute('data-id', invoiceData.id);
    tab.setAttribute('data-code', invoiceData.code);
    const btnDeleteBill = tab.querySelector('.delete-btn');
    btnDeleteBill.addEventListener('click', function () {
        const idDelete = btnDeleteBill.getAttribute('data-id');
        deleteBill(idDelete);
    })
    tab.addEventListener('click', function () {
        const selectedInvoiceId = this.getAttribute('data-id');
        const codeSelectBill = this.getAttribute('data-code');
        console.log("Hóa đơn  chọn: ", selectedInvoiceId);
        const tabContent = document.querySelector('.tab-content.active-content');
        const tbody = tabContent.querySelector('tbody');
        tbody.innerHTML = '';
        fetchProductsForAllBills(selectedInvoiceId)
        fetchVouchers(currentPageVoucher);
        console.log("InvoiCoumt", invoiceCount);
        invoiceCodeLocal = codeSelectBill
        idBill = selectedInvoiceId;
        console.log("Check id bill ", idBill);
    });

    async function deleteBill(invoiceId) {
        try {

            const response = await axios.get(`/products/${invoiceId}`, {
                params: {t: new Date().getTime()}
            });
            if (Array.isArray(response.data) && response.data.length > 0) {
                toastr.options.positionClass = 'toast-top-right';
                toastr.error('Không thể xóa hóa đơn này vì có sản phẩm liên quan!');
                return;
            }

            // Nếu không có sản phẩm liên quan, tiếp tục xóa hóa đơn
            await axios.delete(`deleteBill/${invoiceId}`)
                .then(response => {
                    toastr.options.positionClass = 'toast-top-right';
                    toastr.success('Xóa thành công hóa đơn');
                    setTimeout(() => {
                        location.reload();
                    }, 1900);
                })
                .catch(error => {
                    alert("Thao tác xóa thất bại.");
                });
        } catch (error) {
            console.log("Lỗi khi gọi API kiểm tra sản phẩm:", error);
            alert("Lỗi khi kiểm tra dữ liệu.");
        }
    }


    // Thêm tab vào tab container
    tabContainer.appendChild(tab);

    // Tạo nội dung cho tab mới
    const tabContent = document.createElement('div');
    tabContent.classList.add('tab-content');
    let productHTML = '';
    tabContent.innerHTML = `
        <p class="bill-tt">Thông tin hóa đơn ${invoiceData.code}</p>
        <table class="table table-striped" style="margin-top: 0">
            <thead>
                <tr>
                    <th scope="col">STT</th>
                    <th scope="col">Ảnh</th>
                    <th scope="col">Tên sản phẩm</th>
                    <th scope="col">Size</th>
                    <th scope="col">Màu sắc</th>
                    <th scope="col">Số lượng</th>
                    <th scope="col">Đơn giá</th>
                    <th scope="col">Tổng tiền</th>
                    <th scope="col">Hành động</th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    `;
    tabContent.setAttribute('data-tab', invoiceNumber);

    tabContentContainer.appendChild(tabContent);
    tab.addEventListener('click', function () {
        const allTabs = document.querySelectorAll('.tab');
        const allContents = document.querySelectorAll('.tab-content');
        allTabs.forEach(t => t.classList.remove('active-tab'));
        allContents.forEach(c => c.classList.remove('active-content'));

        tab.classList.add('active-tab');
        tabContent.classList.add('active-content');
        restTab();
        revoveLocalStrong();
    });

    if (invoiceNumber === 1) {
        tab.classList.add('active-tab');
        tabContent.classList.add('active-content');
    }

}

async function restTab() {
    const customerName = document.getElementById('customerName');
    const customerEmail = document.getElementById('customerEmail');
    const customerPhone = document.getElementById('customerPhone');
    const nameCustomerAddress = document.getElementById('nameCustomer');
    const numberPhoneAddress = document.getElementById('numberPhoneCustomer');
    const provinceSelect = document.getElementById('provinceSelect');
    const districtSelect = document.getElementById('districtSelect');
    const wardSelect = document.getElementById('wardSelect');
    const addessValue = document.getElementById('addressValue');
    const confirmDay = document.getElementById('confirm-day');
    const customerPayment = document.getElementById('customer-payment');
    const shiing = document.getElementById('shipping')
    const remaining = document.getElementById('remaining-amount');
    const strMissing = document.getElementById('str-missing');
    const totalPrice = document.getElementById('total-price');
    const shipping = document.getElementById('shipping')
    const discount = document.getElementById('discount');
    const reaming = document.getElementById('remaining-amount');
    const totalAmount = document.getElementById('total-amount');
    totalAmount.innerText = '';
    shipping.innerText= '';
    discount.innerText= '';
    reaming.innerText = '';
    totalPrice.innerText = '';
    strMissing.innerText = '';
    remaining.innerText = '';
    shiing.innerText = '';
    customerPayment.innerText = '';
    customerName.innerText = 'Khách lẻ';
    customerEmail.innerText = '';
    customerPhone.innerText = '';
    idCusomter = null;
    nameCustomerAddress.value = '';
    numberPhoneAddress.value = '';
    provinceSelect.value = '';
    districtSelect.value = '';
    wardSelect.value = '';
    addessValue.value = '';
    confirmDay.innerText = '';
    if (checkBox.checked) {
        checkBox.checked = false;
        openAddress.style.display = 'none';
        lastRight.style.display = 'none';
    }

}

const createInvoice = document.getElementById('createInvoiceBill');
createInvoice.addEventListener(
    "click", function () {
        const allTabs = document.querySelectorAll('.tab');
        if (allTabs.length >= 10) {
            toastr.options.positionClass = 'toast-top-right'
            toastr.error('Không tạo quá 10 hóa đơn');
            return;
        }
        console.log("config")
        const id = 1;
        axios.post(`sale-counter/${id}`)
            .then(response => {
                const invoiceData = response.data;
                invoiceCount++
                createInvoiceTab(invoiceCount, response);
                // location.reload();
            })
            .catch(error => {
                console.log(error);
            });

    }
)


var modal = document.getElementById('myModal');
var btn = document.getElementById('openModal');
var closeModal = document.getElementById('closeModal');
btn.onclick = function () {
    modal.style.display = 'block';
    console.log("Check")
}
closeModal.onclick = function () {
    modal.style.display = 'none';
}
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = 'none';
    }
}
var modalCustomer = document.getElementById('modalCustomer');
var btnCustomer = document.getElementById('btnChoseCustomer');
var closeModalCustomer = document.getElementById('closeModalCustomer');
btnCustomer.onclick = function () {
    modalCustomer.style.display = 'block';
}
closeModalCustomer.onclick = function () {
    modalCustomer.style.display = 'none';
}
window.onclick = function (even) {
    if (even.target == modalCustomer) {
        modalCustomer.style.display = 'none'
    }
}
var modalVoucher = document.getElementById('modalVoucher');
var btnVoucher = document.getElementById('btnChoseVoucher');
var closeModalVoucher = document.getElementById('closeModalVoucher');
btnVoucher.onclick = function () {
    modalVoucher.style.display = 'block';
}
closeModalVoucher.onclick = function () {
    modalVoucher.style.display = 'none';
}
window.onclick = function (even) {
    if (even.target == modalVoucher) {
        modalVoucher.style.display = 'none'
    }
}

var modalInput = document.getElementById('myModalInput');
var btnChoseProduct = document.getElementById('choseProduct');
var closeInput = document.getElementById('closeModalInput');

closeInput.onclick = function () {
    modalInput.style.display = 'none';
}

window.onload = function () {
    axios.get(`getAllBill`)
        .then(response => {
            const invoices = response.data;
            invoices.forEach((invoice, index) => {
                createInvoiceTab(index + 1, invoice)
            })
        })
        .catch(error => {
            console.log(error);
        });
    fetchAllProvince();
};
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll('.text-status').forEach(function (statusCell) {
        let statusValue = statusCell.textContent.trim();
        if (statusValue === 'DANG_SU_DUNG') {
            statusCell.textContent = 'Đang Sử Dụng';
        } else if (statusValue === 'NGUNG_SU_DUNG') {
            statusCell.textContent = 'Ngưng Sử Dụng';
        } else {
            statusCell.textContent = "Hết Sản Phẩm";
        }
    });

    document.querySelectorAll('.text-price').forEach(function (priceCell) {
        let priceValue = priceCell.textContent.trim();
        if (priceValue && !isNaN(parseFloat(priceValue))) {
            priceCell.textContent = formatVND(priceValue);
        } else {
            console.error("Giá trị không hợp lệ:", priceValue);
        }
    });
    const printVNPAY = localStorage.getItem('printBillVNPAY');
    if (printVNPAY === 'true') {
        printBillVnPay();
        localStorage.removeItem('printBillVNPAY');
    }
});


let idCusomter = null, name = null, phoneNumber = null, mail = null;
function  attachChoseCustomer() {
    document.querySelectorAll('.btnChose_customer').forEach(button => {
        button.addEventListener('click', function () {
            const id = this.getAttribute('data-id');
            const fullName = this.getAttribute('data-fullname');
            const phone = this.getAttribute('data-phone');
            const email = this.getAttribute('data-email');
            document.getElementById('customerName').innerText = fullName;
            document.getElementById('customerEmail').innerText = email;
            document.getElementById('customerPhone').innerText = phone;
            document.getElementById('modalCustomer').style.display = 'none';
            idCusomter = id;
            name = fullName;
            phoneNumber = phone;
            mail = email;
            fetchAllAddressCustomer(id);
            console.log("Chọn khah thanh công id" , idCusomter )
            toastr.options.positionClass = 'toast-top-right'
            toastr.success('Chọn khách hàng thành công');
        });
    });
}

function openModalInput() {
    const quantityInput = document.getElementById('input-quantity');
    const btnIncrease = document.getElementById('btn-increase');
    const btnReduct = document.getElementById('btn-reduce');
    const confirm = document.getElementById('btn-confirm-product');

    quantityInput.value = 1;

    document.getElementById('myModalInput').style.display = 'block';

    btnIncrease.removeEventListener('click', increaseQuantity);
    btnIncrease.addEventListener('click', increaseQuantity);

    btnReduct.removeEventListener('click', reduceQuantity);
    btnReduct.addEventListener('click', reduceQuantity);

    confirm.removeEventListener('click', confirmProduct);
    confirm.addEventListener('click', confirmProduct);

}

function increaseQuantity() {
    const quantityInput = document.getElementById('input-quantity');
    let currentQuantity = parseInt(quantityInput.value);
    if (!isNaN(currentQuantity)) {
        quantityInput.value = currentQuantity + 1;

    }
}

// Hàm giảm số lượng
let quantityInputChange = 0;

function reduceQuantity() {
    const quantityInput = document.getElementById('input-quantity');
    let currentQuantity = parseInt(quantityInput.value);
    if (!isNaN(currentQuantity) && currentQuantity > 1) {
        quantityInput.value = currentQuantity - 1;

    }
}

// Hàm xác nhận sản phẩm
async function confirmProduct() {
    const quantityInput = document.getElementById('input-quantity');
    let currentQuantity = parseInt(quantityInput.value);
    const maxQuantity = parseInt(document.querySelector('.chose-product[data-product-id="' + idProductD + '"]').getAttribute('data-product-quantity'));

    if (!isNaN(currentQuantity)) {
        if (currentQuantity > maxQuantity) {
            toastr.options.positionClass = 'toast-top-right';
            toastr.error('Số lượng nhập vào vượt quá số lượng hiện tại. Vui lòng nhập lại.');
            return;
        }

        quantityInputChange = currentQuantity;
        console.log("Check currment quantity", currentQuantity);

        billDetails.push({
            idProduct: parseInt(idProductD),
            quantity: quantityInputChange,
            price: parseInt(priceProductD)
        });

        console.log("data send ", billDetails);
        addProductToInvoice1(idProductD, nameProductD, sizeProductD, colorProductD, quantityInputChange, priceProductD , imageD);
        await saveProductInBill(idBill);
        fetchProducts(0);
        toastr.options.positionClass = 'toast-top-right';
        toastr.success('Thêm sản phẩm thành công');
        document.getElementById('myModalInput').style.display = 'none';
    }
}

let idProductD = null, nameProductD = null, sizeProductD = null, colorProductD = null, priceProductD = null , imageD = null;

let idProductDetail = null;
let productForBill = null;

            async function fetchProductsForAllBills(billIds) {

                try {
                    const response = await axios.get(`/products/${billIds}`, {
                        params: {t: new Date().getTime()}
                    });
                    console.log("Dữ liệu trả về từ API:", response.data);
                    idProductDetail = response.data[0].idProductDetail;

                    console.log('Check dữ liệu ', idProductDetail)
                    if (Array.isArray(response.data)) {


                        response.data.forEach(product => {
                            addProductToInvoice(product.id, product.name, product.size, product.color, product.quantity, product.price , product.image);
                        });
                    } else {
                        console.error("Dữ liệu trả về không phải là mảng");
                    }
                } catch (error) {
                    console.log("Lỗi khi gọi API sản phẩm:", error);
                }

}

async function getVoucherInBill(minimumBill) {
    try {
        const response = await axios.get(`/getMinimumBill`, {
            params: {
                minimumBill: minimumBill
            }
        })
        if (isDelivery === false) {
            totalShipLocal = 0;
        }
        console.log("APi minimumBill", response.data);
        console.log("miniMumBill", response.data[0].minimumBill)
        const voucherName = response.data[0].name;
        const voucherID = response.data[0].id;
        const voucherMinimumBill = response.data[0].minimumBill;
        const voucherValue = response.data[0].value;
        voucherValueLocal = voucherValue
        console.log("Name", voucherName, "value", voucherValue)
        document.getElementById('ip-voucher').value = voucherName;
        document.getElementById('customer-payment').innerText = "0đ";
        document.getElementById('remaining-amount').innerText = "0đ";
        applyVoucher(totalBill, voucherID, voucherValueLocal, voucherMinimumBill, totalShipLocal);

    } catch (error) {
        console.log("Api minimumBill error", error)
    }
}

function clearTable() {
    const table = document.getElementById("invoice-table"); // Thay "invoice-table" bằng id bảng của bạn
    const rows = table.querySelectorAll("tr");
    rows.forEach(row => {
        if (row !== table.querySelector("thead")) {
            row.remove(); // Xóa tất cả các hàng trong bảng, trừ header
        }
    });
}

let totalBill = 0;

function addProductToInvoice(id, name, size, color, quantity, price , image) {
    const tabContent = document.querySelector('.tab-content.active-content'); // Bảng chi tiết hóa đơn
    const tbody = tabContent.querySelector('tbody');
    let productExists = false;
    let productTotal = 0;

    // Kiểm tra xem sản phẩm đã có trong bảng chưa
    for (let row of tbody.rows) {
        const rowName = row.cells[2].textContent;
        const rowSize = row.cells[3].textContent;
        const rowColor = row.cells[4].textContent;
        if (rowName === name && rowSize === size && rowColor === color) {
            const rowQuantityCell = row.cells[5];
            const rowPriceCell = row.cells[6];
            const rowTotalCell = row.cells[7];
            let currentQuantity = parseInt(rowQuantityCell.textContent);

            // Cập nhật số lượng
            // rowQuantityCell.textContent = currentQuantity + quantity;

            // Tính lại tổng tiền
            // productTotal = (currentQuantity + quantity) * parseFloat(rowPriceCell.textContent.replace(/[^\d.-]/g, ''));
            // rowTotalCell.textContent = formatVND(productTotal);

            productExists = true;
            break;
        }
    }

    // Nếu sản phẩm chưa có trong bảng, thêm dòng mới
    if (!productExists) {
        const newRow = document.createElement('tr');
        productTotal = quantity * price;
        newRow.setAttribute('data-product-id', id);

        newRow.innerHTML = `
            <td >${tbody.rows.length + 1}</td>
            <td><img src="${image}" alt="${name}" style="width: 50px; height: 50px;"></td>
            <td>${name}</td>
            <td>${size}</td>
            <td>${color}</td>
            <td>${quantity}</td>
            <td>${formatVND(price)}</td>
            <td>${formatVND(productTotal)}</td>
            <td><button onclick="removeProduct(this)" class="remove-product"> <i class="bi bi-trash" style="color: white ; font-size: 15px;"></i></button></td>
        `;

        tbody.appendChild(newRow);
        console.log("Thông tin sản phẩm được lưu trong dòng: ", newRow.product);
    }

    let totalAmount = 0;
    for (let row of tbody.rows) {
        const rowTotalCell = row.cells[7];
        totalAmount += parseFloat(rowTotalCell.textContent.replace(/[^\d.-]/g, ''));  // Loại bỏ ký tự không phải số
    }

    totalBill = totalAmount;

    document.getElementById('total-price').innerText = formatVND(totalAmount) + "đ";
    document.getElementById('total-amount').innerText = formatVND(totalAmount) + "đ";
    document.getElementById('amount').innerText = formatVND(totalAmount) + "đ";
    document.getElementById('input-payment').value = formatVND(totalAmount) + "đ";
    getVoucherInBill(totalBill);
    fetchVouchers(currentPageVoucher);
    console.log('Check totoal Bill', totalBill)
}

function addProductToInvoice1(id, name, size, color, quantity, price , image) {
    const tabContent = document.querySelector('.tab-content.active-content'); // Bảng chi tiết hóa đơn
    const tbody = tabContent.querySelector('tbody');
    let productExists = false;
    let productTotal = 0;

    let products = JSON.parse(localStorage.getItem('invoiceProducts')) || [];
    // Kiểm tra xem sản phẩm đã có trong bảng chưa
    for (let row of tbody.rows) {
        const rowName = row.cells[2].textContent;
        const rowSize = row.cells[3].textContent;
        const rowColor = row.cells[4].textContent;

        if (rowName === name && rowSize === size && rowColor === color) {
            const rowQuantityCell = row.cells[5];
            const rowPriceCell = row.cells[6];
            const rowTotalCell = row.cells[7];
            let currentQuantity = parseInt(rowQuantityCell.textContent);

            // Cập nhật số lượng
            rowQuantityCell.textContent = currentQuantity + quantity;

            productTotal = (currentQuantity + quantity) * parseFloat(rowPriceCell.textContent.replace(/[^\d.-]/g, ''));
            rowTotalCell.textContent = formatVND(productTotal);

            productExists = true;

            const existingProductIndex = products.findIndex(product => product.id === id);
            if (existingProductIndex !== -1) {
                products[existingProductIndex].quantity += quantity;
                products[existingProductIndex].total = products[existingProductIndex].quantity * products[existingProductIndex].price;
            }
            break;
        }
    }

    // Nếu sản phẩm chưa có trong bảng, thêm dòng mới
    if (!productExists) {
        const newRow = document.createElement('tr');
        newRow.setAttribute('data-product-id', id);
        productTotal = quantity * price;
        newRow.innerHTML = `
            <td >${tbody.rows.length + 1}</td>
            <td><img src="${image}" alt="${name}" style="width: 50px; height: 50px;"></td>
            <td>${name}</td>
            <td>${size}</td>
            <td>${color}</td>
            <td>${quantity}</td>
            <td>${formatVND(price)}</td>
            <td>${formatVND(productTotal)}</td>
            <td><button onclick="removeProduct(this)" class="remove-product"> <i class="bi bi-trash" style="color: white ; font-size: 15px;"></i></button></td>
        `;

        tbody.appendChild(newRow);
        const newProduct = {
            id,
            name,
            size,
            color,
            quantity,
            price,
            total: productTotal,
            image
        };
        products.push(newProduct);

        console.log("data-product-id của dòng mới: ", newRow.getAttribute('data-product-id'));
    }
    localStorage.setItem('invoiceProducts', JSON.stringify(products));
    let totalAmount = 0;
    for (let row of tbody.rows) {
        const rowTotalCell = row.cells[7];
        totalAmount += parseFloat(rowTotalCell.textContent.replace(/[^\d.-]/g, ''));  // Loại bỏ ký tự không phải số
    }
    totalBill = totalAmount;
    console.log("Check totalPrice 1" , formatVND(totalAmount))
    document.getElementById('total-price').innerText = formatVND(totalAmount) + "đ";
    document.getElementById('total-amount').innerText = formatVND(totalAmount) + "đ";
    document.getElementById('amount').innerText = formatVND(totalAmount) + "đ";
    document.getElementById('input-payment').value = formatVND(totalAmount) + "đ";
    getVoucherInBill(totalBill);
    fetchVouchers(currentPageVoucher);
    console.log('Check totoal Bill', totalBill)
}


async function removeProduct(button) {
    const row = button.closest('tr');
    const productId = row.getAttribute('data-product-id');

    console.log("ID sản phẩm bị xóa: ", productId);
    console.log('ID bill bị xóa', idBill);

    try {
        await deleteProductInBill(idBill, productId);
        row.remove();
        fetchProducts(0);
    } catch (error) {
        console.error("Lỗi khi xóa sản phẩm:", error);
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

const inputPayment = document.getElementById('input-payment');
inputPayment.addEventListener('input', () => {
    let value = inputPayment.value;
    value = value.replace(/[^\d]/g, '');
    inputPayment.value = formatVND(value);
})

function checkInputPayMent() {
    const inputPayment = document.getElementById("input-payment").value;
    const inputFiltered = inputPayment.replace(/[^0-9,\.]/g, "");
    const numberRegex = /^[0-9,\.]*$/;
    const error = document.getElementById('moneyBackInputError');
    if (!numberRegex.test(inputFiltered)) {
        error.innerText = "Vui lòng không nhập chữ và ký tự đặc biệt";
        error.style.display = 'block';
        return false;
    } else {
        error.innerText = '';
        error.style.display = 'none';
        return true;
    }
}

document.getElementById("input-payment").addEventListener('input', checkInputPayMent);

let billDetails = [];


let voucherValueLocal = null;
let voucherMininumLocal = null;
let voucherNameLocal = null;


function attachChangeVoucher() {
    document.querySelectorAll('.select-voucher-btn').forEach(button => {
        button.addEventListener('click', async function () {
            try {
                const response = await axios.get(`/products/${idBill}`, {
                    params: {t: new Date().getTime()}
                });


                if (!Array.isArray(response.data) || response.data.length === 0) {
                    toastr.options.positionClass = 'toast-top-right';
                    toastr.error('Vui lòng chọn sản phẩm cần mua');
                    return;
                }

                const voucherId = this.getAttribute('data-id');
                const voucherValue = this.getAttribute('data-value');
                const voucherName = this.getAttribute('data-name');
                const voucherMininum = this.getAttribute('data-mininumBill');
                voucherValueLocal = voucherValue;
                voucherMininumLocal = voucherMininum;
                voucherNameLocal = voucherName;
                document.getElementById('ip-voucher').value = voucherName;
                applyVoucher(totalBill, voucherId, voucherValue, voucherMininum, totalShipLocal);
                document.getElementById('modalVoucher').style.display = 'none';
                document.getElementById('shipping').textContent = formatVND(totalShipLocal);
                // toastr.options.positionClass = 'toast-top-right'
                // toastr.success('Chọn voucher thành công');
            } catch (error) {
                toastr.error("Có lỗi xảy ra trong quá trình xử lý!");
                console.error(error);
            }
        })

    })
}


let totalCustomerPayment = 0;
let voucherDetail = [];

function applyVoucher(totalPrice, voucherId, voucherValue, voucherMinium, shipping) {
    let discount = 0;
    console.log("Check voucher", voucherValue)

    console.log("voucher", voucherValue)
    if (totalPrice >= voucherMinium) {
        discount = (totalPrice - voucherValue) + shipping;
    } else if (totalPrice < voucherMinium) {
        alert("Không áp dụng")
        return;
    } else {
        discount = totalPrice + shipping;
    }
    totalCustomerPayment = discount;
    console.log("check total customerpaayment", totalCustomerPayment)
    voucherDetail = [];
    voucherDetail.push({
        idVoucher: voucherId,
        beforVoucher: totalPrice,
        afterVoucher: discount,
        discountVoucher: voucherValue
    })
    localStorage.setItem('idVoucher', voucherId);
    console.log("Check voucher detail push", voucherDetail);
    console.log("Total Customer Payment:", totalCustomerPayment);
    document.getElementById('amount').innerText = formatVND(discount) + "đ";
    document.getElementById('input-payment').value = formatVND(discount) + "đ";
    document.getElementById('discount').innerText = formatVND(voucherValue) + "đ";
    document.getElementById('total-amount').innerText = formatVND(discount) + "đ";


}

const btnpayment = document.getElementById("btnChosePayMent");
const modald = document.getElementById("modalPayment");
const closeModalPaymey = document.getElementById('closeModalPayment');
btnpayment.addEventListener('click', () => {
    modald.style.display = 'block';
})
closeModalPaymey.addEventListener('click', () => {
    modald.style.display = 'none';
})

const btnPaymentSuccess = document.getElementById('btn-payment-success');

btnPaymentSuccess.addEventListener('click', async () => {
    try {
        if (isDelivery === true) {
            const isNameValid = checkName();
            const isPhoneValid = checkPhone();
            // const isDistrictValid = checkDistrict();
            // const isProvinceValid = checkProvince();
            // const isWardValid = checkWard();
        // || !isDistrictValid || !isProvinceValid || !isWardValid
            if (!isNameValid || !isPhoneValid ) {
                toastr.options.positionClass = 'toast-top-right';
                toastr.error('Vui lòng kiểm tra lại các thông tin giao hàng');
                return;
            }
        }
        const response = await axios.get(`/products/${idBill}`, {
            params: {t: new Date().getTime()}
        });
        if (!Array.isArray(response.data) || response.data.length === 0) {
            toastr.options.positionClass = 'toast-top-right';
            toastr.error('Vui lòng chọn sản phẩm cần mua');
            return;
        }

        const customerPay = document.getElementById('customer-payment').innerText.trim();
        console.log("Giá trị thanh toán:", customerPay);
        if (customerPay === "0đ") {
            toastr.options.positionClass = 'toast-top-right';
            toastr.error('Vui lòng chọn phương thức thanh toán');
            return;
        }
        Swal.fire({
            title: 'Xác nhận',
            text: 'Bạn có xác nhận thanh toán không?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Xác nhận',
            cancelButtonText: 'Hủy'
        }).then((result) => {
            if (result.isConfirmed) {
                saveBill(idBill);
                toastr.options.positionClass = 'toast-top-right';
                toastr.options.timeOut = 2000;
                toastr.success('Thanh toán hóa đơn thành công');
                // setTimeout(() => {
                //     location.reload();
                // }, 2200);
            }
        });

    } catch (error) {
        toastr.error("Có lỗi xảy ra trong quá trình xử lý!");
        console.error(error);
    }
});


const modal1 = document.getElementById("confirmationModal");
const openModalBtn = document.getElementById("openModalBtn");
const closeModalBtn = document.getElementById("closeModalBtn");
const confirmBtn = document.getElementById("confirmBtn");
const cancelBtn = document.getElementById("cancelBtn");
let billPrintPay = null;
let customerPayMentInput = null;
const btnPayment = document.getElementById('btn-payment')
btnPayment.addEventListener("click", async () => {
    try {
        const response = await axios.get(`/products/${idBill}`, {
            params: {t: new Date().getTime()}
        });
        if (!Array.isArray(response.data) || response.data.length === 0) {
            toastr.options.positionClass = 'toast-top-right';
            toastr.error('Vui lòng chọn sản phẩm cần mua');
            return;
        }
        Swal.fire({
            title: 'Xác nhận',
            text: 'Bạn có xác nhận không?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Xác nhận',
            cancelButtonText: 'Hủy'
        }).then((result) => {
            if (result.isConfirmed) {
                const btnBank = document.getElementById('btn-bank');
                let totalPayment = document.getElementById('input-payment').value;
                let missing = 0;
                totalPayment = totalPayment.replace(/,/g, '');
                totalPayment = parseFloat(totalPayment);
                customerPayMentInput = totalPayment;
                console.log("Check customerPaymet", customerPayMentInput)
                const strMissing = document.getElementById('str-missing');
                if (isNaN(totalPayment)) {
                    alert("Vui lòng nhập giá trị hợp lệ !");
                    return;
                }
                document.getElementById('customer-payment').innerText = formatVND(totalPayment) + "đ";

                if (voucherValueLocal == null) {
                    console.log("Check total payment", totalPayment);
                    console.log("Check tổng tiền đơn hàng", totalBill);
                    console.log("check shipp null ")
                    console.log("ship", totalShipLocal)
                    const priceBill = totalBill + totalShipLocal;
                    console.log("price", priceBill, "totalpaymet", totalPayment, 'mistsing 1')
                    missing = totalPayment - priceBill;
                } else {
                    console.log("check totalcustoemr 2", totalCustomerPayment)
                    console.log("total ship 2", totalShipLocal)
                    const price = totalCustomerPayment + totalShipLocal;
                    console.log("total paymetn", totalPayment, "price ", price, 'missign 2')
                    missing = totalPayment - price;

                }
                console.log("Check missing ", missing)
                confirmPayemt = missing;
                if (missing < 0) {
                    strMissing.textContent = formatVND(Math.abs(missing) + "đ");
                } else if (missing > 0) {
                    strMissing.textContent = formatVND(missing) + "đ";
                    document.getElementById('missing').textContent = 'Tiền thừa :';
                    strMissing.style.color = 'green';
                } else {
                    strMissing.textContent = "0đ";
                    document.getElementById('missing').textContent = 'Tiền thiếu :';
                    strMissing.style.color = 'red';

                }
            } else {
            }
        });
    } catch (error) {
        toastr.error("Có lỗi xảy ra trong quá trình xử lý!");
        console.error(error);
    }
});


// confirmBtn.addEventListener('click', () => {
//     const btnBank = document.getElementById('btn-bank');
//     let totalPayment = document.getElementById('input-payment').value;
//     let missing = 0;
//     totalPayment = totalPayment.replace(/,/g, '');
//     totalPayment = parseFloat(totalPayment);
//
//     const strMissing = document.getElementById('str-missing');
//     if (isNaN(totalPayment)) {
//         alert("Vui lòng nhập giá trị hợp lệ !");
//         return;
//     }
//     document.getElementById('customer-payment').innerText = formatVND(totalPayment) + "đ";
//
//     if (voucherValueLocal == null) {
//         console.log("pay", totalPayment);
//         console.log("bill", totalBill);
//         console.log("ship", totalShipLocal)
//         const priceBill = totalBill + totalShipLocal;
//         missing = totalPayment - priceBill;
//     } else {
//         missing = totalPayment - totalCustomerPayment;
//     }
//     console.log("Check missing ", missing)
//     confirmPayemt = missing;
//     if (missing < 0) {
//         strMissing.textContent = formatVND(Math.abs(missing) + "đ");
//     } else if (missing > 0) {
//         strMissing.textContent = formatVND(missing) + "đ";
//         document.getElementById('missing').textContent = 'Tiền thừa :';
//         strMissing.style.color = 'green';
//     } else {
//         strMissing.textContent = "0đ";
//         document.getElementById('missing').textContent = 'Tiền thiếu :';
//         strMissing.style.color = 'red';
//
//     }
//     modal1.style.display = 'none';
// })
const btnSuccess = document.getElementById('btn-success');
btnSuccess.addEventListener('click', () => {

    if (confirmPayemt < 0) {
        document.getElementById('remaining-amount').innerText = formatVND(Math.abs(confirmPayemt) + "đ");
    } else if (confirmPayemt > 0) {
        document.getElementById('str-remaining').textContent = 'Tiền thừa :';
        document.getElementById('remaining-amount').innerText = formatVND(confirmPayemt) + "đ"
    } else {
        document.getElementById('remaining-amount').innerText = formatVND(confirmPayemt) + "đ"
    }
    document.getElementById('modalPayment').style.display = 'none';

})


closeModalBtn.addEventListener("click", () => {
    modal1.style.display = "none";
});

cancelBtn.addEventListener("click", () => {
    modal1.style.display = "none";
});

const checkBox = document.getElementById('toggleButton');
const lastRight = document.getElementById('last-right');
const openAddress = document.getElementById('btn-address');
// Khi trang tải lên, mặc định last-right ẩn đi
lastRight.style.display = 'none';
let isDelivery = null;
checkBox.addEventListener('change', () => {
    if (checkBox.checked) {
        openAddress.style.display = 'block';
        isDelivery = checkBox.checked;
        lastRight.style.display = 'block';
    } else {
        openAddress.style.display = 'none';
        isDelivery = checkBox.checked;
        lastRight.style.display = 'none';
        document.getElementById('shipping').innerText = "0đ";

        document.getElementById('total-amount').innerText = formatVND(totalBill);
        document.getElementById('amount').innerText = formatVND(totalBill);
        document.getElementById('input-payment').value = formatVND(totalBill)
        const nameCustomerAddress = document.getElementById('nameCustomer');
        const numberPhoneAddress = document.getElementById('numberPhoneCustomer');
        const provinceSelect = document.getElementById('provinceSelect');
        const districtSelect = document.getElementById('districtSelect');
        const wardSelect = document.getElementById('wardSelect');
        const addessValue = document.getElementById('addressValue');
        const confirmDay = document.getElementById('confirm-day');
        nameCustomerAddress.value = '';
        numberPhoneAddress.value = '';
        provinceSelect.value = '';
        districtSelect.value = '';
        wardSelect.value = '';
        addessValue.value = '';
        confirmDay.innerText = '';
        getVoucherInBill(totalBill)
    }
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
    if (provinceName === "Chọn") {
        document.getElementById("provinceSelectError").style.display = "block";
        document.getElementById("provinceSelectError").innerText = "Vui lòng chọn tỉnh/thành phố!";
        return false;
    } else {
        document.getElementById("provinceSelectError").style.display = "none";

    }
    if (provinceId) {
        fetchProvinceDistricts(provinceId);
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
    } else {
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
                document.getElementById('shipping').innerText = formatVND(totalShip.total) + "đ";
                priceAmountBillAndShipNoVoucher = totalBill + totalShipLocal - voucherValueLocal;
                document.getElementById('total-amount').innerText = formatVND(priceAmountBillAndShipNoVoucher) + "đ";
                document.getElementById('amount').innerText = formatVND(priceAmountBillAndShipNoVoucher);
                document.getElementById('input-payment').value = formatVND(priceAmountBillAndShipNoVoucher)
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

async function saveProductInBill(id) {
    const billData = {
        billDetails: billDetails
    };
    console.log("Check data bill", billData)
    try {
        const response = await axios.post(`/save-product-bill/${id}`, billData);
        console.log("InProductBill", response);
        billDetails = [];
        console.log("Rest BillLDetail", billDetails)
        return response;
    } catch (error) {
        console.error("Lỗi khi lưu sản phẩm vào hóa đơn", error);
        throw error;
    }
}

async function deleteProductInBill(idBill, idProduct) {
    console.log("Xóa id Bill", idBill, "Xóa id product", idProduct);
    try {
        const response = await axios.delete(`/delete-product-bill/${idBill}/${idProduct}`);
        console.log("Delete Success", response);
        return response.data;
    } catch (error) {
        console.error("Error deleting product:", error);
        throw error;
    }
}


function saveBill(id) {
    const nameCustomer = document.getElementById('nameCustomer').value ;
    const numberCusomter = document.getElementById('numberPhoneCustomer').value;
    if (voucherValueLocal == null) {
        voucherValueLocal = 0; // Gán giá trị mặc định 0 nếu voucherNameLocal là null
    }
    console.log("Check date ship ", dateShip)
    console.log("voucherDetail:", voucherDetail);
    let changeIDCustomer = idCusomter || 3;
    const billData = {
        idUser: changeIDCustomer,
        userName: nameCustomer,
        note: "Office",
        phoneNumber: numberCusomter,
        email: mail,
        openDelivery: isDelivery,
        itemDiscount: voucherValueLocal,
        totalMoney: customerPayMentInput -totalShipLocal,
        moneyShip: totalShipLocal,
        type: 'OFFLINE',
        address: fullAddress,
        deliveryDate: dateShip,
        voucherDetails: voucherDetail
    }
    console.log("Bill data", billData)
    axios.post(`/save-bill/${id}`, billData)
        .then(response => {
            console.log("gửi thành công")
            printBill(billData)
        })
        .catch(error => {
            console.log("Có lỗi xảy ra", error)
        })

}

function  printBillVnPay() {
    const code = localStorage.getItem('code');
    const name = localStorage.getItem('name');
    const phone = localStorage.getItem('phone');
    const userName = localStorage.getItem('userName');
    const totalPrice = localStorage.getItem('totalPrice');
    const totalPayment = localStorage.getItem('totalPayment');
    const phoneNumber = localStorage.getItem('phoneNumber');
    const itemDiscount = localStorage.getItem('itemDiscount');
    const moneyShip = localStorage.getItem('moneyShip');
    const address = localStorage.getItem('address');
    const deliveryDate = localStorage.getItem('deliveryDate');
    document.getElementById('printCode').innerText = code;
    document.getElementById('printCreateDate').innerText = new Date().toLocaleDateString('vi-VN');
document.getElementById('printNameCustomer').innerText = name || 'Khách lẻ';
document.getElementById('printNumberPhoneCustomer').innerText = phone || '';

    document.getElementById('printTotal').innerText = formatVND(totalPrice) || '0đ';
    document.getElementById('printDiscount').innerText = formatVND(itemDiscount) || '0đ';
    document.getElementById('printShip').innerText = formatVND(moneyShip) || '0đ';

    document.getElementById('printTotalPayment').innerText = formatVND(totalPayment) || '0đ';
    document.getElementById('printCustomerPayment').innerText = formatVND(totalPayment) || '0đ';
    document.getElementById('printMethod').innerText = 'VNPAY';

    if (!deliveryDate || deliveryDate === "null" || deliveryDate === "") {
        document.getElementById('p-printShipDay').style.display = 'none';
        document.getElementById('printInforShip').style.display = 'none';
        document.getElementById('p-printNameShip').style.display = 'none';
        document.getElementById('p-printPhoneShip').style.display = 'none'
        document.getElementById('p-printAddress').style.display = 'none'

    } else {
        document.getElementById('printShipDay').innerText = deliveryDate;
        document.getElementById('printNameShip').innerText = userName;
        document.getElementById('printPhoneShip').innerText = phoneNumber;
        document.getElementById('printAddress').innerText = address || '';
        document.getElementById('p-printShipDay').style.display = 'block';
        document.getElementById('printInforShip').style.display = 'block';
    }
    const printTableBody = document.querySelector('#printable-content #ttt tbody');
    printTableBody.innerHTML = '';

    const products = JSON.parse(localStorage.getItem('invoiceProducts')) || [];

    products.forEach((product, index) => {
        const newRow = document.createElement('tr');
        newRow.innerHTML = `
            <td>${index + 1}</td>
            <td>${product.name}</td>
            <td>${product.size}</td>
            <td>${product.color}</td>
            <td>${product.quantity}</td>
            <td>${formatVND(product.price)}</td>
            <td>${formatVND(product.total)}</td>
        `;
        printTableBody.appendChild(newRow);
    });
    localStorage.removeItem('invoiceProducts')
    printJS({
        printable: 'printable-content',
        type: 'html', // Loại in là HTML
        header: 'Hóa Đơn Thanh Toán',
        style: `
            /* Đảm bảo tất cả CSS in ấn được áp dụng */
            @media print {
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 0;
                }
                #printable-content {
                    display: block !important;
                    padding: 20px;
                    width: 100%;
                    box-sizing: border-box;
                }
                table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-bottom: 20px;
                }
                th, td {
                    border: 1px solid #ddd;
                    padding: 8px;
                    text-align: left;
                }
                th {
                    background-color: #f4f4f4;
                }
                .rightCustomer {
            margin: 0;
            padding: 0;
            }  
                p {
                    font-size: 16px;
                    margin: 5px 0;
                }
                * {
                    -webkit-print-color-adjust: exact !important;
                    print-color-adjust: exact !important;
                }
            }
        `
    });

    document.getElementById("printable-content").style.display = 'none';
    setTimeout(() => {
        if (!document.hidden) {
            location.reload();
        }
    }, 1000);
}

function printBill(billData) {
    console.log('Check configMayMent', confirmPayemt)
    const printBack = document.getElementById('printBack');
    if (confirmPayemt < 0) {
        printBack.textContent = formatVND(Math.abs(confirmPayemt)) + 'đ';
    } else if (confirmPayemt > 0) {
        printBack.innerText = formatVND(confirmPayemt) + 'đ';
    } else {
        printBack.textContent = '0đ';
    }
    const phoneCustomer = document.getElementById("numberPhoneCustomer").value;
    const nameCustomer = document.getElementById("nameCustomer").value;
    document.getElementById('printCode').innerText = invoiceCodeLocal || 'Chưa có mã';
    document.getElementById('printCreateDate').innerText = new Date().toLocaleDateString('vi-VN');
    // document.getElementById('printStaff').innerText = billData.idUser || 'Nhân viên chưa có';
    document.getElementById('printNameCustomer').innerText = name || 'Khách lẻ';
    document.getElementById('printNumberPhoneCustomer').innerText = phoneNumber || '';
    document.getElementById('printAddress').innerText = fullAddress || '';
    document.getElementById('printTotal').innerText = formatVND(totalBill) || '0đ';
    document.getElementById('printDiscount').innerText = formatVND(billData.itemDiscount) || '0đ';
    document.getElementById('printShip').innerText = formatVND(billData.moneyShip) || '0đ';

    document.getElementById('printTotalPayment').innerText =
        totalShipLocal !== 0 && totalShipLocal ? formatVND(priceAmountBillAndShipNoVoucher) : formatVND(totalCustomerPayment) || '0đ';

    document.getElementById('printCustomerPayment').innerText = formatVND(customerPayMentInput) || '0đ';
    document.getElementById('printBack').innerText = formatVND(confirmPayemt) || '0đ';
    if (dateShip === null) {
        document.getElementById('p-printShipDay').style.display = 'none';
        document.getElementById('printInforShip').style.display = 'none';
        document.getElementById('p-printNameShip').style.display = 'none';
        document.getElementById('p-printPhoneShip').style.display = 'none'
        document.getElementById('p-printAddress').style.display = 'none'

    } else {
        document.getElementById('printShipDay').innerText = dateShip;
        document.getElementById('printNameShip').innerText = nameCustomer;
        document.getElementById('printPhoneShip').innerText = phoneCustomer;
        document.getElementById('printAddress').innerText = fullAddress || '';
        document.getElementById('p-printShipDay').style.display = 'block';
        document.getElementById('printInforShip').style.display = 'block';

    }
    const printTableBody = document.querySelector('#printable-content #ttt tbody');
    printTableBody.innerHTML = '';
    const tabContent = document.querySelector('.tab-content.active-content');
    const tbody = tabContent.querySelector('tbody');

    for (let row of tbody.rows) {
        const rowName = row.cells[2].textContent;
        const rowSize = row.cells[3].textContent;
        const rowColor = row.cells[4].textContent;
        const rowQuantity = row.cells[5].textContent;
        const rowPrice = row.cells[6].textContent;
        const totalPrice = row.cells[7].textContent;

        // Tạo dòng mới trong bảng in hóa đơn
        const newRow = document.createElement('tr');
        newRow.innerHTML = `
            <td>${row.cells[0].textContent}</td>
            <td>${rowName}</td>
            <td>${rowSize}</td>
            <td>${rowColor}</td>
            <td>${rowQuantity}</td>
            <td>${rowPrice}</td>
            <td>${totalPrice}</td>
            
        `;

        // Thêm dòng mới vào bảng
        printTableBody.appendChild(newRow);
    }

    printJS({
        printable: 'printable-content',
        type: 'html', // Loại in là HTML
        header: 'Hóa Đơn Thanh Toán',
        style: `
            /* Đảm bảo tất cả CSS in ấn được áp dụng */
            @media print {
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 0;
                }
                #printable-content {
                    display: block !important;
                    padding: 20px;
                    width: 100%;
                    box-sizing: border-box;
                }
                table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-bottom: 20px;
                }
                th, td {
                    border: 1px solid #ddd;
                    padding: 8px;
                    text-align: left;
                }
                th {
                    background-color: #f4f4f4;
                }
                p {
                    font-size: 16px;
                    margin: 5px 0;
                }
                * {
                    -webkit-print-color-adjust: exact !important;
                    print-color-adjust: exact !important;
                }
            }
        `
    });

    document.getElementById("printable-content").style.display = 'none';
    setTimeout(() => {
        if (!document.hidden) {
            location.reload();
        }
    }, 1000);
}


let addressCustomer = {};

function fetchAllAddressCustomer(idCustomer) {
    axios.get(`/address-user/${idCustomer}`)
        .then(response => {
            const data = response.data;
            addressCustomer = data;

            console.log("data", data)
            displayAddress(data);
        })
        .catch(error => {
            console.log("Check error", error);
        });
}

let idDistrictChose = null, idWardCodeChose = null;
function displayAddress(addresses) {
    const addressListContainer = document.getElementById('addressList');

    if (!addressListContainer) {
        console.log("Không tìm thấy phần tử addressList");
        return;
    }

    if (!Array.isArray(addresses)) {
        console.log("Dữ liệu không phải là mảng");
        return;
    }

    addresses.sort((a, b) => {
        if (a.status === 'DANG_SU_DUNG' && b.status !== 'DANG_SU_DUNG') return -1;
        if (b.status === 'DANG_SU_DUNG' && a.status !== 'DANG_SU_DUNG') return 1;
        return 0;
    });

    addresses.forEach(address => {
        const fullName = address.fullName || 'Không có tên';
        console.log('address', address.fullName);
        const addressItem = document.createElement('div');
        addressItem.classList.add('address-item');

        const status = address.status === 'DANG_SU_DUNG' ? 'Đang sử dụng' : address.status;

        addressItem.innerHTML = `
            <div class="address-container">
                <div class="address-left">
                    <p><strong style="color: #0c85d0"><i class="bi bi-geo-alt"></i></strong><strong> Họ và tên: </strong><strong>${address.fullName}</strong> | <strong>Số điện thoại: </strong><strong>${address.phoneNumber}</strong></p>
                    <p style="color: black">Địa chỉ: ${address.line}, ${address.district}, ${address.province}, ${address.ward}</p>
                    ${address.status === 'DANG_SU_DUNG' ? `<p style="border-radius: 5px;color: orange ; border: 1px solid orange; width: 140px;padding: 4px; text-align: center;">${status}</p>` : ''}
                </div>
                <div class="address-right">
                    <button class="btnChoseAddress"
                        data-id="${address.id}"
                        data-fullname="${address.fullName}"
                        data-phone="${address.phoneNumber}" 
                        data-address="${address.line}"
                        data-province="${address.provinceId}" 
                        data-district="${address.districtId}" 
                        data-ward="${address.wardCode}">Chọn</button>
                </div>
            </div>
        `;

        // Thêm phần tử địa chỉ vào container
        addressListContainer.appendChild(addressItem);
    });

    document.querySelectorAll('.btnChoseAddress').forEach(button => {
        button.addEventListener('click', function () {
            // Lấy thông tin từ thuộc tính data-* của button
            const fullName = this.getAttribute('data-fullname');
            const phoneNumber = this.getAttribute('data-phone');
            const province = this.getAttribute('data-province');
            const district = this.getAttribute('data-district');
            const ward = this.getAttribute('data-ward');
            const address = this.getAttribute('data-address');

            // Điền vào các trường trong form
            console.log("Tỉnh", province)
            console.log("Huyen", district)
            console.log("xa", ward)
            idDistrictChose = district;
            idWardCodeChose = ward;

            fetchProvinceDistricts(province)
            fetchProvinceWard(district)
            document.getElementById('nameCustomer').value = fullName;
            document.getElementById('numberPhoneCustomer').value = phoneNumber;
            document.getElementById('provinceSelect').value = province;
            document.getElementById('addressValue').value = address;
            document.getElementById('provinceSelect').dispatchEvent(new Event('change'));
            document.getElementById('districtSelect').dispatchEvent(new Event('change'));
            document.getElementById('wardSelect').dispatchEvent(new Event('change'));
            fetchDayShip(district, ward);
            fetchMoneyShip(district, ward, 1)
            document.getElementById('modalAddress').style.display = 'none';
            toastr.options.positionClass = 'toast-top-right'
            toastr.success('Chọn địa chỉ thành công');
        });
    });
}


document.getElementById('btn-address').addEventListener('click', () => {
    document.getElementById('modalAddress').style.display = 'block'
})
document.getElementById('closeModalAddress').addEventListener('click', () => {
    document.getElementById('modalAddress').style.display = 'none';
})
document.getElementById('btn-exit-product').addEventListener('click', () => {
    document.getElementById('myModalInput').style.display = 'none';
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
    let districtSelect = document.getElementById("districtSelect").value;
    const districtSelectError = document.getElementById("districtSelectError");
    if (!idDistrictChose ) {
        console.log("chay 1")
        districtSelectError.innerText = "Vui lòng chọn Quận/Huyện";
        districtSelectError.style.display = "block";
        return false;
    }

    else {
        districtSelectError.innerText = "";
        districtSelectError.style.display = "none";
        return true;
    }
}

function checkWard() {
    let wardSelect = document.getElementById("wardSelect").value;
    const wardSelectError = document.getElementById("wardSelectError");
    if (!idWardCodeChose ) {
        wardSelectError.innerText = "Vui lòng chọn xã/phường";
        wardSelectError.style.display = "block";
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
    }  else {
        addressValueError.innerText = "";
        addressValueError.style.display = 'none';
        return true;
    }
}
// document.getElementById("provinceSelect").addEventListener('change', checkProvince);
// document.getElementById("districtSelect").addEventListener('change', checkDistrict);
// document.getElementById("wardSelect").addEventListener('change', checkWard);

document.getElementById("nameCustomer").addEventListener('input', checkName);
document.getElementById("numberPhoneCustomer").addEventListener('input', checkPhone);
document.getElementById("addressValue").addEventListener('input', checkFullAddress);

function attachChooseProductEvent() {
    document.querySelectorAll('.chose-product').forEach(button => {
        button.addEventListener('click', function () {
            console.log("Check th gọi add product");

            const productId = this.getAttribute('data-product-id');
            const productName = this.getAttribute('data-product-name');
            const productSize = this.getAttribute('data-product-size');
            const productColor = this.getAttribute('data-product-color');
            const productQuantity = this.getAttribute('data-product-quantity');
            const productPrice = this.getAttribute('data-product-price');
            const image = this.getAttribute('data-product-image');
            console.log("Check product id", productId)
            console.log("Check price", productPrice)
            console.log('Check quantity', productQuantity)
            idProductD = productId;
            nameProductD = productName;
            sizeProductD = productSize;
            colorProductD = productColor;
            priceProductD = productPrice;
            imageD = image;
            openModalInput();

            console.log("Check input chose quantity", quantityInputChange);
        });
    });
}

let currentPage1 = 0;
const pageSize1 = 5;

function fetchProducts(page = 0) {

    axios.get(`http://localhost:8080/getAllProduct?page=${page}&size=${pageSize1}`)
        .then((response) => {
            const data = response.data;
            console.log("Check data product" , data)
            const products = data.content;
            const totalPages = data.totalPages;
            updateProductTable(products);
            updatePaginationProduct(totalPages, page);
        })
        .catch((error) => {
            console.error("Lỗi khi lấy sản phẩm:", error);
        });
}
function updateProductTable(products) {
    const tbody = document.querySelector('#productTable tbody');
    tbody.innerHTML = '';  // Xóa dữ liệu cũ

    products.forEach((productDetail, index) => {
        let statusProduct = productDetail.status;
        if (statusProduct === "DANG_SU_DUNG") {
            statusProduct = "Đang sử dụng";
        } else {
            statusProduct = "Hết sản phẩm";
        }

        // Tạo một dòng mới trong bảng
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${index + 1}</td>
              <td><img src="${productDetail.image}" alt="${productDetail.name}" style="width: 50px; height: 50px;"></td>
            <td>${productDetail.code}</td>
            <td>${productDetail.name}</td>
            <td>${productDetail.categoryName}</td>
            <td>${productDetail.brandName}</td>
            <td>${productDetail.colorName}</td>
            <td>${productDetail.materialName}</td>
            <td>${productDetail.sizeName}</td>
            <td>${productDetail.gender}</td>
            <td>${productDetail.quantity}</td>
            <td>${formatVND(productDetail.price)}</td>
            <td>
                <button class="chose-product"
                    data-product-id="${productDetail.id}"
                    data-product-name="${productDetail.name}"
                    data-product-size="${productDetail.sizeName}"
                    data-product-color="${productDetail.colorName}"
                    data-product-quantity="${productDetail.quantity}"
                    data-product-price="${productDetail.price}"
                    data-product-image ="${productDetail.image}">
                    <i class="bi bi-chevron-down"></i>
                </button>
            </td>
        `;

        tbody.appendChild(row);
    });

    attachChooseProductEvent();
}
function updatePaginationProduct(totalPages, currentPage1) {
    const paginationDiv = document.getElementById('pagination1');
    paginationDiv.innerHTML = '';

    if (currentPage1 > 0) {
        const prevButton = document.createElement('li');
        prevButton.classList.add('page-item');
        prevButton.innerHTML = `
            <a class="page-link" href="#" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
                <span class="sr-only">Previous</span>
            </a>
        `;
        prevButton.onclick = () => fetchProducts(currentPage1 - 1);
        paginationDiv.appendChild(prevButton);
    }

    for (let i = 0; i < totalPages; i++) {
        const pageButton = document.createElement('li');
        pageButton.classList.add('page-item');
        if (i === currentPage1) {
            pageButton.classList.add('active');
        }
        pageButton.innerHTML = `
            <a class="page-link" href="#">${i + 1}</a>
        `;
        pageButton.onclick = () => fetchProducts(i);
        paginationDiv.appendChild(pageButton);
    }

    if (currentPage1 < totalPages - 1) {
        const nextButton = document.createElement('li');
        nextButton.classList.add('page-item');
        nextButton.innerHTML = `
            <a class="page-link" href="#" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
                <span class="sr-only">Next</span>
            </a>
        `;
        nextButton.onclick = () => fetchProducts(currentPage1 + 1);
        paginationDiv.appendChild(nextButton);
    }
}

fetchProducts(currentPage1);



document.getElementById('btn-bank').addEventListener('click', function () {
    const vnp_Amount = totalShipLocal !== 0 && totalShipLocal ? priceAmountBillAndShipNoVoucher : totalCustomerPayment || 0;
    localStorage.setItem('totalPayment' , vnp_Amount)
    localStorage.setItem('totalPrice' , totalBill)
    const payModel = {
        vnp_Amount: vnp_Amount,
        vnp_OrderInfo: "Thanh toán cho đơn hàng",
        vnp_OrderType: "other",
        vnp_TxnRef: invoiceCodeLocal
    };
    const nameCustomer = document.getElementById('nameCustomer').value;
    const phoneCustomer = document.getElementById('numberPhoneCustomer').value;
    let idCustomerPay = idCusomter || "13";
    // let billData = {
    //     code : invoiceCodeLocal ,
    //     name : name ,
    //     phoneNumber : phoneNumber,
    //     nameCustomer : nameCustomer ,
    //     phoneCustomer : phoneCustomer ,
    //     fullAddress : fullAddress ,
    //     dateShip : dateShip ,
    //     totalBill : totalBill,
    //     itemDiscount: voucherValueLocal ,
    //     moneyShip : totalShipLocal,
    // }
    // localStorage.setItem('billDataaaaa', JSON.stringify(billData));
    localStorage.setItem('idUser', idCustomerPay);
    localStorage.setItem('name' , name);
    localStorage.setItem('phone' , phoneNumber)
    localStorage.setItem('userName', nameCustomer);
    localStorage.setItem('note', "Office");
    localStorage.setItem('phoneNumber', phoneCustomer);
    localStorage.setItem('code', invoiceCodeLocal);
    localStorage.setItem('email', mail);
    localStorage.setItem('openDelivery', isDelivery);
    localStorage.setItem('itemDiscount', voucherValueLocal);
    localStorage.setItem('moneyShip', totalShipLocal);
    localStorage.setItem('type', 'OFFLINE');
    localStorage.setItem('address', fullAddress);
    localStorage.setItem('deliveryDate', dateShip);
    localStorage.setItem('voucherDetails', JSON.stringify(voucherDetail));
    localStorage.setItem('idBill', idBill)

    console.log("Check payModal", payModel)
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
});




document.getElementById('closeModalADDCustomer').style.display = 'none'
document.getElementById('btnAddCacel').addEventListener('click', () => {
    document.getElementById('myModalADDCustomer').style.display = 'none';
})
document.getElementById('btnAddForm').addEventListener('click', function () {
    document.getElementById('myModalADDCustomer').style.display = 'block'
})

document.getElementById('btnAdd').addEventListener('click', function () {
    const nameCustomerNew = document.getElementById('inputAddNameCustomer').value;
    const phoneCustomerNew = document.getElementById('inputAddPhoneCustomer').value;
    const emailCustomerNew = document.getElementById('inputAddEmailCustomer').value;
    const nameError = document.getElementById('inputErrrorName');
    const phoneError = document.getElementById('inputErrrorPhone');
    const emailError = document.getElementById('inputErrrorEmail');

    let isValid = true;

    if (nameCustomerNew.trim() === "") {
        nameError.style.display = 'block';
        nameError.innerText = "Vui lòng nhập tên";
        isValid = false;
    } else {
        nameError.style.display = 'none';
    }

    if (phoneCustomerNew.trim() === "") {
        phoneError.style.display = 'block';
        phoneError.innerText = "Vui lòng nhập số điện thoại";
        isValid = false;
    } else if (!/^\d{10}$/.test(phoneCustomerNew)) {
        phoneError.style.display = 'block';
        phoneError.innerText = "Số điện thoại không hợp lệ";
        isValid = false;
    } else {
        phoneError.style.display = 'none';
    }

    if (emailCustomerNew.trim() === "") {
        emailError.style.display = 'block';
        emailError.innerText = "Vui lòng nhập email";
        isValid = false;
    } else if (!/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test(emailCustomerNew)) {
        emailError.style.display = 'block';
        emailError.innerText = "Email không hợp lệ";
        isValid = false;
    } else {
        emailError.style.display = 'none';
    }
    if (!isValid) {
        return;
    }
    const createCustomer = {
        customerName: nameCustomerNew,
        customerEmail: emailCustomerNew,
        customerPhone: phoneCustomerNew
    }
    Swal.fire({
        title: 'Xác nhận',
        text: 'Bạn có xác nhận thêm khách hàng không?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            createCustomerBill(createCustomer)
                .then((response) => {
                    // Thành công
                    toastr.options.positionClass = 'toast-top-right';
                    toastr.options.timeOut = 2000;
                    toastr.success('Thêm khách hàng thành công');
                    document.getElementById('myModalADDCustomer').style.display = 'none';
                    document.getElementById('inputAddNameCustomer').value = '';
                    document.getElementById('inputAddPhoneCustomer').value = '';
                    document.getElementById('inputAddEmailCustomer').value = '';
                    fetchCustomers(currentPage);
                })
                .catch((error) => {
                    toastr.options.positionClass = 'toast-top-right';
                    toastr.options.timeOut = 2000;
                    toastr.error(error);
                });
        }
    });
});

function createCustomerBill(createCustomerBill) {
    return axios.post('/createCustomerBill', createCustomerBill)
        .then(response => {
            console.log("Create customer success", response.data);
            return response;
        })
        .catch(error => {
            console.log("Fail create customer", error);
            if (error.response && error.response.data && error.response.data.error) {
                const errorMessage = error.response.data.error;
                console.log("Check error", errorMessage);
                return Promise.reject(errorMessage);
            } else {
                return Promise.reject("Có lỗi khác!");
            }
        });

}

let currentPage = 0;
const pageSize = 5;

function fetchCustomers(page = 0) {
    axios.get(`http://localhost:8080/customerPage?page=${page}&size=${pageSize}`)
        .then(response => {
            const data = response.data;
            const customers = data.content;
            const totalPages = data.totalPages;

            const customerTableBody = document.getElementById('customerTable');
            customerTableBody.innerHTML = '';
            customers.forEach((customer, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                        <td>${index + 1}</td>
                        <td>${customer.fullName}</td>
                        <td>${customer.numberPhone}</td>
                        <td>${customer.email}</td>
                        <td>
                            <button class="btnChose_customer" data-id="${customer.id}" data-fullName="${customer.fullName}" data-phone="${customer.numberPhone}" data-email="${customer.email}">
                                <i class="bi bi-chevron-down"></i>
                            </button>
                        </td>
                    `;
                customerTableBody.appendChild(row);
            });

            updatePagination(page, totalPages);
            attachChoseCustomer();
        })
        .catch(error => {
            console.error('Error fetching customers:', error);
        });
}


function updatePagination(currentPage, totalPages) {
    const paginationDiv = document.getElementById('pagination');
    paginationDiv.innerHTML = '';

    if (currentPage > 0) {
        const prevButton = document.createElement('li');
        prevButton.classList.add('page-item');
        prevButton.innerHTML = `
                <a class="page-link" href="#" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                    <span class="sr-only">Previous</span>
                </a>
            `;
        prevButton.onclick = () => fetchCustomers(currentPage - 1);
        paginationDiv.appendChild(prevButton);
    }

    for (let i = 0; i < totalPages; i++) {
        const pageButton = document.createElement('li');
        pageButton.classList.add('page-item');
        if (i === currentPage) {
            pageButton.classList.add('active');
        }
        pageButton.innerHTML = `
                <a class="page-link" href="#">${i + 1}</a>
            `;
        pageButton.onclick = () => fetchCustomers(i);
        paginationDiv.appendChild(pageButton);
    }

    if (currentPage < totalPages - 1) {
        const nextButton = document.createElement('li');
        nextButton.classList.add('page-item');
        nextButton.innerHTML = `
                <a class="page-link" href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                    <span class="sr-only">Next</span>
                </a>
            `;
        nextButton.onclick = () => fetchCustomers(currentPage + 1);
        paginationDiv.appendChild(nextButton);
    }

}

fetchCustomers(currentPage);

function revoveLocalStrong() {
    localStorage.removeItem('idUser');
    localStorage.removeItem('userName');
    localStorage.removeItem('note');
    localStorage.removeItem('phoneNumber');
    localStorage.removeItem('email');
    localStorage.removeItem('openDelivery');
    localStorage.removeItem('itemDiscount');
    localStorage.removeItem('moneyShip');
    localStorage.removeItem('type');
    localStorage.removeItem('address');
    localStorage.removeItem('deliveryDate');
    localStorage.removeItem('voucherDetails');
}
let currentPageVoucher = 0;
const pageSizeVoucher = 5;

function fetchVouchers(page = 0) {
    const totalAmount = totalBill;
    console.log("Check totalamount" , totalAmount)
    axios.get(`/getAllVoucher?totalAmount=${totalAmount}&page=${page}&size=${pageSizeVoucher}`)
        .then((response) => {
            const data = response.data;
            console.log("Data voucher" , data)
            const vouchers = data.content;
            const totalPages = data.totalPages;
            updateVoucherTable(vouchers);
            updatePaginationVoucher(totalPages, page);
        })
        .catch((error) => {
            console.error("Lỗi khi lấy danh sách voucher:", error);
        });
}

function updateVoucherTable(vouchers) {
    const tbody = document.querySelector('#voucherTable tbody');
    tbody.innerHTML = '';
    let formatINT = (amount) => {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    };

    vouchers.forEach((voucher, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${index + 1}</td>
            <td>${voucher.name}</td>
            <td>${formatINT(voucher.value)}</td>
            <td>${formatINT(voucher.minimumBill)}</td>
                <td>${voucher.quantity}</td>
            <td>${new Date(voucher.startDate).toLocaleDateString()}</td>
            <td>${new Date(voucher.endDate).toLocaleDateString()}</td>
            <td>
                <button class="select-voucher-btn"
                        data-id="${voucher.id}"
                        data-value="${voucher.value}"
                        data-minimumBill="${voucher.minimumBill}"
                        data-name="${voucher.name}">
                    <i class="bi bi-chevron-down"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
        attachChangeVoucher()
    });
}
function updatePaginationVoucher(totalPages, currentPage) {
    const paginationDiv = document.getElementById('paginationVoucher');
    paginationDiv.innerHTML = '';

    if (currentPage > 0) {
        const prevButton = document.createElement('li');
        prevButton.classList.add('page-item');
        prevButton.innerHTML = `
            <a class="page-link" href="#" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
        `;
        prevButton.onclick = () => fetchVouchers(currentPage - 1);
        paginationDiv.appendChild(prevButton);
    }

    for (let i = 0; i < totalPages; i++) {
        const pageButton = document.createElement('li');
        pageButton.classList.add('page-item');
        if (i === currentPage) {
            pageButton.classList.add('active');
        }
        pageButton.innerHTML = `
            <a class="page-link" href="#">${i + 1}</a>
        `;
        pageButton.onclick = () => fetchVouchers(i);
        paginationDiv.appendChild(pageButton);
    }

    if (currentPage < totalPages - 1) {
        const nextButton = document.createElement('li');
        nextButton.classList.add('page-item');
        nextButton.innerHTML = `
            <a class="page-link" href="#" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        `;
        nextButton.onclick = () => fetchVouchers(currentPage + 1);
        paginationDiv.appendChild(nextButton);
    }
}
fetchVouchers(currentPageVoucher);

function searchCustomer() {
 let search = document.getElementById('inputSearchCustomer').value;
 axios.get('/searchCustomer' , {
     params : {
         searchQuery : search
     }
 })
     .then(response => {
         const data = response.data;
         console.log("data search" , response.data)
         const customerTableBody = document.getElementById('customerTable');
         customerTableBody.innerHTML = '';

         if (data.length === 0) {
             const noResultsRow = document.createElement('tr');
             noResultsRow.innerHTML = '<td colspan="5">No customers found.</td>';
             customerTableBody.appendChild(noResultsRow);
         } else {
             data.forEach((customer, index) => {
                 const row = document.createElement('tr');
                 row.innerHTML = `
                    <td>${index + 1}</td>
                    <td>${customer.fullName}</td>
                    <td>${customer.numberPhone}</td>
                    <td>${customer.email}</td>
                    <td>
                        <button class="btnChose_customer" data-id="${customer.id}" data-fullName="${customer.fullName}" data-phone="${customer.numberPhone}" data-email="${customer.email}">
                            <i class="bi bi-chevron-down"></i>
                        </button>
                    </td>
                `;
                 customerTableBody.appendChild(row);
             });
         }
         attachChoseCustomer();
     })
     .catch(error => {
         console.log("error" , error)
     })
}

document.getElementById('inputSearchCustomer').addEventListener('input' , function () {
    let search = document.getElementById("inputSearchCustomer").value.trim();
    if (search) {
        searchCustomer();
    } else {
        fetchCustomers(currentPage);
    }
})

function getAllBrand(){
    axios.get('/getAllBrand')
        .then(response => {
            const data = response.data;
            console.log("Data brand",data)
            const brandSelect = document.getElementById('brand');
            brandSelect.innerHTML = '<option>Chọn</option>';
            data.forEach(item => {
                const option = document.createElement('option');
                option.value = item.id;
                option.textContent = item.name;
                brandSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.log("error", error)
        })
}
function getAllCategory() {
    axios.get('/getAllCategory')
        .then(response => {
            const data = response.data;
            console.log("Data brand", data);

            const categorySelect = document.getElementById('category');

            categorySelect.innerHTML = '<option>Chọn</option>';

            data.forEach(item => {
                const option = document.createElement('option');
                option.value = item.id;
                option.textContent = item.name;
                categorySelect.appendChild(option);
            });
        })
        .catch(error => {
            console.log("Error:", error);
        });
}
function getAllColor(){
    axios.get('/getAllColor')
        .then(response => {
            const data = response.data;
            const colorSelect = document.getElementById('color');
            colorSelect.innerHTML = '<option>Chọn</option>';
            data.forEach(item => {
                const option = document.createElement('option');
                option.value = item.id;
                option.textContent = item.name;
                colorSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.log("error", error)
        })
}
function getAllSize(){
    axios.get('/getAllSize')
        .then(response => {
            const data = response.data;
            const sizeSelect = document.getElementById('size');
            sizeSelect.innerHTML = '<option>Chọn</option>';
            data.forEach(item => {
                const option = document.createElement('option');
                option.value = item.id;
                option.textContent = item.name;
                sizeSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.log("error", error)
        })
}
function getAllMaterial(){
    axios.get('/getAllMaterial')
        .then(response => {
            const data = response.data;
            const materialSelect = document.getElementById('material');
            materialSelect.innerHTML = '<option>Chọn</option>';
            data.forEach(item => {
                const option = document.createElement('option');
                option.value = item.id;
                option.textContent = item.name;
                materialSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.log("error", error)
        })
}
getAllBrand();
getAllColor()
getAllMaterial()
getAllSize()
getAllCategory()


function searchProduct(page = 0) {
    const categoryId = document.querySelector('#category').value === "Chọn" ? null : document.querySelector('#category').value;
    const colorId = document.querySelector('#color').value === "Chọn" ? null : document.querySelector('#color').value;
    const materialId = document.querySelector('#material').value === "Chọn" ? null : document.querySelector('#material').value;
    const sizeId = document.querySelector('#size').value === "Chọn" ? null : document.querySelector('#size').value;
    const brandId = document.querySelector('#brand').value === "Chọn" ? null : document.querySelector('#brand').value;
    const name = document.getElementById('nameInputProduct').value;


    axios.get('/searchProduct', {
        params: {
            name: name || null,
            category: categoryId,
            color: colorId,
            material: materialId,
            kichCo: sizeId,
            brand: brandId,
            page: page,
            size: 5
        }
    })
        .then(response => {
            console.log("Check data search product", response.data);
            updateProductTable(response.data.content);
            updatePaginationProduct(response.data.totalPages, page);
        })
        .catch(error => {
            console.log("Error", error);
        });
}

document.getElementById('category').addEventListener('change', function () {
    searchProduct(0);
});
document.getElementById('color').addEventListener('change', function () {
    searchProduct(0);
});
document.getElementById('material').addEventListener('change', function () {
    searchProduct(0);
});
document.getElementById('size').addEventListener('change', function () {
    searchProduct(0);
});
document.getElementById('brand').addEventListener('change', function () {
    searchProduct(0);
});

document.getElementById('nameInputProduct').addEventListener('input', function () {
    searchProduct(0);
});
