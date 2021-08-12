package com.example.mycareshoe.data.model;

import com.example.mycareshoe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SensorsReading {

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
    private int T1;
    private int T2;
    private int H1;
    private int H2;
    private String date;
    private int patient_number;
    private ArrayList<String> hiperpressionSensors;

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


    public SensorsReading(int reading_id, int s1, int s2, int s3, int s4, int s5, int s6, int s7, int s8, int s9, int s10, int s11, int s12, int s13, int s14, int s15, int s16, int s17, int s18, int s19, int s20, int s21, int s22, int s23, int s24, int s25, int s26, int T1, int T2, int H1, int H2, String date, int patient_number) {
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

    public ArrayList<String> getHiperpressionSensors() {
        return hiperpressionSensors;
    }

    public String getDate() {
        return date;
    }

    public void setHiperpressionSensors(ArrayList<String> hiperpressionSensors) {
        this.hiperpressionSensors = hiperpressionSensors;
    }

    public void setS1(int s1) {
        S1 = s1;
        checkHiperpressionSensors(s1, Sensors.S1.toString());
    }

    public void setS2(int s2) {
        S2 = s2;
        checkHiperpressionSensors(s2, Sensors.S2.toString());
    }

    public void setS3(int s3) {
        S3 = s3;
        checkHiperpressionSensors(s3, Sensors.S3.toString());
    }

    public void setS4(int s4) {
        S4 = s4;
        checkHiperpressionSensors(s4, Sensors.S4.toString());
    }

    public void setS5(int s5) {
        S5 = s5;
        checkHiperpressionSensors(s5, Sensors.S5.toString());
    }

    public void setS6(int s6) {
        S6 = s6;
        checkHiperpressionSensors(s6, Sensors.S6.toString());
    }

    public void setS7(int s7) {
        S7 = s7;
        checkHiperpressionSensors(s7, Sensors.S7.toString());
    }

    public void setS8(int s8) {
        S8 = s8;
        checkHiperpressionSensors(s8, Sensors.S8.toString());
    }

    public void setS9(int s9) {
        S9 = s9;
        checkHiperpressionSensors(s9, Sensors.S9.toString());
    }

    public void setS10(int s10) {
        S10 = s10;
        checkHiperpressionSensors(s10, Sensors.S10.toString());
    }

    public void setS11(int s11) {
        S11 = s11;
        checkHiperpressionSensors(s11, Sensors.S11.toString());
    }

    public void setS12(int s12) {
        S12 = s12;
        checkHiperpressionSensors(s12, Sensors.S12.toString());
    }

    public void setS13(int s13) {
        S13 = s13;
        checkHiperpressionSensors(s13, Sensors.S13.toString());
    }

    public void setS14(int s14) {
        S14 = s14;
        checkHiperpressionSensors(s14, Sensors.S14.toString());
    }

    public void setS15(int s15) {
        S15 = s15;
        checkHiperpressionSensors(s15, Sensors.S15.toString());
    }

    public void setS16(int s16) {
        S16 = s16;
        checkHiperpressionSensors(s16, Sensors.S16.toString());
    }

    public void setS17(int s17) {
        S17 = s17;
        checkHiperpressionSensors(s17, Sensors.S17.toString());
    }

    public void setS18(int s18) {
        S18 = s18;
        checkHiperpressionSensors(s18, Sensors.S18.toString());
    }

    public void setS19(int s19) {
        S19 = s19;
        checkHiperpressionSensors(s19, Sensors.S19.toString());
    }

    public void setS20(int s20) {
        S20 = s20;
        checkHiperpressionSensors(s20, Sensors.S20.toString());
    }

    public void setS21(int s21) {
        S21 = s21;
        checkHiperpressionSensors(s21, Sensors.S21.toString());
    }

    public void setS22(int s22) {
        S22 = s22;
        checkHiperpressionSensors(s22, Sensors.S22.toString());
    }

    public void setS23(int s23) {
        S23 = s23;
        checkHiperpressionSensors(s23, Sensors.S23.toString());
    }

    public void setS24(int s24) {
        S24 = s24;
        checkHiperpressionSensors(s24, Sensors.S24.toString());
    }

    public void setS25(int s25) {
        S25 = s25;
        checkHiperpressionSensors(s25, Sensors.S25.toString());
    }

    public void setS26(int s26) {
        S26 = s26;
        checkHiperpressionSensors(s26, Sensors.S26.toString());
    }

    public void checkHiperpressionSensors(int sensor, String name){

        ArrayList<String> hiperpressionSensors = new ArrayList<>();

        if(getHiperpressionSensors()!=null)
            hiperpressionSensors=getHiperpressionSensors();

        if(sensor>=200){
            hiperpressionSensors.add(name);
            setHiperpressionSensors(hiperpressionSensors);
        }
    }
}
