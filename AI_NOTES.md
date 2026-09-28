# AI 활용 기록

## 2026-09-23 · 저장소 정리

- 요청: README가 비어 있던 강의/실습 저장소에 문서를 추가하고 GitHub 프로필 핀 구성을 갱신.
- 도구: Claude. Spring Boot 프로젝트 구조(컨트롤러·서비스·VO·템플릿·설정 파일)를 직접 읽고 README·AI_NOTES·CHANGELOG를 작성. 빈 파일이었던 `Readme.md`를 다른 저장소와 표기를 맞춰 `README.md`로 변경.
- 변경 범위: README, 이번 변경 기록. 애플리케이션 코드(.java, .html)와 설정 파일(application.properties, .gitignore)은 수정하지 않음.
- 확인한 보안 이슈: `src/main/resources/application.properties`에 공공데이터포털(data.go.kr) 서비스 키가, `src/main/resources/templates/map/kakao_map.html`에 카카오맵 JavaScript 키가 하드코딩되어 커밋되어 있음을 확인했습니다. 두 값 모두 Public 저장소 히스토리에 이미 노출된 상태입니다. 데이터베이스 비밀번호(`spring.datasource.password`)도 평문으로 커밋되어 있습니다.
  - 이번 작업에서는 애플리케이션 동작에 영향을 줄 수 있어 코드/설정 파일을 직접 고치지 않았고, README에 사실을 그대로 기록만 했습니다.
  - 서비스 키 재발급(회전)과, 이후 별도 요청 시 `application.properties`를 `.gitignore` 처리하고 예시 파일(`application.properties.example`)로 대체하는 정리를 권장합니다.
- 한계: 이 작업은 문서 정리 범위이며, 애플리케이션의 실제 실행·DB 연동·외부 API 응답을 검증하지 않았습니다.

## 2026-09-28 · 자격증명 노출 수정

- 도구: Codex. 사용자 요청으로 설정·지도·로그·비밀번호 출력 경로를 수정했습니다.
- 공공데이터포털 키, DB 계정, 카카오 JavaScript 키를 환경변수로 분리했습니다.
- 카카오 REST 키를 사용하던 브라우저 코드를 SDK 주소 검색으로 교체했습니다.
- 키 없는 지도, API 예외 체인, 비밀번호 직렬화 방지에 대한 회귀 테스트를 추가했습니다.
- 과거 자격증명과 새 소스의 대조 검사는 키 값을 출력하지 않고 수행합니다.
- 외부 발급 서비스의 키 폐기·재발급은 수행하지 않았습니다.

- 검증 결과: Java 17에서 gradlew test bootJar 성공. 보안 회귀 7개 + H2 컨텍스트 1개, 총 8개 테스트 통과. 실제 외부 API·DB 연결은 검증하지 않았습니다.
