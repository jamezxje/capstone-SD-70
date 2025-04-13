   document.addEventListener("DOMContentLoaded", function() {
                   const valueElements = document.querySelectorAll('.voucher-value');
                   valueElements.forEach(function (element) {
              // Lấy giá trị và loại bỏ các ký tự không phải số, bao gồm dấu phân cách hàng nghìn
                   const value = parseFloat(element.innerText.replace(/[^\d.-]/g, ''));
              // Đảm bảo giá trị là số hợp lệ
                if (!isNaN(value)) {
              // Làm tròn xuống đến 2 chữ số thập phân
                const roundedValue = Math.floor(value * 100) / 100;
              // Định dạng số theo kiểu tiền tệ Việt Nam
              element.innerText = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(roundedValue);
          }
        });
        });

         document.getElementById("status").addEventListener("change", function () {
            document.getElementById("voucherSearchForm").submit();
          });