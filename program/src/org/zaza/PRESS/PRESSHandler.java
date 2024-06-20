package org.zaza.PRESS;
import java.math.BigInteger;
import java.util.*;

import org.zaza.utils.*;;

public class PRESSHandler {
    
    private static BigInteger[] splitSecret(byte[] secretInByte, int chunks) {
        BigInteger[] result = new BigInteger[chunks];
        int counter = 0;
        int i = 0;
        byte[] pecahanRahasia;
        while (i < chunks) {
            if (secretInByte.length % chunks == 0) {
                pecahanRahasia = Arrays.copyOfRange(secretInByte, counter, counter+(secretInByte.length/chunks));
                // result[i] = new BigInteger(pecahanRahasia);                    
                result[i] = new BigInteger(HelperTools.prependWithZero(pecahanRahasia));                    
                counter += (secretInByte.length/chunks);
            }
            else{
                pecahanRahasia = Arrays.copyOfRange(secretInByte, counter, (counter + 1) + (secretInByte.length/chunks));
                // result[i] = new BigInteger(pecahanRahasia);                    
                result[i] = new BigInteger(HelperTools.prependWithZero(pecahanRahasia));                    
                counter += (secretInByte.length/chunks) + 1;
            }
            i++;
        }
        return result;
    }

    private static byte[] join(BigInteger[] chunks, int secretLength) {        
        byte[] tmpJoinedData = new byte[chunks.length * chunks[0].toByteArray().length];

        int counter = 0;
        int i = 0;
        while (i < chunks.length) {
            byte[] chunk = chunks[i].toByteArray();
            int chunkLength = chunk.length;
            if (chunk[0] == (byte)0x00) {
                chunkLength -= 1;
                System.arraycopy(chunks[i].toByteArray(), 1, tmpJoinedData, counter, chunkLength);                
            }
            else {
                System.arraycopy(chunks[i].toByteArray(), 0, tmpJoinedData, counter, chunkLength);
            }
            // counter += chunks[i].toByteArray().length;
            counter += chunkLength;
            i++;
        }
        // System.out.println("tmpJoinedData:");
        // HelperTools.printUnsignedByteArray(tmpJoinedData);
        byte[] joinedData = new byte[secretLength];
        System.arraycopy(tmpJoinedData, 0, joinedData, 0, secretLength);
        return joinedData;
    }

    private static BigInteger findPrimeAfterLargestChunk(BigInteger input) {
        BigInteger result = input.nextProbablePrime();
        return result;
    }

    private static BigInteger findLargestChunk(BigInteger[] chunks) {
        BigInteger largest = new BigInteger("0");
        for (int i = 0; i < chunks.length; i++) {
            if (chunks[i].compareTo(largest) == 1) {
                largest = chunks[i];
            }
        }
        return largest;
    }

    private static BigInteger addMod(BigInteger a, BigInteger b, BigInteger primeNum) {
        return (a.add(b)).mod(primeNum);
    }

    private static BigInteger mulMod(BigInteger a, BigInteger b, BigInteger primeNum) {
        return (a.multiply(b)).mod(primeNum);
    }

    private static BigInteger generateRandomCoefficients(BigInteger prime) {
        BigInteger maxLimit = prime;
        BigInteger minLimit = new BigInteger("1");
        BigInteger bigInteger = maxLimit.subtract(minLimit);
        Random randNum = new Random();
        int len = maxLimit.bitLength();
        BigInteger res = new BigInteger(len, randNum);
        if (res.compareTo(minLimit) < 0)
            res = res.add(minLimit);
        if (res.compareTo(bigInteger) >= 0)
            res = res.mod(bigInteger).add(minLimit);
        return res;
    }    

    private static BigInteger[] generatePoly(BigInteger prime, int degree, BigInteger chunk) {
        BigInteger[] poly = new BigInteger[degree+1];
        for (int i = 0; i < poly.length; i++) {
            poly[i] = generateRandomCoefficients(prime);
        }
        // set y intercept
        poly[0] = chunk;

        return poly;
    }

