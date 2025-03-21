package org.fpoly.capstone.controller.user_online;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.OnlineAddressService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.address.CreateAddressRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("online/customer")
@RequiredArgsConstructor
public class OnlineCustomerController {

    private final UserService userService;
    private final OnlineAddressService onlineAddressService;

    @GetMapping(path = "profile")
    public String onOpenCustomerProfile(Model model) {
        User loggedUser = this.userService.getUserFromContext();

        List<Address> listAddress = this.onlineAddressService.getListAddressByLoggedUser();

        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("listAddress", listAddress);
        model.addAttribute("createAddressRequest", new CreateAddressRequest());
        model.addAttribute("updateAddressRequest", new CreateAddressRequest());

        return "/views/user-online-view/customer/profile";
    }

    @PostMapping(path = "address/delete/{id}")
    public String deleteAddress(@PathVariable Integer id) {

        try {
            this.onlineAddressService.deleteAddress(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/online/customer/profile";

    }

    @PostMapping(path = "address/set-default/{id}")
    public String setDefaultAddress(@PathVariable Integer id) {

        try {
            this.onlineAddressService.setDefaultAddress(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/online/customer/profile";

    }
}
