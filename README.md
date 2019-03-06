# AOMC

## 프로젝트 정의
협업용 채팅 서비스 Slack의 핵심 기능을 Spring Boot와 Vue.js를 통해 구현하였다.

## 기능
* #### 회원가입 및 로그인 
사용자는 회원가입과 로그인을 할 수 있다.

* #### 이메일 인증
사용자는 회원가입을 할 때와 팀에 초대를 받을 때 이메일 인증을 거친다.

* #### 팀 생성
사용자는 팀을 생성할 수 있다.

* #### 팀 초대
사용자는 다른 사용자들을 팀에 초대할 수 있다.

* #### 채널 생성
사용자는 팀 내 소규모 채팅방인 채널을 생성할 수 있다.

* #### 채널 초대
사용자는 채널에 다른 사용자를 초대할 수 있다.

* #### 채팅
사용자는 다른 사용자들과 채팅을 할 수 있다.

![coop-chatting](https://github.com/AOMC-Coop/AOMC/blob/master/COMMON/chatting.png?raw=true)


* #### 파일 업로드 및 다운로드
사용자는 업로드 및 다운로드를 통해 파일을 공유 할 수 있다.

## 구조
사용자는 nGinx를 통해 서버와 통신한다. 모든 서버는 redis와 연결되는데, 사용자의 인증 토큰을 redis에 저장하여 확인하는 방식으로 connectionless하게 설계하기 위함이다. ChatApiServer와 ChattingServer, FileServer사이에 rabbitMQ를 두어 메시지가 사라질 확률을 줄였다.
// 사라질 확률이 줄어든 것인지, 사라지지 않게 한 것인지 -> 표현이 애매

***

![Coop](https://github.com/AOMC-Coop/AOMC/blob/master/COMMON/coop-architecture.png?raw=true)
// 3월 둘째 주 이후엔 ELK 스택 그레이 아웃 지우기

## 성능
* #### TPS : 
// 그래프 사진 첨부

성능 측정 환경 | 세부 정보
:---: | :---:
성능 테스트 툴 | nGrinder
호스팅 랩탑 CPU | Intel i7-6700HQ 2.60GHZ
호스팅 랩탑 OS | CentOS 7
nGrinder 랩탑 CPU |
nGrinder 랩탑 OS |
Vuser 수 |
// 그 외 필요한 정보가 있다면 추가할 것

## 로그 분석
서버 로그 수집 및 분석은 ELK 스택을 활용하였다.
// 대략의 결과 

## 역할

* #### 이윤재 : 
* #### 이은미 : 
* #### 최가람 : 인증 서버, 파일 서버, 파일 스토리지

'2019년 스마일 게이트 Winter:Dev’

***
