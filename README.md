# Spring Boot Snack World 서비스

- Snack World는 Spring Boot를 이용하여 만든 다양한 스낵(먹거리)을 검색하고 구매할 수 있는 온라인 스낵몰 서비스입니다.
- 관리자
  - 상품 관리(등록, 수정, 삭제), 회원 관리, 마이 페이지에서 판매 내역 조회
- 회원
  - 장바구니, 상품 구매, 마이 페이지에서 구매 내역 조회, 비밀번호 변경, 캐시 충전
- 비회원
  - 상품 조회, 검색, 리뷰 페이지 조회만 가능

## Dependencies
- Spring Data JDBC
- Thymeleaf
- Validation
- Lombok
- Spring Boot DevTools
- MySQL Driver
- Spring Web
- Java Mail Sender

## 개발 환경
- 통합개발환경(IDE): IntelliJ
- Spring Boot: 3.3.2
- Java 버전: JDK 17
- 데이터베이스: MySQL
- 빌드 도구: Gradle

## 기술 스택
- 프론트엔드
  - HTML, CSS, JavaScriptm Thymeleaf
- 백엔드
  - Java, Spring Boot, Spring Data JDBC
- 데이터베이스
  - MySQL, MySQL Workbench
  - ID: jhl9812181
  - Password: lee2863!
  - 183.111.242.55:3306

# DB Table
- shop_user
- product
- cart
- cart_item
- shopping_list
- notice_board

