package br.com.ebc.kboard.web.spring.filter

import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CorsFilter extends OncePerRequestFilter {
  protected def doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    response.addHeader("Access-Control-Allow-Origin", "*")
    if (request.getHeader("Access-Control-Request-Method") != null && ("OPTIONS" == request.getMethod)) {
      response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
      response.addHeader("Access-Control-Allow-Headers", "X-Requested-With,Origin,Content-Type, Accept")
    }
    filterChain.doFilter(request, response)
  }
}