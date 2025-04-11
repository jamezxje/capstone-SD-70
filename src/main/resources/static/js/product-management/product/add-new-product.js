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
        <input id="price" name="productVariantList[${variantIndex}].price" type="text" class="form-control" placeholder="" oninput="formatCurrency(event)" required>
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
    let value = input.value;

    // Xóa tất cả ký tự không phải là số
    value = value.replace(/\D/g, '');

    // Định dạng thành tiền tệ với dấu phân cách hàng nghìn
    let formattedValue = new Intl.NumberFormat('vi-VN').format(value);

    // Cập nhật lại giá trị trong input
    input.value = formattedValue;
}

function formatPrice(input) {
    let value = input.value.replace(/[^\d]/g, '');
    if (value) {
        value = Number(value).toLocaleString('de-DE');
    }
    input.value = value;
}
