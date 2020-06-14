# AOMC

## 프로젝트 개요
협업용 채팅 서비스 Slack의 핵심 기능 및 구조를 Spring Boot와 Vue.js를 통해 구현했습니다.

## 프로젝트 목표
#### 1.   Slack의 핵심 기능 모두 구현
#### 2.   스케일 아웃이 가능한 서버 아키텍처 설계
#### 3.   서버 성능 측정 및 개선 

## 1. 기능
* #### 회원가입 및 로그인 

* #### 이메일 인증
  회원가입 및 팀 초대시 이메일 인증을 거칩니다.

* #### 팀 생성 및 초대
  Slack과 같이 팀을 만들고, 팀원을 초대할 수 있습니다.

* #### 채널 생성 및 초대 
  채널은 팀 내 소규모 채팅방입니다. 개설한 팀에서 채널을 만들고, 채널 구성원을 초대할 수 있습니다.

* #### 채팅

* #### 파일 업로드 및 다운로드

![coop-chatting](https://github.com/AOMC-Coop/AOMC/blob/master/COMMON/chatting.png?raw=true)

* #### 실행 동영상 : https://www.youtube.com/watch?v=8QQRGSRJ4h8

***

## 2. 아키텍처

![Coop](https://github.com/AOMC-Coop/AOMC/blob/master/COMMON/coop-arc.png?raw=true)
(그레이 아웃 된 요소는 초기 설계에는 포함되었으나, 다른 부분에 비해 중요도가 낮다고 판단하여 최종적으로는 구현하지 않은 요소들입니다.)
- 스케일 아웃 서버 아키텍처
  - 인증 서버
    - 유저가 로그인하여 발급받은 토큰을 Redis에 저장합니다. 인증 정보가 필요한 모든 서버는 여러 대의 인증 서버가 아닌 Redis를 조회하면 됩니다.
  - 채팅 서버
    - 각 채팅 서버가 메시지 큐(RabbitMQ)를 가지고, 동일한 채팅 내용을 각 큐에 전송하는 구조를 적용했습니다. 같은 채널 내 유저들이 다른 채팅 서버에 접속해있더라도 채팅 내용이 동기화됩니다.

* 사용자는 Nginx를 통해 서버와 통신합니다.

***

## 3. 성능
* #### TPS : 249.5

![Coop](https://github.com/AOMC-Coop/AOMC/blob/master/COMMON/TPS_1.png)

성능 측정 환경 | 세부 정보 
:---: | :---: |
성능 테스트 툴 | nGrinder
호스팅 랩탑 CPU | Intel i7-6700HQ 2.60GHZ
호스팅 랩탑 OS | CentOS 7
Vuser 수 | 3,000

***

## 역할

* #### 이윤재 : ChatAPI 서버, Chatting 서버, Nginx, nGrinder, Vue.js
* #### 이은미 : ChatAPI 서버, Chatting 서버, Vue.js
* #### 최가람 : 인증 서버, 파일 서버, CentOS 배포, Vue.js
