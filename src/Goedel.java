import p2pmpi.mpi.MPI;

public class Goedel {

	/**
	 * AAB, A = role, B = rank
	 * 
	 *   RM1    RM2
	 *     \    /
	 *      \  /
	 *       RM3
	 *      /  \
	 *     /    \
	 *   RM4    RM5
	 *    |      |
	 *    |      |
	 *   FE6    FE7
	 * 
	 * 
	 */
	
	//[node][0/fe|1/rm][i] = rank
	static int[][][] neighbours = {
			//{{}, {3}}, //1
			//{{}, 3}, //2
		    {{}, {2,3}}, //{{}, {1,2,4,5}}, //3
			{{4}, {1}},//4
			{{5}, {1}}, //5
			{{}, {2}}, //6
			{{}, {3}} //7
	};
	static int frontendsFrom = 3;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int rank, size;
		MPI.Init(args);
		
 		size = MPI.COMM_WORLD.Size();
		rank = MPI.COMM_WORLD.Rank();
		
		if(size == neighbours.length) {
			Node n = null;
			if(rank < frontendsFrom)
				n = new ReplicationManager(rank, neighbours[rank][0], neighbours[rank][1]);
			else
				n = new FrontEnd(rank, neighbours[rank][0], neighbours[rank][1]);			
			n.work();

			MPI.Finalize();			
		}
		else {
			System.out.println("Size and rank differ (" + size + " and " + rank + "), aborting");
		}				
	}

}
