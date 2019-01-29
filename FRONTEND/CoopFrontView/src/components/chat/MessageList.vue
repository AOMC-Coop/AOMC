<template>

<!-- <div id="mydiv" v-infinite-scroll="loadMore" infinite-scroll-disabled="busy" infinite-scroll-distance="10"> -->

  <v-list class="card">
    <v-card-title>
      <v-icon large left>#</v-icon>
      <span class="title font-weight-light">{{this.$store.state.channelInfo.channelName}}</span>
    </v-card-title>

      <!-- <infinite-loading direction="top" @infinite="infiniteHandler" spinner="waveDots"></infinite-loading> -->

      <div v-for="(item,$index) in getReceivedMessages" v-bind:key="$index">
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
import InfiniteLoading from 'vue-infinite-loading';


export default {
  name: 'MessageList',
   data() {
    return {
      page: 1,
      busy: false
    };
  },
  // components: {
  //   'infinite-loading':InfiniteLoading
  // },
  methods: {
    loadMore: function() {
      this.busy = true;
      // alert("Hello")
 
      

        axios
        .get("http://localhost:8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx, {
        params: {
          page: this.page,
        },
      })
        .then(response => {
            if(response.data) {
              
              this.$store.state.received_messages = response.data.data;
              this.page += 1;
              
            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });
        




        this.busy = false;
     
    }
  },
  // methods: {
  //   infiniteHandler($state) {
  //     // alert('Hello')

  //     axios.get("http://localhost:8083/api/channel/message?channelIdx=" + "40", {
  //       params: {
  //         page: this.page,
  //       },
  //     }).then(({ data }) => {
  //       if (data.data) {

  //         debugger
  //         this.$store.state.received_messages = response.data.data;
  //         this.page += 1;
  //         // this.list.unshift(...data.hits.reverse());
  //         $state.loaded();
  //       } else {
  //         $state.complete();
  //       }
  //     });
  //   },
  // },
  // methods: {
  //   infiniteHandler($state) {
  //     axios.get("http://localhost:8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx)
  //     .then(response => {
  //       if(response.data) {
  //         this.$store.state.received_messages = response.data.data;
  //       } else {
  //         this.errors.push(e);
  //       }
  //     }).catch(e => {
  //       this.errors.push(e);
  //     });
  //   },
  // },


    // created() {
      // debugger;
      // this.$nextTick(function() {
        // debugger;
        // axios
        // .get("http://localhost:8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx)
        // .then(response => {
        //   debugger;
        //     if(response.data) {
              
        //       this.$store.state.received_messages = response.data.data;
        //       debugger;
        //     //   console.log(msgs);
              
        //     } else {
        //     //   app.renderNotification('Successfully Singed Up');
        //     //   app.toggleSignUp();
        //     this.errors.push(e);
        //     }
        //   })
        // .catch(e => {
        //   // location.href = './';
        //   this.errors.push(e);
        // });
      // })
      
    // },
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
.card{
  padding-left: 2%;
  /* background-color: aqua; */
}
</style>