package com.saijyothi.bookstore.controller;

import com.saijyothi.bookstore.dto.StoreContentResponse;
import com.saijyothi.bookstore.service.StoreContentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content")
public class StoreContentController {

    private final StoreContentService storeContentService;

    public StoreContentController(StoreContentService storeContentService) {
        this.storeContentService = storeContentService;
    }

    @GetMapping
    public StoreContentResponse getStoreContent() {
        return storeContentService.getStoreContent();
    }
}