    private static BigInteger createShare(BigInteger[] poly, BigInteger x, BigInteger primeNum) {
        BigInteger result = new BigInteger("0");
        for (int i = poly.length-1; i >= 0 ; i--) {
            result = addMod(mulMod(result, x, primeNum), poly[i], primeNum);
        }
        return result;
    }

    private static BigInteger[] createShares(BigInteger[] poly, int numberShare, BigInteger primeNum) {
        BigInteger[] hasilShares = new BigInteger[numberShare];
        // System.out.println("Shares: ");
        for (int i = 1; i <= numberShare; i++) {
            hasilShares[i-1] = createShare(poly, BigInteger.valueOf(i), primeNum);
            // System.out.println("share ke-" + i + ": " + hasilShares[i-1].toString());
        }
        return hasilShares;
    }
 
    // UP FROM multiplyPolynomials TO getPolynomial function IS FOR LAGRANGE INTERPOLATION

    private static BigInteger[] multiplyPolynomials(BigInteger[] a, BigInteger[] b, BigInteger primeNum) {
        int degA = a.length;
        int degB = b.length;
        BigInteger[] result = new BigInteger[degA + degB - 1];

        // Initialize the result array elements to BigInteger.ZERO
        for (int i = 0; i < result.length; i++) {
            result[i] = BigInteger.ZERO;
        }

        for (int i = 0; i < degA; i ++) {
            for (int j = 0; j < degB; j ++) {
                result[i + j] = result[i + j].add(mulMod(a[i], b[j], primeNum)).mod(primeNum);
            }
        }
        return result;
    }

    private static BigInteger[] addPolynomials(BigInteger[] a, BigInteger[] b, BigInteger prime) {
        BigInteger[] result = new BigInteger[a.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = addMod(a[i], b[i], prime);
        }
        return result;
    }

    private static BigInteger[] getDeltaPolynomial(BigInteger[] xs, int xpos, BigInteger prime) {
        BigInteger[] poly = {BigInteger.ONE};
        BigInteger denom = BigInteger.ONE;

        for (int i = 0; i < xs.length; i++) {
            if (i != xpos) {
                BigInteger[] currentTerm = {BigInteger.ONE, xs[i].negate()};
                denom = mulMod(denom, xs[xpos].subtract(xs[i].mod(prime)), prime);
                poly = multiplyPolynomials(poly, currentTerm, prime);
            }
        }

        return scalePoly(poly, denom.modInverse(prime), prime);
    }

    private static BigInteger[] scalePoly(BigInteger[] poly, BigInteger denom, BigInteger prime) {
        BigInteger[] result = new BigInteger[poly.length];
        // denom = denom.modInverse(prime);
        for (int i = 0; i < poly.length; i++) {
            result[i] = mulMod(poly[i], denom, prime);
        }
        return result;
    } 

    private static BigInteger[] getPolynomial(BigInteger[] xs, BigInteger[] ys, BigInteger prime) {
        int degree = xs.length;
        BigInteger[][] deltas = new BigInteger[degree][degree];
        BigInteger[] result = new BigInteger[degree];

        // Initialize the result array elements to BigInteger.ZERO
        for (int i = 0; i < result.length; i++) {
            result[i] = BigInteger.ZERO;
        }

        for (int i = 0; i < degree; i ++) {
            deltas[i] = getDeltaPolynomial(xs, i, prime);
        }

        for (int i = 0; i < degree; i ++) {
            // System.out.println(Arrays.toString(deltas[i]));
            result = addPolynomials(result, scalePoly(deltas[i], ys[i], prime), prime);
            
        }

        return result;

    }

