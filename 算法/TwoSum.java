import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class TwoSum {

    public static class Solution {
        /**
         * Java 版哈希表优化解法
         * 时间复杂度: O(N)
         * 空间复杂度: O(N)
         */
        public int[] twoSum(int[] nums, int target) {
            // key: 数值, value: 对应的数组下标
            Map<Integer, Integer> numMap = new HashMap<>();

            for (int i = 0; i < nums.length; i++) {
                int complement = target - nums[i]; // 计算互补的值

                // 判断哈希表中是否存在这个 complement 键
                if (numMap.containsKey(complement)) {
                    return new int[] { numMap.get(complement), i }; // 找到后直接返回包含两个下标的数组
                }

                // 没找到则将当前数值和下标放入 Map 中
                numMap.put(nums[i], i);
            }
            return new int[] {}; // 没找到返回空数组
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {2, 7, 11, 15};
        int target = 9;

        int[] result = solution.twoSum(nums, target);

        System.out.println("========== 运行 Java 版哈希优化解法 ==========");
        // Arrays.toString() 可以把数组格式化打印成类似 [0, 1] 的字符串
        System.out.println("找到目标下标: " + Arrays.toString(result));
    }
}
