(function ($) {
    "use strict"

    jQuery(".form-create-product").validate({
        ignore: [],
        errorClass: "invalid-feedback animated fadeInUp",
        errorElement: "div",
        errorPlacement: function (e, a) {
            jQuery(a).parents(".form-group").append(e)
        },
        highlight: function (e) {
            jQuery(e).closest(".form-group").removeClass("is-invalid").addClass("is-invalid")
        },
        success: function (e) {
            jQuery(e).closest(".form-group").removeClass("is-invalid"), jQuery(e).remove()
        },
    });

})(jQuery);

ClassicEditor
    .create(document.querySelector('#productDescription'), {
        toolbar: [
            'ckbox', 'imageUpload', '|', 'heading', '|', 'undo', 'redo', '|', 'bold', 'italic', '|',
            'blockQuote', 'indent', 'link', '|', 'bulletedList', 'numberedList'
        ],
    })
    .catch(error => {
        console.error(error);
    });

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#imageResult')
                .attr('src', e.target.result);
        };
        reader.readAsDataURL(input.files[0]);
    }
}

$(function () {
    $('#upload').on('change', function () {
        readURL(input);
    });
});

/*  ==========================================
    SHOW UPLOADED IMAGE NAME
* ========================================== */
var input = document.getElementById('upload');
var infoArea = document.getElementById('upload-label');

input.addEventListener('change', showFileName);

function showFileName(event) {
    var input = event.srcElement;
    var fileName = input.files[0].name;
    infoArea.textContent = 'File name: ' + fileName;
}

$("#productMediaGallery").fileinput({
    language: "vi",
    uploadAsync: false,
    showUpload: false,
    previewFileType: 'image',
    initialPreviewAsData: false, // allows you to set a raw markup
    overwriteInitial: false,
});

let variantIndex = 0;
document.getElementById("add-row-btn").addEventListener("click", function () {
    variantIndex++;

    const container = document.getElementById("product-details-container");

    // Hàm tạo thẻ <option> từ danh sách
    const createOptions = (list) => {
        return list.map(item => `<option value="${item.id}">${item.name}</option>`).join("");
    };

    // Tạo nội dung cho hàng mới
    const newRow = `
    <div class="form-row mt-3 border-info">
    <div class="form-group col-md-3">
        <label for="productColor">Màu sắc <span class="text-danger">*</span> </label>
        <select id="productColor" name="productVariantList[${variantIndex}].colorId" class="form-control default-select" required>
          <option value="" selected>Chọn màu</option>
          ${createOptions(colorList)}
        </select>
      </div>
      <div class="form-group col-md-3">
        <label for="productSize">Size <span class="text-danger">*</span> </label>
        <select id="productSize" name="productVariantList[${variantIndex}].sizeId" class="form-control default-select" required>
          <option value="" selected>Chọn size</option>
          ${createOptions(sizeList)}
        </select>
      </div>
      <div class="form-group col-md-3">
        <label for="quantity">Số lượng <span class="text-danger">*</span></label>
        <input id="quantity" type="text" name="productVariantList[${variantIndex}].quantity" class="form-control" placeholder="" required>
      </div>
      <div class="form-group col-md-3">
        <label for="price">Giá (nghìn VND) <span class="text-danger">*</span> </label>
        <input id="price" name="productVariantList[${variantIndex}].price" type="text" class="form-control product-detail-price" placeholder="" oninput="formatPrice(this)" required>
      </div>
      <div class="form-group col-md-3 d-flex align-items-end">
        <button type="button" class="btn btn-danger btn-delete mb-1">Delete</button>
      </div>
    </div>
`;
    // Thêm hàng mới vào container
    container.insertAdjacentHTML("beforeend", newRow);
});


document.getElementById("product-details-container").addEventListener("click", function (e) {
    if (e.target && e.target.classList.contains("btn-delete")) {
        const row = e.target.closest(".form-row");
        if (row) {
            row.remove();
        }
    }
});

function formatCurrency(event) {
    let input = event.target;
    let value = input.value.replace(/\D/g, ''); // Xóa tất cả ký tự không phải số
    let formattedValue = new Intl.NumberFormat('vi-VN').format(value);
    input.value = formattedValue;
}

function formatPrice(input) {
    // Remove non-numeric characters
    let value = input.value.replace(/\D/g, '');

    // Format the number with commas
    value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");

    // Set the formatted value back into the input field
    input.value = value;
}

function removeCommasBeforeSubmit() {
    const inputs = document.querySelectorAll('.product-detail-price');
    inputs.forEach(input => {
        input.value = input.value.replace(/,/g, ''); // Remove commas before submission
    });
}

const input = document.getElementById('productName');
input.addEventListener('input', function () {
    this.value = this.value.replace(/[^a-zA-Z0-9\s]/g, '');
});