    // public static BigInteger[] dealPhase(BigInteger[] chunks, BigInteger primeNum, int P, int t, int n) {
    //     // BigInteger[] polynomial = {chunks[0], new BigInteger("263")};
    //     // BigInteger[] polynomial = {chunks[0], new BigInteger("22")};
    //     BigInteger[] polynomial = generatePoly(primeNum, 1, chunks[0]);
    //     BigInteger[] shares = null;
    //     for (int i = 1; i <= P; i++) {
    //         int shareNumber = 0;
    //         if (i <= (P-2)) {
    //             shareNumber = i + 1;
    //         }
    //         else if (i == (P-1)) {
    //             shareNumber = t-1;
    //         }
    //         else if (i == P) {
    //             shareNumber = n;
    //         }
    //         shares = createShares(polynomial, shareNumber, primeNum);
    //         ArrayList<BigInteger> sharesList = new ArrayList<>(Arrays.asList(shares));
    //         if (i < P) {
    //             sharesList.add(0, chunks[i]);                
    //         }
    //         polynomial = sharesList.toArray(polynomial);
    //     }
    //     return shares;
    // }

    public static List<List<TLV>> computeShare(BigInteger[] chunks, BigInteger primeNum, int secretLength, int P, int t, int n) {
        long start = System.nanoTime();

        BigInteger[] polynomial = generatePoly(primeNum, 1, chunks[0]);
        BigInteger[] shares = null;
        for (int i = 1; i <= P; i++) {
            int shareNumber = 0;
            if (i <= (P-2)) {
                shareNumber = i + 1;
            }
            else if (i == (P-1)) {
                shareNumber = t-1;
            }
            else if (i == P) {
                shareNumber = n;
            }
            shares = createShares(polynomial, shareNumber, primeNum);
            ArrayList<BigInteger> sharesList = new ArrayList<>(Arrays.asList(shares));
            if (i < P) {
                sharesList.add(0, chunks[i]);                
            }
            polynomial = sharesList.toArray(polynomial);
        }

        //to print share size
        StringBuilder sb = new StringBuilder();
        sb.append("Size of shares:");
        int totalSize = 0;
        for (BigInteger s : shares) {
            totalSize += s.toByteArray().length;
            sb.append(s.toByteArray().length);
            sb.append("|");
        }
        sb.append("total size: ");
        sb.append(totalSize);
        System.out.println(sb.toString() );

        // Put final result into list of TLV
        List<List<TLV>> tlvShares = new ArrayList<List<TLV>>();
        for (int i = 0; i < shares.length; i++) {
            List<TLV> tlvPerShare = new ArrayList<TLV>();
            // public static final byte TYPE_THRESHOLD = 1;
            // public static final byte TYPE_CHUNK_NUMBER = 2;
            // public static final byte TYPE_PRIME = 3;
            // public static final byte TYPE_SECRET_LENGTH = 4;
            // public static final byte TYPE_X_COORDINATE = 5;
            // public static final byte TYPE_Y_SHARE = 6;
            byte[] thresholdBytes = new byte[Integer.BYTES];
            thresholdBytes = HelperTools.intToBytes(t); //max threshold is 255 TO DO DI BUKUTA: tulis maksimum threshold
            TLV threshold = new TLV(TLV.TYPE_THRESHOLD, thresholdBytes.length, thresholdBytes);

            byte[] chunkNumberBytes = new byte[Integer.BYTES];
            chunkNumberBytes = HelperTools.intToBytes(P);
            TLV chunkNumber = new TLV(TLV.TYPE_CHUNK_NUMBER, chunkNumberBytes.length, chunkNumberBytes);

            TLV prime = new TLV(TLV.TYPE_PRIME, primeNum.toByteArray().length, primeNum.toByteArray());

            TLV secretLengthTLV = new TLV(TLV.TYPE_SECRET_LENGTH, Integer.BYTES, HelperTools.intToBytes(secretLength));

            TLV xCoordinate = new TLV(TLV.TYPE_X_COORDINATE, BigInteger.valueOf(i+1).toByteArray().length, BigInteger.valueOf(i+1).toByteArray());

            TLV yShare = new TLV(TLV.TYPE_Y_SHARE, shares[i].toByteArray().length, shares[i].toByteArray());

            tlvPerShare.add(threshold);
            tlvPerShare.add(chunkNumber);
            tlvPerShare.add(prime);
            tlvPerShare.add(secretLengthTLV);
            tlvPerShare.add(xCoordinate);
            tlvPerShare.add(yShare);

            tlvShares.add(tlvPerShare);
        }

        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.printf( "putSharesInFiles: Elapsed time in dealPhase: %,d nanosecond\n",  timeElapsed);
        return tlvShares;
    }

