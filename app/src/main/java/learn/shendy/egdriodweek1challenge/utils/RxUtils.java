package learn.shendy.egdriodweek1challenge.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

@SuppressLint("CheckResult")
public class RxUtils {
    public static final PublishSubject<Completable> introMillisecondTimer = PublishSubject.create();

    private static final String TAG = "RxUtils";

    private static final ReplaySubject<Integer> mNumberListRelay = ReplaySubject.create();

    // MARK: Observer Methods

    public static Observable<Integer> observeNumberList() {
        generateNumbersAsync();

        return mNumberListRelay
                .subscribeOn(Schedulers.computation())
                .delay(2, TimeUnit.SECONDS);
    }

    public static void runIntroMillisecondTimer(int delay) {
        Completable
                .timer(delay, TimeUnit.MILLISECONDS)
                .doOnComplete(() -> introMillisecondTimer.onNext(Completable.complete()))
                .subscribe();
    }

    public static Completable milliSecondTimer(int delay) {
        return Completable
                .complete()
                .delay(delay, TimeUnit.MILLISECONDS);
    }

    private static void generateNumbersAsync() {
       Observable
               .range(0, 4)
               .concatMap(number -> Observable.just(number).delay(1, TimeUnit.SECONDS))
               .doOnNext(number -> Log.d(TAG, "observeNumberList: number = " + number))
               .doOnNext(mNumberListRelay::onNext)
               .subscribeOn(Schedulers.computation())
               .subscribe();
    }
}
