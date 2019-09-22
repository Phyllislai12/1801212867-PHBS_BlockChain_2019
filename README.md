# 1801212867-PHBS_BlockChain_2019
# HW1
Lai Lin 赖琳 1801212867

## Summary

### Part 1
public class TxHandler {

/** Creates a public ledger whose current UTXOPool (collection of unspent
*	transaction outputs) is utxoPool. This should make a defensive copy of
*	utxoPool by using the UTXOPool(UTXOPool uPool) constructor.
*/

Solution：

Create a defensive copy: utxopool is a private property that cannot be invoked or modified outside of the class, and the value passed in when new cannot be modified

### Part 2
public TxHandler(UTXOPool utxoPool);

/** Returns true if
*	(1) all outputs claimed by tx are in the current UTXO pool,
*	(2) the signatures on each input of tx are valid,
*	(3) no UTXO is claimed multiple times by tx,
*	(4) all of tx’s output values are non-negative, and
*	(5) the sum of tx’s input values is greater than or equal to the sum of its output values; and false otherwise.
*/

Solution：

1. Input one Transaction at a time to isValidTx and iterate over each input of the Transaction to determine whether it is in the UTXO pool.

2. Every time when input a Transaction to isValidTx, traverse the Transaction each input to gain output PublicKey, Transaction. GetRawDataToSign(i) and the input of the signature, going to Crypto. VerifySignature method, if verification is successful, returns true.

3. Create a Set<UTXO> collection that cannot be repeated, input one Transaction to isValidTx each time, iterate through the Transaction and add each UTXO created to the Set<UTXO> collection, and repeat UTXO if adding the Set<UTXO> collection return false.

4. Input one Transaction at a time to isValidTx, walk through each output of the Transaction to determine if the value is negative, and if it is negative, the method returns false and terminates.

5. Each time one Transaction is input into isValidTx, the input value of the Transaction is iterated over and the output value of the Transaction is iterated over and bothe of them are accumulated seperately then compared this two value. If the input value of the Transaction is greater than or equal to the output value of the Transaction, it passes validation, otherwise false is returned

### Part 3
public boolean isValidTx(Transaction tx);

/** Handles each epoch by receiving an unordered array of proposed
*	transactions, checking each transaction for correctness,
*	returning a mutually valid array of accepted transactions,
*	and updating the current UTXO pool as appropriate.
*/

