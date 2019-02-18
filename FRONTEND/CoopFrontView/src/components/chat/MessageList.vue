<template>
<div class="topdiv" infinite-wrapper  style="overflow: auto;">  
  <!-- <div class="div" infinite-wrapper> -->
    
      <!-- <v-infinite-scroll :onTopScrollsToBottom=false :loading="loading" @top="infiniteHandler" :offset='30' style="max-height: 100%; overflow-y: scroll;"> -->

    <infinite-loading ref="infiniteLoading" direction="top" @infinite="infiniteHandler" spinner="waveDots" v-if="this.$store.state.scrollFlag" force-use-infinite-wrapper="true"></infinite-loading>
   <v-list class="card">
     
      <div v-for="(item,index) in getReceivedMessages" v-bind:key="index" class="list_div">
        <v-divider v-if="item.send_date" :key="index" inset ></v-divider>
        <v-subheader v-if="item.send_date" :key="item.send_date">{{ item.send_date }}</v-subheader>
        <!-- <v-list-tile> -->
          <v-card-actions>
            <v-avatar size="42px" class="mr-3">
              <v-img
            class="elevation-6"
            :src= "item.image"
          ></v-img>
                  <!-- <v-img src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png" alt=""/> -->
            </v-avatar>

            <v-list-tile-content v-if="item.file_url !== null">
            <v-list-tile-title><h5>{{item.nickname}} {{item.send_time}}</h5></v-list-tile-title>
            <v-card-text>{{item.content}}</v-card-text>
            <v-card-text>{{item.file_url}}</v-card-text>
          </v-list-tile-content>
          
          <v-list-tile-content v-else-if="item.file_url === null">
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
      // beforeValue:0,
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
    infiniteHandler() {
      console.log("1 "+this.$store.state.messageStartNum);
      console.log(this.$store.state.messageLastIdx);
      debugger;
      // if(this.$store.state.messageStartNum===-1)
      //   this.$store.state.scrollFlag=false

      // if(this.$store.state.scrollFlag === true) {
      // axios.get(this.$store.state.ip + ":8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx, {
      //   params: {
      //     start: this.$store.state.messageStartNum,
      //     messageLastIdx: this.$store.state.messageLastIdx
      //   },
      // })
      let token = localStorage.getItem('token');
      debugger
      console.log(this.$store.state.messageLastIdx);
       axios({
        method: 'get',
        url: this.$store.state.ip + ":8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx,
        params: {
          start: this.$store.state.messageStartNum,
          messageLastIdx: this.$store.state.messageLastIdx
        },
        headers: { 'X-Auth-Token': `${token}` },
      })
      .then((response) => {
        if (response.data.status==200) {
        debugger;
        console.log("2 "+this.$store.state.messageStartNum);

        if(response.data.plusData === -3) {
          this.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
          this.$store.state.scrollFlag=false
        }
        else if(response.data.plusData === -2) {
          this.$store.state.messageStartNum = -1;
        }
        else if(response.data.plusData === -1) {
          this.$store.state.messageStartNum = 0;
        }else {
          this.$store.state.messageStartNum = response.data.plusData;
          this.$store.state.messageStartNum+=10;
        }

        if(response.data.data == null) {
          this.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
          this.$store.state.scrollFlag=false;
          return;
        }

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

        if(result.length<10){
          debugger;
          this.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
          this.$store.state.scrollFlag=false
        }
        
        this.$refs.infiniteLoading.$emit('$InfiniteLoading:loaded');
        // this.$store.state.scrollFlag=false
      } else {
        debugger;
        this.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
        this.$store.state.scrollFlag=false
      }
      this.$store.state.messageLastIdx = result[0].message_idx;
      console.log("MessageList - LastIdx = " + this.$store.state.messageLastIdx);
    }); 
    
    }
  },
  created() {
    
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
.list_div:hover {
  background: rgba(230, 230, 230, 0.979);
}

</style>