package com.live.zeropragasolutions.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.live.zeropragasolutions.Dao.PragaDao;
import com.live.zeropragasolutions.Dao.TipoColetaDao;
import com.live.zeropragasolutions.Dao.UsuarioDao;
import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.Model.TipoColeta;
import com.live.zeropragasolutions.Model.Usuario;

@Database(entities = {Usuario.class,Praga.class, TipoColeta.class} , version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract UsuarioDao getUsuarioDao();

    public abstract PragaDao getPragaDao();

    public abstract TipoColetaDao getTipoColetaDao();

    private static AppDataBase instance;

    public static AppDataBase getInstance(Context contexto){

        if(instance == null ){
            instance = Room.databaseBuilder(contexto,AppDataBase.class,"app").allowMainThreadQueries().build();
        }

        return instance;
    }

}
