import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.Math.abs;

public class main {

    public static void wyswietl_macierz(double[][] tab, int n) {
        for (int i = 0; i < n; i++) {
            System.out.printf("[");
            for (int j = 0; j < n; j++) {
                System.out.printf("%7.3f  ", tab[i][j]);
            }
            System.out.printf("]");
            System.out.println();
        }
    }

    public static void wyswietl_macierz_pojedyncza(double[] tab, int n) {
        for (int i = 0; i < n; i++) {
            System.out.printf("[");
            for (int j = 0; j < 1; j++) {
                System.out.printf("%7.2f  ", tab[i]);
            }
            System.out.printf("]");
            System.out.println();
        }
    }

    public static double det(double[][] macierz, int n) {
        double pomocnicza[][];
        double whatReturn = 0;
        if (n == 1) {
            return macierz[0][0];
        } else {
            pomocnicza = new double[n - 1][n - 1];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n - 1; j++) {
                    for (int k = 0; k < n - 1; k++) {
                        pomocnicza[j][k] = macierz[j + 1][k < i ? k : k + 1];//jesli k <i to k, jeśli nie to k+1
                    }
                }
                if (i % 2 == 0) {
                    whatReturn += macierz[0][i] * det(pomocnicza, n - 1);
                } else {
                    whatReturn -= macierz[0][i] * det(pomocnicza, n - 1);
                }
            }
            return whatReturn;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner scan = new Scanner(System.in);
        System.out.print("Wybierz plik : ");
        String nazwa = scan.nextLine();
        nazwa += ".txt";
        System.out.println("Wybrany plik: " + nazwa);
        File plik = new File(nazwa);
        Scanner odczytzpliku = new Scanner(plik);


        int n;
        System.out.print("Podaj wymiar macierzy n  : ");
        n = odczytzpliku.nextInt();
        System.out.println(n);
        double[][] macierz = new double[n][n];
        double[][] macierz_przekatna = new double[n][n];
        double[][] macierz_bez_przekatnej = new double[n][n];
        double[][] macierz_odwrotnosci = new double[n][n];
        double[][] M = new double[n][n];
        double[] X = new double[n];
        double[] X1 = new double[n];
        double[] B = new double[n];
        double[] tab_wyznaczniki_pomocnicze = new double[n];
        double[][] wyznacznik_pomocniczy = new double[n][n];

