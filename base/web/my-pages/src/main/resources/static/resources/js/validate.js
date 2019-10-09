
//判断字符串是否是日期
function ValidateUtil(){

}
//判断字符串是否是日期
ValidateUtil.prototype.isDate=function(txt,error){
	if(!txt){
		return true;
	}
	var message=error||"日期格式应为:YYYY-MM-DD";
	var txts=txt.split("-");
	if(txts.length!=3){
		return message;
	}
	try{

		var days=[31,29,31,30,31,30,31,31,30,31,30,31];
		if(txts[0].length>4){
			return message;
		}
		if(txts[1].length>2){
			return message;
		}
		if(txts[2].length>2){
			return message;
		}
		var year=eval(txts[0]);
		var month=eval(txts[1])
		var day=eval(txts[2])
		if(year>9999||year<0){
			return "年份不正确";
		}
		if(month>12||month<1){
			return "月份不正确";
		}
		if(day<1){
			return "日期不正确";
		}
		if(day>days[month-1]){
			return month+"月不能超过"+days[month-1]+"天";
		}

		if(month==2&&day==29){
			if(year%4==0&&year%100!=0){
				return true;
			}
			if(year%400==0){
				return true;
			}
			return "2月份不能大于28";
		}

		return true;
	}catch(e){
		console.log(e);
		return message;
	}
}

ValidateUtil.prototype.isDatetime=function(txt){
	if(!txt){
		return true;
	}
	var message="时间格式应为:YYYY-MM-DD hh:mm:ss";
	var txts=txt.split(" ");
	if(txts.length!=2){
		return message;
	}
	var mesg=common.isDate(txts[0],message);
	if(mesg!=true){
		return mesg;
	}
	try{
		var times=txts[1].split(":");
		var max=[24,60,60];
		var name=["小时","分钟","秒"];
		for(var i=0;i<times.length;i++){
			if(times[i].length>2){
				return message
			}
			var t=eval(times[i]);
			if(t>max[i]){
				return name[i]+"不能大于"+max[i];
			}
			if(i>1){
				if(times[0]=='24'){
					if(t>0){
						return "时间不能大于24点";
					}
				}
			}
		}

		return true;
	}catch(e){
		console.log(e);
		return message;
	}
}

//文本框内允许输入多个用哪逗号隔开的标签
ValidateUtil.prototype.multiInput=function(txt){
	var txt=txt.split(",");
	for(var i=0;i<txt.length;i++){
		var success=(txt[i]==txt[i].replace(/[^0-9a-zA-Z\u4e00-\u9fa5_|]/g,''));
		if(!success){
			return "只能包含字符数字或汉字,多个用逗号隔开";
		}
	}
	return true;
}

var validateUtil=new ValidateUtil();
//input字段设置validate属性,validate.valiFrom的时候就会split这个validate,然后调用regx验证,如果regx离是正则就用正则匹配,否则用方法
var validates=[
    {name:"common",regx:/[^0-9a-zA-Z\u4e00-\u9fa5_|]/g,msg:"包含非法字符"},//没写的时候默认值
	{name:"text",regx:/[^0-9a-zA-Z\u4e00-\u9fa5]/g,msg:"只能是数字字母或汉字"},
	{name:"number",regx:/[^0-9]/g,msg:"只能是数字"},
	{name:"ip",regx:/[^0-9.]/g,msg:"不是有效的ip"},
	{name:"letter",regx:/[^a-zA-Z]/g,msg:"只能是字母"},
	{name:"remark",regx:/[^0-9a-zA-Z\u4e00-\u9fa5_，。！（）]/g,msg:"格式不正确"},//备注字段允许填的
	{name:"code",regx:/[^a-zA-Z0-9_]/g,msg:"只能是数字字母或下划线"},
	{name:"url",regx:/[^a-zA-Z0-9_:/.&\\?=]/g,msg:"不是有效的地址"},
	{name:"email",regx:/[^a-zA-Z0-9_@/.]/g,msg:"不是有效的邮箱"},
	{name:"date",regx:validateUtil.isDate,msg:"不是有效的日期"},
	{name:"datetime",regx:validateUtil.isDatetime,msg:"不是有效的时间"},
	{name:"multiInput",regx:validateUtil.multiInput,msg:"只能输入字母或数字,多个用逗号隔开"}
]


function Validate() {
}

Validate.prototype.valiForm=function(form) {
	var els=form.elements;
	for(var i=0;i<els.length;i++){
		//如果元素没设置name,也不严重
		if(!els[i].name){
			continue;
		}
		var vali=$(els[i]).attr("validate");
		if(!vali){
			continue;
		}
		if(!this.valiElement(els[i],vali)){
			return false;
		}
	}
	return true;
}

Validate.prototype.valiElement=function(el,vali) {
	var value=el.value;
	if(!value){
		if(vali=="notNull"){
			dialog.alert(this.getName(el)+"不能为空");
			return false;
		}
		if(vali.indexOf("notNull,")==0){
			dialog.alert(this.getName(el)+"不能为空");
			return false;
		}
	}
	//将notNull前缀去掉
	if(vali.indexOf("notNull,")==0){
		vali=vali.substring("notNull,".length);
	}
	
	//去掉notNull前缀
	if(vali=="notNull"){
		vali="";
	}
	if(!vali||vali=="none"){
		return true;
	}
	for(var i=0;i<validates.length;i++){
		if(vali==validates[i].name){
			if(typeof(validates[i].regx)=='function'){
				var msg=validates[i].regx(value);
				if(msg!=true){
					dialog.alert(this.getName(el)+msg);
					return false;
				}
				return true;
			}
			var success=(value==value.replace(validates[i].regx,''));			
			if(!success){
				dialog.alert(this.getName(el)+validates[i].msg);
				return false;
			}
			return true;
		}
	}
	if(value!=value.replace(eval("/[^"+vali+"]/g"),'')){
		dialog.alert(this.getName(el)+"格式不正确");
		return false;
	}
	
	return true;
	
	
}

Validate.prototype.getName=function(el) {
	var tit=$(el).attr("title");
	if(tit){
		return tit;
	}
	var text=$(el).parent().prev().text();
	if(!text){
		text=$(el).parent().text();
	}
	return text;
}

var validate = new Validate();



