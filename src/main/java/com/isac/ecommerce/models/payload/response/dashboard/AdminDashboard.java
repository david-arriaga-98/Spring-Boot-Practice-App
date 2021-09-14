package com.isac.ecommerce.models.payload.response.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AdminDashboard {

    @Getter
    @Setter
    private List<IFindProductWithQuantity> bestProducts;

    @Getter
    @Setter
    private List<IBestSellers> bestSellers;

    @Getter
    @Setter
    private IOrderGeneralDescription orderGeneralDescription;

    @Getter
    @Setter
    private Long articles;

    @Getter
    @Setter
    private Long users;

    @Getter
    @Setter
    private Long categories;


}
