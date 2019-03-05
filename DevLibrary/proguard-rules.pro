# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\AndroidStudio_Source\InStallPath\AndroidSDK_InstallPath/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
# For more details, see
# http://developer.android.com/guide/developing/tools/proguard.html
# Add any project specific keep options here:
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#public *;}


####################################################################################################
##########################################基本属性配置###############################################
-verbose
-dontoptimize
-dontpreverify
-ignorewarnings
-optimizationpasses 5
-allowaccessmodification
-useuniqueclassmembernames
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-renamesourcefileattribute SourceFile
-dontskipnonpubliclibraryclassmembers
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*,InnerClasses,Signature,EnclosingMethod
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*



####################################################################################################
##########################################本地混淆配置###############################################
#----------------------------------android.support包中的所有类和接口---------------------------------
-dontwarn android.support.**
-keep class android.support.**{*;}
-keep interface android.support.**{*;}
-keep class * extends android.support.**{*;}
-keep class * implements android.support.**{*;}
#----------------------------------------Application与四大组件---------------------------------------
-keep class * extends android.app.Fragment{*;}
-keepnames class * extends android.app.Application{*;}
-keepclassmembernames class * extends android.app.Service{*;}
-keepclassmembernames class * extends android.content.ContentProvider{*;}
-keepclassmembernames class * extends android.content.BroadcastReceiver{*;}
-keepclassmembernames class * extends android.app.Activity{public void *(android.view.View);}
#-----------------------View控件(系统View与自定义View等等)以及上面它的OnClick函数---------------------
-keep class android.view.View{*;}
-keep class * extends android.view.View{
    *** is*();
    *** get*();
    void set*(...);
   public <init>(android.content.Context);
   public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
    public <init>(android.content.Context,android.util.AttributeSet,int,int);
}
#-------------------------------Webview和WebviewClient以及Js的交互-----------------------------------
-keep class android.webkit.WebView{*;}
-keep class * extends android.webkit.WebView{*;}
-keepclassmembers class * extends android.webkit.webViewClient{
    public void *(android.webkit.webView,java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient{
    public boolean *(android.webkit.WebView,java.lang.String);
    public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
}
-keepclassmembers class fqcn.of.javascript.interface.for.webview{
    public *;
}
#-----------------------------------------------R资源类---------------------------------------------
-keepclassmembers class **.R$*{
    public static <fields>;
}
#----------------------------------------JNI保留本地所有native方法-----------------------------------
-keepclasseswithmembernames class *{
    native <methods>;
}
#------------------------------------------------枚举类---------------------------------------------
-keepclassmembers enum *{
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#------------------------------------------禁止Log打印任何数据---------------------------------------
-assumenosideeffects class android.util.Log{
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }
#--------------------------------------------Serializable-------------------------------------------
-keepclassmembers class * implements java.io.Serializable{
    java.lang.Object readResolve();
    java.lang.Object writeReplace();
    static final long serialVersionUID;
    private void readObject(java.io.ObjectInputStream);
    private void writeObject(java.io.ObjectOutputStream);
    private static final java.io.ObjectStreamField[] serialPersistentFields;
}
#----------------------------------------------Parcelable-------------------------------------------
-keepclassmembers class * implements android.os.Parcelable{
    static ** CREATOR;
}
#-------------------------------------------MVP模式禁止混淆-----------------------------------------


#---------------------------------------------Model数据类-------------------------------------------


#----------------------------------------------反射对象类-------------------------------------------


#----------------------------------------------回调函数类-------------------------------------------




####################################################################################################
##########################################远端混淆配置###############################################
#-------------------------------------------第三方库-------------------------------------------------
#------------Zxing----------#
-dontwarn com.google.zxing.**
-keepnames class com.google.zxing.**{*;}
#-----------Pinyin4j--------#
-dontwarn com.hp.hpl.sparta.**
-keepnames class com.hp.hpl.sparta.**{*;}
#------NineOldAndroids------#
-dontwarn com.nineoldandroids.**
-keepnames class com.nineoldandroids.**{*;}
#-------PictureSelector-----#
-dontwarn com.yalantis.ucrop.**
-dontwarn com.luck.picture.lib.**
-keepnames class com.yalantis.ucrop.**{*;}
-keepnames class com.luck.picture.lib.**{*;}
#-------------Gson----------#
-keepattributes *Annotation*
-keep class sun.misc.Unsafe{*;}
#-keepnames class com.google.gson.**{*;}
-keepnames class com.google.gson.stream.**{*;}
#-----NumberProgressbar-----#
-dontwarn com.daimajia.numberprogressbar.**
-keepnames class com.daimajia.numberprogressbar.**{*;}
-keepnames interface com.daimajia.numberprogressbar.**{*;}
-keepnames class * extends com.daimajia.numberprogressbar.**{*;}
-keepnames class * implements com.daimajia.numberprogressbar.**{*;}
-keepnames interface * extends com.daimajia.numberprogressbar.**{*;}
#-----------Okhttp3---------#
-dontwarn okio.**
#-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
#------------Glide----------#
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$**{**[] $VALUES;public *;}
#----------Retrofit2--------#
-dontwarn kotlin.Unit
-dontwarn retrofit2.**
-dontwarn javax.annotation.**
-keep,allowobfuscation interface <1>
-dontwarn retrofit2.KotlinExtensions
#-if interface * {@retrofit2.http.* <methods>;}
-keep interface * {@retrofit2.http.* <methods>;}
-keepattributes Signature,InnerClasses,EnclosingMethod
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface *{@retrofit2.http.* <methods>;}
#---------OkDownload-------#
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn org.conscrypt.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keepnames class com.liulishuo.okdownload.core.connection.DownloadOkHttp3Connection
-keep class com.liulishuo.okdownload.core.breakpoint.BreakpointStoreOnSQLite{######
    public com.liulishuo.okdownload.core.breakpoint.DownloadStore createRemitSelf();
    public com.liulishuo.okdownload.core.breakpoint.BreakpointStoreOnSQLite(android.content.Context);
}
#-----------EventBus-------#
-keepattributes *Annotation*
-keep enum org.greenrobot.eventbus.ThreadMode{*;}
-keepclassmembers class *{@org.greenrobot.eventbus.Subscribe <methods>;}
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent{<init>(java.lang.Throwable);}
#------RxJava,RxAndroid----#
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field*{long producerIndex;long consumerIndex;}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef{rx.internal.util.atomic.LinkedQueueNode producerNode;}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef{rx.internal.util.atomic.LinkedQueueNode consumerNode;}