package com.syalux.splash.entities;

import com.syalux.splash.data.Resource;

public class CoinEntity extends StaticEntity {
    public CoinEntity(double x, double y) {
        super(Resource.Environment.COIN, x, y, 1, 40);
    }
}