# Spring Boot + JPA + Kotlin 백엔드 템플릿 가이드

## 개요

이 프로젝트는 Spring Boot, JPA, Kotlin을 기반으로 한 백엔드 애플리케이션 템플릿입니다.
공통 기능들이 표준화되어 있어 빠르게 개발을 시작할 수 있습니다.

## 기술 스택

- **Spring Boot 4.0.0**
- **Kotlin 2.2.21**
- **Spring Data JPA**
- **H2 Database** (개발용, 운영환경에서는 변경 필요)
- **Gradle**

## 프로젝트 구조

```
src/main/kotlin/com/example/demo/
├── common/                    # 공통 기능
│   ├── api/                  # API 응답 관련
│   │   ├── ApiResponse.kt   # 표준 API 응답 형식
│   │   └── PageResponse.kt  # 페이징 응답 형식
│   ├── aop/                  # AOP 관련
│   │   ├── LoggingAspect.kt        # 요청/응답 로깅
│   │   ├── ExecutionTimeAspect.kt  # 실행 시간 측정
│   │   └── LogExecutionTime.kt     # 실행 시간 측정 어노테이션
│   ├── config/               # 설정
│   │   ├── JpaAuditingConfig.kt   # JPA Auditing 설정
│   │   └── JacksonConfig.kt       # JSON 직렬화 설정
│   ├── entity/               # 공통 엔티티
│   │   ├── BaseEntity.kt          # Auditing 포함 기본 엔티티
│   │   └── BaseTimeEntity.kt      # 시간만 포함하는 기본 엔티티
│   └── exception/            # 예외 처리
│       ├── RestExceptionHandler.kt    # 전역 예외 핸들러
│       └── ErrorCodeExtensions.kt     # ErrorCode 확장 함수
├── sample/                    # 샘플 코드
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── domain/
│   └── dto/
└── DemoApplication.kt
```

## 주요 기능

### 1. 전역 예외 처리 (RestExceptionHandler)

모든 컨트롤러에서 발생하는 예외를 일관되게 처리합니다.

**지원하는 예외:**
- `DemoBizException` - 비즈니스 로직 예외
- `DemoSystemException` - 시스템 예외
- `DemoValidationException` - 검증 예외
- `MethodArgumentNotValidException` - Spring Validation 예외
- `ConstraintViolationException` - 제약조건 위반
- `IllegalArgumentException` - 잘못된 인자
- 기타 예외

**위치:** `common/exception/RestExceptionHandler.kt`

### 2. 공통 응답 형식 (ApiResponse)

모든 API 응답을 표준화된 형식으로 반환합니다.

**성공 응답:**
```kotlin
ApiResponse.success(data)           // 데이터 포함
ApiResponse.success()               // 데이터 없음
```

**실패 응답:**
```kotlin
ApiResponse.error<T>(code, message)
ApiResponse.error<T>(errorInfo)
```

**응답 형식:**
```json
{
  "success": true,
  "data": { ... },
  "error": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**위치:** `common/api/ApiResponse.kt`

### 3. 페이징 응답 (PageResponse)

Spring Data의 Page 객체를 표준화된 형식으로 변환합니다.

```kotlin
// Page 객체를 그대로 변환
PageResponse.from(page)

