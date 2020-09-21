package com.pavilion.service.impl;

import com.pavilion.hclib.HCNetSDK;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 实现类
 */
public class MemberFlowUPloadCallBackImpl implements HCNetSDK.FMSGCallBack_V31  {
    private static final Logger log = LoggerFactory.getLogger(MemberFlowUPloadCallBackImpl.class);
    @Override
    public boolean invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        System.out.println("进入回调了");
            switch (lCommand){
                case HCNetSDK.COMM_ALARM_PDC://客流量统计报警信息
                    try {
                        String sAlarmType = new String();
                        //报警时间
                        Date today = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String[] sIP = new String[2];

                        sAlarmType = new String("lCommand=") + lCommand;
                        //lCommand是传的报警类型
                        HCNetSDK.NET_DVR_PDC_ALRAM_INFO strPDCResult = new HCNetSDK.NET_DVR_PDC_ALRAM_INFO();
                        strPDCResult.write();
                        Pointer pPDCInfo = strPDCResult.getPointer();
                        pPDCInfo.write(0, pAlarmInfo.getByteArray(0, strPDCResult.size()), 0, strPDCResult.size());
                        strPDCResult.read();

                        if (strPDCResult.byMode == 0) {
                            strPDCResult.uStatModeParam.setType(HCNetSDK.NET_DVR_STATFRAME.class);
                            sAlarmType = sAlarmType + "：客流量统计，进入人数：" + strPDCResult.dwEnterNum + "，离开人数：" + strPDCResult.dwLeaveNum +
                                    ", byMode:" + strPDCResult.byMode + ", dwRelativeTime:" + strPDCResult.uStatModeParam.struStatFrame.dwRelativeTime +
                                    ", dwAbsTime:" + strPDCResult.uStatModeParam.struStatFrame.dwAbsTime;
                        }
                        if (strPDCResult.byMode == 1) {
                            strPDCResult.uStatModeParam.setType(HCNetSDK.NET_DVR_STATTIME.class);
                        }
                        System.out.println("sAlarmType---》" +sAlarmType);
                        //报警类型
                        //报警设备IP地址
                        sIP = new String(strPDCResult.struDevInfo.struDevIP.sIpV4).split("\0", 2);
                        return true;
                    } catch (Exception ex) {
                        return false;
                    }
                default:
                    log.info("其他报警，报警信息类型: ", lCommand);
                    return false;
            }

    }
}
