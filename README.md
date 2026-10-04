# WebMiniAttraction_PJT · 국내 여행지 추천 웹 서비스

Spring Boot(Java 17) + MyBatis + Thymeleaf로 만든 국내 여행지 추천/관리 웹 애플리케이션입니다. 회원가입·로그인(Spring Security), 여행지 게시글 CRUD, 카카오맵 연동, 공공데이터포털 API 연동 관광 사진·연관 관광지 조회 기능으로 구성되어 있습니다.

## 주요 기능

| 영역 | 코드 | 내용 |
| --- | --- | --- |
| 회원 | [member/](src/main/java/org/example/Travel/member) | 회원가입·로그인·정보수정·탈퇴, Spring Security + BCrypt 비밀번호 암호화 |
| 여행지 게시판 | [travel/](src/main/java/org/example/Travel/travel) | 여행지 글 등록·조회·수정·삭제 (지역·제목·설명·주소·전화번호) |
| 지도 | [map/](src/main/java/org/example/Travel/map) | 카카오맵 SDK로 지도 표시 |
| 공공데이터 연동 | [location_image/](src/main/java/org/example/Travel/location_image), [location_relation_title/](src/main/java/org/example/Travel/location_relation_title), [tac/](src/main/java/org/example/Travel/tac) | 한국관광공사 공공데이터(사진 갤러리, 연관 관광지, 지역/시군구 코드) API 연동 |

## 실행 방법

~~~bash
git clone https://github.com/kwakyun/WebMiniAttraction_PJT.git
cd WebMiniAttraction_PJT
~~~

Java 17과 MySQL이 필요합니다. DB_USERNAME, DB_PASSWORD, PUBLIC_DATA_SERVICE_KEY, KAKAO_MAP_JAVASCRIPT_KEY를 터미널 또는 IDE 환경변수로 설정합니다. 실제 값을 파일에 커밋하지 마세요. 변수 목록과 키 종류는 [.env.example](.env.example), [보안 설정 안내](SECURITY.md)를 참고하세요. .env는 자동으로 로드되지 않습니다.

~~~bash
./gradlew bootRun
~~~

기본 포트(8080)로 접속: http://localhost:8080



