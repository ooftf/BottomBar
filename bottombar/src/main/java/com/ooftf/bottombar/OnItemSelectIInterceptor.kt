package com.ooftf.bottombar

interface OnItemSelectIInterceptor {
    /**
     * 返回ture:拦截
     * 返回false:不拦截
     */
    fun onIntercept(oldIndex:Int,newIndex:Int):Boolean
}