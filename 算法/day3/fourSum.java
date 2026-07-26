import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class fourSum {

    public static class Solution {
        /**
         * 四数之和：排序 + 双重循环固定 + 双指针夹逼
         * 时间复杂度: O(N^3)
         * 空间复杂度: O(1) (不计结果列表)
         */
        public List<List<Integer>> fourSum(int[] nums, int target) {
            List<List<Integer>> ans = new ArrayList<>();
            if (nums == null || nums.length < 4) {
                return ans;
            }

            // 1. 先进行升序排序 (去重与双指针的基础)
            Arrays.sort(nums);
            int n = nums.length;

            // 2. 第一层循环：固定第一个数 nums[i]
            for (int i = 0; i < n - 3; i++) {
                // 第一层去重：如果和前一个数相同，跳过
                if (i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }

                // 剪枝优化 1：当前最小的四个数之和已经大于 target，后续肯定更大，直接 break
                if ((long) nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target) {
                    break;
                }
                // 剪枝优化 2：当前数加上最大的三个数之和仍小于 target，说明当前的 nums[i] 太小，continue 换下一个 nums[i]
                if ((long) nums[i] + nums[n - 3] + nums[n - 2] + nums[n - 1] < target) {
                    continue;
                }

                // 3. 第二层循环：固定第二个数 nums[j]
                for (int j = i + 1; j < n - 2; j++) {
                    // 第二层去重：j > i + 1 且和前一个数相同，跳过
                    if (j > i + 1 && nums[j] == nums[j - 1]) {
                        continue;
                    }

                    // 剪枝优化 3：当前组合最小和 > target，break
                    if ((long) nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target) {
                        break;
                    }
                    // 剪枝优化 4：当前组合最大和 < target，continue
                    if ((long) nums[i] + nums[j] + nums[n - 2] + nums[n - 1] < target) {
                        continue;
                    }

                    // 4. 双指针夹逼寻找后两个数 nums[left] 和 nums[right]
                    int left = j + 1;
                    int right = n - 1;

                    while (left < right) {
                        // 关键点：使用 long 强制转换避免四个 int 相加产生数值溢出
                        long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];

                        if (sum == target) {
                            ans.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));

                            // 左指针和右指针去重
                            while (left < right && nums[left] == nums[left + 1]) {
                                left++;
                            }
                            while (left < right && nums[right] == nums[right - 1]) {
                                right--;
                            }

                            left++;
                            right--;
                        } else if (sum < target) {
                            left++;
                        } else {
                            right--;
                        }
                    }
                }
            }
            return ans;
        }

    }

    public static void main(String[] args) {
        Solution solution = new Solution();

        int[] nums1 = {1, 0, -1, 0, -2, 2};
        int target1 = 0;
        System.out.println("========== 示例 1 ==========");
        System.out.println("输入: nums = [1,0,-1,0,-2,2], target = 0");
        System.out.println("输出: " + solution.fourSum(nums1, target1));

        int[] nums2 = {2, 2, 2, 2, 2};
        int target2 = 8;
        System.out.println("========== 示例 2 ==========");
        System.out.println("输入: nums = [2,2,2,2,2], target = 8");
        System.out.println("输出: " + solution.fourSum(nums2, target2));

        int[] nums3 = {1000000000, 1000000000, 1000000000, 1000000000};
        int target3 = -294967296;
        System.out.println("========== 溢出测试用例 ==========");
        System.out.println("输出: " + solution.fourSum(nums3, target3));
    }
}