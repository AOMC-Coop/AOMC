<template>
    <div  >
      <v-card >
        <v-card-title
          class="grey lighten-4 py-4 title"
        >
          Create Channel
        </v-card-title>
        <v-container grid-list-sm class="pa-4">
          <v-layout row wrap>
            <v-flex xs12 align-center justify-space-between>
              <v-layout align-center>
                <!-- <v-avatar size="42px" class="mr-3">
                  <img
                    src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png"
                    alt=""
                  >
                </v-avatar> -->
                <v-text-field
                  prepend-icon="notes"
                  placeholder="ChannelName"
                ></v-text-field>
              </v-layout>
            </v-flex>
            
            <!-- <v-flex xs12 align-center justify-space-between>
            <v-layout align-center>
                <v-avatar size="42px" class="mr-3">
                  <img
                    src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png"
                    alt=""
                  >
                </v-avatar>
                <v-text-field
                  placeholder="User"
                ></v-text-field>
                <v-icon>add</v-icon>
              </v-layout>
            </v-flex> -->

              <v-list-tile
              v-for="(child, i) in teamMembers"
              :key="i"
              
            >
            <v-flex xs12 align-center justify-space-between>
            <v-layout align-center>
              <v-list-tile-action >
                <v-avatar size="42px" class="mr-3">
                  <img
                    src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png"
                    alt=""
                  >
                </v-avatar>
              </v-list-tile-action>
              <v-list-tile-content>
                <v-list-tile-title justify-space-between>
                  {{ child.nickname }}
                </v-list-tile-title>
              </v-list-tile-content>
              <v-list-tile-action>
              <v-icon right fab @click="">add</v-icon>
            </v-list-tile-action>
            
            </v-layout>
            </v-flex>
            </v-list-tile>
            

            <!-- <v-flex xs12>
              <v-text-field
                type="tel"
                prepend-icon="phone"
                placeholder="(000) 000 - 0000"
                mask="phone"
              ></v-text-field>
            </v-flex> -->
            
            <!-- <v-flex xs12>
              <v-text-field
                prepend-icon="notes"
                placeholder="Notes"
              ></v-text-field>
            </v-flex> -->
          </v-layout>
          
        </v-container>
        <v-card-actions>
          <v-btn flat color="primary">More</v-btn>
          <v-spacer></v-spacer>
          <v-btn flat color="primary" @click="del_data()">Cancel</v-btn>
          <v-btn flat @click="save_data()">Save</v-btn>
        </v-card-actions>
      </v-card>
    </div>
</template>
<script>
export default {
  name: 'CreateChannel',
  data:function(){
      return {
          del_password:''
      } 
  },props : [
      // 'hot_table',
      'teamMembers'
  ],methods : {
      del_data(){
          
          this.$emit('close')
      },
      save_data() {

        this.$emit('close')
      },
      printLog(log) {
        console.log(log);
      }
  },
  created() {
    debugger;
    this.printLog(this.teamMembers)
  }
}
</script>
