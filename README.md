# ImageSearch
Google image search with SerpApi.

Example project containing grid list (RecyclerView) with search bar and view pager to observe the images.

Written on Kotlin.

# Libraries and approaches used
- Architecture
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): store data being unaffected by configuration changes. One of the building blocks of MVVM pattern
  - [Navigation](https://developer.android.com/guide/navigation): in-app navigation replacing FragmentManager and providing storyboard via navigation graphs
  - [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle): creating UI components automatically responding to lifecycle change events
  - [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html): following inward dependency rule. Encapsulating business rules and entities.
  - [Modularization](https://youtu.be/PZBg5DIzNww?t=658): keep functional layers separate from each other: application, data handling and independant business logic
- UI
  - [Shared element transitions](https://developer.android.com/guide/fragments/animate): animations for navigations between fragments
  - [Fragment](https://developer.android.com/guide/fragments): UI building blocks for easier use instead of multiple Activity approach
  - [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview): library for fetching paged data
  - [ViewPager](https://developer.android.com/training/animation/vp2-migration): swipe Views or Fragments inside other UI component like Fragment or Activity
- Third party libraries
  - [Glide](https://bumptech.github.io/glide/): image loading
  - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android): Dependency Injection
  - [Kotlin coroutines](https://kotlinlang.org/docs/coroutines-overview.html): executions code concurrently, Flow usage for managing reactive streams of data
