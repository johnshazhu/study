package com.study.lib.util

import android.content.Context
import android.text.TextUtils
import android.text.format.Time
import android.util.Log
import com.study.lib.common.R
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间工具类
 * Created by zq on 2015/5/2.
 */
object DateTimeUtil {
    private val TAG = DateTimeUtil::class.java.simpleName
    const val ONE_MINUTE_SECS = 60
    const val ONE_HOUR_SECS = 3600
    const val ONE_DAY_SECS = 86400
    const val THREE_DAY_SECS = 259200
    const val ONE_DAY_TIMESTAMP = 24 * 3600 * 1000L
    const val ONE_WEEK_TIMESTAMP = 7 * ONE_DAY_TIMESTAMP
    private var sIsYxyCommentTime = true
    @JvmStatic
    fun setUseYxyCommentTime(useYxyCommentTime: Boolean) {
        sIsYxyCommentTime = useYxyCommentTime
    }

    @JvmStatic
    val currentTimestamp: Long
        get() = System.currentTimeMillis()

    @JvmStatic
    fun getTimestampYMD(date: String?): Long {
        return getTimestamp("yyyy年MM月dd日", date)
    }

    @JvmStatic
    fun getTimestampSimple(date: String?): Long {
        return getTimestamp("yyyy-MM-dd", date)
    }

    @JvmStatic
    fun getSimpleTimestamp(time: Long): String {
        return SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(Date(time))
    }

    @JvmStatic
    fun getTimestamp(time: String?): Long {
        return getTimestamp("yyyy-MM-dd HH:mm:ss", time)
    }

