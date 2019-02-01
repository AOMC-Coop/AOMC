<template>
  <div id="app">
    <!-- <router-link to="/">login</router-link> -->
    <!-- <router-link to="/chat">chat</router-link> -->
    <router-view/>

    <!-- <Signup></Signup> -->
    
  </div>



</template>


<script>
<<<<<<< HEAD
import Chat from './components/Chat.vue'
import Login from './components/auth/Login.vue'
import Signup from './components/auth/Signup.vue'
import Signin from './components/auth/Signin.vue'

// Login.vue : 로그인
// Chat.vue 위에 : 로그아웃  
// Register.vue : 회원가입 email인증 (Login.vue위에)
// Mypage.vue : 마이페이지 회원수정 회원탈퇴 비밀번호 찾기 (Chat.vue 위에)

=======
import Chat from './components/chat/ChatHome.vue' 
import Login from './components/Auth/Login.vue' // + Login.vue 컴포넌트를 추가할 것
>>>>>>> 5930582238c9abc8167e8f6498c289904bf0eec0

export default {
  name: 'App',
  components : {
    'Chat' : Chat,
    'Login' : Login,
    'Signup' : Signup,
    'Signin' : Signin
  }
}
</script>

<<style>
 #app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;

  /* margin-top: 60px; */
}

</style>

<style lang="stylus">
  $color-pack = false
  @import '~vuetify/src/stylus/main'
</style>
