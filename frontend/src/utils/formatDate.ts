export function formatDate(date: Date): string {
    return date.toISOString().split('T')[0];
}

export function formatDateToList(date: Date): [String, String, String] {
    const year = String(date.getFullYear()); 
    const month = String(date.getMonth() + 1).padStart(2, '0');   
    const day = String(date.getDate()).padStart(2, '0');       
  
    return [year, month, day];
}