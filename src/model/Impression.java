/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

/**
 *
 * @author ak47@minduos
 */
public class Impression {
    
    public Impression(){}
    public void create(String nom, String type,double rh,double temps,double dp, double ha) throws FileNotFoundException, UnsupportedEncodingException
    {
    PrintWriter writer = new PrintWriter(nom+".txt", "UTF-8");
    writer.print("Nom de l'etudiant : \t");
    writer.println(nom);
    writer.print("Date : \t");
        Date date=new Date();
    writer.println(date);
    writer.print("Plante : \t");
    writer.println(type);
    writer.print("HR de l'air entrant : \t");
    writer.println(rh);
    writer.print("DP de l'air entrant : \t");
    writer.println(dp);
    writer.print("teneur de l'eau de l'air entrant (mg/L): \t");
    writer.println(ha);
    
    }
    public void add(String nom,double rh,double temp,double dp, double ha,int time)
    {
        Double RH=new Double(rh);
        Double TEMP=new Double(temp);
        Double DP=new Double(dp);
        Double HA=new Double(ha);
        Integer TIME=new Integer(time);
        String srh=RH.toString();
        String stemp=TEMP.toString();
        String sdp=DP.toString();
        String sha=HA.toString();
        String stime  =   TIME.toString();
        try {
            Files.write(Paths.get(nom+".txt"), "\r\n HR = ".getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(nom+".txt"), srh.getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(nom+".txt"), " DP = ".getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(nom+".txt"), sdp.getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(nom+".txt"), " Temperature = ".getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(nom+".txt"), stemp.getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(nom+".txt"), " \t TEMPS = ".getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(nom+".txt"), stime.getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(nom+".txt"), " \r\n teneur en eau sortie (mg/L): \t ".getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(nom+".txt"), sha.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
    //exception handling left as an exercise for the reader
        }
    
    
    
    }
    public void prob(String nom,int time)
    {
        Integer TIME=new Integer(time);
        String stime  =   TIME.toString();
        try {
            Files.write(Paths.get(nom+".txt"), "\r\n Probléme détection valeurs \t \t TEMPS=".getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(nom+".txt"), stime.getBytes(), StandardOpenOption.APPEND);
            
        }catch (IOException e) {
    //exception handling left as an exercise for the reader
        }
        
    
    }
    public void fin(String nom)
    {
    
            try {
            Files.write(Paths.get(nom+".txt"), "\r\n FIN des mesures".getBytes(), StandardOpenOption.APPEND);
            
            
        }catch (IOException e) {
    //exception handling left as an exercise for the reader
        }
    
    }
    
}
