package com.study.lib.api

import com.study.lib.util.SimpleUtil

object APIConfig1 {
    private const val CONFIG_NET_BASE = "net_base"
    private const val CONFIG_NEW_API_PATH = "new_api_path"

    private const val CONFIG_WAP_BASE = "wap_base"
    private const val CONFIG_PUSH_BASE = "push_base"
    private const val CONFIG_WEI_XIN_BASE = "weixin_base"
    private const val CONFIG_NEW_UPLOAD_BASE = "new_upload_base"
    private const val CONFIG_WEB_IM_BASE = "webim_base"
    private const val CONFIG_NET_COLLECT = "net_collect"
    private const val CONFIG_MALL_URL_BASE = "mall_url_base"
    private const val CONFIG_SNAPSHOT_LECTURE_BASE = "snapshoot_lecture_base"
    private const val CONFIG_NEW_API_BASE_HOST = "new_api_base_host"
    const val CONFIG_APP_CODE = "appcode"

    val NET_BASE = SimpleUtil.getPropertyValue(CONFIG_NET_BASE)
    val BASE = "$NET_BASE${SimpleUtil.getPropertyValue(CONFIG_NEW_API_PATH)}"
    val WAP_BASE = SimpleUtil.getPropertyValue(CONFIG_WAP_BASE)
    val PUSH_BASE = SimpleUtil.getPropertyValue(CONFIG_PUSH_BASE)
    val NEW_UPLOAD_BASE = SimpleUtil.getPropertyValue(CONFIG_NEW_UPLOAD_BASE)
    val WEB_IM_BASE = SimpleUtil.getPropertyValue(CONFIG_WEB_IM_BASE)
    val NETWORK_COLLECT_BASE = SimpleUtil.getPropertyValue(CONFIG_NET_COLLECT)
    val MALL_URL_BASE = SimpleUtil.getPropertyValue(CONFIG_MALL_URL_BASE)
    val SNAPSHOT_LECTURE_BASE = SimpleUtil.getPropertyValue(CONFIG_SNAPSHOT_LECTURE_BASE)
    val NEW_API_BASE_HOST = SimpleUtil.getPropertyValue(CONFIG_NEW_API_BASE_HOST)
    val WEI_XIN_BASE = SimpleUtil.getPropertyValue(CONFIG_WEI_XIN_BASE)

    var IM_BASE = "$BASE/consult"
    var COURSE_BASE = "$BASE/course"
    var USER_INFOR_BASE = BASE
    var WAP_APPVIEW_BASE = "/yxy-view/appview"
    var FANS_BASE = "$WAP_BASE$WAP_APPVIEW_BASE/fansking"
    var COUP_BASE = "$BASE/coup"
    var SNS_BASE = BASE
    var VIP_BASE = BASE
    var VIP_OLD_BASE = "$BASE/vipOld"
    var FEEDBACK_BASE = BASE
    var LECTURE_BASE = "$BASE/lecture"
    var RECIPE_BASE = BASE
    var AUDIO_INVITE_SHARE_CALLBACK = "$BASE/audioTreat/treatNotify"
    var PREGNANCY_BASE = BASE
    var NEW_API_BASE = BASE
    var YMALL_BASE = "$WAP_BASE$WAP_APPVIEW_BASE/goodsMall"
    var YMALL_TASK_BASE = "$WAP_BASE$WAP_APPVIEW_BASE/taskMall"
    var YMALL_RECORD_BASE = "$WAP_BASE$WAP_APPVIEW_BASE/myOrderList"

    var THANKS_DR_CUI_BASE = "$WEI_XIN_BASE$WAP_APPVIEW_BASE"
    var VIP_GATEWAY = "$NET_BASE/yxy-vip-gateway/api/json"
    var VIP_VCOURSE_SHARE = "$WEI_XIN_BASE/yxy-view/appview/vclassShare?courseActivityId="
    var VIP_LECTURE_BASE = "$VIP_GATEWAY/lecture"

    var YXY_REGISTER_PROTOCOL = "$WEI_XIN_BASE$WAP_APPVIEW_BASE/yxyvisions"

    var BABY_LOG = "$BASE/babyLog"
    var RECORD_BASE = "$BABY_LOG/getdaylog"
    var RECORD_BASE_PREGNANCY = "$BASE/gestationLog"
    var RECORD_PREGNANCY = "$RECORD_BASE_PREGNANCY/getGravidaDaylog"

