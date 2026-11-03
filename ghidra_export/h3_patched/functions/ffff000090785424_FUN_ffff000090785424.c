// ffff000090785424  size=20  FUN_ffff000090785424

uint FUN_ffff000090785424(uint param_1)

{
  if (0x10e < param_1) {
    return 0xfffffffe;
  }
  return *(uint *)(((ulong)(param_1 >> 1) & 0x7ffffff0) - 0xffffeffd2e00) >> (param_1 & 0x1f) & 1;
}


