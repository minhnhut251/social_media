document.addEventListener('DOMContentLoaded', function() {
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    // Xử lý nút "Thêm vào giỏ hàng" cho trang shop
    const addToCartButtons = document.querySelectorAll('.add-to-cart-btn');

    if (addToCartButtons.length > 0) {
        addToCartButtons.forEach(button => {
            button.addEventListener('click', function() {
                const productId = this.getAttribute('data-product-id');
                const quantity = 1; // Default quantity for shop page

                // Show loading state
                const originalText = this.innerHTML;
                this.innerHTML = '<i class="fa fa-spinner fa-spin"></i> Đang thêm...';
                this.disabled = true;

                // Gửi request đến server
                fetch(`/cart/add/${productId}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        [header]: token
                    },
                    body: JSON.stringify({ quantity: quantity })
                })
                .then(response => response.json())
                .then(data => {
                    // Reset button
                    this.innerHTML = originalText;
                    this.disabled = false;

                    if (data.success) {
                        // Show success message
                        showNotification(data.message || 'Sản phẩm đã được thêm vào giỏ hàng', 'success');

                        // Update cart count
                        updateCartCount();
                    } else {
                        showNotification(data.message || 'Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng', 'error');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    this.innerHTML = originalText;
                    this.disabled = false;
                    showNotification('Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng', 'error');
                });
            });
        });
    }

    // Xử lý nút "Mua ngay" cho trang shop
    const buyNowButtons = document.querySelectorAll('.buy-now-btn');

    if (buyNowButtons.length > 0) {
        buyNowButtons.forEach(button => {
            button.addEventListener('click', function() {
                const productId = this.getAttribute('data-product-id');
                const quantity = 1; // Default quantity for shop page

                // Show loading state
                const originalText = this.innerHTML;
                this.innerHTML = '<i class="fa fa-spinner fa-spin"></i> Đang xử lý...';
                this.disabled = true;

                // Gửi request đến server để thêm vào giỏ hàng
                fetch(`/cart/add/${productId}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        [header]: token
                    },
                    body: JSON.stringify({ quantity: quantity })
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        // Chuyển hướng đến trang giỏ hàng
                        window.location.href = '/cart';
                    } else {
                        // Reset button
                        this.innerHTML = originalText;
                        this.disabled = false;
                        showNotification(data.message || 'Có lỗi xảy ra khi thêm sản phẩm', 'error');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    this.innerHTML = originalText;
                    this.disabled = false;
                    showNotification('Có lỗi xảy ra khi xử lý yêu cầu', 'error');
                });
            });
        });
    }

    // Hàm cập nhật số lượng sản phẩm trong giỏ hàng
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