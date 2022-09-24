package com.example.OptimizationMethods.controller;

import com.example.OptimizationMethods.controller.MethodController.Function.FunctionModule;
import com.example.OptimizationMethods.controller.MethodController.Function.FunctionModule.Info;
import com.example.OptimizationMethods.controller.MethodController.Function.FunctionModule.Options;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;


@RestController
@RequestMapping("/method")
public class MethodController {

    /*
     Вот для этой функции считаем:  y = x^2 - 2 * x + 1
     */

    @PostMapping("/halving")
    public double halving (@RequestParam @NonNull String functionString
            , @RequestParam @NonNull double searchAccuracy
            , @RequestParam @NonNull double constantDistinguishability
            , @RequestParam @NonNull double startingPoint
            , @RequestParam @NonNull double finishingPoint) {
        functionString = functionDecoder(functionString);
        double result = 0;
        boolean stopCreterion = false;
        Function function = new Function(functionString);
        while(!stopCreterion){
            double x1 = ((startingPoint + finishingPoint) / 2)  - constantDistinguishability;
            double x2 = ((startingPoint + finishingPoint) / 2)  + constantDistinguishability;
            double y1 = function.calculateValueByParametr(x1);
            double y2 = function.calculateValueByParametr(x2);
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
        public Function (String functionString) {
            // Создаем объект с общей информацией для всех модулей
            GeneralInfo generalInfo = new GeneralInfo();
            // Создаем список с модулями функции
            ArrayList<String> functionModules = new ArrayList<String>(Arrays.asList(functionString.split(" ")));
            ArrayList<String> intermoduleOperatorsList = new ArrayList<>();
            ArrayList<String> calculationModule = new ArrayList<>();
            // Заполняем значение поля Info
            if(!functionModules.isEmpty()){
                if(functionModules.size() > 2){
                    for(int i = 2 ; i < functionModules.size(); i+=2){
                        intermoduleOperatorsList.add(functionModules.get(i));
                    }
                }
                for(int i = 1; i < functionModules.size(); i+=2){
                    calculationModule.add(functionModules.get(i));
                }
            }
            generalInfo.moduleCount = intermoduleOperatorsList.size();
            generalInfo.intermoduleOperators = intermoduleOperatorsList;
            this.modules = new ArrayList<>();
            /*
            Создаем первый модуль
             */
            for(int moduleNumber = 0; moduleNumber <= generalInfo.getModuleCount(); moduleNumber ++){
                FunctionModule functionModule = new FunctionModule();
                functionModule.info = new Info();
                functionModule.options = new Options();

                Info info = functionModule.info;
                Options options = functionModule.options;

                // existenceVariable
                if(calculationModule.get(moduleNumber).contains("x")) {
                    info.existenceVariable = true;
                } else {
                    info.existenceVariable = false;
                }

                // notNull
                if(calculationModule.get(moduleNumber).isEmpty()) {
                    info.notNull = false;
                } else {
                    info.notNull = true;
                }

                // trigonometricFunction
                String[] trigonometricFunctionArray= {"sin","cos","tg","ctg"};
                for(int i = 0; i<trigonometricFunctionArray.length; i++){
                    if(calculationModule.get(moduleNumber).contains(trigonometricFunctionArray[i])){
                        info.trigonometricFunction = true;
                        break;
                    } else {
                        info.trigonometricFunction = false;
                    }
                }

                // exponent
                if(calculationModule.get(moduleNumber).contains("^")){
                    info.exponent = true;
                } else {
                    info.exponent = false;
                }

                // factorAvailability
                if(calculationModule.get(moduleNumber).contains("*")){
                    info.factorAvailability = true;
                } else {
                    info.factorAvailability  =false;
                }

                // values trigonometric
                if(info.trigonometricFunction){
                    for(int i = 0; i < trigonometricFunctionArray.length; i++){
                        if(calculationModule.get(moduleNumber).contains(trigonometricFunctionArray[i])){
                            options.trigonometric = trigonometricFunctionArray[i];
                        }
                    }
                } else {
                    options.trigonometric = null;
                }

                if(info.exponent){
                    options.exponentiation = Character.digit(
                            calculationModule.get(moduleNumber)
                            .charAt(calculationModule.get(moduleNumber).indexOf("^")+1)
                            ,10);
                } else {
                    options.exponentiation = 1;
                }

                if(info.factorAvailability){
                    options.factor =  Character.digit(
                            calculationModule.get(moduleNumber)
                            .charAt(calculationModule.get(moduleNumber).indexOf("*")-1)
                    ,10) ;
                } else {
                    options.factor = 1;
                }


                this.modules.add(functionModule);
            }
        }
        public class GeneralInfo {

            public int getModuleCount() {
                return this.moduleCount;
            }

            public void setModuleCount(final int moduleCount) {
                this.moduleCount = moduleCount;
            }

            public List<String> getIntermoduleOperators() {
                return this.intermoduleOperators;
            }

            public void setIntermoduleOperators(final List<String> intermoduleOperators) {
                this.intermoduleOperators = intermoduleOperators;
            }

            public int moduleCount;
            public List<String> intermoduleOperators;

            public GeneralInfo(){

            }



        }

        public ArrayList<FunctionModule> getModules() {
            return this.modules;
        }

        public void setModules(
            final ArrayList<FunctionModule> modules) {
            this.modules = modules;
        }

        public ArrayList<FunctionModule> modules;
        public class FunctionModule {
            public Info info;
            public Options options;
            public FunctionModule(){

            }
            public static class Info {

                public boolean isExistenceVariable() {
                    return this.existenceVariable;
                }

                public void setExistenceVariable(final boolean existenceVariable) {
                    this.existenceVariable = existenceVariable;
                }

                public boolean isNotNull() {
                    return this.notNull;
                }

                public void setNotNull(final boolean notNull) {
                    this.notNull = notNull;
                }

                public boolean isTrigonometricFunction() {
                    return this.trigonometricFunction;
                }

                public void setTrigonometricFunction(final boolean trigonometricFunction) {
                    this.trigonometricFunction = trigonometricFunction;
                }

                public boolean isExponent() {
                    return this.exponent;
                }

                public void setExponent(final boolean exponent) {
                    this.exponent = exponent;
                }

                public boolean isFactorAvailability() {
                    return this.factorAvailability;
                }

                public void setFactorAvailability(final boolean factorAvailability) {
                    this.factorAvailability = factorAvailability;
                }

                public boolean existenceVariable;
                public boolean notNull;
                public boolean trigonometricFunction;
                public boolean exponent;
                public boolean factorAvailability;

                public Info(){

                }
            }
            public static class Options {
                public String trigonometric;
                public double exponentiation;
                public double factor;

                public Options(){

                }
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
                if(modules.get(i).info.exponent){
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

    public String functionDecoder (String str){
        String resultString;
        if(str.contains("**")){
            resultString = str.replace("**","^");
        } else {
            resultString = str;
        }
        return resultString;
    }
}