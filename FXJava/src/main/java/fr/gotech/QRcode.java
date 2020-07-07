package fr.gotech;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class QRcode {

    public static HashMap<String, String> generator(){
        HashMap<String, String> test = new HashMap<String, String>();
        test.put("id","LTugoMTqQ1RoUDASZ9LX");
        test.put("client","CESI");
        return test;
    }
    private static BitMatrix generateMatrix(final String data, final int size) throws WriterException {
        final BitMatrix bitMatrix = new QRCodeWriter().encode(String.valueOf(data), BarcodeFormat.QR_CODE, size, size);
        return bitMatrix;
    }

    private static BufferedImage writeImage(BitMatrix bitMatrix){
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }


    public static String getClient(){
        return "CERI";
        //return "CERI";
    }
    public static String getID(){
        //CESI
        return "xH5B1Ayxz3djtXkRaytD";
        //CERI
        //return "QN2aMOM5qeBOIGzqWfaN";
        //return "f56JAmMxDDUQdzPRG065";
    }

    public static com.itextpdf.layout.element.Image creaQRCODE() {
        System.out.println("SimpleQrcodeGenerator DEBUT");

        try {
            //final HashMap<String,String> data = generator();
            //String data = "http://www.mspr.inventaire.com/CESI:LTugoMTqQ1RoUDASZ9LX";
            String data = "http://www.mspr.inventaire.com/"+ getClient()+":"+getID();
            final String imageFormat = "png";
            //final String outputFileName = "c:/code/mspr "+getClient() +"." + imageFormat;
            final int size = 400;
            // encode
            final BitMatrix bitMatrix = generateMatrix(data, size);
            //System.out.println(outputFileName);
            // write in a file
            BufferedImage image = writeImage(bitMatrix);
            Image test = image;
            ImageData imageData = ImageDataFactory.create(test,Color.black);
            com.itextpdf.layout.element.Image qrcode = new com.itextpdf.layout.element.Image(imageData);
            System.out.println("SimpleQrcodeGenerator FIN");
            return qrcode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
