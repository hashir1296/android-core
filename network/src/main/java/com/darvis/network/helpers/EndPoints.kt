package com.darvis.network.helpers

object EndPoints {
    object Auth {
        const val LOGIN = "token"
        const val FORGOT_PASSWORD = "users/forgot_password"
        const val CHANGE_PASSWORD = "users/reset_password"
        const val REGISTER_DEVICE_FCM_TOKEN = "users/fcm_token/add"
    }

    object Socket {
        const val Detections = "detections"
    }

    object User {
        const val ALERT_PREFERENCE = "users/alert_setting"
        const val ALERT_HISTORY = "users/alert_history"
        const val STATISTICS = "users/statistics"
        const val ALERT_FEEDBACK = "users/alert_feedback"
        const val USER_WINGS = "users/self/wings"
        const val ACTIVE_TIERS = "users/tiers/active"
        const val REGISTER_DEVICE_FCM_TOKEN = "users/fcm_token/add"
        const val ALERT_SOUND = "users/alert/sound"
        const val ROOMS = "users/self/wings/rooms"

    }

    object Dashboard {
        const val DASHBOARD_SITES = "dashboard/site/stats"
        const val SITE_UPDATE = "site/update"
        const val DASHBOARD_SITE_STATS = "dashboard/site/apps/stats"
        const val CAMERA_LIST = "cameras-list"
    }
}

object Ports {
    const val Auth = 8090
    const val Socket = 3002
    const val Dashboard = 8000
}