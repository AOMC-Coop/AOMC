<template>
  <v-form>
    <v-container fluid>
      <v-layout row wrap>
        <form @submit.prevent="changePwd"> 
            <v-flex xs12 sm6>
                <v-text-field
                    v-model="password"
                    :append-icon="show1 ? 'visibility_off' : 'visibility'"
                    :rules="[rules.required, rules.min]"
                    :type="show1 ? 'text' : 'password'"
                    name="input-10-1"
                    label="Normal with hint text"
                    hint="At least 8 characters"
                    counter
                    @click:append="show1 = !show1"
                ></v-text-field>
                </v-flex>

                <v-flex xs12 sm6>
                <v-text-field
                    :append-icon="show2 ? 'visibility_off' : 'visibility'"
                    :rules="[rules.required, rules.min]"
                    :type="show2 ? 'text' : 'password'"
                    name="input-10-2"
                    label="Visible"
                    hint="At least 8 characters"
                    value="wqfasds"
                    class="input-group--focused"
                    @click:append="show2 = !show2"
                ></v-text-field>
                </v-flex>

             <v-btn type="submit" :loading="loading">Change Password</v-btn>
        </form>
      </v-layout>
    </v-container>
  </v-form>
</template>

<script>
import axios from 'axios'


  export default {
    name : 'changePwd',
    data () {
      return {
          pwd: ''
      }
    },
    props: {
      source: String
    },
    computed: {
      user () {
        return this.$store.getters.user
      },
      error () {
        return this.$store.getters.error
      },
      loading () {
        return this.$store.getters.loading
      }
    },
    watch: {
      user (value) {
        if (value !== null && value !== undefined) {
          this.$router.push('/chat/0')
        }
      }
    },
    methods: {

      changePwd: function () {
        axios.post(this.$store.state.ip + `:8082/`, this.userInfo) 
          .then(response => { 
              
            
            }
          ).catch(e => {
            console.log(e)
            this.errors(e)
            location.href = './login'
          })
      }   
    }
}

</script>