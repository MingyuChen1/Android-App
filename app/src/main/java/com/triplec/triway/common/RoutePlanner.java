package com.triplec.triway.common;

import com.triplec.triway.common.TriPlan;
import com.triplec.triway.common.TriPlace;

import java.util.List;

/**
 * Take an array list of places and give a route with minimum cost
 * @author Qingran Li
 */
public class RoutePlanner {
    /* input list of places */
    private static List<TriPlace> list;
    private static int num;

    /* Store places and relative weight */
    private static SelectedPlace[] mplaces;
    private static int[] index;

    /* Store result */
    private static TriPlan plan;
    private static TriPlace[] result;
    private static double minCost = Integer.MAX_VALUE;

    /**
     * Set list and get number of places
     *
     * @param alist The given list of selected places
     */
    public static void setRoutePlanner(List<TriPlace> alist) {
        list = alist;
        num = list.size();
    }

    /**
     * Get the route with minimum cost (shortest distance for now)
     * Must call setRoutePlanner() with a nonempty array list before calling this function
     *
     * @return array list of places with the order such that the route has minimum cost
     */
    public static TriPlan planRoute() {
        // return empty array list if input list is empty
        if(num == 0) {
			TriPlan.TriPlanBuilder myBuilder = new TriPlan.TriPlanBuilder();
            return myBuilder.buildPlan();
        }

        // initialize selected places, index array, and plan
        mplaces = new SelectedPlace[num];
        index = new int[num];

        // set up places with weight and index array
        for(int i = 0; i < num; i++) {
            index[i] = i;
            mplaces[i] = new SelectedPlace(list.get(i), num);
            // set weight
            for(int j = 0; j < num; j++) {
                mplaces[i].setNeighbor(list.get(j), j);
            }
        }

        // initialize the result array list and plan
        result = new TriPlace[num];
		TriPlan.TriPlanBuilder myBuilder = new TriPlan.TriPlanBuilder();

        // iterate through all permutations, get best route with minCost
        permute(index, 0);
        for(int i = 0; i < num; i++) {
            myBuilder.addPlace(result[i]);
        }

        return myBuilder.buildPlan();
    }

    /**
     * Iterate through all permutations, update route and minCost
     *
     * @param index		Array of index of neighbors
     * @param start		The start index to permute
     */
    private static void permute(int[] index, int start){
        // base case
        if (start == num - 1){
            // check the cost of current route
            double currCost = getRouteCost(index);

            // update minCost and route
            if(currCost < minCost) {
                minCost = currCost;
                for(int i = 0; i < num; i++) {
                    result[i] = mplaces[index[i]].getPlace();
                }
            }
        }

        // recursion
        for(int i = start; i < num; i++){
            swap(index, i, start);
            permute(index, start + 1);
            swap(index, start, i);
        }

    }

    /**
     * Swap start and end index
     *
     * @param index		Array of index of neighbors
     * @param start		Start index to swap
     * @param end		End index to swap
     */
    private static void swap(int[] index, int start, int end) {
        int tmp = index[start];
        index[start] = index[end];
        index[end] = tmp;
    }

    /**
     * Get cost of current route
     *
     * @param index		Array of index of neighbors
     * @return			Cost of current route
     */
    private static double getRouteCost(int[] index) {
        double cost = 0;

        // add weights
        for(int i = 0; i < num - 1; i++) {
            cost += mplaces[index[i]].getWeight(index[i + 1]);
        }

        return cost;
    }
}