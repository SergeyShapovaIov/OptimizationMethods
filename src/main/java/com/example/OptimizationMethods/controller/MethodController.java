package com.example.OptimizationMethods.controller;

import lombok.NonNull;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/method")
public class MethodController {

    /*
     Вот для этой функции считаем:  y = x^2 - 2 * x + 1
     */

    @PostMapping("/halving1")
    public double halving1 (@RequestParam @NonNull double searchAccuracy
            , @RequestParam @NonNull double constantDistinguishability
            , @RequestParam @NonNull double startingPoint
            , @RequestParam @NonNull double finishingPoint) {

        double result = 0;
        boolean stopCreterion = false;

        while(!stopCreterion){
            double x1 = ((startingPoint + finishingPoint) / 2)  - constantDistinguishability;
            double x2 = ((startingPoint + finishingPoint) / 2)  + constantDistinguishability;

            double y1 = x1*x1 - 2*x1 +1;
            double y2 = x2*x2 - 2*x2 +1;

            if(y1 <= y2){

                finishingPoint = x2;
            } else {
                startingPoint = x1;
            }

            if(((finishingPoint - startingPoint)/2 ) < searchAccuracy){
                result = (startingPoint + finishingPoint) /2 ;
                stopCreterion = true;
            }
        }
        return result;
    }


    /*
    Формат записи функции следующий y = x^2 - 2 * x + 1
     */
    class Function{
        Map<String, BigDecimal> dictionary = new HashMap<String, BigDecimal>();

        public Function (String stringFunction) {

        }
    }
}