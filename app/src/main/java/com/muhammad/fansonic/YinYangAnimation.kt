package com.muhammad.fansonic

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ShaderBrush

private const val YY_SHADER = """
uniform float2 resolution;
uniform float progress;   // 0..1
uniform float rotation;   // degrees
float sdCircle(vec2 p, float r){ return length(p) - r; }
float sdRightHalf(vec2 p){ return -p.x; }   // <0 when x>=0 (right side)
// rotate around origin by degrees
vec2 rot(vec2 p, float deg){
  float a = radians(deg);
  float c = cos(a), s = sin(a);
  return vec2(c*p.x - s*p.y, s*p.x + c*p.y);
}
half4 main(float2 fragCoord){
  float2 res = resolution;
  float s = min(res.x, res.y);
  // center at (0,0); units so that LEFT/RIGHT edges are ~±0.5
  vec2 p = (fragCoord - res*0.5) / s;
  p = rot(p, rotation);
  // target taijitu geometry at radius R = 0.5  (fits on any aspect)
  float R = 0.45;
  float rSmall = R * 0.5;
  float dOuter  = sdCircle(p, R);                        // <0 inside big circle
  float dRight  = sdRightHalf(p);
  float dTopSm  = sdCircle(p - vec2(0.0, -rSmall), rSmall);
  float dBotSm  = sdCircle(p - vec2(0.0,  +rSmall), rSmall);
  // right semicircle = outer ∩ right half-plane
  float dRightSemi = max(dOuter, dRight);
  // union with bottom small circle
  float dUnion = min(dRightSemi, dBotSm);
  // subtract top small circle -> black region of final taijitu
  float dBlackYY = max(dUnion, -dTopSm);
  // start state: plain vertical split (black on right)
  float dSplit = sdRightHalf(p);
  // morph boundary: interpolate SDFs (keeps edges crisp)
  float d = mix(dSplit, dBlackYY, progress);
  // base color from SDF (1 = black, 0 = white)
  float black = step(d, 0.0);
  // eyes grow in as we approach the circle
  float eyeR = mix(0.0, R * 0.18, progress); // final eye size ~ R*0.18
  float dEyeTop = sdCircle(p - vec2(0.0, -rSmall), eyeR);
  float dEyeBot = sdCircle(p - vec2(0.0,  +rSmall), eyeR);
  float inTopEye = 1.0 - step(0.0, dEyeTop);
  float inBotEye = 1.0 - step(0.0, dEyeBot);
  // top eye is black, bottom eye is white
  black = max(black, inTopEye);
  black = black * (1.0 - inBotEye);
  // fade outside of the big circle to white only near the end,
  // so earlier frames still use the full-screen split.
  float outside = step(0.0, dOuter); // 1 outside circle
  float outFade = smoothstep(1.0, 1.0, progress);
  black = mix(black, 0.0, outside * outFade);
  return half4(black, black, black, 1.0);
}
"""

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun YinYangAnimation(
    cycleMs: Int = 4200,
    holdMs: Int = 900,
    rotatePerCycle : Float = 180f
) {
    val transition = rememberInfiniteTransition("phase")
    val phase by transition.animateFloat(
        initialValue = 0f , targetValue = 1f, animationSpec = infiniteRepeatable(
            animation  = keyframes {
                durationMillis = cycleMs
                0f at 0 using FastOutSlowInEasing
                1f at (cycleMs - holdMs) using FastOutSlowInEasing
                1f at cycleMs
            }, repeatMode = RepeatMode.Reverse
        ), label = "phaseAnimation"
    )
    val progress = FastOutSlowInEasing.transform(phase).coerceIn(0f ,1f)
    val rotation = rotatePerCycle * phase
    val shader = remember { RuntimeShader(YY_SHADER) }
    Canvas(modifier = Modifier.fillMaxSize()) {
        shader.setFloatUniform("resolution", size.width, size.height)
        shader.setFloatUniform("progress", progress)
        shader.setFloatUniform("rotation", rotation)
        drawRect(brush = ShaderBrush(shader), size = Size(width = size.width, height =size.height))
    }
}