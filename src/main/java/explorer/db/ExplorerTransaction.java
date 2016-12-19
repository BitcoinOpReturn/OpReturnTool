package explorer.db;

import org.bitcoinj.core.Coin;

public class ExplorerTransaction {
	private String txHash;
	private String blockHash;
	private String opReturn;
	private Coin fee;
	
	public ExplorerTransaction(String txHash, String blockHash, String opReturn, Coin fee){
		this.txHash = txHash;
		this.blockHash = blockHash;
		this.opReturn = opReturn;
		this.fee = fee;
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
	
	public String getOpReturn() {
		return opReturn;
	}

	public void setOpReturn(String opReturn) {
		this.opReturn = opReturn;
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
