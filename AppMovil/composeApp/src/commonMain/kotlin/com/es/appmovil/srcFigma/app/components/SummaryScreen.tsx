import { useState } from 'react';

type Period = 'weekly' | 'monthly' | 'yearly';

interface DayData {
  day: string;
  hours: number;
}

export function SummaryScreen() {
  const [selectedPeriod, setSelectedPeriod] = useState<Period>('weekly');

  // Datos de ejemplo
  const weeklyData: DayData[] = [
    { day: 'Lun', hours: 8 },
    { day: 'Mar', hours: 7.5 },
    { day: 'Mié', hours: 8 },
    { day: 'Jue', hours: 8 },
    { day: 'Vie', hours: 6.5 },
    { day: 'Sáb', hours: 0 },
    { day: 'Dom', hours: 0 },
  ];

  const monthlyData: DayData[] = [
    { day: 'Sem 1', hours: 40 },
    { day: 'Sem 2', hours: 38 },
    { day: 'Sem 3', hours: 40 },
    { day: 'Sem 4', hours: 32.5 },
  ];

  const yearlyData: DayData[] = [
    { day: 'Ene', hours: 160 },
    { day: 'Feb', hours: 152 },
    { day: 'Mar', hours: 150.5 },
    { day: 'Abr', hours: 0 },
    { day: 'May', hours: 0 },
    { day: 'Jun', hours: 0 },
    { day: 'Jul', hours: 0 },
    { day: 'Ago', hours: 0 },
    { day: 'Sep', hours: 0 },
    { day: 'Oct', hours: 0 },
    { day: 'Nov', hours: 0 },
    { day: 'Dic', hours: 0 },
  ];

  const getData = () => {
    switch (selectedPeriod) {
      case 'weekly':
        return weeklyData;
      case 'monthly':
        return monthlyData;
      case 'yearly':
        return yearlyData;
    }
  };

  const getTotalHours = () => {
    return getData().reduce((sum, item) => sum + item.hours, 0);
  };

  const getAverageHours = () => {
    const data = getData();
    const nonZeroDays = data.filter(d => d.hours > 0);
    if (nonZeroDays.length === 0) return 0;
    return (nonZeroDays.reduce((sum, item) => sum + item.hours, 0) / nonZeroDays.length).toFixed(1);
  };

  const getMaxValue = () => {
    return Math.max(...getData().map(d => d.hours));
  };

  const getPeriodTitle = () => {
    switch (selectedPeriod) {
      case 'weekly':
        return 'Semana Actual';
      case 'monthly':
        return 'Marzo 2026';
      case 'yearly':
        return 'Año 2026';
    }
  };

  const data = getData();
  const maxValue = getMaxValue();

  return (
    <div className="flex-1 overflow-auto p-6 pb-24">
      <h1 className="mb-6">Resumen</h1>

      {/* Selector de período */}
      <div className="flex gap-2 mb-6 bg-muted p-1 rounded-[--radius-lg]">
        <button
          onClick={() => setSelectedPeriod('weekly')}
          className={`flex-1 py-2 px-4 rounded-[--radius-md] transition-all ${
            selectedPeriod === 'weekly'
              ? 'bg-background shadow-sm'
              : 'text-muted-foreground hover:text-foreground'
          }`}
        >
          Semanal
        </button>
        <button
          onClick={() => setSelectedPeriod('monthly')}
          className={`flex-1 py-2 px-4 rounded-[--radius-md] transition-all ${
            selectedPeriod === 'monthly'
              ? 'bg-background shadow-sm'
              : 'text-muted-foreground hover:text-foreground'
          }`}
        >
          Mensual
        </button>
        <button
          onClick={() => setSelectedPeriod('yearly')}
          className={`flex-1 py-2 px-4 rounded-[--radius-md] transition-all ${
            selectedPeriod === 'yearly'
              ? 'bg-background shadow-sm'
              : 'text-muted-foreground hover:text-foreground'
          }`}
        >
          Anual
        </button>
      </div>

      {/* Título del período */}
      <h3 className="mb-4">{getPeriodTitle()}</h3>

      {/* Estadísticas */}
      <div className="grid grid-cols-2 gap-4 mb-6">
        <div className="bg-card rounded-[--radius-lg] p-4 border border-border">
          <p className="text-muted-foreground text-sm mb-1">Total</p>
          <p className="text-3xl">{getTotalHours()}h</p>
        </div>
        <div className="bg-card rounded-[--radius-lg] p-4 border border-border">
          <p className="text-muted-foreground text-sm mb-1">Promedio</p>
          <p className="text-3xl">{getAverageHours()}h</p>
        </div>
      </div>

      {/* Gráfico de barras simple */}
      <div className="bg-card rounded-[--radius-lg] p-6 border border-border mb-6">
        <h4 className="mb-6">Distribución de Horas</h4>
        <div className="space-y-4">
          {data.map((item, idx) => (
            <div key={idx} className="flex items-center gap-3">
              <div className="w-12 text-sm text-muted-foreground">{item.day}</div>
              <div className="flex-1 bg-muted rounded-full h-8 overflow-hidden relative">
                <div
                  className="bg-primary h-full transition-all flex items-center justify-end pr-3"
                  style={{ width: `${maxValue > 0 ? (item.hours / maxValue) * 100 : 0}%` }}
                >
                  {item.hours > 0 && (
                    <span className="text-sm text-primary-foreground">{item.hours}h</span>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Detalles adicionales */}
      <div className="bg-card rounded-[--radius-lg] p-4 border border-border">
        <h4 className="mb-3">Detalles</h4>
        <div className="space-y-2">
          <div className="flex justify-between py-2 border-b border-border">
            <span className="text-muted-foreground">Días trabajados</span>
            <span>{data.filter(d => d.hours > 0).length}</span>
          </div>
          <div className="flex justify-between py-2 border-b border-border">
            <span className="text-muted-foreground">Día máximo</span>
            <span>{maxValue}h</span>
          </div>
          <div className="flex justify-between py-2">
            <span className="text-muted-foreground">
              {selectedPeriod === 'weekly' ? 'Meta semanal' : selectedPeriod === 'monthly' ? 'Meta mensual' : 'Meta anual'}
            </span>
            <span className="text-muted-foreground">
              {selectedPeriod === 'weekly' ? '40h' : selectedPeriod === 'monthly' ? '160h' : '1920h'}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
