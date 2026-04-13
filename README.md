# Diffie-Hellman Key Exchange Demo

This Java project demonstrates the Diffie-Hellman key exchange algorithm and proves that an eavesdropper cannot calculate the final shared secret key.

## Overview

The Diffie-Hellman key exchange allows two parties (Alice and Bob) to establish a shared secret over an insecure channel, even if an eavesdropper (Eve) can intercept all communications.

## How It Works

1. **Public Parameters**: Both parties agree on a large prime number `p` and a generator `g`
2. **Private Keys**: Each party generates a secret private key (`a` for Alice, `b` for Bob)
3. **Public Keys**: Each party calculates a public key:
   - Alice: `A = g^a mod p`
   - Bob: `B = g^b mod p`
4. **Exchange**: Public keys are exchanged over the insecure channel
5. **Shared Secret**: Each party calculates the shared secret:
   - Alice: `S = B^a mod p = (g^b)^a mod p = g^(ba) mod p`
   - Bob: `S = A^b mod p = (g^a)^b mod p = g^(ab) mod p`

## Security Proof

The eavesdropper knows:
- `p` (prime)
- `g` (generator) 
- `A = g^a mod p` (Alice's public key)
- `B = g^b mod p` (Bob's public key)

To find the shared secret `S = g^(ab) mod p`, the eavesdropper would need to solve the **Discrete Logarithm Problem**: find `a` from `A = g^a mod p` or find `b` from `B = g^b mod p`.

This is computationally infeasible for large primes (2048+ bits), making the algorithm secure.

## Running the Demo

To compile and run the program:

```bash
javac main.java
java DiffieHellmanDemo
```

## Demo Output

The program shows:
1. Private key generation for both users
2. Public key calculation and exchange
3. Shared secret calculation (both users get the same result)
4. Eavesdropper's failed attempt to calculate the secret
5. Security analysis explaining why the eavesdropper cannot succeed

## Classes

- `DiffieHellmanDemo`: Main class that orchestrates the demonstration
- `User`: Represents a participant in the key exchange
- `Eavesdropper`: Simulates an attacker trying to discover the shared secret

## Note

This demo uses small primes (p=23) for demonstration purposes. In real-world applications, primes of 2048+ bits are used, making brute-force attacks computationally impossible.
