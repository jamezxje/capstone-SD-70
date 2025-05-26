document.addEventListener('DOMContentLoaded', function () {
    const cartBody = document.getElementById('cart-body');
    const totalElement = document.getElementById('cart__subtotal-title money');
    const selectAllCheckbox = document.getElementById('select-all'); // Add a "select all" checkbox

    if (!cartBody) {
        console.error("Element with ID 'cart-body' not found.");
        return;
    }

    const cart = JSON.parse(localStorage.getItem('noLoginCart')) || [];

    cartBody.innerHTML = '';

    if (cart.length === 0) {
        cartBody.innerHTML = `
        <tr>
          <td colspan="10" class="text-center pt-5"><h3>Giỏ hàng của bạn đang trống</h3></td>
        </tr>`;
        return;
    }

    cart.forEach((item, index) => {
        const total = item.price * item.quantity;

        const row = document.createElement('tr');
        row.className = 'cart__row border-bottom line1 cart-flex border-top';
        row.innerHTML = `
        <td>
          <div class="custom-control custom-checkbox">
            <input type="checkbox" class="custom-control-input" name="selectedCartDetailIds" id="chk-${index}" data-index="${index}">
            <label class="custom-control-label" for="chk-${index}"></label>
          </div>
        </td>
        <td class="cart__image-wrapper cart-flex-item">
          <img class="cart__image" alt="${item.name}" src="${item.featureImageUrl || '/assets/images/product-default.jpg'}">
        </td>
        <td class="cart__meta small--text-left cart-flex-item">
          <div class="list-view-item__title">${item.name}</div>
          <div class="cart__meta-text">
            Màu: ${item.color.name}<br>
            Size: ${item.size.name}
          </div>
        </td>
        <td class="cart__price-wrapper cart-flex-item text-center">
          <span class="money">${item.price.toLocaleString()} VND</span>
        </td>
        <td class="cart__update-wrapper cart-flex-item text-right">
          <div class="cart__qty text-center">
            <div class="qtyField">
              <button class="qtyBtn minus" onclick="updateQty(${index}, -1)">-</button>
              <input class="cart__qty-input qty" type="text" value="${item.quantity}" readonly>
              <button class="qtyBtn plus" onclick="updateQty(${index}, 1)">+</button>
            </div>
          </div>
        </td>
        <td class="text-right small--hide cart-price">
          <span class="money">${total.toLocaleString()} VND</span>
        </td>
        <td class="text-center small--hide">
          <a class="btn btn--secondary cart__remove" onclick="removeItem(${index})"><i class="icon anm anm-times-l"></i></a>
        </td>
      `;
        cartBody.appendChild(row);
    });

    const checkboxes = document.querySelectorAll('input[name="selectedCartDetailIds"]');
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', calculateTotal);
    });

    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', function () {
            const isChecked = selectAllCheckbox.checked;
            checkboxes.forEach(checkbox => {
                checkbox.checked = isChecked;
            });
            calculateTotal();
        });
    }

    function calculateTotal() {
        let total = 0;
        checkboxes.forEach(checkbox => {
            if (checkbox.checked) {
                const index = checkbox.getAttribute('data-index');
                const item = cart[index];
                total += item.price * item.quantity;
            }
        });
        totalElement.innerHTML = `${total.toLocaleString()} VND`;
        document.getElementById('totalPayMent').innerHTML = `${total.toLocaleString()} VND`;
        getVoucherInBill(total)
        totalPayment = total;
    }
});
let totalPayment = 0;

function updateQty(index, change) {

    const cart = JSON.parse(localStorage.getItem('noLoginCart')) || [];
    const selectedIndices = Array.from(document.querySelectorAll('input[name="selectedCartDetailIds"]:checked'))
        .map(checkbox => checkbox.getAttribute('data-index'));

    localStorage.setItem('selectedCartIndices', JSON.stringify(selectedIndices));

    const item = cart[index];
    if (item) {
        item.quantity += change;

        if (item.quantity <= 0) {
            removeItem(index);
            return;
        }

        localStorage.setItem('noLoginCart', JSON.stringify(cart));
        location.reload();

    }
}

