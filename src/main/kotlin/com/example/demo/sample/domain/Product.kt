package com.example.demo.sample.domain

import com.example.demo.common.entity.BaseEntity
import jakarta.persistence.*
import java.math.BigDecimal

/**
 * 상품 엔티티 (샘플)
 */
@Entity
@Table(name = "products")
class Product(
    @Column(nullable = false, length = 100)
    var name: String,

    @Column(length = 500)
    var description: String? = null,

    @Column(nullable = false, precision = 10, scale = 2)
    var price: BigDecimal,

    @Column(nullable = false)
    var stockQuantity: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: ProductStatus = ProductStatus.AVAILABLE

) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    /**
     * 재고 감소
     */
    fun decreaseStock(quantity: Int) {
        if (this.stockQuantity < quantity) {
            throw IllegalStateException("재고가 부족합니다. 현재 재고: ${this.stockQuantity}, 요청 수량: $quantity")
        }
        this.stockQuantity -= quantity
    }

    /**
     * 재고 증가
     */
    fun increaseStock(quantity: Int) {
        this.stockQuantity += quantity
    }

    /**
     * 상품 비활성화
     */
    fun deactivate() {
        this.status = ProductStatus.UNAVAILABLE
    }

    /**
     * 상품 활성화
     */
    fun activate() {
        this.status = ProductStatus.AVAILABLE
    }
}

/**
 * 상품 상태
 */
enum class ProductStatus {
    AVAILABLE,      // 판매 가능
    UNAVAILABLE,    // 판매 불가
    OUT_OF_STOCK    // 품절
}
