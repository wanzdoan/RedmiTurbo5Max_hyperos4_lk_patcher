// ffff000090778c80  size=80  FUN_ffff000090778c80

void FUN_ffff000090778c80(long param_1)

{
  *(undefined2 *)(param_1 + 0x1730) = 0;
  *(undefined4 *)(param_1 + 0x1734) = 0;
  *(long *)(param_1 + 0xb58) = param_1 + 0xd4;
  *(undefined ***)(param_1 + 0xb68) = &PTR_DAT_ffff000090832790;
  *(undefined ***)(param_1 + 0xb80) = &PTR_DAT_ffff0000908327b0;
  *(long *)(param_1 + 0xb70) = param_1 + 0x9c8;
  *(undefined **)(param_1 + 0xb98) = &DAT_ffff0000908327d0;
  *(long *)(param_1 + 0xb88) = param_1 + 0xabc;
  FUN_ffff000090778cd0();
  return;
}


