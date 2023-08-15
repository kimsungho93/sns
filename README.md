# my-sns
간단한 포스트 작성과 및 이와 관련된 기능들을 제공하는 API 서비스입니다.
# Tech stack
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/postgresql-231F20?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/apachekafka-231F20?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white">

# 🔨 프로젝트 기능 및 설계
## 🤷‍♂️ 회원
* **회원가입**
  * 회원가입은 이메일과 비밀번호를 입력 받아 진행
  * 회원가입 시 중복된 이메일 사용 불가
* **로그인**
  * 이메일과 비밀번호를 입력 받아 진행
  * 로그인 성공하면 jwt 토큰 발행하여 이후에는 요청에 대한 인가 처리를 진행한다.
## 👀 포스트
* **포스트 작성**
  * 포스트 작성은 가입된 회원만 진행이 가능하다.
  * 포스트 작성에는 간단한 포스트 제목과 내용이(텍스트 기반) 입력이 가능하다. -> 향후 사진 or 동영상 추가 예정
* **포스트 수정**
  * 수정 요청은 로그인이 되어야지만 가능하다.
  * 포스트 수정은 수정하려는 유저와 포스트 작성한 유저가 동일해야 한다.
  * 존재하지 않는 포스트에 대한 수정은 불가하다.
  * 포스트 수정은 포스트에 대한 제목 또는 내용에 대한 수정이 가능하다.
* **포스트 삭제**
  * 삭제 요청은 로그인이 되어야지만 가능하다.
  * 포스트 삭제는 삭제하려는 유저와 포스트 작성한 유저가 동일해야 한다.
  * 존재하지 않는 포스트에 대한 수정은 불가하다.
* **포스트 목록**
  * 포스트 목록 조회는 기본적으로 페이징 처리를 하여 결과 반환
  * 포스트 목록 조회는 **전체 포스트 목록 조회**, **내 포스트 목록 조회**가 가능하다.
* **댓글 작성**
  * 댓글은 가입된 회원만 진행이 가능하다.
  * 댓글 작성은 횟수 제한이 없이 가능하다.
* **좋아요**
  * 좋아요는 포스트에 대해 유저당 1회만 가능
  * 좋아요 수를 조회하는 API 작성
## 📢 알림
* **알림 보내기**
  * 알림은 작성한 포스트에 대해 좋아요 또는 댓글이 달렸을 경우로 한다.
  * 알림은 로그인이 되어야지만 확인이 가능하다.

# 🗺 ERD
  
# 🤔 Trouble Shooting

# ❗느낀점
1. 
2. 
  


