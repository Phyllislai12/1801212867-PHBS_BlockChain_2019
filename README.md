# 1801212867-PHBS_BlockChain_2019
# HW1
Lai Lin 赖琳 1801212867

##Summary

1. Pass in one Transaction at a time to isValidTx and iterate over each input of the Transaction to determine whether it is in the UTXO pool

2. Every time I pass in a Transaction to isValidTx, traverse the Transaction each input, output PublicKey, Transaction. GetRawDataToSign (I) and the input of the signature, the incoming to Crypto. VerifySignature method, if verification is successful, returns true

3. Create a Set<UTXO> collection that cannot be repeated, pass in a Transaction to isValidTx each time, iterate through the Transaction and add each UTXO created to the Set<UTXO> collection, and repeat UTXO if the Set<UTXO> collection adds false

4. Pass in one Transaction at a time to isValidTx, walk through each output of the Transaction to determine if the value is negative, and if it is negative, the method returns false and terminates

5. Each time a Transaction is passed into isValidTx, the input value of the Transaction is iterated over and the output value of the Transaction is iterated over and the accumulated value is compared. If the input accumulated value is greater than or equal to the output accumulated value, it passes the validation, otherwise false is returned
