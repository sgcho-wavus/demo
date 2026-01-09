package com.example.demo.sample.repository

import com.example.demo.sample.domain.Product
import com.example.demo.sample.domain.ProductStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigDecimal

/**
 * 상품 리포지토리 (샘플)
 */
interface ProductRepository : JpaRepository<Product, Long> {

    /**
     * 상품명으로 검색
     */
    fun findByNameContaining(name: String, pageable: Pageable): Page<Product>

    /**
     * 상태로 조회
     */
    fun findByStatus(status: ProductStatus, pageable: Pageable): Page<Product>

    /**
     * 가격 범위로 조회
     */
    fun findByPriceBetween(minPrice: BigDecimal, maxPrice: BigDecimal, pageable: Pageable): Page<Product>

    /**
     * 재고가 특정 수량 이하인 상품 조회
     */
    fun findByStockQuantityLessThanEqual(quantity: Int): List<Product>

    /**
     * 상품명과 상태로 조회 (JPQL 예제)
     */
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.status = :status")
    fun searchByNameAndStatus(
        @Param("name") name: String,
        @Param("status") status: ProductStatus,
        pageable: Pageable
    ): Page<Product>

    /**
     * 가격 범위와 재고 수량 조건으로 조회 (JPQL 예제)
     */
    @Query("""
        SELECT p FROM Product p
        WHERE p.price BETWEEN :minPrice AND :maxPrice
        AND p.stockQuantity >= :minStock
        ORDER BY p.price ASC
    """)
    fun findProductsInPriceRangeWithStock(
        @Param("minPrice") minPrice: BigDecimal,
        @Param("maxPrice") maxPrice: BigDecimal,
        @Param("minStock") minStock: Int
    ): List<Product>
}
