package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.HomepageViewDto;
import com.example.SpringShop.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final CategoryService categoryService;

    @Autowired
    public HomeController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<HomepageViewDto> loadHome() {
        HomepageViewDto dto = new HomepageViewDto();

        return ResponseEntity.ok(dto);
    }
}
