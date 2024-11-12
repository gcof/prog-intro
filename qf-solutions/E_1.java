import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class E {
    private static class IntList {
        private int[] data;
        private int size;

        IntList() {
            data = new int[1];
            size = 0;
        }

        public void add(int value) {
            if (size == data.length) {
                data = Arrays.copyOf(data, data.length * 2);
            }
            data[size++] = value;
        }

        public int get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException();
            }
            return data[index];
        }

        public int size() {
            return size;
        }
    }

    private static int dfs(ArrayList<IntList> g, int[] depth, boolean[] term, int[] maxDepthTerm, int u, int p) {
        int maxDepth = 0;
        depth[u] = (p == -1) ? 0 : depth[p] + 1;
        if (term[u]) {
            maxDepthTerm[0] = Math.max(maxDepthTerm[0], depth[u]);
        }
        int deepestNode = u;
        for (int j = 0; j < g.get(u).size(); j++) {
            int v = g.get(u).get(j);
            if (v != p) {
                int deepNode = dfs(g, depth, term, maxDepthTerm, v, u);
                if (depth[deepNode] > maxDepth) {
                    maxDepth = depth[deepNode];
                    deepestNode = deepNode;
                }
            }
        }
        return deepestNode;
    }

    private static int dfs_midpoint(ArrayList<IntList> g, int[] depth, boolean[] term, int[] neededVertex, int u, int p, int dist) {
        if (depth[u] == dist) {
            neededVertex[0] = u;
            return neededVertex[0];
        }
        for (int j = 0; j < g.get(u).size(); j++) {
            int v = g.get(u).get(j);
            if (v != p) {
                int deepNode = dfs_midpoint(g, depth, term, neededVertex, v, u, dist);
                if (depth[deepNode] == dist) {
                    return deepNode;
                }
            }
        }
        return neededVertex[0];
    }

    private static boolean verifyDistances(ArrayList<IntList> g, int[] c, boolean[] term, int[] maxDepthTerm, int v, int d) {
        int[] depth = new int[g.size()];
        dfs(g, depth, term, maxDepthTerm, v, -1);
        for (int city : c) {
            if (depth[city] != d) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<IntList> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new IntList());
        }
        int u, v;
        for (int i = 0; i < n - 1; i++) {
            u = scanner.nextInt();
            v = scanner.nextInt();
            u--;
            v--;
            g.get(u).add(v);
            g.get(v).add(u);
        }
        int[] c = new int[m];
        for (int i = 0; i < m; i++) {
            c[i] = scanner.nextInt() - 1;
        }
        int[] depth = new int[n];
        boolean[] term = new boolean[n];
        int[] maxDepthTerm = new int[1];
        int[] neededVertex = new int[1];
        for (int i = 0; i < m; i++) {
            term[c[i]] = true;
        }
        int cf = dfs(g, depth, term, maxDepthTerm, c[0], -1);
        int ansV = dfs_midpoint(g, depth, term, neededVertex, c[0], -1, depth[maxDepthTerm[0]] / 2);
        ansV = neededVertex[0];

        if (verifyDistances(g, c, term, maxDepthTerm, ansV, depth[maxDepthTerm[0]] / 2)) {
            System.out.println("YES");
            System.out.println(ansV + 1);
        } else {
            System.out.println("NO");
        }
    }
}