    public static List<List<TLV>> dealSecret(byte[] arr, int P, int t, int n) {
        long start = System.nanoTime();

        BigInteger[] chunks = splitSecret(arr, P);
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.printf( "putSharesInFiles: dealSecret: Elapsed time to call split(): %,d nanosecond\n",  timeElapsed);

        start = System.nanoTime();
        BigInteger largestChunk = findLargestChunk(chunks);
        finish = System.nanoTime();
        timeElapsed = finish - start;
        System.out.printf( "putSharesInFiles: dealSecret: Elapsed time to call findLargestChunk(): %,d nanosecond, largest chunk: %s\n",  timeElapsed, largestChunk.toString());

        start = System.nanoTime();
        BigInteger prime = findPrimeAfterLargestChunk(largestChunk);
        finish = System.nanoTime();
        timeElapsed = finish - start;
        System.out.printf( "putSharesInFiles: dealSecret: Elapsed time to call nextPrime(): %,d nanosecond, prime number: %s\n",  timeElapsed, prime.toString());


        start = System.nanoTime();
        List<List<TLV>> result = PRESSHandler.computeShare(chunks, prime, arr.length, P, t, n);
        finish = System.nanoTime();
        timeElapsed = finish - start;
        System.out.printf( "putSharesInFiles: dealSecret: Elapsed time to call dealPhase(): %,d nanosecond\n",  timeElapsed);

        return result;
    }

    private static BigInteger[] reverse(BigInteger a[]) 
    { 
        ArrayList<BigInteger> aList = new ArrayList<>(Arrays.asList(a));
        Collections.reverse(aList); 

        BigInteger[] arrAfterArrList = new BigInteger[aList.size()];
        arrAfterArrList = aList.toArray(arrAfterArrList);

        return arrAfterArrList;
    } 

    // private static BigInteger[] reconstructPhase(BigInteger[] xs, BigInteger[] ys, BigInteger prime, int P, int t, int n) {
    //     BigInteger[] result = new BigInteger[P];
    //     BigInteger[] priorPoly = null;
    //     int share = 0;
    //     for(int i = P; i >= 1; i--) {
    //         if (i == P) {
    //             share = t;
    //             priorPoly = getPolynomial(xs, ys, prime);
    //             result[i-1] = priorPoly[(priorPoly.length-1)]; 
    //             ys = reverse(Arrays.copyOfRange(priorPoly, 0, (priorPoly.length-1)));
    //             continue;
    //         }
    //         else {
    //             if (i == (P-1)) {
    //                 share = P;
    //             }
    //             else if (i < (P-1)) {
    //                 share = i+1;
    //             }
    //             ys = Arrays.copyOfRange(ys, 0, share);
    //             BigInteger[] xAfterFirstLagrange = new BigInteger[ys.length];
    //             for (int j = 0; j < ys.length; j++) {
    //                 xAfterFirstLagrange[j] = BigInteger.valueOf(j + 1);
    //             }
    //             priorPoly = getPolynomial(xAfterFirstLagrange, ys, prime);
    //             result[i-1] = priorPoly[(priorPoly.length-1)]; 
    //             ys = reverse(Arrays.copyOfRange(priorPoly, 0, (priorPoly.length-1)));    
    //         }
    //     }
    //     return result;
    // }

