console.log("Check header")
function updateCartQuantity() {
    const cartBadge = document.querySelector('.cart-count-badge');
    const cart = JSON.parse(localStorage.getItem('noLoginCart')) || [];
    const totalQuantity = cart.reduce((sum, item) => sum + item.quantity, 0);

    if (totalQuantity > 0) {
        cartBadge.textContent = totalQuantity;
        cartBadge.style.display = 'inline-block';
        cartBadge.style.backgroundColor = 'red';
        cartBadge.style.color = 'white';
        cartBadge.style.borderRadius = '50%';
        cartBadge.style.padding = '5px 10px';
    } else {
        cartBadge.style.display = 'none';
    }
}

window.addEventListener('storage', function(event) {
    if (event.key === 'noLoginCart') {
        updateCartQuantity();
    }
});

updateCartQuantity();