    var GETS_AD = "$NEW_API_BASE/advert/getsAd"

    var GET_INTEREST_LIST = "$SNS_BASE/interest/list"
    var FOLLOW_USER = "$SNS_BASE/socialGraph/follower"
    var UNFOLLOW_USER = "$SNS_BASE/socialGraph/unFollower"
    var FOLLOW_CHANGE_TIP = "$SNS_BASE/snsUsers/newUserFollowCount"
    var SNS_FOLLOW = "$SNS_BASE/socialGraph/followList"
    var SNS_FANS = "$SNS_BASE/socialGraph/fansList"

    var BREAST_RECORD_ADD = "$BABY_LOG/addStraight"
    var BREAST_RECORD_DELETE = "$BABY_LOG/deleteStraightById"
    var BREAST_RECORD_UPDATE = "$BABY_LOG/updateStraight"
    var BREAST_RECORD_QUERY = "$BABY_LOG/findStraightById"

    var BOTTLE_ADD = "$BABY_LOG/addBottle"
    var BOTTLE_DELETE = "$BABY_LOG/deletebottle"
    var BOTTLE_UPDATE = "$BABY_LOG/updateBottle"

    var FOOD_ADD = "$BABY_LOG/addFood"
    var FOOD_DELETE = "$BABY_LOG/deleteFoodById"
    var FOOD_UPDATE = "$BABY_LOG/updateFood"
    var FOOD_QUERY = "$BABY_LOG/findFoodById"

    var SLEEP_RECORD_ADD = "$BABY_LOG/addSleep"
    var SLEEP_RECORD_DELETE = "$BABY_LOG/deleteSleepById"
    var SLEEP_RECORD_UPDATE = "$BABY_LOG/updateSleep"
    var SLEEP_RECORD_QUERY = "$BABY_LOG/findSleepById"

    var DEFECATE_ADD = "$BABY_LOG/addDefecate"
    var DEFECATE_DELETE = "$BABY_LOG/deleteDefecateById"
    var DEFECATE_UPDATE = "$BABY_LOG/updateDefecate"
    var DEFECATE_QUERY = "$BABY_LOG/findDefecateById"

    var GROW_ADD = "$BABY_LOG/addGrow"
    var GROW_DELETE = "$BABY_LOG/deleteGrow"
    var GROW_UPDATE = "$BABY_LOG/updateGrow"
    var GROW_QUERY = "$BABY_LOG/findGrow"

    var PHOTOGRAPH_ADD = "$BABY_LOG/addPhotograph"
    var PHOTOGRAPH_DELETE = "$BABY_LOG/deletePhotographById"
    var PHOTOGRAPH_UPDATE = "$BABY_LOG/updatePhotograph"
    var PHOTOGRAPH_QUERY = "$BABY_LOG/findPhotographById"

    var ADD_PILL = "$BABY_LOG/addPill"
    var UPDATE_PILL = "$BABY_LOG/updatePill"
    var DELETE_PILL = "$BABY_LOG/deletePill"

    var CONFIG = "/cConfigVersion"
    var CONFIG_VERSION = "$BASE$CONFIG/getcConfigVersion"

    var USER = "/user"
    var UPGRADE_USER_INFO = "$BASE/userCenter/upgradeRebuild"
    var LOGIN = "$BASE/userCenter/loginByMobilePassword"
    var FAST_LOGIN = "$BASE/userCenter/fastRegisterByMobile"
    var IMPLICIT_LOGIN = "$BASE/userCenter/implicitLogin"
    var ALI_OAUTH_LOGIN = "$BASE/userCenter/getAlipayAuthInfo"
    var GET_VERIFY_CODE = "$BASE/v55/user/sendVerificationCode"
    var PERFECT_USERINFOR = "$BASE$USER/perfectUserInfo"
    var REGISTER = "$BASE/v55$USER/fastRegisterNew"
    var RESET_PASSWORD = "$BASE/v55$USER/resetPassword"
    var UPDATE_HEADER_IMAGE = "$BASE$USER/updateHeadImg"
    var UPDATE_NICKNAME = "$BASE$USER/updateNickname"
    var UPDATE_BABYNAME = "$BASE$USER/updateBabyname"
    var UPDATE_SEX = "$BASE$USER/updateSex"
    var UPDATE_BIRTHDAY = "$BASE/v64/user/updateBirthday"
    var INVITATION_CHECK = "$BASE$USER/checkInvitationCodeRequired"
    var INVITATION_CODE_CHECK = "$BASE$USER/checkInvitationCodeOk"
    var MOBILE_CHECK = "$BASE$USER/checkMobileRegistered"
    var VERIFYCODE_CHECK = "$BASE/v55/user/checkVerificationCodeOk"
    var GET_INVITATION_CODES = "$BASE$USER/getInvitationCode"

