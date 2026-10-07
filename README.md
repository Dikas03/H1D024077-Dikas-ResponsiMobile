# 🎮 KasGame - Katalog dan Unduhan Video Game

Aplikasi mobile berbasis Android untuk mencari, melihat, menyaring kategori, mengunduh game, dan mengeksplorasi informasi katalog video game secara dinamis menggunakan **RAWG Video Games Database REST API**. Proyek ini dikembangkan menggunakan bahasa **Kotlin**, antarmuka deklaratif **Jetpack Compose (Material Design 3)**, dan arsitektur **MVVM (Model-View-ViewModel)**.

---

## 📋 Daftar Isi
1. [Fitur & Pemenuhan Persyaratan Teknis](#-fitur--pemenuhan-persyaratan-teknis)
2. [Arsitektur Aplikasi (MVVM)](#-arsitektur-aplikasi-mvvm)
3. [Alur Data, State, dan Recomposition](#-alur-data-state-dan-recomposition)
4. [Struktur Direktori Proyek](#-struktur-direktori-proyek)
5. [Panduan Konfigurasi RAWG API Key](#-panduan-konfigurasi-rawg-api-key)
6. [Cara Membuka & Menjalankan di Android Studio](#-cara-membuka--menjalankan-di-android-studio)

---

## ✨ Fitur & Pemenuhan Persyaratan Teknis

| Kategori | Persyaratan Soal | Implementasi pada Aplikasi KasGame |
| :--- | :--- | :--- |
| **Bahasa Pemrograman** | Kotlin (Data Class, Null Safety, Lambda) | • `data class Game`, `GameDetail`, `GameCategory`<br>• Null safety (`String?`, `?.let`, `?:`) di semua parsing DTO<br>• Lambda expression pada event callback (`onClick: () -> Unit`, `onQueryChange`, trailing lambda Compose) |
| **User Interface** | Jetpack Compose & Material 3 | • Full Declarative UI dengan Composable Layout<br>• Material Design 3 (`Scaffold`, `TopAppBar`, `Card`, `FilterChip`, `SuggestionChip`, `OutlinedTextField`)<br>• Dynamic Theming & Typography di `Theme.kt` dan `Type.kt` |
| **List & Data** | Lazy Layout (`LazyColumn` / `LazyVerticalGrid`) | • Menggunakan `LazyColumn` dengan `items(key = { it.id })` untuk efisiensi render list game |
| **State & Recomposition**| State-driven UI & Search Functionality | • `UiState<T>` (Sealed Interface: `Loading`, `Success`, `Error`)<br>• `StateFlow` di ViewModel dengan fungsi pencarian reaktif (debounced search query) |
| **Menu Kategori** | Filter Kategori / Genre | • Kategori interaktif (Action, RPG, Shooter, Adventure, Strategy, Sports, Racing, dll.) menggunakan `CategoryChipRow` (`LazyRow` + `FilterChip`) |
| **Fitur Download** | Unduh Game / Installer | • Fitur simulasi unduh game lengkap dengan progress bar interaktif, persentase kecepatan download, status unduhan, dan notifikasi Toast |
| **Networking** | REST API RAWG | • Retrofit 2 + Gson Converter + OkHttp Logging Interceptor<br>• Pengambilan data: Judul game, Rating angka, Tanggal rilis ISO 8601 (`YYYY-MM-DD`), dan Deskripsi game |
| **Arsitektur** | MVVM Architecture | • Model: Data DTO RAWG<br>• Repository: `GameRepository` & `GameRepositoryImpl`<br>• ViewModel: `HomeViewModel` & `DetailViewModel`<br>• View: Composable Screens |
| **Screens** | Minimal 2 Layar | 1. **Home Screen**: List game, rating, tanggal rilis, gambar banner, search bar interaktif, dan menu kategori.<br>2. **Game Detail Screen**: Banner besar, judul, rating angka, tanggal rilis ISO 8601, deskripsi lengkap, developer, genre, serta fitur Download Game. |

---

## 🏛️ Arsitektur Aplikasi (MVVM)

Aplikasi ini menerapkan pola desain **MVVM (Model-View-ViewModel)** dengan **Repository Pattern**:

```
 ┌─────────────────────────────────────────────────────────────┐
 │                         VIEW LAYER                          │
 │      HomeScreen (LazyColumn)  &  DetailScreen (Compose)     │
 └──────────────────────────────▲──────────────────────────────┘
                                │ Observe State (StateFlow)
                                │ Triggers User Events (Lambdas)
 ┌──────────────────────────────▼──────────────────────────────┐
 │                      VIEWMODEL LAYER                        │
 │           HomeViewModel      &      DetailViewModel         │
 └──────────────────────────────▲──────────────────────────────┘
                                │ Calls Suspend Functions
                                │ Returns Result<T>
 ┌──────────────────────────────▼──────────────────────────────┐
 │                     REPOSITORY LAYER                        │
 │            GameRepository ◄── GameRepositoryImpl           │
 └──────────────────────────────▲──────────────────────────────┘
                                │ Network Calls (Dispatchers.IO)
 ┌──────────────────────────────▼──────────────────────────────┐
 │                      NETWORK / DATA                         │
 │      ApiClient (Retrofit + OkHttp) ──► RawgApiService       │
 │      API Endpoint: https://api.rawg.io/api/games            │
 └─────────────────────────────────────────────────────────────┘
```

1. **Model & Network Layer**:
   - `RawgApiService`: Mendefinisikan endpoint Retrofit untuk request daftar game (`/games`) dan detail game (`/games/{id}`).
   - `ApiClient`: Singleton yang menyediakan instance Retrofit, converter Gson, dan OkHttpClient dengan logging interceptor.
2. **Repository Layer**:
   - `GameRepository`: Abstraksi data sumber yang mengeksekusi panggilan API di background thread (`Dispatchers.IO`) dan membungkus hasil dalam `Result<T>`.
3. **ViewModel Layer**:
   - Menyimpan state UI dalam bentuk `StateFlow<UiState<T>>` yang bertahan terhadap perubahan konfigurasi layar.
   - Mengelola logika pencarian dengan debouncing serta filter kategori/genre.
4. **View (Compose UI)**:
   - Komponen UI mengamati state melalui `collectAsState()` dan merender UI secara deklaratif sesuai status (`Loading`, `Success`, `Error`).

---

## 📁 Struktur Direktori Proyek

```
app/src/main/java/com/example/gamecatalog/
│
├── MainActivity.kt                  # Entry point aplikasi (set up Theme & NavGraph)
│
├── data/
│   ├── model/
│   │   └── GameModels.kt            # Data classes: Game, GameDetail, Genre, GameCategory
│   ├── remote/
│   │   ├── ApiClient.kt             # Konfigurasi Retrofit & OkHttpClient singleton
│   │   └── RawgApiService.kt        # Interface endpoint Retrofit RAWG API
│   └── repository/
│       ├── GameRepository.kt        # Interface kontrak repository
│       └── GameRepositoryImpl.kt    # Implementasi fetching data & error handling
│
├── ui/
│   ├── common/
│   │   └── UiState.kt               # Sealed interface penanganan state UI
│   ├── detail/
│   │   ├── DetailScreen.kt          # UI Screen detail game & fitur download
│   │   └── DetailViewModel.kt       # ViewModel, Factory, & DownloadState management
│   ├── home/
│   │   ├── HomeScreen.kt            # UI Screen utama dengan LazyColumn & kategori
│   │   ├── HomeViewModel.kt         # ViewModel untuk katalog, kategori, & pencarian
│   │   └── components/
│   │       ├── CategoryChipRow.kt   # Komponen baris kategori/genre (LazyRow)
│   │       ├── GameItemCard.kt      # Card item game di dalam LazyColumn
│   │       └── SearchBarView.kt     # Komponen kolom input pencarian
│   ├── navigation/
│   │   ├── AppNavGraph.kt           # NavHost & rute navigasi antar screen
│   │   └── Screen.kt                # Definisi route & parameter navigasi
│   └── theme/
│       ├── Color.kt                 # Palet warna modern Material 3
│       ├── Theme.kt                 # Dark & Light Theme configuration
│       └── Type.kt                  # Tipografi Material 3
│
└── util/
    └── Constants.kt                 # Base URL & RAWG API Key
```

---

## 🔑 Panduan Konfigurasi RAWG API Key

Aplikasi ini menggunakan API resmi dari RAWG:
1. Kunjungi [https://rawg.io/apidocs](https://rawg.io/apidocs) dan buat akun gratis.
2. Dapatkan API Key Anda pada dashboard akun.
3. Buka file `Constants.kt` di lokasi:
   `app/src/main/java/com/example/gamecatalog/util/Constants.kt`
4. Ganti nilai `RAWG_API_KEY`:
   ```kotlin
   object Constants {
       const val BASE_URL = "https://api.rawg.io/api/"
       const val RAWG_API_KEY = "MASUKKAN_API_KEY_ANDA_DISINI"
   }
   ```

---

## 🚀 Cara Membuka & Menjalankan di Android Studio

1. **Buka Project**:
   - Buka Android Studio.
   - Pilih menu **File > Open**, lalu arahkan ke folder proyek ini (`responsi pemmob`).
2. **Sinkronisasi Gradle**:
   - Tunggu Android Studio mengunduh dependensi dan menyelesaikan proses *Gradle Sync*.
3. **Konfigurasi API Key**:
   - Pastikan telah mengisi `RAWG_API_KEY` pada `Constants.kt`.
4. **Jalankan Aplikasi**:
   - Pilih emulator Android atau perangkat fisik dengan USB Debugging aktif (Minimum SDK: Android 7.0 / API 24).
   - Klik tombol **Run 'app'** (ikon segitiga hijau) atau tekan `Shift + F10`.
