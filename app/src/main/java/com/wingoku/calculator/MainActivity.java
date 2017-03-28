package com.wingoku.calculator;

import android.os.Bundle;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.crashlytics.android.Crashlytics;
import com.wingoku.calculator.fragments.CalculatorFragment;
import com.wingoku.calculator.fragments.SplashScreenFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Umer on 3/25/2017.
 */
public class MainActivity extends AppCompatActivity {

    // this idling resource will be used by Espresso to wait for and synchronize with RetroFit Network call
    // https://youtu.be/uCtzH0Rz5XU?t=3m23s
    private CountingIdlingResource mEspressoTestIdlingResource;
    private FragmentManager mFManager;

    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initializing Crashlytics for error reporting
        Fabric.with(this, new Crashlytics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        //must bind for View Injection
        ButterKnife.bind(this);
        mEspressoTestIdlingResource = new CountingIdlingResource("Lottie_Animations");

        mFManager = getSupportFragmentManager();

        if(savedInstanceState == null) {
            mEspressoTestIdlingResource.increment();
            openFragment(new SplashScreenFragment(), true, false, false);
        }
    }

    /**
     * Open a fragment
     *
     * @param frag Fragment to open
     * @param isReplaced should this fragment replace current visible fragment
     * @param isAdded should this fragment be added on top of current fragment
     * @param addToBackStack should this fragment be added to backstack for removal upon onBackPressed
     */
    private void openFragment(Fragment frag, boolean isReplaced, boolean isAdded, boolean addToBackStack) {
        FragmentTransaction fTranscation = mFManager.beginTransaction();

        if (isReplaced)
            fTranscation.replace(R.id.fragment_container, frag);
        else if (isAdded)
            fTranscation.add(R.id.fragment_container, frag);

        if (addToBackStack)
            fTranscation.addToBackStack(frag.getClass().getSimpleName());

        fTranscation.commit();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // this method will be called when splash screen animation has been completed
    @Subscribe
    public void onAnimationEvent(Boolean onAnimationCompleted) {
        if(onAnimationCompleted) {
            openFragment(new CalculatorFragment(), true, false, false);
            mEspressoTestIdlingResource.decrement();
        }
    }

    /**
     * This method will return Espresso IdlingResource for aiding sync between Lottie custom threads & Espresso
     *
     * @return MainActvity's idling resource for Espresso testing
     */
    public CountingIdlingResource getMainActivityEspressoIdlingResource() {
        return mEspressoTestIdlingResource;
    }
}
