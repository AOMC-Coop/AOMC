# AOMC

## 프로젝트 개요
협업용 채팅 서비스 Slack의 핵심 기능 및 구조를 Spring Boot와 Vue.js를 통해 구현했습니다.

## 기능
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

## 구조

![Coop](https://github.com/AOMC-Coop/AOMC/blob/master/COMMON/coop-architecture.png?raw=true)

- 스케일 아웃 서버 아키텍처
  - 인증 서버
    - 문제점 : 인증 서버가 여러 대일 경우, 서버 간 유저 인증 정보가 일치하지 않는 문제가 발생합니다. 여러 대의 인증 서버 중, 유저가 접속한 하나의 서버에만 인증 정보가 등록되기 때문입니다. 
    - 해결 방법 : 유저가 로그인하여 발급받은 토큰을 Redis에 저장하여 이 문제를 해결했습니다. 인증 정보가 필요한 모든 서버는 여러 대의 인증 서버가 아닌 Redis를 조회하면 되기 때문입니다.
  - 채팅 서버
    - 문제점 : 다른 채팅 서버에 접속한 유저들이 같은 채팅방에서 대화를 나눌 경우, 채팅 내용이 동기화되지 않는 문제가 발생합니다.
    - 해결 방법 : 각 채팅 서버가 메시지 큐(RabbitMQ)를 가지고, 동일한 채팅 내용을 각 큐에 전송하는 구조를 적용해 이 문제를 해결했습니다.

* 사용자는 Nginx를 통해 서버와 통신합니다.

***

## 성능
* #### TPS : 249.5

nGrinder로 Vuser 3,000명 기준 로그인 TPS를 측정했습니다. 로그인 TPS 측정 초기에는 성능이 상당히 낮게 나왔고, 서버가 곧바로 다운 되기도 했습니다. 성능이 낮게 나오는 이유를 다방면으로 파악했고, 짐작이 가는 원인들을 모두 개선했습니다.

- 로그인 시 불필요하게 DB를 거치는 코드를 삭제했습니다.
- Tomcat의 maxConnection, maxThread 및 MySQL의 max_connections 수를 늘렸습니다.
- 서버 OS를 Windows 10에서 CentOS 7으로 변경했습니다.

데스크탑을 사용할 수 없었다는 점이 아쉬움으로 남습니다. 기대만큼 성능을 개선하지는 못했지만, 성능이 심각하게 낮았던 원인을 파악하고 이를 개선할 수 있었습니다.


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
