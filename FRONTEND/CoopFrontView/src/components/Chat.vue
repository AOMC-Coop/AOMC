<template >
  <v-app id="inspire">
    <v-navigation-drawer 
      class="primary"
      fixed
      app
      permanent
    >
    <!-- <v-flex xs6>
      <v-subheader class="white--text">
         <v-text style = "fontSize : 30px">{{}}</v-text> 
         <v-icon @click="" class="white--text" style = "fontSize : 30px">keyboard_arrow_down</v-icon>
      </v-subheader>
    </v-flex> -->

    <v-list dense class="white--text">
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
              <v-list-tile-content >
                <v-list-tile-title style = "fontSize : 25px">
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
                <v-icon class="white--text" >widgets</v-icon>
              </v-list-tile-action>
              <v-list-tile-content>
                <v-list-tile-title class="white--text">
                  {{ child.name }}
                </v-list-tile-title>
              </v-list-tile-content>
            </v-list-tile>
          </v-list-group>
        </template>
      </v-list> 
    

     <v-flex xs6>
      <v-subheader class="white--text">
         <v-text style = "fontSize : 18px"> Members </v-text>
      </v-subheader>

      <v-list dense class="white--text">
        <template v-for="item in teamMembers">
          
          <v-list-tile v-if :key="item.text" @click="">
            <v-list-tile-action>
              <v-icon class="white--text" >people</v-icon>
            </v-list-tile-action>
            <v-list-tile-content>
              <v-list-tile-title>
                 {{ item.nickname }}
              </v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </template>
      </v-list>
    </v-flex>

     <v-flex xs6>
      <v-subheader class="white--text">
        <v-text style = "fontSize : 18px">  Channels </v-text>
        <v-icon @click="doc_del_rendar()" class="white--text" right fab>add</v-icon>
      </v-subheader>
      <v-list dense class="white--text">
        <template v-for="item in channels">
          
          <v-list-tile v-if :key="item.text" @click="">
            <!-- <v-list-tile-action>
              <v-icon class="white--text">{{ item.icon }}</v-icon>
            </v-list-tile-action> -->
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
        <v-icon class="white--text" @click="dialog = !dialog">add</v-icon> 
        <v-text style = "fontSize : 15px">invite people</v-text>
      </v-subheader>
    </v-flex>



      <!-- <v-list dense class="white--text">
        <template v-for="item in items">
          <v-layout
            v-if="item.heading"
            :key="item.heading"
            row
            align-center
          >
            <v-flex xs6>
              <v-subheader v-if="item.heading">
                {{ item.heading }}
              </v-subheader>
            </v-flex> -->
            <!-- <v-flex xs6 class="text-xs-center">
              <a href="#!" class="body-2 black--text">EDIT</a>
            </v-flex>
          </v-layout>
          <v-list-group
            v-else-if="item.children"
            v-model="item.model"
            class="white--text"
            :key="item.text"
            :prepend-icon="item.model ? item.icon : item['icon-alt']"
            append-icon=""
          >
            <v-list-tile slot="activator">
              <v-list-tile-content >
                <v-list-tile-title >
                  {{ item.text }}
                </v-list-tile-title>
              </v-list-tile-content>
            </v-list-tile>
            <v-list-tile
              v-for="(child, i) in item.children"
              :key="i"
              @click=""
            >
              <v-list-tile-action v-if="child.icon" >
                <v-icon class="white--text">{{ child.icon }}</v-icon>
              </v-list-tile-action>
              <v-list-tile-content>
                <v-list-tile-title class="white--text">
                  {{ child.text }}
                </v-list-tile-title>
              </v-list-tile-content>
            </v-list-tile>
          </v-list-group>
          <v-list-tile v-else :key="item.text" @click="">
            <v-list-tile-action>
              <v-icon class="white--text">{{ item.icon }}</v-icon>
            </v-list-tile-action>
            <v-list-tile-content>
              <v-list-tile-title>
                {{ item.text }}
              </v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </template>
      </v-list>  -->





    </v-navigation-drawer>
    <!-- <v-toolbar
      :clipped-left="$vuetify.breakpoint.lgAndUp"
      color="primary lighten-2"
      dark
      app
      fixed
    >
      <v-toolbar-title style="width: 300px" class="ml-0 pl-3">
        <v-toolbar-side-icon  @click.stop="drawer = !drawer" ></v-toolbar-side-icon>
        <span class="hidden-sm-and-down">Cooperation</span>
      </v-toolbar-title>
      <v-text-field
        flat
        solo-inverted
        hide-details
        prepend-inner-icon="search"
        label="Search"
        class="hidden-sm-and-down" 
      ></v-text-field>
      <v-spacer></v-spacer>
      <v-btn icon >
        <v-icon>apps</v-icon>
      </v-btn>
      <v-btn icon>
        <v-icon>notifications</v-icon>
      </v-btn>
      <v-btn icon large>
        <v-avatar size="32px" tile>
          <img
            src="https://cdn.vuetifyjs.com/images/logos/logo.svg"
            alt="Vuetify"
          >
        </v-avatar>
      </v-btn>
    </v-toolbar> -->
    
    <Content></Content>

    <!-- <v-btn
      fab
      bottom
      right
      color="#3F0E40"
      dark
      fixed
      @click="dialog = !dialog"
    >
      <v-icon>add</v-icon>
    </v-btn> -->
    
    <v-dialog v-model="dialog" width="800px" id="chat">
      <v-card>
        <v-card-title
          class="grey lighten-4 py-4 title"
        >
        Invite People
        </v-card-title>
        <v-container grid-list-sm class="pa-4">
          <v-layout row wrap>
            <!-- <v-flex xs12 align-center justify-space-between>
              <v-layout align-center>
                <v-avatar size="40px" class="mr-3">
                  <img
                    src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png"
                    alt=""
                  >
                </v-avatar>
                <v-text-field
                  placeholder="Name"
                ></v-text-field>
              </v-layout>
            </v-flex> -->
            <!-- <v-flex xs6>
              <v-text-field
                prepend-icon="business"
                placeholder="Company"
              ></v-text-field>
            </v-flex> -->
            <!-- <v-flex xs6>
              <v-text-field
                placeholder="Job title"
              ></v-text-field>
            </v-flex> -->
            <v-text>invite member</v-text><v-icon right @click="inviteMember">add</v-icon>
            <!-- <my-component v-for="item in buttons" :is="item"></my-component> -->
          
            <!-- <v-flex xs12>
              <v-text-field
                prepend-icon="mail"
                placeholder="Email"
              ></v-text-field>
            </v-flex> -->

            <InviteUserEmail v-for="item in this.$store.state.components" v-bind:key="InviteUserEmail">
           <!-- vm.currentView가 변경되면 컴포넌트가 변경됩니다! -->
            </InviteUserEmail>

            
            <!-- <v-flex xs12>
              <v-text-field
                type="tel"
                prepend-icon="phone"
                placeholder="(000) 000 - 0000"
                mask="phone"
              ></v-text-field>
            </v-flex>
            <v-flex xs12>
              <v-text-field
                prepend-icon="notes"
                placeholder="Notes"
              ></v-text-field>
            </v-flex> -->
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

     <modals-container />

    <!-- <create-channel
      title="This is modal"
      :visible.sync="visible">
      <div>
        This is modal body
      </div>
    </create-channel> -->

    

  </v-app>
