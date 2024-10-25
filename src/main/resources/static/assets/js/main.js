window.onload = function () {
    // Success alert timeout
    const successAlert = document.getElementById('successAlert');
    if (successAlert) {
        setTimeout(function () {
            // Bootstrap 5 hide method
            var alert = new bootstrap.Alert(successAlert);
            alert.close(); // Dismiss the alert
        }, 2000); // Set timeout for 3 seconds
    }

    // Error alert timeout
    const errorAlert = document.getElementById('errorAlert');
    if (errorAlert) {
        setTimeout(function () {
            // Bootstrap 5 hide method
            var alert = new bootstrap.Alert(errorAlert);
            alert.close(); // Dismiss the alert
        }, 2000); // Set timeout for 3 seconds
    }
};