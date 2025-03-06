package org.fpoly.capstone.controller.payload.cart_detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailUpdateRequest;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailUpdateModel {

    private Map<Long, CartDetailUpdateRequest> requests = new HashMap<>();

    // Getter and setter for requests
    public Map<Long, CartDetailUpdateRequest> getRequests() {
        return this.requests;
    }

    public void setRequests(Map<Long, CartDetailUpdateRequest> requests) {
        this.requests = requests;
    }

}
