import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class GraphTraversalAlgorithms {

	ArrayList<ArrayList<String>> graph = new ArrayList<>();
	HashMap<String, Integer> nodes = new HashMap<>();
	int[][] graphMatrix;

	/**
	 * Adds an edge to the graph, checks for nodes and adds them if they don't exist
	 * @param urlFrom The start of the edge
	 * @param urlTo The end of the edge
	 */
	public void addEdge(String urlFrom, String urlTo) {
		boolean fromExists = false;
		boolean toExists = false;
		for (int i = 0; i < graph.size(); i++) {
			if (graph.get(i).get(0).equals(urlTo)) {
				toExists = true;
			}
			if (graph.get(i).get(0).equals(urlFrom)) {
				fromExists = true;
				graph.get(i).add(urlTo);
			}
		}
		if (!toExists) {
			ArrayList<String> a = new ArrayList<String>();
			a.add(urlTo);
			graph.add(a);
			nodes.put(urlTo, graph.size() - 1);
		}
		if (!fromExists) {
			ArrayList<String> a = new ArrayList<String>();
			a.add(urlFrom);
			a.add(urlTo);
			graph.add(a);
			nodes.put(urlFrom, graph.size() - 1);
		}
	}

	/**
	 * Finds the shortest path between the two specified vertices
	 * @param urlFrom The start node
	 * @param urlTo The end node
	 * @return The minimum number of links to go from urlFrom to urlTo, -1 if a path is not possible or the links are not in the graph
	 */
	ArrayList<Integer> distanceArray = new ArrayList<>();

	
	public int getShortestPath(String urlFrom, String urlTo) {
		int start = -1;
		int finish = -1;
		int[] distance = new int[graph.size()];
		boolean[] visited = new boolean[graph.size()];

		for (int i = 0; i < graph.size(); i++) {
			if (urlFrom.equals(graph.get(i).get(0))) {
				start = i;
			}
			if (urlTo.equals(graph.get(i).get(0))) {
				finish = i;
			}
		}
		if (start == -1 || finish == -1) {
			return -1;
		}
		for (int i = 0; i < graph.size(); i++) {
			visited[i] = false;
			distance[i] = Integer.MAX_VALUE;
		}
		distance[start] = 0;

		while (!allVisited(visited)) {
			int min = minNode(distance, visited);
			if (min == -1) {
				break;
			}
			visited[min] = true;

			for (int test = 0; test < distance.length; test++) {
				if (adjacentTo(min, test) && visited[test] == false && (distance[min] + 1) < distance[test]) {
					distance[test] = 1 + distance[min];
				}
			}
		}

		int shortestDistance = distance[finish];
		for (int i = 0; i < distance.length; i++) {
			distanceArray.add(distance[i]);
		}
		if (shortestDistance == Integer.MAX_VALUE) {
			shortestDistance = -1;
		}
		return shortestDistance;
	}

	/**
	 * Finds if the node test is adjacent to the node min
	 * @param min The node from which adjacency to test is found
	 * @param test The node compared to min
	 * @return true if test is adjacent to min, false otherwise
	 */
	private boolean adjacentTo(int min, int test) {
		String url = graph.get(test).get(0);
		for (int i = 1; i < graph.get(min).size(); i++) {
			if (url.equals(graph.get(min).get(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Tests whether all vertices in the graph have been visited
	 * @param a The array of nodes visited
	 * @return false if there is a node that has not yet been visited, false otherwise
	 */
	private boolean allVisited(boolean[] a) {
		for (int i = 0; i < a.length; i++) {
			if (!a[i]) {
				return false;
			}
		}
		return true;
	}

	/** 
	 * Finds the unvisited node with the lowest distance
	 * @param distance The array of edge distances
	 * @param visited The array of visited nodes
	 * @return The index of the unvisited node with lowest distance, -1 if there is not one
	 */
	private int minNode(int[] distance, boolean[] visited) {
		int minDistance = Integer.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < distance.length; i++) {
			if (distance[i] < minDistance && visited[i] == false) {
				minDistance = distance[i];
				index = i;
			}
		}
		return index;
	}

	/**
	 * Finds all the centers of the graph i.e. the nodes with the shortest distance to all other nodes in the graph
	 * @return An array of nodes which are centers
	 */
	public String[] getCenters() {
		ArrayList<ArrayList<Integer>> distances = new ArrayList<>();
		for (int i = 0; i < graph.size(); i++) {
			getShortestPath(graph.get(i).get(0), graph.get(0).get(0));
			distances.add(new ArrayList<>(distanceArray));
			distanceArray.clear();
		}
		ArrayList<int[]> maxDistance = new ArrayList<>();
		a: for (int i = 0; i < distances.size(); i++) {
			int[] max = { 0, 0 };
			for (int j = 0; j < distances.get(i).size(); j++) {
				if (distances.get(i).get(j) == Integer.MAX_VALUE) {
					continue a;
				}
				if (distances.get(i).get(j) > max[0]) {
					max[0] = distances.get(i).get(j);
					max[1] = i;
				}
			}
			maxDistance.add(max);
		}
		int min = Integer.MAX_VALUE;
		int count = 0;
		for (int i = 0; i < maxDistance.size(); i++) {
			if (maxDistance.get(i)[0] < min) {
				min = maxDistance.get(i)[0];
				count = 0;
			}
			if (maxDistance.get(i)[0] == min) {
				count++;
			}
		}
		String[] centers = new String[count];
		int iterate = 0;
		for (int i = 0; i < maxDistance.size(); i++) {
			if (maxDistance.get(i)[0] == min) {
				centers[iterate] = graph.get(maxDistance.get(i)[1]).get(0);
				iterate++;
			}
		}
		/*for(int i = 0; i < centers.length; i++) {
			System.out.println(centers[i]);
		}*/
		return centers;
	}

	/**
	 * Finds all the strongly connected components i.e. the groups of nodes such that a node in the group can reach every other node in the group
	 * @return A matrix of nodes, each row is a strongly connected component
	 */
	Stack<Integer> stack = new Stack<Integer>();
	ArrayList<ArrayList<Integer>> components = new ArrayList<>();
	ArrayList<ArrayList<Integer>> reversedGraph = new ArrayList<>();
	boolean[] visited;
	int component = 0;

	
	public String[][] getStronglyConnectedComponents() {
		visited = new boolean[graph.size()];
		graphMatrix = new int[graph.size()][];
		for (int i = 0; i < graph.size(); i++) {
			graphMatrix[i] = new int[graph.get(i).size() - 1];
			for (int j = 0; j < graph.get(i).size() - 1; j++) {
				graphMatrix[i][j] = nodes.get(graph.get(i).get(j + 1));
			}
		}
		for (int i = 0; i < graph.size(); i++) {
			if (!visited[i]) {
				depthFirstRecursion(i);
			}
		}
		for (int i = 0; i < graph.size(); i++) {
			ArrayList<Integer> e = new ArrayList<>();
			reversedGraph.add(e);
		}
		for (int i = 0; i < graphMatrix.length; i++) {
			for (int j = 0; j < graphMatrix[i].length; j++) {
				reversedGraph.get(graphMatrix[i][j]).add(i);
			}
		}
		for (int i = 0; i < visited.length; i++) {
			visited[i] = false;
		}
		int count = 0;
		while (!stack.isEmpty()) {
			ArrayList<Integer> a = new ArrayList<>();
			components.add(a);
			depthFirstRecursion2(stack.get(stack.size() - 1), count);
			count++;
		}
		String[][] ret = new String[components.size()][];
		for (int i = 0; i < components.size(); i++) {
			ret[i] = new String[components.get(i).size()];
			for (int j = 0; j < components.get(i).size(); j++) {
				ret[i][j] = graph.get(components.get(i).get(j)).get(0);
			}
		}
		return ret;
	}

	/**
	 * A recursive implementation of depth first search to add elements to a stack
	 * @param node The node at which to begin
	 */
	public void depthFirstRecursion(int node) {
		visited[node] = true;
		for (int i = 0; i < graphMatrix[node].length; i++) {
			if (!visited[graphMatrix[node][i]]) {
				depthFirstRecursion(graphMatrix[node][i]);
			}
		}
		stack.push(node);
	}

	/**
	 * A recursive implementation of depth first search to find the strongly connected components
	 * @param node The node at which to begin
	 * @param count The current strongly connected component which is being dealt with
	 */
	public void depthFirstRecursion2(int node, int count) {
		visited[node] = true;
		for (int i = 0; i < reversedGraph.get(node).size(); i++) {
			if (!visited[reversedGraph.get(node).get(i)]) {
				depthFirstRecursion2(reversedGraph.get(node).get(i), count);
			}
		}
		components.get(count).add(node);
		stack.pop();
	}

	/**
	 * Finds the Hamiltonian path i.e. a path that goes though every node in the graph exactly once
	 * @return The Hamiltonian path as an array of nodes
	 */
	ArrayList<Integer> answer = new ArrayList<>();
	boolean keepGoing = true;

	
	public String[] getHamiltonianPath() {
		long start = System.nanoTime();
		int n = graph.size();
		graphMatrix = new int[n + 1][];
		for (int i = 0; i < n; i++) {
			graphMatrix[i] = new int[graph.get(i).size() - 1];
			for (int j = 0; j < graph.get(i).size() - 1; j++) {
				graphMatrix[i][j] = nodes.get(graph.get(i).get(j + 1));
			}
		}
		graphMatrix[n] = new int[n];
		for (int i = 0; i < graphMatrix.length - 1; i++) {
			graphMatrix[n][i] = i;
		}
		ArrayList<Integer> a = new ArrayList<>();
		a.add(n);
		depthFirstSearch(a);
		keepGoing = true;
		if (answer.size() == 0) {
			return new String[0];
		}
		String[] ret = new String[answer.size() - 1];
		for (int i = 1; i < answer.size(); i++) {
			ret[i - 1] = graph.get(answer.get(i)).get(0);
		}
		long end = System.nanoTime() - start;
		System.out.println(end);
		for(int i = 0; i < ret.length; i++) {
			System.out.print(ret[i] + " ");
		}
		return ret;
	}

	/**
	 * Performs a recursive depth first search of the graph, finding the Hamiltonian path
	 * @param path The current path to check
	 */
	public void depthFirstSearch(ArrayList<Integer> path) {
		if (keepGoing) {
			if (path.size() == graph.size() + 1) {
				answer = new ArrayList<Integer>(path);
				//keepGoing = false;
				return;
			}
			int n = graphMatrix[path.get(path.size() - 1)].length;
			for (int i = 0; i < n; i++) {
				if (!inPath(graphMatrix[path.get(path.size() - 1)][i], path)) {
					path.add(graphMatrix[path.get(path.size() - 1)][i]);
					depthFirstSearch(path);
					path.remove(path.size() - 1);
				}
			}
		}
	}

	/**
	 * Checks if the node is in the path
	 * @param node The node to identify
	 * @param path The path to check if the node is in
	 * @return true if the node is in the path, false otherwise
	 */
	public boolean inPath(int node, ArrayList<Integer> path) {
		for (int i = 0; i < path.size(); i++) {
			if (node == path.get(i)) {
				return true;
			}
		}
		return false;
	}
}
