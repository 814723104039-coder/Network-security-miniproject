import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class DiffieHellmanDemo {
    
    // Common public parameters (prime and generator)
    private static final BigInteger PRIME = new BigInteger("23"); // Small prime for demonstration
    private static final BigInteger GENERATOR = new BigInteger("5"); // Generator
    
    public static void main(String[] args) {
        System.out.println("=== Diffie-Hellman Key Exchange Demo ===\n");
        
        // Create two users
        User alice = new User("Alice");
        User bob = new User("Bob");
        Eavesdropper eve = new Eavesdropper("Eve");
        
        // Step 1: Generate private keys
        System.out.println("Step 1: Users generate private keys");
        alice.generatePrivateKey();
        bob.generatePrivateKey();
        System.out.println("Alice's private key: " + alice.getPrivateKey());
        System.out.println("Bob's private key: " + bob.getPrivateKey());
        System.out.println();
        
        // Step 2: Calculate public keys
        System.out.println("Step 2: Users calculate public keys");
        alice.calculatePublicKey();
        bob.calculatePublicKey();
        System.out.println("Alice's public key: " + alice.getPublicKey());
        System.out.println("Bob's public key: " + bob.getPublicKey());
        System.out.println();
        
        // Step 3: Exchange public keys (eavesdropper can see these)
        System.out.println("Step 3: Public keys are exchanged (eavesdropper can see them)");
        eve.interceptPublicKey(alice.getPublicKey(), bob.getPublicKey());
        System.out.println("Eve intercepted both public keys");
        System.out.println();
        
        // Step 4: Calculate shared secret
        System.out.println("Step 4: Users calculate shared secret");
        alice.calculateSharedSecret(bob.getPublicKey());
        bob.calculateSharedSecret(alice.getPublicKey());
        System.out.println("Alice's shared secret: " + alice.getSharedSecret());
        System.out.println("Bob's shared secret: " + bob.getSharedSecret());
        System.out.println();
        
        // Step 5: Verify shared secrets match
        System.out.println("Step 5: Verification");
        if (alice.getSharedSecret().equals(bob.getSharedSecret())) {
            System.out.println("✓ SUCCESS: Both users have the same shared secret!");
        } else {
            System.out.println("✗ FAILURE: Shared secrets do not match!");
        }
        System.out.println();
        
        // Step 6: Eavesdropper attempts to calculate the secret
        System.out.println("Step 6: Eavesdropper attempts to calculate the secret");
        eve.attemptToCalculateSecret();
        System.out.println();
        
        // Security explanation
        System.out.println("=== Security Analysis ===");
        System.out.println("The eavesdropper knows:");
        System.out.println("- Prime (p): " + PRIME);
        System.out.println("- Generator (g): " + GENERATOR);
        System.out.println("- Alice's public key (g^a mod p): " + alice.getPublicKey());
        System.out.println("- Bob's public key (g^b mod p): " + bob.getPublicKey());
        System.out.println();
        System.out.println("To find the shared secret, the eavesdropper needs to solve:");
        System.out.println("Discrete Logarithm Problem: Find 'a' from g^a mod p = public_key");
        System.out.println("This is computationally infeasible for large primes!");
        System.out.println();
        System.out.println("The actual shared secret is: " + alice.getSharedSecret());
        System.out.println("Eve's calculated value: " + eve.getAttemptedSecret());
        System.out.println("Eve " + (eve.getAttemptedSecret().equals(alice.getSharedSecret()) ? "CORRECTLY" : "INCORRECTLY") + " calculated the secret!");
    }
}

class User {
    private String name;
    private BigInteger privateKey;
    private BigInteger publicKey;
    private BigInteger sharedSecret;
    private Random random;
    
    public User(String name) {
        this.name = name;
        this.random = new SecureRandom();
    }
    
    public void generatePrivateKey() {
        // Generate private key (1 < private key < prime)
        int max = DiffieHellmanDemo.PRIME.intValue() - 2;
        privateKey = BigInteger.valueOf(random.nextInt(max) + 2);
    }
    
    public void calculatePublicKey() {
        // Public key = g^private_key mod p
        publicKey = DiffieHellmanDemo.GENERATOR.modPow(privateKey, DiffieHellmanDemo.PRIME);
    }
    
    public void calculateSharedSecret(BigInteger otherPublicKey) {
        // Shared secret = other_public_key^private_key mod p
        sharedSecret = otherPublicKey.modPow(privateKey, DiffieHellmanDemo.PRIME);
    }
    
    public BigInteger getPrivateKey() { return privateKey; }
    public BigInteger getPublicKey() { return publicKey; }
    public BigInteger getSharedSecret() { return sharedSecret; }
    public String getName() { return name; }
}

class Eavesdropper {
    private String name;
    private BigInteger alicePublicKey;
    private BigInteger bobPublicKey;
    private BigInteger attemptedSecret;
    
    public Eavesdropper(String name) {
        this.name = name;
    }
    
    public void interceptPublicKey(BigInteger alicePubKey, BigInteger bobPubKey) {
        this.alicePublicKey = alicePubKey;
        this.bobPublicKey = bobPubKey;
    }
    
    public void attemptToCalculateSecret() {
        System.out.println(name + " is attempting to calculate the shared secret...");
        System.out.println(name + " knows: p=" + DiffieHellmanDemo.PRIME + ", g=" + DiffieHellmanDemo.GENERATOR);
        System.out.println(name + " intercepted: Alice's public key=" + alicePublicKey + ", Bob's public key=" + bobPublicKey);
        
        // Brute force attempt (only works for small primes)
        System.out.println(name + " attempts brute force (only possible with small primes):");
        
        BigInteger foundPrivateKeyA = null;
        BigInteger foundPrivateKeyB = null;
        
        // Try to find Alice's private key
        for (int i = 2; i < DiffieHellmanDemo.PRIME.intValue(); i++) {
            BigInteger testKey = BigInteger.valueOf(i);
            BigInteger calculatedPubKey = DiffieHellmanDemo.GENERATOR.modPow(testKey, DiffieHellmanDemo.PRIME);
            if (calculatedPubKey.equals(alicePublicKey)) {
                foundPrivateKeyA = testKey;
                System.out.println("Found Alice's private key: " + foundPrivateKeyA);
                break;
            }
        }
        
        // Try to find Bob's private key
        for (int i = 2; i < DiffieHellmanDemo.PRIME.intValue(); i++) {
            BigInteger testKey = BigInteger.valueOf(i);
            BigInteger calculatedPubKey = DiffieHellmanDemo.GENERATOR.modPow(testKey, DiffieHellmanDemo.PRIME);
            if (calculatedPubKey.equals(bobPublicKey)) {
                foundPrivateKeyB = testKey;
                System.out.println("Found Bob's private key: " + foundPrivateKeyB);
                break;
            }
        }
        
        if (foundPrivateKeyA != null) {
            attemptedSecret = bobPublicKey.modPow(foundPrivateKeyA, DiffieHellmanDemo.PRIME);
            System.out.println("Calculated shared secret using Alice's private key: " + attemptedSecret);
        } else if (foundPrivateKeyB != null) {
            attemptedSecret = alicePublicKey.modPow(foundPrivateKeyB, DiffieHellmanDemo.PRIME);
            System.out.println("Calculated shared secret using Bob's private key: " + attemptedSecret);
        } else {
            System.out.println("Could not find private keys (would be impossible with large primes)");
            attemptedSecret = BigInteger.ZERO;
        }
        
        System.out.println("Note: With large primes (2048+ bits), brute force would take billions of years!");
    }
    
    public BigInteger getAttemptedSecret() { return attemptedSecret; }
}