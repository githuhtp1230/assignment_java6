package com.assignment.controller;

import com.assignment.entity.Category;
import com.assignment.entity.Product;
import com.assignment.service.CategoryService;
import com.assignment.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping({ "/home/index", "/" })
    public String index(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) { // 12 sản phẩm/trang
        Page<Product> pageProducts = productService.getAllProductsPaged(page, size);
        List<Category> categories = categoryService.getAllCategories();
        List<Product> randomProducts = productService.getRandomProducts(8);

        model.addAttribute("randomProducts", randomProducts);
        model.addAttribute("products", pageProducts.getContent());
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        return "home";
    }

    // Hiển thị sản phẩm theo danh mục
    @GetMapping("/product/list-by-category/{id}")
    public String listByCategory(@PathVariable Integer id, 
                               @RequestParam(required = false, defaultValue = "name_asc") String sort,
                               Model model) {
        List<Product> products = productService.getProductsByCategoryAndSort(id, sort);
        Category category = categoryService.getCategoryById(id);
        
        model.addAttribute("products", products);
        model.addAttribute("category", category);
        model.addAttribute("title", "Products - " + category.getName());
        return "product-list";
    }

    // Hiển thị chi tiết sản phẩm
    @GetMapping("/product/detail/{id}")
    public String productDetail(@PathVariable Integer id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("title", product.getName());
            return "product-detail";
        }
        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        List<Product> searchResults = productService.searchProducts(keyword);
        model.addAttribute("products", searchResults);
        model.addAttribute("keyword", keyword);
        return "product-list";
    }
}
