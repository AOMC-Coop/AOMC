<template>
<div class="topdiv" infinite-wrapper  style="overflow: auto;">  
  <!-- <div class="div" infinite-wrapper> -->
    <v-card-title>
      <v-icon large left>#</v-icon>
      <span class="title font-weight-light">{{this.$store.state.channelInfo.channelName}}</span>
    </v-card-title>
      <!-- <v-infinite-scroll :onTopScrollsToBottom=false :loading="loading" @top="infiniteHandler" :offset='30' style="max-height: 100%; overflow-y: scroll;"> -->

    <infinite-loading ref="infiniteLoading" direction="top" @infinite="infiniteHandler" spinner="waveDots" v-if="this.$store.state.scrollFlag" force-use-infinite-wrapper="true"></infinite-loading>
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
   <!-- </v-infinite-scroll> -->
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
      // loading:false,
      // beforeValue:0,
      // count:0
    };
  },
  watch: {
    getReceivedMessages: function (e) {
     this.change()
    }
  },
  computed:{
        ...mapGetters([
      'getReceivedMessages'
    ])
    },
  methods: {
    change () {
        this.$nextTick(() => {
          if(this.$refs.infiniteLoading.status == 2) {
            this.$refs.infiniteLoading.stateChanger.reset();
          }
      })
    },
    //한번 complete가 되고나면 함수가 아예 불리지않는 문제 해결해야함!!!!! -> 계속 reset되는것같음 -> 해결했지만 가끔 리스트2번불림 -> 포커스문제 해결해야함
    //v-infinite-scroll을 사용하면  스크롤 포커스 문제가 발생한다 -> 새로운데이터 들어왔을때 포커스가 잘안됨
    infiniteHandler() {
      // this.loading = true
      
      if(this.$store.state.messageStartNum===0)
        this.$store.state.scrollFlag=false

      axios.get("http://localhost:8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx, {
        params: {
          start: this.$store.state.messageStartNum
        },
      }).then((response) => {
        if (response.data.status==200) {

        this.$store.state.messageStartNum+=10;
        var result = response.data.data;
        var firstValue= this.$store.state.received_messages[0].send_date;

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
        }

        //  this.loading = false
        if(result.length<10){
          // $state.complete();
          this.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
          this.$store.state.scrollFlag=false
        }
        
        // $state.loaded();
        this.$refs.infiniteLoading.$emit('$InfiniteLoading:loaded');
        // this.$store.state.scrollFlag=false
      } else {
        // this.loading = false
        // $state.complete();
        this.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
        this.$store.state.scrollFlag=false
      }
    }); 

    }
  },
  created() {
    // this.$store.state.received_messages=''

    // axios.get("http://localhost:8083/api/channel/message?channelIdx=" + 8, {
    //   params: {
    //     start: this.start
    //   },
    // }).then((response) => {
    //   if (response.data.status==200) {
    //     this.start += 10;

    //     var sendDate = "";
    //     var result = response.data.data.reverse();

    //   if(result[0].send_date===today){
    //     sendDate = 'today'
    //     result[0].send_date = 'today'
    //   }else{
    //     sendDate = result[0].send_date
    //   }
      
    //     for(var i=1;i<result.length;i++){
    //       if(result[i].send_date==today){
    //         result[i].send_date='today'
    //       }

    //       if(result[i].send_date === sendDate){
    //         result[i].send_date=''
    //       }else{
    //         sendDate = result[i].send_date
    //       }
    //     }
        
    //     this.$store.state.received_messages = result          
    //     } else {
    //       alert(response.data.message)
    //     }
    //   });
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
   /* overflow-y: scroll; */
}

</style>