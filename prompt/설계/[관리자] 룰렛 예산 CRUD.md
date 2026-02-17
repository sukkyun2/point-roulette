# [관리자] 룰렛 예산 CRUD

```sql
╭───────────────────────────────────────────────────╮
│ ✻ Welcome to Claude Code!                         │
│                                                   │
│   /help for help, /status for your current setup  │
│                                                   │
│   cwd: /Users/goobg/Desktop/point-roulette        │
╰───────────────────────────────────────────────────╯

> @api/src/main/kotlin/org/example/roulette/api/roulette/domain/DailyBudget.kt 을 RouletteBudget으로 이름을 변경하고 테이블명도 변경할거야
  예상되는 수정범위 계산해줘
  ⎿  Read api/src/main/kotlin/org/example/roulette/api/roulette/domain/DailyBudget.kt (47 lines)

⏺ Search(pattern: "DailyBudget")
  ⎿  Found 7 files (ctrl+r to expand)

⏺ Search(pattern: "daily_budget")
  ⎿  Found 2 files (ctrl+r to expand)

⏺ Search(pattern: "DailyBudget", output_mode: "content")
  ⎿  Found 14 lines (ctrl+r to expand)

⏺ Read(api/src/main/resources/db/migration/V1__Create_initial_tables.sql)
  ⎿  Read 107 lines (ctrl+r to expand)

⏺ Search(pattern: "**/*test*/**/*.kt")
  ⎿  Found 0 files 

⏺ Search(pattern: "**/*Test*.kt")
  ⎿  Found 4 files (ctrl+r to expand)
  ⎿  api/CLAUDE.md

⏺ Search(pattern: "DailyBudget", glob: "**/*Test*.kt", type: "kt")
  ⎿  Found 0 files 
  ⎿  User rejected Claude's plan:
     ╭────────────────────────────────────────────────────────────────────────────────────────────╮
     │ DailyBudget → RouletteBudget 이름 변경 예상 수정 범위                                      │
     │                                                                                            │
     │ 1. 코드 파일 수정 (총 7개 파일)                                                            │
     │                                                                                            │
     │ - DailyBudget.kt → RouletteBudget.kt로 파일명 변경 + 클래스명 변경                         │
     │ - DailyBudgetRepository.kt → RouletteBudgetRepository.kt로 파일명 변경 + 인터페이스명 변경 │
     │ - RouletteCancelService.kt - import 및 필드명 변경                                         │
     │ - AdminDashboardQueryService.kt - import 및 필드명 변경                                    │
     │ - RouletteParticipateService.kt - import 및 필드명 변경                                    │
     │ - RouletteStatusQueryService.kt - import 및 필드명 변경                                    │
     │ - Roulette.kt - 필드 타입 및 getter 메서드명 변경                                          │
     │                                                                                            │
     │ 2. 데이터베이스 마이그레이션 파일 생성                                                     │
     │                                                                                            │
     │ - V2__Rename_daily_budget_to_roulette_budget.sql 생성                                      │
     │ - daily_budget 테이블을 roulette_budget으로 이름 변경                                      │
     │ - 테이블 및 컬럼 주석 업데이트                                                             │
     │                                                                                            │
     │ 3. 주요 변경사항 요약                                                                      │
     │                                                                                            │
     │ - 파일명: DailyBudget.kt → RouletteBudget.kt                                               │
     │ - 클래스명: DailyBudget → RouletteBudget                                                   │
     │ - 테이블명: daily_budget → roulette_budget                                                 │
     │ - Repository: DailyBudgetRepository → RouletteBudgetRepository                             │
     │ - 모든 import 문 및 타입 참조 업데이트                                                     │
     │ - 테스트 파일 없음 확인됨                                                                  │
     │                                                                                            │
     │ 총 8개 파일 수정 (7개 Kotlin 파일 + 1개 마이그레이션 파일 생성)                            │
     ╰────────────────────────────────────────────────────────────────────────────────────────────╯

> flyway는 v4야

⏺ Search(pattern: "**/db/migration/V*.sql")
  ⎿  Found 6 files (ctrl+r to expand)
                                                                                               
⏺ User approved Claude's plan:
  ⎿  DailyBudget → RouletteBudget 이름 변경 예상 수정 범위

     1. 코드 파일 수정 (총 7개 파일)

     - DailyBudget.kt → RouletteBudget.kt로 파일명 변경 + 클래스명 변경
     - DailyBudgetRepository.kt → RouletteBudgetRepository.kt로 파일명 변경 + 인터페이스명 변경
     - RouletteCancelService.kt - import 및 필드명 변경
     - AdminDashboardQueryService.kt - import 및 필드명 변경  
     - RouletteParticipateService.kt - import 및 필드명 변경
     - RouletteStatusQueryService.kt - import 및 필드명 변경
     - Roulette.kt - 필드 타입 및 getter 메서드명 변경

     2. 데이터베이스 마이그레이션 파일 생성

     - V4__Rename_daily_budget_to_roulette_budget.sql 생성 (기존 V3 다음)
     - daily_budget 테이블을 roulette_budget으로 이름 변경
     - 테이블 및 컬럼 주석 업데이트

     3. 주요 변경사항 요약

     - 파일명: DailyBudget.kt → RouletteBudget.kt
     - 클래스명: DailyBudget → RouletteBudget  
     - 테이블명: daily_budget → roulette_budget
     - Repository: DailyBudgetRepository → RouletteBudgetRepository
     - 모든 import 문 및 타입 참조 업데이트
     - 테스트 파일 없음 확인됨

     총 8개 파일 수정 (7개 Kotlin 파일 + 1개 V4 마이그레이션 파일 생성)

> 수정진행해

⏺ Update(api/src/main/kotlin/org/example/roulette/api/roulette/domain/DailyBudget.kt)
  ⎿  Updated api/src/main/kotlin/org/example/roulette/api/roulette/domain/DailyBudget.kt with 2 additions and 2 removals
       12    import java.time.LocalDateTime
       13    
       14    @Entity
       15 -  @Table(name = "daily_budget")
       16 -  data class DailyBudget(
       15 +  @Table(name = "roulette_budget")
       16 +  data class RouletteBudget(
       17        @Id
       18        @GeneratedValue(strategy = GenerationType.IDENTITY)
       19        @Column(name = "id")

⏺ Bash(mv "/Users/goobg/Desktop/point-roulette/api/src/main/kotlin/org/example/roulette/api/roulette/domain/DailyBudget.kt" "/Users/goobg/Desktop/point-roulette/api/sr…)
  ⎿  (No content)

⏺ Read(api/src/main/kotlin/org/example/roulette/api/roulette/domain/DailyBudgetRepository.kt)
  ⎿  Read 11 lines (ctrl+r to expand)

⏺ Update(api/src/main/kotlin/org/example/roulette/api/roulette/domain/DailyBudgetRepository.kt)
  ⎿  Updated api/src/main/kotlin/org/example/roulette/api/roulette/domain/DailyBudgetRepository.kt with 2 additions and 2 removals
        5    import java.time.LocalDate
        6    
        7    @Repository
        8 -  interface DailyBudgetRepository : JpaRepository<DailyBudget, Long> {
        9 -      fun findByBudgetDate(budgetDate: LocalDate): DailyBudget?
        8 +  interface RouletteBudgetRepository : JpaRepository<RouletteBudget, Long> {
        9 +      fun findByBudgetDate(budgetDate: LocalDate): RouletteBudget?
       10    }

⏺ Bash(mv "/Users/goobg/Desktop/point-roulette/api/src/main/kotlin/org/example/roulette/api/roulette/domain/DailyBudgetRepository.kt" "/Users/goobg/Desktop/point-roule…)
  ⎿  (No content)

⏺ Read(api/src/main/kotlin/org/example/roulette/api/roulette/app/RouletteCancelService.kt)
  ⎿  Read 65 lines (ctrl+r to expand)

⏺ Update(api/src/main/kotlin/org/example/roulette/api/roulette/app/RouletteCancelService.kt)
  ⎿  Updated api/src/main/kotlin/org/example/roulette/api/roulette/app/RouletteCancelService.kt with 6 additions and 6 removals
        5    import org.example.roulette.api.point.domain.Point
        6    import org.example.roulette.api.point.domain.PointType
        7    import org.example.roulette.api.point.domain.ReferenceType
        8 -  import org.example.roulette.api.roulette.domain.DailyBudgetRepository
        8 +  import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
        9    import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
       10    import org.example.roulette.api.roulette.domain.RouletteStatus
       11    import org.example.roulette.api.user.domain.UserQueryService
     ...
       16    @Transactional
       17    class RouletteCancelService(
       18        private val rouletteHistoryRepository: RouletteHistoryRepository,
       19 -      private val dailyBudgetRepository: DailyBudgetRepository,
       19 +      private val rouletteBudgetRepository: RouletteBudgetRepository,
       20        private val userQueryService: UserQueryService,
       21        private val pointHistoryAppender: PointHistoryAppender,
       22    ) {
     ...
       37            user.deductBalanceAllowNegative(earnedPoint)
       38    
       39            // 예산에 포인트 재반영
       40 -          val dailyBudget =
       41 -              dailyBudgetRepository.findByBudgetDate(rouletteHistory.eventDate)
       40 +          val rouletteBudget =
       41 +              rouletteBudgetRepository.findByBudgetDate(rouletteHistory.eventDate)
       42                    ?: throw NoDataException("해당 일자의 예산 정보를 찾을 수 없습니다.")
       43    
       44 -          dailyBudget.addBudget(earnedPoint)
       44 +          rouletteBudget.addBudget(earnedPoint)
       45    
       46            // 룰렛 참여 상태를 취소로 변경
       47            val updatedHistory =
     ...
       49                    status = RouletteStatus.CANCELED,
       50                )
       51    
       52 -          dailyBudgetRepository.save(dailyBudget)
       52 +          rouletteBudgetRepository.save(rouletteBudget)
       53            rouletteHistoryRepository.save(updatedHistory)
       54    
       55            // 포인트 회수 히스토리 기록

⏺ Read(api/src/main/kotlin/org/example/roulette/api/roulette/app/AdminDashboardQueryService.kt)
  ⎿  Read 26 lines (ctrl+r to expand)

⏺ Update(api/src/main/kotlin/org/example/roulette/api/roulette/app/AdminDashboardQueryService.kt)
  ⎿  Updated api/src/main/kotlin/org/example/roulette/api/roulette/app/AdminDashboardQueryService.kt with 5 additions and 5 removals
       1    package org.example.roulette.api.roulette.app
       2    
       3 -  import org.example.roulette.api.roulette.domain.DailyBudgetRepository
       3 +  import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
       4    import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
       5    import org.springframework.stereotype.Service
       6    import org.springframework.transaction.annotation.Transactional
     ...
        9    @Service
       10    @Transactional(readOnly = true)
       11    class AdminDashboardQueryService(
       12 -      private val dailyBudgetRepository: DailyBudgetRepository,
       12 +      private val rouletteBudgetRepository: RouletteBudgetRepository,
       13        private val rouletteHistoryRepository: RouletteHistoryRepository,
       14    ) {
       15        fun getDashboard(targetDate: LocalDate): AdminDashboardQueryResponse {
       16 -          val dailyBudget = dailyBudgetRepository.findByBudgetDate(targetDate)
       16 +          val rouletteBudget = rouletteBudgetRepository.findByBudgetDate(targetDate)
       17            val participantCount = rouletteHistoryRepository.countDistinctUsersByEventDate(targetDate)
       18    
       19            return AdminDashboardQueryResponse(
       20 -              totalBudget = dailyBudget?.totalBudget ?: 0L,
       21 -              remainingBudget = dailyBudget?.remainingBudget ?: 0L,
       20 +              totalBudget = rouletteBudget?.totalBudget ?: 0L,
       21 +              remainingBudget = rouletteBudget?.remainingBudget ?: 0L,
       22                participantCount = participantCount,
       23            )
       24        }

⏺ Read(api/src/main/kotlin/org/example/roulette/api/roulette/app/RouletteParticipateService.kt)
  ⎿  Read 71 lines (ctrl+r to expand)

⏺ Update(api/src/main/kotlin/org/example/roulette/api/roulette/app/RouletteParticipateService.kt)
  ⎿  Updated api/src/main/kotlin/org/example/roulette/api/roulette/app/RouletteParticipateService.kt with 6 additions and 6 removals
        5    import org.example.roulette.api.point.domain.Point
        6    import org.example.roulette.api.point.domain.PointType
        7    import org.example.roulette.api.point.domain.ReferenceType
        8 -  import org.example.roulette.api.roulette.domain.DailyBudgetRepository
        8 +  import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
        9    import org.example.roulette.api.roulette.domain.Roulette
       10    import org.example.roulette.api.roulette.domain.RouletteHistory
       11    import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
     ...
       18    @Transactional
       19    class RouletteParticipateService(
       20        private val userQueryService: UserQueryService,
       21 -      private val dailyBudgetRepository: DailyBudgetRepository,
       21 +      private val rouletteBudgetRepository: RouletteBudgetRepository,
       22        private val rouletteHistoryRepository: RouletteHistoryRepository,
       23        private val pointHistoryAppender: PointHistoryAppender,
       24    ) {
     ...
       32            }
       33    
       34            // 당일 예산 조회
       35 -          val dailyBudget =
       36 -              dailyBudgetRepository.findByBudgetDate(today)
       35 +          val rouletteBudget =
       36 +              rouletteBudgetRepository.findByBudgetDate(today)
       37                    ?: throw EventPeriodException()
       38    
       39 -          val roulette = Roulette(dailyBudget, user)
       39 +          val roulette = Roulette(rouletteBudget, user)
       40    
       41            // 예산 충분성 검증
       42            if (!roulette.canParticipate()) {
     ...
       45    
       46            val history = roulette.participate()
       47    
       48 -          dailyBudgetRepository.save(roulette.getDailyBudget())
       48 +          rouletteBudgetRepository.save(roulette.getRouletteBudget())
       49            appendHistory(history)
       50    
       51            return Point(history.earnPoint)

⏺ Read(api/src/main/kotlin/org/example/roulette/api/roulette/app/RouletteStatusQueryService.kt)
  ⎿  Read 32 lines (ctrl+r to expand)

⏺ Update(api/src/main/kotlin/org/example/roulette/api/roulette/app/RouletteStatusQueryService.kt)
  ⎿  Updated api/src/main/kotlin/org/example/roulette/api/roulette/app/RouletteStatusQueryService.kt with 5 additions and 5 removals
       2    
       3    import org.example.roulette.api.common.app.NoDataException
       4    import org.example.roulette.api.roulette.app.RouletteStatusQueryResponse
       5 -  import org.example.roulette.api.roulette.domain.DailyBudgetRepository
       5 +  import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
       6    import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
       7    import org.springframework.stereotype.Service
       8    import org.springframework.transaction.annotation.Transactional
     ...
       11    @Service
       12    @Transactional(readOnly = true)
       13    class RouletteStatusQueryService(
       14 -      private val dailyBudgetRepository: DailyBudgetRepository,
       14 +      private val rouletteBudgetRepository: RouletteBudgetRepository,
       15        private val rouletteHistoryRepository: RouletteHistoryRepository,
       16    ) {
       17        fun getStatus(userId: Long): RouletteStatusQueryResponse {
     ...
       19    
       20            val hasParticipatedToday = rouletteHistoryRepository.existsByUserIdAndEventDate(userId, today)
       21    
       22 -          val dailyBudget =
       23 -              dailyBudgetRepository.findByBudgetDate(today)
       22 +          val rouletteBudget =
       23 +              rouletteBudgetRepository.findByBudgetDate(today)
       24                    ?: throw NoDataException()
       25    
       26            return RouletteStatusQueryResponse(
       27                hasParticipatedToday = hasParticipatedToday,
       28 -              remainingBudget = dailyBudget.remainingBudget,
       28 +              remainingBudget = rouletteBudget.remainingBudget,
       29            )
       30        }
       31    }

⏺ Read(api/src/main/kotlin/org/example/roulette/api/roulette/domain/Roulette.kt)
  ⎿  Read 39 lines (ctrl+r to expand)

⏺ Update(api/src/main/kotlin/org/example/roulette/api/roulette/domain/Roulette.kt)
  ⎿  Updated api/src/main/kotlin/org/example/roulette/api/roulette/domain/Roulette.kt with 5 additions and 5 removals
        5    import java.time.LocalDate
        6    
        7    class Roulette(
        8 -      private val dailyBudget: DailyBudget,
        8 +      private val rouletteBudget: RouletteBudget,
        9        private val user: User,
       10        private val roulettePointPolicy: RoulettePointPolicy = RoulettePointPolicy(),
       11        private val eventDate: LocalDate = LocalDate.now(),
       12    ) {
       13 -      fun canParticipate(): Boolean = dailyBudget.canParticipate(roulettePointPolicy.getMinPoint())
       13 +      fun canParticipate(): Boolean = rouletteBudget.canParticipate(roulettePointPolicy.getMinPoint())
       14    
       15        fun participate(): RouletteHistory {
       16            // 랜덤 포인트 생성
       17 -          val earnedPoint = roulettePointPolicy.generateRandomPoints(dailyBudget.remainingBudget)
       17 +          val earnedPoint = roulettePointPolicy.generateRandomPoints(rouletteBudget.remainingBudget)
       18    
       19            // 예산 차감
       20 -          dailyBudget.deductBudget(earnedPoint)
       20 +          rouletteBudget.deductBudget(earnedPoint)
       21    
       22            // 사용자 포인트 적립
       23            user.addBalance(earnedPoint)
     ...
       32                earnPoint = earnedPoint.value,
       33            )
       34    
       35 -      fun getDailyBudget() = dailyBudget
       35 +      fun getRouletteBudget() = rouletteBudget
       36    
       37        fun getUser() = user
       38    }

⏺ Write(api/src/main/resources/db/migration/V4__Rename_daily_budget_to_roulette_budget.sql)
  ⎿  Wrote 11 lines to api/src/main/resources/db/migration/V4__Rename_daily_budget_to_roulette_budget.sql
     -- 일일 예산 테이블을 룰렛 예산 테이블로 이름 변경
     ALTER TABLE daily_budget RENAME TO roulette_budget;
     -- 테이블 주석 업데이트
     COMMENT ON TABLE roulette_budget IS '룰렛 예산 관리';
     COMMENT ON COLUMN roulette_budget.id IS '룰렛 예산 ID';
     COMMENT ON COLUMN roulette_budget.budget_date IS '예산 기준 날짜 (yyyy-MM-dd)';
     COMMENT ON COLUMN roulette_budget.total_budget IS '해당 일자의 총 예산';
     COMMENT ON COLUMN roulette_budget.remaining_budget IS '현재 남은 예산';
     COMMENT ON COLUMN roulette_budget.created_at IS '룰렛 예산 생성 일시';
     … +1 line (ctrl+r to expand)

⏺ DailyBudget을 RouletteBudget으로 이름 변경 완료했습니다.

  수정된 파일:
  - DailyBudget.kt → RouletteBudget.kt (클래스명 및 테이블명 변경)
  - DailyBudgetRepository.kt → RouletteBudgetRepository.kt (인터페이스명 변경)
  - RouletteCancelService.kt (import 및 필드 참조 변경)
  - AdminDashboardQueryService.kt (import 및 필드 참조 변경)
  - RouletteParticipateService.kt (import 및 필드 참조 변경)
  - RouletteStatusQueryService.kt (import 및 필드 참조 변경)
  - Roulette.kt (필드 타입 및 getter 메서드명 변경)
  - V4__Rename_daily_budget_to_roulette_budget.sql (마이그레이션 파일 생성)

  모든 참조가 업데이트되었고 데이터베이스 마이그레이션 파일도 생성되었습니다.

> /export 
  ⎿  Conversation copied to clipboard

```