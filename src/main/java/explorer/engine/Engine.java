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
import explorer.protocol.IPlatformTestable;
import explorer.protocol.Protocol;
import explorer.protocol.UnsupportedProtocolException;
import explorer.protocol.list.ascribe.Ascribe;
import explorer.protocol.list.bitpos.BitPos;
import explorer.protocol.list.bitproof.BitProof;
import explorer.protocol.list.blockai.Blockai;
import explorer.protocol.list.blocksign.BlockSign;
import explorer.protocol.list.blockstore.Blockstore;
import explorer.protocol.list.coinspark.CoinSpark;
import explorer.protocol.list.colu.Colu;
import explorer.protocol.list.counterparty.CounterParty;
import explorer.protocol.list.cryptocopyright.CryptoCopyright;
import explorer.protocol.list.empty.Empty;
import explorer.protocol.list.eternitywall.EternityWall;
import explorer.protocol.list.factom.Factom;
import explorer.protocol.list.lapreuve.LaPreuve;
import explorer.protocol.list.monegraph.Monegraph;
import explorer.protocol.list.nicosia.Nicosia;
import explorer.protocol.list.omni.Omni;
import explorer.protocol.list.openassets.OpenAssets;
import explorer.protocol.list.openbazzar.OpenBazzar;
import explorer.protocol.list.originalmy.OriginalMy;
import explorer.protocol.list.proofofexistence.ProofOfExistence;
import explorer.protocol.list.provebit.ProveBit;
import explorer.protocol.list.remembr.Remembr;
import explorer.protocol.list.smartbit.SmartBit;
import explorer.protocol.list.stampd.Stampd;
import explorer.protocol.list.stampery.Stampery;
import explorer.protocol.list.tradle.Tradle;
import explorer.protocol.list.unknown.Unknown;


public class Engine implements Runnable{
	private final NetworkParameters params;
	private final IPlatformTestable conf;
	private Protocol platform;
	private Sha256Hash firstBlock;
	private Sha256Hash lastBlock;
	private final HikariDataSource ds;
	private final int CODE;
	
	private Set<ExplorerBlock> blocks;
	private Set<ExplorerTransaction> transactions;
	
	
	public Engine(NetworkParameters params, HikariDataSource ds, int CODE, Protocol platform, Sha256Hash firstBlock, Sha256Hash lastBlock) throws UnsupportedProtocolException{
		this.params = params;
		this.platform = platform;
		this.firstBlock = firstBlock;
		this.lastBlock = lastBlock;
		this.ds = ds;
		
		this.blocks = new HashSet<>();
		this.transactions = new HashSet<>();
		this.CODE = CODE;
		
		switch(platform){
			case COLU: this.conf = new Colu();
			break;
			case COINSPARK: this.conf = new CoinSpark();
			break;
			case OPENASSETS: this.conf = new OpenAssets();
			break;
			case OMNI: this.conf = new Omni();
			break;
			case FACTOM: this.conf = new Factom();
			break;
			case MONEGRAPH: this.conf = new Monegraph();
			break;
			case STAMPERY: this.conf = new Stampery();
			break;
			case ETERNITYWALL: this.conf = new EternityWall();
			break;
			case BLOCKSTORE: this.conf = new Blockstore();
			break;
			case CRYPTOCOPYRIGHT: this.conf = new CryptoCopyright();
			break;
			case STAMPD: this.conf = new Stampd();
			break;
			case TRADLE: this.conf = new Tradle();
			break;
			case ORIGINALMY: this.conf = new OriginalMy();
			break;
			case ASCRIBE: this.conf = new Ascribe();
			break;
			case BLOCKSIGN: this.conf = new BlockSign();
			break;
			case PROOFOFEXISTENCE: this.conf = new ProofOfExistence();
			break;
			case BITPROOF: this.conf = new BitProof();
			break;
			case PROVEBIT: this.conf = new ProveBit();
			break;
			case REMEMBR: this.conf = new Remembr();
			break;
			case SMARTBIT: this.conf = new SmartBit();
			break;
			case OPENBAZZAR: this.conf = new OpenBazzar();
			break;
			case COUNTERPARTY: this.conf = new CounterParty();
			break;
			case BLOCKAI: this.conf = new Blockai();
			break;
			case NICOSIA: this.conf = new Nicosia();
			break;
			case BITPOS: this.conf = new BitPos();
			break;
			case LAPREUVE: this.conf = new LaPreuve();
			break;
			case EMPTY: this.conf = new Empty();
			break;
			case UNKNOWN: this.conf = new Unknown();
			break;
			default: throw new UnsupportedProtocolException(platform.toString());
		}
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
				System.out.println(firstBlock);
				
				Future<Block> future = peer.getBlock(firstBlock);
				Block block = future.get();
				
				List<Transaction> listTx = block.getTransactions();
				for(Transaction tx : listTx){
					
					// Checking transaction
					Boolean isValidTransaction = conf.init(block, tx);

					if(isValidTransaction){

						// Adding block
						ExplorerBlock tempBlock = new ExplorerBlock(block.getHashAsString(), block.getTime());
						blocks.add(tempBlock);
						
						// Adding transaction
						ExplorerTransaction tempTx = new ExplorerTransaction(tx.getHashAsString(), block.getHashAsString(), conf.getOpReturnBytes(), tx.getFee());
						transactions.add(tempTx);
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
					String stringLock = "LOCK TABLE explorer." + platform.toString().toLowerCase() + "transactions WRITE;";
					String stringTx = 
							"INSERT IGNORE INTO explorer." + platform.toString().toLowerCase() + "transactions " +
							"(transactionHash, block, opreturn) " +
							"VALUES (?, ?, ?);";
					String stringUnlock = "UNLOCK TABLE";
					
					PreparedStatement statementLock = con.prepareStatement(stringLock);
					PreparedStatement statementTx = con.prepareStatement(stringTx);
					PreparedStatement statementUnlock = con.prepareStatement(stringUnlock);
					
					statementTx.setString(1, t.getTxHash());
					statementTx.setString(2, t.getBlockHash());
					statementTx.setString(3, t.getOpReturn());
					
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