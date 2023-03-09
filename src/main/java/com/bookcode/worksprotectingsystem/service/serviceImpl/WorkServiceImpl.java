package com.bookcode.worksprotectingsystem.service.serviceImpl;


import com.bookcode.worksprotectingsystem.entity.User;
import com.bookcode.worksprotectingsystem.entity.Works;
import com.bookcode.worksprotectingsystem.service.WorkService;
import com.bookcode.worksprotectingsystem.utils.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor =  Exception.class)
public class WorkServiceImpl implements WorkService {
    @Resource
    private com.bookcode.worksprotectingsystem.Dao.WorkRepository WorkRepository;
    @Resource
    private com.bookcode.worksprotectingsystem.Dao.UserDao UserDao;
    @Value("${websiteURL}")
    String websiteURL;
    @Override
    public void addWork(Works work) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String StrDate = formatter.format(date);
        work.setUploadtime(StrDate);            //设置上传时间
        work.setWatermarkstatus(false);        //设置水印状态
        work.setFingerprintstatus(false);      //设置指纹状态
        work.setWatermarkdate("");
        work.setFingerprintdate("");
        work.setFingerprintvalue("");
        work.setWatermarkvalue("");
        WorkRepository.save(work);
    }
    @Override
    public boolean removeWorkById(long id) {//删除作品
        Works resu=WorkRepository.findByworkid(id);
        if (resu != null) {
            WorkRepository.deleteById(id);
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean updateWork(Works work) {                    //更新作品
        if (work.getWorkid() != null && work.getWorkname().length() > 0) {
            Works resu=WorkRepository.findByworkid(work.getWorkid());
            if (resu != null) {
                resu.update(work);
                WorkRepository.save(resu);
                return true;
            }
            else return false;
        }
        else return false;
    }

    @Override
    public Works getWorkInformationById(long id) {          //获取作品信息通过id
        Works workResult=new Works();
        workResult=null;
        if (id > 0) {
            workResult = WorkRepository.findByworkid(id);
        }
        return workResult;
    }
    @Override
    public List<Works> searchWorksByName(long ownerid,long workid,String Name) {
        List<Works> res=null;
        if (!Objects.equals(Name, "")) {
            res = WorkRepository.findByWorknameAndOwnerid(Name,ownerid);
        }
        return res;
    }

    @Override
    public boolean setinterval(long workid, int intervaltime) {
        Works work = WorkRepository.findByworkid(workid);
        User user = UserDao.findByUid(work.getOwnerid());
        if (user.getIsVip()==1) {
        work.setIntervaltime(intervaltime);
        WorkRepository.save(work);
        return true;
        }
        else return false;
    }
    @Override
    public boolean addWaterMark(int workid) {           //添加水印
        Works work = WorkRepository.findByworkid(workid);
        if (work.getWorkid() != null && work.getWorkname().length() > 0) {
            Works resu=WorkRepository.findByworkid(work.getWorkid());
            if (resu != null) {
                resu.setWatermarkstatus(true);
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String StrDate = formatter.format(date);
                resu.setWatermarkdate(StrDate);
                resu.setWatermarkvalue("");     //水印内容待添加
                //调用添加水印外部程序
                WorkRepository.save(resu);
                return true;
            }
            else return false;
        }
        else return false;
    }
    @Override
    public boolean addFingerPrint(int workid) {         //添加指纹
        Works work = WorkRepository.findByworkid(workid);
        if (work.getWorkid() != null && work.getWorkname().length() > 0) {
            Works resu=WorkRepository.findByworkid(work.getWorkid());
            if (resu != null) {
                resu.setFingerprintstatus(true);
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String StrDate = formatter.format(date);
                resu.setFingerprintdate(StrDate);
                resu.setFingerprintvalue("");   //指纹内容待添加
                //调用添加指纹外部程序
                WorkRepository.save(resu);
                return true;
            }
            else return false;
        }
        else return false;
    }
    @Override
    public Result uploadFile(MultipartFile file, Works work) {
        if (!file.isEmpty()) {
            String fileName=file.getOriginalFilename();
            //String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/";
            ApplicationHome home = new ApplicationHome(getClass());
            File jarfile = home.getSource();
            String path = jarfile.getParentFile().toString()+"static/";
            String sonPath = Integer.toString(Math.toIntExact(work.getOwnerid()));
            String localPath = path+sonPath+"/"+fileName;
            System.out.println(localPath);
            Works res = WorkRepository.findByworkfile(localPath);
            if (res != null) {
                return Result.error("403","upload failed,file name is repeated");
            }
            File dest = new File(localPath);
            if (!dest.getParentFile().exists()) {
                if (!dest.getParentFile().mkdirs()) return Result.error("500","upload failed,mkdirs failed");
            }
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
                return Result.error("500","upload failed " + e.getMessage());
            }
            addWork(work);
            //Works oldWork = WorkRepository.findByworkid(work.getWorkid());
            work.setWorkfile(localPath);
            WorkRepository.save(work);
            int size= (int) file.getSize();
            return Result.success(null,"successful");
        }
        else {
            return Result.error("403","upload failed,file is empty.");
        }
    }
    @Override
    public String downWorkFile(Works work,HttpServletResponse resp) {
        int workid = Math.toIntExact(work.getWorkid());
        work = WorkRepository.findByworkid(workid);
        if (work == null)
        {
            return "file is not exist";
        }
        String downloadUrl=work.getWorkfile();
        if (!downloadUrl.isEmpty()) {
            String filename=work.getWorkname();
            String pathName=work.getWorkfile();
            File file = new File(pathName);
            if (!file.exists()) {
                return "file is not exist";
            }
            resp.reset();
            resp.setContentType("application/octet-stream");
            resp.setCharacterEncoding("utf-8");
            resp.setContentLength((int)file.length());
            resp.setHeader("Content-Disposition","attachment;filename="+filename);
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os =resp.getOutputStream();
                bis = new BufferedInputStream(Files.newInputStream(file.toPath()));
                int i = bis.read(buff);
                while (i!=-1) {
                    os.write(buff,0,buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
            finally {
                if (bis != null) {
                    try {
                        bis.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "successful";
        }
        else {
            return "file is not exist";
        }
    }
}
