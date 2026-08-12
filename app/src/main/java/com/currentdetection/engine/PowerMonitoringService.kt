package com.currentdetection.engine

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.currentdetection.data.local.AppDatabase
import com.currentdetection.data.repository.NetworkRepositoryImpl
import com.currentdetection.wifi.WifiScannerImpl

class PowerMonitoringService : Service() {

    private lateinit var monitoringManager: PowerMonitoringManager
    private lateinit var notificationManager: AppNotificationManager

    override fun onCreate() {
        super.onCreate()
        
        notificationManager = AppNotificationManager(this)
        
        val database = AppDatabase.getDatabase(this)
        val networkRepository = NetworkRepositoryImpl(database.networkDao())
        val eventManager = EventManager.getInstance(database.powerEventDao())
        val wifiScanner = WifiScannerImpl(this)
        
        monitoringManager = PowerMonitoringManager(
            context = this,
            wifiScanner = wifiScanner,
            networkRepository = networkRepository,
            eventManager = eventManager,
            notificationManager = notificationManager
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = notificationManager.getServiceNotification()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            startForeground(2001, notification, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(2001, notification)
        }
        
        monitoringManager.startMonitoring()
        
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        monitoringManager.stopMonitoring()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
