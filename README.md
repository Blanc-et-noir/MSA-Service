<br>
<hr>
<br>

### 북덕북덕 서비스 소개

<details>
<summary>서비스 소개 확인하기</summary>

  1. 높은 교재 구매 비용에 따른 대학생들의 경제적 부담을 완화하고자 개발중인 중고 직거래 커뮤니티입니다.

  2. MSA 구조로 개발된 RESTful API 서버를 통해 서버 장애 발생시에 일부 기능은 그대로 사용할 수 있도록 가용성을 확보했습니다.

  3. Spring Cloud, Security를 활용하여 JWT 토큰을 활용한 API-Gateway를 개발했으며, 각 서비스에 대한 요청을 처리하기 이전에 사용자 인증을 총괄적으로 처리합니다.

</details>

<br>
<hr>
<br>

### 서비스 개발 언어 및 환경

<details>
<summary>개발 환경 확인하기</summary>

  #### Front-end : HTML5, CSS3, JavaScript, JQuery, Thymeleaf

  #### Back-end : Java 21, Spring Boot, Spring Security, Spring Cloud, JPA, MariaDB

</details>

<br>
<hr>
<br>

### 북덕북덕 서비스 구조

<details>
  <summary>서비스 구조 확인하기</summary>
  
![image](https://github.com/Blanc-et-noir/MSA-Service/assets/83106564/02479dfa-b0de-4dd2-833c-93c094f7d19a)

1. API GATEWAY 및 특정 기능을 담당하는 8개의 각 서비스로 구성되어 있습니다. 모든 서비스로의 요청은 반드시 API GATEWAY를 거쳐야하며, 일부 API는 인증된 사용자만 접근하실 수 있습니다.


![image](https://github.com/Blanc-et-noir/MSA-Service/assets/83106564/7e38fba7-d3bd-4893-8c0a-c65d79c2ecd8)

2. 위의 그림은 등록된 사용자가 토큰을 발급받는 과정을 나타냅니다. MSA 구조로 설계 및 개발된 서비스이므로, 해당 작업에 참여하지 않는 REPORT SERVICE, VERIFICATION SERVICE 등의 다른 서비스에 장애가 발생하더라도 사용자는 정상적으로 토큰을 발급받을 수 있습니다.

</details>

<br>
<hr>
<br>

### 북덕북덕 서비스 일부 시연

<details>
<summary>회원가입</summary>

![ezgif-6-c250e18983](https://github.com/Blanc-et-noir/MSA-Service/assets/83106564/e42745d4-0b39-40b9-94ce-8ef072596ba2)

  1. 회원 가입시 이메일 인증을 통해, 무분별한 회원 가입을 방지하고 있습니다.

</details>

<br>

<details>
<summary>로그인</summary>
  
![ezgif-5-f407c934d6](https://github.com/Blanc-et-noir/MSA-Service/assets/83106564/5b53697f-668a-4e82-beb5-e0c45ca308a3)

  1. 로그인 실패를 포함하여 에러가 발생할 경우, 사용자에게 토스트 메세지를 전달합니다. 

</details>

<br>

<details>
<summary>도서 등록</summary>

![ezgif-6-5d02562ade](https://github.com/Blanc-et-noir/MSA-Service/assets/83106564/f386eca4-839e-4823-b149-528799d36f17)

  1. 도서 등록 과정을 단계별로 진행하여, 모든 등록절차가 완료되지 않더라도, 도서 정보가 임시 저장될 수 있도록 했습니다.

  2. 임시 저장된 도서 정보는, 나중에 다시 불러와서 그대로 등록 절차를 진행하실 수 있습니다.

</details>

<br>

<details>
<summary>도서 목록 조회</summary>

![도서목록조회](https://github.com/Blanc-et-noir/MSA-Service/assets/83106564/42074f35-fb97-4dfb-b5d9-0058d3bd1b48)

1. 사이드바 메뉴를 통해 카테고리, 등급, 가격, 도서명, 출판사명 등을 기준으로 필터링하여 원하는 중고 도서를 쉽게 검색할 수 있습니다.

2. 각 도서별로 도서의 정보를 간단히 묘사한 태그를 표시함으로써, 사용자의 편의를 돕고 있습니다.

</details>

<br>
<hr>
<br>
