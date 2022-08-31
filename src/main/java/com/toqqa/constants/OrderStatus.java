package com.toqqa.constants;

public enum OrderStatus {
    /*Don't shuffle or change the order constants*/
    CANCELLED("Cancelled"), PLACED("Placed"), RECEIVED("Received"), CONFIRMED("Confirmed"), READY_FOR_DISPATCH("Ready for Dispatch"), OUT_FOR_DELIVERY("Out for Delivery"), DELIVERED("Delivered");
    private final String value;

    OrderStatus(String orderStatus) {
        this.value = orderStatus;
    }

    public String getValue() {
        return value;
    }
}