    var IMAGE = "/img"
    var IMAGE_UPLOAD = "$BASE$IMAGE/uploadimg"

    var HOME = "/home"
    var HOME_INDEX = "$BASE/v62_1$HOME/index"

    var SIGN_PATH = "$NEW_API_BASE/sign"
    var SIGN = "$SIGN_PATH/sign"
    var GET_SIGN_DATA = "$SIGN_PATH/getSignData"

    var YUANDOU_PATH = "$NEW_API_BASE/yuandou"
    var GET_YUANDOU_DETAIL = "$YUANDOU_PATH/getYuandouDetails"
    var YUANDOU_INDEX = "$YUANDOU_PATH/index"
    var ACCOUNT_YUANDOU_INDEX = "$YUANDOU_PATH/accountIndex"

    var YMALL_PATH = "$NEW_API_BASE/ymall"
    var GET_EXCHANGE_ORDERS = "$YMALL_PATH/getExchangeOrders"
    var EXCHANGE_GOODS = "$YMALL_PATH/exchangeGoods"
    var GET_GOODS_BTN = "$YMALL_PATH/getGoodsButton"

    var YOU_ZAN_TOKEN = "$NEW_API_BASE/youZanNew/getAuthToken"

    var GET_COUPON_LIST = "$NEW_API_BASE/coupon/getUserCouponList"
    var GET_COUPON_COUNT = "$NEW_API_BASE/coupon/getUserCouponListCount"
    var GET_COUPON_LIST_FOR_PAY = "$NEW_API_BASE/coupon/getUserAvailableCouponList"
    var GET_COUPON_COUNT_FOR_PAY = "$NEW_API_BASE/coupon/getUserAvailableCouponListCount"
    var USE_COUPON = "$NEW_API_BASE/coupon/useCoupon"
    var CHECK_COUPON = "$NEW_API_BASE/coupon/checkValidCoupon"
    var GET_RED_DOT = "$NEW_API_BASE/redDot/getModuleRedDotList"
    var ADD_ONE_SECOND = "$NEW_API_BASE/oneSecondRecord/saveOneSecondRecord"
    var DEL_ONE_SECOND = "$NEW_API_BASE/oneSecondRecord/delOneSecondRecord"
    var GET_EXPORT_NOTICE = "$NEW_API_BASE/oneSecondRecord/getOneSecondExportSummary"
    var EXPORT_ONESECOND_RECORD = "$NEW_API_BASE/oneSecondRecord/exportOneSecondRecord"

    var USER_RECIPE_LIST = "$RECIPE_BASE/v60/recipes/userrecipes"
    var FAVORITE_RECIPE_LIST = "$RECIPE_BASE/v60/recipes/mycollectionrecipes"

    var MESSAGE_UNREAD_COUNT = "$NEW_API_BASE/sysMsg/getNewMessageCount"
    var MESSAGE_REPLY_TO_ME = "$NEW_API_BASE/comment/getReplyToMeComments"
    var MESSAGE_LIKE_ME = "$NEW_API_BASE/liked/getLikedToMeList"
    var MESSAGE_FOLLOW_ME = "$NEW_API_BASE/socialGraph/newFansMessageList"
    var MESSAGE_SYSTEM_LIST = "$NEW_API_BASE/sysMsg/getMemberMsg"

    var KNOWLEDGE = "/knowledge"


