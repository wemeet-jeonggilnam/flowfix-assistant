# FlowFix Assistant

Spring Boot 기반 백엔드 애플리케이션 (com.wemeet)

## Tech Stack

- **Language**: Kotlin 1.9.25 / Java 17
- **Framework**: Spring Boot 3.4.5
- **Build**: Gradle (Kotlin DSL)
- **DB**: PostgreSQL (runtime), H2 (test)
- **ORM**: Spring Data JPA (allOpen: Entity, MappedSuperclass, Embeddable)

## Package Structure

Base package: `com.wemeet.flowfixassistant`

```
src/main/kotlin/com/wemeet/flowfixassistant/
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
- JPA Entity에는 `data class` 대신 일반 `class` 사용
- `-Xjsr305=strict` 활성화: null-safety 엄격 적용
- 테스트는 JUnit 5 + `@SpringBootTest` 기반
- REST API는 Spring Web MVC 패턴 (Controller → Service → Repository)
