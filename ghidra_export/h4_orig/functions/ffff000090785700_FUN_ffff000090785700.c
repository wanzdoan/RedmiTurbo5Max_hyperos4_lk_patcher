// ffff000090785700  size=48  FUN_ffff000090785700

uint FUN_ffff000090785700(ulong param_1)

{
  return *(uint *)((param_1 >> 1 & 0x7ffffff0) - 0xffffeffd2e00) >> ((uint)param_1 & 0x1f) & 1;
}


