<template>
<div>

<!-- <div v-infinite-scroll="loadMore" infinite-scroll-disabled="busy" infinite-scroll-distance="10"> -->

<div infinite-wrapper>
  <div id="msg-list" style="overflow: auto;" ref="scrollWrapper">
  
   <v-list class="card">
    <v-card-title>
      <v-icon large left>#</v-icon>
      <span class="title font-weight-light">{{this.$store.state.channelInfo.channelName}}</span>
    </v-card-title>
          <infinite-loading direction="top" @infinite="infiniteHandler" spinner="waveDots"></infinite-loading>
      <!-- <infinite-loading direction="top" :on-infinite="onInfinite" ref="infiniteLoading"
      spinner="waveDots"></infinite-loading> -->

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
</div>

</div>

 
  
</template>
<script>
import axios from "axios";
import { mapGetters } from 'vuex'
import InfiniteLoading from 'vue-infinite-loading';

export default {
  name: 'MessageList',
  // components: {
  //   InfiniteLoading,
  // },
   data() {
    return {
      start:0,
      height: 0,
      scrollingEnabled: false
    };
  },
   mounted() {
     
    this.loadInitial()
		// this.container.scrollTop = this.container.scrollHeight;
    // this.scrollingEnabled = true;
		// this.$nextTick(() => {
      // this.$refs.infiniteLoading.$emit('$InfiniteLoading:loaded');
    // });
      
      
	},
  methods: {
    loadInitial(){
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
    infiniteHandler($state) {
    // onInfinite() {
      // alert('hello')
       debugger
       setTimeout(() => {

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

              // this.height = this.container.scrollHeight
              this.height = this.$refs.scrollWrapper.scrollHeight

              if(result.length<10){
                $state.complete();
                //  this.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
              }
              $state.loaded();
              // this.$refs.infiniteLoading.$emit('$InfiniteLoading:loaded');
            } else {
              $state.complete();
              // this.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');

            }
            }, 1000);
        // // // //
      }); 

    }
  },

  watch: {
    
    height: function (newVal, oldVal) {
      var diff = newVal - oldVal
      // this.container.scrollTop = diff
      debugger
      this.$refs.scrollWrapper.scrollTop = diff
    }
  },
 
  created() {
    





  },

    computed:{
        ...mapGetters([
      'getReceivedMessages'
    ]),
    container() {
			return this.$el.querySelector('#msg-list');
		},
    }
    
     
};

//https://github.com/yelianjie/nbbp2.0/blob/a46e27b6b6c7b4789c2487c71f32ff02d0cd98fd/src/components/HelloWorld.vue

//https://github.com/milesrout/irc-log-viewer/blob/3a9d28f0e048d332845dfb57d7c1de0a873763d2/www/index.html
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
</style>