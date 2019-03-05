package company.petrifaction.boss.service;

import android.os.IBinder;
import android.content.Intent;
import android.support.annotation.Nullable;

public class Service extends android.app.Service
{
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}