import { useState } from 'react';

export function HomeScreen() {
  const [todayHours, setTodayHours] = useState(6.5);
  const [weekHours, setWeekHours] = useState(32.5);

  // Horas acumuladas y objetivo anual
  const currentHours = 462.5; // Horas trabajadas hasta ahora
  const targetHours = 1792; // Objetivo anual (224 días laborables × 8 horas)
  const progress = (currentHours / targetHours) * 100; // Porcentaje de progreso

  // Calcular el color según el progreso en el termómetro
  const getProgressColor = () => {
    if (progress < 50) {
      // Rojo a amarillo (0% - 50%)
      const ratio = progress / 50;
      return `rgb(${239}, ${Math.round(68 + (158 - 68) * ratio)}, ${Math.round(68 + (11 - 68) * ratio)})`;
    } else {
      // Amarillo a verde (50% - 100%)
      const ratio = (progress - 50) / 50;
      return `rgb(${Math.round(245 - (245 - 34) * ratio)}, ${Math.round(158 + (197 - 158) * ratio)}, ${Math.round(11 + (94 - 11) * ratio)})`;
    }
  };

  const handleRegisterHours = () => {
    alert('Abrir formulario de registro de jornada');
  };

  const formatDate = () => {
    return new Date().toLocaleDateString('es-ES', { weekday: 'long', day: 'numeric', month: 'long' });
  };

  return (
    <div className="flex-1 overflow-auto p-6 pb-24">
      {/* Fecha */}
      <div className="mb-8">
        <p className="text-muted-foreground capitalize">{formatDate()}</p>
        <h1 className="mt-2">Seguimiento de Horas</h1>
      </div>

      {/* Termómetro de progreso anual */}
      <div className="mb-8 bg-card rounded-[--radius-lg] p-6 border border-border">
        <h3 className="mb-6">Progreso Anual</h3>

        <div className="text-center mb-8">
          <div className="text-5xl mb-2" style={{ color: getProgressColor() }}>
            {currentHours}/{targetHours}
          </div>
          <p className="text-muted-foreground">Horas trabajadas este año</p>
        </div>

        {/* Barra termómetro */}
        <div className="relative mb-8">
          <div className="h-12 rounded-full overflow-hidden relative"
               style={{
                 background: 'linear-gradient(to right, #ef4444 0%, #f59e0b 50%, #22c55e 100%)'
               }}>
            <div className="absolute inset-0 bg-foreground/80"
                 style={{
                   clipPath: `inset(0 0 0 ${progress}%)`,
                   transition: 'clip-path 0.5s ease'
                 }}>
            </div>
          </div>

          {/* Punto indicador */}
          <div
            className="absolute top-1/2 -translate-y-1/2 -translate-x-1/2 w-6 h-6 bg-white border-4 border-primary rounded-full shadow-lg transition-all duration-500"
            style={{ left: `${Math.min(progress, 100)}%` }}
          >
          </div>

          {/* Indicadores de porcentaje */}
          <div className="flex justify-between mt-2 text-xs text-muted-foreground">
            <span>0%</span>
            <span>50%</span>
            <span>100%</span>
          </div>
        </div>

        <button
          onClick={handleRegisterHours}
          className="w-full py-4 bg-primary text-primary-foreground rounded-[--radius-lg] hover:bg-primary/90 transition-colors"
        >
          Registrar Jornada
        </button>
      </div>

      {/* Resumen del día */}
      <div className="space-y-4">
        <h3>Resumen de Hoy</h3>

        <div className="bg-card rounded-[--radius-lg] p-4 border border-border">
          <div className="flex justify-between items-center mb-2">
            <span className="text-muted-foreground">Horas Hoy</span>
            <span className="text-2xl">{todayHours}h</span>
          </div>
          <div className="w-full bg-muted rounded-full h-2 overflow-hidden">
            <div
              className="bg-primary h-full transition-all"
              style={{ width: `${(todayHours / 8) * 100}%` }}
            />
          </div>
          <p className="text-muted-foreground text-sm mt-2">Meta: 8h</p>
        </div>

        <div className="bg-card rounded-[--radius-lg] p-4 border border-border">
          <div className="flex justify-between items-center mb-2">
            <span className="text-muted-foreground">Horas esta Semana</span>
            <span className="text-2xl">{weekHours}h</span>
          </div>
          <div className="w-full bg-muted rounded-full h-2 overflow-hidden">
            <div
              className="bg-primary h-full transition-all"
              style={{ width: `${(weekHours / 40) * 100}%` }}
            />
          </div>
          <p className="text-muted-foreground text-sm mt-2">Meta: 40h</p>
        </div>

        {/* Registros recientes */}
        <div className="mt-6">
          <h4 className="mb-3">Registros Recientes</h4>
          <div className="space-y-2">
            {[
              { date: 'Hoy', hours: 6.5, period: '09:00 - 17:30' },
              { date: 'Ayer', hours: 8, period: '09:00 - 18:00' },
              { date: '23 Mar', hours: 7.5, period: '09:00 - 17:30' },
            ].map((record, idx) => (
              <div key={idx} className="bg-card rounded-[--radius-lg] p-4 border border-border flex justify-between items-center">
                <div>
                  <p>{record.date}</p>
                  <p className="text-muted-foreground text-sm">{record.period}</p>
                </div>
                <span>{record.hours}h</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
