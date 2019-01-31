<template>
<div class="div" infinite-wrapper  style="overflow: auto;">
    <v-card-title>
      <v-icon large left>#</v-icon>
      <span class="title font-weight-light">{{this.$store.state.channelInfo.channelName}}</span>
    </v-card-title>
    <infinite-loading direction="top" @infinite="infiniteHandler" spinner="waveDots" v-if="flag" force-use-infinite-wrapper="true"></infinite-loading>

   <v-list class="card">   
      <div v-for="(item,index) in getReceivedMessages" v-bind:key="index">
        <v-divider v-if="item.send_date" :key="index" inset ></v-divider>
        <v-subheader v-if="item.send_date" :key="item.send_date">{{ item.send_date }}</v-subheader>
        <!-- <v-list-tile> -->
          <v-card-actions>
            <v-avatar size="42px" class="mr-3">
              <v-img
            class="elevation-6"
            src="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
          ></v-img>
                  <!-- <v-img src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png" alt=""/> -->
            </v-avatar>
          
          <v-list-tile-content>
            <v-list-tile-title><h5>{{item.nickname}} {{item.send_time}}</h5></v-list-tile-title>
            <v-card-text>{{item.content}}</v-card-text>
          </v-list-tile-content>
        <!-- </v-list-tile> -->
        </v-card-actions>
      </div>
  </v-list>

    <!-- <v-list subheader three-line>
     <transition-group name="list">
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
      </div>
    </transition-group> 
  </v-list> -->

</div>

 
  
</template>
<script>
import axios from "axios";
import { mapGetters } from 'vuex'

export default {
  name: 'MessageList',
   data() {
    return {
      start:0,
      flag:true
    };
  },
  computed:{
        ...mapGetters([
      'getReceivedMessages'
    ])
    },
  methods: {
    infiniteHandler($state) {
      // alert('hello')

      debugger
      axios.get("http://localhost:8083/api/channel/message?channelIdx=" + 8, {
        params: {
          start: this.start
        },
      }).then((response) => {
        if (response.data.status==200) {
          debugger
          this.start += 10;
          var result = response.data.data;
          for(var i=0;i<result.length;i++){
              this.$store.state.received_messages.unshift(result[i]);
          }
          if(result.length<10){
            $state.complete();
          }
          $state.loaded();
        } else {
          $state.complete();
        }
      }); 

    }
  },
  created() {
    this.$store.state.received_messages=''

    axios.get("http://localhost:8083/api/channel/message?channelIdx=" + 8, {
      params: {
        start: this.start
      },
    }).then((response) => {
      if (response.data.status==200) {
        this.start += 10;
        var result = response.data.data.reverse();
        this.$store.state.received_messages = result          
        } else {
          alert(response.data.message)
        }
      });
  },
     
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
.card{
  padding-left: 2%;
}
.div{
   overflow-y: scroll;
}

</style>