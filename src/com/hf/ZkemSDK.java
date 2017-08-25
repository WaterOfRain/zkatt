package com.hf;

import java.util.ArrayList;  
import java.util.Calendar;  
import java.util.Date;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
  
import com.jacob.activeX.ActiveXComponent;  
import com.jacob.com.Dispatch;  
import com.jacob.com.Variant;  
  
/** 
 * �пؿ��ڻ�sdk���������� 
 * @author yuhang 
 * 
 */  
public class ZkemSDK {  
      
    private static ActiveXComponent zkem = new ActiveXComponent("zkemkeeper.ZKEM.1");  
      
    /** 
     * ���ӿ��ڻ� 
     * @param address ���ڻ���ַ 
     * @param port �˿ں� 
     * @return  
     */  
    public boolean connect(String address,int port){  
        boolean result = zkem.invoke("Connect_NET",address,port).getBoolean();  
        return result;  
    }  
    /** 
     * �Ͽ����ڻ����� 
     */  
    public void disConnect(){  
        zkem.invoke("Disconnect");  
    } 
    
    /** 
     * ��ȡ���ڼ�¼��pc���档���getGeneralLogDataʹ�� 
     * @return 
     */  
    public boolean readGeneralLogData(int iMachineNumber){  
        boolean result = zkem.invoke("ReadGeneralLogData",iMachineNumber).getBoolean();  
        return result;  
    }  
  
      
    /** 
     * ��ȡ�����еĿ������ݡ����readGeneralLogData / readLastestLogDataʹ�á� 
     * @return ���ص�map�У��������¼�ֵ�� 
        "EnrollNumber"   ��Ա��� 
        "Time"           ����ʱ�䴮����ʽ: yyyy-MM-dd HH:mm:ss 
        "VerifyMode" 
        "InOutMode" 
        "Year"          ����ʱ�䣺�� 
        "Month"         ����ʱ�䣺�� 
        "Day"           ����ʱ�䣺�� 
        "Hour"          ����ʱ�䣺ʱ 
        "Minute"        ����ʱ�䣺�� 
        "Second"        ����ʱ�䣺�� 
     */  
    public List<Map<String,Object>> getGeneralLogData(){  
        Variant v0 = new Variant(1);  
        Variant dwEnrollNumber = new Variant("",true);  
        Variant dwVerifyMode = new Variant(0,true);  
        Variant dwInOutMode = new Variant(0,true);  
        Variant dwYear = new Variant(0,true);  
        Variant dwMonth = new Variant(0,true);  
        Variant dwDay = new Variant(0,true);  
        Variant dwHour = new Variant(0,true);  
        Variant dwMinute = new Variant(0,true);  
        Variant dwSecond = new Variant(0,true);  
        Variant dwWorkCode = new Variant(0,true);  
        List<Map<String,Object>> strList = new ArrayList<Map<String,Object>>();  
        boolean newresult = false;  
        do{  
            Variant   vResult = Dispatch.call(zkem, "SSR_GetGeneralLogData", v0,dwEnrollNumber,dwVerifyMode,dwInOutMode,dwYear,dwMonth,dwDay,dwHour,  
                    dwMinute,dwSecond,dwWorkCode);    
            newresult = vResult.getBoolean();  
            if(newresult)  
            {  
                String enrollNumber = dwEnrollNumber.getStringRef();  
                  
                //���û�б�ţ���������  
                if(enrollNumber == null || enrollNumber.trim().length() == 0)  
                    continue;  
                Map<String,Object> m = new HashMap<String, Object>();  
                m.put("EnrollNumber", enrollNumber);  
                m.put("Time", dwYear.getIntRef() + "-" + dwMonth.getIntRef() + "-" + dwDay.getIntRef() + " " + dwHour.getIntRef() + ":" + dwMinute.getIntRef() + ":" + dwSecond.getIntRef());  
                m.put("VerifyMode", dwVerifyMode.getIntRef());  
                m.put("InOutMode", dwInOutMode.getIntRef());  
//                m.put("Year", dwYear.getIntRef());  
//                m.put("Month", dwMonth.getIntRef());  
//                m.put("Day", dwDay.getIntRef());  
//                m.put("Hour", dwHour.getIntRef());  
//                m.put("Minute", dwMinute.getIntRef());  
//                m.put("Second", dwSecond.getIntRef());  
                strList.add(m);  
            }  
        }while(newresult == true);  
        return strList;  
    }  
      
