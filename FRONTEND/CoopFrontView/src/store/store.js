// store.js
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export const store = new Vuex.Store({
  // 메세지 를 state 속성으로 추가
  state: {
    ip:"http://10.240.202.225",
    stompClient:null,
    stompSubscription: null,
    connected:false,
    userId:'',
    userIdx:'',
    userNickName:'',
    scrollFlag:false,
    generalFlag:false,
    messageLastIdx:0,
    messageStartNum:0,
    channelUserCount:0,
    starChannelCount:0,
    starFlag:false,
    channelInvite:{
      fromInvite :{
        idx: '',
        uid:'',
        nickname:'',
        image:''
      },
      toInvite:[
        {
          "idx": '',
          "uid":'',
          "nickname":'',
          "image":''
        }
      ],
      channel:{
        idx:'',
        name:'',
        userHasLastIdx:''
      }
    },
    channelUsers: [
      {
        "idx":0,
        "uid":'',
        "nickname":''
      }
    ],
    exceptChannelUsers: [
      {
        idx: '' ,
        uid: '',
        nickname: '',
        you:''
      }
    ],
    channelInfo: {
      idx:'',
      channelName:'',
      userHasLastIdx:''
    },
    received_messages:[
      {
        message_idx:'',
        content : '',
        nickname:'',
        send_date:'',
        send_time:'',
        file_url:'',
        image:''
      }
    ],
    inviteUsers: [
      {uid:''}, //email
    ],
    components: []
  },
  getters: {
    getReceivedMessages: function (state) {
      return state.received_messages;
    },
    getInviteUsers: function (state) {
      return state.inviteUsers;
    }
  },
  // // setter쓸때 mutations이용 / 비동기는 actions 이용 -> get 메소드 
  // mutations: {
  //   setReceivedMessages: function (state, payload) {
  //     payload.slice(-1)[0]
  //     return state.counter++;
  //   }
  // }
});