    @JvmStatic
    fun formatPushMsgDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("M月d日", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    @JvmStatic
    fun formatLiveSubscribeDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("M月d日 HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    @JvmStatic
    fun formatVideoDuration(timestamp: Long): String {
        val sdf = SimpleDateFormat("m:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    @JvmStatic
    fun formatStandardDuration(timestamp: Long): String {
        val sdf = SimpleDateFormat("mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    @JvmStatic
    fun getTimestamp(format: String?, time: String?): Long {
        var result: Long = 0
        if (!TextUtils.isEmpty(time)) {
            val sf = SimpleDateFormat(format, Locale.getDefault())
            try {
                val date = sf.parse(time)
                result = date.time
            } catch (e: Throwable) {
                Log.e(TAG, "getTimestamp e[$e]")
            }
        }
        return result
    }

    @JvmStatic
    fun formatDefault(timestamp: Long): String {
        val formate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formate.format(Date(timestamp))
    }

    @JvmStatic
    fun format(template: String?, timestamp: Long): String {
        val formate = SimpleDateFormat(template, Locale.getDefault())
        return formate.format(Date(timestamp))
    }

    @JvmStatic
    fun format(timestamp: Long): String {
        val formate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formate.format(Date(timestamp))
    }

    @JvmStatic
    fun formatYMD(timestamp: Long): String {
        val formate = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return formate.format(Date(timestamp))
    }

    @JvmStatic
    fun formatHourMin(timestamp: Long): String {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(Date(timestamp))
    }

    // 通过时间戳获取月, 从0开始
    @JvmStatic
    fun getMonth(timestamp: Long): Int {
        val currentCalendar = Calendar.getInstance(Locale.getDefault())
        currentCalendar.timeInMillis = timestamp
        return currentCalendar[Calendar.MONTH]
    }

    // 通过时间戳获取小时及分钟, 返回float
    @JvmStatic
    fun getFloatHour(timestamp: Long): Float {
        val currentCalendar = Calendar.getInstance(Locale.getDefault())
        currentCalendar.timeInMillis = timestamp
        return currentCalendar[Calendar.HOUR_OF_DAY] + currentCalendar[Calendar.MINUTE] / 60f
    }

    // 获取毫秒时间的时分字符串(long ts) {
    @JvmStatic
    fun getHourMinuteString(ts: Long): String {
        var ts = ts
        ts /= 60000
        val hour = ts / 60
        val minute = ts % 60
        return if (0L == hour && 0L == minute) {
            ""
        } else hour.toString() + ":" + String.format("%02d", minute) + "'"
    }

    // 获取当天的起始时间字符串
    @JvmStatic
    fun getDayStartString(ts: Long): String {
        return format("yyyy-MM-dd", ts) + " 00:00:00"
    }

    // 获取当天的起始时间戳
    @JvmStatic
    fun getDayStartTimestamp(ts: Long): Long {
        return getTimestamp("yyyy-MM-dd", format("yyyy-MM-dd", ts))
    }

    // 获取当天最后的时间字符串
    @JvmStatic
    fun getDayEndString(ts: Long): String {
        return format("yyyy-MM-dd", ts) + " 23:59:59"
    }

    // 获取当天最后的时间戳
    @JvmStatic
    fun getDayEndTimestamp(ts: Long): Long {
        return getTimestamp("yyyy-MM-dd", format("yyyy-MM-dd", ts)) + ONE_DAY_TIMESTAMP - 1
    }

    // 是否同一月
    @JvmStatic
    fun isSameMonth(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.MONTH] == cal2[Calendar.MONTH]
    }

    // 是否同一年
    @JvmStatic
    fun isSameYear(ts1: Long, ts2: Long): Boolean {
        val cal1 = Calendar.getInstance(Locale.getDefault())
        cal1.timeInMillis = ts1
        val cal2 = Calendar.getInstance(Locale.getDefault())
        cal2.timeInMillis = ts2
        return cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
    }

    // 是否同一年
    @JvmStatic
    fun isSameYear(date1: String?, date2: String?): Boolean {
        return isSameYear(getTimestamp(date1), getTimestamp(date2))
    }

    // 是否同一天
    @JvmStatic
    fun isSameDay(ts1: Long, ts2: Long): Boolean {
        val sf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sf.format(ts1) == sf.format(ts2)
    }

    // 是否同一天
    @JvmStatic
    fun isSameDay(date1: String?, date2: String?): Boolean {
        return isSameDay(getTimestamp(date1), getTimestamp(date2))
    }

    // 判断是否超过当前天
    @JvmStatic
    fun isFutureDay(ts: Long): Boolean {
        return ts / ONE_DAY_TIMESTAMP > currentTimestamp / ONE_DAY_TIMESTAMP
    }

    //获取某一年的一月一日的时间戳
    @JvmStatic
    fun getOneYearTimestamp(year: Int): Long {
        val instance = Calendar.getInstance(Locale.getDefault())
        instance[Calendar.YEAR] = instance[Calendar.YEAR] - year
        instance[Calendar.MONTH] = 0
        instance[Calendar.DAY_OF_MONTH] = 1
        return instance.timeInMillis
    }

    // 获取星期几。顺序：日，一，二，三，四，五，六。值：1-7
    @JvmStatic
    fun getDayOfWeek(ts: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = ts
        return cal[Calendar.DAY_OF_WEEK]
    }

    @JvmStatic
    fun getDayOfWeekString(ts: Long): String? {
        return try {
            val result = arrayOf("", "日", "一", "二", "三", "四", "五", "六")
            result[getDayOfWeek(ts)]
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

    // 两个时间戳的天数差
    @JvmStatic
    fun getDiffDay(ts1: Long, ts2: Long): Long {
        val cal1 = Calendar.getInstance()
        cal1.timeInMillis = ts1
        cal1[Calendar.HOUR_OF_DAY] = 0
        cal1[Calendar.MINUTE] = 0
        cal1[Calendar.SECOND] = 0
        cal1[Calendar.MILLISECOND] = 0
        val cal2 = Calendar.getInstance()
        cal2.timeInMillis = ts2
        cal2[Calendar.HOUR_OF_DAY] = 0
        cal2[Calendar.MINUTE] = 0
        cal2[Calendar.SECOND] = 0
        cal2[Calendar.MILLISECOND] = 0
        return (cal1.timeInMillis - cal2.timeInMillis) / ONE_DAY_TIMESTAMP
    }

    // 两个时间戳的周数差
    @JvmStatic
    fun getDiffWeek(ts1: Long, ts2: Long): Long {
        if (ts1 < ts2) {
            return -getDiffWeek(ts2, ts1)
        }
        val cal1 = getWeekInitCalendar(ts1)
        val cal2 = getWeekInitCalendar(ts2)
        var weeks = 0
        while (cal2.before(cal1)) {
            cal2.add(Calendar.WEEK_OF_YEAR, 1)
            weeks++
        }
        return weeks.toLong()
    }

    // 获取时间戳所在周的开始Calendar
    @JvmStatic
    fun getWeekInitCalendar(ts: Long): Calendar {
        val cal = Calendar.getInstance()
        cal.timeInMillis = ts
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        cal.add(Calendar.DAY_OF_YEAR, -(cal[Calendar.DAY_OF_WEEK] - 1))
        return cal
    }

    // ts2 - ts1的年月日差
    @JvmStatic
    fun getDiffTime(ts1: Long, ts2: Long): TimeInfo {
        val result = TimeInfo()
        val calendar1 = Calendar.getInstance(Locale.getDefault())
        calendar1.timeInMillis = ts1
        val calendar2 = Calendar.getInstance(Locale.getDefault())
        calendar2.timeInMillis = ts2
        result.mDay = calendar2[Calendar.DAY_OF_MONTH] - calendar1[Calendar.DAY_OF_MONTH]
        result.mMonth = calendar2[Calendar.MONTH] - calendar1[Calendar.MONTH]
        result.mYear = calendar2[Calendar.YEAR] - calendar1[Calendar.YEAR]
        if (result.mDay < 0) {
            result.mMonth -= 1
            calendar2.add(Calendar.MONTH, -1) // 得到上一个月，用来得到上个月的天数。
            result.mDay = result.mDay + calendar2.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
        if (result.mMonth < 0) {
            result.mMonth = (result.mMonth + 12) % 12
            result.mYear--
        }
        return result
    }

    /**
     * 格式化到日
     *
     * @param timen
     * @return
     */
    @JvmStatic
    fun dateToStr(timen: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(Date(timen))
    }

    /**
     * 比较时间大小
     *
     * @param s1
     * @param s2
     * @return
     */
    @JvmStatic
    fun Ccompareday(s1: String?, s2: String?): Boolean {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        try {
            c1.time = df.parse(s1)
            c2.time = df.parse(s2)
        } catch (e: ParseException) {
            System.err.println("格式不正确")
        }
        val result = c1.compareTo(c2)
        return result > 0
    }

    /**
     * 是否大于两岁
     *
     * @param s1 选择日期
     * @param s2 当前日期
     * @return
     */
    @JvmStatic
    fun isGreaterThanTwoYear(s1: String?, s2: String?): Boolean {
        var isGreaterThan = false
        val mArrayS1: Array<String>
        val mArrayS2: Array<String>
        if (s1 != null && s2 != null) {
            try {
                mArrayS1 = s1.split("-".toRegex()).toTypedArray()
                mArrayS2 = s2.split("-".toRegex()).toTypedArray()
                if (mArrayS1 != null && mArrayS2 != null) {
                    val mYear = mArrayS2[0].toInt() - mArrayS1[0].toInt()
                    val mMonthS1 = mArrayS1[1].toInt()
                    val mMonthS2 = mArrayS2[1].toInt()
                    val mDayS1 = mArrayS1[2].toInt()
                    val mDayS2 = mArrayS2[2].toInt()
                    if (mYear > 2) {
                        isGreaterThan = true
                    } else if (mYear == 2) {
                        when {
                            mMonthS1 > mMonthS2 -> {
                                isGreaterThan = false
                            }
                            mMonthS1 == mMonthS2 -> {
                                isGreaterThan = mDayS1 < mDayS2
                            }
                            mMonthS1 < mMonthS2 -> {
                                isGreaterThan = true
                            }
                        }
                    } else if (mYear < 2) {
                        isGreaterThan = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return isGreaterThan
    }

    //判断时间是过去日期 、当前日期、未来日期
    @JvmStatic
    fun Ccomparedays(s1: String?, s2: String?): Int {
        var result = -2
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        try {
            c1.time = df.parse(s1)
            c2.time = df.parse(s2)
            result = if (c1.before(c2)) {
                -1
                //   System.out.println("小于当前时间");
            } else if (c1.after(c2)) {
                //  System.out.println("大于当前时间");
                1
            } else {
                //  System.out.println("等于当前时间");
                0
            }
        } catch (e: ParseException) {
            System.err.println("格式不正确")
        }
        return result
    }

    @JvmStatic
    fun isToday(format: String?, dateStr: String?): Boolean {
        return isSameDay(currentTimestamp, getTimestamp(format, dateStr))
    }

    //某一日期是否为今天或明天
    @JvmStatic
    fun isTomorrow(dateNumber: Int, dateStr: String?): Boolean {
        var date = Date() //取时间
        val calendar: Calendar = GregorianCalendar()
        calendar.time = date
        calendar.add(Calendar.DATE, dateNumber) //把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.time //这个时间就是日期往后推一天的结果
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tomorrowString = formatter.format(date)
        var date1: Date? = null
        try {
            date1 = formatter.parse(dateStr)
            val dateString = formatter.format(date1)
            if (tomorrowString == dateString) {
                return true
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 是不是当前时间的明天
     * @param currTime string类型时间戳
     * @param tomorrowTime string类型时间戳
     * @return
     */
    @JvmStatic
    fun isTomorrow(currTime: String?, tomorrowTime: String?): Boolean {
        var tomorrowTime = tomorrowTime
        try {
            tomorrowTime = format(SimpleUtil.parseLong(tomorrowTime))
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            var date = Date(SimpleUtil.parseLong(currTime))
            val calendar: Calendar = GregorianCalendar()
            calendar.time = date
            calendar.add(Calendar.DATE, 1) //把日期往后增加一天.整数往后推
            date = calendar.time
            val addOneDayTime = sdf.format(date) //这个时间就是日期往后推一天的结果
            if (TextUtils.equals(tomorrowTime, addOneDayTime)) {
                return true
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 字符串的日期格式的计算
     */
    @JvmStatic
    fun daysBetween(smdate: String?, bdate: String?): Int {
        var between_days: Long = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        try {
            cal.time = sdf.parse(smdate)
            val time1 = cal.timeInMillis
            cal.time = sdf.parse(bdate)
            val time2 = cal.timeInMillis
            between_days = (time2 - time1) / (1000 * 3600 * 24)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return between_days.toString().toInt()
    }

    // 获取日
    @JvmStatic
    fun getDayOfMonth(date: String?): String {
        return if (null != date) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = getTimestamp(date)
            cal[Calendar.DAY_OF_MONTH].toString() + ""
        } else {
            ""
        }
    }

    // 获取年月
    @JvmStatic
    fun getYearAndMonth(date: String?): String {
        return if (null != date) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = getTimestamp(date)
            cal[Calendar.YEAR].toString() + "." + String.format("%02d", cal[Calendar.MONTH] + 1)
        } else {
            ""
        }
    }

    @JvmStatic
    fun getYearAndMonthChese(date: String?): String {
        return if (null != date) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = getTimestamp(date)
            cal[Calendar.YEAR].toString() + "年" + (cal[Calendar.MONTH] + 1) + "月"
        } else {
            ""
        }
    }

    /**
     * 日期格式如 : 2016-09-18 21:59:49
     * 比较两个日期串，判断是否是同年同月
     *
     * @param time1
     * @param time2
     * @return
     */
    @JvmStatic
    fun isSameMonth(time1: String, time2: String): Boolean {
        if (TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2)) {
            return false
        }

        //yyyy-MM长度
        val date1 = time1.substring(0, 7)
        val date2 = time2.substring(0, 7)
        return date1 == date2
    }

    /**
     * 判断当前时间 是否在某一时间段
     *
     * @param startData 开始时间
     * @param endData   结束时间
     * @return
     */
    @JvmStatic
    fun isTimeQuantum(startData: String, endData: String): Boolean {
        var isTime = false
        val formate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        try {
            val time = formate.parse(formatDefault(currentTimestamp))
            val startTime = formate.parse("$simpleTimestamp $startData")
            val endTime = formate.parse("$simpleTimestamp $endData")
            if (time.time >= startTime.time && time.time <= endTime.time) {
                isTime = true
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return isTime
    }

    @JvmStatic
    val simpleTimestamp: String
        get() {
            val mFormate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return mFormate.format(Date(currentTimestamp))
        }

    private const val LECTURE_CUR_DAY_FORMAT = "HH:mm"
    private const val LECTURE_OTHER_DAY_FORMAT = "yyyy-MM-dd HH:mm"
    private fun getGMTTimeStamp(format: String, timestamp: Long): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(timestamp + TimeZone.getDefault().rawOffset)
    }

    @JvmStatic
    fun getLectureTimeStamp(timestamp: Long): String {
        return getGMTTimeStamp(LECTURE_CUR_DAY_FORMAT, timestamp)
    }

    @JvmStatic
    fun getHomePushTimeStamp(timestamp: Long): String {
        return format(LECTURE_CUR_DAY_FORMAT, timestamp)
    }

    @JvmStatic
    fun getLectureTimeStamp(cur: Long, prev: Long): String? {
        if (cur != 0L && prev != 0L) {
            if (Math.abs(cur - prev) > 3 * ConstantsUtil.ONE_MIN_MS) {
                //如果距离上一次显示时间条超过3分钟就显示新的时间条，如果没超过就不显示新的
                return if ( /*!isSameDay(cur, prev) || */!isSameDay(cur, System.currentTimeMillis())) {
                    //非当天的时间为年-月-日 小时:分钟
                    getGMTTimeStamp(LECTURE_OTHER_DAY_FORMAT, cur)
                } else {
                    getLectureTimeStamp(cur)
                }
            }
        }
        return null
    }

    @JvmStatic
    val curGMTTime: Long
        get() = System.currentTimeMillis() - TimeZone.getDefault().rawOffset
    @JvmStatic
    fun getCurGMTTime(cur: Long): Long {
        return cur - TimeZone.getDefault().rawOffset
    }

    @JvmStatic
    fun isBetweenTime(start: String?, end: String?): Boolean {
        val result: Boolean
        val cur = format("HH:mm:ss", currentTimestamp)
        result = if (cur.compareTo(start!!, ignoreCase = true) < 0) {
            false
        } else cur.compareTo(end!!, ignoreCase = true) <= 0
        Log.i(TAG, "isBetweenTime cur[$cur] result[$result]")
        return result
    }

    @JvmStatic
    fun isSameYear(timestamp: Long): Boolean {
        val start = Calendar.getInstance()
        start.timeInMillis = System.currentTimeMillis()
        val curYear = start[Calendar.YEAR]
        start.timeInMillis = timestamp
        val year = start[Calendar.YEAR]
        return year == curYear
    }

    @JvmStatic
    fun getSystemMessageTime(timestamp: Long): String {
        return if (isSameYear(timestamp)) {
            SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(timestamp)
        } else {
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(timestamp)
        }
    }

    @JvmStatic
    fun getAddDayTimestamp(cur: Long, addDay: Int): Long {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = cur
        calendar.add(Calendar.DAY_OF_YEAR, addDay)
        return calendar.timeInMillis
    }

    @JvmStatic
    fun getAddMonthTimestamp(cur: Long, addValue: Int): Long {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = cur
        calendar.add(Calendar.MONTH, addValue)
        return calendar.timeInMillis
    }

    /**
     * 是否是同一天
     *
     * @param dBeginStr
     * @param dEndStr
     * @return
     */
    @JvmStatic
    fun isSameDate(dBeginStr: String?, dEndStr: String?): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = sdf.parse(dBeginStr)
            date2 = sdf.parse(dEndStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        val isSameYear = cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
        val isSameMonth = (isSameYear
                && cal1[Calendar.MONTH] == cal2[Calendar.MONTH])
        return (isSameMonth
                && cal1[Calendar.DAY_OF_MONTH] == cal2[Calendar.DAY_OF_MONTH])
    }

    /**
     * 是否是同一天
     *
     * @param dBeginStr
     * @param dEndStr
     * @return
     */
    @JvmStatic
    fun isSameDate(sdf: SimpleDateFormat, date1: Date?, date2: Date?, cal1: Calendar, cal2: Calendar, dBeginStr: String?, dEndStr: String?): Boolean {
        var date1 = date1
        var date2 = date2
        try {
            date1 = sdf.parse(dBeginStr)
            date2 = sdf.parse(dEndStr)
            cal1.time = date1
            cal2.time = date2
            val isSameYear = cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
            val isSameMonth = (isSameYear
                    && cal1[Calendar.MONTH] == cal2[Calendar.MONTH])
            return (isSameMonth
                    && cal1[Calendar.DAY_OF_MONTH] == cal2[Calendar.DAY_OF_MONTH])
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 获取某个时间段的日期数组
     *
     * @param dBeginStr
     * @param dEndStr
     * @return
     */
    @JvmStatic
    fun findDates(dBeginStr: String?, dEndStr: String?): List<Date?> {
        val lDate: MutableList<Date?> = ArrayList<Date?>()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var dBegin: Date? = null
        var dEnd: Date? = null
        try {
            dBegin = sdf.parse(dBeginStr)
            dEnd = sdf.parse(dEndStr)
            lDate.add(dBegin)
            val calBegin = Calendar.getInstance()
            // 使用给定的 Date 设置此 Calendar 的时间
            calBegin.time = dBegin
            val calEnd = Calendar.getInstance()
            // 使用给定的 Date 设置此 Calendar 的时间
            calEnd.time = dEnd
            // 测试此日期是否在指定日期之后
            while (dEnd.after(calBegin.time)) {
                // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
                calBegin.add(Calendar.DAY_OF_MONTH, 1)
                lDate.add(calBegin.time)
            }
            return lDate
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return lDate
    }

    /**
     * 结束时间到起始时间之间相隔的天数
     * @param startDay
     * @param endDay
     * @return
     */
    @JvmStatic
    fun intervalDays(startDay: String?, endDay: String?): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        //跨年不会出现问题
        var fDate: Date? = null
        var days = 0
        try {
            fDate = sdf.parse(startDay)
            val oDate = sdf.parse(endDay)
            days = ((oDate.time - fDate.time) / (1000 * 3600 * 24)).toInt()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return days
    }

    @JvmStatic
    fun isInTime(startTime: String?, curTime: String?, endTime: String?): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val now = sdf.parse(curTime).time
            val start = sdf.parse(startTime).time
            val end = sdf.parse(endTime).time
            return if (end < start) {
                now !in end..start
            } else {
                now in start..end
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

    @JvmStatic
    fun updateTime(dateStr: String, dateNum: Int): String {
        var d1: Date? = null
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            d1 = sdf.parse(dateStr) // 定义起始日期
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (d1 != null) {
            val calendar = Calendar.getInstance()
            calendar.time = d1
            calendar.add(Calendar.DAY_OF_MONTH, dateNum)
            return sdf.format(calendar.time)
        }
        return dateStr
    }

    @JvmStatic
    fun updateLongDate(dateStr: String?, dateNum: Int): Long {
        var d1: Date? = null
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            d1 = sdf.parse(dateStr) // 定义起始日期
            if (d1 != null) {
                val calendar = Calendar.getInstance()
                calendar.time = d1
                calendar.add(Calendar.DAY_OF_MONTH, dateNum)
                return calendar.timeInMillis
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

    @JvmStatic
    fun isCloseEnough(timestamp1: Long, timestamp2: Long): Boolean {
        var timestamp3 = timestamp1 - timestamp2
        if (timestamp3 < 0L) {
            timestamp3 = -timestamp3
        }
        return timestamp3 < 30000L
    }

    @JvmStatic
    fun formatTimeWithoutSecond(timestamp: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return format.format(Date(timestamp))
    }

    @JvmStatic
    fun getCommentTime(context: Context, timestamp: Long): String {
        return getCommentTime(context, timestamp, sIsYxyCommentTime)
    }

    //育学园 ：时间展示：
    // 0-60秒：xx秒前
    //超过1分钟~不超过1小时：xx分钟前
    //1小时~不超过24小时：x小时xx分钟前
    //24小时~不超过72小时：x天前
    //72小时及以上：具体日期时分
    //咕咕酱 ：时间展示：
    //0-60秒：刚刚
    //超过1分钟~不超过1小时：xx分钟前
    //1小时~不超过24小时：x小时前
    //24小时~不超过72小时：x天前
    //72小时及以上：具体日期时分
    @JvmStatic
    fun getCommentTime(context: Context, timestamp: Long, isYxyFormat: Boolean): String {
        val cur = System.currentTimeMillis() / 1000
        val last = timestamp / 1000
        return if (last > cur) {
            //评论时间晚于当前时间，未来时间？
            ""
        } else if (cur - last < ONE_MINUTE_SECS && !isYxyFormat) {
            context.resources.getString(R.string.lib_comment_now)
        } else {
            var diff = cur - last
            if (diff == 0L) {
                diff = 1
            }
            if (diff < ONE_MINUTE_SECS && isYxyFormat) {
                SimpleUtil.getFormatString(context.resources.getString(R.string.lib_comment_sec), diff)
            } else if (diff < ONE_HOUR_SECS) {
                SimpleUtil.getFormatString(context.resources.getString(R.string.lib_comment_min), diff / ONE_MINUTE_SECS)
            } else if (diff < ONE_DAY_SECS) {
                if (isYxyFormat) {
                    var minute = diff % ONE_HOUR_SECS / ONE_MINUTE_SECS
                    if (minute <= 0 && diff % ONE_HOUR_SECS != 0L) {
                        minute = 1
                    }
                    SimpleUtil.getFormatString(context.resources.getString(R.string.lib_comment_hour),
                            diff / ONE_HOUR_SECS,
                            minute)
                } else {
                    SimpleUtil.getFormatString(context.resources.getString(R.string.lib_comment_hour), diff / ONE_HOUR_SECS)
                }
            } else if (diff < THREE_DAY_SECS) {
                SimpleUtil.getFormatString(context.resources.getString(R.string.lib_comment_day), diff / ONE_DAY_SECS)
            } else {
                format(timestamp)
            }
        }
    }

    @JvmStatic
    fun getHomeRefreshTimeStamp(cur: Long, prev: Long): Boolean {
        var isRefresh = false
        if (cur != 0L && prev != 0L) {
            if (Math.abs(cur - prev) > 2 * ConstantsUtil.ONE_HOUR_MS) {
                //如果距离上一次显示时间条超过2小时刷新
                isRefresh = true
            }
        }
        return isRefresh
    }

    @JvmStatic
    fun strToDateFormat(date: String?): String {
        try {
            if (TextUtils.isEmpty(date)) {
                return ""
            }
            var formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            formatter.isLenient = false
            val newDate = formatter.parse(date)
            formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return formatter.format(newDate)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return ""
    }
    @JvmStatic
    fun getUTCTime(calendar: Calendar): Date {
        //获得时区和 GMT-0 的时间差,偏移量
        val offset = calendar[Calendar.ZONE_OFFSET]
        //获得夏令时  时差
        val dstoff = calendar[Calendar.DST_OFFSET]
        calendar.add(Calendar.MILLISECOND, -(offset + dstoff))
        return calendar.time
    }

    val timeZone: Int
        get() {
            try {
                val cal = Calendar.getInstance()
                val offset = cal[Calendar.ZONE_OFFSET]
                cal.add(Calendar.MILLISECOND, -offset)
                val timeStampUTC = cal.timeInMillis
                val timeStamp = System.currentTimeMillis()
                val timeZone = (timeStamp - timeStampUTC) / (1000 * 3600)
                return timeZone.toInt()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return 0
        }

    //重置日历时间戳为当天开始时间
    @JvmStatic
    fun resetCalendarHms(calendar: Calendar) {
        calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH], 0, 0] = 0
        calendar[Calendar.MILLISECOND] = 0
    }

    //根据指定日期起始时间戳，获取对应时间
    @JvmStatic
    fun getTimestampWithStartTime(timestamp: Long): Long {
        val cur = System.currentTimeMillis()
        val now = Calendar.getInstance()
        now.timeInMillis = cur
        resetCalendarHms(now)
        return timestamp + cur - now.timeInMillis
    }
    @JvmStatic
    fun isCurrentInTimeScope(beginHour: Int, beginMin: Int, endHour: Int, endMin: Int): Boolean {
        var result = false // 结果
        val aDayInMillis = 1000 * 60 * 60 * 24.toLong() // 一天的全部毫秒数
        val currentTimeMillis = System.currentTimeMillis() // 当前时间
        val now = Time() // 注意这里导入的时候选择android.text.format.Time类,而不是java.sql.Time类
        now.set(currentTimeMillis)
        val startTime = Time()
        startTime.set(currentTimeMillis)
        startTime.hour = beginHour
        startTime.minute = beginMin
        val endTime = Time()
        endTime.set(currentTimeMillis)
        endTime.hour = endHour
        endTime.minute = endMin
        if (!startTime.before(endTime)) {
            // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis)
            result = !now.before(startTime) && !now.after(endTime) // startTime <= now <= endTime
            val startTimeInThisDay = Time()
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis)
            if (!now.before(startTimeInThisDay)) {
                result = true
            }
        } else {
            // 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime) // startTime <= now <= endTime
        }
        return result
    }

    @JvmStatic
    val timeInfoData: TimeInfo
        get() = getTimeInfoData(currentTimestamp)
    @JvmStatic
    fun getTimeInfoData(dateTime: Long): TimeInfo {
        val info = TimeInfo()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateTime //填入当前时间
        info.mYear = calendar[Calendar.YEAR] //获取年
        info.mMonth = calendar[Calendar.MONTH] + 1 //获取月份，0表示1月份
        info.mDay = calendar[Calendar.DAY_OF_MONTH] //获取当前天数
        try {
            val arr = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
            info.mWeek = arr[calendar[Calendar.DAY_OF_WEEK] - 1] //周几
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return info
    }

    /**
     * 获取时间点上个月的第一天
     * @param time
     * @return
     */
    @JvmStatic
    fun getDayOfLastMonth(time: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        cal.add(Calendar.MONTH, -1) //获取当前时间上一个月
        cal[Calendar.DAY_OF_MONTH] = cal.getActualMinimum(Calendar.DAY_OF_MONTH) //第一天
        return cal.timeInMillis
    }

    /**
     * 获取时间点下个月的第一天
     * @param time
     * @return
     */
    @JvmStatic
    fun getDayOfNextMonth(time: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        cal.add(Calendar.MONTH, 1) //获取当前时间的下一个月
        cal[Calendar.DAY_OF_MONTH] = cal.getActualMinimum(Calendar.DAY_OF_MONTH) //第一天
        return cal.timeInMillis
    }

    /**
     * 获取时间点当前月的第一天
     * @param time
     * @return
     */
    @JvmStatic
    fun getDayOfCurrentMonth(time: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        cal[Calendar.DAY_OF_MONTH] = cal.getActualMinimum(Calendar.DAY_OF_MONTH) //第一天
        return cal.timeInMillis
    }

    class TimeInfo {
        @JvmField
        var mYear = 0
        @JvmField
        var mMonth = 0
        @JvmField
        var mDay = 0
        @JvmField
        var mWeek: String? = null
    }
}