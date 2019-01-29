<script src="https://unpkg.com/vee-validate@2.0.0-beta.25"></script>

<template>
<v-flex xs12>
<div id="app">
  <form class="ui form" >

<div class="field">
    <v-flex xs12 align-center justify-space-between> 
      <v-layout align-center>
    <v-text-field clearable justify-space-between prepend-icon="mail" type="email" name="email" placeholder="Email" v-validate="'required|email'" v-model="email">
    </v-text-field>
    <v-icon right fab @click="add">add</v-icon>
    </v-layout>
    <span class="error" v-if="errors.has('email')">{{errors.first('email')}}</span>
    </v-flex>

    </div>

    
  </form>
</div>
</v-flex>
</template>

<script>

export default{
  el: 'InviteUserEmail',
  data: () => ( {
      email: ''
  }),

  methods: {
    add() {
      this.$validator.validateAll()
      debugger;
      if (!this.errors.any()) {
        this.$store.state.inviteUsers.push({uid:this.email});
        alert('추가 되었습니다.')
      }else {
        alert('잘못된 이메일 입니다.')
      }
    }
  },
  created() {
    // this.$store.state.inviteUsers.pop();
    // this.$store.state.inviteUsers.splice(0);
  }
}
</script>

<style>
@import url('https://unpkg.com/semantic-ui-css@2.2.9/semantic.css');

span.error {
  color: #9F3A38;
}
</style>