document.addEventListener('DOMContentLoaded', function () {
    localStorage.removeItem('voucherIDNoLogin');
    localStorage.removeItem('voucherValueNoLogin')
    localStorage.removeItem('checkUseVoucher')
    const selectedIndices = JSON.parse(localStorage.getItem('selectedCartIndices')) || [];
    const checkboxes = document.querySelectorAll('input[name="selectedCartDetailIds"]');
    const totalElement = document.getElementById('cart__subtotal-title money');
    const cart = JSON.parse(localStorage.getItem('noLoginCart')) || [];

    // Restore selected checkboxes
    checkboxes.forEach(checkbox => {
        const index = checkbox.getAttribute('data-index');
        if (selectedIndices.includes(index)) {
            checkbox.checked = true;
        }
    });


    calculateTotal();

    localStorage.removeItem('selectedCartIndices');

    function calculateTotal() {
        let total = 0;
        checkboxes.forEach(checkbox => {
            if (checkbox.checked) {
                const index = checkbox.getAttribute('data-index');
                const item = cart[index];
                total += item.price * item.quantity;
            }
        });
        totalElement.innerHTML = `${total.toLocaleString()} VND`;
        document.getElementById('totalPayMent').innerHTML = `${total.toLocaleString()} VND`;
        getVoucherInBill(total)

    }
});

function removeItem(index) {
    const cart = JSON.parse(localStorage.getItem('noLoginCart')) || [];
    cart.splice(index, 1);
    localStorage.setItem('noLoginCart', JSON.stringify(cart));
    location.reload();
}

const openModalVoucher = document.getElementById('openVoucherModalBtn');
openModalVoucher.addEventListener('click', function () {
    const voucherModal = document.getElementById('modalVoucher');
    const modal = new bootstrap.Modal(voucherModal);
    modal.show();

});

