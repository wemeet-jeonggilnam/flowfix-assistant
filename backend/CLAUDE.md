# FlowFix Assistant - Backend

Spring Boot 기반 백엔드 애플리케이션 (com.wemeet)

## Tech Stack

- **Language**: Kotlin 1.9.25 / Java 17
- **Framework**: Spring Boot 3.4.5
- **Build**: Gradle (Kotlin DSL)
- **DB**: PostgreSQL (runtime), H2 (test)
- **ORM**: Spring Data JPA (allOpen: Entity, MappedSuperclass, Embeddable)

## Package Structure

Base package: `com.wemeet.flowfixassistant` (도메인별 4계층 DDD 구조)

```
src/main/kotlin/com/wemeet/flowfixassistant/
├── conversation/                       # 애그리거트 루트: Conversation
│   ├── presentation/           # ChatController, dto/
│   ├── application/
│   │   ├── service/            # ChatService, TokenUsageService
│   │   ├── dto/                # ChatSendCommand, ChatSendResult, RagRequest, RagResponse
│   │   └── RagClient.kt       # 외부 RAG 인터페이스
│   ├── domain/
│   │   ├── model/              # ChatMessage, Conversation, MessageSource, TokenUsageLog
│   │   ├── enums/              # ChatRole
│   │   ├── vo/                 # TokenUsageInfo (@Embeddable 값 객체)
│   │   └── repository/         # 순수 인터페이스 (JPA 의존 없음)
│   └── infrastructure/
│       ├── persistence/jpa/    # JpaConversationRepository, JpaTokenUsageLogRepository
│       └── WebClientRagClient.kt
├── user/
│   ├── presentation/           # AuthController, dto/
│   ├── application/
│   │   ├── dto/                # SignUpCommand, LoginCommand, AuthResult
│   │   ├── TokenProvider.kt    # 토큰 생성/검증 인터페이스 (DIP)
│   │   └── UserService.kt
│   ├── domain/
│   │   ├── model/              # AssistantUser
│   │   └── repository/         # 순수 인터페이스 (JPA 의존 없음)
│   └── infrastructure/
│       ├── persistence/jpa/    # JpaAssistantUserRepository
│       └── security/           # SecurityConfig, JwtTokenProvider, JwtAuthenticationFilter, UserPrincipal, CustomUserDetailsService
├── common/
│   ├── domain/                 # BaseEntity (@MappedSuperclass)
│   ├── presentation/           # ApiResponse, GlobalExceptionHandler
│   └── infrastructure/config/  # CorsConfig, JpaAuditingConfig, RestClientConfig, WebSocketConfig
└── FlowfixAssistantApplication.kt

src/main/resources/application.yaml
src/test/kotlin/com/wemeet/flowfixassistant/
src/test/resources/application.yaml
```

## Build & Test

```bash
./gradlew build          # 빌드
./gradlew test           # 테스트 (H2 in-memory DB 사용)
./gradlew bootRun        # 로컬 실행
```

## Layer Dependency Rules

```
presentation → application → domain ← infrastructure
```

- **presentation**: application, domain 참조 가능. infrastructure 직접 참조 금지
- **application**: domain 참조 가능. presentation, infrastructure 직접 참조 금지
- **domain**: 어떤 계층도 참조하지 않음 (순수 도메인, 외부 의존성 없음)
- **infrastructure**: domain, application 인터페이스 참조 가능 (DIP로 인터페이스 구현)
- 역방향 의존 금지: domain → application, application → presentation 등
- `common` 패키지는 특정 도메인 모듈에 의존 금지 (도메인 무관 공통 모듈)

## Conventions

- 한국어 커밋 메시지 및 코드 주석 사용
- Kotlin 코딩 컨벤션 준수
- JPA Entity에는 `data class` 대신 일반 `class` 사용 (VO/@Embeddable은 `data class` 허용)
- `-Xjsr305=strict` 활성화: null-safety 엄격 적용
- 테스트는 JUnit 5 + `@SpringBootTest` 기반
- REST API는 Spring Web MVC 패턴 (Controller → Service → Repository)
- 토큰 사용량 기록은 `REQUIRES_NEW` 트랜잭션으로 분리
- 연관관계 편의 메서드는 `@OneToMany` 쪽(`Conversation.addMessage()`)에서 양방향 동기화
- 도메인 엔티티 생성은 `companion object` 팩토리 메서드 사용 (`ofUser`, `ofAssistant`)
- Presentation DTO 네이밍: `<도메인><행위>Request/Response` (예: `ChatSendRequest`, `ConversationListResponse`)
- Application DTO 네이밍: 쓰기 → `Command`, 읽기 → `Query`, 결과 → `Result` (예: `ChatSendCommand`, `ChatSendResult`)
- Application Service는 도메인 객체 또는 Application DTO를 반환, Presentation DTO 직접 사용 금지
- Request → Command 변환은 `Request.toCommand()` 메서드로 Request DTO 내부에서 처리
- Controller에서 도메인/Result → Response 변환은 `Response.of()` 또는 `Response.from()` 팩토리 메서드 사용
- Controller 메서드 패턴: `return XxxResponse.of/from(service.xxx(...)).toSuccessResponse()` (중간 변수 없이 체이닝)
- RAG 등 외부 API DTO는 `application/dto/`에 위치 (`RagRequest`, `RagResponse`)
- API 응답은 `T.toSuccessResponse()` 확장 함수 사용 (`ResponseEntity<ApiResponse<T>>` 반환)
- Request 검증은 `@Valid` + Jakarta Validation 어노테이션 사용 (`@NotBlank`, `@Size` 등)
- Service의 public 메서드에는 KDoc 주석 작성: 메서드 설명, `@param`, `@return` 포함 (한국어)
- 외부 인프라 인터페이스(TokenProvider, RagClient 등)는 `application/` 계층에 정의, 구현체는 `infrastructure/`에 위치 (DIP)
- 보안 인프라 클래스(JWT, UserDetails 등)는 해당 도메인의 `infrastructure/security/`에 위치, `common`에 두지 않음
- JWT 시크릿 등 민감 설정은 환경변수로 주입 (`${JWT_SECRET}`), 소스코드에 기본값 하드코딩 금지
