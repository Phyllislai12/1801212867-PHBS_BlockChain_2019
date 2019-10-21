// Block Chain should maintain only limited block nodes to satisfy the functions
// You should not have all the blocks added to the block chain in memory
// as it would cause a memory overflow.

import java.util.*;

public class BlockChain {
    private HashMap<ByteArrayWrapper, BlockNode> blockChain;
    private LinkedHashMap<ByteArrayWrapper,Block> blocks;
    private LinkedHashMap<ByteArrayWrapper,Integer> endBlocksHeight;
    private BlockNode maxHeightNode;
    private TransactionPool txPool;

    public static final int CUT_OFF_AGE = 10;
    public class BlockNode {
        public Block b;
        @SuppressWarnings("unused")
        public BlockNode parent;
        public ArrayList<BlockNode> children;
        public int height;
        // UTXO pool for making a new block on top of this block
        private UTXOPool uPool;


        public BlockNode(Block b, BlockNode parent, UTXOPool uPool) {
            this.b = b;
            this.parent =  parent;
            children = new ArrayList<>();
            this.uPool = uPool;
            if (parent != null) {
                height = parent.height + 1;
                parent.children.add(this);
            } else {
                height = 1;
            }
        }
        public ArrayList<BlockNode> getNode_childNodes() {
            return this.children;
        }
        public UTXOPool getUTXOPoolCopy() {
            return new UTXOPool(uPool);
        }

        public UTXOPool getNode_UTXOPool() {
            return this.uPool;
        }

        public Block getNode_block() {
            return this.b;
        }

        public int getNode_height() {
            return this.height;
        }

        public BlockNode getNode_parentNode() {
            return this.parent;
        }

    }




    /**
     * create an empty block chain with just a genesis block. Assume {@code genesisBlock} is a valid
     * block
     */
    public BlockChain(Block genesisBlock) {
        blocks = new LinkedHashMap<ByteArrayWrapper,Block>();
        endBlocksHeight = new LinkedHashMap<ByteArrayWrapper,Integer>();
        blockChain = new HashMap<>();
        UTXOPool utxoPool = new UTXOPool();
        addCoinbaseToUTXOPool(genesisBlock, utxoPool);
        BlockNode genesisNode = new BlockNode(genesisBlock, null, utxoPool);
        blockChain.put(wrap(genesisBlock.getHash()), genesisNode);
        blocks.put(new ByteArrayWrapper(genesisBlock.getHash()), genesisBlock);
        updateEndBlocksHeight(new ByteArrayWrapper(genesisBlock.getHash()));
        txPool = new TransactionPool();
        Transaction tx = genesisBlock.getCoinbase();
        txPool.addTransaction(tx);
        maxHeightNode = genesisNode;

    }

    /** Get the UTXOPool for mining a new block on top of max height block */
    public UTXOPool getMaxHeightUTXOPool() {
        // IMPLEMENT THIS
        return getNode_maxHeight(this.maxHeightNode).getNode_UTXOPool();
    }

