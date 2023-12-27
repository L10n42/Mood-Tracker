# ProGuard rules:
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-keepattributes Signature

-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

-keep class java.time.LocalDate { *; }
-keep class com.kappdev.moodtracker.domain.model.** { *; }