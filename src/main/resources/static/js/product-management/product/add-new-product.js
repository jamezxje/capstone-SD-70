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
    uploadAsync: false,
    showUpload: false,
    previewFileType: 'image',
    initialPreviewAsData: false, // allows you to set a raw markup
    overwriteInitial: false,
});

let variantIndex = 0;
document.getElementById("add-row-btn").addEventListener("click", function () {
    const container = document.getElementById("product-details-container");

    // Hàm tạo thẻ <option> từ danh sách
    const createOptions = (list) => {
        return list.map(item => `<option value="${item.id}">${item.name}</option>`).join("");
    };

    // Tạo nội dung cho hàng mới
    const newRow = `
            <div class="form-row mt-3 border-info">
            <div class="form-group col-md-2">
                        <label for="productCategory">Danh mục <span class="text-danger">*</span> </label>
                        <select id="productCategory" name="variants[${variantIndex}].categoryId" class="form-control default-select" required>
                          <option value="" selected>Chọn danh mục</option>
                          ${createOptions(categoryList)}
                          </option>
                        </select>
                      </div>
              <div class="form-group col-md-2">
                <label for="productMaterial">Chất liệu <span class="text-danger">*</span> </label>
                <select id="productMaterial" name="variants[${variantIndex}].materialId" class="form-control default-select" required>
                  <option value="" selected>Chọn chất liệu</option>
                  ${createOptions(materialList)}
                </select>
              </div>
              <div class="form-group col-md-2">
                <label for="productColor">Màu sắc <span class="text-danger">*</span> </label>
                <select id="productColor" name="variants[${variantIndex}].colorId" class="form-control default-select" required>
                  <option value="" selected>Chọn màu</option>
                  ${createOptions(colorList)}
                </select>
              </div>
              <div class="form-group col-md-2">
                <label for="productSize">Size <span class="text-danger">*</span> </label>
                <select id="productSize" name="variants[${variantIndex}].sizeId" class="form-control default-select" required>
                  <option value="" selected>Chọn size</option>
                  ${createOptions(sizeList)}
                </select>
              </div>
              <div class="form-group col-md-2">
                <label for="stockQuantity">Số lượng <span class="text-danger">*</span></label>
                <input id="stockQuantity" type="text" name="variants[${variantIndex}].stockQuantity" class="form-control" placeholder="" required>
              </div>
              <div class="form-group col-md-1">
                        <label for="productBasePrice">Giá (nghìn VND) <span class="text-danger">*</span> </label>
                        <input id="productBasePrice" name="variants[${variantIndex}].basePrice" type="text" class="form-control"
                               placeholder="" required>
                      </div>
              <div class="form-group col-md-1 d-flex align-items-end">
              <button type="button" class="btn btn-danger btn-delete mb-1">Delete</button>
            </div>
            </div>
    `;
    // Thêm hàng mới vào container
    container.insertAdjacentHTML("beforeend", newRow);
});
variantIndex++;

document.getElementById("product-details-container").addEventListener("click", function (e) {
    if (e.target && e.target.classList.contains("btn-delete")) {
        const row = e.target.closest(".form-row");
        if (row) {
            row.remove();
        }
    }
});

document.getElementById("btn-add-product").addEventListener("click", function () {
    const productName = document.getElementById("productName").value;
    const productCode = document.getElementById("productCode").value;
    const basePrice = document.getElementById("productBasePrice").value;
    const productDescription = document.getElementById("productDescription").value;
    const categoryId = document.getElementById("productCategory").value;

    const detailsContainer = document.getElementById("product-details-container");
    const detailRows = detailsContainer.querySelectorAll(".form-row");

    const products = [];
    detailRows.forEach((row) => {
        const materialId = row.querySelector('select[name="materialId"]').value;
        const colorId = row.querySelector('select[name="colorId"]').value;
        const sizeId = row.querySelector('select[name="sizeId"]').value;
        const stockQuantity = row.querySelector('#stockQuantity').value;

        if (materialId && colorId && sizeId && stockQuantity) {
            products.push({
                productName,
                productCode,
                basePrice,
                productDescription,
                categoryId,
                materialId,
                colorId,
                sizeId,
                stockQuantity,
            });
        }
    });

    console.log("Products added:", products);
});
