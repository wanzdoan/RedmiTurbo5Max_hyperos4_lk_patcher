
void FUN_ffff0000be05ab74(long param_1)

{
  *(undefined2 *)(param_1 + 0x1730) = 0;
  *(undefined4 *)(param_1 + 0x1734) = 0;
  *(long *)(param_1 + 0xb58) = param_1 + 0xd4;
  *(undefined ***)(param_1 + 0xb68) = &PTR_DAT_ffff0000be0ef188;
  *(undefined ***)(param_1 + 0xb80) = &PTR_DAT_ffff0000be0ef1a8;
  *(long *)(param_1 + 0xb70) = param_1 + 0x9c8;
  *(undefined **)(param_1 + 0xb98) = &DAT_ffff0000be0ef1c8;
  *(long *)(param_1 + 0xb88) = param_1 + 0xabc;
  FUN_ffff0000be05abc4();
  return;
}


