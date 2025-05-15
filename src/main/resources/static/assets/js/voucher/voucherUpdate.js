// Định dạng tiền tệ khi nhập số tiền
function formatCurrency(event) {
  let input = event.target;
  let value = input.value.replace(/\D/g, ''); // Xóa tất cả ký tự không phải số
  let formattedValue = new Intl.NumberFormat('vi-VN').format(value);
  input.value = formattedValue;
}

// Kiểm tra dữ liệu nhập vào form
document.getElementById("voucherForm").addEventListener("submit", function(event) {
  let form = this;
  let isValid = true;

  // Kiểm tra Tên Voucher
  let nameInput = document.getElementById("name");
  let specialCharRegex = /[^\p{L}0-9\s]/u;
  if (nameInput == null || nameInput.value.trim() === "" || nameInput.value.length > 255 || specialCharRegex.test(nameInput.value)) {
    nameInput.classList.add("is-invalid");
    isValid = false;
  } else {
    nameInput.classList.remove("is-invalid");
  }

  // Kiểm tra số tiền hợp lệ
  let valueInput = document.getElementById("value");
  let numericValue = valueInput.value.replace(/\D/g, ''); // Loại bỏ tất cả ký tự không phải số

  // Kiểm tra nếu giá trị không phải là một số hợp lệ
  // if (!numericValue || isNaN(numericValue) || parseInt(numericValue) < 10000 || parseInt(numericValue) > 100000000) {
  //     valueInput.classList.add("is-invalid");
  //     isValid = false;
  // } else {
  //     valueInput.classList.remove("is-invalid");
  // }

  // Kiểm tra số lượng hợp lệ
  let quantityInput = document.getElementById("quantity");
  if (quantityInput.value.trim() === "" || /[a-zA-Z]/.test(quantityInput.value) || isNaN(quantityInput.value) || parseInt(quantityInput.value) < 1 || parseInt(quantityInput.value) > 100000) {
    quantityInput.classList.add("is-invalid");
    isValid = false;
  } else {
    quantityInput.classList.remove("is-invalid");
  }

  // Kiểm tra ngày bắt đầu và ngày kết thúc
  let startDate = new Date(document.getElementById("startDate").value);
  let endDate = new Date(document.getElementById("endDate").value);
  if (isNaN(startDate.getTime()) || startDate >= endDate) {
    document.getElementById("startDate").classList.add("is-invalid");
    isValid = false;
  } else {
    document.getElementById("startDate").classList.remove("is-invalid");
  }

  if (isNaN(endDate.getTime()) || endDate <= startDate) {
    document.getElementById("endDate").classList.add("is-invalid");
    isValid = false;
  } else {
    document.getElementById("endDate").classList.remove("is-invalid");
  }

  // Kiểm tra hóa đơn tối thiểu
  let minimumBillInput = document.getElementById("minimumBill");
  let minimumBillValue = minimumBillInput.value.replace(/\D/g, ''); // Loại bỏ tất cả ký tự không phải số
  // if (!minimumBillValue || isNaN(minimumBillValue) || parseInt(minimumBillValue) < 1000 || parseInt(minimumBillValue) > 10000000) {
  //     minimumBillInput.classList.add("is-invalid");
  //     isValid = false;
  // } else {
  //     minimumBillInput.classList.remove("is-invalid");
  // }

  // Kiểm tra trạng thái
  let statusSelect = document.getElementById("status");
  if (statusSelect.value === "") {
    statusSelect.classList.add("is-invalid");
    isValid = false;
  } else {
    statusSelect.classList.remove("is-invalid");
  }

  var now = new Date();
  if (endDate > now) {
    if (statusSelect.value !== "ACTIVE") {
      isValid = false;
      statusSelect.classList.add("is-invalid");
    }
  } else {
    if (statusSelect.value !== "EXPIRED" || parseInt(quantityInput.value) <= 0) {
      isValid = false;
      statusSelect.classList.add("is-invalid");
    }
  }

  // Kiểm tra nếu giá trị voucher (số tiền) lớn hơn hóa đơn tối thiểu
  if (parseInt(numericValue) > parseInt(minimumBillValue)) {
    minimumBillInput.classList.add("is-invalid");
    valueInput.classList.add("is-invalid");
    isValid = false;
  } else {
    // Nếu số tiền hợp lệ, xóa dấu lỗi
       if (!numericValue || isNaN(numericValue) || parseInt(numericValue) < 10000 || parseInt(numericValue) > 100000000) {
           valueInput.classList.add("is-invalid");
           isValid = false;
       } else {
           valueInput.classList.remove("is-invalid");
       }

       if (!minimumBillValue || isNaN(minimumBillValue) || parseInt(minimumBillValue) < 1000 || parseInt(minimumBillValue) > 10000000) {
             minimumBillInput.classList.add("is-invalid");
             isValid = false;
         } else {
             minimumBillInput.classList.remove("is-invalid");
         }
  }

  // Ngăn chặn gửi form nếu không hợp lệ
  if (!isValid) {
    event.preventDefault();
    event.stopPropagation();
  } else {
    // Chuyển số tiền và hóa đơn tối thiểu về dạng số trước khi gửi form (loại bỏ dấu phẩy)
    valueInput.value = numericValue;
    minimumBillInput.value = minimumBillValue;
  }

  form.classList.add("was-validated");
});
