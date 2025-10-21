<template>
  <section>
  <div id="count">
    {{ currentTime }} - 6.350 m
    <BButton pill variant="warning" @click="$router.back()">Go back</BButton>
  </div>
  <div id="resultcontainer" ref="container">
    <div id="results" ref="results">
      <div v-for="result in results" class="result">
        #{{ result.id }} - {{result.first_name}} {{result.last_name}}: {{result.lanes * 25}}m
      </div>
    </div>
    <div style="clear:both"/>
  </div>
  </section>
</template>
<script>
import FlipClock from "../components/UI/FlipClock.vue";

const SCROLL_FACTOR = 800;
export default {
  name: 'Results',
  components: {FlipClock},
  data() {
    return {
      count: 0,
      currentTime: new Date().toLocaleTimeString(),
      baseValue: 0,
      results: [
          {
        "id": 1,
        "first_name": "Viviene",
        "last_name": "Ebertz",
        "lanes": 913
      }, {
        "id": 2,
        "first_name": "Jami",
        "last_name": "Maile",
        "lanes": 902
      }, {
        "id": 3,
        "first_name": "Edwina",
        "last_name": "Sangster",
        "lanes": 426
      }, {
        "id": 4,
        "first_name": "Dorie",
        "last_name": "Batters",
        "lanes": 349
      }, {
        "id": 5,
        "first_name": "Ketti",
        "last_name": "Pidgeon",
        "lanes": 51
      }, {
        "id": 6,
        "first_name": "Jada",
        "last_name": "Geoghegan",
        "lanes": 554
      }, {
        "id": 7,
        "first_name": "Shurlock",
        "last_name": "Rodder",
        "lanes": 437
      }, {
        "id": 8,
        "first_name": "Joane",
        "last_name": "Breakwell",
        "lanes": 625
      }, {
        "id": 9,
        "first_name": "Ximenez",
        "last_name": "Mennell",
        "lanes": 668
      }, {
        "id": 10,
        "first_name": "Larina",
        "last_name": "Cordeau",
        "lanes": 542
      }, {
        "id": 11,
        "first_name": "Darryl",
        "last_name": "Raison",
        "lanes": 867
      }, {
        "id": 12,
        "first_name": "Annetta",
        "last_name": "Jervois",
        "lanes": 317
      }, {
        "id": 13,
        "first_name": "Wilmar",
        "last_name": "Longbottom",
        "lanes": 56
      }, {
        "id": 14,
        "first_name": "Jourdan",
        "last_name": "Jaxon",
        "lanes": 46
      }, {
        "id": 15,
        "first_name": "Hubie",
        "last_name": "Commins",
        "lanes": 369
      }, {
        "id": 16,
        "first_name": "Louisette",
        "last_name": "Galton",
        "lanes": 536
      }, {
        "id": 17,
        "first_name": "Gareth",
        "last_name": "Dovey",
        "lanes": 268
      }, {
        "id": 18,
        "first_name": "Jaymee",
        "last_name": "Elliss",
        "lanes": 869
      }, {
        "id": 19,
        "first_name": "Addie",
        "last_name": "Schelle",
        "lanes": 572
      }, {
        "id": 20,
        "first_name": "Malanie",
        "last_name": "Bohlje",
        "lanes": 574
      }, {
        "id": 21,
        "first_name": "Cleavland",
        "last_name": "Gwillym",
        "lanes": 753
      }, {
        "id": 22,
        "first_name": "Jackson",
        "last_name": "Sail",
        "lanes": 451
      }, {
        "id": 23,
        "first_name": "Clarke",
        "last_name": "Gaskill",
        "lanes": 561
      }, {
        "id": 24,
        "first_name": "Wenona",
        "last_name": "Worley",
        "lanes": 767
      }, {
        "id": 25,
        "first_name": "Joline",
        "last_name": "Iacobassi",
        "lanes": 612
      }, {
        "id": 26,
        "first_name": "Jania",
        "last_name": "Petranek",
        "lanes": 412
      }, {
        "id": 27,
        "first_name": "Violette",
        "last_name": "Batiste",
        "lanes": 583
      }, {
        "id": 28,
        "first_name": "Amos",
        "last_name": "Peckett",
        "lanes": 750
      }, {
        "id": 29,
        "first_name": "Monika",
        "last_name": "Morrish",
        "lanes": 635
      }, {
        "id": 30,
        "first_name": "Donni",
        "last_name": "Hebburn",
        "lanes": 100
      }, {
        "id": 31,
        "first_name": "Hastings",
        "last_name": "Kingzet",
        "lanes": 21
      }, {
        "id": 32,
        "first_name": "Colman",
        "last_name": "Elecum",
        "lanes": 878
      }, {
        "id": 33,
        "first_name": "Dunn",
        "last_name": "Mapes",
        "lanes": 760
      }, {
        "id": 34,
        "first_name": "Dall",
        "last_name": "Duggen",
        "lanes": 681
      }, {
        "id": 35,
        "first_name": "Carmencita",
        "last_name": "Jansey",
        "lanes": 650
      }, {
        "id": 36,
        "first_name": "Concordia",
        "last_name": "Turnor",
        "lanes": 782
      }, {
        "id": 37,
        "first_name": "Abie",
        "last_name": "Einchcombe",
        "lanes": 728
      }, {
        "id": 38,
        "first_name": "Kennith",
        "last_name": "Meys",
        "lanes": null
      }, {
        "id": 39,
        "first_name": "Margery",
        "last_name": "Axelby",
        "lanes": 51
      }, {
        "id": 40,
        "first_name": "Weber",
        "last_name": "Finneran",
        "lanes": 462
      }, {
        "id": 41,
        "first_name": "Rollie",
        "last_name": "Dyble",
        "lanes": 275
      }, {
        "id": 42,
        "first_name": "Berkly",
        "last_name": "Garrioch",
        "lanes": 416
      }, {
        "id": 43,
        "first_name": "Hephzibah",
        "last_name": "Sturney",
        "lanes": 831
      }, {
        "id": 44,
        "first_name": "Genvieve",
        "last_name": "Lambarth",
        "lanes": 572
      }, {
        "id": 45,
        "first_name": "Dorothy",
        "last_name": "Dumbellow",
        "lanes": 273
      }, {
        "id": 46,
        "first_name": "Beltran",
        "last_name": "Dumper",
        "lanes": 9
      }, {
        "id": 47,
        "first_name": "Thea",
        "last_name": "Braizier",
        "lanes": 613
      }, {
        "id": 48,
        "first_name": "Guillermo",
        "last_name": "Wogdon",
        "lanes": 189
      }, {
        "id": 49,
        "first_name": "Desmond",
        "last_name": "Beggs",
        "lanes": 752
      }, {
        "id": 50,
        "first_name": "Hynda",
        "last_name": "Wearing",
        "lanes": 139
      }, {
        "id": 51,
        "first_name": "Roseanne",
        "last_name": "Perryman",
        "lanes": 459
      }, {
        "id": 52,
        "first_name": "Olympe",
        "last_name": "Boast",
        "lanes": 614
      }, {
        "id": 53,
        "first_name": "Tabby",
        "last_name": "Fabbro",
        "lanes": 951
      }, {
        "id": 54,
        "first_name": "Averyl",
        "last_name": "Boylan",
        "lanes": 275
      }, {
        "id": 55,
        "first_name": "Anatol",
        "last_name": "Conwell",
        "lanes": 199
      }, {
        "id": 56,
        "first_name": "Nikolai",
        "last_name": "de Castelain",
        "lanes": 494
      }, {
        "id": 57,
        "first_name": "Adriano",
        "last_name": "Pearce",
        "lanes": 335
      }, {
        "id": 58,
        "first_name": "Harp",
        "last_name": "Heymann",
        "lanes": 872
      }, {
        "id": 59,
        "first_name": "Leodora",
        "last_name": "Chawner",
        "lanes": 105
      }, {
        "id": 60,
        "first_name": "Karrah",
        "last_name": "DelaField",
        "lanes": 562
      }, {
        "id": 61,
        "first_name": "Cinderella",
        "last_name": "Drance",
        "lanes": 395
      }, {
        "id": 62,
        "first_name": "Monte",
        "last_name": "Fernando",
        "lanes": 611
      }, {
        "id": 63,
        "first_name": "Miles",
        "last_name": "Struijs",
        "lanes": 24
      }, {
        "id": 64,
        "first_name": "Britteny",
        "last_name": "Lakin",
        "lanes": 58
      }, {
        "id": 65,
        "first_name": "Kellina",
        "last_name": "Dilley",
        "lanes": 103
      }, {
        "id": 66,
        "first_name": "Inigo",
        "last_name": "Granham",
        "lanes": 709
      }, {
        "id": 67,
        "first_name": "Sax",
        "last_name": "Breddy",
        "lanes": 595
      }, {
        "id": 68,
        "first_name": "Rosana",
        "last_name": "Nancekivell",
        "lanes": 970
      }, {
        "id": 69,
        "first_name": "Kara-lynn",
        "last_name": "Heyball",
        "lanes": 544
      }, {
        "id": 70,
        "first_name": "Ryan",
        "last_name": "Bricksey",
        "lanes": 155
      }, {
        "id": 71,
        "first_name": "Penn",
        "last_name": "Toten",
        "lanes": null
      }, {
        "id": 72,
        "first_name": "Truda",
        "last_name": "Castilljo",
        "lanes": 386
      }, {
        "id": 73,
        "first_name": "Kellie",
        "last_name": "Bawme",
        "lanes": 626
      }, {
        "id": 74,
        "first_name": "Jeannie",
        "last_name": "Wiggans",
        "lanes": 221
      }, {
        "id": 75,
        "first_name": "Merle",
        "last_name": "St Angel",
        "lanes": 798
      }, {
        "id": 76,
        "first_name": "Brandy",
        "last_name": "Shimoni",
        "lanes": 842
      }, {
        "id": 77,
        "first_name": "Cherlyn",
        "last_name": "Whooley",
        "lanes": 752
      }, {
        "id": 78,
        "first_name": "Renell",
        "last_name": "Bruhnicke",
        "lanes": 328
      }, {
        "id": 79,
        "first_name": "Derril",
        "last_name": "Guyers",
        "lanes": 101
      }, {
        "id": 80,
        "first_name": "Kelcey",
        "last_name": "Nockalls",
        "lanes": 446
      }, {
        "id": 81,
        "first_name": "Anderea",
        "last_name": "Vine",
        "lanes": 790
      }, {
        "id": 82,
        "first_name": "Ashia",
        "last_name": "Di Ruggero",
        "lanes": 383
      }, {
        "id": 83,
        "first_name": "Sibella",
        "last_name": "Jepps",
        "lanes": 824
      }, {
        "id": 84,
        "first_name": "Sharon",
        "last_name": "Conry",
        "lanes": 962
      }, {
        "id": 85,
        "first_name": "Reynold",
        "last_name": "Whiteoak",
        "lanes": 618
      }, {
        "id": 86,
        "first_name": "Berry",
        "last_name": "Dutt",
        "lanes": 876
      }, {
        "id": 87,
        "first_name": "Stacie",
        "last_name": "Pentony",
        "lanes": 70
      }, {
        "id": 88,
        "first_name": "Aline",
        "last_name": "Luard",
        "lanes": 774
      }, {
        "id": 89,
        "first_name": "Codi",
        "last_name": "Tewnion",
        "lanes": 311
      }, {
        "id": 90,
        "first_name": "Harlene",
        "last_name": "Landy",
        "lanes": null
      }, {
        "id": 91,
        "first_name": "Marietta",
        "last_name": "Rawnsley",
        "lanes": 642
      }, {
        "id": 92,
        "first_name": "Alleen",
        "last_name": "Muncey",
        "lanes": 483
      }, {
        "id": 93,
        "first_name": "Olivier",
        "last_name": "Benyon",
        "lanes": 686
      }, {
        "id": 94,
        "first_name": "Finlay",
        "last_name": "Kirkman",
        "lanes": 925
      }, {
        "id": 95,
        "first_name": "Alessandro",
        "last_name": "Swaine",
        "lanes": 254
      }, {
        "id": 96,
        "first_name": "Ardene",
        "last_name": "Pucknell",
        "lanes": 242
      }, {
        "id": 97,
        "first_name": "Sharleen",
        "last_name": "Scherme",
        "lanes": 90
      }, {
        "id": 98,
        "first_name": "Raddie",
        "last_name": "Shelly",
        "lanes": 534
      }, {
        "id": 99,
        "first_name": "Wini",
        "last_name": "Steer",
        "lanes": 537
      }, {
        "id": 100,
        "first_name": "Whitney",
        "last_name": "Desborough",
        "lanes": 716
      }]
    }
  },


  methods: {
    updateTime() {
      this.currentTime = new Date().toLocaleTimeString();
    },

    animateScroll(target, duration) {
      let start = this.$refs.container.scrollTop;
      let change = target - start;
      let startTime = performance.now();

      let animateScroll = (currentTime) => {
        let timeElapsed = currentTime - startTime;
        let progress = timeElapsed / duration;

        if (progress > 1) {
          progress = 1;
        }

        let easeInOutQuad = progress < 0.5 ? 2 * progress * progress : -1 + (4 - 2 * progress) * progress;
        this.$refs.container.scrollTop = start + change * easeInOutQuad;

        if (progress < 1) {
          requestAnimationFrame(animateScroll);
        }
      };

      requestAnimationFrame(animateScroll);
    },
    doAnimation() {
      // Scrollen Sie nach unten
      let durration = SCROLL_FACTOR * this.results.length;
      this.animateScroll(this.$refs.results.clientHeight, durration, 'linear');

      // Warten Sie einige Sekunden (z.B. 5 Sekunden)
      setTimeout(() => {
        // Scrollen Sie nach oben
        this.animateScroll(0, 100);

        // Warten Sie einige Sekunden (z.B. 5 Sekunden), bevor Sie die Animation erneut ausführen
        setTimeout(() => {
          this.doAnimation();
        }, 60);
      }, durration);
    },

    pollResults() {
      $.ajax({
        url: 'results',
        method: 'POST',
        success: function (e) {
          this.results = e;
        },
        complete: function () {
          setTimeout(this.pollResults, 60000);
        }
      });
    }
  },
  mounted() {
    this.timer = setInterval(this.updateTime, 1000);
    this.doAnimation();
    this.timer2 = setInterval(() => {
      this.baseValue += Math.floor(Math.random() * (500 - 50 + 1)) + 50;
    }, 5000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
    clearInterval(this.timer2);
  }

}
</script>
<style scoped>
html, body {
  background-color: black;
  width: 100%;
  height: 100%;
  margin: 0;
  overflow: hidden;
}

#resultcontainer {
  background-color: black;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  width: auto;
  clientHeight: auto;
  padding-top: 150px;
  overflow-y: scroll;
}

#count {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  text-align: center;
  font-size: 120px;
  color: white;
  font-family: Arial;
  font-weight: bold;
}

#results {
  font-size: 60px;
  color: white;
  font-family: Arial;
}

.result {
  display: inline-block;
  padding-left: 25px;
  min-width: 35em;
}

#count {
  background-color: #bf0f0f;
  z-index: 100;
}
</style>