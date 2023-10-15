package labyrinth;

public class UnionFind {
    private final int[] parent;
    private final int[] rank;

    public int numSets;

    public UnionFind(int size) {
        parent = new int[size];
        rank = new int[size];
        numSets = size;

        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        int root = x;
        // Trouver la racine
        while (root != parent[root]) {
            root = parent[root];
        }
        // Compresser le chemin
        while (x != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return;
        }

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        numSets--;
    }
}
