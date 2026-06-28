# Multiple Item Capacitated Lot Sizing Problem (CLSP)

A **Mixed Integer Linear Programming (MILP)** model in **Java** for the **Multi-Item Capacitated Lot Sizing Problem**, built with the **IBM ILOG CPLEX 12.5** optimization engine via the CPLEX Java API.

## Overview

The Capacitated Lot Sizing Problem (CLSP) is a classic production planning problem in Operations Research. Given a set of items, a planning horizon divided into discrete periods, and a shared production resource with limited capacity, the goal is to decide **how much of each item to produce in each period** in order to satisfy known demand while minimizing total cost — without exceeding capacity in any period.

This repository contains a working Java implementation of the multi-item variant, where several products compete for the same capacitated resource in every time period.

## Repository Contents

| File | Description |
|---|---|
| `src/main/java/` | Java source code implementing and solving the CLSP via CPLEX API |
| `Multi_Item_Capacited_Lot_Sizing.lp` | LP file exported by CPLEX representing the model in standard LP format |
| `Multi_Item_Capacited_Lot_Sizing.log` | CPLEX solver log with run details and solution output |
| `Multy Item Capacitated Lot Sizing Problem (CLSP).pdf` | Mathematical formulation of the problem |
| `pom.xml` | Maven project descriptor (dependencies and build configuration) |
| `nbactions.xml` | NetBeans IDE action configuration |

## Mathematical Formulation

### Sets
- $T$ = the planning horizon (index $t = 0, 1, \dots, n$)
- $J$ = set of items (index $j = 1, \dots, m$)

### Parameters
- $d_{tj}$ = the demand forecast at time $t$ for item $j$
- $c_{tj}$ = the unit production or purchasing cost at time $t$ for item $j$
- $h_{tj}$ = the unit inventory cost at time $t$ for item $j$
- $K_j$ = the fixed setup or ordering cost for item $j$
- $C_{tj}$ = the maximum feasible lot size (capacity) at time $t$ for item $j$

### Variables
- $I_{tj}$ = inventory level at the end of period $t$ for item $j$
- $q_{tj}$ = quantity to be produced or ordered during period $t$ for item $j$
- $y_{tj} = \begin{cases} 1 & \text{if units of item } j \text{ are manufactured/ordered in period } t \\ 0 & \text{otherwise} \end{cases}$

### Objective Function

**(1)**

$$
\displaystyle \min \sum_{t=1}^{n} \sum_{j=1}^{m} \left( K_j \cdot y_{tj} + c_{tj} \cdot q_{tj} + h_{tj} \cdot I_{tj} \right)
$$

### Constraints

**(2)** — Inventory is zero at the start and at the end of the horizon

$$
I_{tj} = 0 \qquad t = 0 \ \text{and} \ t = n,\ \ \forall j \in J
$$

**(3)** — Demand satisfaction and inventory balance

$$
q_{tj} + I_{t-1,j} = d_{tj} + I_{tj} \qquad \forall t \in T \setminus \{0\},\ \ \forall j \in J
$$

**(4)** — Production is capped by capacity and linked to the setup decision

$$
q_{tj} \le C_{tj} \cdot y_{tj} \qquad \forall t \in T \setminus \{0\},\ \ \forall j \in J
$$

**(5)** — Non-negative production

$$
q_{tj} \ge 0 \qquad \forall t \in T \setminus \{0\},\ \ \forall j \in J
$$

**(6)** — Non-negative inventory

$$
I_{tj} \ge 0 \qquad \forall t \in T \setminus \{0\},\ \ \forall j \in J
$$

**(7)** — Binary setup variable

$$
y_{tj} \in \{0,1\} \qquad \forall t \in T \setminus \{0\},\ \ \forall j \in J
$$

### Interpretation

The objective function (1) represents the total management costs, including production (and/or purchasing), inventory, and setup/ordering costs. Conditions (2) impose that inventory levels at the beginning and end of the planning horizon are equal to zero. Constraints (3) reproduce the demand satisfaction and inventory balance constraint for each period. Constraints (4)–(5) allow positive production (bounded between 0 and the period capacity $C_{tj}$) if and only if the setup variable $y_{tj}$ is equal to 1.

A copy of this formulation is also available as a standalone PDF in this repository.

## Example Instance

The repository ships with a sample instance featuring:

- **3 items**, **5 demand periods** (plus an initial period $t = 0$)
- Per-period production capacity: **100 units** (uniform across all periods)
- Setup costs: **400** (item 1), **150** (item 2), **100** (item 3)
- Holding costs: **4 / 3 / 2** per unit per period (items 1, 2, 3)
- Demand is sparse: concentrated in periods 1, 3, and 5

The exported LP and solver log for this instance are included in the repository.

## Requirements

- **Java** 13 or later
- **Apache Maven** 3.x
- **IBM ILOG CPLEX** 12.5 (must be installed separately and added to the local Maven repository as `External_Libraries:Cplex:12.5`)

> CPLEX is a commercial solver. An academic license is available free of charge through the [IBM Academic Initiative](https://www.ibm.com/academic).

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/Diego-Fabbri/Multiple_Item_Capacitated_Lot_Sizing_Problem_CLSP.git
   ```

2. Install the CPLEX JAR into your local Maven repository:
   ```bash
   mvn install:install-file \
     -Dfile=<path-to-cplex.jar> \
     -DgroupId=External_Libraries \
     -DartifactId=Cplex \
     -Dversion=12.5 \
     -Dpackaging=jar
   ```

3. Build and run with Maven:
   ```bash
   mvn clean package
   mvn exec:java
   ```

   Or open the project directly in **NetBeans IDE** and use the pre-configured run action (`nbactions.xml`).

## Output

When executed, the program:
- Builds the MILP model programmatically using the CPLEX Java API
- Solves it and prints the solver status and optimal objective value
- Exports the model in LP format (`Multi_Item_Capacited_Lot_Sizing.lp`)
- Writes the solver log to `Multi_Item_Capacited_Lot_Sizing.log`

## License

No license has been specified for this repository. Please contact the author before reusing this code for purposes beyond personal study or reference.
