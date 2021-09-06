package com.study.lib.ui

interface UIUpdateListener {
    /**
     * 网络连接异常时UI展示接口
     *
     * @param isConnected true 服务器连接失败, false 无网络
     */
    fun showConnectExceptionView(isConnected: Boolean) {}

    /**
     * 服务端无数据返回时UI展示接口
     */
    fun showEmptyContentView() {}

    /**
     * 展示业务错误页面
     *
     * @param bsCode 错误业务码
     * @param msg    错误消息
     */
    fun showBusinessErrorView(bsCode: String?, msg: String?) {}

    /**
     * 隐藏刷新或无数据页面
     */
    fun hideRefreshView() {}

    /**
     * 无网时上拉下拉刷新处理
     */
    fun releaseToRefresh() {}

    /**
     * 请求取消后处理
     */
    fun canceled() {}
}