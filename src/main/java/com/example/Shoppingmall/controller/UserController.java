package com.example.Shoppingmall.controller;

import com.example.Shoppingmall.entity.*;
import com.example.Shoppingmall.form.*;
import com.example.Shoppingmall.repository.ReviewRepository;
import com.example.Shoppingmall.repository.ShoppingRepository;
import com.example.Shoppingmall.repository.UserRepository;
import com.example.Shoppingmall.service.CartService;
import com.example.Shoppingmall.service.EmailService;
import com.example.Shoppingmall.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@SessionAttributes("emailCode")
@RequestMapping("/shop")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductService productService;

    @Autowired
    ShoppingRepository shoppingRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    EmailService emailService;

    @GetMapping
    public String home(@RequestParam(value = "category", defaultValue = "all") String category,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "8") int size,
                       Model model,
                       HttpSession session) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> products;

        if("all".equals(category)) {
            products = productService.getAllProducts(pageable);
        } else {
            products = productService.getProductsByCategory(category, pageable);
        }

        int totalPages = products.getTotalPages();
        int pageGroupSize = 10;
        int currentGroupStartPage = (page / pageGroupSize) * pageGroupSize;
        int currentGroupEndPage = Math.min(currentGroupStartPage + pageGroupSize - 1, totalPages - 1);

        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("totalElements", products.getTotalElements());
        model.addAttribute("category", category);
        model.addAttribute("currentGroupStartPage", currentGroupStartPage);
        model.addAttribute("currentGroupEndPage", currentGroupEndPage);
        model.addAttribute("hasPreviousGroup", currentGroupStartPage > 0);
        model.addAttribute("hasNextGroup", currentGroupEndPage < totalPages - 1);

        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        return "/shop/home";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("productName") String productName,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "8") int size,
                                 Model model,
                                 HttpSession session) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> products = productService.searchProductsByName(productName, pageable);

        if (page >= products.getTotalPages() && products.getTotalPages() > 0){
            return "redirect:/shop/search?productName= " + productName + "&page=" + (products.getTotalPages() - 1) + "&size=" + size;
        }

        int totalPages = products.getTotalPages();
        int pageGroupSize = 10;
        int currentGroupStartPage = (page / pageGroupSize) * pageGroupSize;
        int currentGroupEndPage = Math.min(currentGroupStartPage + pageGroupSize - 1, totalPages - 1);

        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("totalElements", products.getTotalElements());
        model.addAttribute("productName", productName);
        model.addAttribute("currentGroupStartPage", currentGroupStartPage);
        model.addAttribute("currentGroupEndPage", currentGroupEndPage);
        model.addAttribute("hasPreviousGroup", currentGroupStartPage > 0);
        model.addAttribute("hasNextGroup", currentGroupEndPage < totalPages - 1);

        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        return "/shop/home";
    }

    @GetMapping("/category/{category}")
    public String getCategoryProducts(@PathVariable("category") String category,
                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "8") int size,
                                      Model model,
                                      HttpSession session) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> products = productService.getProductsByCategory(category, pageable);

        int totalPages = products.getTotalPages();
        int pageGroupSize = 10;
        int currentGroupStartPage = (page / pageGroupSize) * pageGroupSize;
        int currentGroupEndPage = Math.min(currentGroupStartPage + pageGroupSize - 1, totalPages - 1);

        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("totalElements", products.getTotalElements());
        model.addAttribute("category", category);
        model.addAttribute("currentGroupStartPage", currentGroupStartPage);
        model.addAttribute("currentGroupEndPage", currentGroupEndPage);
        model.addAttribute("hasPreviousGroup", currentGroupStartPage > 0);
        model.addAttribute("hasNextGroup", currentGroupEndPage < totalPages - 1);

        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        return "/shop/home";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@Validated @ModelAttribute UserForm userForm,
                               @RequestParam("emailCode") String emailCode,
                               BindingResult result,
                               HttpSession session,
                               Model model) {
        String sessionEmailCode = (String) session.getAttribute("emailCode");

        if(result.hasErrors()) {
            return "register";
        }

        if(userRepository.findByLoginId(userForm.getLoginId()) != null) {
            model.addAttribute("msg", "이미 존재하는 아이디입니다.");
            return "register";
        }

        if(userForm.getPassword().length() < 7 || userForm.getPassword().length() > 15) {
            model.addAttribute("msg", "비밀번호는 7자 이상 15자 이하로 설정해야 합니다.");
            return "register";
        }

        if(!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            model.addAttribute("msg", "두 비밀번호가 일치하지 않습니다.");
            return "register";
        }

        if(!emailCode.equals(sessionEmailCode)) {
            model.addAttribute("msg", "인증번호가 일치하지 않습니다.");
            return "register";
        }

        User user = new User();
        user.setUsername(userForm.getUsername());
        user.setLoginId(userForm.getLoginId());
        user.setPassword(userForm.getPassword());
        user.setConfirmPassword(userForm.getConfirmPassword());
        user.setTel(userForm.getTel());
        user.setEmail(userForm.getEmail());
        user.setMoney(userForm.getMoney() != null ? userForm.getMoney() : 100000);
        user.setRole(userForm.getRole() != null ? userForm.getRole() : "ROLE_USER");

        userRepository.save(user);

        session.removeAttribute("emailCode");

        return "redirect:/shop/login?success=true";
    }

    @PostMapping("/register/sendEmailCode")
    public String emailCode(@RequestParam("email") String email,
                            HttpSession session,
                            Model model) {
        String emailCode = String.format("%04d", new Random().nextInt(10000));

        emailService.sendEmailCode(email, emailCode);
        session.setAttribute("emailCode", emailCode);
        model.addAttribute("emailSent", true);
        model.addAttribute("userForm", new UserForm());

        return "register";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute UserForm userForm,
                            Model model,
                            HttpSession session) {
        User user = userRepository.findByLoginId(userForm.getLoginId());
        if(user != null && user.getPassword().equals(userForm.getPassword())) {
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            return "redirect:/shop";
        } else {
            model.addAttribute("message", "아이디 또는 비밀번호가 틀렸습니다.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/shop";
    }

    @GetMapping("/product/details/{id}")
    public String getProductDetails(@PathVariable("id") Integer id,
                                    Model model,
                                    HttpSession session) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            User user = (User) session.getAttribute("user");
            if(user != null) {
                Cart cart = cartService.getCart(user.getId());
                List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
                model.addAttribute("cartItems", cartItems);
                model.addAttribute("userMoney", user.getMoney());
            }

            model.addAttribute("product", product);

            List<Review> reviews = reviewRepository.findByProductId(id);
            model.addAttribute("reviews", reviews);

            return "/shop/productDetails";
        }
        return "redirect:/shop";
    }

    @PostMapping("/product/buy")
    public String buyProduct(@RequestParam("productId") Integer productId,
                             @RequestParam("quantity") Integer quantity,
                             HttpSession session,
                             Model model) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        if (user == null) {
            return "redirect:/shop/login?redirect=/shop/product/details/" + productId;
        }

        Optional<Product> productOptional = productService.getProductById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (product.getStock() >= quantity) {
                int totalPrice = product.getPrice() * quantity;
                if (user.getMoney() >= totalPrice) {
                    product.setStock(product.getStock() - quantity);
                    product.setSalesCount(product.getSalesCount() + quantity);

                    user.setMoney(user.getMoney() - totalPrice);
                    productService.updateProduct(product);
                    userRepository.save(user);

                    cartService.removeItemFromCart(user.getId(), productId);

                    ShoppingList shoppingList = new ShoppingList();
                    shoppingList.setUserId(user.getId());
                    shoppingList.setUsername(user.getUsername());
                    shoppingList.setProductId(productId);
                    shoppingList.setProductName(product.getName());
                    shoppingList.setQuantity(quantity);
                    shoppingList.setTotalPrice(totalPrice);
                    shoppingList.setShoppingDate(LocalDateTime.now());
                    shoppingRepository.save(shoppingList);

                    return "redirect:/shop";
                } else {
                    model.addAttribute("message", "보유금액이 부족합니다.");
                    model.addAttribute("product", product);
                    return "productDetails";
                }
            } else {
                model.addAttribute("message", "재고가 부족합니다.");
                model.addAttribute("product", product);
                return "productDetails";
            }
        }

        return "redirect:/shop";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        if (user == null) {
            return "redirect:/shop/login";
        }

        model.addAttribute("user", user);
        return "/shop/profile";
    }

    @GetMapping("/shoppingList")
    public String shoppingList(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               Model model,
                               HttpSession session) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("shoppingDate").descending());
        Page<ShoppingList> shoppingListPage;

        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        if (user == null) {
            return "redirect:/shop/login";
        }

        shoppingListPage = shoppingRepository.findByUserId(user.getId(), pageable);

        if (page >= shoppingListPage.getTotalPages() && shoppingListPage.getTotalPages() > 0) {
            return "redirect:/shop/shoppingList?page=" + (shoppingListPage.getTotalPages() - 1) + "&size=" + size;
        }

        int totalPages = shoppingListPage.getTotalPages();
        int pageGroupSize = 10;
        int currentGroupStartPage = (page / pageGroupSize) * pageGroupSize;
        int currentGroupEndPage = Math.min(currentGroupStartPage + pageGroupSize - 1, totalPages - 1);

        model.addAttribute("shoppingLists", shoppingListPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", shoppingListPage.getTotalPages());
        model.addAttribute("totalElements", shoppingListPage.getTotalElements());
        model.addAttribute("currentGroupStartPage", currentGroupStartPage);
        model.addAttribute("currentGroupEndPage", currentGroupEndPage);
        model.addAttribute("hasPreviousGroup", currentGroupStartPage > 0);
        model.addAttribute("hasNextGroup", currentGroupEndPage < totalPages - 1);

        return "/shop/shoppingList";
    }

    @GetMapping("/review/{id}")
    public String getWriteReviewPage(@PathVariable("id") Integer id,
                                     Model model,
                                     HttpSession session) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            User user = (User) session.getAttribute("user");
            if(user != null) {
                Cart cart = cartService.getCart(user.getId());
                List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
                model.addAttribute("cartItems", cartItems);
                model.addAttribute("userMoney", user.getMoney());
            }

            if (user == null) {
                return "redirect:/shop/login";
            }

            model.addAttribute("product", product);
            return "/shop/writeReview";
        }
        return "/shop/writeReview";
    }

    @PostMapping("/review/save")
    public String writeReview(@Validated @ModelAttribute ReviewForm reviewForm,
                             BindingResult result,
                             @RequestParam("imgFile") MultipartFile imgFile,
                             HttpSession session,
                             Model model) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        if (user == null) {
            return "redirect:/shop/login";
        }

        if(result.hasErrors()) {
            model.addAttribute("product", productService.getProductById(reviewForm.getProductId()));
            return "/shop/writeReview";
        }

        if(reviewForm.getContent() == null || reviewForm.getContent().trim().isEmpty()) {
            model.addAttribute("message", "리뷰 내용을 작성해주세요.");
            model.addAttribute("product", productService.getProductById(reviewForm.getProductId()));
            return "/shop/writeReview";
        }

        Review review = new Review();
        review.setProductId(reviewForm.getProductId());
        review.setUserId(user.getId());
        review.setUsername(user.getUsername());
        review.setContent(reviewForm.getContent());
        review.setRating(reviewForm.getRating());
        review.setCreateDate(LocalDateTime.now());

        Optional<Product> productOptional = productService.getProductById(reviewForm.getProductId());
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            review.setProductName(product.getName());
        } else {
            model.addAttribute("message", "유효하지 않은 상품입니다.");
            model.addAttribute("product", productService.getProductById(reviewForm.getProductId()));
            return "/shop/writeReview";
        }

        if (imgFile != null && !imgFile.isEmpty()) {
            try {
                byte[] fileBytes = imgFile.getBytes();
                String base64EncodedImage = Base64.getEncoder().encodeToString(fileBytes);
                review.setImageUrl(base64EncodedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        reviewRepository.save(review);

        return "redirect:/shop";
    }

    @GetMapping("/showReviews")
    public String showReviews(@RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "8") int size,
                              HttpSession session,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<Review> reviewPage = reviewRepository.findAll(pageable);

        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        model.addAttribute("totalElements", reviewPage.getTotalElements());

        int totalPages = reviewPage.getTotalPages();
        int pageGroupSize = 10;
        int currentGroupStartPage = (page / pageGroupSize) * pageGroupSize;
        int currentGroupEndPage = Math.min(currentGroupStartPage + pageGroupSize - 1, totalPages - 1);

        model.addAttribute("currentGroupStartPage", currentGroupStartPage);
        model.addAttribute("currentGroupEndPage", currentGroupEndPage);
        model.addAttribute("hasPreviousGroup", currentGroupStartPage > 0);
        model.addAttribute("hasNextGroup", currentGroupEndPage < totalPages - 1);

        Map<Integer, String> productNames = new HashMap<>();
        Set<Integer> productIds = new HashSet<>();
        for (Review review : reviewPage.getContent()) {
            productIds.add(review.getProductId());
        }

        for (Integer productId : productIds) {
            Optional<Product> product = productService.getProductById(productId);
            product.ifPresent(p -> productNames.put(productId, p.getName()));
        }

        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        model.addAttribute("productNames", productNames);

        return "/shop/showReviews";
    }

    @GetMapping("/changePassword")
    public String getChangePassword(HttpSession session,
                                    Model model) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        if (user == null) {
            return "redirect:/shop/login";
        }

        model.addAttribute("changePasswordForm", new PasswordForm());

        return "/shop/changePassword";
    }

    @PostMapping("/changePassword")
    public String postChangePassword(@Validated @ModelAttribute PasswordForm passwordForm,
                                     HttpSession session,
                                     Model model) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        if (user == null) {
            return "redirect:/shop/login";
        }

        if (!user.getPassword().equals(passwordForm.getCurrentPassword())) {
            model.addAttribute("message", "현재 비밀번호가 일치하지 않습니다.");
            return "/shop/changePassword";
        }

        if(passwordForm.getNewPassword().length() < 7 || passwordForm.getNewPassword().length() > 15) {
            model.addAttribute("message", "비밀번호는 7자 이상 15자 이하로 설정해야 합니다.");
            return "/shop/changePassword";
        }

        if(user.getPassword().equals(passwordForm.getNewPassword())) {
            model.addAttribute("message", "새 비밀번호는 현재 비밀번호와 다르게 설정해야 합니다.");
            return "/shop/changePassword";
        }

        if (!passwordForm.getNewPassword().equals(passwordForm.getConfirmNewPassword())) {
            model.addAttribute("message", "새 비밀번호의 두 값이 일치하지 않습니다.");
            return "/shop/changePassword";
        }

        user.setPassword(passwordForm.getNewPassword());
        userRepository.save(user);

        return "redirect:/shop/profile?passwordSuccess=true";
    }

    @GetMapping("/cash")
    public String getCash(HttpSession session,
                          Model model) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        if (user == null) {
            return "redirect:/shop/login";
        }

        model.addAttribute("cashForm", new CashForm());
        return "/shop/cash";
    }

    @PostMapping("/cash")
    public String postCash(@Validated @ModelAttribute CashForm cashForm,
                           HttpSession session,
                           Model model) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        if (user == null) {
            return "redirect:/shop/login";
        }

        int cash = cashForm.getCash();

        if (cash < 1000) {
            model.addAttribute("message", "1000원 이상만 충전할 수 있습니다.");
            return "/shop/cash";
        }

        user.setMoney(user.getMoney() + cash);
        userRepository.save(user);

        model.addAttribute("userMoney", user.getMoney());

        return "redirect:/shop/profile?cashSuccess=true";
    }

    @GetMapping("/popular")
    public String popularProduct(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "8") int size,
                                 HttpSession session,
                                 Model model) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("salesCount").descending());
        Page<Product> popularProducts = productService.getPopularProducts(pageable);

        int totalPages = popularProducts.getTotalPages();
        int pageGroupSize = 10;
        int currentGroupStartPage = (page / pageGroupSize) * pageGroupSize;
        int currentGroupEndPage = Math.min(currentGroupStartPage + pageGroupSize - 1, totalPages - 1);

        model.addAttribute("products", popularProducts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("totalElements", popularProducts.getTotalElements());
        model.addAttribute("currentGroupStartPage", currentGroupStartPage);
        model.addAttribute("currentGroupEndPage", currentGroupEndPage);
        model.addAttribute("hasPreviousGroup", currentGroupStartPage > 0);
        model.addAttribute("hasNextGroup", currentGroupEndPage < totalPages - 1);

        return "/shop/popular";
    }
}

