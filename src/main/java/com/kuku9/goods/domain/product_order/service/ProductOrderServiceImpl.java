package com.kuku9.goods.domain.product_order.service;

import com.kuku9.goods.domain.order_product.entity.OrderProduct;
import com.kuku9.goods.domain.order_product.repository.OrderProductRepository;
import com.kuku9.goods.domain.product.dto.ProductResponse;
import com.kuku9.goods.domain.product.entity.Product;
import com.kuku9.goods.domain.product.repository.ProductRepository;
import com.kuku9.goods.domain.product_order.dto.ProductOrderResponse;
import com.kuku9.goods.domain.product_order.dto.ProductOrdersRequest;
import com.kuku9.goods.domain.product_order.entity.ProductOrder;
import com.kuku9.goods.domain.product_order.repository.ProductOrderRepository;
import com.kuku9.goods.domain.user.entity.User;
import com.kuku9.goods.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductOrderServiceImpl implements ProductOrderService {

    private final ProductOrderRepository productOrderRepository;
    private final OrderProductRepository orderProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductOrder createOrder(User user, ProductOrdersRequest productOrderRequest) {
        userRepository.findById(user.getId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        ProductOrder productOrder = productOrderRepository.save(
            new ProductOrder(user, productOrderRequest.getAddress()));
        for (int i = 0; i < productOrderRequest.getProducts().size(); i++) {
            Product product = productRepository.findById(
                    productOrderRequest.getProducts().get(i).getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
            orderProductRepository.save(new OrderProduct(productOrder, product,
                productOrderRequest.getProducts().get(i).getQuantity()));
        }
        return productOrder;
    }

    @Override
    public ProductOrderResponse getOrder(User user, Long orderId) {
        ProductOrder productOrder = productOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        if (!productOrder.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 주문에 접근할 수 없습니다.");
        }
        return getProductOrderResponse(orderId, productOrder);
    }

    @Override
    //결제 수정권은 누가 가지고 있나요?
    public ProductOrderResponse updateOrder(User user, Long orderId) {
        ProductOrder productOrder = productOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        if (!productOrder.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 주문에 접근할 수 없습니다.");
        }
        productOrder.updateStatus("결제 취소");
        return getProductOrderResponse(orderId, productOrder);
    }

    @Override
    public void deleteOrder(User user, Long orderId) {
        ProductOrder productOrder = productOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        if (!productOrder.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 주문에 접근할 수 없습니다.");
        }
        productOrderRepository.delete(productOrder);
    }

    private ProductOrderResponse getProductOrderResponse(Long orderId, ProductOrder productOrder) {
        List<OrderProduct> orderProducts = orderProductRepository.findAllByProductOrderId(orderId);
        List<ProductResponse> products = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            products.add(new ProductResponse(orderProduct.getProduct()));
        }
        return new ProductOrderResponse(productOrder, products);
    }


}