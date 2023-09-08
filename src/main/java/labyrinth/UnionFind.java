package labyrinth;

class UnionFind {
    int[] group;

    public UnionFind(int size) {
        group = new int[size];
        for(int i = 0; i < size; i++) {
            group[i] = i;
        }
    }

    public int find(int x) {
        return group.length > x ? group[x] : -1;
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if(rootX != rootY && rootX != -1 && rootY != -1) {
            for(int i = 0; i < group.length; i++) {
                if(group[i] == rootY) {
                    group[i] = rootX;
                }
            }
        }
    }
}