async function getVoucherInBill(minimumBill) {
    console.log("Fetching vouchers for minimum bill:", minimumBill);

    try {
        const response = await axios.get(`/getMinimumBillNoLogin`, {
            params: {minimumBill: minimumBill}
        });

        const vouchers = response.data;
        console.log("Voucher list:", vouchers);

        const voucherListContainer = document.getElementById('voucherList');
        if (!voucherListContainer) {
            console.error("Element with ID 'voucherList' not found.");
            return;
        }

        voucherListContainer.innerHTML = ''; // Clear old content

        if (vouchers.length === 0) {
            voucherListContainer.innerHTML = '<p>Vui lòng chọn sản phẩm cần mua để sử dụng mã giảm giá.</p>';
            return;
        }

        vouchers.forEach(voucher => {
            const voucherItem = document.createElement('div');
            voucherItem.classList.add('voucher-item', 'border', 'rounded', 'p-3', 'mb-2');

            voucherItem.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <div class="voucher-info">
                        <p class="mb-1"><strong>${voucher.name}</strong></p>
                        <p class="mb-1"> Giảm: <span>${voucher.value.toLocaleString()}</span> VND</p>
                        <p class="mb-1">Đơn tối thiểu: <span>${voucher.minimumBill.toLocaleString()}</span> VND</p>
                        <p class="mb-0">HSD: <span>${formatDateFromArray(voucher.endDate)}</span></p>
                    </div>
                    <div class="voucher-select">
                        <input type="radio"
                            name="selectedVoucher"
                            value="${voucher.id}"
                            data-value="${voucher.value}"
                            data-name="${voucher.name}"
                            data-minimum="${voucher.minimumBill}"
                            style="transform: scale(1.3); cursor: pointer;">
                    </div>
                </div>
            `;

            voucherListContainer.appendChild(voucherItem);
        });

    } catch (error) {
        console.error("Error fetching vouchers:", error);
        const voucherListContainer = document.getElementById('voucherList');
        if (voucherListContainer) {
            voucherListContainer.innerHTML = '<p>Error loading vouchers. Please try again later.</p>';
        }
    }
}

function formatDateFromArray(dateArray) {
    if (!Array.isArray(dateArray) || dateArray.length < 3) return "Invalid Date";

    const year = dateArray[0];
    const month = String(dateArray[1]).padStart(2, '0');
    const day = String(dateArray[2]).padStart(2, '0');
    const hour = dateArray[3] !== undefined ? String(dateArray[3]).padStart(2, '0') : '00';
    const minute = dateArray[4] !== undefined ? String(dateArray[4]).padStart(2, '0') : '00';

    return `${day}/${month}/${year} ${hour}:${minute}`;
}
let voucherValueLocal = 0;
document.getElementById('btnApplyVoucher').addEventListener('click', function () {
    const selectVoucher = document.querySelector('input[name="selectedVoucher"]:checked');
    if (selectVoucher) {
        const voucherId = selectVoucher.value;
        const voucherValue = selectVoucher.getAttribute('data-value');
        const voucherName = selectVoucher.getAttribute('data-name');
        const minimumBill = selectVoucher.getAttribute('data-minimum');
        voucherValueLocal = voucherValue;
        localStorage.setItem('voucherValueNoLogin', voucherValue);
        localStorage.setItem('voucherIDNoLogin', voucherId)
        console.log("Selected Voucher ID:", voucherId);
        console.log("Selected Voucher Value:", voucherValue);
        console.log("Selected Voucher Name:", voucherName);
        console.log("Selected Voucher Minimum Bill:", minimumBill);
        document.getElementById('voucher').innerHTML = voucherName + ' - ' +  parseInt(voucherValue, 10).toLocaleString() + ' VND';

        const totalPaymentElement = document.getElementById('totalPayMent');
        if (totalPaymentElement) {
            const totalPayment = parseInt(totalPaymentElement.innerText.replace(/[^0-9]/g, ''));
            if (totalPayment >= minimumBill) {
                const discountedTotal = totalPayment - parseInt(voucherValue);
                totalPaymentElement.innerText = `${discountedTotal.toLocaleString()} VND`;
                toastr.options.positionClass = 'toast-top-right';
                toastr.success('Áp dụng mã giảm giá thành công!');
            } else {
                toastr.options.positionClass = 'toast-top-right';
                toastr.error(`Đơn hàng của bạn chưa đủ điều kiện áp dụng mã giảm giá ${voucherName}. Đơn hàng tối thiểu là ${minimumBill.toLocaleString()} VND`);
            }
        }
    }
    const voucherModal = document.getElementById('modalVoucher');
    const modal = bootstrap.Modal.getInstance(voucherModal) || new bootstrap.Modal(voucherModal);
    modal.hide();
})
document.getElementById('btnCancelVoucher').addEventListener('click' , function () {
    document.getElementById('voucher').innerHTML = '0 VND';
    localStorage.setItem('voucherValueNoLogin' , 0);
    localStorage.setItem('voucherIDNoLogin', 12);
    document.getElementById('totalPayMent').innerHTML = `${totalPayment.toLocaleString()} VND`
    const selectedVoucher = document.querySelector('input[name="selectedVoucher"]:checked');
    if (selectedVoucher) {
        selectedVoucher.checked = false;
    }

    const voucherModal = document.getElementById('modalVoucher');
    const modal = bootstrap.Modal.getInstance(voucherModal) || new bootstrap.Modal(voucherModal);
    modal.hide();
    showToast('Hủy voucher thành công')
})
document.getElementById('cart_checkout_button').addEventListener('click', function () {
    const selectedProducts = [];
    const checkboxes = document.querySelectorAll('input[name="selectedCartDetailIds"]:checked');
    const cart = JSON.parse(localStorage.getItem('noLoginCart')) || [];

    checkboxes.forEach(checkbox => {
        const index = checkbox.getAttribute('data-index');
        const item = cart[index];
        if (item) {
            selectedProducts.push(item);
        }
    });

    if (selectedProducts.length > 0) {
        localStorage.setItem('selectedProductsForCheckout', JSON.stringify(selectedProducts));
        if (!voucherValueLocal || voucherValueLocal === "0") {
            localStorage.setItem('checkUseVoucher', true);
        }

    } else {
        showToastError('Vui lòng chọn  sản phẩm để thanh toán.' , 'error');
        return;
    }

    window.location.href = '/buyNow-NoLogin';
});

document.addEventListener('DOMContentLoaded', function () {
    const checkAll = document.getElementById('checkAll');
    const productCheckboxes = document.querySelectorAll('input[name="selectedCartDetailIds"]');
    const totalElement = document.getElementById('cart__subtotal-title money');
    const totalPaymentElement = document.getElementById('totalPayMent');
    const cart = JSON.parse(localStorage.getItem('noLoginCart')) || [];
    checkAll.addEventListener('change', function () {
        const isChecked = checkAll.checked;
        productCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });
        calculateTotal();
    });

    productCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            checkAll.checked = Array.from(productCheckboxes).every(cb => cb.checked);

            calculateTotal();
        });
    });


    function calculateTotal() {
        let total = 0;
        productCheckboxes.forEach(checkbox => {
            if (checkbox.checked) {
                const index = checkbox.getAttribute('data-index');
                const item = cart[index];
                if (item) {
                    total += item.price * item.quantity;
                }
            }
        });
        getVoucherInBill(total)
        totalElement.innerHTML = `${total.toLocaleString()} VND`;
        totalPaymentElement.innerHTML = `${total.toLocaleString()} VND`;
    }
    calculateTotal();
});

async function updateQty(index, change) {
    const cart = JSON.parse(localStorage.getItem('noLoginCart')) || [];
    const item = cart[index];

    if (item) {
        try {
            const response = await axios.get(`/getQuantityProductDetail/${item.idProductLocal}`);
            const availableQuantity = response.data.quantity;
            console.log('Check available quantity:', availableQuantity);
            if (item.quantity + change > availableQuantity) {
               showToastError("Số lượng sản phảm vượt quá số lượng trong kho!");
                return;
            }

            item.quantity += change;
            if (item.quantity <= 0) {
             showToastError("Vui lòng không giảm số lượng về 0!");
                return;
            }
            localStorage.setItem('noLoginCart', JSON.stringify(cart));
            location.reload();
        } catch (error) {
            console.error('Lỗi không lấy được dữ liệu từ API:', error);
            alert('Không thể kiểm tra số lượng sản phẩm. Vui lòng thử lại sau.');
        }
    }
}

function showToastError(message, type = "error", duration = 1800) {
    const toastContainer = document.getElementById("toast-container");
    const toast = document.createElement("div");

    let bgColor, textColor, borderColor, icon;

    if (type === "success") {
        bgColor = "#d4edda";
        textColor = "#155724";
        borderColor = "#28a745";
        icon = `<i class="fas fa-check-circle" style="color: #28a745; margin-right: 10px;"></i>`;
    } else {
        bgColor = "#fff3cd";
        textColor = "#856404";
        borderColor = "#ffc107";
        icon = `<i class="fas fa-exclamation-triangle" style="color: #ffc107; margin-right: 10px;"></i>`;
    }

    // Phần thân toast
    toast.innerHTML = `
      <div style="display: flex; align-items: center;">${icon}<span>${message}</span></div>
      <div class="toast-progress-bar"></div>
    `;

    toast.style.cssText = `
      background-color: ${bgColor};
      color: ${textColor};
      border-left: 5px solid ${borderColor};
      padding: 12px 20px 6px 20px;
      font-size: 16px;
      border-radius: 5px;
      display: flex;
      flex-direction: column;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      opacity: 0;
      transform: translateX(100%);
      transition: opacity 0.5s, transform 0.5s ease-in-out;
      margin-bottom: 10px;
      position: relative;
      min-width: 280px;
    `;

    // Thanh thời gian
    const progressBarStyle = document.createElement("style");
    progressBarStyle.innerHTML = `
      .toast-progress-bar {
        height: 4px;
        background-color: ${borderColor};
        width: 100%;
        animation: progress ${duration}ms linear forwards;
        border-radius: 0 0 5px 5px;
        margin-top: 8px;
      }
      @keyframes progress {
        from { width: 100%; }
        to { width: 0%; }
      }
    `;
    document.head.appendChild(progressBarStyle);

    toastContainer.appendChild(toast);

    // Animate in
    setTimeout(() => {
        toast.style.opacity = "1";
        toast.style.transform = "translateX(0)";
    }, 100);

    // Animate out and remove
    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.transform = "translateX(100%)";
        setTimeout(() => toast.remove(), 500);
    }, 1800);
}