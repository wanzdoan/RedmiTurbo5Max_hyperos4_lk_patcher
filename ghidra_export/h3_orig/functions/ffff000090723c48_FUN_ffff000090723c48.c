// ffff000090723c48  size=56  FUN_ffff000090723c48

uint FUN_ffff000090723c48(ulong param_1,long *param_2)

{
  return *(uint *)(*(long *)(*param_2 + 8) +
                  ((param_1 & 0xffffffff) >> ((ulong)*(uint *)(param_2 + 1) & 0x3f) &
                  (ulong)*(uint *)(*param_2 + 0x10)) * 4) ^ -(int)*(char *)((long)param_2 + 0xc);
}


