package com.android.phone;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.utils.MiuiThreadUtil;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.util.v;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyJobService extends JobService {
    private static ComponentName sTelephonyJobService = new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, TelephonyJobService.class.getName());
    private TelephonyJobHandler mJobHandler;

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mJobHandler = new TelephonyJobHandler(this, MiuiThreadUtil.getWorkThreadLooper());
        Log.d("TelephonyJobService", "onCreate");
    }

    public class TelephonyJobHandler extends Handler {
        public TelephonyJobHandler(TelephonyJobService telephonyJobService, Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws Throwable {
            MiuiTelephonyStatistic miuiTelephonyStatistic;
            if (message.what == 1 && (miuiTelephonyStatistic = MiuiTelephonyStatistic.getInstance()) != null) {
                miuiTelephonyStatistic.reportTelephonyStatisticData();
            }
        }
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        if (jobParameters == null) {
            return false;
        }
        int jobId = jobParameters.getJobId();
        Log.d("TelephonyJobService", "onStartJob id = " + jobId);
        if (jobId == 74664) {
            TelephonyJobHandler telephonyJobHandler = this.mJobHandler;
            telephonyJobHandler.sendMessage(telephonyJobHandler.obtainMessage(1));
        }
        return false;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("TelephonyJobService", "OnStopJob id = " + jobParameters.getJobId());
        return false;
    }

    public static void initReportJobSchedule(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        if (jobScheduler == null) {
            Log.d("TelephonyJobService", "initReportJobSchedule jobScheduler == null");
            return;
        }
        List<JobInfo> allPendingJobs = jobScheduler.getAllPendingJobs();
        if (allPendingJobs != null && allPendingJobs.size() > 0) {
            for (JobInfo jobInfo : allPendingJobs) {
                if (jobInfo != null && jobInfo.getId() == 74664) {
                    Log.d("TelephonyJobService", "initReportJobSchedule(): job exist, return");
                    return;
                }
            }
        }
        JobInfo jobInfoBuild = new JobInfo.Builder(74664, new ComponentName(context, (Class<?>) TelephonyJobService.class)).setPeriodic(v.b).setBackoffCriteria(30000L, 1).setPersisted(true).build();
        Log.d("TelephonyJobService", "initReportJobSchedules jobInfo = " + jobInfoBuild.getId() + " result=" + jobScheduler.schedule(jobInfoBuild));
    }

    public static void cancelReportJobSchedule(Context context) {
        Log.d("TelephonyJobService", "cancelReportJobSchedule()");
        ((JobScheduler) context.getSystemService("jobscheduler")).cancel(74664);
    }
}
