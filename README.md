# Plugin ukazujące podstawowe statystyki dot. plików w kodzie źródłowym projektu.
## Zadanie dodatkowe na przedmiot **"Zaawansowane zagadnienia programowania w języku Java"**, realizowany na 6-tym semestrze studiów  na kierunku informatyka stosowana na Politechnice Łódzkiej.

## Autorzy:
- Jędrzej Bartoszewski 251482
- Kacper Maziarz 251586

<!-- Plugin description -->


### Opis
Jest to prosta wtyczka do IntelliJ IDEA  oferująca statystyki ukazane w formie wykresów słupkowych dotyczące pików w projekcie,
na ten moment:
- wszystkich linii w plikach o danym rozszerzeniu,
- linii kodu w plikach o danym rozszerzeniu
- ilości komentarzy
- ilości pustych linii
- ilości plików o danych typach.

Film prezentujący ww. działanie jest zawarty w głównym folderze projektu pod nazwą "Film_prezentujący_działanie_Pluginu.mp4".

<!-- Plugin description end -->
### Jak zainstalować?

Należy z pomocą gradle zbudować wersję, so-called "dystrybucyjną", można to zrobić poprzez interfejs graficzny IDE:
1. Otworzyć panel Gradle (ikonka "słonika" po prawej stronie widoku)
2. Wybrać kolejno sekcje: IntelliJ Plugin with Compose -> Tasks -> Intellij platform -> buildPlugin
3. Zbudowana wtyczka znajdzie się w katalogu ./build/distributions jako plik `.zip`.
W celu dodania wtyczki do ide należy w ustawieniach przejść do sekcji "Plugins", następnie kliknąć ikonę koła zębategop koło zakłądki 
"Installed", wybrać opcję "Install Plugin from Disk", zaakceptować, po jakimś czasie wtyczka powinna być wczytana


#### Ważne uwagi
Domyślnie IntelliJ zapisuje pliki po utracie tzw. focusa, tzn. trzeba przełączyć na inne okno po modyfikacji pliku podczas testowania plugina,
aby IDE automatycznie zapisywało pliki.
