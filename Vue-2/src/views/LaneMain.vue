<template>
  <section>
  <b-row align-h="between">
    <b-col cols="12" sm="6">
      <b-button-group class="mb-2">
        <b-button
            v-for="lane in lanes" :id="'laneButton' + lane.id"
            @click="this.$router.push('/lane/' + lane.id)"
            :variant="laneId === lane.id ? 'primary' : 'info'"
            :disabled="laneId === lane.id"
            size="sm">
          {{ 'Bahn ' + lane.id }}
        </b-button>
      </b-button-group>
    </b-col>
    <b-col cols="2">
      <b-button @click="showMessageModal = true">Message</b-button>
    </b-col>
  </b-row>
  <h1> BAHN {{ laneId }}</h1>

  <header>
    <!-- Fixed navbar -->
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
      <a class="navbar-brand" href="#">Bahn {{ Lane }}</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse"
              aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <!--<a id="nav-message" class="nav-link" href="#">Nachricht senden</a>-->
          </li>
        </ul>
        <form class="form-inline mt-2 mt-md-0">
          <!--<input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
          <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>-->
        </form>
      </div>
    </nav>
  </header>


  <!-- Begin page content -->
  <main role="main" class="container-fluid" style="margin-top: 60px">
    <div class="row">
      <div class="col-sm-9">
        <div id="activeSwimmers" class="container-fluid">
          <b-row>
            <b-col>
              <b-button v-b-toggle.collapse-1 variant="primary">Bahn 1</b-button>
              <b-collapse id="collapse-1" class="mt-2">
                <b-card>
                  <b-card-text>
                    <div class="row active swimmer" style="border: 1px solid black;">
                      <div class="col" style="overflow: hidden; white-space: nowrap">
                        <div class="f-edit"
                             style="float: right; width: 75px; height: 50px; margin: 5px; padding-left: 20px; background-image: url(../components/content/pen_blue.png); background-repeat: no-repeat; background-position: center; background-color: red;"></div>
                        <div class="f-attributes"></div>
                        <h2 class="f-name"></h2>
                        <div class="f-info">Nächster Anschlag: <span class="countdown">--:--</span>, Distanz: <span
                            class="distance">-- Bahnen / -- m</span></div>
                      </div>
                    </div>
                  </b-card-text>
                </b-card>
              </b-collapse>
            </b-col>
          </b-row>

          <div class="row activeSwimmer" style="border: 1px solid black;">
            <div class="col" style="overflow: hidden; white-space: nowrap">
              <div class="f-edit"
                   style="float: right; width: 75px; height: 50px; margin: 5px; padding-left: 20px; background-image: url(../components/content/pen_blue.png); background-repeat: no-repeat; background-position: center; background-color: red;"></div>
              <div class="f-attributes"></div>
              <h2 class="f-name"></h2>
              <div class="f-info">Nächster Anschlag: <span class="countdown">--:--</span>, Distanz: <span
                  class="distance">-- Bahnen / -- m</span></div>
            </div>
          </div>
          <div class="row activeSwimmer" style="border: 1px solid black;">
            <div class="col" style="overflow: hidden; white-space: nowrap">
              <div class="f-edit"
                   style="float: right; width: 75px; height: 50px; margin: 5px; padding-left: 20px; background-image: url(../components/content/pen_blue.png); background-repeat: no-repeat; background-position: center; background-color: red;"></div>
              <div class="f-attributes">
                <div
                    style="float: right; width: 50px; height: 50px; margin: 2px; background-image: url(../components/content/Tattoo.png);"></div>
              </div>
              <h2 class="f-name"></h2>
              <div class="f-info">Nächster Anschlag: <span class="countdown">--:--</span>, Distanz: <span
                  class="distance">-- Bahnen / -- m</span></div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-sm-3">
        <div id="inactiveSwimmers" class="container-fluid">
          <div class="row activeSwimmer" style="border: 1px solid black;">
            <div class="col" style="overflow: hidden; white-space: nowrap">
              <div class="f-edit"
                   style="float: right; width: 75px; height: 50px; margin: 5px; padding-left: 20px; background-image: url(../components/content/pen_blue.png); background-repeat: no-repeat; background-position: center; background-color: red;"></div>
              <div class="f-attributes"></div>
              <h2 class="f-name"></h2>
              <div class="f-info">Nächster Anschlag: <span class="countdown">--:--</span>, Distanz: <span
                  class="distance">-- Bahnen / -- m</span></div>
            </div>
          </div>
          '
          <div class="row activeSwimmer" style="border: 1px solid black;">
            <div class="col" style="overflow: hidden; white-space: nowrap">
              <div class="f-edit"
                   style="float: right; width: 75px; height: 50px; margin: 5px; padding-left: 20px; background-image: url(../components/content/pen_blue.png); background-repeat: no-repeat; background-position: center; background-color: red;"></div>
              <div class="f-attributes"></div>
              <h2 class="f-name"></h2>
              <div class="f-info">Nächster Anschlag: <span class="countdown">--:--</span>, Distanz: <span
                  class="distance">-- Bahnen / -- m</span></div>
            </div>
          </div>
          '
        </div>
      </div>
    </div>
  </main>


  <b-button @click="showEditSwimmerModal = true" variant="success" size="lg">xl modal</b-button>

  <b-modal v-model="showEditSwimmerModal" size="xl" title="Bearbeiten" scrollable>
    <b-row>
      <b-col>
        <b-form-group label="Radios using sub-components" v-slot="{ ariaDescribedby }">
          <b-form-radio-group
              id="radio-group-2"
              v-model="selected"
              :aria-describedby="ariaDescribedby"
              name="radio-sub-component"
              buttons
          >
            <b-form-radio value="first">Toggle this custom radio</b-form-radio>
            <b-form-radio value="second">Or toggle this other custom radio</b-form-radio>
            <b-form-radio value="third" disabled>This one is Disabled</b-form-radio>
            <b-form-radio :value="{ fourth: 4 }">This is the 4th radio</b-form-radio>
          </b-form-radio-group>
        </b-form-group>
      </b-col>
    </b-row>
    <p>
      Badebekleidung<br/>
      <input id="ClothingTypeShorts" name="ClothingType" type="radio" value="Shorts"/>
      <label
          for="ClothingTypeShorts"></label>
      <input id="ClothingTypeTrunks" name="ClothingType" type="radio" value="Trunks"/><label
        for="ClothingTypeTrunks"><img src="../components/content/ClothingType/Trunks.jpg" width="50" height="50"/></label>
      <input id="ClothingTypeSwimSuit" name="ClothingType" type="radio" value="SwimSuit"/><label
        for="ClothingTypeSwimSuit"><img src="../components/content/ClothingType/SwimSuit.jpg" width="50" height="50"/></label>
      <input id="ClothingTypeBikini" name="ClothingType" type="radio" value="Bikini"/><label
        for="ClothingTypeBikini"><img src="../components/content/ClothingType/Bikini.jpg" width="50" height="50"/></label>
      <br/>
      <input id="ClothingColorRed" name="ClothingColor" type="radio" value="red"/><label for="ClothingColorRed"><img
        src="../components/content/Color.png" width="50" height="50"
        style="background-color: red; border: 1px dashed black;"/></label>
      <input id="ClothingColorBlue" name="ClothingColor" type="radio" value="blue"/><label
        for="ClothingColorBlue"><img
        src="../components/content/Color.png" width="50" height="50"
        style="background-color: blue; border: 1px dashed black;"/></label>
      <input id="ClothingColorGreen" name="ClothingColor" type="radio" value="green"/><label
        for="ClothingColorGreen"><img src="../components/content/Color.png" width="50" height="50"
                                      style="background-color: green; border: 1px dashed black;"/></label>
      <input id="ClothingColorDarkmagenta" name="ClothingColor" type="radio" value="darkmagenta"/><label
        for="ClothingColorDarkmagenta"><img src="../components/content/Color.png" width="50" height="50"
                                            style="background-color: darkmagenta; border: 1px dashed black;"/></label>
      <input id="ClothingColorChartreuse" name="ClothingColor" type="radio" value="chartreuse"/><label
        for="ClothingColorChartreuse"><img src="../components/content/Color.png" width="50" height="50"
                                           style="background-color: chartreuse; border: 1px dashed black;"/></label>
      <input id="ClothingColorBlack" name="ClothingColor" type="radio" value="black"/><label
        for="ClothingColorBlack"><img src="../components/content/Color.png" width="50" height="50"
                                      style="background-color: black; border: 1px dashed black;"/></label>
      <input id="ClothingColorWhite" name="ClothingColor" type="radio" value="white"/><label
        for="ClothingColorWhite"><img src="../components/content/Color.png" width="50" height="50"
                                      style="background-color: white; border: 1px dashed black;"/></label>
      <input id="ClothingColorOrange" name="ClothingColor" type="radio" value="orange"/><label
        for="ClothingColorOrange"><img src="../components/content/Color.png" width="50" height="50"
                                       style="background-color: orange; border: 1px dashed black;"/></label>
      <input id="ClothingColorPink" name="ClothingColor" type="radio" value="hotpink"/><label
        for="ClothingColorPink"><img src="../components/content/Color.png" width="50" height="50"
                                     style="background-color: hotpink; border: 1px dashed black;"/></label>
    </p>
    <p>
      Schwimmbrille<br/>
      <input id="GooglesColorNone" name="GooglesColor" type="radio" value=""/><label for="GooglesColorNone"><img
        src="../components/content/GooglesNone.png" width="50" height="50" style="background-color: none"/></label>
      <input id="GooglesColorRed" name="GooglesColor" type="radio" value="red"/><label for="GooglesColorRed"><img
        src="../components/content/GooglesColor.png" width="50" height="50"
        style="background-color: red; border: 1px dashed black;"/></label>
      <input id="GooglesColorBlue" name="GooglesColor" type="radio" value="blue"/><label for="GooglesColorBlue"><img
        src="../components/content/GooglesColor.png" width="50" height="50"
        style="background-color: blue; border: 1px dashed black;"/></label>
      <input id="GooglesColorGreen" name="GooglesColor" type="radio" value="green"/><label
        for="GooglesColorGreen"><img
        src="../components/content/GooglesColor.png" width="50" height="50"
        style="background-color: green; border: 1px dashed black;"/></label>
      <input id="GooglesColorDarkmagenta" name="GooglesColor" type="radio" value="darkmagenta"/><label
        for="GooglesColorDarkmagenta"><img src="../components/content/GooglesColor.png" width="50" height="50"
                                           style="background-color: darkmagenta; border: 1px dashed black;"/></label>
      <input id="GooglesColorChartreuse" name="GooglesColor" type="radio" value="chartreuse"/><label
        for="GooglesColorChartreuse"><img src="../components/content/GooglesColor.png" width="50" height="50"
                                          style="background-color: chartreuse; border: 1px dashed black;"/></label>
      <input id="GooglesColorBlack" name="GooglesColor" type="radio" value="black"/><label
        for="GooglesColorBlack"><img
        src="../components/content/GooglesColor.png" width="50" height="50"
        style="background-color: black; border: 1px dashed black;"/></label>
      <input id="GooglesColorWhite" name="GooglesColor" type="radio" value="white"/><label
        for="GooglesColorWhite"><img
        src="../components/content/GooglesColor.png" width="50" height="50"
        style="background-color: white; border: 1px dashed black;"/></label>
      <input id="GooglesColorOrange" name="GooglesColor" type="radio" value="orange"/><label
        for="GooglesColorOrange"><img src="../components/content/GooglesColor.png" width="50" height="50"
                                      style="background-color: orange; border: 1px dashed black;"/></label>
      <input id="GooglesColorPink" name="GooglesColor" type="radio" value="hotpink"/><label
        for="GooglesColorPink"><img
        src="../components/content/GooglesColor.png" width="50" height="50"
        style="background-color: hotpink; border: 1px dashed black;"/></label>
    </p>
    <p>
      Badekappe<br/>
      <input id="HatColorNone" name="HatColor" type="radio" value=""/><label for="HatColorNone"><img
        src="../components/content/HatNone.png" width="50" height="50" style="background-color: none"/></label>
      <input id="HatColorRed" name="HatColor" type="radio" value="red"/><label for="HatColorRed"><img
        src="../components/content/HatColor.png" width="50" height="50"
        style="background-color: red; border: 1px dashed black;"/></label>
      <input id="HatColorBlue" name="HatColor" type="radio" value="blue"/><label for="HatColorBlue"><img
        src="../components/content/HatColor.png" width="50" height="50"
        style="background-color: blue; border: 1px dashed black;"/></label>
      <input id="HatColorGreen" name="HatColor" type="radio" value="green"/><label for="HatColorGreen"><img
        src="../components/content/HatColor.png" width="50" height="50"
        style="background-color: green; border: 1px dashed black;"/></label>
      <input id="HatColorDarkmagenta" name="HatColor" type="radio" value="darkmagenta"/><label
        for="HatColorDarkmagenta"><img src="../components/content/HatColor.png" width="50" height="50"
                                       style="background-color: darkmagenta; border: 1px dashed black;"/></label>
      <input id="HatColorChartreuse" name="HatColor" type="radio" value="chartreuse"/><label
        for="HatColorChartreuse"><img src="../components/content/HatColor.png" width="50" height="50"
                                      style="background-color: chartreuse; border: 1px dashed black;"/></label>
      <input id="HatColorBlack" name="HatColor" type="radio" value="black"/><label for="HatColorBlack"><img
        src="../components/content/HatColor.png" width="50" height="50"
        style="background-color: black; border: 1px dashed black;"/></label>
      <input id="HatColorWhite" name="HatColor" type="radio" value="white"/><label for="HatColorWhite"><img
        src="../components/content/HatColor.png" width="50" height="50"
        style="background-color: white; border: 1px dashed black;"/></label>
      <input id="HatColorOrange" name="HatColor" type="radio" value="orange"/><label for="HatColorOrange"><img
        src="../components/content/HatColor.png" width="50" height="50"
        style="background-color: orange; border: 1px dashed black;"/></label>
      <input id="HatColorPink" name="HatColor" type="radio" value="hotpink"/><label for="HatColorPink"><img
        src="../components/content/HatColor.png" width="50" height="50"
        style="background-color: hotpink; border: 1px dashed black;"/></label>
      <input id="HatSchonungen" name="HatColor" type="radio" value="schonungen"/><label for="HatSchonungen"><img
        src="../components/content/HatSchonungen.png" width="50" height="50" style="border: 1px dashed black;"/></label>
    </p>
    <p>
      Haare<br/>
      <input id="HairColorNone" name="HairColor" type="radio" value=""/><label for="HairColorNone"><img
        src="../components/content/HairNone.png" width="50" height="50" style="background-color: none"/></label>
      <input id="HairColor1" name="HairColor" type="radio" value="sandybrown"/><label for="HairColor1"><img
        src="../components/content/HairColor.png" width="50" height="50"
        style="background-color: sandybrown; border: 1px dashed black;"/></label>
      <input id="HairColor2" name="HairColor" type="radio" value="saddlebrown"/><label for="HairColor2"><img
        src="../components/content/HairColor.png" width="50" height="50"
        style="background-color: saddlebrown; border: 1px dashed black;"/></label>
      <input id="HairColor3" name="HairColor" type="radio" value="gold"/><label for="HairColor3"><img
        src="../components/content/HairColor.png" width="50" height="50"
        style="background-color: gold; border: 1px dashed black;"/></label>
      <input id="HairColor4" name="HairColor" type="radio" value="gainsboro"/><label for="HairColor4"><img
        src="../components/content/HairColor.png" width="50" height="50"
        style="background-color: gainsboro; border: 1px dashed black;"/></label>
      <input id="HairColor5" name="HairColor" type="radio" value="ghostwhite"/><label for="HairColor5"><img
        src="../components/content/HairColor.png" width="50" height="50"
        style="background-color: ghostwhite; border: 1px dashed black;"/></label>
      <input id="HairColorBlack" name="HairColor" type="radio" value="black"/><label for="HairColorBlack"><img
        src="../components/content/HairColor.png" width="50" height="50"
        style="background-color: black; border: 1px dashed black;"/></label>
      <input id="HairColorRed" name="HairColor" type="radio" value="red"/><label for="HairColorRed"><img
        src="../components/content/HairColor.png" width="50" height="50"
        style="background-color: red; border: 1px dashed black;"/></label>
      <input id="HairColorBlue" name="HairColor" type="radio" value="blue"/><label for="HairColorBlue"><img
        src="../components/content/HairColor.png" width="50" height="50"
        style="background-color: blue; border: 1px dashed black;"/></label>
      <input id="HairColorGreen" name="HairColor" type="radio" value="green"/><label for="HairColorGreen"><img
        src="../components/content/HairColor.png" width="50" height="50"
        style="background-color: green; border: 1px dashed black;"/></label>
    </p>
    <p>
      Sonstiges<br/>
      <input id="Tattoo" name="Tattoo" type="checkbox" value="1"/><label for="Tattoo"><img src="../components/content/Tattoo.png"
                                                                                           width="50" height="50"
                                                                                           style="background-color: none"/></label>
      <input id="Headphones" name="Headphones" type="checkbox" value="1"/><label for="Headphones"><img
        src="../components/content/Headphones.png" width="50" height="50" style="background-color: none"/></label>
    </p>
    <p>
      Bemerkung<br/>
      <input type="text" name="Remarks"/>
    </p>
    <b-form-input type="color"></b-form-input>
  </b-modal>

  <b-modal v-model="showMessageModal" title="Nachricht an den Bahnen-Einteiler">
    <div id="dialog2" title="Nachricht senden">
      <p>
        <b-button id="pause" style="width: 100%">Brauche Ablösung (Pipi-Pause)</b-button>
      </p>
      <p>
        <b-button id="question" style="width: 100%">Habe eine Frage (bitte vorbeikommen)</b-button>
      </p>
    </div>
  </b-modal>
  </section>
</template>

<script>
export default {
  name: "LaneSelect",
  data() {
    return {
      laneId: null,
      lanes: [
        {id: 1, name: "Bahn 1"},
        {id: 2, name: "Bahn 2"},
        {id: 3, name: "Bahn 3"},
        {id: 4, name: "Bahn 4"},
        {id: 5, name: "Bahn 5"},
      ],
      showEditSwimmerModal: false,
      showMessageModal: false,
    };
  },
  mounted() {
    this.laneId = this.$route.params.id;
    console.log("MOUNTED", this.laneId)
  },
  beforeUpdate() {
    this.laneId = this.$route.params.id;
    console.log("UPDATED", this.laneId)
  }
}
</script>

<style scoped>

</style>