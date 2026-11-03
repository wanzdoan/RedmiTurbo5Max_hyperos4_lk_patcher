
ulong FUN_ffff00009077aa68(long param_1)

{
  undefined *puVar1;
  
  puVar1 = PTR_DAT_ffff000090832848 + 7;
  PTR_DAT_ffff000090832848 =
       (undefined *)(((ulong)puVar1 & 0xfffffffffffffff8) + (param_1 + 7U & 0xfffffffffffffff8));
  return (ulong)puVar1 & 0xfffffffffffffff8;
}


