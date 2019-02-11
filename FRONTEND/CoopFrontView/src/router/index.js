import Vue from 'vue'
import Router from 'vue-router'
import Signup from '@/components/auth/Signup'
import Signin from '@/components/auth/Signin'
import ChatHome from '@/components/chat/ChatHome'
import Vuetify from 'vuetify'
import '../assets/stylus/main.styl'
import colors from 'vuetify/es5/util/colors'
import Profile from '@/components/auth/Profile'
import Pwd from '@/components/auth/ChangePwd'

Vue.use(Vuetify, {
  theme: {
    primary: '#3F0E40', //
    secondary: colors.red.lighten4, // #FFCDD2
    accent: colors.indigo.base // #3F51B5
  }
})

export default new Router({
  mode: 'history',
  routes: [
    // {path: '/chat/:teamIdx', name: 'ChatHome', component: ChatHome, props: true },
    {
      path: '/',
      name: 'Signin',
      component: Signin
    },
    {
      path: '/chat',
      name: 'ChatHome',
      component: ChatHome
    },
    {
      path: '/profile',
      name: 'Profile',
      component: Profile
    },
    {
      path: '/signup',
      name: 'Signup',
      component: Signup
    },
    { 
      path: '/signup/:token',
      redirect: to => {
        const {params} = to
        if (params.token) {
          localStorage.setItem('invite_token', params.token)
          return '/signup'
        }
      }
    },
    {
      path: '/pwd',
      name: 'Pwd',
      component: Pwd
    },
    {
      path: '/members/:authUrl',
      redirect: to => {
        const {params} = to
        if (params.authUrl) {
          return '/signin'
        }
      }
    } 
  ]
})

Vue.use(Router)
