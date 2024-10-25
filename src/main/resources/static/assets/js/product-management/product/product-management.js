document.addEventListener('DOMContentLoaded', function () {
    var editProductModal = document.getElementById('editProductModal');

    editProductModal.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget;
        var productId = button.getAttribute('data-id');
        var productName = button.getAttribute('data-name');
        var productCode = button.getAttribute('data-code');
        var productWeight = button.getAttribute('data-weight');
        var gsmQualification = button.getAttribute('data-gsmQualification');
        var featureImageUrl = button.getAttribute('data-featureImageUrl');
        var sizeGuideUrl = button.getAttribute('data-sizeGuideUrl');
        var productDescription = button.getAttribute('data-description');
        var productStatus = button.getAttribute('data-status') === 'true';

        var modalProductId = editProductModal.querySelector('#editProductId');
        var modalProductName = editProductModal.querySelector('#editProductName');
        var modalProductCode = editProductModal.querySelector('#editProductCode');
        var modalProductWeight = editProductModal.querySelector('#editProductWeight');
        var modalGsmQualification = editProductModal.querySelector('#editGsmQualification');
        var modalDescription = editProductModal.querySelector('#editDescription');
        var modalFeatureImage = editProductModal.querySelector('#featureImagePreview');
        var modalSizeGuideImage = editProductModal.querySelector('#sizeGuidePreview');
        var modalProductStatus = editProductModal.querySelector('#editProductStatus');

        modalProductId.value = productId;
        modalProductCode.value = productCode;
        modalProductName.value = productName;
        modalProductWeight.value = productWeight;
        modalGsmQualification.value = gsmQualification;
        modalDescription.value = productDescription;
        if (featureImageUrl) {
            modalFeatureImage.src = '/' + featureImageUrl;
        } else {
            modalFeatureImage.src = '';
        }

        if (sizeGuideUrl) {
            modalSizeGuideImage.src = '/' + sizeGuideUrl;
        } else {
            modalSizeGuideImage.src = '';
        }

        if (CKEDITOR.instances['editDescription']) {
            CKEDITOR.instances['editDescription'].setData(productDescription);
        }

        modalProductStatus.value = productStatus ? 'true' : 'false';
    });

    // Detail modal
    var detailProductModal = document.getElementById('productDetailModal');

    detailProductModal.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget;
        var productId = button.getAttribute('data-id');
        var productName = button.getAttribute('data-name');
        var productCode = button.getAttribute('data-code');
        var productWeight = button.getAttribute('data-weight');
        var gsmQualification = button.getAttribute('data-gsmQualification');
        var featureImageUrl = button.getAttribute('data-featureImageUrl');
        var sizeGuideUrl = button.getAttribute('data-sizeGuideUrl');
        var productStatus = button.getAttribute('data-status') === 'true';
        var productDescription = button.getAttribute('data-description');

        var modalProductId = detailProductModal.querySelector('#detailProductId');
        var modalProductCode = detailProductModal.querySelector('#detailProductCode');
        var modalProductName = detailProductModal.querySelector('#detailProductName');
        var modalProductWeight = detailProductModal.querySelector('#detailProductWeight');
        var modalGsmQualification = detailProductModal.querySelector('#detailGsmQualification');
        var modalFeatureImage = detailProductModal.querySelector('#detailFeatureImagePreview');
        var modalSizeGuideImage = detailProductModal.querySelector('#detailSizeGuidePreview');
        var modalProductStatus = detailProductModal.querySelector('#detailProductStatus');
        var modalProductDescription = detailProductModal.querySelector('#detailProductDescription'); // Add reference for description


        // Set the values in the detail modal
        modalProductId.value = productId;
        modalProductCode.value = productCode;
        modalProductName.value = productName;
        modalProductWeight.value = productWeight;
        modalGsmQualification.value = gsmQualification;
        modalProductStatus.value = productStatus ? 'Active' : 'Inactive';
        modalProductDescription.value = productDescription; // Set the description


        // Set image previews
        if (featureImageUrl) {
            modalFeatureImage.src = '/' + featureImageUrl;
        } else {
            modalFeatureImage.src = '';
        }

        if (sizeGuideUrl) {
            modalSizeGuideImage.src = '/' + sizeGuideUrl;
        } else {
            modalSizeGuideImage.src = '';
        }

        if (CKEDITOR.instances['detailProductDescription']) {
            CKEDITOR.instances['detailProductDescription'].setData(productDescription);
        }
    });

    var updateProductStatusModal = document.getElementById('updateProductStatusModal');

    updateProductStatusModal.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget;
        var productId = button.getAttribute('data-id');

        var updateStatusForm = document.getElementById('updateProductStatusForm');
        var action = updateStatusForm.getAttribute('action');
        updateStatusForm.setAttribute('action', action.replace('/0', '/' + productId));
    });
})
;