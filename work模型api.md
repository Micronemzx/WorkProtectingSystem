# 我的API文档

## 接口
作品：主要实现的增添作品（增添作品，作品上传），作品信息修改，作品删除（从数据库中删除作品信息，未实现文件从本地删除），作品查询（按作品名字查询），作品下载，以及添加水印和添加指纹的基本框架（未实现调用外部接口程序）

## 作品
### 作品模型
work{//作品信息
    workid	    integer($int64) //作品id
    workname    string          //作品名称
    fingerprintstatus bool      //指纹状态（是否添加）
    fingerprintdate string      //指纹添加日期
    fingerprintvalue string      //指纹数据
    watermarkstatus   bool      //水印状态
    watermarkdate string        //水印添加日期
    watermarkvalue string       //水印数据
    ownername       string      //作者名字
    ownerid         int         //作者id
    uploadtime      string      //作品上传时间
    lastchecktime   string      //最后一次审查时间
    intervaltime    long        //审查周期
    keywords        string[]    //关键词
    workabstract    string      //摘要
    workfile        string      //下载地址
    urllist         string[]    //侵权作品地址
    worktype        string      //作品类型
    platform    string          //审查平台

    //以下属性尚未添加
    photoUrls*	[                     //封面
    //xml: OrderedMap { "wrapped": true }
    string
    xml: OrderedMap { "name": "photoUrl" }//看不懂
    xml:
        name: photoUrl]
//     tags	[
//     xml: OrderedMap { "wrapped": true }
//     Tag{
//         id	integer($int64)
//         name	string
// }]
    status	string  //作品阶段

    //pet status in the store
        Enum:
        [ uploading, checking, passed ]//上传状态
    count   Count{
        censord         integer($int32)//审查次数
        affendingnum    integer($int32)//侵权作品数
    }       
    affendingwork{
    id //侵权作品id
    time //侵权作品发现时间
    matchingdegree //侵权作品匹配度
    }


},


### 作品接口(work类型)
- (POST)    /work/add  上传新的作品
- (POST)    /work/update   更新已有的作品
- (DELETE)     /work/delete  删除对应id的作品
- (POST)    /work/search    查找对应名称的作品及作品报表
- (GET)      /work/info/{id}     根据作品id获取作品详细信息
- (POST)     /work/downloadFile  下载作品
- (POST)    /work/addWaterMark  添加水印
- (POST)    /work/addFingerPrint  添加指纹
- (POST)    /work/setinterval  设置审查周期
- (POST)    /work/worklist  获取作品列表

- ps：作品报表要一个数据结构
- 
#### (POST)    /work/add  上传新的作品
- 请求地址：http://localhost:8080/work/add
- 请求参数：
workinfoForm: {
    workname    string          #作品名称
    ownername       string      #作者名字
    ownerid         int         #作者id
    uploadtime      string      #作品上传时间
    intervaltime    long        #审查周期
    keywords        string[]    #关键词
    workabstract    string      #摘要
    worktype        string      #作品类型
    platform    string          #审查平台
},
- 响应参数：
	- 上传成功：
    meta:{
    code:200, #状态码
	msg:"successful",
	}
	- 上传失败，文件不合法：
	meta:{
    code:403, #状态码
	msg:"upload failed,"+{报错信息},
	}
	- 上传失败，创建文件出错：
	meta:{
    code:500, #状态码
	msg:"upload failed "+ {报错信息},
	}


#### (POST)     /work/updte   更新已有的作品
- 请求地址：http://localhost:8080/work/update
- 请求参数：
workinfoForm: {    
    workid	    integer($int64) #作品id
    workname    string          #作品名称
    ownername       string      #作者名字
    ownerid         int         #作者id
    uploadtime      string      #作品上传时间
    intervaltime    long        #审查周期
    keywords        string[]    #关键词
    workabstract    string      #摘要
    worktype        string      #作品类型
    platform    string          #审查平台
},
- 响应参数：{
  - 更新成功：
    meta:{
    code:200,
    msg:"successful"
    }
  - 更新失败：
    meta:{
    code:400,
    msg:"failed"
    }
    }

#### (DELETE)  /work/delete  删除对应id的作品
- 请求地址：http://localhost:8080/work/delete
- 请求参数：
workinfoForm: {
        workid          int         #作品id
},
- 返回参数：{
	- 删除成功
    meta:{
    code:200,
    msg:"successful",
    }
	- 找不到作品，删除失败：
	  meta:{
	  code:404,
	  msg:"successful",
	  }
	  }

