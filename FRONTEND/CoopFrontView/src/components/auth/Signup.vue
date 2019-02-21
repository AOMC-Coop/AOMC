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
                    <v-btn 
                    color="secondary"
                    type="submit" 
                    :loading="loading"
                    :disabled="loading"
                    @click="loader = 'loading'">
                    <!-- https://vuetifyjs.com/en/components/buttons#api 의 예제를 긁어 왔으나, 버튼 로딩 애니메이션이 안 됨 -->
                    Sign Up </v-btn>
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
import { locale } from 'moment';
  export default {
    data () {
      return {
        userInfo: {
          uid: '',
          pwd: '',
          confirm_pwd: '',
          nickname: '',
          gender:'',
          invite_token:''
        },
        radios: 'radio-1',
        loader: null
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
      },
      loader () {
        const l = this.loader
        this[l] = !this[l]

        setTimeout(() => (this[l] = false), 3000)

        this.loader = null
      }
    },
    created(){
      //가람님 이게 초대 토큰입니다
      console.log(localStorage.getItem('invite_token'))  

      // const info = axios.defaults.headers.common
      // console.log(info)

      // const url = (new URL(document.location))
      // console.log("url = "+url)

      // const token2 = (new URL(document.location)).searchParams.get('token')
      // console.log("token2 = "+token2)

      // const token3 = axios.defaults.headers.common['token']
      // console.log("token3"+token3)

      // axios.interceptors.response.use(function (response) {
      //  // Do something with response data
      //  debugger
      //  console.log("response.status = " + response.status)
       
      //   return response;
      // }, function (error) {
      // // Do something with response error
      // console.log("error.status = "+ error.status)
      // return Promise.reject(error);
      // });

    },
    methods: {
      onSignup () {
        this.$store.dispatch('signUserUp', {email: this.email, password: this.password, nickname: this.nickname})
      },
      onDismissed () {
        this.$store.dispatch('clearError')
      },
      signup: function () {
        this.userInfo.invite_token = localStorage.getItem('invite_token')
        
        if(this.userInfo.pwd !== this.userInfo.confirm_pwd ){
          alert('Passwords do not match! Are you insane?')
        } else {
          /*this.$store.state.ip*/
          axios.post(`http://localhost:8082/api/members`, 
          this.userInfo
          )
            .then(response => { 
              let description = response.data.description
              if(description == "Fail Register : Already registered e-mail address"){
                alert("Already signed up e-mail address! please use another e-mail!")
              } else {
                alert("Authorization mail has been sent to your e-mail account! Please Check your email!")
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

<style>
  .custom-loader {
    animation: loader 1s infinite;
    display: flex;
  }
  @-moz-keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }
  @-webkit-keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }
  @-o-keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }
  @keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }
</style>