    public static byte[] reconstructData(List<List<TLV>> parts) {
        long start = System.nanoTime();

        BigInteger[] xs = new BigInteger[parts.size()];
        BigInteger[] ys = new BigInteger[parts.size()];
        BigInteger prime = null;
        int P = 0; 
        int t = -1;
        // int n = -1;

        TLV secretLengthTLV = null;
        for (int i = 0; i < parts.size(); i++) {
            List<TLV> listTLVShare = parts.get(i);
            TLV thresholdTLV = null;
            TLV chunkNumberTLV = null;
            TLV primeTLV = null;
            TLV xCoordinateTLV = null;
            TLV yShareTLV = null;
            for (int j = 0; j < listTLVShare.size(); j++) {
                switch (listTLVShare.get(j).getType()) {
                    case TLV.TYPE_THRESHOLD:
                        thresholdTLV = listTLVShare.get(j);
                        break;
                
                    case TLV.TYPE_CHUNK_NUMBER:
                        chunkNumberTLV = listTLVShare.get(j);
                        break;
                
                    case TLV.TYPE_PRIME:
                        primeTLV = listTLVShare.get(j);
                        break;
                
                    case TLV.TYPE_SECRET_LENGTH:
                        secretLengthTLV = listTLVShare.get(j);
                        break;
                
                    case TLV.TYPE_X_COORDINATE:
                        xCoordinateTLV = listTLVShare.get(j);
                        break;
                
                    case TLV.TYPE_Y_SHARE:
                        yShareTLV = listTLVShare.get(j);
                        break;
                
                    default:
                        break;
                }
            }
            // BigInteger[] xs = new BigInteger[parts.size()];
            // BigInteger[] ys = new BigInteger[parts.size()];
            // BigInteger prime;
            // int P; 
            // int t;
            // int n;
            xs[i] = new BigInteger(xCoordinateTLV.getValueAsBinary());
            ys[i] = new BigInteger(yShareTLV.getValueAsBinary());
            prime = new BigInteger(primeTLV.getValueAsBinary());
            P = HelperTools.bytesToint(chunkNumberTLV.getValueAsBinary());
            t = HelperTools.bytesToint(thresholdTLV.getValueAsBinary());
                            
        }

        BigInteger[] result = new BigInteger[P];
        BigInteger[] priorPoly = null;
        int share = 0;
        for(int i = P; i >= 1; i--) {
            if (i == P) {
                share = t;
                priorPoly = getPolynomial(xs, ys, prime);
                result[i-1] = priorPoly[(priorPoly.length-1)]; 
                ys = reverse(Arrays.copyOfRange(priorPoly, 0, (priorPoly.length-1)));
                continue;
            }
            else {
                if (i == (P-1)) {
                    share = P;
                }
                else if (i < (P-1)) {
                    share = i+1;
                }
                ys = Arrays.copyOfRange(ys, 0, share);
                BigInteger[] xAfterFirstLagrange = new BigInteger[ys.length];
                for (int j = 0; j < ys.length; j++) {
                    xAfterFirstLagrange[j] = BigInteger.valueOf(j + 1);
                }
                priorPoly = getPolynomial(xAfterFirstLagrange, ys, prime);
                result[i-1] = priorPoly[(priorPoly.length-1)]; 
                ys = reverse(Arrays.copyOfRange(priorPoly, 0, (priorPoly.length-1)));    
            }
        }
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.printf( "Elapsed time in reconstructData(): %,d nanosecond\n",  timeElapsed);

        return join(result, HelperTools.bytesToint(secretLengthTLV.getValueAsBinary()));
    }
    // TODO di BUKUTA = PRESS require multiple Lagrange compared to SSS only once
    // TODO di BUKUTA = Jelaskan handling urutan (x, y) dalam skema PRESS

}
