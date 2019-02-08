module.exports = (Vue) => {  
  Vue.directive('auto-bottom', {
    update: function (el, newValue) {      
      if(newValue.value.length>0){
        if(localStorage.getItem('scrollControlValue')==newValue.value[0].message_idx){
          setTimeout(() => {
            el.scrollTop = el.scrollHeight;
          }, 0);
        }else{ 
          localStorage.setItem('scrollControlValue', newValue.value[0].message_idx)
        }
      }  
    },    
  });  
};
