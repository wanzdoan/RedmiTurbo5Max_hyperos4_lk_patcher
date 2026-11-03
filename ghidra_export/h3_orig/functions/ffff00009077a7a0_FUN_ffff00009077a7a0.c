// ffff00009077a7a0  size=48  FUN_ffff00009077a7a0

ulong FUN_ffff00009077a7a0(long param_1)

{
  ulong uVar1;
  
  uVar1 = DAT_ffff000090832828 + 7U & 0xfffffffffffffff8;
  DAT_ffff000090832828 = uVar1 + (param_1 + 7U & 0xfffffffffffffff8);
  return uVar1;
}


