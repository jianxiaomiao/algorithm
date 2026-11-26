import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class threeSum {

    public static class Solution {
        /**
         * 三数之和：排序 + 双指针解法
         * 时间复杂度: O(N^2)
         * 空间复杂度: O(1) - 仅需常数级指针空间
         */
        public List<List<Integer>> threeSum(int[] nums) {
            List<List<Integer>> ans = new ArrayList<>();
            if (nums == null || nums.length < 3) {
                return ans;
            }

            // 1. 先进行升序排序 (去重的基础)
            Arrays.sort(nums);

            for (int i = 0; i < nums.length; i++) {
                // 如果当前固定数已经大于 0，后面都是正数，不可能相加为 0，直接退出
                if (nums[i] > 0) {
                    break;
                }

                // 对固定数 i 进行去重：如果和前一个数相同，说明这个起步数的全部组合刚才已经找过了，直接跳过
                if (i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }

                // 2. 左右双指针夹逼
                int left = i + 1;
                int right = nums.length - 1;

                while (left < right) {
                    int sum = nums[i] + nums[left] + nums[right];

                    if (sum == 0) {
                        // 找到符合条件的三元组，存入结果列表
                        ans.add(Arrays.asList(nums[i], nums[left], nums[right]));

                        // 核心去重：跳过左指针和右指针旁边重复的数值
                        while (left < right && nums[left] == nums[left + 1]) {
                            left++;
                        }
                        while (left < right && nums[right] == nums[right - 1]) {
                            right--;
                        }

                        // 收缩指针，继续在当前 i 之下寻找其他可能
                        left++;
                        right--;
                    } else if (sum < 0) {
                        // 此时和小于 0，需要让数值变大，左指针右移
                        left++;
                    } else {
                        // 此时和大于 0，需要让数值变小，右指针左移
                        right--;
                    }
                }
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {-1, 0, 1, 2, -1, -4};

        System.out.println("========== 运行三数之和 (排序+双指针) ==========");
        System.out.println("输入数组: " + Arrays.toString(nums));
        
        List<List<Integer>> result = solution.threeSum(nums);
        
        System.out.println("输出三元组: " + result);
    }
}