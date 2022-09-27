package com.example.OptimizationMethods.controller;

import com.example.OptimizationMethods.Function;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/method")
public class MethodController {

    @PostMapping("/halving")
    public double halving(@RequestParam @NonNull String functionString
        , @RequestParam @NonNull double searchAccuracy
        , @RequestParam @NonNull double constantDistinguishability
        , @RequestParam @NonNull double startPosition
        , @RequestParam @NonNull double step) {


        functionString = functionDecoder(functionString);

        Map<String,String> paramsDSC = new HashMap<String, String>();
        paramsDSC.put("functionString", functionString);
        paramsDSC.put("startPosition", String.valueOf(startPosition));
        paramsDSC.put("step",String.valueOf(step));

        double segmentList[] = DSC(paramsDSC);
        double startingPoint = segmentList[0];
        double finishingPoint = segmentList[1];

        double result = 0;
        boolean stopCreterion = false;
        Function function = new Function(functionString);
        while (!stopCreterion) {
            double x1 = ((startingPoint + finishingPoint) / 2) - constantDistinguishability;
            double x2 = ((startingPoint + finishingPoint) / 2) + constantDistinguishability;
            double y1 = function.calculateValueByParametr(x1);
            System.out.println("Input param :" + x1 + "  Output param: " + y1);
            double y2 = function.calculateValueByParametr(x2);
            System.out.println("Input param :" + x2 + "   Output param: " + y2);
            if (y1 <= y2) {
                finishingPoint = x2;
            } else {
                startingPoint = x1;
            }
            if (((finishingPoint - startingPoint) / 2) < searchAccuracy) {
                result = (startingPoint + finishingPoint) / 2;
                stopCreterion = true;
            }
        }
        System.out.println("Finish");
        return result;
    }

    public String functionDecoder(String str) {
        String resultString;
        if (str.contains("**")) {
            resultString = str.replace("**", "^");
        } else {
            resultString = str;
        }
        return resultString;
    }


    private double[] DSC(Map<String,String> paramsDSC) {

        String functionString = paramsDSC.get("functionString");
        double startPosition = Double.valueOf(paramsDSC.get("startPosition"));
        double step = Double.valueOf(paramsDSC.get("step"));

        functionString = functionDecoder(functionString);
        Function function = new Function(functionString);
        double segmentLimits[] = new double[2];
        List<Double> xParam = new ArrayList<>();
        xParam.add(startPosition);
        // 1 step
        double y1 = function.calculateValueByParametr(startPosition);
        double y2 = function.calculateValueByParametr(startPosition + step);
        int counter;
        // 2 step
        if (y1 > y2) {
            segmentLimits[0] = startPosition;
            xParam.add(1, startPosition + step);
            counter = 2;
        } else {
            // 3 step
            double y3 = function.calculateValueByParametr(startPosition - step);
            if (y3 >= y1) {
                segmentLimits[0] = startPosition - step;
                segmentLimits[1] = startPosition + step;
                return segmentLimits;
            } else {
                segmentLimits[1] = startPosition;
                xParam.add(startPosition - step);
                step = 0 - step;
                counter = 2;
            }
        }
        do {
            xParam.add(xParam.get(0) + Math.pow(2, counter - 1) * step);
            y1 = function.calculateValueByParametr(
                xParam.get(counter)
            );
            y2 = function.calculateValueByParametr(
                xParam.get(counter-1)
            );
            if(y2 <= y1){
                if(step > 0){
                    segmentLimits[1] = xParam.get(counter);
                } else{
                    segmentLimits[0] = xParam.get(counter);
                }
                break;
            } else {
                if (step > 0){
                    segmentLimits[0] = xParam.get(counter-1);
                } else {
                    segmentLimits[1] = xParam.get(counter-1);
                }
                counter++;
            }
        } while (y2 > y1);

        return segmentLimits;
    }
}