    var KNOWLEDGE_DAY_URL = "$BASE/taglib/getEveryDayKnowledge"
    var KNOWLEDGE_GROUP_PATH_URL = "$BASE/taglib/getKnowledgeWithGroupByPath"
    var KNOWLEDGE_DETAIL = "$BASE$KNOWLEDGE/getKnowledgeDetail"
    var KNOWLEDGE_MIX_DETAIL = "$BASE$KNOWLEDGE/getMixKnowledgeDetail"
    var KNOWLEDGE_CATEGORY_LIST = "$COUP_BASE/v50/knowledge/findMonthCategorys"
    var KNOWLEDGE_MONTH_LIST = "$COUP_BASE/v50$KNOWLEDGE/findKnowlages"
    var KNOWLEDGE_DAY_LIST = "$COUP_BASE/v50$KNOWLEDGE/findKnowlageByBirthDay"
    var KNOWLEDGE_OTHER = "$COUP_BASE/v50$KNOWLEDGE/getmoreknowleges"

    var COUP = "/v50/coup"
    var COUP_V612 = "/v612/coup"
    var COUP_LIST_BY_KID = "$COUP_BASE$COUP_V612/findCoupPageByKid"
    var KNOWLEDGE_HOT_COUP_LIST = "$COUP_BASE$COUP_V612/findHotCoupPageByKid"
    var COUP_ADD = "$COUP_BASE/publishCoup"
    var COUP_DELETE = "$COUP_BASE/deleteCoup"
    var COUP_UPDATE = "$COUP_BASE/editCoup"
    var COUP_QUERY = "$COUP_BASE/getCoupDetail"
    var COUP_TOPIC = "$COUP_BASE/getHotOrNewCoups"
    var COUP_SELF_RECOMMEND = "$COUP_BASE/selfRecommend"
    var COUP_MINE = "$BASE/socialGraph/psersonalTimeline"

    var COLLECTION = "/v50/collection"
    var COLLECTION_V612 = "/v612/collection"
    var COLLECTION_ADD_KNOWLEDGE = "$COUP_BASE$COLLECTION/addKnowledgeCollection"
    var COLLECTION_DELETE_KNOWLEDGE = "$COUP_BASE$COLLECTION/deleteKnowledgeCollection"
    var COLLECTION_QUERY_KNOWLEDGE = "$COUP_BASE$COLLECTION/findKnowledgeCollectionPage"

    var COLLECTION_QUERY_COUP = "$COUP_BASE$COLLECTION_V612/findCoupCollectionPage"

    var VOTE = "/bTaskvote"
    var VOTE_DETAIL = "$BASE$VOTE/findVoteDetail"
    var VOTE_SET_OPTS = "$BASE$VOTE/setVoteUser"

    var TASK = "$BASE/task"
    var TASK_TOADY_DETAIL = "$TASK/getTaskDetail"
    var TASK_COMPLETE_URL = "$TASK/completeTask"
    var TASK_LIST = "$TASK/findDayList"
    var TASK_DETAILS_LIST = "$TASK/getTaskForDate"

    var GET_QIUNIU_TOKEN = "$BASE/storage/getQiniuToken"
    var QINIU_IMAGE_BASE = "http://qn.ivybaby.me/"
    var QINIU_COURSE_IMAGE_BASE = "http://course.qn.ivybaby.me/"
    var QINIU_LECTURE_BASE = "http://lecture.qn.ivybaby.me/"
    var QINIU_VIDEO_BASE = "http://video.qn.ivybaby.me/"

    var GET_BANNER = "$BASE/v66/bBanner/getBannersNew"

    var GET_BORARD = "$BASE/v2/rankings/rankings"
    var GET_RECIPE_BOARD = "$RECIPE_BASE/v60/recipeRankings/rankings"

    var GET_KEYWORDS = "$BASE/search/getHotSearchKey"
    var GET_KEYWORDS_VIPZONE = "$NEW_API_BASE/vipHotKey/getHotSearchKey"

    var GET_PRAISE_COUP = "$COUP_BASE/v612/praise/findPraisePage"

    var SEARCH_COUP = "$BASE/search/searchCoup"
    var SEARCH_LOG_KNOWLEDGE_CLICK = "$BASE/search/addKSearchResultClickLog"
    var SEARCH_LOG_COUP_CLICK = "$BASE/search/addCSearchResultClickLog"
    var SEARCH_LOG_KNOWLEDGE_STAY = "$BASE/search/addKSearchStayLog"
    var SEARCH_LOG_COUP_STAY = "$BASE/search/addCSearchStayLog"
    var SEARCH_LOG_RECIPE_CLICK = "$BASE/search/addRSearchResultClickLog"
    var SEARCH_LOG_RECIPE_STAY = "$BASE/search/addRSearchStayLog"
    var SEARCH_GETSSUGGEST = "$BASE/search/getsSuggest"
    var SEARCH_MORE_USERS = "$BASE/search/listPageUser"

