package com.example.ramadan.learnenglishforchildren.presenters;

import android.content.Context;

import com.example.ramadan.learnenglishforchildren.R;
import com.example.ramadan.learnenglishforchildren.base.BaseView;
import com.example.ramadan.learnenglishforchildren.views.Interfaces.LearnEnglishView;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LearnEnglishPresenterImpl implements LearnEnglishPresenter {
    private LearnEnglishView learnEnglishView;
    private Context context;
    private CompositeDisposable compositeDisposable;

    public LearnEnglishPresenterImpl(Context mContext, BaseView view) {
        setView(view);
        context = mContext;
        compositeDisposable = new CompositeDisposable();

    }


    @Override
    public void getCharactersOfPhotos() {
        Flowable<String[]> getCharactersPhotoListFlowable = Flowable.fromCallable(() -> context.getResources().getStringArray(R.array.abc));

        Disposable disposeCharactersPhotos = getCharactersPhotoListFlowable.onBackpressureBuffer().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(charactersVoice -> learnEnglishView.onCharactersReceived(charactersVoice));

        compositeDisposable.add(disposeCharactersPhotos);
    }

    @Override
    public void getWordsOfPhotos() {
        Observable<String[]> todoObservable = Observable.create(emitter -> {
            try {
                String[] wordsVoice = context.getResources().getStringArray(R.array.wordsvoice);
                emitter.onNext(wordsVoice);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
        Disposable disposable = todoObservable.subscribe(wordsVoice -> learnEnglishView.onWordsReceived(wordsVoice));
        compositeDisposable.add(disposable);
    }

    @Override
    public void getPhotosOfCharacters() {
        Observable<int[]> todoObservable = Observable.just(
                getCharactersPhotoIndex()
        );
        Disposable disposablePhotosOfCharacters = todoObservable.subscribe(arrayPhotosOfCharacters -> learnEnglishView.onPhotosOfCharactersReceived(arrayPhotosOfCharacters));
        compositeDisposable.add(disposablePhotosOfCharacters);
    }

    @Override
    public void getPhotosOfWords() {

        Observable<int[]> todoObservable = Observable.just(
                getPhotosOfWordsIndex()
        );
        Disposable photosOfWordsSubscription = todoObservable.subscribe(arrayPhotosOfCharacters -> learnEnglishView.onPhotosOfWordsReceived(arrayPhotosOfCharacters));
        compositeDisposable.add(photosOfWordsSubscription);

    }

    @Override
    public void setView(BaseView view) {
        learnEnglishView = (LearnEnglishView) view;
    }

    @Override
    public void onStop() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }

    }

    @Override
    public void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }

    }

    @Override
    public int[] getCharactersPhotoIndex() {
        return new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.j, R.drawable.h,
                R.drawable.i, R.drawable.g, R.drawable.k, R.drawable.l, R.drawable.m, R.drawable.n, R.drawable.o, R.drawable.p, R.drawable.q, R.drawable.r
                , R.drawable.s, R.drawable.t, R.drawable.u, R.drawable.v, R.drawable.w, R.drawable.x, R.drawable.y, R.drawable.z};
    }

    @Override
    public int[] getPhotosOfWordsIndex() {
        return new int[]{R.drawable.ant, R.drawable.butterfly, R.drawable.cat, R.drawable.dog, R.drawable.elephont, R.drawable.frog, R.drawable.jellyfish
                , R.drawable.hippoptamous, R.drawable.iguana, R.drawable.giraffe, R.drawable.kangaroo, R.drawable.lion, R.drawable.monkey, R.drawable.narwhal
                , R.drawable.owl, R.drawable.panda, R.drawable.quetzal, R.drawable.rat, R.drawable.sheep, R.drawable.turtle, R.drawable.unicorn, R.drawable.viper
                , R.drawable.worm, R.drawable.xrayfish, R.drawable.yak, R.drawable.zebra};
    }

}
