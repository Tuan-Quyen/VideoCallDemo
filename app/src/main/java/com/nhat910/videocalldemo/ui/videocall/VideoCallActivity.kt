package com.nhat910.videocalldemo.ui.videocall

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.nhat910.videocalldemo.R
import com.nhat910.videocalldemo.interfaces.ReceiveChatListener
import com.nhat910.videocalldemo.others.Constant
import com.nhat910.videocalldemo.ui.base.BaseActivity
import com.nhat910.videocalldemo.ui.chat.ChatFragment
import com.nhat910.videocalldemo.utils.AppUtils
import com.nhat910.videocalldemo.utils.KeyboardUtils
import com.nhat910.videocalldemo.utils.WebrtcSessionManagement
import com.quickblox.auth.session.QBSettings
import com.quickblox.chat.QBChatService
import com.quickblox.chat.connections.tcp.QBTcpChatConnectionFabric
import com.quickblox.chat.connections.tcp.QBTcpConfigurationBuilder
import com.quickblox.chat.model.QBChatDialog
import com.quickblox.videochat.webrtc.*
import com.quickblox.videochat.webrtc.callbacks.*
import com.quickblox.videochat.webrtc.exception.QBRTCSignalException
import com.quickblox.videochat.webrtc.view.QBRTCSurfaceView
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack
import kotlinx.android.synthetic.main.activity_video_call.*
import org.webrtc.EglBase
import org.webrtc.RendererCommon
import org.webrtc.SurfaceViewRenderer

