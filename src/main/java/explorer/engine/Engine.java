package explorer.engine;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.MemoryBlockStore;

import com.zaxxer.hikari.HikariDataSource;

import explorer.db.ExplorerBlock;
import explorer.db.ExplorerTransaction;


public class Engine implements Runnable{
	private final NetworkParameters params;
	private Sha256Hash firstBlock;
	private Sha256Hash lastBlock;
	private final HikariDataSource ds;
	private final int CODE;
	
	private Set<ExplorerBlock> blocks;
	private Set<ExplorerTransaction> transactions;
	
	
	public Engine(NetworkParameters params, HikariDataSource ds, int CODE, Sha256Hash firstBlock, Sha256Hash lastBlock){
		this.params = params;
		this.firstBlock = firstBlock;
		this.lastBlock = lastBlock;
		this.ds = ds;
		
		this.blocks = new HashSet<>();
		this.transactions = new HashSet<>();
		this.CODE = CODE;
	}
	
	
	@Override
	public void run(){
		try{			
			System.out.println("Thread " + CODE + ", connecting to Bicoin node: firstBlock " + firstBlock + " lastBlock " + lastBlock);
			BlockStore blockStore = new MemoryBlockStore(params);
			BlockChain chain = new BlockChain(params, blockStore);
			PeerGroup peerGroup = new PeerGroup(params, chain);
			peerGroup.startAsync();
			peerGroup.awaitRunning();
			PeerAddress addr = new PeerAddress(InetAddress.getLocalHost(), params.getPort());
			peerGroup.addAddress(addr);
			peerGroup.waitForPeers(1).get();
			Peer peer = peerGroup.getConnectedPeers().get(0);
		
			// Scan all the blocks of the current interval
			while(!firstBlock.equals(lastBlock)){
				Future<Block> future = peer.getBlock(firstBlock);
				Block block = future.get();
				
				List<Transaction> listTx = block.getTransactions();
				for(Transaction tx : listTx){
					
					// Checking transaction
					ExplorerTransaction optx = new ExplorerTransaction(block, tx); 

					if(optx.isOpReturn()){

						// Adding block
						ExplorerBlock tempBlock = new ExplorerBlock(block.getHashAsString(), block.getTime());
						blocks.add(tempBlock);
						
						// Adding transaction
						optx.setTxHash(tx.getHashAsString());
						optx.setBlockHash(block.getHashAsString());
						optx.setFee(tx.getFee());
						transactions.add(optx);
					}
				}

				// Get next block
				firstBlock = block.getPrevBlockHash();

				// Skip the blocks that throw the "com.google.bitcoin.core.ProtocolException: No magic bytes+header after reading 65536 bytes" exception.
				if(firstBlock.toString().equals("000000000000000002792ce1fdd61b7f0a269a12948ea482fd1ba7b5f932c2ac")){
					firstBlock = new Sha256Hash("0000000000000000003bfc6c59756e7611322dbb824a0489b2b5311491d847e6");
				}
				if(firstBlock.toString().equals("00000000000000000eed03d82db963e606c07c408e863e8a80e896ef1cf557c7")){
					firstBlock = new Sha256Hash("00000000000000003606eaf825796e818e8b34cd2808eedb15feccc68b1aba68");
				}
				else if(firstBlock.toString().equals("0000000000000000052baa120e0e4eeb6af5b395b6ca684075428966c993928f")){
					firstBlock = new Sha256Hash("00000000000000000aed9276e0421d920bcfe0a99f1308fc944ce8240b14f2c8");
				}
				else if(firstBlock.toString().equals("000000000000000007e31cc9995cade2cb5c42834f984d6db38f798dc57790d0")){
					firstBlock = new Sha256Hash("0000000000000000096b41dd18c907e71a69ec29864ee24f97fefcdcda22d11e");
				}
				else if(firstBlock.toString().equals("00000000000000000eed03d82db963e606c07c408e863e8a80e896ef1cf557c7")){
					firstBlock = new Sha256Hash("00000000000000003606eaf825796e818e8b34cd2808eedb15feccc68b1aba68");
				}
			}	
			

			// Writing data on db
			try (Connection con = ds.getConnection()){
				
				// Writing blocks
				for(ExplorerBlock b : blocks){
					System.out.println("Adding block " +  b.getBlockHash());
					String stringLock = "LOCK TABLE explorer.block WRITE;"; 
					String stringBlock = "INSERT IGNORE INTO explorer.block (blockHash, timestamp) VALUES (?,?); ";
					String stringUnlock = "UNLOCK TABLE;";

					PreparedStatement statementLock = con.prepareStatement(stringLock);
					PreparedStatement statementBlock = con.prepareStatement(stringBlock);
					PreparedStatement statementUnlock = con.prepareStatement(stringUnlock);
					statementBlock.setString(1, b.getBlockHash());
					statementBlock.setString(2, b.getBlockDate());
					statementLock.execute();
					statementBlock.executeUpdate();
					statementUnlock.execute();
				}

				// Writing transactions
				for(ExplorerTransaction t : transactions){
					System.out.println("Adding transaction " +  t.getTxHash());
					String stringLock = "LOCK TABLE explorer." + t.getProtocol() + "transactions WRITE;";
					String stringTx = 
							"INSERT IGNORE INTO explorer." + t.getProtocol() + "transactions " +
							"(transactionHash, block, opreturn) " +
							"VALUES (?, ?, ?);";
					String stringUnlock = "UNLOCK TABLE";
					
					PreparedStatement statementLock = con.prepareStatement(stringLock);
					PreparedStatement statementTx = con.prepareStatement(stringTx);
					PreparedStatement statementUnlock = con.prepareStatement(stringUnlock);
					
					statementTx.setString(1, t.getTxHash());
					statementTx.setString(2, t.getBlockHash());
					statementTx.setString(3, t.getData());
					
					statementLock.execute();
					statementTx.executeUpdate();
					statementUnlock.execute();
				}
			} catch(SQLException e){
				e.printStackTrace();
			}
			
			System.out.println("Thread " + CODE + ": closing Bitcoin connection");
			peerGroup.stopAsync();
	        peerGroup.awaitTerminated();
	
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}