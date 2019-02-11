<template>
<div>
    <v-card-title>
        <v-icon large left>#</v-icon>
        <span class="title font-weight-light">{{this.$store.state.channelInfo.channelName}}</span>
    </v-card-title>
</div>

</template>
<script>
export default {
  data() {
    return {
      // start:10
      // datas: [],
      // msgs: '',
    };
  },
  
    
  methods: {    
    sendMessage(msg) {
      // console.log(msg);
      // this.pushMsgData({
      //   from: {
      //     name: '나',
      //   },
      //   msg,
      // });
      // this.$sendMessage({
      //   name: this.$route.params.username,
      //   msg,
      // });
    },
  },
};
</script>