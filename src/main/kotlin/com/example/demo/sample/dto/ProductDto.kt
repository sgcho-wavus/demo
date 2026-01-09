package com.example.demo.sample.dto

import com.example.demo.sample.domain.Product
import com.example.demo.sample.domain.ProductStatus
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * 상품 생성 요청 DTO
 */
data class ProductCreateRequest(
    @field:NotBlank(message = "상품명은 필수입니다")
    @field:Size(max = 100, message = "상품명은 100자 이하여야 합니다")
    val name: String,

    @field:Size(max = 500, message = "설명은 500자 이하여야 합니다")
    val description: String? = null,

    @field:NotNull(message = "가격은 필수입니다")
    @field:DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다")
    val price: BigDecimal,

    @field:Min(value = 0, message = "재고는 0 이상이어야 합니다")
    val stockQuantity: Int = 0
) {
    fun toEntity(): Product {
        return Product(
            name = name,
            description = description,
            price = price,
            stockQuantity = stockQuantity
        )
    }
}

/**
 * 상품 수정 요청 DTO
 */
data class ProductUpdateRequest(
    @field:NotBlank(message = "상품명은 필수입니다")
    @field:Size(max = 100, message = "상품명은 100자 이하여야 합니다")
    val name: String,

    @field:Size(max = 500, message = "설명은 500자 이하여야 합니다")
    val description: String? = null,

    @field:NotNull(message = "가격은 필수입니다")
    @field:DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다")
    val price: BigDecimal,

    @field:Min(value = 0, message = "재고는 0 이상이어야 합니다")
    val stockQuantity: Int
)

/**
 * 상품 응답 DTO
 */
data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val stockQuantity: Int,
    val status: ProductStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(product: Product): ProductResponse {
            return ProductResponse(
                id = product.id!!,
                name = product.name,
                description = product.description,
                price = product.price,
                stockQuantity = product.stockQuantity,
                status = product.status,
                createdAt = product.createdAt,
                updatedAt = product.updatedAt
            )
        }
    }
}

/**
 * 재고 수정 요청 DTO
 */
data class StockUpdateRequest(
    @field:NotNull(message = "수량은 필수입니다")
    val quantity: Int
)
