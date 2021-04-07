# Android Navigation
<a href="https://jitpack.io/#HadiDadkhah99/Navigation/"><img id="badge" src="https://jitpack.io/v/HadiDadkhah99/Navigation.svg"></a>


<p>
   <b>Step 1.</b> Add the JitPack repository to your build file
</p>
			
```groovy
allprojects {
		repositories {
	                //...
			maven { url 'https://jitpack.io' }
		}
	}
```



<p><b>Step 2.</b> Add the dependency</p>

```groovy
dependencies {
	        implementation 'com.github.HadiDadkhah99:Navigation:$last_version'
	}
```

			
		
# Usage

![](http://www.mytelbot.ir/resume/navigation_gif.gif)

## Initialization

### in onCreate Activity

```java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //init navigation
        NavigationPro navigationPro = NavigationPro.init(this, R.id.main_frame);

        //add fragments
        navigationPro.
                addFragment(HomeFragment.class, true).
                addFragment(NewsFragment.class, true).
                addFragment(ProfileFragment.class, true).
                addFragment(ProductFragment.class).
                addFragment(ProfileSettingsFragment.class).
                addFragment(ProductDetailFragment.class).
                addFragment(TopNewsFragment.class);

    }
```

## Manage Backstack

### in onBackPressed Activty

```java
 @Override
    public void onBackPressed() {
        navigationPro.onBackPress(this);
    }
```

## Attach BottomNavigationView

### in onCreate Activity after initialization

for

```java
        //find bottomNavigation
        bottomNavigationView = findViewById(R.id.bottom_view);
        //attach
        this.navigationPro.attachBottomNavigation(new BVNavigation(bottomNavigationView));
```



