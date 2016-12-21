package explorer.protocol;


public enum Protocol {
	COLU,
	COINSPARK,
	OPENASSETS,
	OMNI,
	FACTOM,
	MONEGRAPH,
	STAMPERY,
	ETERNITYWALL,
	BLOCKSTORE,
	CRYPTOCOPYRIGHT,
	STAMPD,
	TRADLE,
	ORIGINALMY,
	ASCRIBE,
	BLOCKSIGN,
	PROOFOFEXISTENCE,
	BITPROOF,
	PROVEBIT,
	REMEMBR,
	SMARTBIT,
	OPENBAZZAR,
	COUNTERPARTY,
	BLOCKAI,
	NICOSIA,
	BITPOS,
	LAPREUVE,
	EMPTY,
	UNKNOWN,
	ASSETS,
	DOCUMENT,
	MEDIAMARKET,
	OTHER,
	ALL;
	
	public static Protocol getProtocol(String protocol) throws UnsupportedProtocolException{
		if(protocol.toLowerCase().equals("colu")) return COLU;
		if(protocol.toLowerCase().equals("coinspark")) return COINSPARK;
		if(protocol.toLowerCase().equals("openassets")) return OPENASSETS;
		if(protocol.toLowerCase().equals("omni")) return OMNI;
		if(protocol.toLowerCase().equals("factom")) return FACTOM;
		if(protocol.toLowerCase().equals("monegraph")) return MONEGRAPH;
		if(protocol.toLowerCase().equals("stampery")) return STAMPERY;
		if(protocol.toLowerCase().equals("eternitywall")) return ETERNITYWALL;
		if(protocol.toLowerCase().equals("blockstore")) return BLOCKSTORE;
		if(protocol.toLowerCase().equals("cryptocopyright")) return CRYPTOCOPYRIGHT;
		if(protocol.toLowerCase().equals("stampd")) return STAMPD;
		if(protocol.toLowerCase().equals("tradle")) return TRADLE;
		if(protocol.toLowerCase().equals("originalmy")) return ORIGINALMY;
		if(protocol.toLowerCase().equals("ascribe")) return ASCRIBE;
		if(protocol.toLowerCase().equals("blocksign")) return BLOCKSIGN;
		if(protocol.toLowerCase().equals("proofofexistence")) return PROOFOFEXISTENCE;
		if(protocol.toLowerCase().equals("bitproof")) return BITPROOF;
		if(protocol.toLowerCase().equals("provebit")) return PROVEBIT;
		if(protocol.toLowerCase().equals("remembr")) return REMEMBR;
		if(protocol.toLowerCase().equals("smartbit")) return SMARTBIT;
		if(protocol.toLowerCase().equals("openbazzar")) return OPENBAZZAR;
		if(protocol.toLowerCase().equals("counterparty")) return COUNTERPARTY;
		if(protocol.toLowerCase().equals("blockai")) return BLOCKAI;
		if(protocol.toLowerCase().equals("nicosia")) return NICOSIA;
		if(protocol.toLowerCase().equals("bitpos")) return BITPOS;
		if(protocol.toLowerCase().equals("lapreuve")) return LAPREUVE;
		if(protocol.toLowerCase().equals("empty")) return EMPTY;
		if(protocol.toLowerCase().equals("unknown")) return UNKNOWN;
		if(protocol.toLowerCase().equals("assets")) return ASSETS;
		if(protocol.toLowerCase().equals("document")) return DOCUMENT;
		if(protocol.toLowerCase().equals("mediamarket")) return MEDIAMARKET;
		if(protocol.toLowerCase().equals("other")) return OTHER;
		if(protocol.toLowerCase().equals("all")) return ALL;
		throw new UnsupportedProtocolException("The requested platform is not supported");
	}
}