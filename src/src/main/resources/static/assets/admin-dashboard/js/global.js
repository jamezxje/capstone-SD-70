document.addEventListener("DOMContentLoaded", function () {
    const messageElement = document.querySelector('meta[name="flash-message"]');
    const typeElement = document.querySelector('meta[name="flash-type"]');

    if (messageElement && typeElement) {
        const message = messageElement.content || '';
        const type = typeElement.content || '';

        if (message && type) {
            if (type === 'success') {
                toastr.success(message);
            } else if (type === 'error') {
                toastr.error(message);
            }
        }
    }
});

toastr.options = {
    "closeButton": true,
    "debug": false,
    "newestOnTop": true,
    "progressBar": true,
    "positionClass": "toast-bottom-right",
    "preventDuplicates": false,
    "onclick": null,
    "showDuration": "300",
    "hideDuration": "1000",
    "timeOut": "5000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
};
