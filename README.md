# Module 01: Coding Standards

# Refleksi 1

### 1. Clean Code Principles
Selama pengerjaan fitur *Edit* dan *Delete* ini, saya mencoba menerapkan beberapa prinsip *Clean Code* agar kodingan saya gak cuma "jalan", tapi juga rapi:

* **Meaningful Names (Penamaan yang Jelas):** Saya berusaha kasih nama variabel dan function yang deskriptif. Contohnya `productService`, `findById`, dan `update`. Jadi, pas baca kodenya lagi, saya gak perlu nebak-nebak ini fungsinya buat apa. Saya juga misahin `ProductController`, `ProductService`, dan `ProductRepository` supaya jelas tanggung jawab masing-masing.

* **Single Responsibility Principle (SRP):** Saya menjaga agar setiap class punya tugas yang spesifik sesuai arsitektur MVC:
    * **Controller:** Cuma buat ngatur lalu lintas *request* dari user.
    * **Service:** Tempat mikir logika bisnisnya.
    * **Repository:** Fokus urusin data (simpan/ambil dari List).
      Dengan begini, kodenya jadi lebih modular dan gampang di-maintain.

### 2. Secure Coding Practices
Untuk keamanan (walau masih dasar), saya menerapkan hal berikut:

* **UUID for IDs:** Alih-alih pakai angka urut (1, 2, 3...) untuk ID produk, saya pakai `UUID`. Ini penting banget buat ngehindarin orang iseng yang coba-coba nebak ID produk lain (IDOR). Kalau pakai UUID, ID-nya jadi acak dan susah ditebak.

### 3. Mistakes & Future Improvements
Setelah saya review ulang, jujur ada satu kebiasaan kurang aman yang saya lakuin di fitur **Delete**:

* **Kekurangan:** Saya men-trigger proses *delete* menggunakan method `GET` (lewat link `<a>` biasa di HTML dan `@GetMapping` di Controller).

    ```html
    <a href="/product/delete/{id}">Delete</a>
    ```

* **Kenapa ini bahaya?** Menurut standar HTTP, method `GET` itu harusnya cuma buat *read data* aja, bukan buat ngubah atau hapus data. Kalau pakai GET buat delete, aplikasi jadi rentan kena serangan **CSRF (Cross-Site Request Forgery)**. Bisa aja ada orang jahat bikin link jebakan, dan kalau admin ngeklik, produknya kehapus tanpa sadar.

* **Rencana Perbaikan:**
  Nantinya, saya harus ubah mekanisme ini jadi menggunakan method `POST` (atau `DELETE` kalau pakai JS). Di sisi HTML, tombol delete-nya harus dibungkus dalam `<form>`.

    ```html
    <form action="/product/delete/{id}" method="post">
       <button type="submit">Delete</button>
    </form>
    ```


# Refleksi 2

### 1. Tentang Unit Test dan Code Coverage
* **Perasaan setelah menulis Unit Test:** Rasanya jauh lebih tenang dan percaya diri (*confident*). Ketika nanti saya harus mengubah kode atau menambah fitur baru, saya tidak perlu khawatir merusak fitur lama tanpa sadar, karena *unit test* akan langsung mendeteksi (*fail*) jika ada sesuatu yang tidak sesuai ekspektasi.
* **Berapa banyak Unit Test dalam satu class?** Tidak ada angka mutlak. Jumlahnya sangat bergantung pada kompleksitas kelas tersebut. Prinsip utamanya adalah kita harus membuat tes untuk setiap *Positive Case* (kondisi normal), *Negative Case* (kondisi error/salah input), dan *Edge Cases* (kondisi batas).
* **Bagaimana memastikan tes sudah cukup?** Kita bisa menggunakan metrik **Code Coverage** (misalnya menggunakan alat seperti JaCoCo di Java). Metrik ini mengukur seberapa banyak baris kode, percabangan (*branch/if-else*), dan *method* yang berhasil dieksekusi selama *testing* berjalan.
* **Apakah 100% Code Coverage berarti 100% bebas Bug?** **TIDAK**. *Code coverage* 100% hanya membuktikan bahwa setiap baris kode kita telah "dilewati" oleh *test*, tapi metrik ini TIDAK memverifikasi apakah logika atau perhitungan bisnis di dalamnya sudah benar. Bisa saja kita melewatkan skenario logika tertentu yang spesifik, atau ada *bug* yang muncul saat integrasi antar komponen, meskipun baris kodenya sudah ter- *cover*.

