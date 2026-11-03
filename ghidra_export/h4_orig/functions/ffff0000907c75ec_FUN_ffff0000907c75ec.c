// ffff0000907c75ec  size=24  FUN_ffff0000907c75ec

uint FUN_ffff0000907c75ec(long param_1,ulong param_2)

{
  if (param_2 < (ulong)(*(long *)(param_1 + 8) << 5)) {
    return *(uint *)(*(long *)(param_1 + 0x10) + (param_2 >> 3 & 0x1ffffffffffffffc)) >>
           (ulong)((uint)param_2 & 0x1f) & 1;
  }
  return 0;
}