class VideoCallActivity : BaseActivity(),
        View.OnClickListener,
        QBRTCClientVideoTracksCallbacks<QBRTCSession>,
        QBRTCClientSessionCallbacks,
        QBRTCSessionStateCallback<QBRTCSession>,
        QBRTCSessionConnectionCallbacks,
        QBRTCSignalingCallback {

    override fun onSuccessSendingPacket(p0: QBSignalingSpec.QBSignalCMD?, p1: Int?) {
        Toast.makeText(this, "Connect Success", Toast.LENGTH_SHORT).show()
    }

    override fun onErrorSendingPacket(p0: QBSignalingSpec.QBSignalCMD?, p1: Int?, p2: QBRTCSignalException?) {
        Toast.makeText(this, "Connect Fail", Toast.LENGTH_SHORT).show()
    }

    override fun onStartConnectToUser(p0: QBRTCSession?, p1: Int?) {
        Log.e("KKK", "onStartConnectToUser : ${p0.toString()}")
    }

    override fun onDisconnectedTimeoutFromUser(p0: QBRTCSession?, p1: Int?) {
        Log.e("KKK", "onDisconnectedTimeoutFromUser : ${p0.toString()}")
    }

    override fun onConnectionFailedWithUser(p0: QBRTCSession?, p1: Int?) {
        Log.e("KKK", "onConnectionFailedWithUser : ${p0.toString()}")
    }

    val TAG = "DEMO"
    private var isLocalVideoFullScreen: Boolean = false
    lateinit var localVideoView: QBRTCSurfaceView
    var remoteFullScreenVideoView: QBRTCSurfaceView? = null
    lateinit var toggle_startChat: ImageView
    lateinit var toggle_hangUpCall: ToggleButton
    lateinit var localVideoTrack: QBRTCVideoTrack
    lateinit var closeChat: ImageView
    lateinit var unReadMessage: ImageView
    lateinit var frContentChat: FrameLayout
    lateinit var mainContent: FrameLayout
    private var isCurrentCameraFront: Boolean = false
    lateinit var currentsession: QBRTCSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)
        createChatService()
        init()

    }

    fun init() {
        localVideoView = findViewById(R.id.localView)
        toggle_startChat = findViewById(R.id.toggle_start_chat)
        remoteFullScreenVideoView = findViewById(R.id.opponentView)
        toggle_hangUpCall = findViewById(R.id.button_hangup_call)
        closeChat = findViewById(R.id.actVideo_close)
        unReadMessage = findViewById(R.id.actVideo_unReadChat)
        frContentChat = findViewById(R.id.act_frContent)
        mainContent = findViewById(R.id.content)

        //getBundle
        val intent: Intent = intent
        if (intent.hasExtra(Constant.DATA_SEND_CHAT_DIALOG)) {
            var qbChatDialog: QBChatDialog = intent.getSerializableExtra(Constant.DATA_SEND_CHAT_DIALOG) as QBChatDialog
            qbChatDialog.initForChat(QBChatService.getInstance())
            replaceFragment(ChatFragment.newInstance(qbChatDialog, true, ReceiveChatListener {
                unReadMessage.visibility = View.VISIBLE
            }), false);
        }

        KeyboardUtils.setupUI(mainContent, this)

        //initOnClickListener
        toggle_startChat.setOnClickListener(this)
        toggle_hangUpCall.setOnClickListener(this)
        closeChat.setOnClickListener(this)
        localVideoView.setZOrderMediaOverlay(true)
        val eglContext: EglBase = QBRTCClient.getInstance(this).getEglContext()
        localVideoView.init(eglContext.eglBaseContext, null)
        localVideoView.release()
        localVideoView.requestLayout()
        initCorrectSizeForLocalView()
        //init session
        currentsession = WebrtcSessionManagement.getCurrentSession()!!
        addListener()
        onLocalVideoTrackReceive(currentsession, AppUtils.qbrtcVideoTrackLocal)
        onRemoteVideoTrackReceive(currentsession, AppUtils.qbrtcVideoTrackRemote, 0)
    }

    override fun onPause() {
        super.onPause()
        releaseView()
        releaseCurrentSession()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toggle_start_chat -> {
                view_action_button.visibility = View.GONE
                frContentChat.visibility = View.VISIBLE
                unReadMessage.visibility = View.GONE
            }
            R.id.button_hangup_call -> {
                currentsession.hangUp(HashMap())
            }
            R.id.actVideo_close -> {
                view_action_button.visibility = View.VISIBLE
                frContentChat.visibility = View.GONE
                KeyboardUtils.hideSoftKeyboard(this)
            }
        }
    }

    fun createChatService() {
        val configurationBuilder = QBTcpConfigurationBuilder()
        configurationBuilder.socketTimeout = 0
        QBChatService.setConnectionFabric(QBTcpChatConnectionFabric(configurationBuilder))
        QBChatService.setDebugEnabled(true)
    }

    fun addListener() {
        currentsession.addVideoTrackCallbacksListener(this)
        currentsession.addSessionCallbacksListener(this)
        currentsession.addEventsCallback(this)
        currentsession.addSignalingCallback(this)
    }

    fun releaseCurrentSession() {
        currentsession.removeSignalingCallback(this)
        currentsession.removeSessionCallbacksListener(this)
        QBRTCClient.getInstance(this).removeSessionsCallbacksListener(this)
        currentsession.removeVideoTrackCallbacksListener(this)

    }

    fun releaseView() {
        localVideoView.release()
        remoteFullScreenVideoView!!.release()
    }

    fun initCorrectSizeForLocalView() {
        val params = localVideoView.layoutParams
        val displaymetrics = resources.displayMetrics
        val screenWidthPx = displaymetrics.widthPixels
        Log.d("LCVD", "screenWidthPx $screenWidthPx")
        val width = (screenWidthPx * 0.3).toInt()
        val height = width / 2 * 3
        params?.width = width
        params?.height = height
        localVideoView.layoutParams = params
    }

    fun fillVideoView(videoView: QBRTCSurfaceView, videoTrack: QBRTCVideoTrack,
                      remoteRenderer: Boolean) {
        videoTrack.removeRenderer(videoTrack.renderer)
        videoTrack.addRenderer(videoView)
        if (!remoteRenderer) {
            updateVideoView(videoView, isCurrentCameraFront, RendererCommon.ScalingType.SCALE_ASPECT_FILL)
        }
        Log.d(TAG, (if (remoteRenderer) "remote" else "local") + " Track is rendering")
    }

    fun updateVideoView(videoView: SurfaceViewRenderer, mirror: Boolean, scalingType: RendererCommon.ScalingType) {
        Log.i(TAG, "updateVideoView mirror:$mirror, scalingType = $scalingType")
        videoView.setScalingType(scalingType)
        videoView.setMirror(mirror)
        videoView.requestLayout()
    }

    override fun onLocalVideoTrackReceive(p0: QBRTCSession?, viewTrack: QBRTCVideoTrack?) {
        Log.d(TAG, "onLocalVideoTrackReceive() run")
        if (viewTrack != null) {
            isLocalVideoFullScreen = true
            localVideoTrack = viewTrack!!
            localVideoTrack.let { fillVideoView(localVideoView, it, false) }
            isLocalVideoFullScreen = false
        }
    }

    override fun onRemoteVideoTrackReceive(p0: QBRTCSession?, p1: QBRTCVideoTrack?, p2: Int?) {
        if (p1 != null) {
            fillVideoView(remoteFullScreenVideoView!!, p1!!, true)
            updateVideoView(remoteFullScreenVideoView!!, false, RendererCommon.ScalingType.SCALE_ASPECT_FILL)
        }
    }

    override fun onUserNotAnswer(p0: QBRTCSession?, p1: Int?) {


    }

    override fun onSessionStartClose(p0: QBRTCSession?) {
        Log.e("session close", "session close");
        releaseCurrentSession()
        releaseView()
        AppUtils.isVideoHangUp = true
        AppUtils.qbrtcVideoTrackLocal = null
        AppUtils.qbrtcVideoTrackRemote = null
        WebrtcSessionManagement.setCurrentSession(null)
        finish()
    }

    override fun onReceiveHangUpFromUser(p0: QBRTCSession?, userID: Int?, p2: MutableMap<String, String>?) {

    }

    override fun onCallAcceptByUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {
        Log.e("HHH", "onCallAcceptByUser : ${p0.toString()}")

    }

    override fun onReceiveNewSession(p0: QBRTCSession?) {
        Log.e("KKK", "onReceiveNewSession : $p0")
        if (p0 != null) {
            currentsession = p0
            addListener()
            currentsession.acceptCall(p0.userInfo)
        }
    }

    override fun onUserNoActions(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onSessionClosed(p0: QBRTCSession?) {

    }

    override fun onCallRejectByUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {

    }

    override fun onDisconnectedFromUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onConnectedToUser(p0: QBRTCSession?, p1: Int?) {
        Log.e("KKK", "onConnectedToUser : ${p0.toString()}")
    }

    override fun onConnectionClosedForUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onStateChanged(p0: QBRTCSession?, p1: BaseSession.QBRTCSessionState?) {

    }
}