# AOMC

## 프로젝트 개요
협업용 채팅 서비스 Slack의 핵심 기능 및 구조를 Spring Boot와 Vue.js를 통해 구현하였다.

## 기능
* #### 회원가입 및 로그인 

* #### 이메일 인증
  회원가입 및 팀 초대시 이메일 인증을 거친다.

* #### 팀 생성 및 초대

* #### 채널 생성 및 초대 
  채널은 팀 내 소규모 채팅방이다.

* #### 채팅

* #### 파일 업로드 및 다운로드

![coop-chatting](https://github.com/AOMC-Coop/AOMC/blob/master/COMMON/chatting.png?raw=true)

* #### 실행 동영상 : https://www.youtube.com/watch?v=8QQRGSRJ4h8

## 구조
* 사용자는 Nginx를 통해 서버와 통신한다.
* 모든 서버는 Redis와 연결된다. 사용자의 인증 토큰을 Redis에 저장한 후 인증에 활용하여 connectionless하게 설계했다.
* ChatApi Server와 Chatting Server, File Server사이에 RabbitMQ를 두어 메시지가 사라지지 않도록 했다.

***

![Coop](https://github.com/AOMC-Coop/AOMC/blob/master/COMMON/coop-architecture.png?raw=true)

## 성능
* #### TPS : 249.5
![Coop](https://github.com/AOMC-Coop/AOMC/blob/master/COMMON/TPS_1.png)

성능 측정 환경 | 세부 정보 
:---: | :---: |
성능 테스트 툴 | nGrinder
호스팅 랩탑 CPU | Intel i7-6700HQ 2.60GHZ
호스팅 랩탑 OS | CentOS 7
Vuser 수 | 3,000


## 역할

* #### 이윤재 : ChatAPI 서버, Chatting 서버, Nginx, nGrinder, Vue.js
* #### 이은미 : ChatAPI 서버, Chatting 서버, Vue.js
* #### 최가람 : 인증 서버, 파일 서버, CentOS 배포, Vue.js

***
