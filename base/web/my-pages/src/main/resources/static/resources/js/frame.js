//框架内增删改查工具类

function Frame() {
}

Frame.prototype.delete=function(pkName,url,successcCallback){
    successcCallback=successcCallback||tableUtil.remove;
    var selects =tableUtil.getSelections();
    if(selects.length==0){
        dialog.alert("请选择要删除的数据");
        return;
    }
    var count=selects.length;
    dialog.confirm('确定要删除这'+count+"条数据吗?",function(){
        var data='';
        var ids=[];
        for(var i=0;i<selects.length;i++){
            data+=pkName+"="+selects[i][pkName]+"&";
            ids.push(selects[i][pkName]);
        }
        ajaxUtil.json(url,data,function(resp){
            if(!resp.success){
                dialog.alert(resp.message);
            }else{
                successcCallback(pkName,ids);
            }
        })
    });
}

Frame.prototype.update=function (pkName, title,prepareUrl,updateUrl) {
    var selects = tableUtil.getSelections();
    if(selects.length==0){
        dialog.alert("请选择要修改的数据");
        return;
    }
    if(selects.length!=1){
        dialog.alert("每次只能修改1条");
        return;
    }
     prepareUrl=prepareUrl+'?'+pkName+'='+selects[0][pkName];
    dialog.form('prepareUpdate',title,prepareUrl,function(data){
        var fm=$("#prepareUpdate")[0];
        if(!validate.valiForm(fm)){
            return false;
        }
        var data=$("#prepareUpdate").serialize();
        data+="&"+pkName+"="+selects[0][pkName];
        ajaxUtil.json(updateUrl,data,function(rest){
            if(rest.success){
                tableUtil.update(rest.data[pkName],rest.data);
            }else{
                dialog.alert(rest.message);
            }
        });
    });
}

Frame.prototype.prepareInsert=function (title, prepareUrl, insertUrl,successCallback,size) {
    successCallback=successCallback||tableUtil.insertRow;
    dialog.form('prepareInsert',title,prepareUrl,function(data){
        var fm=$("#prepareInsert")[0];
        if(!validate.valiForm(fm)){
            return false;
        }
        var data=$("#prepareInsert").serialize();
        ajaxUtil.json(insertUrl,data,function(rest){
            if(rest.success){
                successCallback(rest.data);
            }else{
                dialog.alert(rest.message);
            }
        });
    },size);
}

var frame=new Frame();