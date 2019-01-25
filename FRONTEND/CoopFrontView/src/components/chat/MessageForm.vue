<template>


 <v-container fluid fill-height>
        <v-layout justify-center align-end>

          <v-flex xs12>
            <v-text-field
            placeholder="대화를 입력하세요."
            v-model="msg"
            outline
            @keyup.13="submitMessageFunc"
            ></v-text-field>
          </v-flex>
        </v-layout>

      </v-container>

  <!-- <div class="inner-wrap">
    <v-text-field
      v-model="msg"
      label="chat"
      placeholder="보낼 메세지를 입력하세요."
      solo
      @keyup.13="submitMessageFunc"
    ></v-text-field>
  </div> -->

</template>

<script>
export default {
  name: 'MessageForm',
  data() {
    return {
      msg: '',
    };
  },
  methods: {
    submitMessageFunc() {
      if (this.msg.length === 0) return false;
      this.$emit('submitMessage', this.msg);
      this.msg = '';
      return true;
    },
  },
};
</script>