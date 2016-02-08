package com.zavrab;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Date> dates = new PrettyTimeParser().parse("We have meeting today and tomorrow and maybe in three days from now!");
        System.out.println("parsed dates: " + dates);
    }
}
