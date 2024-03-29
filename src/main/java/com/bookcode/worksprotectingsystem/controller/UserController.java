package com.bookcode.worksprotectingsystem.controller;

import com.bookcode.worksprotectingsystem.entity.*;
import com.bookcode.worksprotectingsystem.service.UserService;
import com.bookcode.worksprotectingsystem.service.WorkService;
import com.bookcode.worksprotectingsystem.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.lang.Math.min;


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private WorkService workservice;

    @PostMapping("/register")
    public Result<User> registController(@RequestBody User newUser){
        User user = userService.registService(newUser);
        if(user!=null){
            return Result.success("200","successful");
        }
        else{
            return Result.error("403","unsuccessful");
        }
    }//注册

    @PutMapping("/update")
    public Result<User> updateController(@RequestBody User newUser, HttpServletRequest request){
        Boolean isLogin=userService.checkToken(request);

        if(!isLogin){
            return Result.success("401","NeedLogin");
        }
        User user = userService.updateService(newUser);
        if(user!=null){
            return Result.success(user,"200","successful");
        }
        else {
            return Result.error("400","unsuccessful");
        }
    }//用户在此更新自己的数据

    @PostMapping("/checkLogin")
    public Result checkController(@RequestBody String uname,HttpServletRequest request){
        Boolean isLogin=userService.checkToken(request);

        if(!isLogin){
            return Result.error("401","NeedLogin");
        }
        int res = userService.ifLoginService(uname);
        if(res==1){
            return Result.success(1,"200","successful");
        }
        else {
            return Result.success(0,"200","successful");
        }
    }//检查是否登录

    @PostMapping("/login")
    public Result<User> loginController(@RequestBody UserForLogin checkUser, HttpServletResponse response){
        String uname=checkUser.getUname();
        String password=checkUser.getPassword();
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("400","unsuccessful");
        }
        else {
            int flag=userService.checkBlack(user);
            if(flag==0){
                User res=userService.loginService(uname,password);
                if(res==null){
                    return Result.error("403","wrong password");
                }
                else {
                    Cookie cookie = new Cookie("login_token", user.getToken());
                    cookie.setMaxAge(3 * 24 * 60 * 60);//有效期3天
                    cookie.setPath("/");//必须要设置
                    cookie.setDomain("");
                    cookie.setHttpOnly(false);
                    response.addCookie(cookie);
                    return Result.success(res, "200", "successful");
                }
            }
            else {
                return Result.error("403","is black");
            }
        }
    }//登录

    @GetMapping("/logout")
    public Result logoutController(@RequestParam String uname,HttpServletResponse response,HttpServletRequest request){
        Boolean isLogin=userService.checkToken(request);

        if(!isLogin){
            return Result.success("401","NeedLogin");
        }
        int flag=userService.ifLoginService(uname);
        if(flag==0){
            return Result.error("400","unsuccessful");
        }
        userService.logoutService(uname);
        Cookie cookie = new Cookie("login_token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return Result.success("200","successful");
    }//登出

    @DeleteMapping("/delete")
    public Result deleteController(@RequestParam String uname,HttpServletRequest request){
        Boolean isLogin=userService.checkToken(request);

        if(!isLogin){
            return Result.success("401","NeedLogin");
        }
        int res=userService.deleteService(uname);
        if(res==1){
            return Result.success("200","successful");
        }
        else {
            return Result.error("400","unsuccessful");
        }
    }//删除用户

    @GetMapping("/search")
    public Result searchController(@RequestParam String uname,HttpServletRequest request){
        Boolean isLogin=userService.checkToken(request);

        if(!isLogin){
            return Result.success("401","NeedLogin");
        }
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("400","unsuccessful");
        }
        return Result.success(user,"200","successful");
    }//搜索

    @GetMapping("/showWorkList")
    public Result<Works[]> showWorkList(@RequestParam String uname,HttpServletRequest request){
        Boolean isLogin=userService.checkToken(request);

        if(isLogin==false){
            return Result.success("401","NeedLogin");
        }
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("400","unsuccessful");
        }
        else {
            String res=userService.showWorkList(user);
            if(res==null){
                return Result.success(null,"200","successful");
            }
            int sz=res.length();
            Works ans[]=new Works[sz];
            int a[]=new int[sz];
            StringBuilder temp=new StringBuilder("");
            int cnt=0;//cnt个作品
            for(int i=0;i<sz;i++){
                char it=res.charAt(i);
                if(it==','){
                    String x=temp.toString();
                    ans[cnt++]=workservice.getWorkInformationById(Integer.valueOf(x));
                    temp=new StringBuilder("");
                }
                else {
                    temp.append(it);
                }
            }
            if(temp.length()>0){
                String x=temp.toString();
                ans[cnt++]=workservice.getWorkInformationById(Integer.valueOf(x));
                temp=new StringBuilder("");
            }
            return Result.success(ans,"200","successful");
        }
    }//用户作品列表

    @PostMapping("/addWork")
    public Result<String>addWork(@RequestBody WorkIDToAdd work, HttpServletRequest request){
        Boolean isLogin=userService.checkToken(request);

        if(isLogin==false){
            return Result.success("401","NeedLogin");
        }
        String uname=work.getUname();
        long id=work.getId();
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("400","unsuccessful");
        }
        else {
            userService.addWork(user.getUid(), (int) id);
            String res=userService.showWorkList(user);
            return Result.success(res,"200","successful");
        }
    }

    @PostMapping("/checkVip")
    public Result checkVipController(@RequestBody String uname,HttpServletRequest request){
        Boolean isLogin=userService.checkToken(request);
        if(isLogin==false){
            return Result.success("401","NeedLogin");
        }
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("400","unsuccessful");
        }
        else {
            int flag=userService.checkVip(user);
            if(flag==0){
                return  Result.success(0,"200","successful");
            }
            else return Result.success(1,"200","successful");
        }
    }//vip还需要判断endTime是否已经过了，不能简单看boolean

    @PostMapping("/recharge")
    public Result<String> rechargeController(@RequestBody Vip vip,HttpServletRequest request){
        Boolean isLogin=userService.checkToken(request);
        if(isLogin==false){
            return Result.success("401","NeedLogin");
        }
        String uname=vip.getUname();
        int days=vip.getDays();
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("400","unsuccessful");
        }
        else {
            userService.rechargeVip(user,days);
            return Result.success(user.getEndTime(),"200","successful");
        }
    }//充值

    @PostMapping("/setBlack")
    public Result setBlackController(@RequestBody String uname,HttpServletRequest request){
        Boolean isLogin=userService.checkToken(request);
        if(isLogin==false){
            return Result.success("401","NeedLogin");
        }
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("400","unsuccessful");
        }
        int flag=userService.checkBlack(user);
        if(flag==1){
            return Result.success(0,"200","successful");
        }
        else {
            //如果正处于登录状态将被强制logout
            int flag2=userService.ifLoginService(uname);
            if(flag2==1){
                userService.logoutService(uname);
            }
            userService.setBlack(user);
            return Result.success(1,"200","successful");
        }
    }//设置用户进入黑名单

    @PostMapping("/setWhite")
    public Result setWhiteController(@RequestBody String uname,HttpServletRequest request){
        Boolean isLogin=userService.checkToken(request);
        if(isLogin==false){
            return Result.success("401","NeedLogin");
        }
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("400","unsuccessful");
        }
        int flag=userService.checkBlack(user);
        if(flag==0){
            return Result.success(0,"200","successful");
        }
        else {
            userService.setWhite(user);
            return Result.success(1,"200","successful");
        }
    }//移除黑名单中的用户

    @GetMapping("/showPageWorkList")
    public Result<Works[]> showPageWorkList(@RequestParam String uname, @RequestParam int pagesize, @RequestParam int pagenum, HttpServletRequest request){
        Boolean isLogin=userService.checkToken(request);
        if(isLogin==false){
            return Result.success("401","NeedLogin");
        }
        User user=userService.ifExist(uname);
        if(user==null){
            return Result.error("400","unsuccessful");
        }
        else {
            String res=userService.showWorkList(user);
            if(res==null){
                return Result.success(null,"200","successful");
            }
            int sz=res.length();
            Works a[]=new Works[sz];
            Works ans[]=new Works[pagesize];
            StringBuilder temp=new StringBuilder("");
            int cnt=0;//cnt个作品
            for(int i=0;i<sz;i++){
                char it=res.charAt(i);
                if(it==','){
                    String x=temp.toString();
                    a[cnt++]=workservice.getWorkInformationById(Integer.valueOf(x));
                    temp=new StringBuilder("");
                }
                else {
                    temp.append(it);
                }
            }
            if(temp.length()>0){
                String x=temp.toString();
                a[cnt++]=workservice.getWorkInformationById(Integer.valueOf(x));
                temp=new StringBuilder("");
            }
            int id=(pagenum-1)*pagesize;
            int ed=pagenum*pagesize;
            for(int i=id;i<min(sz,ed);i++){
                ans[i-id]=a[i];
            }
            return Result.success(ans,"200","successful");
        }
    }//按照page返回用户作品列表













}
