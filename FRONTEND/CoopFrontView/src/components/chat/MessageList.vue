<template>
  <!-- <v-list v-auto-bottom="msgs"> -->
<v-list>
    <transition-group name="list" >
      <!-- <div v-for="msg in msgs" v-bind:key="msg"> -->
        <v-list-tile>
          <v-list-tile-action>
            <!-- <span>{{msg.from.name}}</span> -->
            <span>이름</span>
          </v-list-tile-action>
          <v-list-tile-content>
            <!-- <v-list-tile-title>{{msg.msg}}</v-list-tile-title> -->
            <v-list-tile-title>메세지입니다</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-divider inset></v-divider>
      <!-- </div> -->
    </transition-group>
  </v-list>
</template>

<script>
export default {
  name: 'MessageList',
  props: ['msgs'],
};
</script>

<style>
.list-item {
  display: inline-block;
  margin-right: 10px;
}
.list-enter-active, .list-leave-active {
  transition: all 1s;
}
.list-enter, .list-leave-to /* .list-leave-active below version 2.1.8 */ {
  opacity: 0;
  transform: translateX(30px);
}
</style>