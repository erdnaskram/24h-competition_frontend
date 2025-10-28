<template>
  <BButton @click="showEditSwimmerModal = true" variant="success" :size="modalsize">xl modal</BButton>
  <b-modal ref="editSwimmerModal" id="editSwimmerModal" v-model="showEditSwimmerModal" size="xl" title="Bearbeiten"
           scrollable>
    <BRow>
      <BCol class="mb-2">
        <label>Badekleidung</label><br/>
        <BFormRadioGroup id="clothingTypeGroup" name="ClothingType" v-model="selectedClothingType"
                         buttons button-variant="outline-primary">
          <BFormRadio id="ClothingTypeShorts" value="Shorts">
            <label for="ClothingTypeShorts">
              <img src="../../assets/content/ClothingType/Shorts.jpg" width="50" height="50"/>
            </label>
          </BFormRadio>
          <BFormRadio id="ClothingTypeSportshorts" value="Sportshorts">
            <label for="ClothingTypeSportshorts">
              Sport-<br/>shorts
            </label>
          </BFormRadio>
          <BFormRadio id="ClothingTypeTrunks" value="Trunks">
            <label for="ClothingTypeTrunks">
              <img src="../../assets/content/ClothingType/Trunks.jpg" width="50" height="50"/>
            </label>
          </BFormRadio>
          <BFormRadio id="ClothingTypeSwimSuit" value="SwimSuit">
            <label for="ClothingTypeSwimSuit">
              <img src="../../assets/content/ClothingType/SwimSuit.jpg" width="50" height="50"/>
            </label>
          </BFormRadio>
          <BFormRadio id="ClothingTypeBikini" value="Bikini">
            <label for="ClothingTypeBikini">
              <img src="../../assets/content/ClothingType/Bikini.jpg" width="50" height="50"/>
            </label>
          </BFormRadio>
        </BFormRadioGroup>
      </BCol>
      <BCol class="mb-2">
        <label>Farbe</label><br/>
        <div v-if="modalSize !== 'md'">
          <BFormRadioGroup id="clothingColorGroup" name="ClothingColor" v-model="selectedClothingColor"
                           buttons button-variant="outline-primary">
            <BFormRadio
                v-for="clothingColor in clothingColorOptions"
                :key="'Radio' + clothingColor.id"
                :id="clothingColor.id"
                :value="clothingColor.value">
              <label :for="clothingColor.id">
                <img src="../../assets/content/Color.png" width="50" height="50"
                     :style="{ backgroundColor: clothingColor.backgroundColor, border: clothingColor.border}"/>
              </label>
            </BFormRadio>
          </BFormRadioGroup>
        </div>
        <div v-else>
          <BFormRadioGroup id="clothingColorGroup1" name="ClothingColor1" v-model="selectedClothingColor"
                           buttons button-variant="outline-primary" class="mb-1">
            <BFormRadio
                v-for="clothingColor in clothingColorOptions.slice(0, 4)"
                :key="'Radio' + clothingColor.id"
                :id="clothingColor.id"
                :value="clothingColor.value">
              <label :for="clothingColor.id">
                <img src="../../assets/content/Color.png" width="50" height="50"
                     :style="{ backgroundColor: clothingColor.backgroundColor, border: clothingColor.border}"/>
              </label>
            </BFormRadio>
          </BFormRadioGroup>
          <BFormRadioGroup id="clothingColorGroup2" name="ClothingColor2" v-model="selectedClothingColor"
                           buttons button-variant="outline-primary">
            <BFormRadio
                v-for="clothingColor in clothingColorOptions.slice(4, 8)"
                :key="'Radio' + clothingColor.id"
                :id="clothingColor.id"
                :value="clothingColor.value">
              <label :for="clothingColor.id">
                <img src="../../assets/content/Color.png" width="50" height="50"
                     :style="{ backgroundColor: clothingColor.backgroundColor, border: clothingColor.border}"/>
              </label>
            </BFormRadio>
          </BFormRadioGroup>
        </div>
      </BCol>
    </BRow>
    <BRow class="mb-2">
      <BCol>
        <label>Schwimmbrille</label><br/>
        <div v-if="modalSize !== 'md'">
          <BFormRadioGroup id="googlesColorGroup" name="GooglesColor" v-model="selectedGooglesColor"
                           buttons button-variant="outline-primary">
            <BFormRadio id="GooglesColorNone" value="">
              <label for="GooglesColorNone">
                <div style="position: relative; width: 50px; height: 50px;">
                  <img src="../../assets/content/swimmer-characteristics/goggles-bold.svg" width="50" height="50"/>
                  <v-icon
                      style="position: absolute; top: 35%; left: 75%; transform: translate(-50%, -50%);"
                      size="40"
                      color="red"
                  >
                    mdi-close-thick
                  </v-icon>
                </div>
              </label>
            </BFormRadio>
            <BFormRadio
                v-for="googlesColor in googlesColorOptions"
                :key="'Radio' + googlesColor.id"
                :id="googlesColor.id"
                :value="googlesColor.value">
              <label :for="googlesColor.id">
                <GogglesIcon
                    width="50"
                    height="50"
                    :style="{ fill: googlesColor.backgroundColor, border: googlesColor.border }"
                />
              </label>
            </BFormRadio>
          </BFormRadioGroup>
        </div>
        <div v-else>
          <BFormRadioGroup id="googlesColorGroup1" name="GooglesColor1" v-model="selectedGooglesColor"
                           buttons button-variant="outline-primary" class="mb-1">
            <BFormRadio id="GooglesColorNone" value="">
              <label for="GooglesColorNone">
                <img src="../../assets/content/GooglesNone.png" width="50" height="50"/>
              </label>
            </BFormRadio>
            <BFormRadio
                v-for="googlesColor in googlesColorOptions.slice(0, 4)"
                :key="'Radio' + googlesColor.id"
                :id="googlesColor.id"
                :value="googlesColor.value">
              <label :for="googlesColor.id">
                <img src="../../assets/content/GooglesColor.png" width="50" height="50"
                     :style="{ backgroundColor: googlesColor.backgroundColor, border: googlesColor.border}"/>
              </label>
            </BFormRadio>
          </BFormRadioGroup>
          <BFormRadioGroup id="googlesColorGroup2" name="GooglesColor2" v-model="selectedGooglesColor"
                           buttons button-variant="outline-primary">
            <BFormRadio
                v-for="googlesColor in googlesColorOptions.slice(4, 8)"
                :key="'Radio' + googlesColor.id"
                :id="googlesColor.id"
                :value="googlesColor.value">
              <label :for="googlesColor.id">
                <img src="../../assets/content/GooglesColor.png" width="50" height="50"
                     :style="{ backgroundColor: googlesColor.backgroundColor, border: googlesColor.border}"/>
              </label>
            </BFormRadio>
          </BFormRadioGroup>
        </div>
      </BCol>
    </BRow>
    <BRow class="mb-2">
      <BCol>
        <label>Badekappe</label><br/>
        <div v-if="modalSize !== 'md'">
          <BFormRadioGroup id="hatColorGroup" name="HatColor" v-model="selectedHatColor" buttons
                           button-variant="outline-primary">
            <BFormRadio id="HatColorNone" value="">
              <label for="HatColorNone"><img src="../../assets/content/HatNone.png" width="50" height="50"/></label>
            </BFormRadio>
            <BFormRadio
                v-for="hatColor in hatColorOptions"
                :key="'Radio' + hatColor.id"
                :id="hatColor.id"
                :value="hatColor.value">
              <label :for="hatColor.id">
                <img src="../../assets/content/HatColor.png" width="50" height="50"
                     :style="{ backgroundColor: hatColor.backgroundColor, border: hatColor.border}"/>
              </label>
            </BFormRadio>
            <BFormRadio id="HatSchonungen" value="schonungen">
              <label for="HatSchonungen">
                <img src="../../assets/content/HatSchonungen.png" width="50" height="50"/>
              </label>
            </BFormRadio>
          </BFormRadioGroup>
        </div>
        <div v-else>
          <BFormRadioGroup id="hatColorGroup1" name="HatColor1" v-model="selectedHatColor" buttons
                           button-variant="outline-primary" class="mb-1">
            <BFormRadio id="HatColorNone" value="">
              <label for="HatColorNone"><img src="../../assets/content/HatNone.png" width="50" height="50"/></label>
            </BFormRadio>
            <BFormRadio
                v-for="hatColor in hatColorOptions.slice(0, 4)"
                :key="'Radio' + hatColor.id"
                :id="hatColor.id"
                :value="hatColor.value">
              <label :for="hatColor.id">
                <img src="../../assets/content/HatColor.png" width="50" height="50"
                     :style="{ backgroundColor: hatColor.backgroundColor, border: hatColor.border}"/>
              </label>
            </BFormRadio>
          </BFormRadioGroup>
          <BFormRadioGroup id="hatColorGroup2" name="HatColor2" v-model="selectedHatColor" buttons
                           button-variant="outline-primary">
            <BFormRadio
                v-for="hatColor in hatColorOptions.slice(4, 8)"
                :key="'Radio' + hatColor.id"
                :id="hatColor.id"
                :value="hatColor.value">
              <label :for="hatColor.id">
                <img src="../../assets/content/HatColor.png" width="50" height="50"
                     :style="{ backgroundColor: hatColor.backgroundColor, border: hatColor.border}"/>
              </label>
            </BFormRadio>
            <BFormRadio id="HatSchonungen" value="schonungen">
              <label for="HatSchonungen">
                <img src="../../assets/content/HatSchonungen.png" width="50" height="50"/>
              </label>
            </BFormRadio>
          </BFormRadioGroup>
        </div>
      </BCol>
    </BRow>
    <BRow class="mb-2">
      <BCol>
        <label>Haare</label><br/>
        <div v-if="modalSize !== 'md'">
          <BFormRadioGroup id="hairColorGroup" name="HairColor" v-model="selectedHairColor" buttons
                           button-variant="outline-primary">
            <BFormRadio id="HairColorNone" value="">
              <label for="HairColorNone">
                <img src="../../assets/content/HairNone.png"
                     width="50" height="50"
                />
              </label>
            </BFormRadio>
            <BFormRadio
                v-for="hairColor in hairColorOptions"
                :key="'Radio' + hairColor.id"
                :id="hairColor.id"
                :value="hairColor.value">
              <label :for="hairColor.id">
                <img src="../../assets/content/HairColor.png" width="50" height="50"
                     :style="{ backgroundColor: hairColor.backgroundColor, border: hairColor.border}"/>
              </label>
            </BFormRadio>
          </BFormRadioGroup>
        </div>
        <div v-else>
          <BFormRadioGroup id="hairColorGroup1" name="HairColor" v-model="selectedHairColor"
                           buttons button-variant="outline-primary" class="mb-1">
            <BFormRadio id="HairColorNone" value="">
              <label for="HairColorNone">
                <img src="../../assets/content/HairNone.png"
                     width="50" height="50"
                />
              </label>
            </BFormRadio>
            <BFormRadio
                v-for="hairColor in hairColorOptions.slice(0, 4)"
                :key="'Radio' + hairColor.id"
                :id="hairColor.id"
                :value="hairColor.value">
              <label :for="hairColor.id">
                <img src="../../assets/content/HairColor.png" width="50" height="50"
                     :style="{ backgroundColor: hairColor.backgroundColor, border: hairColor.border}"/>
              </label>
            </BFormRadio>
          </BFormRadioGroup>
          <BFormRadioGroup id="hairColorGroup2" name="HairColor" v-model="selectedHairColor"
                           buttons button-variant="outline-primary">
            <BFormRadio
                v-for="hairColor in hairColorOptions.slice(4, 9)"
                :key="'Radio' + hairColor.id"
                :id="hairColor.id"
                :value="hairColor.value">
              <label :for="hairColor.id">
                <img src="../../assets/content/HairColor.png" width="50" height="50"
                     :style="{ backgroundColor: hairColor.backgroundColor, border: hairColor.border}"/>
              </label>
            </BFormRadio>
          </BFormRadioGroup>
        </div>
      </BCol>
    </BRow>
    <BRow class="mb-2">
      <BCol lg="2" cols="3">
        <label>Tattoo</label><br/>
        <BFormCheckbox id="Tattoo" value="Tatoo" button button-variant="outline-primary">
          <label for="Tattoo">
            <img src="../../assets/content/Tattoo.png" width="50" height="50"/>
          </label>
        </BFormCheckbox>
      </BCol>
      <BCol lg="2" cols="3">
        <label>Kopfhörer</label><br/>
        <BFormCheckbox id="Headphones" value="Headphones" button button-variant="outline-primary">
          <label for="Headphones">
            <EarPodIcon width="40" height="40" style="margin: 5px;"/>
          </label>
        </BFormCheckbox>
      </BCol>
      <BCol lg="6" cols="12">
        <label>Bemerkung</label><br/>
        <BFormInput size="lg"></BFormInput>
      </BCol>
    </BRow>
  </b-modal>
