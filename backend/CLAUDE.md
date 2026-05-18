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
├── chat/
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
│   └── infrastructure/         # Jpa*Repository, WebClientRagClient (DIP 구현체)
├── user/
│   ├── domain/
│   │   ├── model/              # AssistantUser
│   │   └── repository/         # 순수 인터페이스 (JPA 의존 없음)
│   └── infrastructure/         # JpaAssistantUserRepository (DIP 구현체)
├── common/
│   ├── presentation/           # ApiResponse, GlobalExceptionHandler
│   └── infrastructure/config/  # CorsConfig, RestClientConfig, SecurityConfig, WebSocketConfig
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
- **infrastructure**: domain 참조 가능 (DIP로 domain 인터페이스 구현)
- 역방향 의존 금지: domain → application, application → presentation 등

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
- Controller 메서드 패턴: `val result = service.xxx(...)` → `return ApiResponse.ok(XxxResponse.of/from(result))`
- RAG 등 외부 API DTO는 `application/dto/`에 위치 (`RagRequest`, `RagResponse`)
- API 응답은 `ResponseEntity<ApiResponse<T>>`로 통일
- Request 검증은 `@Valid` + Jakarta Validation 어노테이션 사용 (`@NotBlank`, `@Size` 등)
