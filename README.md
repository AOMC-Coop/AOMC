# AOMC
Slack-like Chatting Server.

## 프로젝트 정의
공동 작업의 중심지로써 대화가 일어나고 의사결정이 이루어지는 slack을 모방한 채팅 서비스

## 기능
* #### 로그인 및 회원가입
사용자는 로그인과 회원가입을 할 수 있다.

* #### 이메일 인증
사용자는 회원가입과 팀에 초대받을 때 이메일 인증을 한다.

* #### 팀 생성
사용자는 팀을 생성할 수 있다.

* #### 팀 초대
사용자는 다른 사용자들을 팀에 초대할 수 있다.

* #### 채널 생성
사용자는 팀 내에 채널을 생성할 수 있다.

* #### 채널 초대
사용자는 채널에 다른 사용자를 초대할 수 있다.

* #### 채팅
사용자는 다른 사용자들과 채팅을 할 수 있다.

![coop-chatting](https://github.com/AOMC-Coop/AOMC/blob/master/COMMON/chatting.png?raw=true)


* #### 파일 업로드 및 다운로드
사용자는 파일을 업로드 및 다운로드 할 수 있다.

## 구조
사용자는 nGinx를 통해 서버와 통신하고 모든 서버는 redis와 연결되어 사용자의 인증토큰값을 이용하여 connectionless하게 설계하였으며 ChatApiServer와 ChattingServer, FileServer사이에 rabbitMQ를 두어 메세지가 사라질 확률을 줄였다.

***

![Coop](https://github.com/AOMC-Coop/AOMC/blob/master/COMMON/coop-architecture.png?raw=true)

***

'2019년 스마일 게이트 Winter:Dev’

***
