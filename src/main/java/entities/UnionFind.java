package entities;

class UnionFind {
    int[] parent;

    public UnionFind(int size) {
        parent = new int[size];
        for(int i = 0; i < size; i++) {
            parent[i] = i;
        }
    }

    public int find(int x) {
        if(parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        /*
        System.out.println("La cellule " + x + " est dans le groupe " + parent[x]);
        if(x != parent[x])
            System.out.println("La cellule " + x + " fait partie du groupe " + parent[x]);
         */
        return parent[x];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if(rootX != rootY) {
            parent[rootY] = rootX;
        }
    }
}
