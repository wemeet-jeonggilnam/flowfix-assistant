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
│   ├── application/            # ChatService, TokenUsageService, RagClient
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
- DTO 네이밍: `<도메인><행위>Request/Response` (예: `ChatSendRequest`, `ConversationListResponse`)