    /**
     * Get the maximum height block
     */
    public Block getMaxHeightBlock() {
        int current_max_height = 0;
        ByteArrayWrapper hash = null;
        Iterator iter = endBlocksHeight.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            if((Integer)val>current_max_height){
                hash = (ByteArrayWrapper)key;
                current_max_height = (Integer)val;
            }
        }
        return blocks.get(hash);
    }

    private void updateEndBlocksHeight(ByteArrayWrapper hash){
        ByteArrayWrapper lastBlockHash = new ByteArrayWrapper(blocks.get(hash).getPrevBlockHash());
        if(endBlocksHeight.get(lastBlockHash)!=null){
            int oldHeight = endBlocksHeight.get(lastBlockHash);
            endBlocksHeight.remove(lastBlockHash);
            endBlocksHeight.put(hash,oldHeight+1);
        }
        else{
            Integer newHeight = getEndBlocksBranch(hash).size();
            endBlocksHeight.put(hash,newHeight);

        }
    }

    public LinkedHashMap<ByteArrayWrapper,Block> getEndBlocksBranch(ByteArrayWrapper hash){
//        if(totalCount==0){
//            return new LinkedHashMap<ByteArrayWrapper,Block>();
//        }
        List<Block> tempList = new ArrayList<Block>();
        LinkedHashMap<ByteArrayWrapper,Block> result = new LinkedHashMap<ByteArrayWrapper,Block>();
        while(true){
            Block tempBlock = (Block)(this.blocks.get(hash));
            tempList.add(tempBlock);
            //有可能删除区块时，把创世块都删掉了，所以不光要previousHash 存在，还要验证他是否还在链上
            if(tempBlock.getPrevBlockHash()!=null&&this.blocks.containsKey(new ByteArrayWrapper(tempBlock.getPrevBlockHash()))){
                hash = new ByteArrayWrapper(tempBlock.getPrevBlockHash());
            }
            else{
                break;
            }

        }
        Collections.reverse(tempList);
        for(int i=0;i<tempList.size();i++){
            result.put(new ByteArrayWrapper(tempList.get(i).getHash()),tempList.get(i));
        }
        return result;
    }


    /**
     * Get the transaction pool to mine a new block
     */
    public TransactionPool getTransactionPool() {
        return txPool;
    }

    /**
     * Add {@code block} to the block chain if it is valid. For validity, all transactions should be
     * valid and block should be at {@code height > (maxHeight - CUT_OFF_AGE)}.
     * <p>
     * <p>
     * For example, you can try creating a new block over the genesis block (block height 2) if the
     * block chain height is {@code <=
     * CUT_OFF_AGE + 1}. As soon as {@code height > CUT_OFF_AGE + 1}, you cannot create a new block
     * at height 2.
     *
     * @return true if block is successfully added
     */
    public boolean addBlock(Block block) {
        byte[] prevBlockHash = block.getPrevBlockHash();
        if (prevBlockHash == null){
            return false;
        }
        BlockNode parentBlockNode = blockChain.get(wrap(prevBlockHash));
        if (parentBlockNode == null) {
            return false;
        }
        TxHandler handler = new TxHandler(parentBlockNode.getUTXOPoolCopy());
        Transaction[] txs = block.getTransactions().toArray(new Transaction[0]);
        Transaction[] validTxs = handler.handleTxs(txs);
        if (validTxs.length != txs.length) {
            return false;
        }
        int proposedHeight = parentBlockNode.height + 1;
        if (proposedHeight <= maxHeightNode.height - CUT_OFF_AGE) {
            return false;
        }
        UTXOPool utxoPool = handler.getUTXOPool();
        addCoinbaseToUTXOPool(block, utxoPool);
        BlockNode node = new BlockNode(block, parentBlockNode, utxoPool);
        blockChain.put(wrap(block.getHash()), node);
        if (proposedHeight > maxHeightNode.height) {
            maxHeightNode = node;
        }
        return true;
    }

    /**
     * Add a transaction to the transaction pool
     */
    public void addTransaction(Transaction tx) {
        txPool.addTransaction(tx);
    }

    private void addCoinbaseToUTXOPool(Block block, UTXOPool utxoPool) {
        Transaction coinbase = block.getCoinbase();
        for (int i = 0; i < coinbase.numOutputs(); i++) {
            Transaction.Output out = coinbase.getOutput(i);
            UTXO utxo = new UTXO(coinbase.getHash(), i);
            utxoPool.addUTXO(utxo, out);
        }
    }

    private static ByteArrayWrapper wrap(byte[] arr) {
        return new ByteArrayWrapper(arr);
    }


    /**
     * Print tree structure
     */
    public void printBlockChain(){
        BlockNode currentBlockNode = this.maxHeightNode;
        printDFSSearch(currentBlockNode);
//        }
    }


    private void printDFSSearch(BlockNode blockNode){
        if(blockNode.getNode_childNodes().size() == 0){
            System.out.println("Block hash is: " + blockNode.getNode_block().hashCode());
            System.out.println("Side Branch End!");
        }else{
            for(BlockNode childBlockNode:blockNode.getNode_childNodes()){
                System.out.println("Block hash is: "+blockNode.getNode_block().hashCode());
                printDFSSearch(childBlockNode);
            }
        }
    }

    /**
     * Get the longest branch from a given block node.
     * @param blockNode a claimed block node
     * @return the last block node in the longest branch
     */
    private BlockNode getNode_maxHeight(BlockNode blockNode) {
        if (blockNode.children.isEmpty()) {
            return blockNode;
        } else {
            int nodeHeight = blockNode.getNode_height();
            for (BlockNode chileNode : blockNode.getNode_childNodes()) {
                BlockNode tempNode = getNode_maxHeight(chileNode);
                if (tempNode.getNode_height() > nodeHeight) {
                    nodeHeight = tempNode.getNode_height();
                    blockNode = tempNode;
                }
            }
            return blockNode;
        }
    }

    /**
     * Get the recent nodes, to avoid overflow.
     */
    public BlockChainClip getNode_recentNodes(int storeHeight, BlockChain blockChain){
        BlockNode currentBlockNode = blockChain.getNode_maxHeight(blockChain.maxHeightNode);
        for(int i=0; i<storeHeight; i++){
            currentBlockNode = currentBlockNode.getNode_parentNode();
        }
        BlockChainClip recentNodes_blockChain = new BlockChainClip(currentBlockNode);
        return recentNodes_blockChain;
    }

    public LinkedHashMap<ByteArrayWrapper, Block> getBlocks() {
        return blocks;
    }
}