</template>
<script>
import Content from './Content.vue'
import CreateChannel from './CreateChannel.vue'
import InviteUserEmail from './InviteUserEmail.vue'
import axios from "axios";

  export default {
    el: "Chat",
    components : {
    'Content' : Content,
    'CreateChannel' : CreateChannel,
    'InviteUserEmail' : InviteUserEmail
  },

 
  data: () => ({
      
    dialog: false,
    drawer: null,
      
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
      teamMembers: [
        {
          idx: '' ,
          uid: '',
          nickname: ''
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
      visible: false
    }),
    
    props: {
      source: String,
      teamMembers: Array,
      channels: Array
    },

    methods: {
      clickSave() {
        debugger;
        console.log(this.$store.state.inviteUsers);
        this.inviteTeam.idx = localStorage.getItem("teamIdx");
        this.inviteTeam.users = this.$store.state.inviteUsers;
        this.inviteTeam.channels.push({idx: this.channels[0]});//general idx 채널을 넣어줘야함

        
        this.dialog = false;
      },
      clickCancel() {
        debugger;
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
        axios
        .get("http://localhost:8083/api/team/" + teamIdx)
        .then(response => {
          debugger;
            if(response.data) {
              this.teamMembers = response.data.data;
            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });
      },
      getChannelsByTeamIdxAndUserIdx(teamIdx, userIdx) {
        axios
        .get("http://localhost:8083/api/team/channel/" + teamIdx + "&" + userIdx)
        .then(response => {
          debugger;
            if(response.data) {
              this.channels = response.data.data;
            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });
      },
      clickTeamName(teamIdx, teamName) {
        localStorage.setItem("teamIdx", teamIdx);
        axios
        .get("http://localhost:8083/api/team/user/" + teamIdx)
        .then(response => {
          debugger;
            if(response.data) {
              debugger;
              //this.teamsFromServer = response.data.data;
              this.teamName = teamName;
              this.userName = "yunjae"; //userName 받기
              this.getMemberByTeamId(teamIdx);
              this.getChannelsByTeamIdxAndUserIdx(teamIdx, 5); // 5->userId로 받아야 함
              
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
      },
      handleClickButton(){
      this.visible = !this.visible
    } 
    },

    updated() {
      debugger;
      console.log("Chat - update");
      // console.log(this.users);
    },

    created() {
      this.$store.state.inviteUsers.splice(0);
      this.inviteTeam.channels.splice(0);
      localStorage.setItem("userId", "yunjae"); //test용으로 임의로 넣어놈. 원래는 로그인 할때 넣어야 함
      debugger;
      axios
        .get("http://localhost:8083/api/team/user/" + "5")
        .then(response => {
          debugger;
            if(response.data) {
              debugger;
              this.teamsFromServer = response.data.data;
              this.teamName = response.data.data[0].name;
              this.userName = "yunjae"; //로그인 한 후 userName 받기 -> localStorage에서 받기 
              localStorage.setItem("teamIdx", response.data.data[0].idx);
              this.getMemberByTeamId(response.data.data[0].idx);
              this.getChannelsByTeamIdxAndUserIdx(response.data.data[0].idx, 5);
              
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
</style>