</template>

<script>
import GogglesIcon from '../../assets/content/swimmer-characteristics/goggles-bold.svg';
import EarPodIcon from '../../assets/content/swimmer-characteristics/earpods-solid.svg';

export default {
  name: "EditSwimmerModal",
  components: {
    GogglesIcon,
    EarPodIcon,
  },
  data() {
    return {
      showEditSwimmerModal: false,
      selectedClothingType: "",
      clothingColorOptions: [
        {id: "ClothingColorRed", value: "red", backgroundColor: "red", border: "none"},
        {id: "ClothingColorBlue", value: "blue", backgroundColor: "blue", border: "none"},
        {id: "ClothingColorGreen", value: "green", backgroundColor: "green", border: "none"},
        {id: "ClothingColorPurple", value: "purple", backgroundColor: "darkmagenta", border: "none"},
        {id: "ClothingColorBlack", value: "black", backgroundColor: "black", border: "none"},
        {id: "ClothingColorWhite", value: "white", backgroundColor: "white", border: "1px dashed black"},
        {id: "ClothingColorOrange", value: "orange", backgroundColor: "orange", border: "none"},
        {id: "ClothingColorPink", value: "pink", backgroundColor: "hotpink", border: "none"},
      ],
      selectedClothingColor: "",
      googlesColorOptions: [
        {id: "GooglesColorRed", value: "red", backgroundColor: "red", border: "none"},
        {id: "GooglesColorBlue", value: "blue", backgroundColor: "blue", border: "none"},
        {id: "GooglesColorGreen", value: "green", backgroundColor: "green", border: "none"},
        {id: "GooglesColorpurple", value: "purple", backgroundColor: "darkmagenta", border: "none"},
        {id: "GooglesColorBlack", value: "black", backgroundColor: "black", border: "none"},
        {id: "GooglesColorWhite", value: "white", backgroundColor: "lightgray", border: "none"},
        {id: "GooglesColorOrange", value: "orange", backgroundColor: "orange", border: "none"},
        {id: "GooglesColorPink", value: "pink", backgroundColor: "hotpink", border: "none"},
      ],
      selectedGooglesColor: "",
      hatColorOptions: [
        {id: "HatColorRed", value: "red", backgroundColor: "red", border: "none"},
        {id: "HatColorBlue", value: "blue", backgroundColor: "blue", border: "none"},
        {id: "HatColorGreen", value: "green", backgroundColor: "green", border: "none"},
        {id: "HatColorPurple", value: "purple", backgroundColor: "darkmagenta", border: "none"},
        {id: "HatColorBlack", value: "black", backgroundColor: "black", border: "none"},
        {id: "HatColorWhite", value: "white", backgroundColor: "white", border: "none"},
        {id: "HatColorOrange", value: "orange", backgroundColor: "orange", border: "none"},
        {id: "HatColorPink", value: "pink", backgroundColor: "hotpink", border: "none"},
      ],
      selectedHatColor: "",
      hairColorOptions: [
        {id: "HairColor1", value: "sandybrown", backgroundColor: "sandybrown", border: "none"},
        {id: "HairColor2", value: "saddlebrown", backgroundColor: "saddlebrown", border: "none"},
        {id: "HairColor3", value: "gold", backgroundColor: "gold", border: "none"},
        {id: "HairColor4", value: "gainsboro", backgroundColor: "gainsboro", border: "none"},
        {id: "HairColor5", value: "ghostwhite", backgroundColor: "ghostwhite", border: "none"},
        {id: "HairColorBlack", value: "black", backgroundColor: "black", border: "none"},
        {id: "HairColorRed", value: "red", backgroundColor: "red", border: "none"},
        {id: "HairColorBlue", value: "blue", backgroundColor: "blue", border: "none"},
        {id: "HairColorGreen", value: "green", backgroundColor: "green", border: "none"},
      ],
      selectedHairColor: "",
      modalWidth: 0,
      windowWidth: 0,
    };
  },
  mounted() {
    window.addEventListener('resize', this.updateWidth);
    this.updateWidth();
  },

  beforeDestroy() {
    window.removeEventListener('resize', this.updateWidth);
  },
  methods: {
    updateWidth() {
      this.windowWidth = window.innerWidth;
    }
  },
  computed: {
    windwowSize() {
      if (this.windowWidth >= 1200) {
        return 'xl';
      } else if (this.windowWidth >= 992) {
        return 'lg';
      } else if (this.windowWidth >= 768) {
        return 'md';
      } else if (this.windowWidth >= 576) {
        return 'sm';
      } else {
        return 'xs';
      }
    },
    modalSize() {
      switch (this.windwowSize) {
        case 'xl':
          return 'xl';
        case 'lg':
          return 'lg';
        case 'md':
        case 'sm':
        case 'xs':
          return 'md';
      }
    }

  },
}
</script>

<style scoped>
:root {
  --breakpoint-xs: 0;
  --breakpoint-sm: 576px;
  --breakpoint-md: 768px;
  --breakpoint-lg: 992px;
  --breakpoint-xl: 1200px;
}

/* Füge Klassen für jeden Breakpoint hinzu */
@media (min-width: 576px) {
  body::before {
    content: "sm";
    display: none;
    color: darkgray;
  }
}

@media (min-width: 768px) {
  body::before {
    content: "md";
    display: none;
  }
}

@media (min-width: 992px) {
  body::before {
    content: "lg";
    display: none;
  }
}

@media (min-width: 1200px) {
  body::before {
    content: "xl";
    display: none;
  }
}
</style>