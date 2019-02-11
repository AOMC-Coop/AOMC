<template>
<div>
     <v-toolbar
      :clipped-left="$vuetify.breakpoint.lgAndUp"
      color="white-text"
      flat
    >
      <v-toolbar-title style="width: 150px" class="ml-0 pl-3">
        <v-icon large left>#</v-icon>
        <span class="hidden-sm-and-down">{{this.$store.state.channelInfo.channelName}}</span>
      </v-toolbar-title>
      
      <v-spacer></v-spacer>
      <v-btn icon>
        <v-avatar size="25px" tile>
          <img src="./../../assets/image/star_off.png" alt="Vuetify">
        </v-avatar>
      </v-btn>
      <v-text-field
        flat
        hide-details
        prepend-inner-icon="search"
        label="Search"
        class="hidden-sm-and-down"
      ></v-text-field>
      
      
    
      <v-btn icon @click="">
        <v-avatar size="25px" tile>
          <img src="./../../assets/image/settings.png" alt="Vuetify">
        </v-avatar>
      </v-btn>
      <v-btn icon @click="">
        <v-avatar size="25px" tile>
          <img src="./../../assets/image/invite.png" alt="Vuetify">
        </v-avatar>
      </v-btn>      
      <v-btn icon @click="exitChannel" v-if="this.$store.state.generalFlag">
        <v-avatar size="20px" tile>
          <img src="./../../assets/image/exit.png" alt="Vuetify">
        </v-avatar>
      </v-btn>

      <!-- <v-btn icon large>
        <v-avatar size="32px" tile>
          <img
            src="https://cdn.vuetifyjs.com/images/logos/logo.svg"
            alt="Vuetify"
          >
        </v-avatar>
      </v-btn> -->
      
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
    };
  },
  
    
  methods: {    
    exitChannel() {
        axios.delete(this.$store.state.ip + ":8083/api/channel", {
        params: {
          channelIdx: this.$store.state.channelInfo.idx,
          userIdx: this.$store.state.userIdx
        },
      })
        .then(response => {
          // debugger;
            if(response.data) {
              console.log("[exitChannel]"+response.data);
              console.log(response.data);
            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });
      
    },
  }
  
};
</script>