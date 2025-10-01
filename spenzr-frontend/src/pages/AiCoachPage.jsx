import React, { useEffect } from 'react';
import useSuggestionStore from '../stores/useSuggestionStore';
import { Lightbulb } from 'lucide-react';

const AiCoachPage = () => {
  const { suggestions, fetchSuggestions, loading, error } = useSuggestionStore();

  useEffect(() => {
    fetchSuggestions();
  }, [fetchSuggestions]);

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-800 mb-8">Your AI Coach</h1>
      {loading && <p>Loading your suggestions...</p>}
      {error && <div className="bg-red-100 text-red-700 p-4 rounded-md">{error}</div>}
      <div className="space-y-4">
        {suggestions.map(tip => (
          <div key={tip.id} className="bg-[#f9f9f9] p-4 rounded-lg shadow-sm border border-gray-200">
            <div className="flex">
              <div className="flex-shrink-0 pt-1">
                <Lightbulb className="h-5 w-5 text-amber-500" />
              </div>
              <div className="ml-3 flex-1">
                <p className="text-sm text-gray-800">{tip.suggestionContent}</p>
                <p className="text-xs text-gray-500 mt-2">
                  From your expense on '{tip.relatedTransactionDescription}' on {new Date(tip.createdAt).toLocaleDateString()}
                </p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AiCoachPage;