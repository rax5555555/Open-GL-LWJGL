#version 400 core

uniform float time;
uniform vec2 resolution;

void main( void ) {
	vec2 p = ( gl_FragCoord.xy / resolution.xy ) * 2.0 - 1.0;
	vec3 c = vec3( 0.0 );
	float amplitude = 0.10;
	float glowT = sin(time) * 0.5 + 0.5;
	float glowFactor = mix( 0.1, 0.15, glowT );
	c += vec3(0.1, 0.03, 0.13) * ( glowFactor * abs( 1.0 / sin(p.x + sin( p.y + time ) * amplitude ) ));
	c += vec3(0.02, 0.10, 0.03) * ( glowFactor * abs( 1.0 / sin(p.x + sin( p.y + time+1.00 ) * amplitude+0.1 ) ));
    c += vec3(0.3, 0.10, 0.02) * ( glowFactor * abs( 1.0 / sin(p.x + sin( p.y + time+3.00 ) * amplitude+0.2 ) ));
	gl_FragColor = vec4( c, 1.0 );
}