// Page 객체를 매핑하여 변환
PageResponse.from(page) { entity -> entity.toDto() }
```

**위치:** `common/api/PageResponse.kt`

### 4. JPA Auditing (BaseEntity)

엔티티의 생성/수정 정보를 자동으로 관리합니다.

**BaseEntity (생성자/수정자 포함):**
```kotlin
@Entity
class Product(...) : BaseEntity() {
    // createdAt, updatedAt, createdBy, updatedBy 자동 관리
}
```

**BaseTimeEntity (시간만 포함):**
```kotlin
@Entity
class Log(...) : BaseTimeEntity() {
    // createdAt, updatedAt만 자동 관리
}
```

**위치:** `common/entity/`

### 5. 로깅 AOP

#### 5.1 자동 요청/응답 로깅

모든 `@RestController`의 요청/응답을 자동으로 로깅합니다.

**로그 내용:**
- HTTP 메서드, URI, IP
- 요청 파라미터
- 실행 시간
- 응답 데이터
- 예외 정보 (예외 발생 시)

**위치:** `common/aop/LoggingAspect.kt`

#### 5.2 메서드 실행 시간 측정

`@LogExecutionTime` 어노테이션으로 특정 메서드의 실행 시간을 측정합니다.

```kotlin
@Service
class ProductService {
    @LogExecutionTime
    fun createProduct(request: ProductCreateRequest): ProductResponse {
        // ... 실행 시간이 로깅됨
    }
}
```

**위치:** `common/aop/ExecutionTimeAspect.kt`

## 사용 예제

### Entity 작성

```kotlin
@Entity
@Table(name = "products")
class Product(
    @Column(nullable = false)
    var name: String,

    var price: BigDecimal
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
```

### Repository 작성

```kotlin
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByNameContaining(name: String, pageable: Pageable): Page<Product>
}
```

### DTO 작성

```kotlin
// 요청 DTO
data class ProductCreateRequest(
    @field:NotBlank
    val name: String,

    @field:NotNull
    val price: BigDecimal
) {
    fun toEntity() = Product(name = name, price = price)
}

// 응답 DTO
data class ProductResponse(
    val id: Long,
    val name: String,
    val price: BigDecimal
) {
    companion object {
        fun from(product: Product) = ProductResponse(
            id = product.id!!,
            name = product.name,
            price = product.price
        )
    }
}
```

### Service 작성

```kotlin
@Service
@Transactional(readOnly = true)
class ProductService(
    private val productRepository: ProductRepository
) {
    @Transactional
    @LogExecutionTime
    fun createProduct(request: ProductCreateRequest): ProductResponse {
        val product = productRepository.save(request.toEntity())
        return ProductResponse.from(product)
    }

    fun getProduct(id: Long): ProductResponse {
        val product = productRepository.findByIdOrNull(id)
            ?: throw DemoBizException(
                ErrorCode.NOT_FOUND.toErrorMessage("상품을 찾을 수 없습니다.")
            )
        return ProductResponse.from(product)
    }
}
```

### Controller 작성

```kotlin
@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(
        @Valid @RequestBody request: ProductCreateRequest
    ): ApiResponse<ProductResponse> {
        val product = productService.createProduct(request)
        return ApiResponse.success(product)
    }

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: Long): ApiResponse<ProductResponse> {
        val product = productService.getProduct(id)
        return ApiResponse.success(product)
    }

    @GetMapping
    fun getAllProducts(
        @PageableDefault(size = 20) pageable: Pageable
    ): ApiResponse<PageResponse<ProductResponse>> {
        val products = productService.getAllProducts(pageable)
        return ApiResponse.success(PageResponse.from(products))
    }
}
```

## 설정 파일

### application.yml

주요 설정:
- JPA Hibernate 설정
- 데이터베이스 연결 정보
- 로깅 레벨
- Jackson JSON 설정

**위치:** `src/main/resources/application.yml`

## 빌드 및 실행

```bash
# 빌드
./gradlew build

# 실행
./gradlew bootRun

# 테스트
./gradlew test
```

## API 테스트

애플리케이션 실행 후:
- H2 Console: http://localhost:8080/h2-console
- API 엔드포인트: http://localhost:8080/api/products

## 운영환경 배포 시 주의사항

1. **데이터베이스 변경**
   - `application.yml`에서 H2를 MySQL, PostgreSQL 등으로 변경
   - `build.gradle`에 해당 데이터베이스 드라이버 추가

2. **JPA DDL 설정**
   - `spring.jpa.hibernate.ddl-auto`를 `validate`로 변경
   - Flyway나 Liquibase를 사용한 마이그레이션 권장

3. **로깅 레벨 조정**
   - SQL 로그를 INFO 또는 WARN으로 변경
   - 민감한 정보 로깅 제거

4. **AuditorAware 구현**
   - `JpaAuditingConfig`에서 실제 사용자 정보 연동
   - Spring Security와 통합

5. **에러 응답 설정**
   - 운영환경에서는 스택트레이스 노출 제거
   - `server.error.include-stacktrace: never`

## 라이센스

이 템플릿은 자유롭게 사용, 수정, 배포할 수 있습니다.
