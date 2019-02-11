<template>

   <v-app id="inspire">

    <!-- <v-flex xs6 >
    <v-card class="card">
          <v-card-title
            class="headline grey lighten-2"
            primary-title
          >
            You have not any team
          </v-card-title>
  
          <v-card-text>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
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

    </v-flex> -->
    <v-btn
              color="primary"
              flat
              @click="addCreateTeamDialog"
            >
            Create Team
            </v-btn>

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
      inviteMember() {
        
        this.$store.state.components.push('component');
      },
    addCreateTeamDialog() {
        this.createTeamDialog = !this.createTeamDialog;
        this.$store.state.inviteUsers.push({uid:localStorage.getItem("userId")});
      },
      saveTeam() {
        this.createTeam.name = this.createTeamName;
        this.createTeam.users = this.$store.state.inviteUsers;

        axios
        .post(this.$store.state.ip + ":8083/api/team", this.createTeam)
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
  },
};

</script>

<style>
div{
    width: 100%;
    height: 100%;
}
.card{
    margin-left: 50%;
    margin-top: 30%;
}
</style>