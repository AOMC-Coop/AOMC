// store.js
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export const store = new Vuex.Store({
  // 메세지 를 state 속성으로 추가
  state: {
    ip:"http://localhost",
    stompClient:null,
    userId:'',
    userIdx:'',
    userNickName:'',
    scrollFlag:false,
    messageLastIdx:0,
    messageStartNum:0,
    channelInfo: {
      idx:'',
      channelName:''
    },
    received_messages:[
      {
        message_idx:'',
        content : '',
        nickname:'',
        send_date:'',
        send_time:''
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