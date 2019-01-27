<template>
<div>
    <v-list subheader three-line>
    <!-- <transition-group name="list"> -->
      <div v-for="(item,index) in getReceivedMessages" v-bind:key="index">
        <v-divider v-if="item.send_date" :key="index" inset ></v-divider>
        <v-subheader v-if="item.send_date" :key="item.send_date">{{ item.send_date }}</v-subheader>
        <v-list-tile>
          <v-list-tile-action>
            <v-avatar size="42px" class="mr-3">
                  <img
                    src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png"
                    alt=""
                  >
                </v-avatar>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title><h5>{{item.nickname}} {{item.send_time}}</h5></v-list-tile-title>
            <v-list-tile-title>{{item.content}}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <!-- <v-divider inset></v-divider> -->
      </div>
    <!-- </transition-group> -->
  </v-list>
</div>

  
</template>

<script>
import axios from "axios";
import { mapGetters } from 'vuex'

export default {
  name: 'MessageList',


    created() {
      
      axios
        .get("http://localhost:8083/api/channel/message?channelIdx=" + "40")
        .then(response => {
          debugger;
            if(response.data) {
              
              this.$store.state.received_messages = response.data.data;
              debugger;
            //   console.log(msgs);
              
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
    computed:{
        ...mapGetters([
      'getReceivedMessages'
    ])
    }
    
     
};
</script>

<style>
.list-item {
  display: inline-block;
  margin-right: 10px;
  margin-bottom: 50px;
}
.list-enter-active, .list-leave-active {
  transition: all 1s;
}
.list-enter, .list-leave-to /* .list-leave-active below version 2.1.8 */ {
  opacity: 0;
  transform: translateX(30px);
}
</style>