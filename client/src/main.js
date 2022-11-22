import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './assets/main.scss'
import App from './App.vue'
import router from './router'
import moment from 'moment'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import './font-awesomes'
import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'

export const app = createApp(App)

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
