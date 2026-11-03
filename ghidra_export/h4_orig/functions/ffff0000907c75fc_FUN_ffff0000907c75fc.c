// ffff0000907c75fc  size=36  FUN_ffff0000907c75fc

uint FUN_ffff0000907c75fc(long param_1,ulong param_2)

{
  return *(uint *)(*(long *)(param_1 + 0x10) + (param_2 >> 3 & 0x1ffffffffffffffc)) >>
         (ulong)((uint)param_2 & 0x1f) & 1;
}


