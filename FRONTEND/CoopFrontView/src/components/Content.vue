<template>
<v-content>
      <v-container fluid fill-height>
        <v-layout justify-center align-end>

          <v-flex xs12>
            <v-text-field
              label="Outline"
              placeholder="대화를 입력하세요."
              outline
            ></v-text-field>
          </v-flex>

          <!-- <v-tooltip right>
            <v-btn
              slot="activator"
              :href="source"
              icon
              large
              target="_blank"
            >
              <v-icon large>code</v-icon>
            </v-btn>
            <span>Source</span>
          </v-tooltip> -->
          <!-- <v-tooltip right>
            <v-btn slot="activator" icon large href="https://codepen.io/johnjleider/pen/EQOYVV" target="_blank">
              <v-icon large>mdi-codepen</v-icon>
            </v-btn>
            <span>Codepen</span>
          </v-tooltip> -->

          
        </v-layout>

        
      </v-container>
    </v-content>

    
  
</template>

<script>
export default {
  name: 'Content',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  }
}
</script>


