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
        if (!numericValue || isNaN(numericValue) || parseInt(numericValue) <= 10000 || parseInt(numericValue) > 10000000) {
           valueInput.classList.add("is-invalid");
           isValid = false;
        } else {
         valueInput.classList.remove("is-invalid");
        }



        // Kiểm tra số lượng hợp lệ
        let quantityInput = document.getElementById("quantity");
        if (parseInt(quantityInput.value) < 1 || parseInt(quantityInput.value) > 100000 || isNaN(quantityInput.value)) {
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

        let minimumBillInput = document.getElementById("minimumBill");
      let minimumBillValue = minimumBillInput.value.replace(/\D/g, '');
      if (!minimumBillValue || isNaN(minimumBillValue) || parseInt(minimumBillValue) < 1000 || parseInt(minimumBillValue) > 10000000) {
         minimumBillInput.classList.add("is-invalid");
         isValid = false;
      } else {
       minimumBillInput.classList.remove("is-invalid");
      }

            if(parseInt(numericValue) > parseInt(minimumBillValue)){
//             minimumBillInput.classList.add("is-invalid");
               valueInput.classList.add("is-invalid");
                isValid = false;
             } else {
//              minimumBillInput.classList.remove("is-invalid");
                valueInput.classList.remove("is-invalid");
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