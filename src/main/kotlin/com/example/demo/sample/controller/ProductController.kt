package com.example.demo.sample.controller

import com.example.demo.common.api.ApiResponse
import com.example.demo.common.api.PageResponse
import com.example.demo.sample.domain.ProductStatus
import com.example.demo.sample.dto.ProductCreateRequest
import com.example.demo.sample.dto.ProductResponse
import com.example.demo.sample.dto.ProductUpdateRequest
import com.example.demo.sample.dto.StockUpdateRequest
import com.example.demo.sample.service.ProductService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * 상품 컨트롤러 (샘플)
 */
@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    /**
     * 상품 생성
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(
        @Valid @RequestBody request: ProductCreateRequest
    ): ApiResponse<ProductResponse> {
        val product = productService.createProduct(request)
        return ApiResponse.success(product)
    }

    /**
     * 상품 조회
     */
    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: Long): ApiResponse<ProductResponse> {
        val product = productService.getProduct(id)
        return ApiResponse.success(product)
    }

    /**
     * 모든 상품 조회 (페이징)
     */
    @GetMapping
    fun getAllProducts(
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): ApiResponse<PageResponse<ProductResponse>> {
        val products = productService.getAllProducts(pageable)
        return ApiResponse.success(PageResponse.from(products))
    }

    /**
     * 상품명으로 검색
     */
    @GetMapping("/search")
    fun searchProducts(
        @RequestParam name: String,
        @PageableDefault(size = 20, sort = ["name"])
        pageable: Pageable
    ): ApiResponse<PageResponse<ProductResponse>> {
        val products = productService.searchByName(name, pageable)
        return ApiResponse.success(PageResponse.from(products))
    }

    /**
     * 상태별 상품 조회
     */
    @GetMapping("/status/{status}")
    fun getProductsByStatus(
        @PathVariable status: ProductStatus,
        @PageableDefault(size = 20)
        pageable: Pageable
    ): ApiResponse<PageResponse<ProductResponse>> {
        val products = productService.getProductsByStatus(status, pageable)
        return ApiResponse.success(PageResponse.from(products))
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @Valid @RequestBody request: ProductUpdateRequest
    ): ApiResponse<ProductResponse> {
        val product = productService.updateProduct(id, request)
        return ApiResponse.success(product)
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: Long): ApiResponse<Unit> {
        productService.deleteProduct(id)
        return ApiResponse.success()
    }

    /**
     * 재고 증가
     */
    @PostMapping("/{id}/stock/increase")
    fun increaseStock(
        @PathVariable id: Long,
        @Valid @RequestBody request: StockUpdateRequest
    ): ApiResponse<ProductResponse> {
        val product = productService.increaseStock(id, request.quantity)
        return ApiResponse.success(product)
    }

    /**
     * 재고 감소
     */
    @PostMapping("/{id}/stock/decrease")
    fun decreaseStock(
        @PathVariable id: Long,
        @Valid @RequestBody request: StockUpdateRequest
    ): ApiResponse<ProductResponse> {
        val product = productService.decreaseStock(id, request.quantity)
        return ApiResponse.success(product)
    }

    /**
     * 상품 활성화
     */
    @PostMapping("/{id}/activate")
    fun activateProduct(@PathVariable id: Long): ApiResponse<ProductResponse> {
        val product = productService.activateProduct(id)
        return ApiResponse.success(product)
    }

    /**
     * 상품 비활성화
     */
    @PostMapping("/{id}/deactivate")
    fun deactivateProduct(@PathVariable id: Long): ApiResponse<ProductResponse> {
        val product = productService.deactivateProduct(id)
        return ApiResponse.success(product)
    }

    /**
     * 재고 부족 상품 조회
     */
    @GetMapping("/low-stock")
    fun getLowStockProducts(
        @RequestParam(defaultValue = "10") threshold: Int
    ): ApiResponse<List<ProductResponse>> {
        val products = productService.getLowStockProducts(threshold)
        return ApiResponse.success(products)
    }
}
