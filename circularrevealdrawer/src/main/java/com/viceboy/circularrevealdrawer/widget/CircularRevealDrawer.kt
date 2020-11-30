package com.viceboy.circularrevealdrawer.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.text.TextUtils
import android.transition.Transition
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import com.viceboy.circularrevealdrawer.R
import com.viceboy.circularrevealdrawer.interfaces.*
import com.viceboy.circularrevealdrawer.transitions.StaggerHideTransition
import com.viceboy.circularrevealdrawer.transitions.StaggerRevealTransition
import kotlinx.android.synthetic.main.layout_cirular_drawer.view.*

class CircularRevealDrawer : RelativeLayout, CircularMenuRevealListener {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.layout_cirular_drawer, this)

        attrs?.let { retrieveAttributes(attrs) }
    }

    private val rootViewGroup: ViewGroup
        get() = rootView as ViewGroup

    private var lnrLyt: LinearLayout? = null
    private var verticalScrollView: NestedScrollView? = null
    private var circularRevealListener: CircularRevealListener? = null
    private var menuItemClickListener: MenuItemClickListener? = null
    private var onProfileImageClickListener: OnProfileImageClickListener? = null

    private var menuContainerPaddingTop = 0
    private var menuContainerPaddingStart = 0

    //Flag to check if animation transition is running
    private var isRunning = false

    //Flag to check if fabCancel or profile image is hidden
    private var initialHide = false

    //Setting up username text
    private var txtUsername = emptyString

    //Setting up userEmail text
    private var txtUserEmail = emptyString

    //Menu text font style
    private var menuFontStyle: Int = 0

    //Menu text size
    private var menuTextSize: Float = 0f

    //Menu text padding Start
    private var menuTextPaddingStart: Int = 0

    //Menu text padding Top
    private var menuTextPaddingTop: Int = 0

    //Menu text padding End
    private var menuTextPaddingEnd: Int = 0

    //Menu text padding Bottom
    private var menuTextPaddingBottom: Int = 0

    //Menu text color
    private var menuTextColor: Int = 0

    //Menu text padding
    private var menuTextPadding: Int = 0
        set(value) {
            if (field != 0) {
                menuTextPaddingTop = menuTextPadding
                menuTextPaddingBottom = menuTextPadding
                menuTextPaddingStart = menuTextPadding
                menuTextPaddingEnd = menuTextPadding

                field = value
            }
        }

    //Inner circle color
    private var innerCircleColor: Int = 0
        set(value) {
            if (field != value) {
                circularMenu?.innerCircleColor = value
                fabCancel?.backgroundTintList = ColorStateList.valueOf(value)
                field = value
            }
        }

    //Outer circle color
    private var outerCircleColor: Int = 0
        set(value) {
            if (field != value) {
                circularMenu?.outerCircleColor = value
                field = value
            }
        }


    //Profile image resource for drawer
    private var profileImgRes: Drawable? = null
        set(value) {
            if (value == null)
                civProfileImg?.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_baseline_person_24
                    )
                )
            else
                civProfileImg?.setImageDrawable(value)

            field = value
        }

    //Profile Image stroke color
    private var profileImageBorderColor: Int = 0
        set(value) {
            if (field != value) {
                civProfileImg.borderColor = value

                field = value
            }
        }

    //Profile image Border width
    private var profileImageBorderWidth: Float = 0f
        set(value) {
            if (field != value) {
                civProfileImg.borderWidth = value.toInt()

                field = value
            }
        }

    //Hamburger bg color
    private var hamburgerBgColor: Int = 0
        set(value) {
            if (field != value) {
                val bg = hamburgerDrawerBtn?.background as LayerDrawable
                val base = bg.findDrawableByLayerId(R.id.hamburgerBg) as GradientDrawable
                base.setColor(value)
                hamburgerDrawerBtn?.background = bg

                field = value
            }
        }

    //Hamburger Inner Bar color
    private var hamburgerInnerBarColor: Int = 0
        set(value) {
            if (field != value) {
                val bg = hamburgerDrawerBtn?.background as LayerDrawable
                val bar1 = bg.findDrawableByLayerId(R.id.hamburgerInnerBar_1) as GradientDrawable
                val bar2 = bg.findDrawableByLayerId(R.id.hamburgerInnerBar_2) as GradientDrawable
                bar1.setColor(value)
                bar2.setColor(value)
                hamburgerDrawerBtn?.background = bg

                field = value
            }
        }

    //Store list of menu items
    private var arrayOfMenus = arrayOf<String>()
    private var menuStringArray: Int = 0
        set(value) {
            if (field != value) {
                arrayOfMenus = resources.getStringArray(value)

                field = value
            }
        }

    //Init Display metrics to calculate screen width and height
    private val displayMetrics: DisplayMetrics = DisplayMetrics().apply {
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay.getMetrics(this)
    }

    private fun retrieveAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularRevealDrawer)

        menuTextSize =
            typedArray.getDimension(
                R.styleable.CircularRevealDrawer_android_textSize,
                DEFAULT_MENU_TEXT_SIZE
            )

        menuTextPadding =
            typedArray.getDimension(R.styleable.CircularRevealDrawer_menuTextPadding, 0f).toInt()

        menuTextPaddingTop =
            typedArray.getDimension(R.styleable.CircularRevealDrawer_menuTextPaddingTop, 0f).toInt()

        menuTextPaddingStart =
            typedArray.getDimension(
                R.styleable.CircularRevealDrawer_menuTextPaddingStart,
                DEFAULT_PADDING
            )
                .toInt()

        menuTextPaddingEnd =
            typedArray.getDimension(
                R.styleable.CircularRevealDrawer_menuTextPaddingEnd,
                DEFAULT_PADDING
            ).toInt()

        menuTextPaddingBottom =
            typedArray.getDimension(
                R.styleable.CircularRevealDrawer_menuTextPaddingBottom,
                DEFAULT_TEXT_PADDING_BOTTOM
            )
                .toInt()

        menuFontStyle =
            typedArray.getResourceId(R.styleable.CircularRevealDrawer_android_fontFamily, 0)

        innerCircleColor = typedArray.getColor(
            R.styleable.CircularRevealDrawer_innerCircularColor,
            context.getColor(R.color.inner_indigo)
        )

        outerCircleColor = typedArray.getColor(
            R.styleable.CircularRevealDrawer_outerCircularColor,
            context.getColor(R.color.indigo)
        )

        menuTextColor = typedArray.getColor(
            R.styleable.CircularRevealDrawer_menuTextColor,
            context.getColor(android.R.color.white)
        )

        hamburgerBgColor = typedArray.getColor(
            R.styleable.CircularRevealDrawer_hamburgerColor,
            context.getColor(R.color.indigo)
        )

        hamburgerInnerBarColor = typedArray.getColor(
            R.styleable.CircularRevealDrawer_hamburgerInnerBarColor,
            context.getColor(android.R.color.white)
        )

        profileImgRes = typedArray.getDrawable(R.styleable.CircularRevealDrawer_profileImg)

        profileImageBorderWidth =
            typedArray.getDimension(R.styleable.CircularRevealDrawer_profileImgBorderWidth, 0f)

        profileImageBorderColor = typedArray.getColor(
            R.styleable.CircularRevealDrawer_profileImgBorderColor,
            context.getColor(android.R.color.darker_gray)
        )

        menuStringArray =
            typedArray.getResourceId(R.styleable.CircularRevealDrawer_menuTextArray, 0)

        txtUsername = typedArray.getString(R.styleable.CircularRevealDrawer_username) ?: emptyString

        txtUserEmail = typedArray.getString(R.styleable.CircularRevealDrawer_userEmail)
            ?: emptyString

        typedArray.recycle()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val statusBarHeight = getStatusBarHeight()

        if (!(statusBarHeight > 0 && height.plus(statusBarHeight) >= screenHeight && width == screenWidth))
            throw RuntimeException(context.getString(R.string.err_layout))

        //Set margins for circular reveal menu
        initLayoutParams()

        //Hide profile imageView and fab button
        hideProfileImageAndFab()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        rootView?.apply {
            elevation = dpToPx(DEFAULT_PADDING)
            //SetUp circular menu reveal listeners (onMenuCollapse/onMenuExpand) callback
            circularMenu?.setCircularMenuRevealListener(this@CircularRevealDrawer)

            //Setup all view click listeners
            hamburgerDrawerBtn?.setOnClickListener {
                circularMenu?.toggleMenu()
            }
            fabCancel?.setOnClickListener {
                if (!isRunning)
                    removeAllMenuItems()
            }
            civProfileImg?.setOnClickListener { view ->
                onProfileImageClickListener?.let {
                    removeAllMenuItems{
                        it.onClick(view)
                    }
                }
            }
        }
    }

    override fun onMenuExpandStart() {
        hamburgerDrawerBtn?.isVisible = false
        addVerticalScrollView()
        showUserHeaderText()
    }

    override fun onMenuCollapseStart() {
        civProfileImg?.animateHideWithTranslationX()
        fabCancel?.animateHideWithTranslationY()
    }

    override fun onMenuCollapseEnd() {
        hamburgerDrawerBtn?.isVisible = true
        removeAllTempViews()

        circularRevealListener?.onMenuCollapsed()
    }

    override fun onMenuExpandEnd() {
        civProfileImg?.animateShowWithTranslationX()
        fabCancel?.animateShowWithTranslationY()

        circularRevealListener?.onMenuExpanded()
    }

    /**
     * Removes run time created views such as NestedScrollView and LinearLayout
     */
    private fun removeAllTempViews() {
        verticalScrollView?.removeView(lnrLyt)
        removeView(verticalScrollView)
        lnrLyt = null
        verticalScrollView = null
    }

    /**
     * Add Nested scroll view to the root layout with calculated padding
     */
    private fun addVerticalScrollView() {
        //calculate dimensions for NestedScrollView
        val height = calculateRevealRadius().toInt()
        val width = width / 2

        //Init LinearLayout child for NestedScrollView
        lnrLyt = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = VERTICAL
        }

        //Init NestedScrollView with padding and child lnrlyt
        verticalScrollView = NestedScrollView(context).apply {
            layoutParams = LinearLayout.LayoutParams(width, height)
            setPadding(menuContainerPaddingStart, menuContainerPaddingTop, 0, 0)
            addView(lnrLyt)
        }

        //Add to rootView
        addView(verticalScrollView)

        //Create all menu items as TextView and add to lnrlyt
        lnrLyt?.let { createMenuItems(it) }
        invalidate()
    }

    /**
     * Create Menu items as textView and assign margins, padding and clickListener
     */
    private fun createMenuItems(linearLayout: LinearLayout) {
        if (arrayOfMenus.isNotEmpty()) {
            //Get menuItem click listener map
            val mapOfListener = menuItemClickListener?.getMenuListener(arrayOfMenus) ?: HashMap()

            arrayOfMenus.forEach { text ->
                val textLayout = createTextView {
                    this.text = text

                    //Set padding, margins and click listener
                    val params = layoutParams as LayoutParams
                    params.setMargins(0, 0, 0, menuTextPaddingBottom)
                    this.layoutParams = params

                    this.setPadding(
                        menuTextPaddingStart,
                        menuTextPaddingTop,
                        menuTextPaddingEnd,
                        menuTextPaddingStart
                    )
                    mapOfListener[text]?.let { onClickListener ->
                        setOnClickListener {
                            removeAllMenuItems{
                                onClickListener.onClick(it)
                            }
                        }
                    }
                }
                rootViewGroup.beginDelayedTransition(StaggerRevealTransition())
                linearLayout.addView(textLayout)
            }
        }
    }

    /**
     * SetUp layout params for MenuItemContainer and ProfileHeader text container
     */
    private fun initLayoutParams() {
        circularMenu?.apply {
            rootView?.hamburgerDrawerBtn?.let {

                //Init MenuContainer padding params
                menuContainerPaddingStart = it.width / 2
                menuContainerPaddingTop = it.y.toInt()

                //Init x & y coordinates to draw circular reveal
                innerCircleX = -(it.width.minus(40f)).plus(it.x)
                outerCircleX = (it.width.toFloat() / 2f).plus(it.x)
                outerCircleY = it.height.minus(it.y)
                innerCircleY = (it.height.minus(it.y)).minus(4f)
            }
        }
        val headerTextParams = rootView?.lnrLytUserDetails?.layoutParams as LayoutParams
        val profileImgParams = rootView?.civProfileImg?.layoutParams as LayoutParams

        val civWidth = rootView?.civProfileImg?.width?.plus(DEFAULT_PADDING)?.toInt() ?: 0
        val marginStart = profileImgParams.marginStart
        val marginEnd = profileImgParams.marginEnd
        val marginTop = menuContainerPaddingTop
        val marginEndForTxt = marginEnd.plus(civWidth)

        //Realign layout with calculated margins
        profileImgParams.setMargins(marginStart, marginTop, marginEnd, 0)
        headerTextParams.setMargins(marginStart, marginTop, marginEndForTxt, 0)

        //Assign updated layout params
        rootView?.lnrLytUserDetails?.layoutParams = headerTextParams
        rootView?.civProfileImg?.layoutParams = profileImgParams
    }

    /**
     * Hide Profile image  and fab cancel on startup
     */
    private fun hideProfileImageAndFab() {
        if (!initialHide) {
            rootView?.fabCancel?.hideWithTranslationY()
            rootView?.civProfileImg?.hideWithTranslationX()
            initialHide = true
        }
    }

    /**
     * Remove all menu text views and header textView from child container
     */
    private fun removeAllMenuItems(callback : () -> Unit = {}) {
        if (lnrLyt?.childCount == 0) circularMenu.toggleMenu()

        //Remove all menu items (Menu TextView)
        lnrLyt?.forEachChild {
            (verticalScrollView as ViewGroup).beginDelayedTransition(
                StaggerHideTransition().apply {
                    addListener(object : TransitionListener() {
                        override fun onTransitionEnd(transition: Transition?) {
                            isRunning = false
                        }

                        override fun onTransitionStart(transition: Transition?) {
                            isRunning = true
                            circularMenu.toggleMenu(DURATION_MENU_COLLAPSE_DELAY) {
                                callback.invoke()
                            }
                        }
                    })
                }
            )
            lnrLyt?.removeView(it)
        }
        //Remove Header textView (username and useremail) from child container
        lnrLytUserDetails.forEachChild {
            rootViewGroup.beginDelayedTransition(StaggerHideTransition())
            lnrLytUserDetails.removeView(it)
        }
    }

    /**
     * Create TextView and [action] for setting up additional properties
     */
    private fun createTextView(action: TextView.() -> Unit): TextView {
        return TextView(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            this.text = text
            typeface = if (menuFontStyle != 0)
                ResourcesCompat.getFont(context, menuFontStyle)
            else
                Typeface.SANS_SERIF

            this.maxLines = 1
            this.ellipsize = TextUtils.TruncateAt.END
            this.setTextAppearance(R.style.MenuTextAppearance)
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize)
            this.setTextColor(menuTextColor)

            action(this)
        }
    }

    /**
     * Show Profile user header text such as name and email
     */
    private fun showUserHeaderText() {
        val arrayOfText = arrayOf(txtUsername, txtUserEmail)
        lnrLytUserDetails.setPadding(0, DEFAULT_CONTAINER_PADDING_TOP, 0, 0)
        arrayOfText.forEach {
            val textLayout = createTextView {
                this.text = it
                this.textSize = DEFAULT_PROFILE_HEADER_TEXT_SIZE
            }
            lnrLytUserDetails.addView(textLayout)
        }
    }


    /**
     * Callback for onMenuCollapse or onMenuExpand
     */
    fun setCircularRevealListener(listener: CircularRevealListener) {
        circularRevealListener = listener
    }

    /**
     * Setup onProfile image click listener
     */
    fun setOnProfileImageClickListener(listener: OnProfileImageClickListener?) {
        onProfileImageClickListener = listener
    }

    /**
     * Set menu items click listener
     */
    fun setMenuItemClickListener(listener: MenuItemClickListener) {
        menuItemClickListener = listener
    }

    companion object {
        private const val emptyString = ""

        private const val DEFAULT_PADDING = 8f
        private const val DEFAULT_TEXT_PADDING_BOTTOM = 84f

        private const val DEFAULT_MENU_TEXT_SIZE = 24f
        private const val DEFAULT_PROFILE_HEADER_TEXT_SIZE = 12f

        private const val DEFAULT_CONTAINER_PADDING_TOP = 24
    }
}