    var GET_LATEST_RECIPE_LIST = "$RECIPE_BASE/v612/recipes/getLatestRecipes"
    var SEARCH_RECIPE = "$BASE/search/searchRecipe"
    var SEARCH_RECIPE_HOT_KEYWORD = "$BASE/v4/lKeywordClick/findRHotKewords"
    var LATEST_RECIPE_HOT_KEYWORD_CONFIG = "$BASE/v4/home/recipessearchconfig"

    var RECIPE_PRAISE_ADD = "$RECIPE_BASE/v60/rPraise/addPraise"
    var RECIPE_PRAISE_CANCEL = "$RECIPE_BASE/v60/rPraise/deletePraise"

    var RECIPE_FAVORITE = "$RECIPE_BASE/v60/rCollection/rcollection"


    var PRAISED_RECIPE_LIST = "$RECIPE_BASE/v60/recipes/getMyPraiseRecipes"

    var PREGNANCY_ADD_GROW = "$RECORD_BASE_PREGNANCY/addGrow"
    var PREGNANCY_UPDATE_GROW = "$RECORD_BASE_PREGNANCY/updateGrow"
    var PREGNANCY_DELETE_GROW = "$RECORD_BASE_PREGNANCY/delGrow"

    var PREGNANCY_ADD_PHOTO = "$RECORD_BASE_PREGNANCY/addPhotograph"
    var PREGNANCY_UPDATE_PHOTO = "$RECORD_BASE_PREGNANCY/updatePhotograph"
    var PREGNANCY_DELETE_PHOTO = "$RECORD_BASE_PREGNANCY/deletePhotographById"

    var PREGNANCY_ADD_BEAT = "$RECORD_BASE_PREGNANCY/addFetalMovement"
    var PREGNANCY_UPDATE_BEAT = "$RECORD_BASE_PREGNANCY/updateFetalMovement"
    var PREGNANCY_DELETE_BEAT = "$RECORD_BASE_PREGNANCY/delFetalMovement"

    var PREGNANCY_ADD_MEDICINE = "$RECORD_BASE_PREGNANCY/addPill"
    var PREGNANCY_UPDATE_MEDICINE = "$RECORD_BASE_PREGNANCY/updatePill"
    var PREGNANCY_DELETE_MEDICINE = "$RECORD_BASE_PREGNANCY/deletePill"

    var PREGNANCY_ADD_HW = "$RECORD_BASE_PREGNANCY/addGestationHw"
    var PREGNANCY_GET_HW = "$RECORD_BASE_PREGNANCY/getGestationHw"

    var BABY_CHANGE = "$COUP_BASE/v50/knowledge/getdaychange"

    var PREGNANCY_COUP = "$COUP_BASE/v612/sCoupRecomGravida/getgravida"
    var PREGNANCY_DAY_KNOWLEDGE = "$COUP_BASE/v50/knowledge/getdayGestationKnows"
    var PREGNANCY_KNOWLEDGES = "$COUP_BASE/v50/knowledge/findKnowlagesforGestation"
    var PREGNANCY_KNOWLEDGE_CATEGORY = "$COUP_BASE/v50/knowledge/findGestationCategorys"

    var TOKEN_CHECK = "$BASE/v42/user/tokenValidate"
    var OTHER_CONTACT = "$BASE/v43/other/othercontact"

    var GET_ADDED_COURSE = "$COURSE_BASE/v67/home/courses"
    var FIND_COURSE = "$COURSE_BASE/findcourse"

    var ADD_NOTE = "$COURSE_BASE/courseNote/add"
    var NOTE_COMMENT = "$COURSE_BASE/courseNoteComment/add"
    var CANCEL_NOTE_PRAISE = "$COURSE_BASE/courseNotePraise/delete"
    var NOTE_PRAISE = "$COURSE_BASE/courseNotePraise/add"

