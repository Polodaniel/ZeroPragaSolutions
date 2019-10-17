package com.live.zeropragasolutions.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.live.zeropragasolutions.Dao.PragaDao;
import com.live.zeropragasolutions.Model.Praga;

@Database(entities = {Praga.class} , version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract PragaDao getPragaDao();

    private static AppDataBase instance;

    public static AppDataBase getInstance(Context contexto){

        if(instance == null ){
            instance = Room.databaseBuilder(contexto,AppDataBase.class,"app").allowMainThreadQueries().build();
        }

        return instance;
    }

}