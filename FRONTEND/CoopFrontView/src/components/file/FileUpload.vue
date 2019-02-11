<template>
  <file-upload :url='url' :thumb-url='thumbUrl' :headers="headers" @change="onFileChange"></file-upload>
</template>

<script>
import Vue from 'vue'
import FileUpload from 'v-file-upload'
Vue.use(FileUpload)

export default {
  data () {
    return {
      url: 'http://your-post.url',
      headers: {'access-token': '<your-token>'},
      filesUploaded: []
    }
  },
  methods: {
    thumbUrl (file) {
      return file.myThumbUrlProperty
    },
    onFileChange (file) {
      // Handle files like:
      this.fileUploaded = file
    }
  }
}
</script>