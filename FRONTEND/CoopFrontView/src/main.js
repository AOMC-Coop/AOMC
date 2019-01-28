// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import {store}  from './store/store.js'

// index.js or main.js
import 'vuetify/dist/vuetify.min.css' // Ensure you are using css-loader

import VModal from 'vue-js-modal'
import Directives from './plugins/directives.js'

import SmoothScrollbar from 'vue-smooth-scrollbar'
import VuePullInfiniteScroller from 'vue-pull-infinite-scroller'

Vue.use(VuePullInfiniteScroller)
Vue.use(SmoothScrollbar)


Vue.use(VModal, { dynamic: true })
Vue.use(Directives)

Vue.config.productionTip = false


import VueScrollFocus from 'vue-scroll-focus'
 
Vue.use(VueScrollFocus)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  store
})
// git push test to 'test3' branch