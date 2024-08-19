# Spring Boot Snack World 서비스

Snack World는 Spring Boot를 이용하여 만든 사용자가 다양한 스낵(먹거리)을 검색하고 구매할 수 있는 온라인 쇼핑몰 서비스입니다.

## 주요 기능
- **회원가입**
  - **이메일 인증 필수**
  - **중복된 ID는 허용하지 않음**
  - **비밀번호는 7자이상 15자 이하만 허용**
  - **비밀번호와 비밀번호 확인 일치 확인**
- **로그인**
  - **ID 또는 비밀번호가 일치하지 않다면 빨간색 메시지 띄움**
- **비밀번호 변경**
  - **프로필에서 비밀번호 변경 가능**
- **상품 조회**
  - **전체, 인기상품, 카테고리별 상품 조회 또는 검색 가능**
- **회원 관리(관리자)**
  - **회원 정보 조회**
- **상품 관리(관리자)**
  - **상품 등록, 수정, 삭제, 마이 페이지에서 판매 내역 조회**
- **장바구니(회원)**
  - **장바구니에 상품 추가, 삭제**
- **상품 구매(회원)**
  - **바로 구매, 장바구니에서 있는 상품 중 선택 상품 구매, 전체 상품 구매 가능, 마이 페이지에서 구매 내역 조회**
- **캐시 충전(회원)**
  - **잔액이 충분하지 않을 때, 잔액 충전**
- **리뷰(회원)**
  - **상품을 구매한 회원은 리뷰 작성 가능, 리뷰 페이지에 저장**
- **공지사항**
  - **관리자만 작성 가능한 게시판, 회원 또는 비회원은 확인만 가능**
- **관리자 페이지**
  - **상품 관리, 회원 관리, 공지사항 작성**

## 기술 스택
- **프론트엔드**: HTML/CSS, JavaScript
- **백엔드**: Java, Spring Boot
- **데이터베이스**: MySQL
- **빌드 도구**: Gradle
- **기타 도구**: IntelliJ IDEA, Git, GitHub
