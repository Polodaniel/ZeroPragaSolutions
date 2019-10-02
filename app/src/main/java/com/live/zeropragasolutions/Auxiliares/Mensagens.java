package com.live.zeropragasolutions.Auxiliares;

import android.content.Context;
import android.widget.Toast;

public class Mensagens {

    public static void mostraMensagem(Context context, int idMensagem)
    {
        Toast.makeText(context, idMensagem, Toast.LENGTH_LONG).show();
    }
}
