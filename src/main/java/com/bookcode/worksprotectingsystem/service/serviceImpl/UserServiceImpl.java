package com.bookcode.worksprotectingsystem.service.serviceImpl;

import com.bookcode.worksprotectingsystem.Dao.UserDao;
import com.bookcode.worksprotectingsystem.entity.User;
import com.bookcode.worksprotectingsystem.service.UserService;
import com.bookcode.worksprotectingsystem.utils.Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class UserServiceImpl implements UserService{

    @Resource
    private UserDao userDao;

    @Override
    public User nameFindService(String uname) {
        return userDao.findByUname(uname);
    }

    @Override
    public User loginService(String uname, String password) {
        User user = userDao.findByUnameAndPassword(uname,password);
        if(user!=null){
            String token= Util.getUUID();
            user.setToken(token);
            user.setLogin(1);
            userDao.save(user);
        }
        return user;
    }//登录

    @Override
    public boolean isVaildToken(String token) {
        User user=userDao.findByToken(token);
        if(user==null){
            return false;
        }
        else return true;
    }//验证token

    @Override
    public User registService(User user) {
        if(userDao.findByUname(user.getUname())!=null){
            return null;
        }
        else {
            User newUser = userDao.save(user);
            if(newUser!=null){
                newUser.setPassword("");
                newUser.setEmail("");
                newUser.setPhone("");
                newUser.setIsVip(0);
            }
            return newUser;
        }
    }//注册的函数

    @Override
    public User updateService(User newUser) {
        String uname=newUser.getUname();
        User oldUser=userDao.findByUname(uname);
        if(oldUser==null){
            return null;
        }
        oldUser.setPassword(newUser.getPassword());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setPhone(newUser.getPhone());
        userDao.save(oldUser);
        return userDao.findByUname(uname);
    }//用户进行修改的函数

    @Override
    public int deleteService(String uname) {
        User oldUser=userDao.findByUname(uname);
        if(oldUser==null){
            return 0;
        }
        userDao.delete(oldUser);
        return 1;
    }//删除用户

    @Override
    public int ifLoginService(String uname) {
        User user=userDao.findByUname(uname);
        if(user==null||user.getLogin()==0){
            return 0;
        }
        else{
            return 1;
        }
    }//检查是否登录状态

    @Override
    public void logoutService(String uname) {
        User user=userDao.findByUname(uname);
        if(user!=null&&user.getLogin()==1){
            user.setLogin(0);
            user.setToken(null);
            userDao.save(user);
        }
    }//登出

    @Override
    public int checkBlack(User user) {
        if(user.getBlacklist()==1){
            return 1;
        }
        else return 0;
    }//检查是否黑名单中

    @Override
    public void setBlack(User user) {
        user.setBlacklist(1);
        userDao.save(user);
    }//将用户设置成为黑

    @Override
    public void setWhite(User user) {
        user.setBlacklist(0);
        userDao.save(user);
    }//接触黑名单限制

    @Override
    public User ifExist(String uname) {
        User user=userDao.findByUname(uname);
        return user;
    }//检查是否存在该用户

    @Override
    public int checkVip(User user) {
        int flag=user.getIsVip();
        if(flag==0){
            return 0;
        }
        else {
            LocalDate nowTime=LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String endTimeStr=user.getEndTime();
            LocalDate endTime = LocalDate.parse(endTimeStr, formatter);
            if(nowTime.isAfter(endTime)){
                return 0;
            }
            return 1;
        }
    }

    @Override
    public void rechargeVip(User user, int days) {
        int flag=user.getIsVip();
        LocalDate nowTime=LocalDate.now();
        LocalDate endTime=nowTime.plus(days,ChronoUnit.DAYS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String endTimeStr = endTime.format(formatter);
        if(flag==0){
            user.setIsVip(1);
            user.setEndTime(endTimeStr);
        }
        else user.setEndTime(endTimeStr);
        userDao.save(user);
    }

    @Override
    public String showWorkList(User user) {
        return user.getWorklist();
    }

    @Override
    public void addWork(User user,int id) {
        String idStr=Integer.toString(id);
        String workList=user.getWorklist();
        StringBuilder temp;
        if(workList!=null) {
            temp = new StringBuilder(workList);
            temp.append(",");
            temp.append(idStr);
        }
        else {
            temp=new StringBuilder(idStr);
        }
        String res=temp.toString();
        user.setWorklist(res);
        userDao.save(user);
    }



    /*@Override
    public User updateService(String uname) {
        User oldUser=userDao.findByUname(uname);
        if(oldUser==null){
            return null;
        }
        else{
            if(oldUser!=null) {
                oldUser.setPassword("");
                oldUser.setEmail("");
                oldUser.setPhone("");
            }
            return oldUser;
        }
    }

     */



}
