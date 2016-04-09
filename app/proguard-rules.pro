# Okhttp, Picasso
-dontwarn com.squareup.**
# Google Guava
-dontwarn com.google.common.**
# Okio
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Required for Google Play Services (see http://developer.android.com/google/play-services/setup.html)
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}
-keep class com.google.android.gms.gcm.**{ *; }


-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

-keepattributes *Annotation*
-keep public class * extends android.support.design.widget.CoordinatorLayout.Behavior { *; }
-keep public class * extends android.support.design.widget.ViewOffsetBehavior { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class com.bumptech.glide.integration.okhttp.OkHttpGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;}

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Google Play Services library
-dontwarn com.google.android.gms**
-keep interface com.google.android.gms.ads.** { *; }
-keep public class com.google.android.gms.common.ConnectionResult { public *;}
-keep public class com.google.android.gms.common.GooglePlayServicesUtil { public *;}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable { public static final *** NULL; }

#Additional Settings:
-dontwarn org.apache.http.**
-dontwarn android.net.**
