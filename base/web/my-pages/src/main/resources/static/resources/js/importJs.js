var widget=['bootstrap','bootstrap-table','bootstrap-treeview','bootstrap-datetimepicker','bootstrap-select'];
//jqery放在开头
document.write('<script src="/resources/js/jquery/1.10.1/jquery.min.js"></script>')
//bootstrap-datetimepicker需要moment
document.write('<script src="/resources/js/bootstrap-datetimepicker/js/moment-with-locales.min.js"></script>')

for(var i=0;i<widget.length;i++){
    var css='<link href="/resources/js/'+widget[i]+'/css/'+widget[i]+'.min.css" rel="stylesheet">';
    var js='<script src="/resources/js/'+widget[i]+'/js/'+widget[i]+'.min.js"></script>';
    var i18n='<script src="/resources/js/'+widget[i]+'/js/'+widget[i]+'-zh_CN.min.js"></script>';
    document.write(css);
    document.write(js);
    document.write(i18n);
}

document.write('<script src="/resources/js/utils.js"></script>');

document.write('<script src="/resources/js/vue/vue.min.js"></script>');
document.write('<script src="/resources/js/vue/vee-validate.min.js"></script>');
document.write('<script src="/resources/js/vue/vue-i18n.min.js"></script>');
document.write('<script src="/resources/js/vue/vueUtil.js"></script>');

document.write('<script src="/resources/js/ajaxUtil.js"></script>');
document.write('<script src="/resources/js/myTree.js"></script>');
document.write('<script src="/resources/js/dialog.js"></script>');
document.write('<script src="/resources/js/frame.js"></script>');
document.write('<script src="/resources/js/tableUtil.js"></script>');
document.write('<script src="/resources/js/validate.js"></script>');
//首页样式放在最后
document.write('<link href="/resources/css/index.css" rel="stylesheet">');