//// Định dạng tiền tệ khi nhập số tiền
//function formatCurrency(event) {
//  let input = event.target;
//  let value = input.value.replace(/\D/g, ''); // Xóa tất cả ký tự không phải số
//  let formattedValue = new Intl.NumberFormat('vi-VN').format(value);
//  input.value = formattedValue;
//}
//
//// Kiểm tra dữ liệu nhập vào form
//document.getElementById("voucherForm").addEventListener("submit", function(event) {
//  let form = this;
//  let isValid = true;
//
//  // Kiểm tra Tên Voucher
//  let nameInput = document.getElementById("name");
//  let specialCharRegex = /[^\p{L}0-9\s]/u;
//  let nameValue = nameInput.value.trim();
//
//  // Kiểm tra tên trùng (không phân biệt hoa thường)
//  let isDuplicateName = existingVoucherNames.some(name => name.toLowerCase() === nameValue.toLowerCase());
//
//  if (
//    nameInput == null ||
//    nameValue === "" ||
//    nameValue.length > 255 ||
//    specialCharRegex.test(nameValue) ||
//    isDuplicateName
//  ) {
//    nameInput.classList.add("is-invalid");
//    isValid = false;
//  } else {
//    nameInput.classList.remove("is-invalid");
//  }
//  // Kiểm tra số tiền hợp lệ
//  let valueInput = document.getElementById("value");
//  let numericValue = valueInput.value.replace(/\D/g, ''); // Loại bỏ tất cả ký tự không phải số
//
////  if (!numericValue || isNaN(numericValue) || parseInt(numericValue) < 10000 || parseInt(numericValue) > 10000000) {
////    valueInput.classList.add("is-invalid");
////    isValid = false;
////  } else {
////    valueInput.classList.remove("is-invalid");
////  }
//
//  // Kiểm tra số lượng hợp lệ
//  let quantityInput = document.getElementById("quantity");
//  if (parseInt(quantityInput.value) < 1 || parseInt(quantityInput.value) > 100000 || isNaN(quantityInput.value)) {
//    quantityInput.classList.add("is-invalid");
//    isValid = false;
//  } else {
//    quantityInput.classList.remove("is-invalid");
//  }
//
//  // Kiểm tra ngày bắt đầu và ngày kết thúc
//  let startDate = new Date(document.getElementById("startDate").value);
//  let endDate = new Date(document.getElementById("endDate").value);
//  if (isNaN(startDate.getTime()) || startDate >= endDate) {
//    document.getElementById("startDate").classList.add("is-invalid");
//    isValid = false;
//  } else {
//    document.getElementById("startDate").classList.remove("is-invalid");
//  }
//
//  if (isNaN(endDate.getTime()) || endDate <= startDate) {
//    document.getElementById("endDate").classList.add("is-invalid");
//    isValid = false;
//  } else {
//    document.getElementById("endDate").classList.remove("is-invalid");
//  }
//
//  let minimumBillInput = document.getElementById("minimumBill");
//  let minimumBillValue = minimumBillInput.value.replace(/\D/g, ''); // Loại bỏ dấu phẩy từ giá trị nhập vào
//
//if (parseInt(numericValue) > parseInt(minimumBillValue)) {
//    minimumBillInput.classList.add("is-invalid");
//    valueInput.classList.add("is-invalid");
//    isValid = false;
//  } else {
//    // Nếu số tiền hợp lệ, xóa dấu lỗi
//       if (!numericValue || isNaN(numericValue) || parseInt(numericValue) < 10000 || parseInt(numericValue) > 100000000) {
//           valueInput.classList.add("is-invalid");
//           isValid = false;
//       } else {
//           valueInput.classList.remove("is-invalid");
//       }
//
//       if (!minimumBillValue || isNaN(minimumBillValue) || parseInt(minimumBillValue) < 1000 || parseInt(minimumBillValue) > 10000000) {
//             minimumBillInput.classList.add("is-invalid");
//             isValid = false;
//         } else {
//             minimumBillInput.classList.remove("is-invalid");
//         }
//  }
//
////  if (parseInt(numericValue) > parseInt(minimumBillValue)) {
////    minimumBillInput.classList.add("is-invalid");
////    valueInput.classList.add("is-invalid");
////    isValid = false;
////  } else {
////    if (!minimumBillValue || isNaN(minimumBillValue) || parseInt(minimumBillValue) < 1000 || parseInt(minimumBillValue) > 10000000) {
////      minimumBillInput.classList.add("is-invalid");
////      isValid = false;
////    } else {
////      minimumBillInput.classList.remove("is-invalid");
////    }
////  }
//
//  // Ngăn chặn gửi form nếu không hợp lệ
//  if (!isValid) {
//    event.preventDefault();
//    event.stopPropagation();
//  } else {
//    // Chuyển số tiền về dạng số trước khi gửi form (loại bỏ dấu phẩy)
//    valueInput.value = numericValue;
//    minimumBillInput.value = minimumBillValue;
//  }
//
//  form.classList.add("was-validated");
//});

// Định dạng tiền tệ khi nhập số tiền
function formatCurrency(event) {
  let input = event.target;
  let value = input.value.replace(/\D/g, ''); // Xóa tất cả ký tự không phải số
  if(value === "") {
    input.value = "";
    return;
  }
  let formattedValue = new Intl.NumberFormat('vi-VN').format(value);
  input.value = formattedValue;
}

