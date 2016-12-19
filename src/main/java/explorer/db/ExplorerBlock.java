package explorer.db;

import java.util.Date;

public class ExplorerBlock {
	private String blockHash;
	private String blockDate;
	
	public ExplorerBlock(String blockHash, Date blockDate){
		this.blockHash = blockHash;
		
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.blockDate = sdf.format(blockDate);
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	public String getBlockDate() {
		return blockDate;
	}

	public void setBlockDate(String blockDate) {
		this.blockDate = blockDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blockHash == null) ? 0 : blockHash.hashCode());
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
		ExplorerBlock other = (ExplorerBlock) obj;
		if (blockHash == null) {
			if (other.blockHash != null)
				return false;
		} else if (!blockHash.equals(other.blockHash))
			return false;
		return true;
	}
}
