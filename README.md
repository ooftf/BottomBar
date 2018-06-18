[![](https://jitpack.io/v/ooftf/BottomBar.svg)](https://jitpack.io/#ooftf/BottomBar)
# BottomBar
一个底部导航栏，和主流的区别在于样式需要自己定义，这样虽然使用起来比较麻烦，但是扩展性也比较好
暂时扩展属性比较少，后续可能会继续扩展细节属性
## 使用方式
### Gradle
``` Gradle
#### Project
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
#### Module
dependencies {
    ...
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'com.github.ooftf:BottomBar:1.1.0'
    implementation "com.android.support:appcompat-v7:$support"
}
```
### XML
```xml
<com.ooftf.bottombar.BottomBar
        android:background="@color/background"
        android:elevation="20dp"
        android:id="@+id/bottomBar"
        android:layout_width="match_parent"
        android:layout_height="@dimen/layout_appbar_height"

```
## 属性和方法
|方法名|描述|
|---|---|
|setAdapter|设置适配器|
|onItemSelectChangedListener|设置改变监听，如果只是点击但是选中项并没有改变，不会回调此方法|
|setSelectedIndex|切换到制定Item|
|setOnItemSelectIInterceptor|设置点击事件的拦截器|
|setOnItemRepeatListener|设置重复点击事件监听|
#### 监听事件的优先判断顺序
setOnItemRepeatListener->setOnItemSelectIInterceptor->onItemSelectChangedListener

