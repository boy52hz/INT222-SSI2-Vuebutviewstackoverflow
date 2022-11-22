import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './assets/main.scss'
import App from './App.vue'
import router from './router'
import moment from 'moment'

import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'

import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {
  faCalendarAlt,
  faList,
  faUsers,
  faClock,
  faXmark,
  faPaperclip,
  faPlus,
  faUpload,
} from '@fortawesome/free-solid-svg-icons'
import { faMicrosoft } from '@fortawesome/free-brands-svg-icons'

library.add(faCalendarAlt, faList, faUsers, faClock, faMicrosoft, faXmark, faPaperclip, faPlus, faUpload)

const app = createApp(App)
app.use(Toast, {
  transition: 'Vue-Toastification__bounce',
  maxToasts: 20,
  position: 'bottom-right',
})
app.component('font-awesome-icon', FontAwesomeIcon)
app.use(createPinia())
app.use(router)

app.config.globalProperties.$getFormattedEventPeriod = (time, duration) => {
  const startTime = moment(time)
  const finishTime = startTime.clone().add(duration, 'minutes')
  return `${startTime.format('LT')} - ${finishTime.format('LT')}`
}

app.mount('#app')