        //Wpisanie liczb do macierzy nxn
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                macierz[i][j] = odczytzpliku.nextDouble();
                M[i][j] = 0;

            }

        System.out.println("Macierz A :");
        System.out.println();
        wyswietl_macierz(macierz, n);
        double wyznacznik = det(macierz, n);
        System.out.println();
        System.out.println("Wyznacznik macierzy =   " + wyznacznik);

        int suma_elementow = 0;
        int suma_przekatnej = 0;
        double iloczyn_przekatnej = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    suma_przekatnej += abs(macierz[i][i]);
                    iloczyn_przekatnej *= macierz[i][i];

                } else {
                    suma_elementow += abs(macierz[i][j]);
                }
            }
        }

        System.out.println();
        for (int i = 0; i < n; i++) {

            B[i] = odczytzpliku.nextDouble();
            X[i] = 0;
            X1[i] = 0;

        }
        System.out.println("Macierz B: ");
        System.out.println();
        wyswietl_macierz_pojedyncza(B, n);
        System.out.println();

        if (abs(wyznacznik) < 0.0001) {

            for (int i = 0; i < n; i++) {

                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        wyznacznik_pomocniczy[j][k] = macierz[j][k];
                    }
                }
                for (int j = 0; j < n; j++) {
                    wyznacznik_pomocniczy[j][i] = B[j];
                }
                tab_wyznaczniki_pomocnicze[i] = det(wyznacznik_pomocniczy, n);
            }

            boolean wszystkiezera = true;
            for (int i = 0; i < n; i++) {
                wszystkiezera = wszystkiezera && (tab_wyznaczniki_pomocnicze[i] == 0);
            }

            if (wszystkiezera) {
                System.out.println("Układ ma nieskończenie wiele rozwiązań - nieoznaczony");
                return;
            } else {
                System.out.println("Układ jest sprzeczny");
                return;
            }
        }
        if (suma_przekatnej < suma_elementow) {
            System.out.println("Macierz nie bedzie rozwiazana ! Niespełniony warunek Jacobiego ! (nie jest diagonalnie dominująca) ");
            return;
        }
        if (iloczyn_przekatnej == 0) {
            System.out.println("Macierz nie bedzie rozwiazana, na przekatnej wystepuje 0");
            return;
        }

        //przepisanie do macierz_przekatna tylko przekątnej macierzy "macierz[][]"
        for (int i = 0; i < n; i++) {
            macierz_przekatna[i][i] = macierz[i][i];

        }
        System.out.println("Macierz tylko z przekatna (reszta wypelniona zerami): ");
        wyswietl_macierz(macierz_przekatna, n);


        //przepisanie do macierzy "macierz_bez_przekatna" macierzy "macierz" i wyzerowanie przekątnej
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                macierz_bez_przekatnej[i][j] = macierz[i][j];
                macierz_bez_przekatnej[i][i] = 0;
            }
        }
        System.out.println("Macierz z wyzerowaną przekątną: ");
        System.out.println();
        wyswietl_macierz(macierz_bez_przekatnej, n);

        //odrtodnośc przekątnej
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                macierz_odwrotnosci[i][j] = 0;
                macierz_odwrotnosci[i][i] = 1 / macierz_przekatna[i][i];
            }
        }
        System.out.println("Macierz z odwrotnoscia przekatnej (reszta macierzy wypełniona zerami): ");
        System.out.println();
        wyswietl_macierz(macierz_odwrotnosci, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    M[i][j] += -(macierz_odwrotnosci[i][k] * macierz_bez_przekatnej[k][j]);
                }
            }
        }
        System.out.println();
        System.out.println("Macierz wyliczona na podstawie wzoru: M = -NR: ");
        wyswietl_macierz(M, n);

        //Interfejs
        System.out.println("Wybierz sposób rozwiązaina: ");
        System.out.println("1. Iteracja");
        System.out.println("2. Eps");
        System.out.print("Wybór: ");
        int wybor = scan.nextInt();

        switch (wybor) {
            case 1:
                System.out.print("Wpisz ilosc iteracji:  ");
                long iter = scan.nextLong();

                for (int i = 0; i < iter; i++) {
                    for (int j = 0; j < n; j++) {
                        for (int k = 0; k < n; k++) {
                            X1[j] += ((M[j][k] * X[k]) + (macierz_odwrotnosci[j][k] * B[k]));
                        }
                    }
                    for (int l = 0; l < n; l++) {
                        X[l] = X1[l];
                        X1[l] = 0;
                    }
                }
                break;

            case 2:
                System.out.print("Wpisz wartosc  eps: ");
                double eps = scan.nextDouble();
                int iteracje = 0;
                while (true) {

                    iteracje += 1;
                    for (int j = 0; j < n; j++) {
                        for (int k = 0; k < n; k++) {
                            X1[j] += ((M[j][k] * X[k]) + (macierz_odwrotnosci[j][k] * B[k]));
                        }
                    }
                    boolean warunek = true;
                    for (int i = 0; i < n; i++) {
                        warunek = warunek && (abs(X1[i] - X[i]) < eps);
                    }
                    if (warunek) {
                        System.out.println("Ilosc wykonanych iteracji ::     " + iteracje);
                        break;
                    }
                    for (int l = 0; l < n; l++) {
                        X[l] = X1[l];
                        X1[l] = 0;
                    }
                }
                break;

            default:
                System.out.println("Nie ma takiej opcji! ");
        }
        System.out.println("Wynik : Macierz x :  ");
        wyswietl_macierz_pojedyncza(X, n);
    }
}