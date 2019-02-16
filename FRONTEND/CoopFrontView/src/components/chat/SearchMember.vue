<template>
<div id="app">
    <v-flex xs12>

  <form class="ui form" >

<div class="field">
    <v-flex xs12 align-center justify-space-between> 
      <v-layout align-center>
    <v-text-field clearable justify-space-between prepend-icon="people" type="email" name="email" placeholder="Search Member" v-validate="'required|email'" v-model="searchName">
    </v-text-field>
    </v-layout>
    </v-flex>

    </div>

    
  </form>

  
</v-flex>

<!-- <v-list dense class="list">
        <template v-for="item in searchNameF"
        >
          
          <v-list-tile v-if :key="item.nickname" >
            <v-list-tile-content>
              <v-list-tile-title>
               <v-text>  {{ item.nickname }} </v-text>
              </v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </template>
      </v-list> -->

      <v-flex
              v-for="(child, i) in searchNameF"
              :key="i" xs4
              
              style = "margin:10px 50px 10px 7px;"
              >

              <v-flex xs12 align-center justify-space-between>
                  <v-layout align-center>
                    <v-list-tile-action>
                        <v-avatar size="40px" class="mr-3">
                            <img
                                src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png"
                                alt=""
                            >
                        </v-avatar>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title justify-space-between> {{ child.nickname }} </v-list-tile-title>
                    </v-list-tile-content>
                    <v-list-tile-action>
                        <v-icon right fab @click="clickInviteUserInChannel(child.idx, child.nickname)">add</v-icon>
                    </v-list-tile-action>
                  </v-layout>
                </v-flex>
            </v-flex>


</div>

</template>

<script>
import axios from "axios";
import * as hangul from 'hangul-js';
import { debug } from 'util';


export default {
  name: 'SearchMember',
  data() {
    return {
        searchName:'',
        inviteUsers:[
            {
                idx: '' ,
                uid: '',
                nickname: '',
                you:''
        }
        ]
    };
  },
  components: {
  },
  props:[
    'teamMembers',
    'channel',
    'channelName'
  ],
  
  methods: {
    clickInviteUserInChannel(userIdx, nickname) {
          debugger;
          this.channel.name = this.channelName;
          
          console.log("users = " + this.channel.users);
          // let user = {idx: userIdx, nickname: nickname};
          var check = false;
          if(this.channel.users.length == 0) {
            this.channel.users.push({idx: userIdx, nickname: nickname});
          }else {
            for(var i=0; i<this.channel.users.length; i++) {
              if(this.channel.users[i].idx == userIdx) {
                // alert("이미 추가된 사용자 입니다."); //?
                break;
              }
              if(i == this.channel.users.length-1) {
                this.channel.users.push({idx: userIdx, nickname: nickname});
              }
          }
          }

      }
  },

  computed: {
    searchNameF: function() {
        debugger;
         this.inviteUsers.splice(0);
        if(this.searchName !== '') {
            for(var i=0; i<this.teamMembers.length; i++) {
              if(hangul.search(this.teamMembers[i].nickname, this.searchName) >= 0) {
                  if(this.teamMembers[i].idx != localStorage.getItem("userIdx")) {
                      console.log(this.teamMembers[i].idx);
                      console.log(localStorage.getItem("userIdx"));
                    this.inviteUsers.push(this.teamMembers[i]);
                  }
                }else {
                
                }
          }
        }
      return this.inviteUsers;
    }
  },

  updated() {
    //   debugger;
    //   for(var i=0; i<this.teamMembers.length; i++) {
    //       if(hangul.search(this.teamMembers[i].nickname, this.searchName) < 0) {
    //             this.inviteUsers.push(this.teamMembers[i]);
    //         }else {
                
    //         }
    //   }
  },
  created() {
      debugger;
      console.log(this.teamMembers);
  }
};

</script>

