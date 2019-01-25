// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import './plugins/socketPlugin';
// index.js or main.js
import 'vuetify/dist/vuetify.min.css' // Ensure you are using css-loader

import VModal from 'vue-js-modal'
Vue.use(VModal, { dynamic: true })

import Directives from './plugins/directives';
Vue.use(Directives);

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
// git push test to 'test3' branch