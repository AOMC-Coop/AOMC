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
import moment from 'moment'

var now = new moment();
var today = now.format("dddd, MMMM Do").toString()


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

      // debugger
      axios.get("http://localhost:8083/api/channel/message?channelIdx=" + 8, {
        params: {
          start: this.start
        },
      }).then((response) => {
        if (response.data.status==200) {

        this.start += 10;
        var result = response.data.data;

        var firstValue= this.$store.state.received_messages[0].send_date;
        console.log(firstValue)

        // if(this.$store.state.received_messages[i].send_date===today||this.$store.state.received_messages[i].send_date==='today'){
        //         this.$store.state.received_messages.slice(-1)[0].send_date = 'today'
        //         newValue='today'
        //}

        debugger
        for(var i=0;i<result.length;i++){
          if((result[i].send_date===today&&firstValue==='today')){
            this.$store.state.received_messages[0].send_date=''
            result[i].send_date = 'today'
          }
         
          if(result[i].send_date===firstValue){
            this.$store.state.received_messages[0].send_date=''
          }else{
            firstValue = result[i].send_date
          }
          this.$store.state.received_messages.unshift(result[i]);

          console.log(i+"내용은 = "+result[i].content)
          console.log(i+"날짜는 = "+result[i].send_date)
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

        var sendDate = "";
        var result = response.data.data.reverse();

      if(result[0].send_date===today){
        sendDate = 'today'
        result[0].send_date = 'today'
      }else{
        sendDate = result[0].send_date
      }

        

        // debugger
        for(var i=1;i<result.length;i++){
          if(result[i].send_date==today){
            result[i].send_date='today'
          }

          if(result[i].send_date === sendDate){
            result[i].send_date=''
          }else{
            sendDate = result[i].send_date
          }
        }
        
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