package com.example.Shoppingmall.controller;
import com.example.Shoppingmall.entity.Cart;
import com.example.Shoppingmall.entity.CartItem;
import com.example.Shoppingmall.entity.User;
import com.example.Shoppingmall.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/shop/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping
    public String Cart(HttpSession session,
                       Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/shop/login";
        }

        model.addAttribute("userMoney", user.getMoney());

        Cart cart = cartService.getCart(user.getId());
        if (cart == null) {
            return "/shop/cart";
        }

        List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();

        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", cartItems);

        return "/shop/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId,
                            @RequestParam Integer quantity,
                            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/shop/login";
        }
        cartService.addItemToCart(user.getId(), productId, quantity);
        return "redirect:/shop";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Integer itemId,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/shop/login";
        }

        cartService.removeItemFromCart(user.getId(), itemId);
        return "redirect:/shop/cart";
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam Integer itemId,
                                 @RequestParam Integer quantity,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/shop/login";
        }
        cartService.updateCartItemQuantity(itemId, quantity);
        return "redirect:/shop/cart";
    }

    @PostMapping("/purchaseAll")
    public String checkout(HttpSession session) throws RuntimeException {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/shop/login";
        }
        Cart cart = cartService.getCart(user.getId());
        if (cart != null) {
            cartService.checkout(cart.getId(), user);
        }
        return "redirect:/shop/cart";
    }

    @PostMapping("/purchaseSelected")
    public String purchaseItem(@RequestParam("selectedItems") List<Integer> itemIds,
                               HttpSession session) throws RuntimeException {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/shop/login";
        }

        for (Integer itemId : itemIds) {
            cartService.purchaseItem(itemId, user);
        }

        return "redirect:/shop/cart";
    }

    @PostMapping("clear")
    public String clearCart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/shop/login";
        }

        cartService.clearCart(user.getId());

        return "redirect:/shop/cart";
    }
}
