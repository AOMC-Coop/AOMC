<template>
  <div id="app">
    <!-- <img src="./assets/logo.png"> -->
    <!-- <router-view/> -->
<Chat></Chat>
  </div>
</template>

<script>
import Chat from './components/Chat.vue'

export default {
  name: 'App',
  components : {
    'Chat' : Chat
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

</style>

<style lang="stylus">
  $color-pack = false

  @import '~vuetify/src/stylus/main'
</style>
