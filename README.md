# LoadingButton
allprojects {

	repositories {
	
			...
			maven { url 'https://jitpack.io' }
			
		}
	}	
	
 dependencies {
 
	        implementation 'com.github.duanhao:LoadingButton:1.0.1'

}

按钮加载动画

 <com.app.loadbtnlib.LoadingButton
        android:id="@+id/main_loadbtn_test"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:background="@drawable/button_main_color_selector"
        apps:circleBgColor="@color/colorPrimary"
        apps:txt="Hello World!"
        apps:txtcolor="#fff"
        apps:txtsize="14" />

 mLoadingBtn.startLoading(new LoadingButton.OnLoadingListener() {
            @Override
            public void onStart() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //  finishLoading();
                        mLoadingBtn.finishLoading();
                    }
                }, 1000);
            }

            @Override
            public void onFinish() {

            }
        });
