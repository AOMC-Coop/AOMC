<template>
  <v-form>
    <v-container fluid>
      <form @submit.prevent="sendNewPwd">
      <v-layout row>

        <v-flex xs12 sm6>
          <v-text-field
            :append-icon="show1 ? 'visibility_off' : 'visibility'"
            :rules="[rules.required, rules.min]"
            :type="show1 ? 'text' : 'password'"
            name="input-10-1"
            label="enter new password"
            hint="At least 8 characters"
            counter
            @click:append="show1 = !show1"
            v-model="pwdInfo.pwd"
          ></v-text-field>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs12 sm6>
          <v-text-field
            :append-icon="show3 ? 'visibility_off' : 'visibility'"
            :rules="[rules.required, rules.min, comparePasswords]"
            :type="show3 ? 'text' : 'password'"
            name="input-10-2"
            label="enter new password again"
            hint="At least 8 characters"
            value="wqfasds"
            class="input-group--focused"
            @click:append="show3 = !show3"
            v-model="pwdInfo.confirm_pwd"
          ></v-text-field>
        </v-flex>
      </v-layout>  
      <v-layout row>
        <v-btn type="submit" :loading="loading" >Change Password</v-btn>
      </v-layout> 
      </form>
    </v-container>
  </v-form>
</template>

<script>
import axios from 'axios'

  export default {
    data () {
      return {
        pwdInfo: {
          idx: '',
          pwd: '',
          confirm_pwd: ''
        },
        show1: false,
        show2: true,
        show3: false,
        show4: false,
        password: 'Password',
        rules: {
          required: value => !!value || 'Required.',
          min: v => v.length >= 8 || 'Min 8 characters',

        }
      }
    },
    computed: {
      comparePasswords () {
        return this.pwdInfo.pwd !== this.pwdInfo.confirm_pwd ? 'Passwords do not match.' : true
      }
    },
    methods: {
      sendNewPwd: function () {
        if(this.pwdInfo.pwd !== this.pwdInfo.confirm_pwd ){
            alert('Passwords do not match! Are you insane?')
        } else {
            let idx = localStorage.getItem('idx')
            let url = "http://localhost:8082/api/members/pwd/" + idx
            this.pwdInfo.idx = idx
            debugger
            axios.put(url, this.pwdInfo)
            .then(response => { 
              let description = response.data.description
              if(description == "Success Change Password"){
                alert("Successfully changed password!")
                this.$router.push({path: '/chat'})
              } else {
                alert("Fail to change password!")
              }
            }
            ).catch(e => {
              console.log(e)
              this.errors(e)

            })
        }
        
        }
    }
}
</script>