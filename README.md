# study
asm javassist kotlin
#如何关闭monkey
adb shell
ps | grep monkey
kill pid(前面会输出)
#monkey事件
LauncherActivity(pkg_name,cl_name)：启动应用的Activity。参数：包名和启动的Activity。

Tap(x,y,tapDuration)：模拟一次手指点击事件。参数：x,y为控件坐标，tapDuration为点击的持续时间

UserWait(sleeptime)：休眠一段时间

DispatchPress(keyName)：按键。参数：keycode。

DIspatchString(input)：输入字符串。

DispatchFilp(true/false)：打开或关闭软键盘

PressAndHold(x,y,pressDuration)：模拟长按事件

Drag(xStart,yStart,xEnd,yEnd,stepCount)：用于模拟一个拖拽操作。

PinchZoom(x1Start,y1Strat,x1End,y1End,x2Start,y2Strat,x2End,y2End,stepCount)：模拟缩放手势
#monkey KEYCODE [https://blog.csdn.net/c_z_w/article/details/52623981]
