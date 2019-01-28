// store.js
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export const store = new Vuex.Store({
  // 메세지 를 state 속성으로 추가
  state: {
    received_messages:[
      {
        content : '',
        nickname:'',
        send_date:'',
        send_time:''
      }
    ],
    inviteUsers: [
      {uid:''}, //email
    ]
  },
  getters: {
    getReceivedMessages: function (state) {
      return state.received_messages;
    }
  },
  //setter쓸때 mutations이용 / 비동기는 actions 이용 -> get 메소드 
  // mutations: {
  //   addCounter: function (state, payload) {
  //     return state.counter++;
  //   }
  // }
});