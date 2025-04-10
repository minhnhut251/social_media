document.addEventListener('DOMContentLoaded', function() {
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    // Xử lý nút tăng/giảm số lượng
    const decreaseBtn = document.querySelector('.decrease-qty');
    const increaseBtn = document.querySelector('.increase-qty');
    const quantityInput = document.getElementById('product-quantity');

    if (decreaseBtn) {
        decreaseBtn.addEventListener('click', function() {
            let value = parseInt(quantityInput.value);
            if (value > 1) {
                quantityInput.value = value - 1;
            }
        });
    }

    if (increaseBtn) {
        increaseBtn.addEventListener('click', function() {
            let value = parseInt(quantityInput.value);
            if (value < 99) {
                quantityInput.value = value + 1;
            }
        });
    }

    // Xử lý nút "Thêm vào giỏ hàng"
    const addToCartBtn = document.getElementById('add-to-cart-btn');

    if (addToCartBtn) {
        addToCartBtn.addEventListener('click', function() {
            const productId = this.getAttribute('data-product-id');
            const quantity = document.getElementById('product-quantity').value;

            // Gửi request đến server
            fetch(`/cart/add/${productId}?quantity=${quantity}`, {
                method: 'POST',
                headers: {
                    [header]: token
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Hiển thị thông báo thành công
                    alert(data.message);

                    // Cập nhật số lượng sản phẩm trong giỏ hàng (nếu có hiển thị)
                    updateCartCount();
                } else {
                    alert(data.message || 'Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng');
            });
        });
    }

    // Hàm cập nhật số lượng sản phẩm trong giỏ hàng (nếu bạn có hiển thị số lượng trên navbar)
    function updateCartCount() {
        fetch('/cart/count', {
            headers: {
                [header]: token
            }
        })
        .then(response => response.json())
        .then(data => {
            const cartCountElement = document.getElementById('cart-count');
            if (cartCountElement) {
                cartCountElement.textContent = data.count;
                if (data.count > 0) {
                    cartCountElement.classList.remove('d-none');
                }
            }
        })
        .catch(error => console.error('Error:', error));
    }
});