package com.nhat910.videocalldemo.utils

import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacksImpl

object WebrtcSessionManagement:QBRTCClientSessionCallbacksImpl() {
    private var currentSession: QBRTCSession? = null
    fun getCurrentSession():QBRTCSession?{
        return  this.currentSession
    }
    fun setCurrentSession(qbrtcSession: QBRTCSession?){
        this.currentSession = qbrtcSession
    }
}