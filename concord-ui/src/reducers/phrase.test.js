import reducer from './phrase';

describe('phrase reducer', () => {
  it('should return the initial state', () => {
    expect(reducer(undefined, {})).toEqual({
      loading: true,
      error: false
    });
  });

  it('should handle call to get next phrase', () => {
    expect(
      reducer([], {
        type: 'CALL_GET_NEXT_PHRASE'
      })
    ).toEqual({
      loading: true,
      error: false
    });
  });

  it('should handle successful call to get next phrase', () => {
    const testPhraseData = {
      phrase: 'Hello madam, how are you?',
      id: 123
    };
    expect(
      reducer([], {
        type: 'CALL_GET_NEXT_PHRASE_SUCCEEDED',
        phraseData: testPhraseData
      })
    ).toEqual({
      loading: false,
      error: false,
      phraseData: testPhraseData
    });
  });

  it('should handle unsuccessful call to get next phrase', () => {
    expect(
      reducer([], {
        type: 'CALL_GET_NEXT_PHRASE_FAILED'
      })
    ).toEqual({
      loading: false,
      error: true
    });
  });

  it('should handle vote for phrase label', () => {
    expect(
      reducer([], {
        type: 'CALL_VOTE_FOR_PHRASE_LABEL'
      })
    ).toEqual({
      loading: true,
      error: false
    });
  });

  it('should handle unsuccessful vote for phrase label', () => {
    expect(
      reducer([], {
        type: 'CALL_VOTE_FOR_PHRASE_LABEL_FAILED'
      })
    ).toEqual({
      loading: false,
      error: true
    });
  });
});
