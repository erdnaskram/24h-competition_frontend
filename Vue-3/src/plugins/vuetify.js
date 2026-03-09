import 'vuetify/styles';
import { createVuetify } from 'vuetify'
import { aliases, mdi } from 'vuetify/iconsets/mdi'
import colors from 'vuetify/util/colors'

export const vuetify = createVuetify({
    theme: {
        defaultTheme: 'light',
        options: {
            customProperties: true,
        },
        themes: {
            light: {
                dark: false,
                colors: {
                    charsRed: colors.red.accent4,
                    charsBlue: colors.blue.accent4,
                    charsGreen: colors.green.darken1,
                    charsPurple: colors.purple.accent4,
                    charsBlack: colors.shades.black,
                    charsWhite: colors.shades.white,
                    charsYellow: colors.amber.base,
                    charsPink: colors.pink.accent1,
                    charsBrown: colors.brown.darken1,
                    charsOrange: colors.orange.accent4,
                    charsGrey: colors.grey.lighten1,
                    charsLightGrey: colors.grey.lighten4,
                },
            },
        }
    },
    icons: {
        defaultSet: 'mdi',
        aliases,
        sets: {
            mdi,
        },
    },
})
