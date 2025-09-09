package com.syduck.hitchstroke.handler.valuation;


//起始价格估值
public class StartPriceValuation implements Valuation {
    private final Valuation valuation;

    public StartPriceValuation(Valuation valuation) {
        this.valuation = valuation;
    }

    @Override
    public float calculation(float km) {
        float beforeCost = (valuation == null ? 0f : valuation.calculation(km));
        float startingPrice = 13.0F;
        return beforeCost + startingPrice;
    }
}
