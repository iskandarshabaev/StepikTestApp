package io.somedomain.stepiktestapp.widget;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class RxSearchView extends SearchView {

    private final static int DEBOUNCE_TIMEOUT = 300;
    private PublishSubject<String> mEditTextSubject = PublishSubject.create();
    private Disposable mSubscription;

    public RxSearchView(Context context) {
        super(context);
    }

    public RxSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxSearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mSubscription != null) {
            mSubscription.dispose();
        }
    }

    public void setOnRxQueryTextListener(final OnRxQueryTextListener listener) {
        if (mSubscription != null) {
            mSubscription.dispose();
        }

        setOnQueryTextListener(new OnQueryTextListenerImpl());

        mSubscription = mEditTextSubject
                .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        listener.onRxQueryTextChange(s);
                    }
                })
                .subscribe();
    }


    public interface OnRxQueryTextListener {
        void onRxQueryTextChange(String newText);
    }

    private class OnQueryTextListenerImpl implements OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {

            if (mEditTextSubject != null) {
                mEditTextSubject.onNext(newText);
            }

            return true;
        }
    }
}

