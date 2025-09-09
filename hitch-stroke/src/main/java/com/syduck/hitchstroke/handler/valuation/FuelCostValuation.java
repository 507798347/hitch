package com.syduck.hitchstroke.handler.valuation;


//燃料成本估值
public class FuelCostValuation implements Valuation {

    private final Valuation valuation;
    private final static float fuelCosPrice = 1.0F;

    public FuelCostValuation(Valuation valuation) {
        this.valuation = valuation;
    }

    @Override
    public float calculation(float km) {
        float beforeCost = (valuation == null ? 0f : valuation.calculation(km));
        return beforeCost + fuelCosPrice;
    }
}
