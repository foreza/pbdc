package com.vartyr.pbdc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aerserv.sdk.AerServSdk;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class MainActivity  extends AppCompatActivity implements RewardedVideoAdListener{


    private InterstitialAd mInterstitialAd;     // Test Interstitial
    private RewardedVideoAd mRewardedVideoAd;   // Test Rewarded
    private AdView mAdView;                      // Test Banner


    private RelativeLayout RL;                  // Reference to the layout so we can programtically draw


    private String APP_ID = "ca-app-pub-5467746033313132~3071695138";                   // Using CC test APP ID
    private String BANNER_AD_ID = "ca-app-pub-5467746033313132/8247522239";             // Using AdMob Test
    private String INTERSTITIAL_AD_ID = "ca-app-pub-5467746033313132/5175854267";       // Using AerServ test
    private String REWARDED_AD_ID = "ca-app-pub-5467746033313132/4860313397";           // Using AerServ test



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, APP_ID);
        AerServSdk.init(this,"380000");

        RL = (RelativeLayout) findViewById(R.id.main_background);     // Get a reference to the relative layout

    }


    public void loadAndShowAdMobBanner(View view){

        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(BANNER_AD_ID);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Log.d("TAG", "The banner is now loading and will show...");
        RL.addView(mAdView);


    }

    public void configureAdMobBannerListener() {

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("TAG", "AdMob banner - onAdLoaded");

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d("TAG", "AdMob banner - onAdFailedToLoad with error: " + errorCode);

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d("TAG", "AdMob banner - onAdOpened");

            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d("TAG", "AdMob banner - onAdLeftApplication");

            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.d("TAG", "AdMob banner - onAdClosed");

            }
        });


    }


    public void loadAdMobInterstitial(View view){

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(INTERSTITIAL_AD_ID);
        configureAdMobInterstitialListener();
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        Log.d("TAG", "The interstitial is now loading.");


    }

    public void showAdMobInterstitial(View view){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    public void configureAdMobInterstitialListener(){
        // Configure interstitial ad listener
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("TAG", "AdMob interstitial - onAdLoaded");

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d("TAG", "AdMob interstitial - onAdFailedToLoad, with code: " + errorCode);

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.d("TAG", "AdMob interstitial - onAdOpened");

            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d("TAG", "AdMob interstitial - onAdLeftApplication");

            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.d("TAG", "AdMob interstitial - onAdClosed");
                mInterstitialAd.loadAd(new AdRequest.Builder().build()); // Load another one right away
            }
        });
    }


    public void loadAdMobRewarded(View view) {

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

        mRewardedVideoAd.loadAd(REWARDED_AD_ID, new AdRequest.Builder().build());
        mRewardedVideoAd.setRewardedVideoAdListener(this);

    }


    public void showAdMobRewarded (View view) {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        } else {
            Log.d("TAG", "The rewarded wasn't loaded yet.");
        }

    }

    // AdMob Rewarded callback
    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
                reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        mRewardedVideoAd.loadAd(REWARDED_AD_ID, new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }

}