    var GET_NOTE_DETAIL = "$COURSE_BASE/courseNote/findNoteById"
    var GET_NOTE_COMMENTS = "$COURSE_BASE/v48/courseNoteComment/findComments"
    var DELETE_NOTE_COMMENT = "$COURSE_BASE/courseNoteComment/delCommentById"
    var DELETE_NOTE = "$COURSE_BASE/courseNote/delNoteById"
    var NOTE_ACCUSATION = "$COURSE_BASE/sCourseNoteAccusation/accusatioin"
    var COLLECT_NOTE = "$COURSE_BASE/sCourseCollection/addCourseCollection"
    var COLLECT_NOTE_LIST = "$COURSE_BASE/v612/sCourseCollection/colletionCourses"
    var CANCEL_COLLECT_NOTE = "$COURSE_BASE/sCourseCollection/subCourseCollection"
    var MY_NOTE_LIST = "$COURSE_BASE/courseNote/myCourses"
    var MY_PRAISED_NOTE_LIST = "$COURSE_BASE/v612/courseNotePraise/praiseCourses"

    var GET_SELF_USERINFOR = "$USER_INFOR_BASE/v612/home/userinfo"
    var GET_USERINFOR = "$USER_INFOR_BASE/v612/home/userhome"
    var OTHER_USER_INFOR = "$USER_INFOR_BASE/v44/home/otheruserinfo"
    var VIP_CENTER = "$WAP_BASE$WAP_APPVIEW_BASE/card"
    var VIP_RECOMMEND = "$WAP_BASE$WAP_APPVIEW_BASE/recommen"
    var VIP_PRICILEGE = "$WAP_BASE$WAP_APPVIEW_BASE/priviLege"
    var THANKS_DRCUI = "$THANKS_DR_CUI_BASE/thanksDrcui"

    var FOOD_MATERIAL_CATEGORY = "$BASE/canEat/findMaterialCateg"
    var FOOD_MATERIAL_SEARCH = "$RECIPE_BASE/canEat/searchFoodMaterials"
    var FOOD_DETAIL = "$RECIPE_BASE/canEat/getFoodMaterialDetail?fmid="
    var GET_FOOD_MATERIAL_LIST = "$BASE/canEat/getallfms"
    var GET_TABOO_INFO = "$BASE/canEat/findTabooWarn"
    var GET_TABOO_LIST = "$BASE/canEat/findTaboo"

    var ADD_COUP_SHARE = "$COUP_BASE/v50/coup/addShareNum"
    var ADD_NOTE_SHARE = "$COURSE_BASE/v45/courseNote/addNoteShareCount"

    var MY_COUP_COMMENT = "$COUP_BASE/v50/comment/findMyCommentList"
    var MY_RECIPE_COMMENT = "$RECIPE_BASE/v60/rcomment/findMyCommentList"
    var MY_NOTE_COMMENT = "$COURSE_BASE/v46/courseNoteComment/findMyComments"

    var GET_DISCUSS_LIST = "$COUP_BASE/v61/talk/findTalks"
    var GET_RECORD_TIP_LIST = "$BASE/tips/findhint"

    var COMMENT_COUP_PRAISE = "$COUP_BASE/v50/comment/addPraiseComment"
    var COMMENT_COUP_CANCEL_PRAISE = "$COUP_BASE/v50/comment/subPraiseComment"

    var CONSULT_HOT_KEYWORD = "$BASE/search/getZxKeywords"
    var SEARCH_CONSULT = "$BASE/search/searchCollectionCase"

    var AUDIO_COMMENT_LIST = "$COUP_BASE/v51/kacomment/findMyAudioCommentList"
    var AUDIO_DETAIL = "$VIP_GATEWAY/audio/getVipAudioDetail"
    var AUDIO_NEXT = "$BASE/audio/getNextAudioInfo"
    var FIND_AUDIO_COMMENTS = "$COUP_BASE/v612/kacomment/findAudioCommentList"
    var DELETE_AUDIO_COMMENT = "$COUP_BASE/v51/kacomment/deleteAudioComment"
    var ACCUSATION_AUDIO_COMMENT = "$COUP_BASE/v51/sKcommentAccusation/addKaudioCommentAccusation"
    var AUDIO_COMMENT_PRAISE = "$COUP_BASE/v51/kacomment/addPraiseAudioComment"
    var AUDIO_COMMENT_CANCEL_PRAISE = "$COUP_BASE/v51/kacomment/subPraiseAudioComment"
    var ADD_AUDIO_COMMENT = "$COUP_BASE/v51/kacomment/addAudioComment"

