public class HashFunction {
    private int prime;
    private int[] randA, randB;

    public HashFunction(int prime, int[] randA, int[] randB) {
        this.prime = prime;
        this.randA = randA;
        this.randB = randB;
    }
    
    public int getHash(int key, int i) {
        int hashCode = (randA[i] * key + randB[i]) % this.prime;
        return hashCode;
    }
    
}
