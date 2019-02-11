import { FileUploadService } from 'v-file-upload'
export default {
  data() {
    return {
      url: 'http://your-post.url',
      headers: { 'access-token': '<your-token>' }
    }
  },
  methods: {
    mySaveMethod(file) {
      let fileUpload = new FileUploadService(
        this.url,
        this.headers,
        this.onProgress
      )
      fileUpload
        .upload(file, { doc_id: 1 })
        .then(e => {
          // Handle success
        })
        .catch(e => {
          // Handle error
        })
    },
    onProgress(event) {
      // Handdle the progress
    }
  }
}