# LoadingButton


按钮加载动画
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
