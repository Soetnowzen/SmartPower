Post Mortem Report - Johan Swetzén
==================================

# Processer och arbetssätt

Processerna och arbetssätten som vi jobbat med är framför allt Scrum, Versionshantering, Git flow och Test-driven utveckling. Man kan väl säga att det det är lite nytt och mycket som inte är direkt nytt men som jag lärt mig mycket av att jobba mer med.

## Scrum

### Fördelar

Med ett agilt arbetssätt (nu vet jag inte riktigt hur scrum skiljer sig från andra sätt så jag använder scrum för att beskriva agila metoder) blir det fokus på att ha en fungerande produkt, man fokuserar nära i tid (en sprint) så målen blir tydliga och man ser snabba resultat vilket driver på motivationen. Jag har aldrig arbetat efter vattenfallsmodellen så jag kan inte riktigt jämföra dem efter erfarenhet men just det att man ska ha en fungerande version efter varje sprint är en sådan skillnad eftersom man kommer igång och har något att visa och testa väldigt snabbt efter att man börjat. Det är också en lättnad att man inte behöver ha allting planerat och veta lösningen på alla problem när man börjar utan tar det som är ett direkt problem *nu*. Då kunde vi fokusera på vad vi ville ha för funktioner först och sedan lägga till saker i backloggen när vi kom på dem.

### Nackdelar

En stor nackdel med Scrum är ju att det inte blir samma sak att släppa en release. Det blir mest en etikett på en version som inte är så långt ifrån den förra, så det är sällan man firar ordentligt. Det blir mindre tårta med Scrum helt enkelt. I vårt projekt ser jag dock inte så många nackdelar. En skulle vara att vi ändrat designen en hel del under tiden vi jobbat, så det har gått mycket tid åt att refaktorera snarare än att utveckla nytt. Om vi beslutat från början hur vi skulle göra det och sedan bara jobbat på hade vi antagligen kommit längre. Samtidigt får man väga det mot att designen nu är mer logisk och intuitiv (efter vår bedömnig).

### Hur effektiv var tekniken?

I vårt fall var just Scrum med dagliga möten inte optimalt effektivt. Vi hade möte varje dag, vilket antagligen var för ofta eftersom vi inte jobbade med projektet varje dag. Vi kunde sparat en halvtimmes möten (tisdag och torsdag) varje vecka per person utan att det påverkat vår kommunikation tror jag.

### I vilka situationer skulle du använda tekniken framöver?

Scrum skulle jag faktiskt använda i alla programmeringsprojekt framöver tills jag hittar någonting annat som är bättre. Men det fungerar väldigt bra i min erfarenhet.

### I vilka situationer skulle du inte använda teknikan framöver?

Jag har svårt att se situationer där det inte skulle vara lämpligt. Ett agtilt sätt att tänka hjälper väldigt mycket för att fokusera på de nuvarande problemen och bygga mjukvara som kan växa och förändras.


## Versionshantering - Git flow

Versonshantering har jag arbetat med länge och sedan några år tillbaka använder jag det för mina egna projekt. Jag tycker att det fungerar så bra att jag rutinmässigt versionshanterar konfigurationsfiler och -mappar på servrar som jag administrerar. Det är så enkelt att ändra, gå tillbaka, etc. speciellt om man inte har konfigurerat ett visst program tidigare.

