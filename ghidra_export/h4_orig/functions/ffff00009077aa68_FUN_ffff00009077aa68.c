// ffff00009077aa68  size=48  FUN_ffff00009077aa68

ulong FUN_ffff00009077aa68(long param_1)

{
  ulong uVar1;
  
  uVar1 = DAT_ffff000090832848 + 7U & 0xfffffffffffffff8;
  DAT_ffff000090832848 = uVar1 + (param_1 + 7U & 0xfffffffffffffff8);
  return uVar1;
}


