package domain;

public class ScontoStrategyFactory {
    private static ScontoStrategyFactory temp;
    private ScontoStrategyFactory(){};


    public  static ScontoStrategyFactory getInstance(){
        if (temp==null)
            temp=new ScontoStrategyFactory();
        return temp;
    }

    public ScontoStrategyInterface getScontoStrategy(){
        return new ScontoStrategy1();
    }

}
