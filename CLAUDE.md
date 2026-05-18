# FlowFix Assistant

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
backend/src/main/kotlin/com/wemeet/flowfixassistant/
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
│   ├── domain/
│   │   ├── model/              # AssistantUser
│   │   └── repository/         # 순수 인터페이스 (JPA 의존 없음)
│   └── infrastructure/
│       └── persistence/jpa/    # JpaAssistantUserRepository
├── common/
│   ├── presentation/           # ApiResponse, GlobalExceptionHandler
│   └── infrastructure/config/  # CorsConfig, RestClientConfig, SecurityConfig, WebSocketConfig
└── FlowfixAssistantApplication.kt

backend/src/main/resources/application.yaml
backend/src/test/kotlin/com/wemeet/flowfixassistant/
backend/src/test/resources/application.yaml
```

## Build & Test

```bash
./gradlew build          # 빌드
./gradlew test           # 테스트 (H2 in-memory DB 사용)
./gradlew bootRun        # 로컬 실행
```

## Conventions

- 한국어 커밋 메시지 및 코드 주석 사용
- Kotlin 코딩 컨벤션 준수
- JPA Entity에는 `data class` 대신 일반 `class` 사용
- `-Xjsr305=strict` 활성화: null-safety 엄격 적용
- 테스트는 JUnit 5 + `@SpringBootTest` 기반
- REST API는 Spring Web MVC 패턴 (Controller → Service → Repository)
