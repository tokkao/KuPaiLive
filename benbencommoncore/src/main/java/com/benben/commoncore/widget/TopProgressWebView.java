package com.benben.commoncore.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.benben.commoncore.R;


public class TopProgressWebView extends LinearLayout {

    private Context mContext;
    private WebView mWebView;
    private ProgressBar mProgressBar;

    public TopProgressWebView(Context context) {
        this(context, null);
    }


    public TopProgressWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopProgressWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.common_view_web_progress, this);
        mWebView = view.findViewById(R.id.web_view);
        mProgressBar = view.findViewById(R.id.progress_bar);
    }

    public void loadUrl(String url) {
        if (url == null) {
            url = "https://www.baidu.com/";
        }
        initWebview(url);
    }

    @SuppressLint("JavascriptInterface")
    private void initWebview(String url) {
        mWebView.addJavascriptInterface(this, "android");

        WebSettings webSettings = mWebView.getSettings();
        // 设置默认缩放方式尺寸是far
//		webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultFontSize(16);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);

//        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        mWebView.loadUrl(url);

        // 设置WebViewClient
        mWebView.setWebViewClient(new WebViewClient() {
            // url拦截
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                return super.shouldOverrideUrlLoading(view, url);
            }

            // 页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            // WebView加载的所有资源url
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//				view.loadData(errorHtml, "text/html; charset=UTF-8", null);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });

        // 设置WebChromeClient
        mWebView.setWebChromeClient(new WebChromeClient() {
            // 设置网页加载的进度条
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            // 设置程序的Title
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        mWebView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) { // 表示按返回键
                        mWebView.goBack(); // 后退
                        // webview.goForward();//前进
                        return true; // 已处理
                    }
                }
                return false;
            }
        });

        //获取webview里面的title
        WebChromeClient wcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        };
        mWebView.setWebChromeClient(wcc);
    }

    /**
     * 加载HTML的文本内容
     *
     * @param content
     */
    public void loadTextContent(String content) {
        initWebviewText(content);
    }

    private void initWebviewText(String content) {
        StringBuilder sb = new StringBuilder();
        sb.append(getHtmlData(content));
        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        mWebView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
        // 设置WebViewClient
        mWebView.setWebViewClient(new WebViewClient() {
            // url拦截
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                return super.shouldOverrideUrlLoading(view, url);
            }

            // 页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            // WebView加载的所有资源url
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//				view.loadData(errorHtml, "text/html; charset=UTF-8", null);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });

        // 设置WebChromeClient
        mWebView.setWebChromeClient(new WebChromeClient() {
            // 设置网页加载的进度条
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            // 设置程序的Title
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        mWebView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) { // 表示按返回键

                        mWebView.goBack(); // 后退

                        // webview.goForward();//前进
                        return true; // 已处理
                    }
                }
                return false;
            }
        });

        //获取webview里面的title
        WebChromeClient wcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        };
        mWebView.setWebChromeClient(wcc);
    }

    /**
     * 富文本适配
     */
    public static String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
}