    /** 
     * ��ȡ�û���Ϣ 
     * @return ���ص�Map�У��������¼�ֵ: 
     *  "EnrollNumber"  ��Ա��� 
        "Name"          ��Ա���� 
        "Password"      ��Ա���� 
        "Privilege" 
        "Enabled"       �Ƿ����� 
     */  
    public List<Map<String,Object>> getUserInfo(){  
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();  
        //���û����ݶ��뻺���С�  
        boolean result = zkem.invoke("ReadAllUserID",0).getBoolean();  
          
        Variant v0 = new Variant(1);  
        Variant sdwEnrollNumber = new Variant("",true);  
        Variant sName = new Variant("",true);  
        Variant sPassword = new Variant("",true);  
        Variant iPrivilege = new Variant(0,true);  
        Variant bEnabled = new Variant(false,true);  
          
        while(result)  
        {     
            //�ӻ����ж�ȡһ�������û�����  
            result = zkem.invoke("SSR_GetAllUserInfo", v0,sdwEnrollNumber,sName,sPassword,iPrivilege,bEnabled).getBoolean();  
  
            //���û�б�ţ�������  
            String enrollNumber = sdwEnrollNumber.getStringRef();  
            if(enrollNumber == null || enrollNumber.trim().length() == 0)  
                continue;  
              
            //�������ֺ����������룬������������˽�ȡ�ַ����İ취�Ѻ��������ȥ���ˣ��Ժ��д�������õİ취��  
            //ֻ֧��2λ��3λ��4λ���ȵ��������֡�  
            String name = "";  
            if(sName.getStringRef().getBytes().length == 9 || sName.getStringRef().getBytes().length == 8)  
            {  
                name = sName.getStringRef().substring(0,3);  
            }else if(sName.getStringRef().getBytes().length == 7 || sName.getStringRef().getBytes().length == 6)  
            {  
                name = sName.getStringRef().substring(0,2);  
            }else if(sName.getStringRef().getBytes().length == 11 || sName.getStringRef().getBytes().length == 10)  
            {  
                name = sName.getStringRef().substring(0,4);  
            }  
              
            //���û�����֣�������  
            if(name.trim().length() == 0)  
                continue;  
              
            Map<String,Object> m = new HashMap<String, Object>();  
            m.put("EnrollNumber", enrollNumber);  
            m.put("Name", name);  
            m.put("Password", sPassword.getStringRef());  
            m.put("Privilege", iPrivilege.getIntRef());  
            m.put("Enabled", bEnabled.getBooleanRef());  
              
            resultList.add(m);  
        }  
        return resultList;  
    }  
      
      
    /** 
     * �����û���Ϣ 
     * @param number 
     * @param name 
     * @param password 
     * @param isPrivilege 
     * @param enabled 
     * @return 
     */  
    public boolean setUserInfo(String number,String name,String password, int isPrivilege,boolean enabled)  
    {  
        Variant v0 = new Variant(1);  
        Variant sdwEnrollNumber = new Variant(number,true);  
        Variant sName = new Variant(name,true);  
        Variant sPassword = new Variant(password,true);  
        Variant iPrivilege = new Variant(isPrivilege,true);  
        Variant bEnabled = new Variant(enabled,true);  
          
        boolean result = zkem.invoke("SSR_SetUserInfo",v0 ,sdwEnrollNumber,sName,sPassword,iPrivilege,bEnabled).getBoolean();  
        return result;  
    }  
      
    /** 
     * ��ȡ�û���Ϣ 
     * @param number ���ں��� 
      * @return 
     */  
    public Map<String,Object> getUserInfoByNumber(String number){  
         Variant v0 = new Variant(1);  
         Variant sdwEnrollNumber = new Variant(number,true);  
        Variant sName = new Variant("",true);  
        Variant sPassword = new Variant("",true);  
        Variant iPrivilege = new Variant(0,true);  
        Variant bEnabled = new Variant(false,true);  
        boolean result = zkem.invoke("SSR_GetUserInfo",v0 ,sdwEnrollNumber,sName,sPassword,iPrivilege,bEnabled).getBoolean();  
        if(result)  
        {  
            Map<String,Object> m = new HashMap<String, Object>();  
            m.put("EnrollNumber", number);  
            m.put("Name", sName.getStringRef());  
            m.put("Password", sPassword.getStringRef());  
            m.put("Privilege", iPrivilege.getIntRef());  
            m.put("Enabled", bEnabled.getBooleanRef());  
            return m;  
        }  
        return null;  
    }  
} 