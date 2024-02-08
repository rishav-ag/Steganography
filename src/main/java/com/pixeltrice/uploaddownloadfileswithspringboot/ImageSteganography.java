package com.pixeltrice.uploaddownloadfileswithspringboot;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class ImageSteganography {
    
    private String input1;
    private String input2;
    private String output;

    @Autowired
    public ImageSteganography() {
        input1 = "D:/uploads/one.jpeg";
        input2 = "D:/uploads/two.jpeg";
        output = "D:/uploads/stego.png";
        nu.pattern.OpenCV.loadLocally();
    }

    public String getCoverFileLocation() {
        return this.input1;
    }

    public String getSecretFileLocation() {
        return this.input2;
    }

    public String getEncryptedFileLocation() {
        return this.output;
    }

    public void encode() {
        Mat mat1 = Imgcodecs.imread(input1);
        Mat mat2 = Imgcodecs.imread(input2);

        for(int i=0; i<mat1.rows(); i++) {
            for(int j=0; j<mat1.cols(); j++) {
                double[] data2 = mat2.get(i, j);
                double[] data1 = mat1.get(i, j);
                for(int k=0; k<3; k++) {
                    int temp1 = (int)(data1[k]);
                    int temp2 = (int)(data2[k]);
                    String s1 = Integer.toBinaryString(temp1);
                    String s2 = Integer.toBinaryString(temp2);
                    s1 = ("00000000" + s1).substring(s1.length());
                    s2 = ("00000000" + s2).substring(s2.length());

                    String s = s1.substring(0, 4) + s2.substring(0, 4);
                    data1[k] = (double)(Integer.parseInt(s, 2));
                }
                mat1.put(i, j, data1);
            }
        }
        Imgcodecs.imwrite(output, mat1);
    }

    public void decode() {
        Mat mat = Imgcodecs.imread(output);
        Mat mat1 = new Mat(mat.rows(), mat.cols(), CvType.CV_64FC3);
        Mat mat2 = new Mat(mat.rows(), mat.cols(), CvType.CV_64FC3);

        for(int i=0; i<mat.rows(); i++) {
            for(int j=0; j<mat.cols(); j++) {
                double[] data = mat.get(i, j);
                double[] data1 = new double[3];
                double[] data2 = new double[3];
                for(int k=0; k<3; k++) {
                    int temp = (int)(data[k]);
                    String s = Integer.toBinaryString(temp);
                    s = ("00000000" + s).substring(s.length());
                    String s1 = s.substring(0, 4) + returnRandom();
                    String s2 = s.substring(4) + returnRandom();
                    data1[k] = (double)(Integer.parseInt(s1, 2));
                    data2[k] = (double)(Integer.parseInt(s2, 2));
                }
                mat1.put(i, j, data1);
                mat2.put(i, j, data2);
            }
        }
        Imgcodecs.imwrite(input1, mat1);
        Imgcodecs.imwrite(input2, mat2);
        
    }
    public String returnRandom() {
        Random random = new Random();
        String ch = String.valueOf((char)(random.nextInt(2)+48));
        return String.valueOf(ch+ch+ch+ch);
    }
}