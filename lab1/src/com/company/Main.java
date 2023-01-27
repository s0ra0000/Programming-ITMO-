package com.company;
public class Main {
    public static void main(String[] args) {

        // Создал одномерный массив, заполнил его числами от 3 до 18 включательно в порядке возрастания
	    long[] a = new long[16];
	    for(int i=3; i<=18; i++) {
            a[i-3] = i; //Переменная i начинается от 3. Чтобы начинать индекс массива от 0 вычитал 3 от i
        }
	    // Создал одномерный массив, заполнил его случайними числами в диапазоне от - 14.0 до 7.0
	    double[] x = new double[15];
	    double min = -14.0, max = 7.0;
	    for(int i=0;i<15;i++){
	        x[i] = (Math.random()*(max-min))+min;
        }
	    // Создал двумерный массив, заполнил его вычислениями по формуле.
	    double[][] result = new double[16][15];
	    for(int i=0; i<16; i++){
	        for(int j=0; j<15;j++) {
                if(a[i] == 12) {
                    result[i][j] = Math.log(Math.acos((x[j]-3.5)/21));
                }
                else if(a[i]==4 || a[i]==5 || a[i]==8 | a[i]==11 || a[i]==15 || a[i]==16 || a[i]==17 || a[i]==18){
                    result[i][j] = Math.log((4+Math.abs(x[j]))/5);
                }
                else{
                    result[i][j] = Math.pow(Math.E,(Math.sin(Math.pow(Math.E,(Math.cos(x[j]))))));
                }
                // вывод результата в формате с пятью знаками после запятой
                System.out.printf("%8.5f ",result[i][j]);
            }
            System.out.println(); // переводить строку.
        }
    }
}
