export const exportFileExcel = (file: Blob, name: string) => {
  const link = document.createElement('a')
  const url = URL.createObjectURL(file)
  link.setAttribute('href', url)
  // link.setAttribute('download', `${name}.xlsx`)
  // link.click()
}