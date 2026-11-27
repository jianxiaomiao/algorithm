package day12;


// 56. 合并区间
// 中等
// 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 
// 。请你合并所有重叠的区间，并返回 一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间 。

// 示例 1：
// 输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
// 输出：[[1,6],[8,10],[15,18]]
// 解释：区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].

// 示例 2：
// 输入：intervals = [[1,4],[4,5]]
// 输出：[[1,5]]
// 解释：区间 [1,4] 和 [4,5] 可被视为重叠区间。

// 示例 3：
// 输入：intervals = [[4,7],[1,4]]
// 输出：[[1,7]]
// 解释：区间 [1,4] 和 [4,7] 可被视为重叠区间。
 

// 提示：

// 1 <= intervals.length <= 104
// intervals[i].length == 2
// 0 <= starti <= endi <= 104

//我的想法:首先进行排序,排序规则是第一个数字的大小,这样就有序了
//有序之后我们判断前面的尾部是否>=后面的头部就可以了,然后一直合并加入....
//按照画图的做法,也是先给每个区间画出来(排序),然后逐项合并,所以我的想法应该是对的
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public int[][] merge(int[][] intervals) {
        // 1. 按区间左端点升序排序
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> res = new ArrayList<>();
        int start = intervals[0][0];
        int end = intervals[0][1];

        // 2. 从第二个区间开始逐个合并
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= end) {
                // 有重叠，更新右端点为较大值（左端点保持 start 不变）
                end = Math.max(end, intervals[i][1]);
            } else {
                // 不重叠，保存当前合并好的区间，并开启新区间
                res.add(new int[]{start, end});
                start = intervals[i][0];
                end = intervals[i][1];
            }
        }

        // 3. 把最后一个区间加入结果
        res.add(new int[]{start, end});

        return res.toArray(new int[res.size()][]);
    }
}
