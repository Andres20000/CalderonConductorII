package calderonconductor.tactoapps.com.calderonconductor.Clases;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import calderonconductor.tactoapps.com.calderonconductor.R;

public class ModalValorCarrera {

    public interface FinalizoModal{
        void ResultadoModal(String valorCarrera);
    }

    private FinalizoModal interfaz;

    public ModalValorCarrera(Context contexto, FinalizoModal actividad)
    {
        interfaz = actividad;

        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogo.setContentView(R.layout.activity_modal_valor_carrera);

        final EditText valor = (EditText) dialogo.findViewById(R.id.valorCarrera);
        final Button btnAceptar = (Button) dialogo.findViewById(R.id.btnAceptar);

        valor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.ResultadoModal(valor.getText().toString());
                dialogo.dismiss();
            }
        });

        dialogo.show();
    }
}
