package com.example.Shoppingmall.controller;

import com.example.Shoppingmall.entity.*;
import com.example.Shoppingmall.form.NoticeBoardForm;
import com.example.Shoppingmall.form.ProductForm;
import com.example.Shoppingmall.repository.NoticeBoardRepository;
import com.example.Shoppingmall.repository.ShoppingRepository;
import com.example.Shoppingmall.service.CartService;
import com.example.Shoppingmall.service.ProductService;
import com.example.Shoppingmall.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class AdminController {
    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    ShoppingRepository shoppingRepository;

    @Autowired
    CartService cartService;

    @Autowired
    NoticeBoardRepository noticeBoardRepository;

    private String checkAdminAndRedirect(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("ROLE_ADMIN")) {
            return "redirect:/shop?adminOnly=true";
        }
        return null;
    }

    @GetMapping("/admin/users")
    public String users(@RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        Model model,
                        HttpSession session) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> userPage = userService.getAllUsers(pageable);

        int totalPages = userPage.getTotalPages();
        int pageGroupSize = 10;
        int currentGroupStartPage = (page / pageGroupSize) * pageGroupSize;
        int currentGroupEndPage = Math.min(currentGroupStartPage + pageGroupSize - 1, totalPages - 1);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalElements", userPage.getTotalElements());
        model.addAttribute("currentGroupStartPage", currentGroupStartPage);
        model.addAttribute("currentGroupEndPage", currentGroupEndPage);
        model.addAttribute("hasPreviousGroup", currentGroupStartPage > 0);
        model.addAttribute("hasNextGroup", currentGroupEndPage < totalPages - 1);

        return "/admin/userList";
    }

    @GetMapping("/admin/product")
    public String listProducts(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "8") int size,
                               Model model,
                               HttpSession session) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("updateDate").descending());
        Page<Product> products = productService.getAllProducts(pageable);

        int totalPages = products.getTotalPages();
        int pageGroupSize = 10;
        int currentGroupStartPage = (page / pageGroupSize) * pageGroupSize;
        int currentGroupEndPage = Math.min(currentGroupStartPage + pageGroupSize - 1, totalPages - 1);

        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("totalElements", products.getTotalElements());
        model.addAttribute("currentGroupStartPage", currentGroupStartPage);
        model.addAttribute("currentGroupEndPage", currentGroupEndPage);
        model.addAttribute("hasPreviousGroup", currentGroupStartPage > 0);
        model.addAttribute("hasNextGroup", currentGroupEndPage < totalPages - 1);

        return "/admin/product";
    }

    @GetMapping("/admin/product/new")
    public String showNewProductForm(Model model,
                                     HttpSession session) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        model.addAttribute("productForm", new ProductForm());
        return "/admin/productForm";
    }

    @PostMapping("/admin/product")
    public String insertProduct(@Validated @ModelAttribute ProductForm productForm,
                                BindingResult result,
                                HttpSession session) throws IOException {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        if(result.hasErrors()) {
            return "/admin/productForm";
        }

        Product product = new Product();
        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setStock(productForm.getStock());
        product.setCategory(productForm.getCategory());
        product.setCreateDate(LocalDateTime.now());
        product.setUpdateDate(LocalDateTime.now());

        MultipartFile imgFile = productForm.getImgFile();
        if (imgFile != null && !imgFile.isEmpty()) {
            byte[] fileBytes = imgFile.getBytes();
            String base64EncodedImage = Base64.getEncoder().encodeToString(fileBytes);
            product.setImageUrl(base64EncodedImage);
        }

        productService.insertProduct(product);

        try {
            String encodedMessage = URLEncoder.encode("새로운 상품이 추가되었습니다.", "UTF-8");
            return "redirect:/shop/admin/product?message=" + encodedMessage;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "redirect:/shop/admin/product";
        }
    }

    @GetMapping("/admin/product/addInventory/{id}")
    public String showAddInventory(@PathVariable("id") Integer id,
                                   Model model,
                                   HttpSession session) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        Optional<Product> productOptional = productService.getProductById(id);
        if(productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);
            return "/admin/addInventory";
        } else {
            return "redirect:/shop/admin/product";
        }
    }

    @PostMapping("/admin/product/addInventory")
    public String addInventory(@RequestParam("id") Integer id,
                               @RequestParam("quantity") Integer quantity,
                               Model model,
                               HttpSession session) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(product.getName());
            product.setStock(product.getStock() + quantity);
            productService.saveProduct(product);

            model.addAttribute("product", product);

            try {
                String encodedMessage = URLEncoder.encode("해당 상품의 재고가 추가되었습니다.", "UTF-8");
                return "redirect:/shop?message=" + encodedMessage;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "redirect:/shop";
            }
        } else {
            return "redirect:/shop";
        }
    }

    @GetMapping("/admin/product/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id,
                              HttpSession session,
                              Model model) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            ProductForm productForm = new ProductForm();
            Product p = product.get();
            productForm.setName(p.getName());
            productForm.setPrice(p.getPrice());
            productForm.setStock(p.getStock());
            productForm.setCategory(p.getCategory());
            model.addAttribute("productForm", productForm);
            model.addAttribute("productId", id);
            return "/admin/productForm";
        } else {
            return "redirect:/shop/admin/product";
        }
    }

    @PostMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable("id") Integer id,
                                @Validated @ModelAttribute ProductForm productForm,
                                BindingResult result,
                                HttpSession session) throws IOException {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        if (result.hasErrors()) {
            return "/admin/productForm";
        }

        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(productForm.getName());
            product.setPrice(productForm.getPrice());
            product.setStock(productForm.getStock());
            product.setCategory(productForm.getCategory());
            product.setUpdateDate(LocalDateTime.now());

            MultipartFile imgFile = productForm.getImgFile();
            if (imgFile != null && !imgFile.isEmpty()) {
                byte[] fileBytes = imgFile.getBytes();
                String base64EncodedImage = Base64.getEncoder().encodeToString(fileBytes);
                product.setImageUrl(base64EncodedImage);
            }

            productService.updateProduct(product);
            
            try {
                String encodedMessage = URLEncoder.encode("해당 상품의 정보가 수정되었습니다.", "UTF-8");
                return "redirect:/shop/admin/product?message=" + encodedMessage;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "redirect:/shop/admin/product";
            }
        }
        return "redirect:/shop/admin/product";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id,
                                HttpSession session) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        productService.deleteProduct(id);

        try {
            String encodedMessage = URLEncoder.encode("해당 상품이 삭제되었습니다.", "UTF-8");
            return "redirect:/shop/admin/product?message=" + encodedMessage;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "redirect:/shop/admin/product";
        }
    }

    @GetMapping("/admin/salesList")
    public String salesList(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            Model model,
                            HttpSession session) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("shoppingDate").descending());
        Page<ShoppingList> salesListPage = shoppingRepository.findAll(pageable);

        int totalPages = salesListPage.getTotalPages();
        int pageGroupSize = 10;
        int currentPageGroup = page / pageGroupSize;
        int startPage = currentPageGroup * pageGroupSize;
        int endPage = Math.min(startPage + pageGroupSize, totalPages);

        boolean hasPreviousPage = currentPageGroup > 0;
        boolean hasNextPage = endPage < totalPages;

        model.addAttribute("salesLists", salesListPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", salesListPage.getTotalElements());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("hasPreviousPageGroup", hasPreviousPage);
        model.addAttribute("hasNextPageGroup", hasNextPage);
        model.addAttribute("previousPageGroup", startPage - pageGroupSize);
        model.addAttribute("nextPageGroup", startPage + pageGroupSize);

        return "/admin/salesList";
    }

    @GetMapping("/noticeBoard")
    public String freeBoard(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            HttpSession session,
                            Model model) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<NoticeBoard> noticeBoardPage = noticeBoardRepository.findAll(pageable);

        int totalPages = noticeBoardPage.getTotalPages();
        int pageGroupSize = 10;
        int currentGroupStartPage = (page / pageGroupSize) * pageGroupSize;
        int currentGroupEndPage = Math.min(currentGroupStartPage + pageGroupSize - 1, totalPages - 1);

        model.addAttribute("noticeBoards", noticeBoardPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("totalElements", noticeBoardPage.getTotalElements());
        model.addAttribute("currentGroupStartPage", currentGroupStartPage);
        model.addAttribute("currentGroupEndPage", currentGroupEndPage);
        model.addAttribute("hasPreviousGroup", currentGroupStartPage > 0);
        model.addAttribute("hasNextGroup", currentGroupEndPage < totalPages - 1);

        return "/admin/noticeBoard";
    }

    @GetMapping("/noticeBoard/write")
    public String getFreeBoardWrite(HttpSession session,
                                    Model model) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        model.addAttribute("noticeBoardForm", new NoticeBoardForm());

        return "/admin/noticeBoardWrite";
    }

    @PostMapping("/noticeBoard/write")
    public String postFreeBoardWrite(@Validated @ModelAttribute NoticeBoardForm noticeBoardForm,
                                     HttpSession session) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        User user = (User) session.getAttribute("user");

        NoticeBoard noticeBoard = new NoticeBoard();
        noticeBoard.setTitle(noticeBoardForm.getTitle());
        noticeBoard.setContent(noticeBoardForm.getContent());
        noticeBoard.setUserId(user.getId());
        noticeBoard.setAuthor(user.getUsername());
        noticeBoard.setCreateDate(LocalDateTime.now());
        noticeBoardRepository.save(noticeBoard);

        return "redirect:/shop/noticeBoard";
    }

    @GetMapping("/noticeBoard/edit/{id}")
    public String freeBoardEdit(@PathVariable("id") Integer id,
                                HttpSession session,
                                Model model) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        Optional<NoticeBoard> noticeBoardOptional = noticeBoardRepository.findById(id);
        if (noticeBoardOptional.isPresent()) {
            NoticeBoard freeBoard = noticeBoardOptional.get();
            NoticeBoardForm noticeBoardForm = new NoticeBoardForm();
            noticeBoardForm.setId(freeBoard.getId());
            noticeBoardForm.setTitle(freeBoard.getTitle());
            noticeBoardForm.setContent(freeBoard.getContent());

            model.addAttribute("noticeBoardForm", noticeBoardForm);
        }

        return "/admin/noticeBoardWrite";
    }

    @PostMapping("/noticeBoard/edit/{id}")
    public String postFreeBoardEdit(@PathVariable("id") Integer id,
                                    @Validated @ModelAttribute NoticeBoardForm noticeBoardForm,
                                    HttpSession session) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        Optional<NoticeBoard> noticeBoardOptional = noticeBoardRepository.findById(id);
        if (noticeBoardOptional.isPresent()) {
            NoticeBoard noticeBoard = noticeBoardOptional.get();
            noticeBoard.setTitle(noticeBoardForm.getTitle());
            noticeBoard.setContent(noticeBoardForm.getContent());
            noticeBoardRepository.save(noticeBoard);
        }

        return "redirect:/shop/noticeBoard";
    }

    @PostMapping("/noticeBoard/delete")
    public String freeBoardDelete(@RequestParam("id") Integer id,
                                  HttpSession session) {
        String redirect = checkAdminAndRedirect(session);
        if(redirect != null) {
            return redirect;
        }

        Optional<NoticeBoard> noticeBoardOptional = noticeBoardRepository.findById(id);

        if(noticeBoardOptional.isPresent()) {
            NoticeBoard freeBoard = noticeBoardOptional.get();
            noticeBoardRepository.delete(freeBoard);
        }

        return "redirect:/shop/noticeBoard";
    }

    @GetMapping("/noticeBoard/content/{id}")
    public String showContent(@PathVariable("id") Integer id,
                              HttpSession session,
                              Model model) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> cartItems = (cart != null) ? cartService.getCartItems(cart.getId()) : List.of();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("userMoney", user.getMoney());
        }

        Optional<NoticeBoard> noticeBoardOptional = noticeBoardRepository.findById(id);

        if(noticeBoardOptional.isPresent()) {
            NoticeBoard noticeBoard = noticeBoardOptional.get();
            noticeBoard.setViews(noticeBoard.getViews() + 1);
            noticeBoardRepository.save(noticeBoard);

            Optional<NoticeBoard> prev = noticeBoardRepository.findFirstByIdLessThanOrderByIdDesc(id);
            Optional<NoticeBoard> next = noticeBoardRepository.findFirstByIdGreaterThanOrderByIdAsc(id);

            model.addAttribute("noticeBoard", noticeBoard);
            model.addAttribute("prev", prev.orElse(null));
            model.addAttribute("next", next.orElse(null));
            return "/admin/noticeBoardContent";
        }

        return "/admin/noticeBoardContent";
    }
}
