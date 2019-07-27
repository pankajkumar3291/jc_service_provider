package android.com.cleaner.rxBus;

import android.com.cleaner.firebaseNotification.notificationModels.ProfileInformation;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {


    public PublishSubject<ProfileInformation> profileInformationPublishSubject = PublishSubject.create();


    public void send(ProfileInformation profileInformation) {
        profileInformationPublishSubject.onNext(profileInformation);
    }


    public Observable<ProfileInformation> toObservable() {
        return profileInformationPublishSubject;
    }

}
