package com.example.demo.sample.service

import com.example.demo.api.ErrorCode
import com.example.demo.common.aop.LogExecutionTime
import com.example.demo.exception.DemoBizException
import com.example.demo.sample.domain.Product
import com.example.demo.sample.domain.ProductStatus
import com.example.demo.sample.dto.ProductCreateRequest
import com.example.demo.sample.dto.ProductResponse
import com.example.demo.sample.dto.ProductUpdateRequest
import com.example.demo.sample.repository.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 상품 서비스 (샘플)
 */
@Service
@Transactional(readOnly = true)
class ProductService(
    private val productRepository: ProductRepository
) {

    /**
     * 상품 생성
     */
    @Transactional
    @LogExecutionTime
    fun createProduct(request: ProductCreateRequest): ProductResponse {
        val product = request.toEntity()
        val savedProduct = productRepository.save(product)
        return ProductResponse.from(savedProduct)
    }

    /**
     * 상품 조회
     */
    fun getProduct(id: Long): ProductResponse {
        val product = findProductById(id)
        return ProductResponse.from(product)
    }

    /**
     * 모든 상품 조회 (페이징)
     */
    fun getAllProducts(pageable: Pageable): Page<ProductResponse> {
        return productRepository.findAll(pageable)
            .map { ProductResponse.from(it) }
    }

    /**
     * 상품명으로 검색
     */
    fun searchByName(name: String, pageable: Pageable): Page<ProductResponse> {
        return productRepository.findByNameContaining(name, pageable)
            .map { ProductResponse.from(it) }
    }

    /**
     * 상태별 조회
     */
    fun getProductsByStatus(status: ProductStatus, pageable: Pageable): Page<ProductResponse> {
        return productRepository.findByStatus(status, pageable)
            .map { ProductResponse.from(it) }
    }

    /**
     * 상품 수정
     */
    @Transactional
    @LogExecutionTime
    fun updateProduct(id: Long, request: ProductUpdateRequest): ProductResponse {
        val product = findProductById(id)

        product.apply {
            name = request.name
            description = request.description
            price = request.price
            stockQuantity = request.stockQuantity
        }

        return ProductResponse.from(product)
    }

    /**
     * 상품 삭제
     */
    @Transactional
    fun deleteProduct(id: Long) {
        val product = findProductById(id)
        productRepository.delete(product)
    }

    /**
     * 재고 증가
     */
    @Transactional
    fun increaseStock(id: Long, quantity: Int): ProductResponse {
        val product = findProductById(id)
        product.increaseStock(quantity)
        return ProductResponse.from(product)
    }

    /**
     * 재고 감소
     */
    @Transactional
    fun decreaseStock(id: Long, quantity: Int): ProductResponse {
        val product = findProductById(id)
        product.decreaseStock(quantity)
        return ProductResponse.from(product)
    }

    /**
     * 상품 활성화
     */
    @Transactional
    fun activateProduct(id: Long): ProductResponse {
        val product = findProductById(id)
        product.activate()
        return ProductResponse.from(product)
    }

    /**
     * 상품 비활성화
     */
    @Transactional
    fun deactivateProduct(id: Long): ProductResponse {
        val product = findProductById(id)
        product.deactivate()
        return ProductResponse.from(product)
    }

    /**
     * 재고 부족 상품 조회
     */
    fun getLowStockProducts(threshold: Int): List<ProductResponse> {
        return productRepository.findByStockQuantityLessThanEqual(threshold)
            .map { ProductResponse.from(it) }
    }

    /**
     * ID로 상품 찾기 (내부용)
     */
    private fun findProductById(id: Long): Product {
        return productRepository.findByIdOrNull(id)
            ?: throw DemoBizException(ErrorCode.NOT_FOUND.toErrorMessage("상품을 찾을 수 없습니다. ID: $id"))
    }
}
