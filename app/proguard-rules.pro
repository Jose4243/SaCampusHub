# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep Retrofit interfaces and Gson model classes so reflection-based
# (de)serialization continues to work correctly when minification is enabled.
-keepattributes Signature
-keepattributes *Annotation*

-keep class com.mzansi.campushub.CampusEvent { *; }
-keep interface com.mzansi.campushub.ApiService { *; }

-dontwarn okhttp3.**
-dontwarn retrofit2.**
