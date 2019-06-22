/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MSI
 */
public class DateHelper {

    static final SimpleDateFormat DATE_FORMATER1 = new SimpleDateFormat("dd-MM-yyyy");
    static final SimpleDateFormat DATE_FORMATER2 = new SimpleDateFormat("MM-dd-yyyy");
    static final SimpleDateFormat DATE_FORMATER3 = new SimpleDateFormat("yyyy-MM-dd");
    
    public Date toDate(String date) throws ParseException{
        DATE_FORMATER1.setLenient(false);
        return DATE_FORMATER1.parse(date);
    }

    public String toString(Date date) {
        DATE_FORMATER1.setLenient(false);
        return DATE_FORMATER1.format(date);
    }

    public String now() {
        return toString(new Date());
    }
    
    public String changeDate(String dateString, int days) throws ParseException {
        Date date   = toDate(dateString);
        date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
        return toString(date);
    }
    
    //Chuyển date form 1(dd-MM-yyyy) sang date form 2 (MM-dd-yyyy)
    public String castDateForm1ToForm2(String dateForm1) throws ParseException{
        DATE_FORMATER1.setLenient(false);
        DATE_FORMATER2.setLenient(false);
        Date date   = DATE_FORMATER1.parse(dateForm1);
        
        return DATE_FORMATER2.format(date);
    }
    
    //Chuyển date form 3 sang date form 1
    public String castDateForm3ToForm1(String dateForm3) throws ParseException{
        DATE_FORMATER1.setLenient(false);
        DATE_FORMATER3.setLenient(false);
        Date date   = DATE_FORMATER3.parse(dateForm3);
        
        return DATE_FORMATER1.format(date);
    }
}