    var AUDIO_TOPIC_DETAIL = "$VIP_GATEWAY/audioTopic/getAudioTopicDetail"
    var LECTURE_DETAIL = "$VIP_LECTURE_BASE/getLectureDetail"
    var GET_LECTURE_MSG = "$LECTURE_BASE/chatList"
    var LECTURE_PLAYBACK_ADD = "$LECTURE_BASE/addPlaybackNum"
    var LECTURE_VIDEO_URL = "$LECTURE_BASE/videoUrl"

    var CHECK_PHONE_BIND = "$USER_INFOR_BASE/v62/user/isBdSj"
    var VIP_LECTURE_CATEGORY = "$VIP_LECTURE_BASE/getLectureTagList"
    var FIND_LECTURE_BY_CATEGORY = "$VIP_LECTURE_BASE/getLectureInfoList"

    var FETAL_EDUCATION_STORY_LIST = "$PREGNANCY_BASE/story/listStoryVo"
    var PRENATAL_EXAM_DETAIL_BY_WEEK = "$PREGNANCY_BASE/bmodedata/detailDataWeek"
    var PRENATAL_EXAM_COMMENT_LIST = "$PREGNANCY_BASE/bmodedata/v6_3/findWeekDataComments"
    var PRENATAL_EXAM_ADD_COMMENT = "$PREGNANCY_BASE/bmodedata/v6_3/addWeekDataComment"
    var PRENATAL_EXAM_DEL_COMMENT = "$PREGNANCY_BASE/bmodedata/v6_3/deleteWeekDataComment"
    var PRENATAL_EXAM_COMMENT_ADD_PRAISE = "$PREGNANCY_BASE/bmodedata/v6_3/addWeekDataCommentPraise"
    var PRENATAL_EXAM_COMMENT_DEL_PRAISE = "$PREGNANCY_BASE/bmodedata/v6_3/delWeekDataCommentPraise"
    var PRENATAL_EXAM_ADD_REPORT = "$PREGNANCY_BASE/report/v6_3/addReport"
    var PRENATAL_EXAM_ADD_RECORD = "$PREGNANCY_BASE/inspection/addInspectionInfo"
    var PRENATAL_EXAM_DEL_RECORD = "$PREGNANCY_BASE/inspection/deleteInspectionInfo"
    var PRENATAL_EXAM_RECORD_LIST = "$PREGNANCY_BASE/inspection/findInspectionInfos"

    var GET_MUSIC_LIST = "$BASE/music/musiclist"
    var GET_MUSIC_DETAIL = "$BASE/music/musicinfo"

    var PRE_EXCHANGE_GOODS = "$YMALL_PATH/checkExchangeGoods"
    var GET_PRE_ORDER_INFO = "$YMALL_PATH/preGoodsOrder"
    var CREATE_GOODS_ORDER = "$YMALL_PATH/creatGoodsOrder"
    var GET_GOODS_ORDER = "$YMALL_PATH/getGoodsOrder"

    var ORDER = "/order"
    var CANCEL_GOODS_ORDER = "$NEW_API_BASE$ORDER/cancelOrder"
    var DELETE_GOODS_ORDER_RECORD = "$NEW_API_BASE$ORDER/hiddenOrder"

    var PAY = "/pay"
    var GET_WIXIN_PAY_ARGS = "$NEW_API_BASE$PAY/getWeixinAppPayArgs"
    var GET_ALI_PAY_ARGS = "$NEW_API_BASE$PAY/getOldAlipayAppPayArgs"
    var GET_HUAWEI_PAY_ARGS = "$NEW_API_BASE$PAY/getHuaweiAppPay"

    var GET_CONFIRM_ORDER = "$NEW_API_BASE/payControl/confirmOrder"
    var PAY_COMMON = "$NEW_API_BASE/payControl/pay"
    var PAY_CANCEL = "$NEW_API_BASE/payControl/cancelPay"

