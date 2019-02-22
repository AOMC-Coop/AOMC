<template>

   <v-app id="inspire">
<div class="topDiv">
    <v-flex xs6 >
    <v-card class="cardshape">
          <v-card-title
            class="headline grey lighten-2"
            primary-title
          >
            You don't have any team
          </v-card-title>
  
          <v-card-text>
            새로운 팀을 생성하시고, 친구들과 즐거운 채팅을 시작해보세요!
          </v-card-text>
  
          <v-divider></v-divider>
  
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="primary"
              flat
              @click="addCreateTeamDialog"
            >
              Create Team
            </v-btn>
          </v-card-actions>
        </v-card>

    </v-flex>

<div>
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
          <v-btn flat color="primary" @click=" createTeamDialog = !createTeamDialog">Cancel</v-btn>
          <v-btn flat @click="saveTeam">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    </div>

    </div>

    

   </v-app>


</template>

<script>
import axios from "axios";
import InviteUserEmail from './InviteUserEmail.vue'


export default {
  name: 'CheckTeam',
  data() {
    return {
      createTeamDialog: false,
      createTeamName:'',
      createTeam: {
        name:'',
        users:[
          {uid:''}
        ]
      },
    };
  },
  components: {
      'InviteUserEmail' : InviteUserEmail
  },
    
  methods: {    
      clickCancel() {
        createTeamDialog = !createTeamDialog;
      },
      inviteMember() {
        
        this.$store.state.components.push('component');
      },
      addCreateTeamDialog() {
        this.$store.state.components.splice(0);
        this.createTeamDialog = !this.createTeamDialog;
        this.$store.state.inviteUsers.push({uid:localStorage.getItem("userId")});
      },
      saveTeam() {
        this.createTeam.name = this.createTeamName;
        this.createTeam.users = this.$store.state.inviteUsers;

        // axios
        // .post(this.$store.state.ip + ":8083/api/team", this.createTeam)
        let token = localStorage.getItem('token');
        axios
        .post(this.$store.state.ip + ":8083/api/team", this.createTeam, 
        //  .post("/api/team", this.createTeam, 
        {headers: { 'X-Auth-Token': `${token}` }}
        )
        .then(response => {
            if(response.data) {
              console.log(response.data);
            this.$router.push({path: '/chat'});
            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });
        
        this.createTeamDialog = false;
      },
  },
  created() {
      this.$store.state.inviteUsers.splice(0);
  }
};

</script>

<style>
.cardshape{
    width: 100%;
    margin-left: 50%;
    margin-top: 30%;
}
</style>