Jag är mest van vid att använda git efter att ha stött på cvs, svn och mercurial innan jag hittat fram till git. Något som var lite nytt för den här kursen var att vi arbetade med en modell för git som finns [här](http://nvie.com/posts/a-successful-git-branching-model/) och handlar om hur man jobbar med brancher. Det är ett sätt att strukturera arbetet för att enkelt ha koll på släppta versioner, vad som fungerar just nu och samtidigt separera ut i brancher när man arbetar med stora funktioner.

### Fördelar

Fördelar med versionshantering i allmänhet är att man kan arbeta med vad man vill utan att fundera så mycket på om andra jobbar i samma filer. Med Git så löses väldigt mycket av merge-konflikter automatiskt. Just med Git är det bra att det är distribuerat, även om vi inte utnyttjat det så mycket nu när vi haft en centraliserad server. Men att man har hela repot själv på sin dator gör att man känner att man har stor kontroll över versionshanteringen. Bara att man kan göra en commit lokalt är väldigt trevligt jämfört med cvs eller svn. Git flow var också väldigt nyttigt att prova eftersom det också innebar mer kontroll och en bättre överblick, både över vårt projekt men också över hur Git fungerar. Att väldigt medvetet tänka på hur och när man gör en branch, commitar lokalt och sedan gör en merge in till develop var bra. Jag har arbetat på ett liknande sätt på mitt förra sommarjobb, en branch för varje issue man jobbade med, och det knöts också ihop med kodgranskning genom att man namngav sin branch rätt. Problemet då var att jag bara följde mallen som satts upp utan att faktiskt förstå, det en annan i teamet som alltid godkände allt och därmed hanterade merge och konflikter som uppstod. Nu har jag koll på hela kedjan. Hur som helst, fördelar - man får bättre koll och kontroll.

### Nackdelar

Det är svårt att komma på nackdelar med versionshantering och git flow. Versionshantering är väldigt användbart och används över allt. Git flow ger en bra struktur och arbetsprocess till Git, som annars är väldigt fritt och skulle kunna användas ostrukturerat om man ville det. Inga nackdelar låter för bra för att vara sant, men så känns det.

### Hur effektiv var tekniken?

Git är väldigt effektivt när man vet vad man gör. Vet man inte vad man gör så finns det gott om möjligheter att ställa till det för sig. Det är väldigt kraftfullt. Om du skulle fråga hur effektivt det är att programmera för Android skulle svaret vara liknande: det är inte så lätt att vara effektiv i början men när man kommer in i det så ökar effektiviteten. Nu kanske vi la mer tid än vad som var nödvändigt på att hitta information om Git, men i framtida projekt har vi bättre koll på hur man bör göra.

### I vilka situationer skulle du använda tekniken framöver?

Jag kommer nog fortsätta som nu. Om jag har en mapp där jag märker att jag sparar mycket och ofta och tycker att det skulle vara bra att ha en historik kan jag mycket väl versionshantera den. I programmeringsprojekt i allmänhet är versionshantering ett måste.

### I vilka situationer skulle du inte använda tekniken framöver?

Git lämpar sig för sådant som förändras mycket och framförallt då det gäller textfiler av olika slag. Jag skulle inte använda Git tex. om jag skriver i word-dokument eller för en konfigurationsfil som är densamma hela tiden.

## Test-driven utveckling

Vi har hört om konceptet i den här kursen och förstått att det finns en del fördelar, men eftersom det kom lite sent så kan man inte säga att vi har jobbat test-drivet. Vi har skrivit tester men det är inte så många och testerna kom efter att vi gjort funktionaliteten. Idealiskt borde vi haft en process där vi utvecklat tester, programmerat, testat och sedan kört dem vid varje commit tex. Jag tyckte ändå att det var värt att nämna.

### Fördelar

Det verkar som att test-driven utveckling ger stabilare program och mer säkerhet till mig som programmerare efter jag ändrat någonting. När jag modifierat en liten bit i ena hörnet av koden som inte borde påverka någonting annat så vet jag av erfarenhet att den ändå gör det ibland. Har jag då tester på huvudfunktionaliteten av appen så kan jag enkelt testa så att inget går sönder av ändringen. En fördel är att programmet blir mer stabilt och det är väldigt viktigt speciellt när man arbetar med riktigt stora projekt.

### Nackdelar

En sak som alltid känns som en nackdel när man ska skriva tester är att det tar sådan tid! Speciellt när jag skulle göra automatiserade acceptance-tester i Robotium så tog det ju lång tid att sätta sig in i hur det fungerade och svårt att lyckas med det som jag ville. Sedan är det en konst att designa bra tester också så att de verkligen testar det som krävs och hittar edge-fallen.

### Hur effektiv var tekniken?

Eftersom vi inte använde det fullt ut är det svårt att bedöma effektiviteten.

### I vilka situationer skulle du använda tekniken framöver?

När jag arbetar på större projekt och vill ha koll på att allt fungerar korrekt.

### I vilka situationer skulle du inte använda tekniken framöver?

I mindre projekt där jag jobbar själv. Tester kan jag mycket väl skriva, men det har inte en så central roll som i test-driven utveckling.

# Tid på kursen

Jag kan ärligt säga att jag inte har lagt ner så mycket tid på den här kursen som jag skulle vilja. Det började dåligt för mig med att jag var sjuk en och en halv vecka i början och sedan hade jag svårt att komma igång med strulande Eclipse bland annat. Nu i slutet har jag jobbat desto mer istället och det var skönt att känna att jag fick upp lite hastighet i Android-kodandet, eftersom jag aldrig programmerat för Android tidigare.

Tiden som jag har lagt på kursen är minst 70 timmar, men det är lite mer tid som jag inte loggat ordentligt. Av det jag skrivit upp vet jag att det är 50 timmar faktiskt arbete, 5 timmar föreläsning och 13 timmar möten. 

Med andra ord är det ganska mycket tid som gått åt till möten. Vi har haft ett 15 minuters scrum-möte varje vardag som vi allt för sällan kunnat kombinera med att arbeta direkt efteråt

# Vad fungerade bra med arbetssättet?

Med Scrum och pivotal tracker kunde vi dela upp arbetet väldigt bra och vi hade egentligen inte behövt träffas mer än de dagliga scrum-mötena. Det gick lätt att hålla koll på vad alla gjorde och också se vad vi gjort tidigare. Vi kommunicerade med en SMS-grupp, facebook-grupp och skype utöver de fysiska mötetna.

# Vad fungerade mindre bra?

Trots scrum-mötena så kändes det ibland som att kommunikationen brast och vi hade svårt att sätta oss in i varnadras kod. Detta berodde nog på dåligt kommenterad kod i början samt att många av oss var nya med Android. I början hade jag inte en chans att förstå hur en activity fungerade när Andreas, som är väldigt erfaren, skrivit en massa bra kod. Det hjälpte inte ens med kommentarer. Men vi hade lite problem i början också med att Henning satt och arbetade på ett klass för att läsa in data från Bregells kandidatgrupps server samtidigt som Johan (Bregell) gjorde API:t som Henning skulle koppla upp sig mot. Då gjorde Henning en massa arbeta för att hantera den data han trodde han skulle få fast det senare visade sig att Bregell löst problemet genom att skicka ut mer lätthanterlig data från första början! Det problemet kunde antagligen ha lösts genom att de två satt sig ner och haft ett möte, men vi hade i princip inga möten förutom det dagliga scrum-mötet. Det skulle knna bli mycket bättre.

# Hur arbetade vi tillsammans, bra/dåligt?

Som sagt delade vi upp arbetet i pivotal tracker. Alla fick skriva user stories som vi ville ha in. Johan Bregell hade vi utsett till någon form av projektledare så han fick gå igenom och prioritera dem, med viss inrådan från oss andra. Genom att vi hade ett möte varje dag och var tydligt fokuserade kring en uppgift som skulle lösas drog det ihop gruppen. Jag har tagit upp det som en nackdel tidigare att vi träffades varenda dag, men den kontinuiteten tror jag hjälpte till att bygga oss som grupp också. Jag har svår att komma på något riktigt negativt med hur vi jobbade som grupp.

# Vad skulle du ändra på i ett framtida projekt?

Ett av de stora problemen som uppkom berodde på att det API som vi jobbade mot skrevs parallellt med vår app. Det vållade lite problem, tex. det jag skrev om Henning tidigare. Annars fungerade det över lag bra, fast det hade varit nyttigt med ett och annat möte ibland för att bara några personer skulle kunna diskutera ett problem. Som det var nu hade vi inte riktigt med det i tankarna så det blev snarare så att alla pratade om det, vilket var ineffektivt, eller att vi inte pratade alls och det var ju ännu värre!

