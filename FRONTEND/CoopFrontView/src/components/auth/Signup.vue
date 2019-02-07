<template>
  <v-container>
    <v-layout row v-if="error">
      <v-flex xs12 sm6 offset-sm3>
        <app-alert @dismissed="onDismissed" :text="error.message"></app-alert>
      </v-flex>
    </v-layout>
    <v-layout row>
      <v-flex xs12 sm6 offset-sm3>
        <img src="https://github.com/AOMC-Coop/AOMC/blob/master/image/coop_logo2.png?raw=true" width="70" height="70">
        <br><br>
        <v-card>
          <v-btn flat>Sign Up</v-btn>
          <v-card-text>
            <v-container>
              <form @submit.prevent="signup">
                <v-layout row>
                  <v-flex xs12>
                    <v-text-field
                      name="email"
                      label="E-mail"
                      id="email"
                      v-model="userInfo.uid"
                      type="email"
                      required></v-text-field>
                  </v-flex>
                </v-layout>
                <v-layout row>
                  <v-flex xs12>
                    <v-text-field
                      name="nickname"
                      label="Nickname"
                      id="nickname"
                      v-model="userInfo.nickname"
                      type="text"
                      required></v-text-field>
                  </v-flex>
                </v-layout>
                <v-layout row>
                  <v-container fluid>
                    <!-- radio 체크 박스를 data()로 전달하는 방법 : 이것만 알면 Gender도 전달 가능 -->
                    <p align="left" style="font-size:120%; color:grey;">Gender</p>
                    <v-radio-group v-model="radios" :mandatory="true">
                      <v-radio label="Male" value="Male"></v-radio>
                      <v-radio label="Female" value="Female" ></v-radio>
                      <v-radio label="Prefer Not To Say" value="PreferNotTosay" ></v-radio>
                    </v-radio-group>
                  </v-container>
                </v-layout>                
                <v-layout row>
                  <v-flex xs12>
                    <v-text-field
                      name="password"
                      label="Password"
                      id="password"
                      v-model="userInfo.pwd"
                      type="password"
                      required></v-text-field>
                  </v-flex>
                </v-layout>
                <v-layout row>
                  <v-flex xs12>
                    <v-text-field
                      name="confirmPassword"
                      label="Validate Password"
                      id="confirmPassword"
                      v-model="userInfo.confirm_pwd"
                      type="password"
                      required
                      :rules="[comparePasswords]"></v-text-field>
                  </v-flex>
                </v-layout>
                <v-layout>
                  <v-flex xs12>
                    <v-btn type="submit" :loading="loading">Sign Up</v-btn>
                  </v-flex>
                </v-layout>
              </form>
            </v-container>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import axios from 'axios'
  export default {
    data () {
      return {
        userInfo: {
          uid: '',
          pwd: '',
          confirm_pwd: '',
          nickname: '',
          gender:''
        },
        radios: 'radio-1'
      }
    },
    computed: {
      comparePasswords () {
        return this.userInfo.pwd !== this.userInfo.confirm_pwd ? 'Passwords do not match.' : true
      },
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
          this.$router.push('/')
        }
      }
    },
    methods: {
      onSignup () {
        this.$store.dispatch('signUserUp', {email: this.email, password: this.password, nickname: this.nickname})
      },
      onDismissed () {
        this.$store.dispatch('clearError')
      },
      signup: function () {
        if(this.userInfo.pwd !== this.userInfo.confirm_pwd ){
          alert('Passwords do not match! Are you insane?')
        } else {
          axios.post(`http://localhost:8082/members`, this.userInfo)
            .then(response => { 
              let description = response.data.description
              if(description == "Fail Register : Already registered e-mail address"){
                alert("Already signed up e-mail address! please use another e-mail!")
              } else {
                alert("Successfully signed up!")
                location.href = './'
              }
            }
            ).catch(e => {
              console.log(e)
              this.errors(e)
              location.href = './signup'
            })
        }

      } 
    }
  }
</script>