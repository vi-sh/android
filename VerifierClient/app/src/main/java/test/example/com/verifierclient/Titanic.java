package test.example.com.verifierclient;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.animation.AccelerateDecelerateInterpolator;


public class Titanic {


    private AnimatorSet animatorSet;
    private Animator.AnimatorListener animatorListener;

    public Animator.AnimatorListener getAnimatorListener() {
        return animatorListener;
    }

    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public void start(final TitanicTextView textView)
    {

        final Runnable animate = new Runnable()
        {

            @Override
            public void run()
            {

                textView.setSinking(true);
                // horizontal animation. 200 = wave.png width
                ObjectAnimator maskXAnimator = ObjectAnimator.ofFloat(textView, "maskX", 0, 3500);
                maskXAnimator.setRepeatCount(ValueAnimator.INFINITE);
                maskXAnimator.setRepeatCount(ValueAnimator.REVERSE);
                maskXAnimator.setDuration(5000);
                maskXAnimator.setStartDelay(0);

                int h = textView.getHeight();

                // vertical animation
                // maskY = 0 -> wave vertically centered
                // repeat mode REVERSE to go back and forth
                ObjectAnimator maskYAnimator = ObjectAnimator.ofFloat(textView, "maskY", h/14, -h);
                maskYAnimator.setRepeatCount(ValueAnimator.INFINITE);
                maskYAnimator.setRepeatMode(ValueAnimator.REVERSE);
                maskYAnimator.setDuration(5000);
                maskYAnimator.setStartDelay(1000);

                // now play both animations together
                animatorSet = new AnimatorSet();
                animatorSet.playTogether(maskXAnimator, maskYAnimator);
                animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSet.addListener(new Animator.AnimatorListener()
                {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        textView.setSinking(false);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            textView.postInvalidate();
                        } else {
                            textView.postInvalidateOnAnimation();
                        }

                        animatorSet = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });


                if (animatorListener != null) {
                    animatorSet.addListener(animatorListener);
                }

                animatorSet.start();
            }
        };

        if (!textView.isSetUp()) {
            textView.setAnimationSetupCallback(new TitanicTextView.AnimationSetupCallback() {
                @Override
                public void onSetupAnimation(final TitanicTextView target) {
                    animate.run();
                }
            });
        } else {
            animate.run();
        }
    }

    public void cancel() {
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }
}