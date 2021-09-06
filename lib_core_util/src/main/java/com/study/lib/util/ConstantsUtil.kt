package com.study.lib.util

interface ConstantsUtil {
    enum class BottleType {
        DEFAULT, FORMULA, BREASTFEED
    }

    enum class NoteListType {
        FAVORITE, PRAISED, MINE, OTHER_USER
    }

    companion object {
        const val USER_ID_TAG = "user_id_tag_"
        const val CHILD_ID_TAG = "child_id_tag_"
        const val PRENATAL_EXAM_RECORD_TAG = "_prenatal"
        const val BIND_USER_ID_TAG = "bind_user_id_"
        const val MINE_TAB_SHOW_FLAG = "_mine_tab"
        const val MY_BEAN_SHOW_FLAG = "_my_bean"
        const val WEB_TITLE_STYLE_WHITE = "1"
        const val WEB_TITLE_STYLE_DEFAULT = "0"
        const val SCHEME_EVENT_TYPE = "yxy_src"
        const val HEADER_CROP = "headerCrop"
        const val HEADER_CROP_W = "width"
        const val HEADER_CROP_H = "height"
        const val TYPE_SAME_AGE = 101
        const val TYPE_HOT_TOPIC = 102
        const val TAG_ADD_VIDEO = "select_video"
        const val TAG_ADD_PHOTO = "select_photo"
        const val TAG_ADD_TAG = "add_tag"
        const val DEFECATE_TAG = "Defecate"
        const val SYMPTOM_TAG = "Symptom"
        const val FIX_TALENT_KNOWLEDGE_ID = 6090
        const val ONE_HOUR_MS: Long = 3600000
        const val ONE_MIN_MS: Long = 60000
        const val FIVE_MIN_MS: Long = 300000
        const val MAX_SLEEP_MS = 14 * ONE_HOUR_MS
        const val ONE_DAY_MS = 24 * ONE_HOUR_MS
        const val ONE_YEAR_MS = 365 * ONE_DAY_MS
        const val XMEN = 0
        const val MALE = 1
        const val FEMALE = 2
        const val PREGNANT_DAYS = 280
        const val SEQ_HEIGHT = 1
        const val SEQ_WEIGHT = 2
        const val SEQ_HEAD = 3
        const val SEQ_BREASTFEED_FORMULA = 4
        const val SEQ_BREASTFEED = 5
        const val SEQ_BOTTLE_BREASTFEED = 6
        const val SEQ_BOTTLE_FORMULA = 7
        const val SEQ_FOOD_TOTAL = 8
        const val SEQ_FOOD = 9
        const val SEQ_DEFECATE_TOTAL = 10
        const val SEQ_DEFECATE = 11
        const val SEQ_SLEEP = 12
        const val SEQ_TEMP = 13
        const val SEQ_SYMPTOM = 14
        const val SEQ_MEDICINE = 15
        const val TYPE_1SECOND = 0
        const val TYPE_BREASTFEED_FORMULA = -1
        const val TYPE_UNDEFINED = 0
        const val TYPE_BREASTFEED = 1
        const val TYPE_BOTTLE_FORMULA = 2
        const val TYPE_BOTTLE_BREASTFEED = 3
        const val TYPE_ADDITIONAL = 4
        const val TYPE_DEFECATE = 5
        const val TYPE_SLEEP = 6
        const val TYPE_GROW = 7
        const val TYPE_PHOTO = 8
        const val TYPE_WAKE = 9
        const val TYPE_DOC_INIT = 10
        const val TYPE_SPECIAL_DAY = 11
        const val TYPE_FEED = 1000
        const val TYPE_MEDICINE = 12
        const val TYPE_SYMPTOM = 13
        const val TYPE_ADD_MEDICINE = 14
        const val TYPE_TEMPERATURE = 15
        const val MEDICINE_END = 16
        const val TYPE_PREGNANCY_1SECOND = 49
        const val TYPE_PREGNANCY_START = 50
        const val TYPE_PREGNANCY_WEIGHT = 50
        const val TYPE_PREGNANCY_MEDICINE = 51
        const val TYPE_PREGNANCY_BEAT = 52
        const val TYPE_PREGNANCY_FEELING = 53
        const val TYPE_KNOWLEDGE = 2999
        const val TYPE_COUP = 3000
        const val TYPE_RECIPE = 3001
        const val TYPE_CONSULT = 3002
        const val TYPE_NOTE = 3003
        const val TYPE_AUDIO = 3004
        const val TYPE_LECTURE = 3005
        const val TYPE_FEEDBACK = 3004
        const val TYPE_COMMENT = 3006
        const val TYPE_HOME_HEADER_IMG = 3000
        const val TYPE_SHORT_VIDEO = 4000
        const val TYPE_SHORT_VIDEO_COVER = 4001

        //DayLog
        const val STATUS_NORMAL = 0
        const val STATUS_ADD = 1
        const val STATUS_UPDATE = 2
        const val STATUS_DELETE = 3
        const val STATUS_UPLOADING = 4
        const val STATUS_UPLOAD_FAILED = 5
        const val STATUS_UPDATE_ID = 6

        //SimpleEditType
        const val EDIT_NICKNAME = 0
        const val EDIT_BABYNAME = 1
        const val EDIT_SIGNATURE = 2

        //MessageType
        const val MSG_TYPE_HOT_JUMP = -2
        const val MSG_TYPE_JUMP = -1
        const val MSG_TYPE_WARNING = 0
        const val MSG_TYPE_COUP_PRAISE = 1
        const val MSG_TYPE_COUP_COMMENT = 2
        const val MSG_TYPE_REPLY_COMMENT = 3
        const val MSG_TYPE_COUP_ADDED = 4
        const val MSG_TYPE_PERSONAL_KNOWLEDGE = 6
        const val MSG_TYPE_PERSONAL_COUP = 7
        const val MSG_TYPE_PERSONAL_WAP = 8
        const val MSG_TYPE_PERSONAL_NULL = 9
        const val MSG_TYPE_RECIPE_PRAISED = 10
        const val MSG_TYPE_RECIPE_COMMENTED = 11
        const val MSG_TYPE_RECIPE_COMMENT_REPLIED = 12
        const val MSG_TYPE_PREGNANCY_EXPIRED = 13
        const val MSG_TYPE_VACCINE_EXPIRED = 14
        const val MSG_TYPE_NOTE_COMMENT = 15
        const val MSG_TYPE_NOTE_PRAISED = 16
        const val MSG_TYPE_COMMENT_PRAISED = 17
        const val MSG_TYPE_TEST = 18
        const val MSG_TYPE_FOLLOW = 19
        const val MSG_TYPE_AUDIO_COMMENT = 21
        const val MSG_TYPE_LECTURE_COMMENT_PRAISED = 22
        const val MSG_TYPE_LECTURE_COMMENT_REPLIED = 23
        const val MSG_TYPE_DISCUSS_COMMENT_REPLIED = 24
        const val MSG_TYPE_DISCUSS_COMMENT = 25
        const val MSG_TYPE_DISCUSS_COMMENT_PRAISED = 26
        const val MSG_TYPE_PRENATAL_EXAM_COMMENT = 27
        const val MSG_TYPE_PRENATAL_EXAM_COMMENT_REPLY = 28
        const val MSG_TYPE_PRENATAL_EXAM_COMMENT_PRAISED = 29
        const val MSG_TYPE_PUSH_LOCAL_KNOWLEDGE = 51 //------对应local_type 1，type 1
        const val MSG_TYPE_PUSH_LOCAL_COUP = 61 //------对应local_type 1，type 2
        const val MSG_TYPE_PUSH_LOCAL_COURSE = 62 //------对应local_type 1，type 3
        const val MSG_TYPE_PUSH_LOCAL_NOTE = 63 //------对应local_type 1，type 4
        const val MSG_TYPE_PUSH_LOCAL_EVALUATION = 64 //------对应local_type 1，type 5
        const val MSG_TYPE_PUSH_LOCAL_LECTURE = 65 //------对应local_type 1，type 6
        const val MSG_TYPE_PUSH_LOCAL_VOICE = 66 //------对应local_type 1，type 7
        const val MSG_TYPE_PUSH_LOCAL_RECIPES = 67 //------对应local_type 1，type 8
        const val MSG_TYPE_PUSH_WEBVIEW = 72 //------对应local_type 2
        const val MSG_TYPE_PUSH_VIP_BUY = 81 //------对应local_type 3

        //0、不显示 1、仅提示上传 2、上传中、3、无记录
        const val RECROD_NOTICE_HIDE = 0
        const val RECROD_NOTICE_UPLOADING = 1
        const val RECROD_NOTICE_SHOW = 2
        const val RECROD_NOTICE_EMPTY = 3

        //1知识 2妙招 3 专题 4 wap页 5 排行榜 6 课程
        const val BANNER_KNOWLEDGE = 1
        const val BANNER_COUP = 2
        const val BANNER_SPEC = 3
        const val BANNER_WEB = 4
        const val BANNER_BOARD = 5
        const val BANNER_COURSE = 6
        const val BANNER_LECTURE = 7
        const val BANNER_VIP_BUY = 8
        const val BANNER_NOTE = 9
        const val BANNER_VOICE = 10
        const val BANNER_RECIPES = 11
        const val BANNER_EVALUATION = 12
        const val BANNER_TASK = 13
        const val BANNER_DISCUSS = 14
        const val BANNER_SIGN = 15
        const val BANNER_GOODS_DETAIL = 16

        //知识类型
        const val KNOWLEDGE_MONTH = 1
        const val KNOWLEDGE_FIX = 3
        const val KNOWLEDGE_AUDIO = 5
        const val KNOWLEDGE_TOPIC = 7

        //默认头像地址
        const val DEFAULT_HEAD_URL = "http://qn.ivybaby.me/img/userhead.png"
        const val MAX_DISPLAY_HOT_COUP_COUNT = 5
        const val MAX_DISPLAY_RECORD_TIP_COUNT = 5
        const val CID_PREGNANCY_LIFE = 57
        const val CID_PREGNANCY_GROW = 58
        const val CID_PREGNANCY_EAT = 59
        const val CID_PREGNANCY_START = CID_PREGNANCY_LIFE
        const val CID_PREGNANCY_END = CID_PREGNANCY_EAT + 1
        const val LOGIN_NORMAL = 1
        const val LOGIN_WEIXIN = 2
        const val LOGIN_WEIBO = 3
        const val LOGIN_QQ = 4
        const val HOME_CACHE_FILE = "home_cache"
        const val HOME_ALERT_CACHE_FILE = "home_alert_cache"
        const val MINE_CACHE_FILE = "mine_cache"
        const val FOLLOW_USERS_CACHE_FILE = "follow_user_cache"
        const val FOLLOW_CONTENT_CACHE_FILE = "follow_content_cache"
        const val HOME_TODAY_FILE = "home_today_cache"
        const val HOME_FOOD_FILE = "home_food_cache"
        const val HOME_GROWTH_FILE = "home_growth_cache"
        const val HOME_NUTRITION_FILE = "home_nutrition_cache"
        const val HOME_ANTENATAL_FILE = "home_antenatal_cache"
        const val HOME_WEEK_FILE = "home_nutrition_week_cache"
        const val HOME_TOOL_FILE = "home_tool_cache"
        const val STARTUP_CACHE_FILE = "startup_cache"
        const val ALL_TOOL_FILE = "all_tool_cache"
        const val HOME_HOT_FILE = "home_hot_cache"
        const val HOME_DY_CARD_FILE = "home_dy_card_cache"
        const val HOME_SELECTION_FILE = "home_selection_cache_new"
        const val HOME_FOLLOW_FILE = "home_follow_cache"
        const val HOME_HEADER_FILE = "home_header_cache"
        const val DY_ITEM_CLICK_FILE = "dy_item_click_cache"
        const val DIRECTION_DOWN = 1
        const val DIRECTION_UP = 2
        const val LECTURE_MSG_TXT = 0
        const val LECTURE_MSG_AUDIO = 1
        const val LECTURE_MSG_IMAGE = 2
        const val PUSH_SWITCH = "push_switch_on"
        const val BABY_TWO_MONTH_DAYS = 61
        const val BABY_ONE_YEAR_HALF_DAYS = 547
        const val ADD_PRENATAL_EXAM_IMAGE_RECORD = 0
        const val UPDATE_PRENATAL_EXAM_IMAGE_RECORD = 1
        const val DELETE_PRENATAL_EXAM_IMAGE_RECORD = 2
        const val RED_DOT_COUPON = "coupon"
        const val RED_DOT_MINE = "me"
        const val API_TYPE_NONE = 0 //无api
        const val API_TYPE_KNOWLEDGE_HOME_RECOM = 1 //知识首页推荐
        const val RIGHT_BUTTON_STRING_VIP_CODE_ACTIVATE = "会员码激活"
        const val ONE_SECOND_DIR = "OneSecond"
        const val ONE_SECOND_EXPORT_DIR = "yuxueyuan"
        const val PAGE_SIZE = 30
        const val PAGE_SIZE_20 = 20
        const val PAGE_SIZE_10 = 10
        const val PAGE_SIZE_5 = 5
        const val TYPE_KNOWLEGE = "KNWL"
        const val TYPE_VOICE = "VOCE"
        const val MSG_TAB_REPLY_TO_ME = "reply"
        const val MSG_TAB_LIKE_ME = "like"
        const val MSG_TAB_FOLLOW_ME = "follow"
        const val MSG_TAB_SYSTEM = "system"
        const val MSG_TAB_GIFT = "gift"
        const val LENGTH_NICKNAME = 5
        const val FOREGROUND_SPLASH_AD = "foreground_splash_ad"
        const val KNOWLEDGE_VIDEO_VOLUME_MUTE = "knowledge_video_volume"
        const val KNOWLEDGE_VIP_VOLUME_MUTE = "vip_volume"
        const val RELATED_KNOWLEDGE = "related_knowledge"
        const val GUIDANCE_VIDEO_SKIP_SWITCH = "guidance_video_skip_switch"
        const val HUAWEI_AUDIT_SWITCH = "huawei_audit_switch"
        const val TASK_HINT_SPACE_TYPE = "task_hint_space_type"
        const val DEF_TYPE = 0
        const val ONE_SECOND_TYPE = 1
        const val COURSE_TYPE_ALL = 0 // 全部
        const val COURSE_TYPE_PURCHASED = 1 //已购
        const val LENGTH_HOT_SEARCH_KEY = 20

        const val PAY_BIZ_TYPE_VIP = 1
        const val PAY_BIZ_TYPE_CONSULT = 2
        const val PAY_BIZ_TYPE_YMALL_GOODS = 3
        const val PAY_BIZ_TYPE_CLINIC = 4
        const val PAY_BIZ_TYPE_EDU = 5
        const val PAY_BIZ_TYPE_CONTENT = 6 //单独购买会员相关内容

        const val PAY_BIZ_TYPE_VIP_AND_CONTENT = 7 //买会员同时购买相关内容
    }
}