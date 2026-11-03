
undefined8 FUN_ffff000090785730(uint param_1,uint param_2)

{
  undefined8 uVar1;
  
  uVar1 = 0xfffffffe;
  if ((param_1 < 0x10f) && ((int)param_2 < 8)) {
    uVar1 = 0;
    *(uint *)(((ulong)(param_1 << 1) & 0xfffffff0) - 0xffffeffd2cf4) =
         1 << (ulong)((param_1 & 7) << 2 | 3) | (param_2 & 7) << (ulong)((param_1 & 7) << 2);
  }
  return uVar1;
}


