# SignView
daily sign view with animation<br><br>
![](https://github.com/FranklinNEO/SignView/blob/master/signview-gif.gif)
#Usage
I published the library with Jitpack, so add it to your build.gradle with:<br>

    repositories {
        ...
        maven { url "https://jitpack.io" }
    }

Add the dependency:<br>

    dependencies {
        compile 'com.github.FranklinNEO:SignView:1.0'
    }
    
An example of basic usage in layout.xml:<br>

    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
       android:layout_height="match_parent"
        android:gravity="center_horizontal"
        android:orientation="vertical">

        <com.neo.libray.SignView
            android:id="@+id/daily_sv"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:background="#ffffff"
            app:signedDays="3" />
    </LinearLayout>
    
  This statement declares the number of signed days<br>
  
      app:signedDays="3"