# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/adiksonline/bin/android-sdk-linux/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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

-dontskipnonpubliclibraryclasses
-dontobfuscate
-forceprocessing
-optimizationpasses 5

-keep class * extends android.app.Activity
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

# Libraries
-keep class com.facebook.** { *; }
-keep class com.androidquery.** { *; }
-keep class com.google.** { *; }
-keep class org.acra.** { *; }
-keep class org.apache.** { *; }
-keep class com.mobileapptracker.** { *; }
-keep class com.nostra13.** { *; }
-keep class net.simonvt.** { *; }
-keep class android.support.** { *; }
-keep class com.nnacres.app.model.** { *; }


# Suppress warnings if you are NOT using IAP:
 -dontwarn com.nnacres.app.**
 -dontwarn com.androidquery.**
 -dontwarn com.google.**
 -dontwarn org.acra.**
 -dontwarn org.apache.**
 -dontwarn com.mobileapptracker.**
 -dontwarn com.nostra13.**
 -dontwarn net.simonvt.**
 -dontwarn android.support.**


 -keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }


# The official support library.
#-keep class android.support.v4.app.** { *; }
#-keep interface android.support.v4.app.** { *; }

#  Library JARs.
#-keep class de.greenrobot.dao.** { *; }
#-keep interface de.greenrobot.dao.** { *; }

# Library projects.
#-keep class com.actionbarsherlock.** { *; }
#-keep interface com.actionbarsherlock.** { *; }

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn com.koushikdutta.**

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
