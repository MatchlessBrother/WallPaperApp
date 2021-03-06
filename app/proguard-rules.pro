# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
# For more details, see
# http://developer.android.com/guide/developing/tools/proguard.html
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable
# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


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
-keep class company.petrifaction.boss.ui.main.activity.model.**{*;}
-keepclassmembernames class company.petrifaction.boss.ui.main.activity.view.**{*;}
-keepclassmembernames class company.petrifaction.boss.ui.main.activity.presenter.**{*;}
-keepclassmembernames interface company.petrifaction.boss.ui.main.activity.view_v.**{*;}
#---------------------------------------------Model数据类-------------------------------------------
-keep class company.petrifaction.boss.bean.**{*;}
#----------------------------------------------反射对象类-------------------------------------------


#----------------------------------------------回调函数类-------------------------------------------


#-------------------------------------------------其他----------------------------------------------
-keep class company.petrifaction.boss.widget.**{*;}
-keepclassmembernames class company.petrifaction.boss.network{*;}
-keepclassmembernames class company.petrifaction.boss.base.**{*;}
-keepclassmembernames class company.petrifaction.boss.util.**{*;}
-keepclassmembernames class company.petrifaction.boss.adapter.**{*;}
-keepclassmembernames interface company.petrifaction.boss.network{*;}




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
-keep class sun.misc.Unsafe {*;}
#-keepnames class com.google.gson.**{*;}
-keepnames class com.google.gson.stream.**{*;}
#---PermissionsDispatcher---#
-dontwarn permissions.dispatcher.**
-keep class permissions.dispatcher.**{*;}
-keep interface permissions.dispatcher.**{*;}
#--------FlycoDialog--------#
-dontwarn com.flyco.**
-keepnames class com.flyco.**{*;}
-keepnames interface com.flyco.**{*;}
-keepnames class * extends com.flyco.**{*;}
-keepnames class * implements com.flyco.**{*;}
-keepnames interface * extends com.flyco.**{*;}
#--------Immersionbar-------#
-dontwarn com.gyf.barlibrary.**
-keepnames class com.gyf.barlibrary.**{*;}
-keepnames interface com.gyf.barlibrary.**{*;}
-keepnames class * extends com.gyf.barlibrary.**{*;}
-keepnames class * implements com.gyf.barlibrary.**{*;}
-keepnames interface * extends com.gyf.barlibrary.**{*;}
#-----NumberProgressbar-----#
-dontwarn com.daimajia.numberprogressbar.**
-keepnames class com.daimajia.numberprogressbar.**{*;}
-keepnames interface com.daimajia.numberprogressbar.**{*;}
-keepnames class * extends com.daimajia.numberprogressbar.**{*;}
-keepnames class * implements com.daimajia.numberprogressbar.**{*;}
-keepnames interface * extends com.daimajia.numberprogressbar.**{*;}
#-----AndroidAutoSize-------#
-dontwarn me.jessyan.autosize.**
-keepnames class me.jessyan.autosize.**{*;}
-keepnames interface me.jessyan.autosize.**{*;}
-keepnames class * extends me.jessyan.autosize.**{*;}
-keepnames class * implements me.jessyan.autosize.**{*;}
-keepnames interface * extends me.jessyan.autosize.**{*;}
#---------PhotoView---------#
-dontwarn com.github.chrisbanes.photoview.**
-keepnames class com.github.chrisbanes.photoview.**{*;}
-keepnames interface com.github.chrisbanes.photoview.**{*;}
-keepnames class * extends com.github.chrisbanes.photoview.**{*;}
-keepnames class * implements com.github.chrisbanes.photoview.**{*;}
-keepnames interface * extends com.github.chrisbanes.photoview.**{*;}
#---------Bga-Banner--------#
-dontwarn cn.bingoogolapple.bgabanner.**
-keepnames class cn.bingoogolapple.bgabanner.**{*;}
-keepnames interface cn.bingoogolapple.bgabanner.**{*;}
#-----------Okhttp3---------#
-dontwarn okio.**
#-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
#------------Glide-----------#
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$**{**[] $VALUES;public *;}
#---Glide-Transformations----#
-dontwarn jp.wasabeef.glide.transformations.**
-keepnames class jp.wasabeef.glide.transformations.**{*;}
-keepnames class * extends jp.wasabeef.glide.transformations.**{*;}
#----------Retrofit2---------#
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
#---------OkDownload--------#
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
#--BaseRecyclerViewAdapterHelper--#
-dontwarn com.chad.library.adapter.**
-keep class com.chad.library.adapter.**{*;}
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keepclassmembers class **$** extends com.chad.library.adapter.base.BaseViewHolder{<init>(...);}
#-----------EventBus--------#
-keepattributes *Annotation*
-keep enum org.greenrobot.eventbus.ThreadMode{*;}
-keepclassmembers class *{@org.greenrobot.eventbus.Subscribe <methods>;}
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent{<init>(java.lang.Throwable);}
#------RxJava,RxAndroid-----#
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field*{long producerIndex;long consumerIndex;}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef{rx.internal.util.atomic.LinkedQueueNode producerNode;}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef{rx.internal.util.atomic.LinkedQueueNode consumerNode;}