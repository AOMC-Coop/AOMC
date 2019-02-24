<template>
 <v-container fluid grid-list-md>
    <v-layout row wrap>
      <v-flex d-flex xs12 sm6 md6>
        <v-flex xs12>
        <v-card dark color="primary">
          <v-card-text>< 프로필 변경 > </v-card-text>
        </v-card>

        <material-card class="v-card-profile">
          <v-avatar
            slot="offset"
            class="mx-auto d-block"
            size="200"
          >
          <img :src="ProfileUrl">
          </v-avatar>
          <v-card-text class="text-xs-center">
            
            <!-- <v-btn
              color="primary"
              round
              class="font-weight-light"
            >Change Photo</v-btn> -->

        <upload-btn icon title='Change Photo' large=false :fileChangedCallback="fileChanged">
          <template slot="icon">
            <v-icon class="white--text">add</v-icon>
          </template>
        </upload-btn>

        <v-btn round icon large
            :loading="loading3"
            :disabled="loading3"
            color="secondary"
            class="white--text"
            @click="loader = 'loading3'"
            v-on:click="submitFile()">
            <v-icon right dark left>cloud_upload</v-icon>
        </v-btn>
            
            <!-- <div class="container">
              <div class="large-12 medium-12 small-12 cell">
                <label>File
                  <input type="file" id="file" ref="file" v-on:change="handleFileUpload()"/>
                </label>
                <button v-on:click="submitFile()">Submit</button>
              </div>
            </div> -->
            
          </v-card-text>
        </material-card>
      </v-flex>
      </v-flex>
      
      <v-flex d-flex xs12 sm6 md6>
          <v-flex d-flex>
            <v-layout row wrap >
              <v-flex  xs class="center">
                
                <v-card dark color="primary">
                    <v-card-text>< 닉네임 변경 > </v-card-text>
                </v-card>
                

                <!-- <form @submit.prevent="setProfile">  -->
                    <v-text-field
                        id="nickname_bind"
                        class="purple-input"
                        label="User Name"
                        v-model="profileWithToken.nickname"
                    />
                    <v-btn type="submit" @click="setProfile" class="mx-0 font-weight-light" color="primary">
                        Update Profile
                    </v-btn>
                <!-- </form> -->
              </v-flex>


              <v-flex  xs12 class="center">
                <form @submit.prevent="changePwd">
                  <v-btn type="submit" color="primary">비밀번호 변경</v-btn>
                </form>              
              </v-flex>


              <v-flex d-flex xs12 class="center">
                <form @submit.prevent="withdrawal">
                  <v-btn type="submit" color="primary" >회원탈퇴</v-btn>
                </form>                
              </v-flex>

            </v-layout>
          </v-flex>

          
          
      </v-flex>


      <v-flex xs12>
          <v-btn @click="goChatHome" color="primary" >홈으로</v-btn>
      </v-flex>

    </v-layout>
  </v-container>  
</template>

<script>
import axios from "axios";
import Vue from 'vue'
import { locale } from 'moment';
import { loadavg } from 'os';
import UploadButton from 'vuetify-upload-button';

// let gender = localStorage.getItem('gender')
let ProfileUrl = ''
new Vue({
    el: '#nickname_bind',
    data: {
        nickname:localStorage.getItem('nickname') 
    }
});

export default {
  created(){
    this.ProfileUrl = localStorage.getItem('userImage')
  },
  components: {
      'upload-btn': UploadButton
  },
  data: () => ({
    profileWithToken : {
      token: localStorage.getItem('token'),  
      idx : localStorage.getItem('idx'),
      //   uid :
      nickname : '',
// ***** 아직 gender는 문제가 많아 부득이하게 1로 고정하여 전송
      // gender : 1
    },
    userWithToken : {
      idx : localStorage.getItem('idx'),
      token: localStorage.getItem('token')
    },
    file: '',
    userIdx: localStorage.getItem("userIdx"),
    userNickName: localStorage.getItem("userNickName"),
    userImage:localStorage.getItem("userImage"),
    loader: null,
    loading: false,
    loading2: false,
    loading3: false,
    loading4: false,

   }),

  watch: {
    loader () {
      const l = this.loader
      this[l] = !this[l]

      setTimeout(() => (this[l] = false), 3000)

      this.loader = null
    }
  },

  methods: {
    goChatHome(){
      this.$router.push({path:'./chat'})
    },
    setProfile: function (){
      let idx = localStorage.getItem('idx')
      let url = this.$store.state.ip + `:8082/api/profile/`+ idx
      let token = localStorage.getItem('token')
      axios.put(url, 
      this.profileWithToken,
      { headers: { 'token': `${token}` }} 
      )
        .then(response => {
          let description = response.data.description
          if(description == "Fail Set Profile"){
              alert("Fail to update profile!")
           } else if (description == "Fail Set Profile : Wrong Idx"){
              alert("Wrong URL!")
           } else {
                localStorage.setItem('userNickname', this.profileWithToken.nickname)
                this.$store.state.userNickName=this.profileWithToken.nickname
                this.profileWithToken.nickname=''
                alert("Successfully update profile!")
                // location.href = './profile'
           }   
           }
         ).catch(e => {
          console.log(e)
          this.errors(e)
          location.href = './'
        })      
    },
    submitFile(){
      // Initialize the form data
      let formData = new FormData();
      // let checkFile = this.file

      let user_idx = localStorage.getItem('userIdx')
      let url = this.$store.state.ip + ":8085/api/files/upload/profile/" + user_idx

      formData.append('file', this.file);
      console.log(formData)

      axios.post( url,
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then(response => {
        debugger
        //console.log(response.data.data)
          if(response.data.status==200) {
              ProfileUrl = response.data.data
              localStorage.setItem("userImage", ProfileUrl);
              console.log('Successfully submit profile photo!');
            } else {
            this.errors.push(e);
            }
        })
        .catch(function(){
          console.log('Fail to submit profile photo!');
        });

      },
      handleFileUpload(){
        this.file = this.$refs.file.files[0];
        debugger
      },
      changePwd : function (){
      this.$router.push({path: '/pwd'})
      },
      withdrawal: function (){
        alert("Are you sure?")
        let token = localStorage.getItem('token')
        let idx = localStorage.getItem('idx')
        let url = this.$store.state.ip + `:8082/api/members/`+ idx
        axios.put(url, this.userWithToken)
          .then(response => {
            let description = response.data.descfription
            if(description == "Fail Withdrawal"){
                alert("Fail to withdraw!")
            } else {
                localStorage.removeItem('token')
                localStorage.removeItem('idx')
                console.log(JSON.stringify(localStorage))
                alert("Successfully withdrew!")
                location.href = './'
            }
          }).catch(e => {
            console.log(e)
            this.errors(e)
            location.href = './'
          })      
      },
      fileChanged (file) {
        debugger
        this.file = file
      }

  }
}
</script>
<style>
.center {
    position: relative;
  }
.center form {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
</style>