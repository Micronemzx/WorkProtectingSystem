package com.bookcode.worksprotectingsystem.controller;

import com.bookcode.worksprotectingsystem.entity.Works;
import com.bookcode.worksprotectingsystem.service.UserService;
import com.bookcode.worksprotectingsystem.service.WorkService;
import com.bookcode.worksprotectingsystem.utils.Result;
import com.sun.istack.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/work")
public class WorkController {
    @Resource
    private WorkService workservice;

    @Resource
    private UserService userService;

    @GetMapping("/test")
    public String testOutput(@RequestParam String a) {
        return "Hello World!I am "+a;
    }

    public boolean checkcookie(Cookie[] cookies) {
        if(cookies==null){
            return false;
        }
        boolean isLogin = false;
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("login_token")){//有储存token的cookie
                isLogin = userService.isVaildToken(cookies[i].getValue());
                if (isLogin) return true;
                break;
            }
        }
        return false;
    }

    @PostMapping(path = "/add",consumes = {"multipart/form-data"})
    public Result addWork(@RequestPart("workinfoform") Works work, @RequestPart("file") @NotNull MultipartFile file, HttpServletRequest request){
        //workservice.addWork(work);
        Cookie[] cookies = request.getCookies();
        if (!checkcookie(cookies)) return Result.error("401","NeedLogin");

        if (work.getWorkname() == null) return Result.error("403","file name is empty.");
        Result res = workservice.uploadFile(file, work);
        //if (res.getCode() != 200)
        return res;
    }

    @DeleteMapping(path = "/delete")
    public Result removeWork(@RequestParam("id") long id,HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (!checkcookie(cookies)) return Result.error("401","NeedLogin");
        if (workservice.removeWorkById(id)) {
            return Result.success(null,"successful");
        }
        else {
            return Result.error("404","failed");
        }
    }

    @PostMapping(path = "/update")
    public Result updateWork(@RequestBody Works work,HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (!checkcookie(cookies)) return Result.error("401","NeedLogin");
        if (workservice.updateWork(work)) return Result.success(null,"successful");
        else return Result.error("400","failed");
    }

    @GetMapping(path = "/info/{id}")
    @ResponseBody
    public Result getWorkInformation(@RequestParam("id") Long id,HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (!checkcookie(cookies)) return Result.success("401","NeedLogin");

        return Result.success(workservice.getWorkInformationById(id),"successful");
    }

    @PostMapping(path = "/addWaterMark")
    public Result addWaterMark(@RequestParam("workid") int workid,HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (!checkcookie(cookies)) return Result.error("401","NeedLogin");
        if (workservice.addWaterMark(workid)) return Result.success(null,"successful");
        else return Result.error("403","failed");
    }

    @PostMapping(path = "/addFingerPrint")
    public Result addFingerPrint(@RequestParam("workid") int workid,HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (!checkcookie(cookies)) return Result.error("401","NeedLogin");
        if (workservice.addFingerPrint(workid)) return Result.success(null,"successful");
        else return Result.error("403","failed");
    }

    @PostMapping(path = "/downloadFile")
    public Result downloadFile(@RequestBody Works works, HttpServletResponse resp,HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (!checkcookie(cookies)) return Result.error("401","NeedLogin");

        String res = workservice.downWorkFile(works,resp);
        if (res == "successful") {
            return Result.success(null,"successful");
        }
        else {
            return Result.error("400",res);
        }
    }

    @PostMapping(path = "/search")
    public Result searchWorks(@RequestBody String name,HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (!checkcookie(cookies)) return Result.error("401","NeedLogin");

        if (Objects.equals(name, "")) return Result.error("400","failed");
        else return Result.success(workservice.searchWorksByName(name),"successful");
    }

    @PostMapping(path = "/worklist")
    public Result<List<Works>> getWorkList(@RequestParam("workid") long[] workid,@RequestParam("ownerid") int ownerid,HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (!checkcookie(cookies)) return Result.error("401","NeedLogin");

        List<Works> res = null;
        for (long id : workid) {
            Works getwork = workservice.getWorkInformationById(id);
            if (getwork != null && getwork.getOwnerid() == ownerid) {
                res.add(getwork);
            }
        }
        return Result.success(res,"successful");
    }

    @PostMapping(path = "/setinterval")
    public Result setInterval(@RequestParam("workid") long workid,@RequestParam("intervaltime") int intervaltime,HttpServletRequest request){
        //对用户权限验证
        Cookie[] cookies = request.getCookies();
        if (!checkcookie(cookies)) return Result.error("401","NeedLogin");
        if (workservice.setinterval(workid,intervaltime)) return Result.success("successful");
        else return Result.error("403","failed");
    }
}
