<template>
  <div>
    <h1>Learning Statistics</h1>
    <div v-if="statistics">
      <p>Total number of users: {{ statistics.totalUsers }}</p>
      <p>Number of active users: {{ statistics.activeUsers }}</p>
      <p>Number of courses offered: {{ statistics.coursesOffered }}</p>
      <p>Number of courses completed: {{ statistics.coursesCompleted }}</p>
      <p>Average time spent on the platform: {{ statistics.averageTimeSpent }}</p>
      <p>Number of logins per user: {{ statistics.loginsPerUser }}</p>
      <p>Number of assignments submitted: {{ statistics.assignmentsSubmitted }}</p>
      <p>Number of quizzes taken: {{ statistics.quizzesTaken }}</p>
      <p>Average quiz scores: {{ statistics.averageQuizScores }}</p>
      <p>Attendance records for live classes: {{ statistics.attendanceRecords }}</p>
      <p>Participation in discussions or forums: {{ statistics.participationInDiscussions }}</p>
      <p>Number of resources accessed: {{ statistics.resourcesAccessed }}</p>
      <p>Completion rates for each course: {{ statistics.completionRates }}</p>
      <p>Comparison of statistics for different partner companies: {{ statistics.comparisonStatistics }}</p>
      <p>Monthly statistics: {{ statistics.monthlyStatistics }}</p>
      <p>Daily statistics: {{ statistics.dailyStatistics }}</p>
      <p>Weekly statistics: {{ statistics.weeklyStatistics }}</p>
      <p>Yearly statistics: {{ statistics.yearlyStatistics }}</p>
      <p>On/Offline education integration statistics: {{ statistics.integrationStatistics }}</p>
      <p>Evaluation results and progress tracking for individual learners: {{ statistics.evaluationResults }}</p>
    </div>
    <div v-else>
      <p>Loading statistics...</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      statistics: null,
    };
  },
  created() {
    this.fetchStatistics();
  },
  methods: {
    async fetchStatistics() {
      try {
        const response = await axios.get('/api/statistics/learning');
        this.statistics = response.data;
      } catch (error) {
        console.error('Error fetching statistics:', error);
      }
    },
  },
};
</script>

<style scoped>
h1 {
  font-size: 24px;
  margin-bottom: 20px;
}
p {
  font-size: 18px;
  margin: 5px 0;
}
</style>
