package org.ecommercecv.mapper;

import org.ecommercecv.dto.OrderDTO;
import org.ecommercecv.dto.OrderItemDTO;
import org.ecommercecv.model.Order;
import org.ecommercecv.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "items")
    OrderDTO toDto(Order order);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "items", source = "orderItems")
    Order toEntity(OrderDTO orderDTO);

    List<OrderDTO> toDTOs(List<Order> orders);

    List<Order> toEntities(List<OrderDTO> orderDTOs);

    @Mapping(target = "productId", source = "product.id")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    @Mapping(target = "product.id", source = "productId")
    OrderItem toOrderItem(OrderItemDTO orderItemDTO);

    // do not need to duplicate the list mapping because it is already done in the OrderMapper

    List<OrderItemDTO> toOrderItemDTOs(List<OrderItem> orderItem);

    List<OrderItem> toOrderItems(List<OrderItemDTO> orderItemDTO);
}
