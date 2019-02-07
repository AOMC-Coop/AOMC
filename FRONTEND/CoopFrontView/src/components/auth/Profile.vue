<template>
  <v-container
    fill-height
    fluid
    grid-list-xl>
    <v-layout
      justify-center
      wrap
    >
      <v-flex
        xs12
        md8
      >
        <material-card
          color="green"
          title="Edit Profile"
          text="Complete your profile"
        >
          <v-form>
            <v-container py-0>
              <v-layout wrap>
                <v-flex xs12 md4>
                  <!-- <v-text-field
                    label="Company (disabled)"
                    disabled/>
                </v-flex>
                <v-flex
                  xs12
                  md4
                > -->
                <form @submit.prevent="setProfile"> 
                    <v-text-field
                        id="nickname_bind"
                        class="purple-input"
                        label="User Name"
                        v-model="profileWithToken.nickname"
                    />
                    <v-btn type="submit" class="mx-0 font-weight-light" color="success">
                        Update Profile
                    </v-btn>
                </form>

<!-- ***** v-text-field안에 v-bind:value=nickname 씨벌탱 이렇게 해도 바인딩이 안된다. v-bind:value="nickname" 여기에 유저 nickname을 가져와서 표시해야 한다.-->
                <!-- </v-flex>
                <v-flex
                  xs12
                  md4
                >
                  <v-text-field
                    label="Email Address"
                    class="purple-input"/>
                </v-flex>
                <v-flex
                  xs12
                  md6
                > 
                   <v-text-field
                    label="First Name"
                    class="purple-input"/>
                </v-flex>
                <v-flex
                  xs12
                  md6
                > 
                  <v-text-field
                    label="Last Name"
                    class="purple-input"/>
                </v-flex>
                <v-flex
                  xs12
                  md12
                > 
                  <v-text-field
                    label="Adress"
                    class="purple-input"/>
                </v-flex>
                <v-flex
                  xs12
                  md4>
                  <v-text-field
                    label="City"
                    class="purple-input"/>
                </v-flex>
                <v-flex
                  xs12
                  md4>
                  <v-text-field
                    label="Country"
                    class="purple-input"/>
                </v-flex>
                <v-flex
                  xs12
                  md4>
                  <v-text-field
                    class="purple-input"
                    label="Postal Code"
                    type="number"/>
                </v-flex>
                <v-flex xs12>
                  <v-textarea
                    class="purple-input"
                    label="About Me"
                  />
                </v-flex>
                <v-flex
                  xs12
                  text-xs-right
                > -->

                </v-flex>
              </v-layout>
            </v-container>
          </v-form>
        </material-card>
      </v-flex>
      <v-flex
        xs12
        md4
      >
        <material-card class="v-card-profile">
          <v-avatar
            slot="offset"
            class="mx-auto d-block"
            size="130"
          >
            <img
              src="https://demos.creative-tim.com/vue-material-dashboard/img/marc.aba54d65.jpg"
            >
          </v-avatar>
          <v-card-text class="text-xs-center">
            <!-- <h6 class="category text-gray font-weight-thin mb-3">CEO / CO-FOUNDER</h6>
            <h4 class="card-title font-weight-light">Alec Thompson</h4>
            <p class="card-description font-weight-light"></p> -->
            <v-btn
              color="success"
              round
              class="font-weight-light"
            >Change Photo</v-btn>
          </v-card-text>
        </material-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import axios from "axios";
import Vue from 'vue'

let gender = localStorage.getItem('gender')

new Vue({
    el: '#nickname_bind',
    data: {
        nickname:localStorage.getItem('nickname') 
    }
});

export default {

  data: () => ({
    profileWithToken : {
      token: localStorage.getItem('token'),  
      idx : localStorage.getItem('idx'),
      //   uid :
      nickname : '',
// ***** 아직 gender는 문제가 많아 부득이하게 1로 고정하여 전송
      gender : 1
    }
   }),
  methods: {
    setProfile: function (){
      let idx = localStorage.getItem('idx')
      let url = `http://localhost:8082/profile/`+ idx
      axios.put(url, this.profileWithToken)
        .then(response => {
          let description = response.data.description
          if(description == "Fail Set Profile"){
              alert("Fail to update profile!")
           } else if (description == "Fail Set Profile : Wrong Idx"){
              alert("Wrong URL!")
           } else {
                alert("Successfully update profile!")
                location.href = './profile'
           }   
           }
         ).catch(e => {
          console.log(e)
          this.errors(e)
          location.href = './'
        })      
    }
  }
}
</script>