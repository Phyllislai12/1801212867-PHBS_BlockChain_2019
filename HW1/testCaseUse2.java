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
        //Create a UTXO and change the following uuid.randomuuid ().tostring ().getbytes () to prevtx.getbytes () to return true
        UTXO utxo = new UTXO(UUID.randomUUID().toString().getBytes(),0);
        utxoPool.addUTXO(utxo,output);
        transaction.getInput(0).addSignature(getPublicKey(transaction.getRawDataToSign(0),myKeyPair));
        TxHandler txHandler = new TxHandler(utxoPool);
        boolean b1 =  txHandler.isValidTx(transaction);
        System.out.println(b1);
    }
    public void testMethod2() throws Exception {
        KeyPairGenerator myKeyGen= KeyPairGenerator.getInstance("RSA");
        myKeyGen.initialize(1024);
        KeyPair myKeyPair = myKeyGen.generateKeyPair();
        KeyPairGenerator myKeyGen1= KeyPairGenerator.getInstance("RSA");
        myKeyGen1.initialize(1024);
        KeyPair myKeyPair1 = myKeyGen.generateKeyPair();
        Transaction transaction = new Transaction();
        String prevTx = UUID.randomUUID().toString();
        Transaction.Output output = transaction.new Output(100,myKeyPair.getPublic());
        transaction.addInput(prevTx.getBytes(),0);
        transaction.addOutput(100,myKeyPair1.getPublic());
        UTXOPool utxoPool = new UTXOPool();
        UTXO utxo = new UTXO(prevTx.getBytes(),0);
        utxoPool.addUTXO(utxo,output);
        //Add the Signature attribute to the first input in the Transaction. Change myKeyPair1 below to myKeyPair below and return true
        transaction.getInput(0).addSignature(getPublicKey(transaction.getRawDataToSign(0),myKeyPair1));
        TxHandler txHandler = new TxHandler(utxoPool);
        boolean b1 =  txHandler.isValidTx(transaction);
        System.out.println(b1);
    }
    public void testMethod3() throws Exception {
        KeyPairGenerator myKeyGen= KeyPairGenerator.getInstance("RSA");
        myKeyGen.initialize(1024);
        KeyPair myKeyPair = myKeyGen.generateKeyPair();
        Transaction transaction = new Transaction();
        String prevTx = UUID.randomUUID().toString();
        Transaction.Output output = transaction.new Output(100,myKeyPair.getPublic());
        //Add two identical inputs to the Transaction, return false and remove the following input to return true
        transaction.addInput(prevTx.getBytes(),0);
        transaction.addInput(prevTx.getBytes(),0);
        transaction.addOutput(100,myKeyPair.getPublic());
        UTXOPool utxoPool = new UTXOPool();
        UTXO utxo = new UTXO(prevTx.getBytes(),0);
        utxoPool.addUTXO(utxo,output);
        transaction.getInput(0).addSignature(getPublicKey(transaction.getRawDataToSign(0),myKeyPair));
        transaction.getInput(1).addSignature(getPublicKey(transaction.getRawDataToSign(1),myKeyPair));
        TxHandler txHandler = new TxHandler(utxoPool);
        boolean b1 =  txHandler.isValidTx(transaction);
        System.out.println(b1);
    }
    public void testMethod4() throws Exception {
        KeyPairGenerator myKeyGen= KeyPairGenerator.getInstance("RSA");
        myKeyGen.initialize(1024);
        KeyPair myKeyPair = myKeyGen.generateKeyPair();
        Transaction transaction = new Transaction();
        String prevTx = UUID.randomUUID().toString();
        Transaction.Output output = transaction.new Output(100,myKeyPair.getPublic());
        transaction.addInput(prevTx.getBytes(),0);
        //Add an output to the Transaction, add a negative value, and return true if the value is positive
        transaction.addOutput(-1,myKeyPair.getPublic());
        UTXOPool utxoPool = new UTXOPool();
        UTXO utxo = new UTXO(prevTx.getBytes(),0);
        utxoPool.addUTXO(utxo,output);
        transaction.getInput(0).addSignature(getPublicKey(transaction.getRawDataToSign(0),myKeyPair));
        TxHandler txHandler = new TxHandler(utxoPool);
        boolean b1 =  txHandler.isValidTx(transaction);
        System.out.println(b1);
    }
    public void testMethod5() throws Exception {
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
    public void testMethod6() throws Exception {
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
        transactions[0]  = transaction;
        TxHandler txHandler = new TxHandler(utxoPool);
        Transaction[] result =  txHandler.handleTxs(transactions);
    }

}
