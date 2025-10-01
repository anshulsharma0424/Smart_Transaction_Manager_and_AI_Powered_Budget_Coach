import React from 'react';
import useSuggestionStore from '../../stores/useSuggestionStore';
import { Lightbulb } from 'lucide-react';

const AiSuggestions = () => {
  const suggestions = useSuggestionStore((state) => state.suggestions);
  const loading = useSuggestionStore((state) => state.loading);

  return (
    <div className="bg-white p-6 rounded-lg shadow-sm">
      {/* <h2 className="text-xl font-bold text-gray-800 mb-4">Your AI Budget Coach</h2> */}
      {/* {loading && <p className="text-gray-500">Thinking of some new tips for you...</p>} */}
      
      <div className="space-y-4">
        {suggestions.length === 0 && !loading ? (
          <p className="text-gray-500 text-sm">No suggestions yet. Add some expenses to get your first tip!</p>
        ) : (
          suggestions.slice(0, 5).map((tip) => ( // Show the latest 5 tips
            <div key={tip.id} className="bg-amber-50 border-l-4 border-amber-400 p-4 rounded-r-lg">
              <div className="flex">
                <div className="flex-shrink-0">
                  <Lightbulb className="h-5 w-5 text-amber-500" />
                </div>
                <div className="ml-3">
                  <p className="text-sm text-amber-800">{tip.suggestionContent}</p>
                  <p className="text-xs text-amber-600 mt-1">
                    Based on your expense: '{tip.relatedTransactionDescription}'
                  </p>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default AiSuggestions;