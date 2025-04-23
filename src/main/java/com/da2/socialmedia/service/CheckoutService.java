package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.*;
import com.da2.socialmedia.repository.AddressRepository;
import com.da2.socialmedia.repository.CartItemRepository;
import com.da2.socialmedia.repository.OrderRepository;
import com.da2.socialmedia.repository.TKBHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CheckoutService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final CartService cartService;
    private final TKBHRepository tkbhRepository;

    @Autowired
    public CheckoutService(OrderRepository orderRepository, CartItemRepository cartItemRepository,
                           AddressRepository addressRepository, CartService cartService, TKBHRepository tkbhRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.addressRepository = addressRepository;
        this.cartService = cartService;
        this.tkbhRepository = tkbhRepository;
    }

    @Transactional
    public String createOrder(User user, Long addressId, List<Long> itemIds, Map<String, String> params,
                              String paymentMethod, String note, double subtotal, double shippingFee,
                              double discount, double total) {

        // Kiểm tra địa chỉ
        AddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Địa chỉ không tồn tại"));

        // Kiểm tra các mặt hàng
        List<CartItemEntity> cartItems = new ArrayList<>();
        for (Long itemId : itemIds) {
            CartItemEntity item = cartItemRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mặt hàng trong giỏ hàng"));

            // Kiểm tra xem mặt hàng có thuộc về người dùng hiện tại không
            if (!item.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Không có quyền đặt hàng cho mặt hàng này");
            }

            // Kiểm tra số lượng từ tham số
            String quantityParam = params.get("quantities[" + itemId + "]");
            if (quantityParam != null) {
                int quantity = Integer.parseInt(quantityParam);
                if (quantity > 0) {
                    item.setQuantity(quantity);
                }
            }

            cartItems.add(item);
        }

        // Tạo mã đơn hàng
        String orderCode = generateOrderCode();

        // Tạo đối tượng đơn hàng
        OrderEntity order = new OrderEntity();
        order.setOrderCode(orderCode);
        order.setUser(user);
//        order.setAddress(address);
        order.setSubtotal(subtotal);
        order.setShippingFee(shippingFee);
        order.setDiscount(discount);
        order.setTotal(total);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("PENDING"); // Trạng thái ban đầu là "Chờ xử lý"
        order.setNote(note);
        order.setCreatedAt(new Date());

        // Lưu đơn hàng để có ID
        OrderEntity savedOrder = orderRepository.save(order);

        // Tạo các mục đơn hàng
        for (CartItemEntity cartItem : cartItems) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getGia());
            orderItem.setTotal(cartItem.getProduct().getGia() * cartItem.getQuantity());

            // Thêm vào danh sách các mục đơn hàng
            savedOrder.getItems().add(orderItem);

            // Them tien vao wallet tkbh
            TaiKhoanBanHangEntity taiKhoanBanHang = tkbhRepository.findById(cartItem.getProduct().getTkbh().getMatkbh())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tkbh"));
            taiKhoanBanHang.setWallet(taiKhoanBanHang.getWallet() + orderItem.getTotal());
            tkbhRepository.save(taiKhoanBanHang);
        }

        // Lưu lại đơn hàng với các mục đơn hàng
        orderRepository.save(savedOrder);

        return orderCode;
    }

    @Transactional
    public void removeOrderedItemsFromCart(User user, List<Long> itemIds) {
        for (Long itemId : itemIds) {
            cartService.removeFromCart(itemId, user);
        }
    }

    private String generateOrderCode() {
        // Tạo mã đơn hàng, ví dụ: DH + timestamp + 4 số ngẫu nhiên
        long timestamp = System.currentTimeMillis();
        int randomNum = new Random().nextInt(10000);
        return "DH" + timestamp % 10000 + String.format("%04d", randomNum);
    }
}