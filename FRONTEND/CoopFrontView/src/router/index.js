import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/auth/Login'
import Signup from '@/components/auth/Signup'
import Signin from '@/components/auth/Signin'
import Chat from '@/components/Chat'
import Vuetify from 'vuetify'

// src/index.js

import '../assets/stylus/main.styl'

// Helpers
import colors from 'vuetify/es5/util/colors'
// import validator from './validator'

// Vue.use(validator)

Vue.use(Vuetify, {
  theme: {
    primary: '#3F0E40', //
    secondary: colors.red.lighten4, // #FFCDD2
    accent: colors.indigo.base // #3F51B5
  }
})

export default new Router({ // 모르면 공부
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Signin',
      component: Signin
    },
    {
      path: '/chat',
      name: 'Chat',
      component: Chat
    }
  ],
  scrollBehavior (to, from, savedPosition) {
    return { x: 100, y: 100 }
  }
})

Vue.use(Router)
