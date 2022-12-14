import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent

class TwoFingerDrag(context: Context, private val listener: Listener) {
    //    private long timeDrawStarted;
    private var oneFingerOperationStatus = 0
    private var oneFingerStartEvent: MotionEvent? = null
    private val handlerConfirmDrawByTime: Handler
    private val confirmDrawByTime: Runnable
    private var panStarted = false

    init {
        DISTANCE_TOLERANCE_PX = context.resources.displayMetrics.density * DISTANCE_TOLERANCE_DP
        handlerConfirmDrawByTime = Handler(Looper.getMainLooper())
        confirmDrawByTime = Runnable {
            Log.d("MyApp3", "TwoFingerDrag   confirming draw by time")
            oneFingerOperationStatus = IN_PROGRESS
            listener.onOneFinger(oneFingerStartEvent)
        }
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
//        if (event.getToolType(0) == MotionEvent.TOOL_TYPE_FINGER) {
        if (event.pointerCount == 1) {
//                Log.d("MyApp3", "TwoFingerDrag onTouchEvent one " + MotionEvent.actionToString(event.getAction()));
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    //                        timeDrawStarted = System.currentTimeMillis();
                    oneFingerOperationStatus = NOT_STARTED
                    panStarted = false
                    oneFingerStartEvent = MotionEvent.obtain(event)
                    handlerConfirmDrawByTime.postDelayed(confirmDrawByTime, CONFIRM_DRAW_DELAY_MS)
                    return true
                }
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                    if (!panStarted) {
                        if (oneFingerOperationStatus == NOT_STARTED) {
                            val distance = Math.hypot(
                                (event.x - oneFingerStartEvent!!.x).toDouble(),
                                (event.y - oneFingerStartEvent!!.y).toDouble()
                            )
                            Log.d(
                                "MyApp3",
                                "   distance " + distance + " vs " + DISTANCE_TOLERANCE_PX
                            )
                            if (distance < DISTANCE_TOLERANCE_PX) {
                                Log.d("MyApp3", "   cancelling draw by distance")
                            } else {
                                Log.d("MyApp3", "   confirming draw by distance")
                                handlerConfirmDrawByTime.removeCallbacks(confirmDrawByTime)
                                oneFingerOperationStatus = IN_PROGRESS
                                listener.onOneFinger(oneFingerStartEvent)
                                listener.onOneFinger(event)
                            }
                        } else {
                            if (event.action == MotionEvent.ACTION_UP) oneFingerOperationStatus =
                                FINISHED
                            listener.onOneFinger(event)
                        }
                    }
                    return true
                }
            }
        } else if (event.pointerCount == 2) {
//                Log.d("MyApp3", "TwoFingerDrag onTouchEvent two " + MotionEvent.actionToString(event.getAction()));
            when (event.actionMasked) {
                MotionEvent.ACTION_POINTER_DOWN -> {
                    handlerConfirmDrawByTime.removeCallbacks(confirmDrawByTime)
                    panStarted = true
                    if (oneFingerOperationStatus == IN_PROGRESS) {
                        oneFingerOperationStatus = FINISHED
                        listener.onOneFinger(event)
                    }
                    Log.d(
                        "MyApp3",
                        "   start pan"
                    ) // + (System.currentTimeMillis() - timeDrawStarted));
                    listener.onTwoFingers(event)
                    return true
                }
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_POINTER_UP -> {
                    listener.onTwoFingers(event)
                    return true
                }
            }
        } else {
            Log.d(
                "MyApp3",
                "TwoFingerDrag onTouchEvent more " + event.pointerCount + " " + MotionEvent.actionToString(
                    event.action
                )
            )
        }
        //        } else {
//            Log.d("MyApp3", "TwoFingerDrag onTouchEvent not finger " + event.getToolType(0) + " " + event.getPointerCount() + " " + MotionEvent.actionToString(event.getAction()));
//        }
        return false
    }

    interface Listener {
        /**
         *
         * @param event can be null if the one finger event series is interrupted
         */
        fun onOneFinger(event: MotionEvent?)
        fun onTwoFingers(event: MotionEvent?)
    }

    companion object {
        private const val CONFIRM_DRAW_DELAY_MS: Long = 200
        private const val DISTANCE_TOLERANCE_DP = 4f
        private const val NOT_STARTED = 0
        private const val IN_PROGRESS = 1
        private const val FINISHED = 2
        private var DISTANCE_TOLERANCE_PX: Float = 0.0f
    }
}