

package com.mycompany.capacitated_lot_sizing_problem_clsp;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVarType;
import ilog.concert.IloObjective;
import ilog.concert.IloObjectiveSense;
import ilog.cplex.IloCplex;

public class CLSP_Model {

    protected IloCplex model;

    protected int T;
    protected int Io;
    protected int In; 
    protected int J;
    protected double[][] unit_costs;
    protected double[][] demands;
    protected double[] fixed_costs;
    protected double[][] inventory_costs;
    protected double[][] capacities;

    protected IloIntVar[][] q;
    protected IloIntVar[][] I;
    protected IloIntVar[][] y;

    CLSP_Model(int numPeriods, int Io, int In,int numItems, double[][] Unit_costs,
            double[][] demands, double[] Fixed_costs, double[][] Inventory_costs,
            double[][] capacities) throws IloException {
        this.model = new IloCplex();
        this.Io = Io;
        this.In = In;
        this.J = numItems;
        this.T = numPeriods;
        this.unit_costs = Unit_costs;
        this.demands = demands;
        this.fixed_costs = Fixed_costs;
        this.inventory_costs = Inventory_costs;
        this.capacities = capacities;
        this.y = new IloIntVar[T][J];
        this.q = new IloIntVar[T + 1][J];
        this.I = new IloIntVar[T+1][J];
    }

    //The following code creates the variables
    protected void addVariables() throws IloException {

        for (int t = 0; t < T; t++) {
            int pos_t = t+1;
            for (int j = 0; j < J; j++) {
             int pos_j = j+1;
             
            q[t][j] = (IloIntVar) model.numVar(0, Float.MAX_VALUE, IloNumVarType.Float, "q[" + pos_t + "]["+pos_j+"]");

        }}
        for (int t = 0; t <=T; t++) {
         for (int j = 0; j <J; j++) {
             int pos_j = j+1;
            I[t][j] = (IloIntVar) model.numVar(0, Float.MAX_VALUE, IloNumVarType.Float, "I[" + t + "]["+pos_j+"]");

        }}
        for (int t = 0; t < T; t++) {
            int pos_t = t+1;
             for (int j = 0; j < J; j++) {
             int pos_j = j+1;
            y[t][j] = (IloIntVar) model.numVar(0, 1, IloNumVarType.Int, "y[" + pos_t + "]["+pos_j+"]");
             }
        }
    }
    
    //The following code creates the objective function for the problem.
    protected void addObjective() throws IloException {
        IloLinearNumExpr objective = model.linearNumExpr();

       
            for (int t = 0; t <T; t++) {
                for (int j = 0; j < J; j++) {
                objective.addTerm(y[t][j], fixed_costs[j]);
                objective.addTerm(q[t][j], unit_costs[t][j]);
                objective.addTerm(I[t+1][j], inventory_costs[t][j]);
                }
            }
        IloObjective Obj = model.addObjective(IloObjectiveSense.Minimize, objective);
    }
    
    
    
    
    //The following code defines the constraints for the problem

    protected void addConstraints() throws IloException {
// BALANCE DEMAND 
        for (int t = 1; t <= T; t++) {
            for (int j = 0; j < J; j++) {
            IloLinearNumExpr expr_1 = model.linearNumExpr();

            expr_1.addTerm(q[t-1][j], 1);
            expr_1.addTerm(I[t - 1][j], 1);
            expr_1.addTerm(I[t][j], - 1);
            model.addEq(expr_1, demands[t-1][j]);

        }}
        // Initial and Final Inventory Level
        for (int j = 0; j < J; j++) {
        model.addEq(I[0][j],Io);
        model.addEq(I[T][j],In);
        }
     
        
        // CAPACITY q_t - C_t*y_t <=0
        for (int t = 0; t < T; t++) {
            for (int j = 0; j < J; j++) {
            IloLinearNumExpr expr_2 = model.linearNumExpr();

            expr_2.addTerm(q[t][j], 1);
            expr_2.addTerm(-capacities[t][j],y[t][j]);
            
            model.addLe(expr_2, 0);

        }
        }

    }
public void solveModel() throws IloException {
        addVariables();
        addObjective();
        addConstraints();
        model.exportModel("Multi_Item_Capacited_Lot_Sizing.lp");

        model.solve();

        if (model.getStatus() == IloCplex.Status.Feasible
                | model.getStatus() == IloCplex.Status.Optimal) {
            System.out.println();
            System.out.println("Solution status = " + model.getStatus());
            System.out.println();
            System.out.println("Total Cost " + model.getObjValue());
            
            for (int t = 0; t <= T ; t++) {
            System.out.println("Time t = "+ t );
            for (int j = 0; j < J; j++) {
                System.out.println("Item j = "+ (j+1) );
                
            System.out.println( "---> "+ I[t][j].getName() +" = "+ model.getValue(I[t][j]));
            
            if(t!=0){
            System.out.println( "---> "+ q[t-1][j].getName() +" = "+ model.getValue(q[t-1][j]));
            
            System.out.println( "---> "+ y[t-1][j].getName() +" = "+ model.getValue(y[t-1][j]));
            }
             }
            System.out.println();
            }
            
        } else {
            System.out.println("The problem status is: " + model.getStatus());
        }
    }

}

    
    
    
    

