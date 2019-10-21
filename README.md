# 1801212867-PHBS_BlockChain_2019

Lai Lin 赖琳 1801212867

# HW1

## Solution Summary

### Part 1
public class TxHandler {

/** Creates a public ledger whose current UTXOPool (collection of unspent
*	transaction outputs) is utxoPool. This should make a defensive copy of
*	utxoPool by using the UTXOPool(UTXOPool uPool) constructor.
*/

**Solution**：

Create a defensive copy: 

utxopool is a private property that cannot be invoked or modified outside of the class, and the value passed in when new cannot be modified

### Part 2
public TxHandler(UTXOPool utxoPool);

/** Returns true if
*	(1) all outputs claimed by tx are in the current UTXO pool,
*	(2) the signatures on each input of tx are valid,
*	(3) no UTXO is claimed multiple times by tx,
*	(4) all of tx’s output values are non-negative, and
*	(5) the sum of tx’s input values is greater than or equal to the sum of its output values; and false otherwise.
*/

**Solution**：

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


**Solution**：

* Create a Transaction HashSet that contains a non-repeatable Transaction
* Traverse the Transaction array, and skip this step to the next loop if the HashSet already exists.
* IsValidTx validates
* Adds the Transaction to the HashSet
* Changes the flag
* Adds to the UTXO pool
* Removes the UTXO pool.


## Test Summary

Generate the Signature object and sign the information with the private key. Generates the signature object with the specified algorithm, initializes the signature object with the private key, passes the data to be signed to the signature object (after initialization), and returns the number of bytes of the signature result.

### TestCaseUse

Test all the true situations

#### Use the testMethod1() method to test the txhandler.isvalidtx method

* Generate the key pair, create a Transaction object
* Generate a random number hash, create an output value of 100, public key mykeyping.getpublic ()
* Add an input to the Transaction 
* Add an output to the Transaction
* Create a UTXO pool
* Add the UTXO object to the UTXO pool
* Add Signature property to the first input in the Transaction, and create TxHandler. 
* Pass the Transaction into isValidTx for validation.

#### Use the testMethod2() method to test the txhandler.handletxs method

* Create a Transaction array
* Generate key pairs
* Create a Transaction object
* Generate a random number hash
* Create an output value of 100. Public key mykeyping.getpublic ()
* Add an input to the Transaction
* Add an output to the Transaction
* Create a UTXO pool
* Create a UTXO
* Add a UTXO object to the UTXO pool
* Add Signature attribute to the first input in the Transaction
* Add the Transaction into the Transaction array 
* Create TxHandler
* Pass in the Transaction array for verification and return.

### TestCaseUse2
Test false situations

#### testMethod1()
Test all outputs claimed by tx are in th current UTXO pool, if not, return false.

#### testMethod2()
Test the signatures on each input of tx are valid, if not, return false.

#### testMethod3()
Test no UTXO is claimed multiple times by tx, if not, return false.

#### testMethod4()
Test all of tx's output values are non-negative and the sum of tx's input values is greater than or equal to the sum of its output values, if not, return false.

#### testMethod5()
test the txhandler.isvalidtx method, all right, return true.

#### testMethod6()
test the txhandler.handletxs method, all right, return true.

# HW2
## Solution Summary
This class of constructors initializes an empty blockchain including UTXOPool and TransactionPool. 

The class also provides addBlock to add a valid block to the block chain and needs to set the data signature. 

The addTransaction method adds a transaction to the transaction pool; 

There are also methods to query blocks: 

getMaxHeightBlock

## Test Summary
### testCase1()
Test：
BlockChain(Block genesisBlock)

Block getMaxHeightBlock()

UTXOPool getMaxHeightUTXOPool() 

TransactionPool getTransactionPool()

boolean addBlock(Block block) 

void addTransaction(Transaction tx)


### The last test
public void testBlockChainInvalidPreviousBranch() test the false case of block validation
