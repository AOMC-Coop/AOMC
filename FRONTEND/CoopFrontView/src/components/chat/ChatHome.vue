<template >
<!-- $route.params.teamidx -->
  <v-app id="inspire">
    <v-navigation-drawer 
      class="primary"
      fixed
      app
      permanent
    >

    <!-- 클라이언트 사이드에서도 JWT 토큰을 파괴해야 함-->
    <v-list class="white--text">
        <template v-for="item in teams">
          <v-layout
            v-if="item.heading"
            :key="item.heading"
            row
            align-center
          >
          </v-layout>
          <v-list-group
            v-else-if="item.children"
            v-model="item.model"
            class="white--text"
            :key="item.text"
          >
            <v-list-tile slot="activator">
              <v-list-tile-content>
                <v-list-tile-title style = "fontSize : 33px">
                  {{ teamName }}
                </v-list-tile-title>
                <v-list-tile-title style = "fontSize : 15px">
                  {{ userName }}
                </v-list-tile-title>
              </v-list-tile-content>
            </v-list-tile>
            <v-list-tile
              v-for="(child, i) in teamsFromServer"
              :key="i"
              @click="clickTeamName(child.idx, child.name)"
            >
              <v-list-tile-action >
                <!-- <v-icon class="white--text" >widgets</v-icon> -->
                  <img src="./../../assets/image/circle.png" alt="widgets">
              </v-list-tile-action>
              <v-list-tile-content>
                <v-list-tile-title class="white--text" style = "fontSize : 20px">
                  {{ child.name }}
                </v-list-tile-title>
              </v-list-tile-content>
            </v-list-tile>

             <v-flex xs6 >
              <v-subheader class="white--text" >
              <v-icon class="white--text" @click="addCreateTeamDialog">add</v-icon> 
              <v-text style = "fontSize : 15px">create Team</v-text>
              </v-subheader>
          </v-flex>

          </v-list-group>
        </template>
      </v-list> 

      <v-flex xs12 v-if="this.$store.state.starChannelCount > 0">
      <v-subheader class="white--text">
        <v-text style = "fontSize : 18px">  Starred </v-text>
      </v-subheader>
      <v-list dense class="white--text">
        <template v-for="item in channels">
          
          <v-list-tile v-if :key="item.text" v-if="item.star_flag === 1">
            <v-list-tile-content>
              <v-list-tile-title>
               <v-text> # {{ item.name }} </v-text>
              </v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </template>
      </v-list>
    </v-flex>
    

     <v-flex xs12>
      <v-subheader class="white--text">
         <v-text style = "fontSize : 18px"> Members </v-text>
      </v-subheader>

      <v-list dense class="white--text">
        <template v-for="item in teamMembers">
          
          <v-list-tile v-if :key="item.text" @click="">
            <v-list-tile-action>
              <!-- <v-icon class="white--text" >people</v-icon> -->
              <img src="./../../assets/image/user.png" alt="widgets">
            </v-list-tile-action>
            <v-list-tile-content>
              <v-list-tile-title>
                 {{ item.nickname }} {{item.you}}
              </v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </template>
      </v-list>
    </v-flex>

     <v-flex xs12>
      <v-subheader class="white--text">
        <v-text style = "fontSize : 18px">  Channels </v-text>
        <v-icon @click="doc_del_rendar()" class="white--text" right fab>add</v-icon>
      </v-subheader>
      <v-list dense class="white--text">
        <template v-for="item in channels">
          
          <v-list-tile v-if :key="item.text" @click="clickChannel(item.idx, item.name)">
            <v-list-tile-content>
              <v-list-tile-title>
               <v-text> # {{ item.name }} </v-text>
              </v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </template>
      </v-list>
    </v-flex>

    <v-flex xs6 >
      <v-subheader class="white--text" >
        <v-icon class="white--text" @click="addInvitePeopleDialog">add</v-icon> 
        <v-text style = "fontSize : 15px">invite people</v-text>
      </v-subheader>
    </v-flex>

    <form @submit.prevent="signOut">
      <v-btn color="warning" type="submit">Sign out</v-btn>
    </form>
    <form @submit.prevent="withdrawal">
      <v-btn color="error" type="submit">Withdrawal</v-btn>
    </form>
    <form @submit.prevent="getProfile">
      <v-btn color="success" type="submit">Profile</v-btn>
    </form>
    <form @submit.prevent="changePwd">
      <v-btn color="info" type="submit">Change Password</v-btn>
    </form>
    </v-navigation-drawer>
    
        
    <!-- <ChatRoom :key="somevalueunderyourcontrol"></ChatRoom> -->
    <ChatRoom :channels="channels" :teamMembers="teamMembers" class="chatroom"></ChatRoom>
    
    <v-dialog v-model="dialog" width="800px" id="chat">
      <v-card>
        <v-card-title
          class="grey lighten-4 py-4 title"
        >
        Invite People
        </v-card-title>
        <v-container grid-list-sm class="pa-4">
          <v-layout row wrap>
            
            <v-text>invite member</v-text><v-icon right @click="inviteMember">add</v-icon>
            

            <InviteUserEmail v-for="item in this.$store.state.components" v-bind:key="InviteUserEmail">
           <!-- vm.currentView가 변경되면 컴포넌트가 변경됩니다! -->
            </InviteUserEmail>

          </v-layout>
        </v-container>
        <v-card-actions>
          <v-btn flat color="primary">More</v-btn>
          <v-spacer></v-spacer>
          <v-btn flat color="primary" @click="clickCancel">Cancel</v-btn>
          <v-btn flat @click="clickSave">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="createTeamDialog" width="800px" id="chat">
      <v-card>
        <v-card-title
          class="grey lighten-4 py-4 title"
        >
        Create Team
        </v-card-title>
        <v-container grid-list-sm class="pa-4">
          <v-layout row wrap>
            <v-flex xs12 align-center justify-space-between>
              <v-layout align-center>

                <v-text-field
                  prepend-icon="notes"
                  placeholder="TeamName"
                  v-model="createTeamName"
                ></v-text-field>
              </v-layout>
            </v-flex>
            
            <v-text>invite member</v-text><v-icon right @click="inviteMember">add</v-icon>
            

            <InviteUserEmail v-for="item in this.$store.state.components" v-bind:key="InviteUserEmail">
           <!-- vm.currentView가 변경되면 컴포넌트가 변경됩니다! -->
            </InviteUserEmail>

          </v-layout>
        </v-container>
        
        <v-card-actions>
          <v-btn flat color="primary">More</v-btn>
          <v-spacer></v-spacer>
          <v-btn flat color="primary" @click="createTeamDialog = !createTeamDialog">Cancel</v-btn>
          <v-btn flat @click="saveTeam">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    
     <modals-container />


  </v-app>
