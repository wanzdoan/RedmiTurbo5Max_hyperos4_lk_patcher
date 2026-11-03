package com.android.phone.beidou;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import com.ot.pubsub.util.v;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BdUpdateEphJobService extends JobService {
    public static final String TAG = BdUpdateEphJobService.class.getSimpleName();
    private static final long UPDATE_BD_EPH_INTERVAL = 1209600000;
    private static final long UPDATE_BD_EPH_INTERVAL_WAIT_WLAN = v.b;

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        if (jobParameters == null) {
            return false;
        }
        int jobId = jobParameters.getJobId();
        String str = TAG;
        Log.d(str, "onStartJob id = " + jobId);
        BdUpdateManager.getInstance().updateEphData();
        Log.d(str, "onStartJob id = " + jobParameters.getJobId() + ", jobFinished");
        return false;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "OnStopJob id = " + jobParameters.getJobId());
        return false;
    }

    public static void initJobScheduler(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        if (jobScheduler == null) {
            Log.d(TAG, "initJobScheduler jobScheduler == null");
            return;
        }
        List<JobInfo> allPendingJobs = jobScheduler.getAllPendingJobs();
        if (allPendingJobs != null && allPendingJobs.size() > 0) {
            for (JobInfo jobInfo : allPendingJobs) {
                if (jobInfo != null && jobInfo.getId() == 74667) {
                    Log.d(TAG, "initJobScheduler(): job exist, return");
                    return;
                }
            }
        }
        JobInfo jobInfoBuild = new JobInfo.Builder(74667, new ComponentName(context, (Class<?>) BdUpdateEphJobService.class)).setPeriodic(UPDATE_BD_EPH_INTERVAL).setPersisted(true).build();
        int iSchedule = jobScheduler.schedule(jobInfoBuild);
        Log.d(TAG, "initJobScheduler jobInfo = " + jobInfoBuild.getId() + " result = " + iSchedule);
    }

    public static void initWaitWlanJobScheduler(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        if (jobScheduler == null) {
            Log.d(TAG, "initWaitWlanJobScheduler jobScheduler == null");
            return;
        }
        List<JobInfo> allPendingJobs = jobScheduler.getAllPendingJobs();
        if (allPendingJobs != null && allPendingJobs.size() > 0) {
            for (JobInfo jobInfo : allPendingJobs) {
                if (jobInfo != null && jobInfo.getId() == 74669) {
                    Log.d(TAG, "initWaitWlanJobScheduler(): job exist, return");
                    return;
                }
            }
        }
        JobInfo jobInfoBuild = new JobInfo.Builder(74669, new ComponentName(context, (Class<?>) BdUpdateEphJobService.class)).setPeriodic(UPDATE_BD_EPH_INTERVAL_WAIT_WLAN).setPersisted(true).setRequiredNetworkType(2).build();
        int iSchedule = jobScheduler.schedule(jobInfoBuild);
        Log.d(TAG, "initWaitWlanJobScheduler jobInfo = " + jobInfoBuild.getId() + " result = " + iSchedule);
    }

    public static void cancelSchedule(Context context) {
        Log.d(TAG, "cancelSchedule");
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        jobScheduler.cancel(74667);
        jobScheduler.cancel(74669);
    }
}
