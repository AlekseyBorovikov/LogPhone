package com.example.loginningphone_12.util

class Constants {

    public class ACTION{
        companion object{
            const val APPS_SERVICE = "log_phone.apps.reader"
            const val START_ACTION = "log_phone.action.start"
            const val STOP_ACTION = "log_phone.action.stop"
            const val MAIN_ACTION = "log_phone.action.main"
        }
    }

    public class STATE_SERVICE{
        companion object{
            const val CONNECTED = 10
            const val NOT_CONNECTED = 0
        }
    }

    companion object{
        const val NOTIFICATION_CHANNEL_NAME = "log_notification_service_channel"
        const val NOTIFICATION_CHANNEL_ID = "log_phone_notification_reader"
        const val INTENT_PROCESS_ID = 45001
        const val INTENT_NOTIFICATION_ID = 8466503
        const val REPEAT_SECONDS: Long = 1000*60*60
    }
}