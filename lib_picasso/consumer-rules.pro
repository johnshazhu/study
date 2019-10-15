# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

-keep class com.third.image.picasso.PicassoUtil$**{*;}
-keep class com.third.image.picasso.PicassoUtil{*;}
-keep class com.squareup.picasso.Picasso{*;}
-keep class com.squareup.picasso.Picasso$Builder{*;}
-keep interface com.squareup.picasso.Callback{*;}
-keep class com.squareup.picasso.RequestCreator{*;}