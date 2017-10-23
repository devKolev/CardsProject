package com.devkolev.cards;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private Retrofit retrofit;
    private static JSONApiInterface jsonApiInterface;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        jsonApiInterface = retrofit.create(JSONApiInterface.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public static JSONApiInterface getApi() {
        return jsonApiInterface;
    }
}
