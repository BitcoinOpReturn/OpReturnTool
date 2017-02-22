package explorer.db;

import java.util.List;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ScriptException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.script.Script;

import explorer.protocol.Codes;
import explorer.protocol.Protocol;

public class ExplorerTransaction {
	private String protocol;
	private String data;
	private Boolean isOpReturn;
	private String txHash;
	private String blockHash;
	private Coin fee;
	
	
	
	public ExplorerTransaction(Block block, Transaction tx){
		isOpReturn = false;
		String value = evaluateType(block, tx);
		if(isOpReturn)
			setData(value);
	}
	
	
	
	public Boolean isOpReturn(){
		if(this.protocol == null) 
			return false;
		return true;
	}
	
	
	
	public String getName(){
		return this.protocol;
	}
	
	
	
	public String getData(){
		return this.data;
	}
	
	
	
	public String evaluateType(Block block, Transaction tx) {
		
		List<TransactionOutput> listOutputs = tx.getOutputs();
		String tempString=null;
		
		// Check if the transactions uses OP_RETURN
		for(TransactionOutput output : listOutputs){
			try{
				tempString = (new Script(output.getScriptBytes())).toString();
				if(tempString.startsWith(Codes.RETURN)){
					this.isOpReturn = true;
					break;
				}
			} catch(ScriptException e){}
		}
		
		if(this.isOpReturn == false) return null;
		
		
		try{
			
			if(!tempString.contains("[")){
				this.protocol = Protocol.EMPTY;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.ASCRIBE_CODE1)){
				this.protocol = Protocol.ASCRIBE;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.BITPOS_CODE1)){
				this.protocol = Protocol.BITPOS;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.BITPROOF_CODE1)){
				this.protocol = Protocol.BITPROOF;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.BLOCKAI_CODE1)){
				this.protocol = Protocol.BLOCKAI;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.BLOCKSIGN_CODE1)){
				this.protocol = Protocol.BLOCKSIGN;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.BLOCKSTORE_CODE1) || 
			   tempString.contains(Codes.BRACKET + Codes.BLOCKSTORE_CODE2) || 
			   tempString.contains(Codes.BRACKET + Codes.BLOCKSTORE_CODE3)){
				this.protocol = Protocol.BLOCKSTORE;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.COINSPARK_CODE1)){
				this.protocol = Protocol.COINSPARK;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.COLU_CODE1)){
				this.protocol = Protocol.COLU;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.CRYPTOCOPYRIGHT_CODE1) || 
			   tempString.contains(Codes.BRACKET + Codes.CRYPTOCOPYRIGHT_CODE2)){
				this.protocol = Protocol.CRYPTOCOPYRIGHT;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.ETERNITYWALL_CODE1)){
				this.protocol = Protocol.ETERNITYWALL;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.FACTOM_CODE1) || 
			   tempString.contains(Codes.BRACKET + Codes.FACTOM_CODE2) || 
			   tempString.contains(Codes.BRACKET + Codes.FACTOM_CODE3) || 
			   tempString.contains(Codes.BRACKET + Codes.FACTOM_CODE4)){
				this.protocol = Protocol.FACTOM;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.LAPREUVE_CODE1)){
				this.protocol = Protocol.LAPREUVE;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.MONEGRAPH_CODE1)){
				this.protocol = Protocol.MONEGRAPH;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.NICOSIA_CODE1)){
				this.protocol = Protocol.NICOSIA;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.OMNI_CODE1)){
				this.protocol = Protocol.OMNI;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.OPENASSETS_CODE1)){
				this.protocol = Protocol.OPENASSETS;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.ORIGINALMY_CODE1)){
				this.protocol = Protocol.ORIGINALMY;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.PROOFOFEXISTENCE_CODE1)){
				this.protocol = Protocol.PROOFOFEXISTENCE;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.PROVEBIT_CODE1)){
				this.protocol = Protocol.PROVEBIT;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.REMEMBR_CODE1) || 
			   tempString.contains(Codes.BRACKET + Codes.REMEMBR_CODE2)){
				this.protocol = Protocol.REMEMBR;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.SMARTBIT_CODE1)){
				this.protocol = Protocol.SMARTBIT;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.STAMPD_CODE1)){
				this.protocol = Protocol.STAMPD;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.STAMPERY_CODE1) || 
			   tempString.contains(Codes.BRACKET + Codes.STAMPERY_CODE2) ||
			   tempString.contains(Codes.BRACKET + Codes.STAMPERY_CODE3) ||
			   tempString.contains(Codes.BRACKET + Codes.STAMPERY_CODE4) ||
			   tempString.contains(Codes.BRACKET + Codes.STAMPERY_CODE5)){
				this.protocol = Protocol.STAMPERY;
				return tempString;
			}
			
			if(tempString.contains(Codes.BRACKET + Codes.TRADLE_CODE1)){
				this.protocol = Protocol.TRADLE;
				return tempString;	
			}	
		
		} catch(ScriptException e){}
		
		this.protocol = Protocol.UNKNOWN;
		return tempString;

	}
	

	public void setData(String value){
		int v1 = value.indexOf("[");
		int v2 = value.indexOf("]");
		if((v1 == -1) || (v2 == -1)) 
			this.data = "";
		else
			this.data = value.substring(v1+1, v2);
	}
	
	public String getProtocol(){
		return protocol;
	}
	
	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}
		
	public Coin getFee() {
		return fee;
	}

	public void setFee(Coin fee) {
		this.fee = fee;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((txHash == null) ? 0 : txHash.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExplorerTransaction other = (ExplorerTransaction) obj;
		if (txHash == null) {
			if (other.txHash != null)
				return false;
		} else if (!txHash.equals(other.txHash))
			return false;
		return true;
	}
}
