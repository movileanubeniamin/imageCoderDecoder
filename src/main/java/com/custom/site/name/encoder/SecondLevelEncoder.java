package com.custom.site.name.encoder;


public class SecondLevelEncoder {
    private static final int N = 8;
    private static double[] c = new double[N];


    public SecondLevelEncoder() {
        this.initializeCoefficients();
    }


    private void initializeCoefficients() {
        for (int i = 1; i < N; i++) {
            c[i] = 1;
        }
        c[0] = 1 / Math.sqrt(2.0);
    }


    public static long[][] blocksToDCT(long[][] f) {
        long[][] F = new long[N][N];
        for (int u = 0; u < N; u++) {
            for (int v = 0; v < N; v++) {
                double sum = 0.0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        sum += Math.cos(((2 * i + 1) / (2.0 * N)) * u * Math.PI) *
                                Math.cos(((2 * j + 1) / (2.0 * N)) * v * Math.PI) * f[i][j];
                    }
                }
                sum *= ((c[u] * c[v]) / 4.0);
                F[u][v] = (long) sum;
            }
        }
        return F;
    }
}