// Hàm validate tên voucher trả về mảng lỗi
function validateVoucherName(nameValue, existingNames) {
  const errors = [];
  if (!nameValue) errors.push("Tên voucher không được để trống.");
  if (nameValue.length > 255) errors.push("Tên voucher không được dài quá 255 ký tự.");
  const specialCharRegex = /[^\p{L}0-9\s]/u;
  if (specialCharRegex.test(nameValue)) errors.push("Tên voucher không được chứa ký tự đặc biệt.");
  const isDuplicate = existingNames.some(n => n.toLowerCase() === nameValue.toLowerCase());
  if (isDuplicate) errors.push("Tên voucher đã tồn tại.");
  return errors;
}

document.getElementById("voucherForm").addEventListener("submit", function(event) {
  let form = this;
  let isValid = true;

  // Validate Tên Voucher
  let nameInput = document.getElementById("name");
  let nameFeedback = nameInput.nextElementSibling; // giả sử là .invalid-feedback
  let nameValue = nameInput.value.trim();
  let nameErrors = validateVoucherName(nameValue, existingVoucherNames);
  if (nameErrors.length > 0) {
    nameInput.classList.add("is-invalid");
    nameFeedback.innerHTML = nameErrors.join("<br>");
    isValid = false;
  } else {
    nameInput.classList.remove("is-invalid");
    nameFeedback.innerHTML = "";
  }

  // Validate Số tiền (value)
  let valueInput = document.getElementById("value");
  let valueFeedback = valueInput.nextElementSibling;
  let numericValue = valueInput.value.replace(/\D/g, '');
  if (!numericValue || isNaN(numericValue) || parseInt(numericValue) < 10000 || parseInt(numericValue) > 100000000) {
    valueInput.classList.add("is-invalid");
    valueFeedback.textContent = "Số tiền phải từ 10,000 đến 100,000,000.";
    isValid = false;
  } else {
    valueInput.classList.remove("is-invalid");
    valueFeedback.textContent = "";
  }

  // Validate Số lượng
  let quantityInput = document.getElementById("quantity");
  let quantityFeedback = quantityInput.nextElementSibling;
  let quantityValue = parseInt(quantityInput.value);
  if (isNaN(quantityValue) || quantityValue < 1 || quantityValue > 100000) {
    quantityInput.classList.add("is-invalid");
    quantityFeedback.textContent = "Số lượng phải từ 1 đến 100,000.";
    isValid = false;
  } else {
    quantityInput.classList.remove("is-invalid");
    quantityFeedback.textContent = "";
  }

  // Validate ngày bắt đầu và kết thúc
  let startDateInput = document.getElementById("startDate");
  let endDateInput = document.getElementById("endDate");
  let startDateFeedback = startDateInput.nextElementSibling;
  let endDateFeedback = endDateInput.nextElementSibling;
  let startDate = new Date(startDateInput.value);
  let endDate = new Date(endDateInput.value);

  if (isNaN(startDate.getTime())) {
    startDateInput.classList.add("is-invalid");
    startDateFeedback.textContent = "Ngày bắt đầu không hợp lệ.";
    isValid = false;
  } else if (startDate >= endDate) {
    startDateInput.classList.add("is-invalid");
    startDateFeedback.textContent = "Ngày bắt đầu phải trước ngày kết thúc.";
    isValid = false;
  } else {
    startDateInput.classList.remove("is-invalid");
    startDateFeedback.textContent = "";
  }

  if (isNaN(endDate.getTime())) {
    endDateInput.classList.add("is-invalid");
    endDateFeedback.textContent = "Ngày kết thúc không hợp lệ.";
    isValid = false;
  } else if (endDate <= startDate) {
    endDateInput.classList.add("is-invalid");
    endDateFeedback.textContent = "Ngày kết thúc phải sau ngày bắt đầu.";
    isValid = false;
  } else {
    endDateInput.classList.remove("is-invalid");
    endDateFeedback.textContent = "";
  }

  // Validate tiền tối thiểu hóa đơn
  let minimumBillInput = document.getElementById("minimumBill");
  let minimumBillFeedback = minimumBillInput.nextElementSibling;
  let minimumBillValue = minimumBillInput.value.replace(/\D/g, '');

  if (!minimumBillValue || isNaN(minimumBillValue) || parseInt(minimumBillValue) < 1000 || parseInt(minimumBillValue) > 10000000) {
    minimumBillInput.classList.add("is-invalid");
    minimumBillFeedback.textContent = "Tiền tối thiểu hóa đơn phải từ 1,000 đến 10,000,000.";
    isValid = false;
  } else if (parseInt(numericValue) > parseInt(minimumBillValue)) {
    // Số tiền voucher không được lớn hơn tiền tối thiểu hóa đơn
    minimumBillInput.classList.add("is-invalid");
    minimumBillFeedback.textContent = "Tiền tối thiểu hóa đơn phải lớn hơn hoặc bằng số tiền voucher.";
    valueInput.classList.add("is-invalid");
    valueFeedback.textContent = "Số tiền voucher không được lớn hơn tiền tối thiểu hóa đơn.";
    isValid = false;
  } else {
    minimumBillInput.classList.remove("is-invalid");
    minimumBillFeedback.textContent = "";

    // Nếu trước đó value bị lỗi do so sánh này thì xóa lỗi
    if (valueInput.classList.contains("is-invalid") && valueFeedback.textContent === "Số tiền voucher không được lớn hơn tiền tối thiểu hóa đơn.") {
      valueInput.classList.remove("is-invalid");
      valueFeedback.textContent = "";
    }
  }

  // Ngăn chặn gửi form nếu không hợp lệ
  if (!isValid) {
    event.preventDefault();
    event.stopPropagation();
  } else {
    // Chuyển số tiền về dạng số trước khi gửi form (loại bỏ dấu phẩy)
    valueInput.value = numericValue;
    minimumBillInput.value = minimumBillValue;
  }

  form.classList.add("was-validated");
});