#### (POST)     /work/search    查找对应名称的作品及作品报表
- 请求地址：http://localhost:8080/work/search
- 请求参数：
workinfoForm: {
    ownerid     int     #用户id
    workid	    int     #作品id
    workname    string          #作品名称
},
- 返回参数：{
	- 查询成功：
    meta:{
    code:200,
    msg:"successful",
    data：{
        List<workinfo>{               #多个作品信息
        workid	    integer($int64) #作品id
        workname    string          #作品名称
        fingerprintstatus bool      #指纹状态（是否添加）
        fingerprintdate string      #指纹添加日期
        fingerprintvalue string     #指纹数据
        watermarkstatus   bool      #水印状态
        watermarkdate string        #水印添加日期
        watermarkvalue string       #水印数据
        ownername       string      #作者名字
        ownerid         int         #作者id
        uploadtime      string      #作品上传时间
        lastchecktime   string      #最后一次审查时间
        intervaltime    long        #审查周期
        keywords        string[]    #关键词
        workabstract    string      #摘要
        urllist         string[]    #侵权作品地址
        worktype        string      #作品类型
        platform    string          #审查平台
    },
    }
    }
    - 查询失败：
    meta:{
    code:400,
    msg:"failed",
    data:null
    }
}

#### (GET)      /work/info/{id}     根据作品id获取作品详细信息
- 请求地址：http://localhost:8080/work/info/{id}
- 请求参数：
workinfoForm: {
        workid      int                 #作品id
},
- 返回参数：{
    meta:{
    code:200,
    msg:"successful",
    data：{
        workinfoForm: {
        workid	    integer($int64) #作品id
        workname    string          #作品名称
        fingerprintstatus bool      #指纹状态（是否添加）
        fingerprintdate string      #指纹添加日期
        fingerprintvalue string      #指纹数据
        watermarkstatus   bool      #水印状态
        watermarkdate string        #水印添加日期
        watermarkvalue string       #水印数据
        ownername       string      #作者名字
        ownerid         int         #作者id
        uploadtime      string      #作品上传时间
        lastchecktime   string      #最后一次审查时间
        intervaltime    long        #审查周期
        keywords        string[]    #关键词
        workabstract    string      #摘要
        workfile        string      #下载地址
        urllist         string[]    #侵权作品地址
        worktype        string      #作品类型
        platform    string          #审查平台
        },
    }
    }
}

#### (POST)     /work/downloadFile  下载作品
- 请求地址：http://localhost:8080/work/downloadFile
- 请求参数：
workinfoForm: {   
    workid	        int         #作品id
    workname        string      #作品名称
    ownername       string      #作者名字
    ownerid         int         #作者id
    uploadtime      string      #作品上传时间
    intervaltime    long        #审查周期
    keywords        string[]    #关键词
    workabstract    string      #摘要
    workfile        string      #下载地址
    worktype        string      #作品类型
    platform    string          #审查平台
}
- 响应参数：{
	- 下载成功：
    meta:{
    code:200
    msg:"successful",
    }
    - 下载失败：
    - meta:{
    code:400
    msg:{报错信息}
    }
}

#### （POST)    /work/addFingerPrint    添加指纹
- 请求地址：http://localhost:8080/work/addFingerPrint
- 请求参数：
workinfoForm: {
    workid	    int     #作品id
},
- 返回参数：{
    meta:{
    msg:"successful",
    code:200 #状态码
    }
}


####  (POST)    /work/addWaterMark      添加水印
- 请求地址：http://localhost:8080/work/addWaterMark
- 请求参数：
workinfoForm: {
        workid      int     #作品id
},
- 返回参数：{
    meta:{
    msg:"successful",
    code:200 #状态码
    }
}

- (POST) 	/work/worklist	获取作品列表信息
- 请求地址：http://localhost:8080/work/worklist
- 请求参数:{
	workid	int[]		#作品id
	ownerid	int 		#用户id
}
- 响应参数：{
	meta:{
	code:200,
	msg:"successful",
	data:{
	List<workinfo>{
		workid	    integer($int64) #作品id
        workname    string          #作品名称
        fingerprintstatus bool      #指纹状态（是否添加）
        fingerprintdate string      #指纹添加日期
        fingerprintvalue string      #指纹数据
        watermarkstatus   bool      #水印状态
        watermarkdate string        #水印添加日期
        watermarkvalue string       #水印数据
        ownername       string      #作者名字
        ownerid         int         #作者id
        uploadtime      string      #作品上传时间
        lastchecktime   string      #最后一次审查时间
        intervaltime    long        #审查周期
        keywords        string[]    #关键词
        workabstract    string      #摘要
        workfile        string      #下载地址
        urllist         string[]    #侵权作品地址
        worktype        string      #作品类型
        platform    string          #审查平台
	}
	}
	}
}

- (POST)	/work/setinterval	设置审查周期
- 请求地址：http://localhost:8080/work/setinterval
- 请求参数:{
	workid	int 	#作品id
	intervaltime	int		#审查周期
}
- 响应参数：{
	- 用户拥有权限
	meta:{
	code:200,
	msg:"successful"
	}
	- 用户缺少权限
	meta:{
	code:400,
	msg:"failed",
	}
}
