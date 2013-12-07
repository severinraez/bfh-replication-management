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
	
	static int[][] neighbours = {
			{3}, //1
			{3}, //2
			{1,2,4,5}, //3
			{3,6}, //4
			{3,7}, //5
			{4}, //6
			{5} //7
	};
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int rank, size;
		MPI.Init(args);
		
 		size = MPI.COMM_WORLD.Size();
		rank = MPI.COMM_WORLD.Rank();
				
		if(rank < 6) {
			System.out.println(rank + " is a RM");
			ReplicationManager rm = new ReplicationManager(rank, neighbours[rank]);
			rm.work();
		}
		else {
			System.out.println(rank + " is a FE");
			FrontEnd fe = new FrontEnd(rank, neighbours[rank]);
			fe.work();
		}

		MPI.Finalize();
	}

}
