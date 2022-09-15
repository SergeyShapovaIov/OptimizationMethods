package com.example.OptimizationMethods.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import org.apache.el.lang.FunctionMapperImpl;
import org.apache.el.lang.FunctionMapperImpl.Function;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        Function function = new Function();
        while(!stopCreterion){
            double x1 = ((startingPoint + finishingPoint) / 2)  - constantDistinguishability;
            double x2 = ((startingPoint + finishingPoint) / 2)  + constantDistinguishability;
            double y1 = function.calculateValueByParametr(x1);
            double y2 = function.calculateValueByParametr(x2);
            /*
            double y1 = x1*x1 - 2*x1 +1;
            double y2 = x2*x2 - 2*x2 +1;
             */

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
    Формат записи функции следующий y = (x^2) - (2 * x) + (1)
     */
    public class Function {
        public void Function (/*String stringFunction*/) {
            GeneralInfo generalInfo = new GeneralInfo();
            generalInfo.moduleCount = 3;
            generalInfo.intermoduleOperators = new ArrayList(Arrays.asList("-","+"));
            modules = new ArrayList<>();
            /*
            Создаем первый модуль
             */
                FunctionModule functionModule_1 = new FunctionModule();
                functionModule_1.info.existenceVariable = true;
                functionModule_1.info.notNull = true;
                functionModule_1.info.trigonometricFunction = false;
                functionModule_1.info.exponentiation = true;
                functionModule_1.info.factorAvailability = true;
                functionModule_1.options.trigonometric = null;
                functionModule_1.options.exponentiation = 2;
                functionModule_1.options.factor = 2;
                /*
                Создаем второй модуль
                 */
                FunctionModule functionModule_2 = new FunctionModule();
                functionModule_2.info.existenceVariable = true;
                functionModule_2.info.notNull = true;
                functionModule_2.info.trigonometricFunction = false;
                functionModule_2.info.exponentiation = false;
                functionModule_2.info.factorAvailability = true;
                functionModule_2.options.trigonometric = null;
                functionModule_2.options.exponentiation = 2;
                functionModule_2.options.factor = 2;
                /*
                Создаем третий модуль
                 */
                FunctionModule functionModule_3 = new FunctionModule();
                functionModule_3.info.existenceVariable = false;
                functionModule_3.info.notNull = true;
                functionModule_3.info.trigonometricFunction = false;
                functionModule_3.info.exponentiation = false;
                functionModule_3.info.factorAvailability = true;
                functionModule_3.options.trigonometric = null;
                functionModule_3.options.exponentiation = 1;

                modules.add(functionModule_1);
                modules.add(functionModule_2);
                modules.add(functionModule_3);
        }
        public class GeneralInfo {
            public int moduleCount;
            public List<String> intermoduleOperators;
        }
        public ArrayList<FunctionModule> modules;
        public class FunctionModule {
            public Info info;
            public Options options;
            public class Info {
                public boolean existenceVariable;
                public boolean notNull;
                public boolean trigonometricFunction;
                public boolean exponentiation;
                public boolean factorAvailability;
            }
            public class Options {
                public String trigonometric;
                public double exponentiation;
                public double factor;
            }
        }

        public double calculateValueByParametr (double parametr){
            double result = 0;
            for(int i = 0; i < modules.size(); i++){
                double moduleResult = 0;
                if(modules.get(i).info.existenceVariable){
                    moduleResult = parametr;
                } else {
                    moduleResult = 1;
                }
                if(modules.get(i).info.exponentiation){
                    moduleResult = Math.pow(parametr, modules.get(i).options.exponentiation);
                }
                if(modules.get(i).info.factorAvailability){
                    moduleResult = moduleResult * modules.get(i).options.factor;
                }
                if(i == 0){
                    result = moduleResult;
                }
                if(i == 1){
                    result -= moduleResult;
                }
                if(i== 2){
                    result += moduleResult;
                }
            }
            return result;
        }
    }
}