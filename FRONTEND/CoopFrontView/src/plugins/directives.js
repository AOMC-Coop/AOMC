// module.exports = (Vue) => {
//   // dom 업데이트시 스크롤을 최하단으로 이동시킵니다.
//   Vue.directive('auto-bottom', {
//     update: (el) => {
//       setTimeout(() => {
//         el.scrollBottom = el.scrollHeight;
//       }, 0);
//     },
//   });
// };

module.exports = function(Vue) {
  Vue.directive('auto-bottom', {
    update: function() {
      this.el.scrollTop = this.el.scrollHeight;
    }
  })
};