</template>

<script>
import ChatRoom from './ChatRoom.vue'
import CreateChannel from './CreateChannel.vue'
import InviteUserEmail from './InviteUserEmail.vue'
import axios from "axios";
import moment from 'moment'
import SockJS from "sockjs-client"
import Stomp from "webstomp-client"

var now = new moment();
var today = now.format("dddd, MMMM Do").toString()

  export default {
    el: "Chat",
    components : {
    'ChatRoom' : ChatRoom,
    'CreateChannel' : CreateChannel,
    'InviteUserEmail' : InviteUserEmail
  },

 
  data: () => ({
    dialog: false,
    createTeamDialog: false,
    createTeamName:'',
    drawer: null,

    userIdx: '',
      
    teamName: '',
    userName:'',
    teams: [
        {
          icon: 'keyboard_arrow_up',
          'icon-alt': 'keyboard_arrow_down',
          text: 'aaa',
          model: false,
          children: [ // 서버에서 받아온 팀 리스트를 저장
            { idx: '', name: '', status: ''}
          ]
        }
      ],
    teamsFromServer: [ // 서버에서 받아온 팀 리스트를 저장
        { 
          idx: '' ,
          name: '',
          status: ''
        }
      ],
      teamFromServer:[ //teamsFromServer에 추가할 team
        { 
          idx: '' ,
          name: '',
          status: ''
        }
      ],
      teamMembers: [
        {
          idx: '' ,
          uid: '',
          nickname: '',
          you:''
        }
      ],
      channels: [
        {
          idx: '',
          name: '',
          star_flag: '',
          status: '',
          teamIdx: ''
        }
      ],
      inviteTeam: { //팀의 멤버 초대 
        idx:'',
        users:[],
        channels:[
          {idx:''}
        ]
      },
      createTeam: {
        name:'',
        users:[
          {uid:''}
        ]
      },
      visible: false,

      userWithToken : {
        idx : localStorage.getItem('idx'),
        token: localStorage.getItem('token')
      },
    }),
    
    props: {
      source: String,
      teamMembers: Array,
      channels: Array
    },

    methods: {
      createSocket() {
      debugger
      this.msg = '';
      this.socket = new SockJS(this.$store.state.ip + ":8083/socketconnect")
      // this.stompClient = Stomp.over(this.socket);
      this.$store.state.stompClient = Stomp.over(this.socket)
      // this.stompClient.connect(
        this.$store.state.stompClient.connect(
        {},
        frame => {
          debugger;
          console.log("connect success");
          // this.connected = true;
          // console.log("////////////////////////"+frame);
          this.$store.state.stompClient.subscribe("/topic/message", tick => {
            // console.log(tick);
            debugger
            var message = JSON.parse(tick.body);
            if(message.channel_idx === this.$store.state.channelInfo.idx){ //현재 있는 채팅방인 경우
              this.$store.state.received_messages.push(JSON.parse(tick.body));

              console.log("subcribe = " + tick.body);

              var newValue= this.$store.state.received_messages.slice(-1)[0].send_date

              for(var i=0; i<this.$store.state.received_messages.length;i++){
               if(this.$store.state.received_messages[i].send_date===now.format("dddd, MMMM Do").toString()||this.$store.state.received_messages[i].send_date==='today'){
                 this.$store.state.received_messages.slice(-1)[0].send_date = 'today'
                 newValue='today'
                }
                if(this.$store.state.received_messages[i].send_date===newValue){
                 this.$store.state.received_messages.slice(-1)[0].send_date = ''
                }
              }
            }else { // 다른 채팅방에서 메세지 올 때 알림 띄우기
              // debugger
              let nicknamePlusTime = message.nickname + "   " + message.send_date;
              this.$notify({
                group: 'foo',
                title: nicknamePlusTime,
                text: message.content,
                // animationType: velocity
              });
            }
          });
        },
        error => {
          console.log(error);
          this.connected = false;
        }
      )
      },
      addInvitePeopleDialog() {
        this.$store.state.components.splice(0);
        this.$store.state.inviteUsers.splice(0);
        this.$store.state.inviteUsers.push({uid:localStorage.getItem("userId")});
        this.dialog = !this.dialog;
      },
      saveTeam() {
        this.createTeam.name = this.createTeamName;
        this.createTeam.users = this.$store.state.inviteUsers;

        // let token = localStorage.getItem('token');
        // axios
        // .post(this.$store.state.ip + ":8083/api/team", this.createTeam, 
        // {headers: { 'X-Auth-Token': `${token}` }}
        // )
        let token = localStorage.getItem('token');
        axios
        .post(this.$store.state.ip + ":8083/api/team", this.createTeam, 
        {headers: { 'X-Auth-Token': `${token}` }}
        )
        .then(response => {
          // debugger;
            if(response.data) {
              // this.teamMembers = response.data.data;
              debugger;
              console.log(response.data);
              this.teamFromServer.idx = response.data.data;
              this.teamFromServer.name = this.createTeam.name;
              this.teamsFromServer.push(this.teamFromServer);
            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });
        
        this.createTeamDialog = false;
      },
      addCreateTeamDialog() {
        this.$store.state.components.splice(0);
        this.createTeamDialog = !this.createTeamDialog;
        this.$store.state.inviteUsers.push({uid:localStorage.getItem("userId")});
      },
      clickChannel(itemIdx, channelName) {
        if(itemIdx !== this.$store.state.channelInfo.idx){
          this.$store.state.messageLastIdx = 0;
          this.$store.state.channelInfo.idx = itemIdx;
          this.$store.state.channelInfo.channelName = channelName;
          this.$store.state.messageStartNum=0
          this.getMessage();
          this.getChannelUsers();

          if(this.$store.state.channelInfo.channelName==='general'){
            this.$store.state.generalFlag=false
          }else{
            this.$store.state.generalFlag=true
          }

          for(var i=0; i<this.channels.length; i++) {
            if(this.channels[i].idx === itemIdx) {
              if(this.channels[i].star_flag === 1) {
                this.$store.state.starFlag = true;
              }else {
                this.$store.state.starFlag = false;
              }
            }
          }
        }
      },
      clickSave() {
        debugger;
        console.log(this.$store.state.inviteUsers);
        this.inviteTeam.idx = localStorage.getItem("teamIdx");
        this.inviteTeam.users = this.$store.state.inviteUsers;
        this.inviteTeam.channels.push({idx: this.channels[0].idx});//general idx 채널을 넣어줘야함

        // axios
        // .post(this.$store.state.ip + ":8083/api/team/invite", this.inviteTeam)
        let token = localStorage.getItem('token');
        axios
        .post(this.$store.state.ip + ":8083/api/team/invite", this.inviteTeam, 
        {headers: { 'X-Auth-Token': `${token}` }}
        )
      .then(response => {
          debugger;
            if(response.data) {
              // this.teamMembers = response.data.data;
              console.log(response.data);
            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });
        
        this.dialog = false;
      },
      clickCancel() {
        // for(item in this.inviteUsers) {
        //   console.log(item.uid)
        //   debugger;
        //   this.inviteUsers.pop();
        // }
        // components.splice(0);
        this.$store.state.components.splice(0);
        this.dialog = false;
      },
      inviteMember() {
        
        this.$store.state.components.push('component');
      },
      doc_del_rendar(){
                this.$modal.show(CreateChannel,{
                    teamMembers : this.teamMembers,
                    channels: this.channels,
                    teamIdx : localStorage.getItem("teamIdx"),
                    modal : this.$modal },{
                        name: 'dynamic-modal',
                        width : '800px',
                        height : '80%',
                        draggable: true
            })
            
      },
      
      getMemberByTeamId(teamIdx){
        // axios
        // .get(this.$store.state.ip + ":8083/api/team/" + teamIdx)
        let token = localStorage.getItem('token');
        axios({
        method: 'get',
        url: this.$store.state.ip + ":8083/api/team/" + teamIdx,
        headers: { 'X-Auth-Token': `${token}` }
      })
        .then(response => {
            if(response.data) {
              this.teamMembers = response.data.data;
              for(var i=0; i<this.teamMembers.length; i++) {
                if(this.teamMembers[i].idx == this.userIdx) {
                  this.teamMembers[i].you = "(you)";
                }
              }
            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });
      },
      getChannelsByTeamIdxAndUserIdx(teamIdx, userIdx) {
        debugger
        // axios
        // .get(this.$store.state.ip + ":8083/api/team/channel/" + teamIdx + "&" + userIdx)
        let token = localStorage.getItem('token');
        axios({
        method: 'get',
        url: this.$store.state.ip + ":8083/api/team/channel/" + teamIdx + "&" + userIdx,
        headers: { 'X-Auth-Token': `${token}` }
      })
      .then(response => {
            if(response.data) {
              debugger
              this.channels = response.data.data;
              this.$store.state.channelInfo.idx = this.channels[0].idx;
              this.$store.state.channelInfo.channelName = this.channels[0].name;

              if(this.$store.state.channelInfo.channelName==='general'){
               this.$store.state.generalFlag=false
              }else{
                this.$store.state.generalFlag=true
              }

              this.$store.state.messageStartNum=0
              this.getMessage();
              this.getChannelUsers();

              for(var i=0; i<this.channels.length; i++) {
                if(this.channels[i].star_flag == 1) {
                  this.$store.state.starChannelCount = this.$store.state.starChannelCount + 1;
                }
              }
              if(this.channels[0].star_flag === 1) {
                this.$store.state.starFlag = true;
              }else {
                this.$store.state.starFlag = false;
              }
          //     for(var i=0; i<this.channels.length; i++) {
          //   if(this.channels[i].idx === itemIdx) {
          //     if(this.channels[i].star_flag === 1) {
          //       this.$store.state.starFlag = true;
          //     }
          //   }
          // }
              

            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });

      },
      getChannelsByTeamIdxAndUserIdxWithOutGetMessage(teamIdx, userIdx) {
        // axios
        // .get(this.$store.state.ip + ":8083/api/team/channel/" + teamIdx + "&" + userIdx)
        let token = localStorage.getItem('token');
        axios({
        method: 'get',
        url: this.$store.state.ip + ":8083/api/team/channel/" + teamIdx + "&" + userIdx,
        headers: { 'X-Auth-Token': `${token}` }
      })
        .then(response => {
          debugger
            if(response.data) {
              this.channels = response.data.data;
              this.$store.state.channelInfo.idx = this.channels[0].idx;
              this.$store.state.channelInfo.channelName = this.channels[0].name;
              this.$store.state.messageStartNum=0

            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });

      },
      getMessage() {
        debugger;
        this.$store.state.received_messages.splice(0);

      //   axios.get(this.$store.state.ip + ":8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx, {
      //    params: {
      //     start: this.$store.state.messageStartNum,
      //     messageLastIdx: this.$store.state.messageLastIdx
      //   },
      // })
      let token = localStorage.getItem('token');
      axios({
        method: 'get',
        url: this.$store.state.ip + ":8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx,
        params: {
          start: this.$store.state.messageStartNum,
          messageLastIdx: this.$store.state.messageLastIdx
        },
        headers: { 'X-Auth-Token': `${token}` }
      })
      .then((response) => {
        debugger
      if (response.data.status==200) {
        // this.start += 10;
        if(response.data.plusData === -3) {
          this.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
          this.$store.state.scrollFlag=false
        }
        else if(response.data.plusData === -2) {
          this.$store.state.messageStartNum = -1;
        }
        else if(response.data.plusData === -1) {
          this.$store.state.messageStartNum = 0;
        }else {
          this.$store.state.messageStartNum = response.data.plusData;
          this.$store.state.messageStartNum+=10
        }

        if(response.data.data == null) {
          // this.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
          // this.$store.state.scrollFlag=false;
          return;
        }
        
        var sendDate = "";
        var result = response.data.data.reverse();

      if(result[0].send_date===today){
        sendDate = 'today'
        result[0].send_date = 'today'
      }else{
        sendDate = result[0].send_date
      }
      
        for(var i=1;i<result.length;i++){
          if(result[i].send_date==today){
            result[i].send_date='today'
          }

          if(result[i].send_date === sendDate){
            result[i].send_date=''
          }else{
            sendDate = result[i].send_date
          }
        }
        debugger;
        this.$store.state.received_messages = result
        this.$store.state.messageLastIdx = this.$store.state.received_messages[this.$store.state.received_messages.length-1].message_idx;
        // console.log("ChatHome - LastIdx = " + this.$store.state.messageLastIdx);
        // console.log("//////////////"+this.$store.state.messageStartNum);
        // console.log(this.$store.state.received_messages);
        this.$store.state.scrollFlag=true
        localStorage.setItem('scrollControlValue', result[0].message_idx)    
        } else {
          alert(response.data.message)
        }
      });
      },
      getChannelUsers(){
      // axios.get(this.$store.state.ip + ":8083/api/channel/users", {
      //   params: {
      //     channelIdx: this.$store.state.channelInfo.idx
      //   },
      // })
      let token = localStorage.getItem('token');
      axios({
        method: 'get',
        url: this.$store.state.ip + ":8083/api/channel/users",
        params: {
         channelIdx: this.$store.state.channelInfo.idx
        },
        headers: { 'X-Auth-Token': `${token}` }
      })
      .then(response => {
            if(response.data.status===200) {
              this.$store.state.channelUsers=response.data.data
              this.$store.state.channelUserCount=this.$store.state.channelUsers.length
              this.getExceptChannelUsers();

            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });

    },
    getExceptChannelUsers(){
      
      this.$store.state.exceptChannelUsers.splice(0)

      for(var i=0; i<this.teamMembers.length;i++){
        for(var j=0; j<this.$store.state.channelUsers.length;j++){
          if(this.$store.state.channelUsers[j].idx==this.teamMembers[i].idx){
            break;
          }
          if(this.$store.state.channelUsers.length-1==j){
            this.$store.state.exceptChannelUsers.push(this.teamMembers[i])
          }
        }
      }

    },

      clickTeamName(teamIdx, teamName) {
        localStorage.setItem("teamIdx", teamIdx);
        this.$store.state.messageLastIdx = 0;

        this.teamName = teamName;
        this.userName = localStorage.getItem("userNickName"); //userName 받기
        this.getMemberByTeamId(teamIdx);
        this.getChannelsByTeamIdxAndUserIdx(teamIdx, localStorage.getItem("userIdx")); // 5->userId로 받아야 함

        // this.$store.state.channelInfo.idx = response.data.data[0].idx;
        // this.$store.state.channelInfo.channelName = response.data.data[0].name;


      },
      handleClickButton(){
      this.visible = !this.visible
    },
    signOut: function (){
      // this.stompClient.disconnect(distick => {
      //   console.log("socket disconnect");
      // });
      axios.post(this.$store.state.ip + `:8082/logout`, this.userWithToken)
      .then(response => {
          let description = response.data.description
          if(description == "Fail Logout"){
              alert("Fail to sign out!")
           } else {
              localStorage.removeItem('token')
              localStorage.removeItem('idx')
              console.log(JSON.stringify(localStorage))
              alert("Successfully Signed out!")
              location.href = './'
           }
         }).catch(e => {
          console.log(e)
          this.errors(e)
          location.href = './'
        })      
    },
    withdrawal: function (){
      let idx = localStorage.getItem('idx')
      let url = this.$store.state.ip + `:8082/members/`+ idx
      axios.put(url, this.userWithToken)
        .then(response => {
          let description = response.data.description
          if(description == "Fail Withdrawal"){
              alert("Fail to withdraw!")
           } else {
              localStorage.removeItem('token')
              localStorage.removeItem('idx')
              console.log(JSON.stringify(localStorage))
              alert("Successfully withdrew!")
              location.href = './'
           }
         }).catch(e => {
          console.log(e)
          this.errors(e)
          location.href = './'
        })      
    },
    getProfile: function (){
      let idx = localStorage.getItem('idx')
      let url = this.$store.state.ip + `:8082/profile/`+ idx
      axios.post(url, this.userWithToken)
        .then(response => {
          let description = response.data.description
          if(description == "Fail Get Profile"){
              alert("Fail to get profile!")
           } else {
              let nickname = response.data.nickname
              let gender = response.data.gender
// ***** localStorage로 저장하여도 ./Profile에는 적용되지 않는다. localStorage의 범위 알아볼 것
// <아래 방법 활용>
// : store.js
// -> Chathome.vue에서 this.$store로 찾아보기
              localStorage.setItem('nickname', nickname)
              localStorage.setItem('gender', gender)
              location.href = './profile'
           }
         }).catch(e => {
          console.log(e)
          this.errors(e)
          location.href = './'
        })      
    },
    changePwd : function (){
      debugger
      this.$router.push({path: '/pwd'})
    }


   //  else if(description == "Fail Set Profile : Wrong Idx")
  },

  mounted() {
    debugger;
    console.log("mounted");
  },
  updated() {
    // debugger;
    // this.getChannelsByTeamIdxAndUserIdxWithOutGetMessage(localStorage.getItem("teamIdx"), localStorage.getItem("userIdx"));
  },

  
    created() {
      
      this.$store.state.inviteUsers.splice(0);
      this.inviteTeam.channels.splice(0);
      // localStorage.setItem("userId", "yunjea0312@naver.com"); //test용으로 임의로 넣어놈. 원래는 로그인 할때 넣어야 함
      // this.$store.state.userId = "yunjea0312@naver.com";
      // this.$store.state.userIdx = 5; //test용으로 넣어놈. 로그인 할때 받아야함
      this.userIdx = localStorage.getItem("userIdx");
      // this.$store.state.userNickName = "yunyun"; //test용으로 넣어놈. 로그인 할때 받아야함

      console.log(this.$store.state.userId);
      console.log(this.$store.state.userIdx);
      console.log(this.$store.state.userNickName);

      console.log("localStorage = " + localStorage.getItem("userId"));
      console.log(localStorage.getItem("userIdx"));
      console.log(localStorage.getItem("userNickName"));

      this.createSocket();

      let token = localStorage.getItem('token');


      // axios
      //   .get(this.$store.state.ip + ":8083/api/team/user/" + localStorage.getItem("userIdx"), {
      //     headers: {
      //       'X-Auth-Token' : '${token}'
      //     }
      //   })
        axios({
        method: 'get',
        url: this.$store.state.ip + ":8083/api/team/user/" + localStorage.getItem("userIdx"),
        headers: { 'X-Auth-Token': `${token}` }
      })
        .then(response => { //
            if(response.data) {

              this.teamsFromServer = response.data.data;
              this.teamName = response.data.data[0].name;
              this.userName = localStorage.getItem("userNickName"); //로그인 한 후 userName 받기 -> localStorage에서 받기 
              localStorage.setItem("teamIdx", response.data.data[0].idx);
              console.log("teamIdx" + response.data.data[0].idx);
              this.getMemberByTeamId(response.data.data[0].idx);

              this.$store.state.messageLastIdx = 0;

              this.getChannelsByTeamIdxAndUserIdx(response.data.data[0].idx, localStorage.getItem("userIdx"));

              this.$store.state.channelInfo.idx = response.data.data[0].idx;
              this.$store.state.channelInfo.channelName = response.data.data[0].name;
              
              
            } else {
            //   app.renderNotification('Successfully Singed Up');
            //   app.toggleSignUp();
            this.errors.push(e);
            }
          })
        .catch(e => {
          // location.href = './';
          this.errors.push(e);
        });
      

    } 
  };

  
</script>

<style>
.v-navigation-drawer {
background-color: aqua;
color: brown;
}

.teamName {
  font-size: 50px;
}
v-dialog {
  overflow-y: scroll;
}
.chatroom{
  background-color: white;
}
img{
  /* margin: 0 */
}
</style>