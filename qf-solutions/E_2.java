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
            return data[index];
        }

        public int size() {
            return size;
        }
    }

    private static int dfs(ArrayList<IntList> g, int[] depth, int u, int p) {
        int farthestNode = u;
        for (int i = 0; i < g.get(u).size(); i++) {
            int v = g.get(u).get(i);
            if (v != p) {
                depth[v] = depth[u] + 1;
                int deepNode = dfs(g, depth, v, u);
                if (depth[deepNode] > depth[farthestNode]) {
                    farthestNode = deepNode;
                }
            }
        }
        return farthestNode;
    }

    private static boolean verifyDistances(ArrayList<IntList> g, int[] c, int expectedDistance, int candidate) {
        int[] depth = new int[g.size()];
        Arrays.fill(depth, -1);
        depth[candidate] = 0;
        dfsDepthOnly(g, depth, candidate, -1);
        for (int city : c) {
            if (depth[city] != expectedDistance) return false;
        }
        return true;
    }

    private static void dfsDepthOnly(ArrayList<IntList> g, int[] depth, int u, int p) {
        for (int i = 0; i < g.get(u).size(); i++) {
            int v = g.get(u).get(i);
            if (v != p) {
                depth[v] = depth[u] + 1;
                dfsDepthOnly(g, depth, v, u);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<IntList> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new IntList());
        }
        for (int i = 0; i < n - 1; i++) {
            int u = scanner.nextInt() - 1;
            int v = scanner.nextInt() - 1;
            g.get(u).add(v);
            g.get(v).add(u);
        }
        int[] c = new int[m];
        for (int i = 0; i < m; i++) {
            c[i] = scanner.nextInt() - 1;
        }

        int[] depth = new int[n];
        int start = c[0];
        int farthestFromStart = dfs(g, depth, start, -1);
        Arrays.fill(depth, 0);
        int farthestFromEnd = dfs(g, depth, farthestFromStart, -1);
        int diameterLength = depth[farthestFromEnd];

        int targetDistance = diameterLength / 2;
        int candidate = farthestFromStart;
        Arrays.fill(depth, 0);
        dfs(g, depth, farthestFromStart, -1);
        for (int i = 0; i < targetDistance; i++) {
            candidate = g.get(candidate).get(0);
        }

        if (verifyDistances(g, c, targetDistance, candidate)) {
            System.out.println("YES");
            System.out.println(candidate + 1);
        } else {
            System.out.println("NO");
        }
    }
}
