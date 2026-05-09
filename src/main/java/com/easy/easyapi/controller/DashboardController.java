package com.easy.easyapi.controller;

import com.easy.easyapi.model.Despesa;
import com.easy.easyapi.model.Receita;
import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.service.DespesaService;
import com.easy.easyapi.service.ReceitaService;
import com.easy.easyapi.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final ReceitaService receitaService;
    private final DespesaService despesaService;
    private final UsuarioService usuarioService;

    public DashboardController(ReceitaService receitaService, DespesaService despesaService, UsuarioService usuarioService) {
        this.receitaService = receitaService;
        this.despesaService = despesaService;
        this.usuarioService = usuarioService;
    }

    private boolean isUsuarioAutenticadoOwner(Long usuarioId) {
        Usuario autenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return autenticado.getId().equals(usuarioId);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> stats(@RequestParam Long userId) {
        if (!isUsuarioAutenticadoOwner(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(userId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Usuario usuario = usuarioOpt.get();

        LocalDate hoje = LocalDate.now();
        int mesAtual = hoje.getMonthValue();
        int anoAtual = hoje.getYear();
        int mesAnterior = mesAtual == 1 ? 12 : mesAtual - 1;
        int anoAnterior = mesAtual == 1 ? anoAtual - 1 : anoAtual;

        List<Receita> todasReceitas = receitaService.buscarPorUsuario(usuario);
        List<Despesa> todasDespesas = despesaService.listarPorUsuario(usuario);

        // Faturado no mês atual (soma de receitas do mês corrente)
        double faturadoMes = todasReceitas.stream()
                .filter(r -> r.getData() != null
                        && r.getData().getMonthValue() == mesAtual
                        && r.getData().getYear() == anoAtual)
                .mapToDouble(Receita::getValor)
                .sum();

        // Despesas do mês atual
        double despesasMes = todasDespesas.stream()
                .filter(d -> d.getData() != null
                        && d.getData().getMonthValue() == mesAtual
                        && d.getData().getYear() == anoAtual)
                .mapToDouble(Despesa::getValor)
                .sum();

        // Projeção: extrapola o faturado até o fim do mês com base no dia atual
        int diaAtual = hoje.getDayOfMonth();
        int diasNoMes = hoje.lengthOfMonth();
        double projetado = diaAtual > 0 ? (faturadoMes / diaAtual) * diasNoMes : 0;

        // Saldo projetado = projeção de receitas - despesas do mês
        double saldo = projetado - despesasMes;

        // Retirada segura = 70% do saldo projetado (reserva de 30% para custos)
        double retirada = Math.max(0, saldo * 0.70);

        // Crescimento em relação ao mês anterior
        double faturadoMesAnterior = todasReceitas.stream()
                .filter(r -> r.getData() != null
                        && r.getData().getMonthValue() == mesAnterior
                        && r.getData().getYear() == anoAnterior)
                .mapToDouble(Receita::getValor)
                .sum();

        String crescimento;
        if (faturadoMesAnterior == 0) {
            crescimento = faturadoMes > 0 ? "+100%" : "0%";
        } else {
            double pct = ((faturadoMes - faturadoMesAnterior) / faturadoMesAnterior) * 100;
            crescimento = (pct >= 0 ? "+" : "") + String.format("%.1f", pct) + "%";
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("faturado", faturadoMes);
        result.put("projetado", projetado);
        result.put("saldo", saldo);
        result.put("retirada", retirada);
        result.put("crescimento", crescimento);

        return ResponseEntity.ok(result);
    }
}
