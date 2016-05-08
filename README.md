#AndroidStudio NDK开发最佳入门实践  
***  
网上一些介绍AndroidStudio NDK入门的教程，感觉都不是很完整和全面，也没有告诉初学AndroidStudio NDK的同学们一些需要注意的地方。  
本文所介绍的是在AndroidStudio上搭建最佳的NDK开发环境，给使用NDK的开发人员最大的方便。
本人AndroidStudio版本2.0。
本人Gradle版本2.10。  

##1. Android NDK开发工具准备  
***  
要进行Android NDK开发，首先要下载Android NDK开发工具。可以在AndroidStudio上面的下载，也可以自己下载好了，然后将NDK的路径设置为自己下载的Android NDK开发工具的路径。Android NDK开发工具下载地址：[http://wear.techbrood.com/tools/sdk/ndk/](http://wear.techbrood.com/tools/sdk/ndk/) 。   
![](http://i.imgur.com/4eeK5SQ.png)  

![](http://i.imgur.com/dmxBEUQ.png)  

##2. Gradle的相关配置  
***  
gradle插件不支持NDK（当然用它也可以进行NDK开发，就是非常不方便），我们需要使用gradle-experimental插件。gradle-experimental插件使用的时候与gradle插件稍微有点区别。  

###1. 我们来配置Project的build.gradle  
***  
配置Project的build.gradle很简单就是将`dependencies`中`classpath`的值改为对应的gradle-experimental插件。  
```  
    dependencies {
        classpath 'com.android.tools.build:gradle-experimental:0.7.0'
    }  
```  

###2. 我们来配置Module的build.gradle  
***  
先给出一个我配置的范例，然后说明那些需要注意的。  
```  
apply plugin: 'com.android.model.application'

model {
    android {
        compileSdkVersion = 23
        buildToolsVersion = "23.0.2"
        defaultConfig {
            applicationId "com.lavor.ndklearning"
            minSdkVersion.apiLevel 15
            targetSdkVersion.apiLevel 23
            versionCode 4
            versionName "1.0.1"
        }
    }
    android.ndk {
        moduleName "lavor"
        ldLibs.addAll(['log'])
        cppFlags.add("-std=c++11")
        cppFlags.add("-fexceptions")
        platformVersion 15
        stl 'gnustl_shared'
    }
    android.buildTypes {
        release {
            minifyEnabled = false
            proguardFiles.add(file("proguard-rules.txt"))
        }
    }
}
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:23.1.1'
    compile 'com.android.support:recyclerview-v7:23.1.1'
    compile 'com.android.support:design:23.1.1'
}  
```  
- 首先在`apply`的时候我们引入的插件名称由`'com.android.application'`变成了`'com.android.model.application'`。  
- 其次，在原来`android`的外层加入了一个`model`层次。  
- 再次，原来在`android`的里面的块，除了`defaultConfig`外，全部移除`android`块放入`model`块中与`android`并列，并且前面的名字加上`android.`。  
- 然后，`compileSdkVersion 23`与 `buildToolsVersion "23.0.2"`改成 `compileSdkVersion = 23`和`buildToolsVersion = "23.0.2"`，中间加上了`=`。  
- 其次，添加上`android.ndk`块，块中的`moduleName`表示C/C++代码打包成so文件的名字。  
- 再次，`android.buildTypes`块中的`proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'`改成`proguardFiles.add(file("proguard-rules.txt"))`。  
- 最后，注意`dependencies`块依然在最外层，它不在`model`块中。  

##3. 开始NDK之旅  
***  
- 首先，在Android程序中添加一个native方法：`public native String getString();`。  
![](http://i.imgur.com/AZeUieV.png)
- 其次，我们注意到添加的native方法的方法名是红色的，将鼠标移动到方法名上（注意不能是括号里面或者后面），然后按下`Alt+Enter`快捷键，弹出一些解决的方法建议，点击第一个`Create Function...`。  
![](http://i.imgur.com/N4R3AIS.png)  
- 再次，此时会自动建立一个与java目录同级的jni目录，在jni目录自动建立一个c文件，在c文件中实现刚才的native方法。
![](http://i.imgur.com/83nPtPW.png)  
- 然后，稍稍修改一下c文件中实现的native方法。
```  
JNIEXPORT jstring JNICALL
Java_com_lavor_ndklearning_MainActivity_getString(JNIEnv *env, jobject instance) {
    // TODO
    return (*env)->NewStringUTF(env, "AndroidStudio NDK开发最佳入门实践");
}  
```  
- 最后在Android程序中添加上加载so库文件的代码。  
```  
       static {
        System.loadLibrary("lavor");
       }  
```  

运行程序后界面如下：  
![](http://i.imgur.com/msEDZyZ.png)  

AndroidStudio NDK开发入门介绍完毕。程序源代码下载地址：[https://github.com/lavor-zl/NDKLearning](https://github.com/lavor-zl/NDKLearning)  

**注意：gradle的版本不能太低，不然会出错，AndroidStudio版本也不能太低不然可能没有集成C/C++编译器，某些1.7的JDK可能会出错，重新下载一个JDK就好了，并设置JDK的路径。**
