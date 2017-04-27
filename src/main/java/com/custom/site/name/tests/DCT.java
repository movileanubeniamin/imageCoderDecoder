package com.custom.site.name.tests;



public class DCT {
    private static final int N = 8;
    private double[] c = new double[N];

    public DCT() {
        this.initializeCoefficients();
    }

    private void initializeCoefficients() {
        for (int i=1;i<N;i++) {
            c[i]=1;
        }
        c[0]=1/Math.sqrt(2.0);
    }

    public double[][] applyDCT(double[][] f) {
        double[][] F = new double[N][N];
        for (int u=0;u<N;u++) {
            for (int v=0;v<N;v++) {
                double sum = 0.0;
                for (int i=0;i<N;i++) {
                    for (int j=0;j<N;j++) {
                        sum+=Math.cos(((2*i+1)/(2.0*N))*u*Math.PI)*Math.cos(((2*j+1)/(2.0*N))*v*Math.PI)*f[i][j];
                    }
                }
                sum*=((c[u]*c[v])/4.0);
                F[u][v]=sum;
            }
        }
        return F;
    }

    public double[][] applyIDCT(double[][] F) {
        double[][] f = new double[N][N];
        for (int i=0;i<N;i++) {
            for (int j=0;j<N;j++) {
                double sum = 0.0;
                for (int u=0;u<N;u++) {
                    for (int v=0;v<N;v++) {
                        sum+=(c[u]*c[v])/4.0*Math.cos(((2*i+1)/(2.0*N))*u*Math.PI)*Math.cos(((2*j+1)/(2.0*N))*v*Math.PI)*F[u][v];
                    }
                }
                f[i][j]=Math.round(sum);
            }
        }
        return f;
    }
}