### 2. Evaluasi Clean Code pada Functional Test
Jika saya membuat kelas *functional test* baru dan melakukan *copy-paste* prosedur *setup* serta variabel *instance* (seperti `serverPort`, `testBaseUrl`, dan `@BeforeEach setupTest()`) dari `CreateProductFunctionalTest.java`, **itu akan sangat menurunkan kualitas dan kebersihan kode**.

* **Isu Clean Code:** Hal ini secara langsung melanggar prinsip **DRY (Don't Repeat Yourself)**.
* **Alasan:** Terjadi duplikasi kode (*code duplication*). Jika di masa depan ada perubahan pada cara kita melakukan inisialisasi *browser* atau perubahan konfigurasi URL *server*, kita harus mencari dan mengubahnya satu per satu di **semua** file *test class* yang ada. Ini sangat melelahkan dan rawan tertinggal (*error-prone*).
* **Solusi / Cara Memperbaiki:**
  Kita bisa menggunakan konsep **Inheritance (Pewarisan) di OOP**.
  1. Buat sebuah kelas induk (*parent class*), misalnya `BaseFunctionalTest.java`.
  2. Pindahkan semua variabel konfigurasi (`serverPort`, `baseUrl`) dan anotasi *setup* (`@SpringBootTest`, `@BeforeEach`) ke dalam kelas induk tersebut.
  3. Kelas `CreateProductFunctionalTest` dan kelas *test* baru yang akan dibuat cukup melakukan **`extends BaseFunctionalTest`**.
     Dengan cara ini, konfigurasi *setup* hanya ditulis **satu kali**, membuat kode jauh lebih *clean*, modular, dan mudah di-*maintain*.


# Module 02: CI/CD & DevOps

**1. Sebutkan masalah kualitas kode (*code quality issue*) yang Anda perbaiki selama latihan dan jelaskan strategi Anda dalam memperbaikinya.**
Selama pengerjaan, SonarCloud mendeteksi beberapa masalah *maintainability* dan *reliability* (*code smells*) di repositori saya. Berikut adalah masalah yang saya perbaiki beserta strategi penyelesaiannya:
* **Modifier `public` yang tidak perlu pada pengujian JUnit 5:** SonarCloud mendeteksi bahwa kelas dan metode *test* pada JUnit 5 tidak perlu dideklarasikan secara eksplisit sebagai `public`. **Strategi:** Saya membuka file-file *test* (seperti `ProductServiceImplTest` dan `ProductControllerTest`) dan menghapus *modifier* `public` tersebut, membiarkannya menjadi *package-private*. Hal ini sesuai dengan *best practice* dari JUnit 5 dan mengurangi kode *boilerplate* yang tidak perlu.
* **Import atau variabel yang tidak terpakai:** Terdapat beberapa *import* yang tidak digunakan lagi setelah melakukan *refactoring* kode. **Strategi:** Saya menggunakan fitur bawaan IDE (seperti "Optimize Imports" di IntelliJ) dan memeriksa secara manual baris kode yang ditandai oleh SonarCloud untuk menghapus kode yang redundan, sehingga file tetap bersih dan mudah dipelihara.

**2. Lihatlah *workflows* CI/CD Anda. Menurut Anda, apakah implementasi saat ini sudah memenuhi definisi *Continuous Integration* dan *Continuous Deployment*? Jelaskan alasannya!**
Ya, menurut saya implementasi saat ini sudah memenuhi definisi dasar dari *Continuous Integration* (CI) maupun *Continuous Deployment* (CD). Untuk aspek CI, *workflows* telah dikonfigurasi sedemikian rupa sehingga setiap kali ada *push* atau *pull request*, GitHub Actions akan otomatis memicu eksekusi *automated test suites* (menggunakan Gradle dan JaCoCo) dan melakukan analisis kualitas kode statis melalui SonarCloud. Hal ini memastikan bahwa setiap penambahan kode baru selalu diverifikasi kebenaran dan kualitasnya secara terus-menerus sebelum digabungkan ke *branch* utama. Sementara itu, untuk aspek CD, repositori ini sudah dilengkapi dengan `Dockerfile` dan disiapkan untuk menggunakan pendekatan *pull-based deployment*. Artinya, setiap kali ada kode yang lolos uji dan di-*merge* ke *branch* `main`, platform PaaS akan secara otomatis mendeteksi perubahan tersebut, mem-*build* *image* Docker, dan men-*deploy* aplikasi ke tahapan *production* tanpa memerlukan intervensi manual.