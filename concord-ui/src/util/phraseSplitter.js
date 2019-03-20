export class PhraseSplitter {
  constructor(fontSizeAndStyle, pageWidth) {
    this.element = document.createElement('canvas');
    this.context = this.element.getContext('2d');
    this.context.font = fontSizeAndStyle;
    this.pageWidth = pageWidth;
  }

  // hack to wrap text in DataTable (while trying not to break words)
  splitPhrase(phrase) {
    const textWidth = this.context.measureText(phrase).width;
    const availableWidth = window.innerWidth * this.pageWidth;
    const widthRatio = textWidth / availableWidth;
    const maxLineLength = Math.trunc(phrase.length / widthRatio) - 1;

    let words = phrase.split(' ');
    // break any long words up
    let i = 0;
    while (i < words.length) {
      if (words[i].length >= maxLineLength) {
        words.splice(i, 1, this.splitWord(words[i], maxLineLength));
        words = this.flattenArray(words);
      }
      i++;
    }

    let lines = [];
    i = 0;
    while (i < words.length) {
      let line = '';
      while (i < words.length && line.length + words[i].length <= maxLineLength) {
        line += ' ' + words[i];
        i++;
      }
      lines.push(line);
    }
    return lines;
  }

  splitWord = (word, splitLength) => {
    let chunks = [];
    for (let i = 0, charsLength = word.length; i < charsLength; i += splitLength) {
      chunks.push(word.substring(i, i + splitLength));
    }
    return chunks;
  };

  flattenArray = (a) => [].concat.apply([], a);
}