    var UPLOAD_IMAGE = "$NEW_UPLOAD_BASE/upload/uploadImage"
    var UPLOAD_IMAGE_NO_DNS = "$NEW_UPLOAD_BASE/upload/uploadImageToImgBucket"
    var UPLOAD_AUDIO = "$NEW_UPLOAD_BASE/upload/uploadAudio"

    var GUGUJIANG_REGISTER_PROTOCOL = "$WEI_XIN_BASE/yxy-h5/yxy-guguapp-web/tweet/trems"

    var GET_BABIES = "$BASE/yxyUser/getMemberChilds"
    var GET_GUEST_BABIES = "$BASE/yxyUser/getAccountChilds"

    var DO_LIKE = "$NEW_API_BASE/liked/doLike"
    var DO_LIKE_SWITCH = "$NEW_API_BASE/liked/doLikeSwitch"
    var CANCEL_LIKE = "$NEW_API_BASE/liked/cancelLiked"

    var ADD_COLLECTION = "$NEW_API_BASE/collection/addCollection"
    var CANCEL_COLLECTION = "$NEW_API_BASE/collection/cancelCollection"
    var LIST_COLLECTION = "$NEW_API_BASE/collection/listPageCollection"

    var GET_LIST_COUP_BY_RESOURCE = "$COUP_BASE/getCoupListByResource"
    var DAY_PUSH_MESSAGE_URL = "$SNS_BASE/index/getPushList"
    var CHATROBOT_CHECKIN_URL = "$SNS_BASE/chatrobot/welcome"
    var CHATROBOT_CHAT_SEND_URL = "$SNS_BASE/chatrobot/chat"
    var TASK_PLAN_DETAILS_URL = "$TASK/getTaskCount"
    var CHATROBOT_EVALUATE_URL = "$SNS_BASE/chatrobot/evaluateReply"
    var CHATROBOT_HISTORY_URL = "$SNS_BASE/chatrobot/getChatHis"
    var CHATROBOT_SOLVED_URL = "$SNS_BASE/chatrobot/getProblemSolved"
    var DOCUMENT_SERVICE_URL = "$SNS_BASE/document/getDocuments"

    var ENTRANCE_CONF_URL = "$SNS_BASE/ymall/entranceConfList"
    var TOPIC = "$BASE/topic"
    var GET_TOPIC_DETAIL = "$TOPIC/getTopicDetail"
    var PUBLISH_TOPIC = "$TOPIC/publishTopic"
    var GET_KNOWLEDGE_TOPIC = "$TOPIC/getTopicRelationKnwl"
    var GET_HOT_TOPICS = "$TOPIC/getHotTopics"
    var GET_CATEGORY_LIST = "$TOPIC/getCategoryList"
    var GET_TOPICS_BY_CATEGORY = "$TOPIC/getTopicsByCategory"
    var SEARCH_TOPIC = "$TOPIC/searchTopic"

    var GET_USER_COUP_RANK = "$COUP_BASE/getUserCoupRank"

    var GET_COURSE_ACTIVITY_INFO = "$VIP_GATEWAY/courseActivity/getCourseActivityInfo"
    var GET_COURSE_LESSON_INFO = "$VIP_GATEWAY/courseActivity/getCourseLessonInfo"
    var COURSE_ACTIVITY_UNLOCK = "$VIP_GATEWAY/courseActivity/courserActivityUnlock"
    var COURSE_CHECK_UNLOCK = "$VIP_GATEWAY/courseActivity/checkUserCourseUnlock"

    var GET_COURSE_INFO_LIST = "$VIP_GATEWAY/course/getCourseInfoList"
    var GET_MY_COURSE_LIST = "$BASE/courseContent/getMyCourses"
    var GET_CHOICENESS_COURSES_LIST = "$BASE/courseContent/getBestRecommendInfo"
    var GET_EARLY_COURSES_LIST = "$BASE/courseContent/getEarlyEnlightenment"
    var GET_BABY_HEALTH_RECORDS = "$BASE/babyLog/getHealthRecords"

    //直播
    var LIVE_HOME_URL = "$BASE/live/liveIndex"
    var LIVE_HISTORY_URL = "$BASE/live/liveHistory"
    var LIVE_LIVEAPPOINTMENT_URL = "$BASE/live/liveAppointment"

    var USER_TASK = "$BASE/userTask"
}

