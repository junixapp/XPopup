package com.lxj.xpopupdemo

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Color
import android.support.v4.view.NestedScrollingParent
import android.support.v4.view.NestedScrollingParentHelper
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.Scroller

class BottomCardContainer : FrameLayout, NestedScrollingParent {
    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attributes: AttributeSet) : super(ctx, attributes)
    constructor(ctx: Context, attributes: AttributeSet, themeId: Int) : super(ctx, attributes, themeId)

    private lateinit var child: View
    private var defaultShowHeight = 0
    private var slideListener: OnSlideListener? = null
    private val argbEvaluator = ArgbEvaluator()

    // 0 > minY > defaultY > maxY
    var minY = 0
    var defaultY = 0
    var maxY = 0
    private val maxShadow = .6f
    var scrollParentHelper: NestedScrollingParentHelper
    var scroller: Scroller

    init {
        setBackgroundColor(Color.TRANSPARENT)
        scrollParentHelper = NestedScrollingParentHelper(this)
        scroller = Scroller(context)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        child = getChildAt(0)
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                minY = -(measuredHeight - child.measuredHeight)
                autoSetShowHeight()
                defaultY = -(measuredHeight - defaultShowHeight)
                minY = defaultY //暂时用不到minY
                maxY = -measuredHeight
//                Log.e("tag", "setDefaultShowHeight defaultY: $defaultY")
//                Log.e("tag", "autoSetShowHeight defaultY: $defaultY  measuredHeight:$measuredHeight   child.measuredHeight:${child.measuredHeight}")

                scrollTo(0, -measuredHeight) // 最初scroll位置，即最下方
            }
        })

    }

    fun autoSetShowHeight() {
        if(defaultShowHeight!=0)return
        defaultShowHeight = if (measuredHeight > child.measuredHeight) child.measuredHeight
        else child.measuredHeight / 2
    }

    var hasTouch = false
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        hasTouch = true
        return super.dispatchTouchEvent(event)
    }

    var downY = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                var dy = event.y - downY
                scrollTo(0, scrollY - dy.toInt())
                downY = event.y
            }
            MotionEvent.ACTION_UP -> {
                downY = 0f
                onStopNestedScroll(this)
            }
        }
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
    }

    fun setDefaultShowHeight(h: Int): BottomCardContainer {
        this.defaultShowHeight = h
        return this
    }

    fun setOnSlideListener(slideListener: OnSlideListener): BottomCardContainer {
        this.slideListener = slideListener
        return this
    }

    fun open() {
        post {
            scroller.startScroll(0, scrollY, 0, defaultY - scrollY, 600)
            invalidate()
        }
    }

    fun close() {
        hasTouch = true
        post {
            // 100 为容错值，防止在某些情况下无法滚动到maxY位置
            scroller.startScroll(0, scrollY, 0, maxY - scrollY - 100, 500)
            invalidate()
        }
    }

    // 0 > minY > defaultY > maxY
    override fun scrollTo(x: Int, y: Int) {
        //limit y
        var targetY = y
        if (y > minY) {
            targetY = minY
        } else if (y < maxY) {
            targetY = maxY
        }
        val fraction = (targetY - maxY) * 1f / (minY - maxY)
        //make bg shadow
        setBackgroundColor(argbEvaluator.evaluate(if (fraction > maxShadow) maxShadow else fraction, Color.TRANSPARENT, Color.BLACK) as Int)

        slideListener?.onSlide(fraction)
//        Log.e("tag", "fraction: $fraction   hasTouch:$hasTouch   maxY:$maxY   targetY:$targetY" )
        if (hasTouch) {
           if(targetY==maxY){
               slideListener?.onClose()
               hasTouch = false
           }
        }
        super.scrollTo(x, targetY)
    }

    /** NestScroll about **/
    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }


    override fun onNestedScrollAccepted(child: View, target: View, nestedScrollAxes: Int) {
        scrollParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes)
    }

    // touch up: 0 > minY > defaultY > maxY
    override fun onStopNestedScroll(target: View) {
//        val middle = (minY + defaultY) / 2
        val secMiddle = ((defaultY + maxY) / 2 + defaultY )/2

        when {
//            scrollY in (middle + 1)..(minY - 1) -> scroller.startScroll(0, scrollY, 0, minY - scrollY)
            scrollY in (secMiddle + 1)..(defaultY - 1) -> scroller.startScroll(0, scrollY, 0, defaultY - scrollY)
            scrollY < secMiddle -> scroller.startScroll(0, scrollY, 0, maxY - scrollY)
        }
        invalidate()
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
//        Log.e("tag", "onNestedScroll   dyConsumed:$dyConsumed   dyUnconsumed:${dyUnconsumed}")

        var t = scrollY
        if (dyUnconsumed < 0) {
            //move down
            t += dyUnconsumed // t越小，则越向下scroll
            if (t < maxY) {
                t = maxY // 最大不超过容器高度
            }
            scrollTo(0, t)
        }

    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
//        Log.e("tag", "onNestedPreScroll   dy:$dy   consumed:${consumed}")
        var t = scrollY
        if (dy > 0) {
            // move up
            if (t < defaultY) { // 不在默认位置
                t += dy // t越大。越向上scroll
                if (t > minY) {
                    t = minY
                }
                scrollTo(0, t)
                consumed[1] = dy
            }
        }

    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
//        if(velocityY < -1000f){
//            scroller.startScroll(0, scrollY, 0, maxY - scrollY)
//            invalidate()
//        }
        return false
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
//        scroller.fling(0 , scrollY, 0, velocityY.toInt(), 0, 0, minY, maxY)
//        invalidate()
        return false
    }

    override fun getNestedScrollAxes() = ViewCompat.SCROLL_AXIS_VERTICAL

    interface OnSlideListener {
        fun onSlide(fraction: Float){}
        fun onClose()
    }
}