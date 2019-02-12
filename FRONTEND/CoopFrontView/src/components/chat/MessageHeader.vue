<template>
<div>
    
     <v-toolbar
      :clipped-left="$vuetify.breakpoint.lgAndUp"
      color="white-text"
      flat
    >
    
      <v-toolbar-title style="width: 130px" class="ml-0 pl-3">
        <!-- <v-icon large class="title font-bold" >#</v-icon> -->
        <span class="title font-weight-bold"># </span>
        <span class="title font-weight-bold">{{this.$store.state.channelInfo.channelName}}</span>
      </v-toolbar-title>
      
      
      
      <v-spacer></v-spacer>
      
      <v-btn icon @click="getUsers">
      <v-avatar size="18px" tile>
        <img src="./../../assets/image/user_black.png" alt="Vuetify">
      </v-avatar>
      <span>5</span>
      </v-btn>

      <v-btn icon @click="starClick" v-if="star === false" >
        <v-avatar size="25px" tile>
          <img src="./../../assets/image/star_off.png" alt="Vuetify">
        </v-avatar>
      </v-btn>

      <v-btn icon @click="starClick" v-if="star === true" >
        <v-avatar size="25px" tile>
          <img src="./../../assets/image/star_on.png" alt="Vuetify">
        </v-avatar>
      </v-btn>

      <!-- <v-text-field
        flat
        hide-details
        prepend-inner-icon="search"
        label="Search"
        class="hidden-sm-and-down"
      ></v-text-field> -->
      
      
        
    
      <v-btn icon @click="">
        <v-avatar size="25px" tile>
          <img src="./../../assets/image/settings.png" alt="Vuetify">
        </v-avatar>
      </v-btn>
      <v-btn icon @click="inviteChannel">
        <v-avatar size="25px" tile>
          <img src="./../../assets/image/invite.png" alt="Vuetify">
        </v-avatar>
      </v-btn>      
      <v-btn icon @click="exitChannel" v-if="this.$store.state.generalFlag">
        <v-avatar size="20px" tile>
          <img src="./../../assets/image/exit.png" alt="Vuetify">
        </v-avatar>
      </v-btn>      
    </v-toolbar>
    <!-- <v-card-title>
        <v-icon large left>#</v-icon>
        <span class="title font-weight-light">{{this.$store.state.channelInfo.channelName}}</span>
    </v-card-title> -->
</div>

</template>
<script>
import axios from "axios";

export default {
  data() {
    return {
      star: '',
    };
  },
  
    
  methods: {
    starClick() {
      if(star === false) { //star_flag가 0에서 1로 바껴야 함
        
      }else { //star_flag가 1에서 0로 바껴야 함

      }
      this.star = !this.star;
    },
    inviteChannel(){
            //     this.$modal.show(CreateChannel,{
            //         teamMembers : this.teamMembers,
            //         channels: this.channels,
            //         teamIdx : localStorage.getItem("teamIdx"),
            //         modal : this.$modal },{
            //             name: 'dynamic-modal',
            //             width : '800px',
            //             height : '80%',
            //             draggable: true
            // })
            
    },
    exitChannel() {
        axios.delete(this.$store.state.ip + ":8083/api/channel", {
        params: {
          channelIdx: this.$store.state.channelInfo.idx,
          userIdx: this.$store.state.userIdx
        },
      }).then(response => {
          // debugger;
            if(response.data.status===200) {
              alert('200')
              //채널나가면 바로 반영되도록하기
            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });      
    },
  },

  created() {
    this.star = false; //star_flag 보고 바꾸기
  }
  
};
</script>
<style>

</style>
