# OpReturnTool

A tool for extracting OP_RETURN metadata from the Bitcoin blockchain, by [Livio Pompianu](http://tcs.unica.it/members/livio-pompianu).  

The results of the analysis performed by using the OpReturnTool are discussed in [An analysis of Bitcoin OP_RETURN metadata](https://arxiv.org/abs/1702.01024).
The paper was presented at the [4th Workshop on Bitcoin and Blockchain Research](http://fc17.ifca.ai/bitcoin/). 

## Background ##
The Bitcoin protocol allows to save arbitrary data on the blockchain through a special instruction of the scripting language, 
called OP RETURN. A growing number of protocols exploit this feature to extend the range of applications of the Bitcoin 
blockchain beyond transfer of currency.

Usually, a protocol is identified by the first few bytes of metadata attached to the OP RETURN, but the exact number of 
bytes may vary from protocol to protocol.

We searched the web for known associations between identifiers and protocols.  
The OpReturnTool accordingly classifies the OP_RETURN transactions of your local copy of the blockchain that 
begin with one of the identifiers discovered.

OpReturnTool is a Java tool expliting the [BitcoinJ APIs](https://bitcoinj.github.io/).

## Install prerequisites ##
1. [Bitcoin Core](https://bitcoin.org/en/bitcoin-core/) will provide you a copy of the Bitcoin blockchain.
2. [Eclipse](https://eclipse.org/) (or an equivalent IDE for developing Maven projects)
3. [MySQL](https://www.mysql.com/) (or an equivalent relational database management system)

## Install OpReturnTool ##
1. Clone the [OpReturnTool](https://github.com/BitcoinOpReturn/OpReturnTool) repository.
2. Eclipse: import the project as Existing Maven Project.
3. MySQL: create a new database and import all the tables of the [OpReturnData](https://github.com/BitcoinOpReturn/OpReturnData) repository.

## Configure ##
You must either edit the file [global.properties](https://github.com/BitcoinOpReturn/OpReturnTool/blob/master/src/main/resources/global.properties) 
or create a file local.properties providing the following information.
- dbname= name of the database you created.
- dbuser= name of the sql user authorized to write in this database.
- dbpassword= password of the database.
- inputFile=input.txt
- firstChunk=0
- lastChunk=0

## Run ##
OpReturnTool inspects the blockchains by reading consecutive chunks: each chunk is analysed by a different thread.
Chunks are defined in the [input.txt](https://github.com/BitcoinOpReturn/OpReturnTool/blob/master/src/main/resources/input.txt) 
file as a couple of values: the hash of the the last block of the chunk (that will NOT be included in the analysis) 
and the hash of first block of the chunk.

You can choose which chunks analyse by editing the previous properties file. 
For instance, with the followig parameters

- inputFile=input.txt
- firstChunk=0
- lastChunk=3

the tool will produce 4 threads that will inspect respectively:

- from block 0 to 10000 (excluded)
- from block 10000 to 20000 (excluded)
- from block 20000 to 30000 (excluded)
- from block 30000 to 40000 (excluded)

Execute the MainEngine.java file.
