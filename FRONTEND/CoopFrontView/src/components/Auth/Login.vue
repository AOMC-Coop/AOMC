<template>
  <v-app id="inspire">
    <v-content>
      <v-container fluid fill-height>
        <v-layout align-center justify-center>
          <v-flex xs12 sm8 md4>
            <v-card class="elevation-12">
              <v-toolbar dark color="primary">
                <v-toolbar-title>Login AOMC Chatting Service</v-toolbar-title>
                <v-spacer></v-spacer>
              </v-toolbar>
              <v-card-text>
                <v-form>
                  <v-text-field prepend-icon="person" name="login" label="Login" type="text" v-model="user.uid"></v-text-field>
                  <v-text-field id="password" prepend-icon="lock" name="password" label="Password" type="password" v-model="user.pwd">></v-text-field>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" type="submit">Login</v-btn>
              </v-card-actions>

            </v-card>
          </v-flex>
          <v-card-actions>
                <p>* Not our member yet?</p>
                <v-btn color="primary">Sign up</v-btn>
          </v-card-actions>
        </v-layout>
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
import axios from 'axios'
const baseURI = localStorage.getItem('baseURI')

  export default {
    name: 'Login',
    data () {
      return {
        user: {
          uid: '',
          pwd: ''
        },
      }
    },
    props: {
      source: String
    },
    methods: {
    login: function () {
      axios.post(`${baseURI}/api/login`, this.user)
        .then(response => {
          // debugger
          this.info = response.data.data
          localStorage.setItem('token', this.info.data.token)
          console.log(this.info.data.token)
          location.href = './api'
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

<!-- -->

<template>
  <div>
    <form @submit.prevent="login">
      <input placeholder="Enter your ID" v-model="user.user_id"><br><br>
      <input placeholder="Enter your password" v-model="user.user_pwd"><br><br>
      <button type="submit">로그인</button>
    </form>
  </div>
</template>
