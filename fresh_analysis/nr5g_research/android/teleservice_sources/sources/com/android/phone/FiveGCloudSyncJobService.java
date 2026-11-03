package com.android.phone;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.ot.pubsub.util.v;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class FiveGCloudSyncJobService extends JobService {
    private FiveGCloudSyncAsyncTask mFiveGCloudSync;

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        if (jobParameters == null) {
            return false;
        }
        if (this.mFiveGCloudSync != null) {
            Log.d("FiveGCloudSyncJobService", "onStartJob cancel mFiveGCloudSync");
            this.mFiveGCloudSync.cancel(false);
        }
        FiveGCloudSyncAsyncTask fiveGCloudSyncAsyncTask = new FiveGCloudSyncAsyncTask();
        this.mFiveGCloudSync = fiveGCloudSyncAsyncTask;
        fiveGCloudSyncAsyncTask.execute(jobParameters);
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("FiveGCloudSyncJobService", "OnStopJob id = " + jobParameters.getJobId());
        return false;
    }

    public static void initCloudSyncJobScheduler(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        if (jobScheduler == null) {
            Log.d("FiveGCloudSyncJobService", "initCloudSyncJobScheduler jobScheduler == null");
            return;
        }
        List<JobInfo> allPendingJobs = jobScheduler.getAllPendingJobs();
        if (allPendingJobs != null && allPendingJobs.size() > 0) {
            for (JobInfo jobInfo : allPendingJobs) {
                if (jobInfo != null && jobInfo.getId() == 78888) {
                    Log.d("FiveGCloudSyncJobService", "initCloudSyncJobScheduler(): job exist, return");
                    return;
                }
            }
        }
        JobInfo jobInfoBuild = new JobInfo.Builder(78888, new ComponentName(context, (Class<?>) FiveGCloudSyncJobService.class)).setPeriodic(v.c).setPersisted(true).build();
        Log.d("FiveGCloudSyncJobService", "initCloudSyncJobScheduler jobInfo = " + jobInfoBuild.getId() + " result=" + jobScheduler.schedule(jobInfoBuild));
    }

    class FiveGCloudSyncAsyncTask extends AsyncTask {
        private FiveGCloudSyncAsyncTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public JobParameters[] doInBackground(JobParameters... jobParametersArr) {
            Log.d("FiveGCloudSyncJobService", "FiveGCloudSyncAsyncTask doInBackground");
            try {
                if (FiveGCloudController.getInstance() == null) {
                    return jobParametersArr;
                }
                FiveGCloudController.getInstance().getCurrentLocation();
                FiveGCloudController.getInstance().fetchAll5GCloudStatus(true);
                return jobParametersArr;
            } catch (Exception e) {
                Log.d("FiveGCloudSyncJobService", "FiveGCloudSyncAsyncTask fetchAll5GCloudStatus exception =" + e);
                return jobParametersArr;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(JobParameters[] jobParametersArr) {
            Log.d("FiveGCloudSyncJobService", "FiveGCloudSyncAsyncTask onPostExecute results=" + jobParametersArr);
            if (jobParametersArr == null || jobParametersArr.length == 0) {
                return;
            }
            for (JobParameters jobParameters : jobParametersArr) {
                FiveGCloudSyncJobService.this.jobFinished(jobParameters, false);
            }
        }
    }
}
