package com.isac.ecommerce.models.payload.response.dashboard;

public interface IOrderGeneralDescription {

    Double getTotal();

    Integer getQuantity();

    Double getImposition();

    Integer getDisabledOrders();

    Integer getEnabledOrders();

    Integer getOrders();

}
