# CircularRevealDrawer
Navigation Drawer where menu items are  revealed in animated fashion

## Demo
<p float="left">
  <img src="demo/demo_drawer.gif" width="250" />
</p>


## Install
```gradle
dependencies {
   implementation 'com.viceboy.circularrevealdrawer:circularrevealdrawer:1.0.0'
}
```


## Usage

### XML

```xml

 <?xml version="1.0" encoding="utf-8"?>
 <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    //Note: Match parent required for width and height
    <com.viceboy.circularrevealdrawer.widget.CircularRevealDrawer
        android:id="@+id/crdMenu"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:textSize="16sp"
        app:hamburgerColor="@color/colorPrimaryDark"
        app:innerCircularColor="@color/colorPrimary"
        app:menuTextArray="@array/menuItems"
        app:menuTextColor="@android:color/white"
        app:outerCircularColor="@color/colorPrimaryDark"
        app:profileImg="@drawable/avatar_sample"
        app:userEmail="alex@redmail.com"
        app:username="Alexander" />

 </androidx.constraintlayout.widget.ConstraintLayout>
 
 //Setting up menu texts or item text inside strings.xml
 
  <array name="menuItems">
        <item>Home</item>
        <item>About Us</item>
        <item>Wallet</item>
        <item>My Trips</item>
        <item>Contributors</item>
        <item>Privacy Policy</item>
        <item>Logout</item>
  </array>

```


### Kotlin

### Basic Usage
```kotlin

        // Set up profie image click listener 
        crdMenu.setOnProfileImageClickListener(object : OnProfileImageClickListener {
            override fun onClick(view: View) {
                showToast("Clicked on profile image")
            }
        })

        //Set up menu item click listener 
        crdMenu.setMenuItemClickListener(object : MenuItemClickListener {
            override fun getMenuListener(menus: Array<String>): HashMap<String, View.OnClickListener> {
                val mapOfClickListener = HashMap<String, View.OnClickListener>()
                menus.forEach {
                    val listener  = when (it) {
                        "Home" -> View.OnClickListener{
                            showToast("Clicked on Home")
                        }

                        "About Us" -> View.OnClickListener {
                            showToast("Clicked on About Us")
                        }

                        "Wallet" -> View.OnClickListener {
                            startActivity(Intent(this@MainActivity,Wallet::class.java))
                        }

                        "My Trips" -> View.OnClickListener {
                            showToast("Clicked on My Trips")
                        }

                        "Contributors" -> View.OnClickListener {
                            showToast("Clicked on Contributors")
                        }

                        else -> View.OnClickListener {
                            showToast("Click not configured")
                        }
                    }
                    mapOfClickListener[it] = listener
                }
                return mapOfClickListener
            }
        })
		
```

### Attributes

* **username**, username to be displayed on Reveal Drawer
* **userEmail**, userEmail to be displayed on Reveal Drawer
* **profileImg**, user profie image to be displayed in Reveal Drawer
* **outerCircularColor**, outer circle reveal color 
* **innerCircularColor**, inner circle reveal color
* **hamburgerColor**, hamburger menu color
* **hamburgerInnerBarColor**, bar color present inside hamburger menu
* **menuTextArray**, array of menu items in string (strings.xml)
* **menuTextColor**, menu item text color
* **menuTextPadding**, menu item text padding
* **profileImgBorderWidth**, profile image border width
* **profileImgBorderColor**, profile image border color
* **textSize**, menu item text size
* **fontFamily**, menu item text font family


## License

```
   Copyright 2020 Sumit Jha

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

