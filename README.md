# Spring Boot SnackMall Project

- Snack World는 Spring Boot를 이용하여 만든 다양한 스낵(먹거리)을 검색하고 구매할 수 있는 온라인 스낵몰 서비스입니다.
- 관리자
  - 상품 관리(등록, 수정, 삭제), 회원 관리, 마이 페이지에서 판매 내역 조회
- 회원
  - 장바구니, 상품 구매, 마이 페이지에서 구매 내역 조회, 비밀번호 변경, 캐시 충전
- 비회원
  - 상품 조회, 검색, 리뷰 페이지 조회만 가능

![1.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/1.JPG)
 
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
- IDE : IntelliJ
- Spring Boot 버전 : 3.3.2
- JDK 버전 : JDK 17
- 데이터베이스 : MySQL
- 빌드 도구 : Gradle
- 관리 도구 : Git, Github

## 기술 스택
- 프론트엔드
  - HTML, CSS, JavaScript, Thymeleaf
- 백엔드
  - Java, Spring Boot
- 데이터베이스
  - MySQL, MySQL Workbench
  - ID : jhl9812181
  - Password : lee2863!
  - 183.111.242.55:3306

## 데이터베이스 구성
- shop_user : 회원
- product : 상품
- cart : 장바구니를 이용한 회원
- cart_item : 장바구니에 등록된 상품
- shopping_list : 구매 내역, 판매 내역
- review: 리뷰
- notice_board : 공지사항 게시판

## View Page
### 회원가입
- 이메일 인증이 필수입니다.
- 이미 가입된 아이디는 사용이 불가능합니다.
- 비밀번호는 7~15자리로 설정해야 합니다.
- 비밀번호와 비밀번호 확인이 일치해야 합니다.
- 모든 필드를 필수로 입력해야 합니다.
    
![2.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/2.JPG)

### 로그인
- 아이디 또는 비밀번호를 잘못 입력하면 빨간색으로 메시지를 띄웁니다.
    
![3.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/3.JPG)
![4.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/4.JPG)

### 비밀번호 변경
- 로그인한 회원은 마이 페이지의 프로필에서 비밀번호를 변경할 수 있습니다.

![10.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/10.JPG)
![11.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/11.JPG)

### 장바구니
- 상품을 클릭하여 상품을 장바구니에 넣을 수 있습니다.
  
![5.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/5.JPG)

- 장바구니에서 상품을 구매할 수도 있습니다.
  
![6.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/6.JPG)

- 상품을 선택하지 않고 구매를 누르면 경고창이 뜹니다.

![12.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/12.JPG)

- 구매하려는 상품의 총 가격보다 보유 금액이 적다면 경고창이 뜹니다.

![13.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/13.JPG)

- 구매한 상품은 마이 페이지의 구매 목록에 저장됩니다.
  
![7.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/7.JPG)

### 리뷰
- 상품을 구매한 회원은 구매 목록 페이지에서 리뷰를 작성할 수 있습니다.
  
![8.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/8.JPG)

- 리뷰 페이지에서 구매자들이 작성한 리뷰를 확인할 수 있습니다.

![9.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/9.JPG)

### 재고추가
- 관리자 계정에서만 홈에서 바로 해당 상품의 재고를 추가할 수 있습니다.

![14.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/14.JPG)
![15.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/15.JPG)
![16.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/16.JPG)

### 상품추가
- 관리자 계정에서만 상단의 상품추가를 눌러서 상품을 추가할 수 있습니다.
  
![17.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/17.JPG)

### 상품관리
- 상품을 추가하면 상품관리 페이지로 이동하며 이 페이지에서 상품을 수정 또는 삭제할 수 있습니다.
  
![18.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/18.JPG)
![19.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/19.JPG)
![20.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/20.JPG)

### 회원목록
- 관리자 계정에서만 회원들의 정보를 확인할 수 있습니다.
  
![21.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/21.JPG)

### 판매 목록
- 관리자 계정에서만 마이 페이지의 판매 목록 페이지에서 판매된 상품 내역을 확인할 수 있습니다.
  
![22.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/22.JPG)

### 공지사항
- 관리자 계정에서만 공지사항을 작성할 수 있습니다. 확인은 누구나 가능합니다.

![23.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/23.JPG)
![24.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/24.JPG)
![25.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/25.JPG)
![26.JPG](https://github.com/ljh1234a/Snackmall/blob/main/img/26.JPG)
