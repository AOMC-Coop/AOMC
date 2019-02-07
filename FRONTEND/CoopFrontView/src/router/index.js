import Vue from 'vue'
import Router from 'vue-router'
import Signup from '@/components/auth/Signup'
import Signin from '@/components/auth/Signin'
import ChatHome from '@/components/chat/ChatHome'
import Vuetify from 'vuetify'
import '../assets/stylus/main.styl'
import colors from 'vuetify/es5/util/colors'

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
    {path: '/', name: 'Signin', component: Signin},
    {path: '/chat/:teamIdx', name: 'ChatHome', component: ChatHome, props: true }
    // {
    //   path: '/chat',
    //   name: 'ChatHome',
    //   component: ChatHome
    // },
  ],
  // scrollBehavior (to, from, savedPosition) {
  //   return { x: 100, y: 100 }
  // }
})

Vue.use(Router)
