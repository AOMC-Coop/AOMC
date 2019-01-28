<template>
  <div id="app">
    <!-- <img src="./assets/logo.png"> -->
<!-- <Login></Login> -->
<router-link to="/">login</router-link>
<router-link to="/chat">chat</router-link>
<router-view/>

<!-- <Chat></Chat> -->
  </div>
</template>

<script>
import Chat from './components/Chat.vue' 
import Login from './components/Login.vue' // + Login.vue 컴포넌트를 추가할 것

export default {
  name: 'App',
  components : {
    'Chat' : Chat,
    'Login' : Login // + Login 추가
  }
}
</script>

<style>
/* #app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
} */
</style>

<style lang="stylus">
  $color-pack = false
  @import '~vuetify/src/stylus/main'
</style>
