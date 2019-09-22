package cn.distribution.config;

import junit.framework.TestCase;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

public class testCaseUse extends TestCase {

    @Override
    protected void setUp() throws Exception {

        // TODO Auto-generated method stub

        super.setUp();

        System.out.println("setUp , hashCode = "+hashCode());

    }

//    @Override
//    protected void tearDown() throws Exception {
//
//// TODO Auto-generated method stub
//
//        super.tearDown();
//
//        System.out.println("tearDown,hashCode = "+hashCode());
//
//    }
private  static byte[] getPublicKey(byte[] key,KeyPair myKeyPair) throws Exception {
    try{
        byte[] info =key;
        //Generate the Signature object and sign the information with the private key.
        Signature mySig = Signature.getInstance("SHA256withRSA");  //Generates a signature object using the specified algorithm
        mySig.initSign(myKeyPair.getPrivate());  //Initializes the signature object with the private key
        mySig.update(info);  //Transfer the data to be signed to the signed object (after initialization)
        byte[] sigResult = mySig.sign();  //Returns the number of signed result bytes
        return sigResult;
    }catch (Exception ex){ex.printStackTrace();}
    return null;
}

    public void testMethod1() throws Exception {
        KeyPairGenerator myKeyGen= KeyPairGenerator.getInstance("RSA");
        myKeyGen.initialize(1024);
        KeyPair myKeyPair = myKeyGen.generateKeyPair();
        Transaction transaction = new Transaction();
        String prevTx = UUID.randomUUID().toString();
        Transaction.Output output = transaction.new Output(100,myKeyPair.getPublic());
        transaction.addInput(prevTx.getBytes(),0);
        transaction.addOutput(100,myKeyPair.getPublic());
        UTXOPool utxoPool = new UTXOPool();
        UTXO utxo = new UTXO(prevTx.getBytes(),0);
        utxoPool.addUTXO(utxo,output);
        transaction.getInput(0).addSignature(getPublicKey(transaction.getRawDataToSign(0),myKeyPair));
        TxHandler txHandler = new TxHandler(utxoPool);
       boolean b1 =  txHandler.isValidTx(transaction);
    }

    public void testMethod2() throws Exception {
        Transaction[] transactions  = new Transaction[1];
        KeyPairGenerator myKeyGen= KeyPairGenerator.getInstance("RSA");
        myKeyGen.initialize(1024);
        KeyPair myKeyPair = myKeyGen.generateKeyPair();
        Transaction transaction = new Transaction();
        String prevTx = UUID.randomUUID().toString();
        Transaction.Output output = transaction.new Output(100,myKeyPair.getPublic());
        transaction.addInput(prevTx.getBytes(),0);
        transaction.addOutput(100,myKeyPair.getPublic());
        UTXOPool utxoPool = new UTXOPool();
        UTXO utxo = new UTXO(prevTx.getBytes(),0);
        utxoPool.addUTXO(utxo,output);
        transaction.getInput(0).addSignature(getPublicKey(transaction.getRawDataToSign(0),myKeyPair));
        transaction.setHash(UUID.randomUUID().toString().getBytes());
        transactions[0]  = transaction;

        TxHandler txHandler = new TxHandler(utxoPool);
        Transaction[] result =  txHandler.handleTxs(transactions);
    }

}
