export const formUtils = {}

/***
 * Return true if some value in editable data object changed from data template object
 */
formUtils.isEditableDataChanged = (dataTemplate, editData) => {
  let changed = false
  for (let [key, value] of Object.entries(editData)) {
    if (value !== dataTemplate[key]) {
      changed = true
    }
  }
  return changed
}
