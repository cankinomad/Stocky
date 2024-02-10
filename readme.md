# STOCKY

## Stocky Kurulum
    
### Docker ile Kurulum

Sisteminizde Docker yoksa öncelikle Docker Desktop kurunuz ve çalıştırınız. [Docker Desktop](https://desktop.docker.com/win/main/amd64/Docker%20Desktop%20Installer.exe?utm_source=docker&utm_medium=webreferral&utm_campaign=dd-smartbutton&utm_location=module)
CMD üzerinden StokYonetımApplication konumuna ulaşın. 
StokYonetimiApplication klasorü içerisinde olduğunuzdan emin olduktan sonra `docker compose up` komutu ile projeyi çalıştırınız.
`docker compose up` komutu ardından Docker Desktop/Container arayüzünden oluşan compose içindeki uygulamaların ayakta olduğundan emin olunuz.
Tüm servisler çalışıyorsa [buraya](http://localhost:8080) tıklayarak veya compose içindeki stocky-frontend-1 üzerindeki port kısmına tıklayarak giriş sayfasını açabilirsiniz.

### Web Üzerinden Erişim

Proje Kubernetes üzerinden canlıya alındığından [buraya](http://34.76.152.9) tıklayarak hazır veritabanı ile görüntüleyebilirsiniz.

**NOT:** Canlıya alınırken bütçe sorunu sebebiyle düşük performanslı bir makine seçilmiştir. Bu nedenle bağlantıya tıkladığınızda sistemsel sorunlar nedeniyle servislere atılan
ilk isteklerde yanıtlar geç gelmekte veya arka planda işlenmesine rağmen ön yüzde bildirim alınamamakta. İlk istekten sonra sayfayı yenileyip yeni bir istek atarsanız o andan 
itibaren istenildiği gibi çalışmakta.

### Github Üzerinden Local Kurulum

Github üzerinden indirdiğiniz projede config-server>src>main>resources>config-repo içerisindeki yml dosyalarında
jwt.issuer: basarsoft
jwt.secretkey: java9_secretkey
jwt.audience: auth_service_tekrar
servislerin resources>.yml uzantılı dosyalarındaki config_url: http://localhost:8888 
şeklinde güncellenmeli. 




## Stocky Uygulama İçeriği

#### Giriş

**KAYIT OL** butonuna basarak kayıt ol sayfasına yönlendirilirsiniz. Kayıt olma işlemi  başarılı olması durumunda giriş sayfasına otomatik olarak
yönlendirilirsiniz.

#### Ana Sayfa

Giriş yaptıktan sonra ürünlerin listelendiği sayfada **Tümü** butonuyla tüm kategorilere ait ürünler listenir. **Diğer** butonu ile kategorisi silinmiş ve henüz
kategori atanmamış ürünleri listelersiniz.
Alt kategorisi olan kategorilere tıklandığında varsa önce kategoriye ait ürünleri getirilir ve Alt kategori başlığı altında listesi gösterilir.
Alt kategori listesinden bir kategoriye tıkladığınızda ise liste bu kategoriye göre güncellenir. Bu sistem yapısı sayesinde istediğiniz kadar
zincir yapı kurabilirsiniz.

### Ürün İşlemleri

#### Ürün Ekle

Ürün işlemleri sayfasında **Ürün Ekle** butonuna basarak ürün ekleme sayfasına yönlendirilirsiniz.Ürün eklerken mevcut kategori listesinde ana kategoriler
gösterilmez. Herhangi bir alt kategoriye ürününüzü ekleyebilirsiniz. Ürün ekleme başarılı olması durumunda ürün işlemleri sayfasına yönlendirilirsiniz.

#### Ürün Düzenle

Ürün işlemleri sayfasında **🖊️** butonuna basarak düzenle sayfasına yönlendirilirsiniz. Düzenle sayfasında ürün ismi, kategorisi, birimi, deposu 
ve adeti gibi bilgileri güncelleyebilirsiniz. Güncelleme başarılı olması durumunda  ürün işlemleri sayfasına yönlendirilirsiniz.

#### Ürün Sil

Ürün işlemleri sayfasında **🗑️** butonuna basarak silme işlemine başlayabilirsiniz. Açılan pencereyi onaylamanız durumunda ürün kalıcı olarak silinir.

### Kategori İşlemleri

#### Kategori Ekle

Kategori işlemleri sayfasında **Kategori Ekle** butonuna basarak kategori ekleme sayfasına yönlendirilirsiniz. **Ana Kategori Olsun Mu?** butonuna basarak 
kategori durumunu belirleyebilirsiniz. Alt kategoriler listesinden istediğiz alt kategoriyi ekleyebilirsiniz. Eklenen alt kategori yeni bir liste halinde
**Eklenen Alt kategoriler** başlığı altında listelenir. Ekleme işlemi başarılı ise Kategori İşlemleri sayfasına yönlendirilirsiniz.

#### Kategori Düzenle

Kategori ekle sayfasındaki tüm özellikler bu sayfa için geçerlidir. Ancak, bir alt kategorinin ana kategori olarak güncellenmesi önerilmemektedir,
çünkü genellikle alt kategorilere ürünler eklenir. Bununla birlikte, kullanıcıların isteğine göre düzenleme yapmalarını sağlamak adına esnek bir yapı 
oluşturulmuş olup, bu durumda özel kısıtlamalar eklenmemiştir

#### Kategori Sil

Kategori işlemleri sayfasında **🗑️** butonuna basarak silme işlemine başlayabilirsiniz. Açılan pencereyi onaylamanız durumunda kategori kalıcı olarak silinir.

### Birim İşlemleri

#### Birim Ekle

Birim işlemleri sayfasında **Birim Ekle** butonuna basarak birim ekleme sayfasına yönlendirilirsiniz.Dinamik bir birim yapısı olması için kullanıcılara birim
ekleme seçeneği sunulmuştur. Ekleme işlemi başarılı olması durumunda **Tüm Birimler** sayfasına yönlendirilirsiniz.

#### Birim Düzenle

Birim işlemleri sayfasında **🖊️** butonuna basarak düzenle sayfasına yönlendirilirsiniz.Mevcut birim ismini değiştirerek güncelleme işlemini yapabilirsiniz.
Düzenleme işlemi başarılı olması durumunda **Tüm Birimler** sayfasına yönlendiriilirsiniz.

#### Birim Sil

Birim işlemleri sayfasında **🗑️** butonuna basarak silme işlemine başlayabilirsiniz. Açılan pencereyi onaylamanız durumunda birim kalıcı olarak silinir.

### Depo İşlemleri

#### Depo Ekle

Tüm Depolar sayfasındaki **Depo Ekle** butonuna basarak depo ekleme sayfasina yönlendirilirsiniz. Ekleme işlemi başarılı olması durumunda **Tüm Depolar** 
sayfasına yönlendirilirsiniz.

#### Depo Düzenle

Depo işlemleri sayfasında **🖊️** butonuna basarak düzenle sayfasına yönlendirilirsiniz. Mevcut depo ismini değiştirerek güncelleme işlemini yapabilirsiniz.
Düzenleme işlemi başarılı olması durumunda **Tüm Depolar** sayfasına yönlendiriilirsiniz.

#### Depo Sil

Depo işlemleri sayfasında **🗑️**  butonuna basarak silme işlemine başlayabilirsiniz. Açılan pencereyi onaylamanız durumunda depo kalıcı olarak silinir.

### Profil

Bu sayfada profil bilgilerinizi güncelleyebilirsiniz. Şifremi değiştir butonuna tıklayarak Şifre güncelle sayfasına yönlendirilirsiniz. Mevcut şifrenizi
ve yeni şifrenizi girerek güncelleme işlemini yapabilirsiniz.
