
package Utility;


public class Data {
    public static int numPeriods() {// Period t=0 NOT TAKEN INTO ACCOUNT
        int numItems = 5 ; // n
        return numItems;
    }
    public static int numItems() {// Period t=0 NOT TAKEN INTO ACCOUNT
        int numItems = 3;
        return numItems;
    }
    public static int Stat_Inventory_Level() {
        int Stat_Inventory_Level = 0;
        return Stat_Inventory_Level;
    }
     public static int End_Inventory_Level() {
        int End_Inventory_Level = 0;
        return End_Inventory_Level;
    }
      public static double[][] Demands() {
        double[][] Demands = {{30,0,0},
                              {0,0,0},
                              {80,30,40},
                              {0,0,0},
                              {40,70,60}};
        return Demands;
    }
    public static double[] Fixed_Costs() {
        double[] Fixed_Costs = {400,150,100};
        return Fixed_Costs;
    }
    
   public static double[][] Unit_Costs() {
        double[][] Unit_Costs = {{0,0,0},
                                  {0,0,0},
                                  {0,0,0},
                                  {0,0,0},
                                  {0,0,0}};
        return Unit_Costs;
    }
    public static double[][] Inventory_Costs() {
        double[][] Inventory_Costs = {{4,3,2},
                                  {4,3,2},
                                  {4,3,2},
                                  {4,3,2},
                                  {4,3,2}};
        return Inventory_Costs;
    }
     public static double[][] Capacities() {
        double[][] Capacities = {{100,100,100},
                                  {100,100,100},
                                  {100,100,100},
                                  {100,100,100},
                                  {100,100,100}};
        return Capacities;
    }
}
