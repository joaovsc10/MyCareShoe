package com.example.mycareshoe.data.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.core.graphics.ColorUtils;

import com.example.mycareshoe.R;
import com.example.mycareshoe.helpers.SharedPrefManager;

import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SensorsReading implements Serializable {

    private int reading_id;
    private int S1;
    private int S2;
    private int S3;
    private int S4;
    private int S5;
    private int S6;
    private int S7;
    private int S8;
    private int S9;
    private int S10;
    private int S11;
    private int S12;
    private int S13;
    private int S14;
    private int S15;
    private int S16;
    private int S17;
    private int S18;
    private int S19;
    private int S20;
    private int S21;
    private int S22;
    private int S23;
    private int S24;
    private int S25;
    private int S26;
    private float T1;
    private float T2;
    private float H1;
    private float H2;
    private String date;
    private int patient_number;
    private boolean leftFootDataSent;
    private boolean rightFootDataSent;
    private boolean sensorColorPrinted;
    private int pressureThreshold;
    private ArrayList<String> hiperpressionSensors;
    private ArrayList<BasicNameValuePair> sensorsQueryParams = new ArrayList<>();

    public Map<String, Integer> sensorDistribution = new HashMap<String, Integer>(){{
            put(Sensors.S1.toString(), R.id.hallux_l);
            put(Sensors.S2.toString(), R.id.phalange_l);
            put(Sensors.S3.toString(), R.id.metatarsal1_l);
            put(Sensors.S4.toString(), R.id.metatarsal2_l);
            put(Sensors.S5.toString(), R.id.metatarsal3_l);
            put(Sensors.S6.toString(), R.id.cuboid_l);
            put(Sensors.S7.toString(), R.id.hindfoot2_l);
            put(Sensors.S8.toString(), R.id.hindfoot1_l);
            put(Sensors.S9.toString(), R.id.hindfoot3_l);
            put(Sensors.S10.toString(), R.id.hallux_r);
            put(Sensors.S11.toString(), R.id.phalange_r);
            put(Sensors.S12.toString(), R.id.metatarsal1_r);
            put(Sensors.S13.toString(), R.id.metatarsal2_r);
            put(Sensors.S14.toString(), R.id.metatarsal3_r);
            put(Sensors.S15.toString(), R.id.cuboid_r);
            put(Sensors.S16.toString(), R.id.hindfoot2_r);
            put(Sensors.S17.toString(), R.id.hindfoot1_r);
            put(Sensors.S18.toString(), R.id.hindfoot3_r);
            put(Sensors.S19.toString(), R.id.metatarsal1_r_upper);
            put(Sensors.S20.toString(), R.id.metatarsal2_r_upper);
            put(Sensors.S21.toString(), R.id.metatarsal3_r_upper);
            put(Sensors.S22.toString(), R.id.dorsal_r_upper);
            put(Sensors.S23.toString(), R.id.metatarsal1_l_upper);
            put(Sensors.S24.toString(), R.id.metatarsal2_l_upper);
            put(Sensors.S25.toString(), R.id.metatarsal3_l_upper);
            put(Sensors.S26.toString(), R.id.dorsal_l_upper);
    }};

    public SensorsReading() {
    }

    public enum Sensors {
        S1,
        S2,
        S3,
        S4,
        S5,
        S6,
        S7,
        S8,
        S9,
        S10,
        S11,
        S12,
        S13,
        S14,
        S15,
        S16,
        S17,
        S18,
        S19,
        S20,
        S21,
        S22,
        S23,
        S24,
        S25,
        S26,
        T1,
        T2,
        H1,
        H2
    }

    public SensorsReading(int pressureThreshold) {
        this.reading_id=-1;
        setPressureThreshold(pressureThreshold);
        setS1(-1);
        setS2(-1);
        setS3(-1);
        setS4(-1);
        setS5(-1);
        setS6(-1);
        setS7(-1);
        setS8(-1);
        setS9(-1);
        setS10(-1);
        setS11(-1);
        setS12(-1);
        setS13(-1);
        setS14(-1);
        setS15(-1);
        setS16(-1);
        setS17(-1);
        setS18(-1);
        setS19(-1);
        setS20(-1);
        setS21(-1);
        setS22(-1);
        setS23(-1);
        setS24(-1);
        setS25(-1);
        setS26(-1);
        this.T1=-1;
        this.T2=-1;
        this.H1=-1;
        this.H2=-1;
        this.date = null;
        this.patient_number = -1;
    }
    

    public SensorsReading(int reading_id, int s1, int s2, int s3, int s4, int s5, int s6, int s7, int s8, int s9, int s10, int s11, int s12, int s13, int s14, int s15, int s16, int s17, int s18, int s19, int s20, int s21, int s22, int s23, int s24, int s25, int s26, int T1, int T2, int H1, int H2, String date, int patient_number, int pressureThreshold) {
        this.pressureThreshold= pressureThreshold;
        this.reading_id = reading_id;
        setS1(s1);
        setS2(s2);
        setS3(s3);
        setS4(s4);
        setS5(s5);
        setS6(s6);
        setS7(s7);
        setS8(s8);
        setS9(s9);
        setS10(s10);
        setS11(s11);
        setS12(s12);
        setS13(s13);
        setS14(s14);
        setS15(s15);
        setS16(s16);
        setS17(s17);
        setS18(s18);
        setS19(s19);
        setS20(s20);
        setS21(s21);
        setS22(s22);
        setS23(s23);
        setS24(s24);
        setS25(s25);
        setS26(s26);
        this.T1=T1;
        this.T2=T2;
        this.H1=H1;
        this.H2=H2;
        this.date = date;
        this.patient_number = patient_number;

    }

    public int getPressureThreshold() {
        return pressureThreshold;
    }

    public void setPressureThreshold(int pressureThreshold) {
        this.pressureThreshold = pressureThreshold;
    }

    public ArrayList<BasicNameValuePair> getsensorsQueryParams() {
        return sensorsQueryParams;
    }

    public void setsensorsQueryParamsParams(ArrayList<String> sensorsQueryParamsParams) {
        this.sensorsQueryParams = sensorsQueryParams;
    }

    public void setLeftFootSensors(String[] leftSensors) {
        setS1(Integer.parseInt(leftSensors[0]));
        setS2(Integer.parseInt(leftSensors[1]));
        setS3(Integer.parseInt(leftSensors[2]));
        setS4(Integer.parseInt(leftSensors[3]));
        setS5(Integer.parseInt(leftSensors[4]));
        setS6(Integer.parseInt(leftSensors[5]));
        setS7(Integer.parseInt(leftSensors[6]));
        setS8(Integer.parseInt(leftSensors[7]));
        setS9(Integer.parseInt(leftSensors[8]));
        setS19(Integer.parseInt(leftSensors[9]));
        setS20(Integer.parseInt(leftSensors[10]));
        setS21(Integer.parseInt(leftSensors[11]));
        setS22(Integer.parseInt(leftSensors[12]));
        this.H1=Float.parseFloat(leftSensors[13]);
        this.T1=Float.parseFloat(leftSensors[14]);
        setDate();
        this.leftFootDataSent=true;
    }


    public void setRightFootSensors(String[] rightSensors) {
        setS10(Integer.parseInt(rightSensors[0]));
        setS11(Integer.parseInt(rightSensors[1]));
        setS12(Integer.parseInt(rightSensors[2]));
        setS13(Integer.parseInt(rightSensors[3]));
        setS14(Integer.parseInt(rightSensors[4]));
        setS15(Integer.parseInt(rightSensors[5]));
        setS16(Integer.parseInt(rightSensors[6]));
        setS17(Integer.parseInt(rightSensors[7]));
        setS18(Integer.parseInt(rightSensors[8]));
        setS23(Integer.parseInt(rightSensors[9]));
        setS24(Integer.parseInt(rightSensors[10]));
        setS25(Integer.parseInt(rightSensors[11]));
        setS26(Integer.parseInt(rightSensors[12]));
        this.H2=Float.parseFloat(rightSensors[13]);
        this.T2=Float.parseFloat(rightSensors[14]);
        this.rightFootDataSent=true;
    }

    public int getS7() {
        return S7;
    }

    public int getS1() {
        return S1;
    }

    public int getS2() {
        return S2;
    }

    public int getS3() {
        return S3;
    }

    public int getS4() {
        return S4;
    }

    public int getS5() {
        return S5;
    }

    public int getS6() {
        return S6;
    }

    public int getS8() {
        return S8;
    }

    public int getS9() {
        return S9;
    }

    public int getS10() {
        return S10;
    }

    public int getS11() {
        return S11;
    }

    public int getS12() {
        return S12;
    }

    public int getS13() {
        return S13;
    }

    public int getS14() {
        return S14;
    }

    public int getS15() {
        return S15;
    }

    public int getS16() {
        return S16;
    }

    public int getS17() {
        return S17;
    }

    public int getS18() {
        return S18;
    }

    public int getRightFootSensorsSum(){

        return S10+S11+S12+S13+S14+S15+S16+S17+S18;

    }

    public int getLeftFootSensorsSum(){
        return S1+S2+S3+S4+S5+S6+S7+S8+S9;
    }


    public int getRightFootSensorsMean(){

        return (S10+S11+S12+S13+S14+S15+S16+S17+S18)/9;

    }

    public int getLeftFootSensorsMean(){
        return (S1+S2+S3+S4+S5+S6+S7+S8+S9)/9;
    }


    public void setDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        this.date=dateFormat.format(c.getTime());
    }

    public boolean isDataReceivedFromBothFeet(){
        return this.leftFootDataSent&&this.rightFootDataSent;
    }

    public ArrayList<String> getHiperpressionSensors() {
        return hiperpressionSensors;
    }

    public String getDate() {
        return date;
    }

    public void setHiperpressionSensors(ArrayList<String> hiperpressionSensors) {
        this.hiperpressionSensors = hiperpressionSensors;
    }


    public boolean isSensorColorPrinted() {
        return sensorColorPrinted;
    }

    public void setSensorColorPrinted(boolean sensorColorPrinted) {
        this.sensorColorPrinted = sensorColorPrinted;
    }

    public void setS1(int s1) {
        S1 = s1;
        checkHiperpressionSensors(s1, Sensors.S1.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S1.toString(), Integer.toString(s1)));
    }

    public void setS2(int s2) {
        S2 = s2;
        checkHiperpressionSensors(s2, Sensors.S2.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S2.toString(), Integer.toString(s2)));
    }

    public void setS3(int s3) {
        S3 = s3;
        checkHiperpressionSensors(s3, Sensors.S3.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S3.toString(), Integer.toString(s3)));
    }

    public void setS4(int s4) {
        S4 = s4;
        checkHiperpressionSensors(s4, Sensors.S4.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S4.toString(), Integer.toString(s4)));
    }

    public void setS5(int s5) {
        S5 = s5;
        checkHiperpressionSensors(s5, Sensors.S5.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S5.toString(), Integer.toString(s5)));
    }

    public void setS6(int s6) {
        S6 = s6;
        checkHiperpressionSensors(s6, Sensors.S6.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S6.toString(), Integer.toString(s6)));
    }

    public void setS7(int s7) {
        S7 = s7;
        checkHiperpressionSensors(s7, Sensors.S7.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S7.toString(), Integer.toString(s7)));
    }

    public void setS8(int s8) {
        S8 = s8;
        checkHiperpressionSensors(s8, Sensors.S8.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S8.toString(), Integer.toString(s8)));
    }

    public void setS9(int s9) {
        S9 = s9;
        checkHiperpressionSensors(s9, Sensors.S9.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S9.toString(), Integer.toString(s9)));
    }

    public void setS10(int s10) {
        S10 = s10;
        checkHiperpressionSensors(s10, Sensors.S10.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S10.toString(), Integer.toString(s10)));
    }

    public void setS11(int s11) {
        S11 = s11;
        checkHiperpressionSensors(s11, Sensors.S11.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S11.toString(), Integer.toString(s11)));
    }

    public void setS12(int s12) {
        S12 = s12;
        checkHiperpressionSensors(s12, Sensors.S12.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S12.toString(), Integer.toString(s12)));
    }

    public void setS13(int s13) {
        S13 = s13;
        checkHiperpressionSensors(s13, Sensors.S13.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S13.toString(), Integer.toString(s13)));
    }

    public void setS14(int s14) {
        S14 = s14;
        checkHiperpressionSensors(s14, Sensors.S14.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S14.toString(), Integer.toString(s14)));
    }

    public void setS15(int s15) {
        S15 = s15;
        checkHiperpressionSensors(s15, Sensors.S15.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S15.toString(), Integer.toString(s15)));
    }

    public void setS16(int s16) {
        S16 = s16;
        checkHiperpressionSensors(s16, Sensors.S16.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S16.toString(), Integer.toString(s16)));
    }

    public void setS17(int s17) {
        S17 = s17;
        checkHiperpressionSensors(s17, Sensors.S17.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S17.toString(), Integer.toString(s17)));
    }

    public void setS18(int s18) {
        S18 = s18;
        checkHiperpressionSensors(s18, Sensors.S18.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S18.toString(), Integer.toString(s18)));
    }

    public void setS19(int s19) {
        S19 = s19;
        checkHiperpressionSensors(s19, Sensors.S19.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S19.toString(), Integer.toString(s19)));
    }

    public void setS20(int s20) {
        S20 = s20;
        checkHiperpressionSensors(s20, Sensors.S20.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S20.toString(), Integer.toString(s20)));
    }

    public void setS21(int s21) {
        S21 = s21;
        checkHiperpressionSensors(s21, Sensors.S21.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S21.toString(), Integer.toString(s21)));
    }

    public void setS22(int s22) {
        S22 = s22;
        checkHiperpressionSensors(s22, Sensors.S22.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S22.toString(), Integer.toString(s22)));
    }

    public void setS23(int s23) {
        S23 = s23;
        checkHiperpressionSensors(s23, Sensors.S23.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S23.toString(), Integer.toString(s23)));
    }

    public void setS24(int s24) {
        S24 = s24;
        checkHiperpressionSensors(s24, Sensors.S24.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S24.toString(), Integer.toString(s24)));
    }

    public void setS25(int s25) {
        S25 = s25;
        checkHiperpressionSensors(s25, Sensors.S25.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S25.toString(), Integer.toString(s25)));
    }

    public void setS26(int s26) {
        S26 = s26;
        checkHiperpressionSensors(s26, Sensors.S26.toString());
        sensorsQueryParams.add(new BasicNameValuePair(Sensors.S26.toString(), Integer.toString(s26)));
    }

    public Map<String, Integer> getSensors(){
        Map<String, Integer> sensorValues = new HashMap<String, Integer>(){{
            put(Sensors.S1.toString(), S1);
            put(Sensors.S2.toString(), S2);
            put(Sensors.S3.toString(), S3);
            put(Sensors.S4.toString(), S4);
            put(Sensors.S5.toString(), S5);
            put(Sensors.S6.toString(), S6);
            put(Sensors.S7.toString(), S7);
            put(Sensors.S8.toString(), S8);
            put(Sensors.S9.toString(), S9);
            put(Sensors.S10.toString(),S10);
            put(Sensors.S11.toString(), S11);
            put(Sensors.S12.toString(), S12);
            put(Sensors.S13.toString(), S13);
            put(Sensors.S14.toString(), S14);
            put(Sensors.S15.toString(), S15);
            put(Sensors.S16.toString(), S16);
            put(Sensors.S17.toString(), S17);
            put(Sensors.S18.toString(), S18);
            put(Sensors.S19.toString(), S19);
            put(Sensors.S20.toString(), S20);
            put(Sensors.S21.toString(), S21);
            put(Sensors.S22.toString(), S22);
            put(Sensors.S23.toString(), S23);
            put(Sensors.S24.toString(), S24);
            put(Sensors.S25.toString(), S25);
            put(Sensors.S26.toString(), S26);
        }};

        return sensorValues;
    }

    public Map<String, Float> getSensorsTempHumidity(){
        Map<String, Float> tempHumidityValues = new HashMap<String, Float>(){{
            put(Sensors.T1.toString(), T1);
            put(Sensors.T2.toString(), T2);
            put(Sensors.H1.toString(), H1);
            put(Sensors.H2.toString(), H2);
        }};

        return tempHumidityValues;
    }

    public float getT1() {
        return T1;
    }

    public float getT2() {
        return T2;
    }

    public float getH1() {
        return H1;
    }

    public float getH2() {
        return H2;
    }

    public void checkHiperpressionSensors(int sensor, String name){

        ArrayList<String> hiperpressionSensors = new ArrayList<>();

        if(getHiperpressionSensors()!=null)
            hiperpressionSensors=getHiperpressionSensors();

        if(sensor>=getPressureThreshold()){
            hiperpressionSensors.add(name);
            setHiperpressionSensors(hiperpressionSensors);
        }
    }

    public int changeSensorsColor(int sensorImageId, View view, int sensorValue, Context context){

        float fraction=0;
        int color=0;

        if(sensorValue<getPressureThreshold()) {
            fraction = ((float) sensorValue) / getPressureThreshold();
        }
        else{
            fraction=1;
        }
        ImageView imageViewIcon = (ImageView) view.findViewById(sensorImageId);

        if(sensorValue<(getPressureThreshold()/3))
            color = ColorUtils.blendARGB(context.getResources().getColor(R.color.dark_green), context.getResources().getColor(R.color.yellow), (float) fraction);
        else
            color = ColorUtils.blendARGB(context.getResources().getColor(R.color.yellow), context.getResources().getColor(R.color.red), (float) fraction);

        imageViewIcon.setColorFilter(color);


        return color;
    }


    public long calculateBalance(SensorsReading sr){

        long sensorsSum=sr.getLeftFootSensorsSum()+sr.getRightFootSensorsSum();

        if(sensorsSum==0){
            return 0;
        }
        return (sr.getLeftFootSensorsSum()*100)/sensorsSum;
    }

    public boolean isLeftHeelStrike(){
        if((this.getS7()>0 || this.getS8()>0 || this.getS9()>0)  && this.getS1()==0&& this.getS2()==0&& this.getS3()==0&& this.getS4()==0&& this.getS5()==0&& this.getS6()==0)
        {
            return true;
        }

        return false;
    }

    public boolean isLeftFootToeOff(){
        if(getLeftFootSensorsSum()==0)
            return true;
        return false;
    }

    public boolean isRightHeelStrike(){
        if((this.getS16()>0 || this.getS17()>0 || this.getS18()>0) && this.getS10()==0&& this.getS11()==0&& this.getS12()==0&& this.getS13()==0&& this.getS14()==0&& this.getS15()==0)
        {
            return true;
        }

        return false;
    }

    public boolean isRightFootToeOff(){
        if(getRightFootSensorsSum()==0)
            return true;
        return false;
    }
}
