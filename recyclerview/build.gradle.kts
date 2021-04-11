plugins {
    id("com.android.library")
}

dependencies {
    val androidRecyclerView: String by rootProject
    api("androidx.recyclerview:recyclerview:$androidRecyclerView")
}
