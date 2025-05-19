package org.fpoly.capstone.controller.user_online.api;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.cart.AddProductToCartModel;
import org.fpoly.capstone.controller.payload.cart_detail.CartDetailUpdateModel;
import org.fpoly.capstone.exceptions.ErrorResponse;
import org.fpoly.capstone.service.CartDetailService;
import org.fpoly.capstone.service.CartService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.cart.AddProductToCartRequest;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "cart")
@RequiredArgsConstructor
public class ApiCartController {

    private final CartService cartService;
    private final CartDetailService cartDetailService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @PostMapping(path = "api/add")
    public ResponseEntity<?> addToCartApi(@RequestBody AddProductToCartModel addProductToCartModel) {
        try {
            AddProductToCartRequest addProductToCartRequest = this.modelMapper.map(addProductToCartModel, AddProductToCartRequest.class);
            this.cartService.addToCart(addProductToCartRequest);

            // Return a successful response
            return new ResponseEntity<>("Cart updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception here for better traceability
            // Log the exception here for better traceability
            e.printStackTrace();

            // Return an error response with detailed error message and error code
            ErrorResponse errorResponse = new ErrorResponse("Thêm vào giỏ hàng thất bại: " + e.getMessage(), "CART_UPDATE_ERROR");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "update")
    public ResponseEntity<?> updateCart(@RequestBody CartDetailUpdateModel cartDetailUpdateModel) {
        try {
            // Convert the incoming model to the request object
            CartDetailUpdateRequest request = this.modelMapper.map(cartDetailUpdateModel, CartDetailUpdateRequest.class);

            // Update the cart details
            this.cartDetailService.updateCartDetail(request);

            // Return a successful response
            return new ResponseEntity<>("Cart updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            // Return an error response with appropriate status
//            return new ResponseEntity<>("Failed to update cart: " + e.getMessage(), HttpStatus.BAD_REQUEST);
            ErrorResponse errorResponse = new ErrorResponse("Sửa số lượng sản phẩm thất bại: " + e.getMessage(), "CART_UPDATE_ERROR");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
