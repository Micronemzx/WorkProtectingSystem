package com.bookcode.worksprotectingsystem.service;

import com.bookcode.worksprotectingsystem.entity.Works;
import com.bookcode.worksprotectingsystem.utils.Result;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface WorkService {
    void addWork(Works work);           //新增作品
    boolean removeWorkById(long id);    //通过id删除作品
    boolean updateWork(Works work);        //修改文件信息
    //Works getWorkInformationByName(String Name);    //获取
    Works getWorkInformationById(long id);  //通过id获取作品详细信息
    List<Works> searchWorksByName(long ownerid,long workid,String Name);
    boolean setinterval(long workid, int intervaltime);

    boolean addWaterMark(int workid);
    boolean addFingerPrint(int workid);
    String downWorkFile(Works work, HttpServletResponse resp);
    Result uploadFile(@RequestParam("file")MultipartFile file, Works work);
}
