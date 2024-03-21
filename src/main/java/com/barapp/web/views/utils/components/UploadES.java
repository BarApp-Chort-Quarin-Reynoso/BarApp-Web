package com.barapp.web.views.utils.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.upload.UploadI18N;

/**
 * Extensión de Upload para que tenga el texto en español
 */
public class UploadES extends UploadI18N {
    UI ui;
    public UploadES() {

//        ui = UI.getCurrent();
//        setDropFiles(new DropFiles().setOne(ui.getTranslation("view.registro.arrastraimagen"))
//                .setMany(ui.getTranslation("view.registro.arrastraimagen")));
//        setAddFiles(new AddFiles().setOne(ui.getTranslation("view.registro.subirimagen"))
//                .setMany(ui.getTranslation("view.registro.subirimagen")));
//        setError(new Error().setTooManyFiles("Liian monta tiedostoa.")
//                .setFileIsTooBig("Tiedosto on liian suuri.")
//                .setIncorrectFileType("Väärä tiedostomuoto."));
//        setUploading(new Uploading().setStatus(new Uploading.Status()
//                        .setConnecting("Yhdistetään...").setStalled("Pysäytetty")
//                        .setProcessing("Käsitellään tiedostoa...").setHeld("Jonossa"))
//                .setRemainingTime(new Uploading.RemainingTime()
//                        .setPrefix("aikaa jäljellä: ")
//                        .setUnknown("jäljellä olevaa aikaa ei saatavilla"))
//                .setError(new Uploading.Error()
//                        .setServerUnavailable("Palvelin ei vastaa")
//                        .setUnexpectedServerError("Palvelinvirhe")
//                        .setForbidden("Kielletty")));
//        setUnits(new Units().setSize(Arrays.asList("B", "kB", "MB", "GB", "TB",
//                "PB", "EB", "ZB", "YB")));
    }
}
