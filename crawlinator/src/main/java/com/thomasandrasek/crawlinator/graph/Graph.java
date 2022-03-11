package com.thomasandrasek.crawlinator.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph {
	private Map<String, ArrayList<String>> graph;
	
	public Graph() {
		this.graph = new HashMap<String, ArrayList<String>>();
	}
	
	public void addNode(String node) {
		if (this.graph.get(node) == null) {
			this.graph.put(node, new ArrayList<String>());
		}
	}
	
	private void insertEdge(String nodeA, String nodeB) {
		ArrayList<String> nodes = this.graph.get(nodeA);
		
		int high = nodes.size();
		int low = 0;
		
		int mid = -1;
		while (low < high) {
			mid = (high + low) / 2;
			
			String node = nodes.get(mid);
			int val = nodeB.compareTo(node);
			
			if (val == 0) {
				low = mid;
				high = mid;
			} else if (val > 0) {
				low = mid + 1;
			} else {
				high = mid;
			}
		}
		
		nodes.add(low, nodeB);
	}
	
	public void addEdge(String nodeA, String nodeB) {
		this.addNode(nodeA);
		this.addNode(nodeB);
		
		insertEdge(nodeA, nodeB);
	}
	
	public ArrayList<String> getEdgesEndFrom(String node) {
		ArrayList<String> edges = this.graph.get(node);
		
		if (edges != null) {
			edges = new ArrayList<String>(edges);
		}
		
		return edges;
	}
}
