package jp.jaxa.iss.kibo.rpc.sampleapk;

import org.opencv.objdetect.QRCodeDetector;
import org.opencv.core.Mat;

public class QRDecipher {
    //The actual detector
    public static QRCodeDetector detector = new QRCodeDetector();
    //The mat of the navcam, looking at the qrCode
    public static Mat image;
    //The plain string returned by the qr
    public static String qrString;
    //The qrString's mission report equivalent
    public static String reportString = "NO_PROBLEM"; //If something fails then it has a 1/6 chance of being right

    //Use if looking at qr code and you want to decipher it
    public static void decipher() {
        image = null;
        //Stores what navCam sees in the Mat image (dockCam needs to be seeing the qrCode)
        image = YourService.myApi.getMatDockCam();

        for(int i = 0; i < 4 && image == null; i++) {
            try {
                Thread.sleep(500);
            } catch(InterruptedException e) {
                YourService.logger.info("Error while sleeping during a qr image reset");
            }

            image = YourService.myApi.getMatDockCam();
        }

        //YourService.myApi.saveMatImage(image, "QR Code Image");
        //Get the plain decipher of the qr code
        qrString = detector.detectAndDecode(image);

        //Match it up with the correct report message
        if(qrString.equals("JEM")) {
            YourService.scanned = true;
            reportString = "STAY_AT_JEM";
        } else if(qrString.equals("COLUMBUS")) {
            YourService.scanned = true;
            reportString = "GO_TO_COLUMBUS";
        } else if(qrString.equals("RACK1")) {
            YourService.scanned = true;
            reportString = "CHECK_RACK_1";
        } else if(qrString.equals("ASTROBEE")) {
            YourService.scanned = true;
            reportString = "I_AM_HERE";
        } else if(qrString.equals("INTBALL")) {
            YourService.scanned = true;
            reportString = "LOOKING_FORWARD_TO_SEE_YOU";
        } else if(qrString.equals("BLANK")){
            YourService.scanned = true;
            reportString = "NO_PROBLEM";
        }

        image.release();
        image = null;

        System.gc();
        System.runFinalization();
    }
}
