
ulong FUN_ffff0000be05c95c(long param_1)

{
  undefined *puVar1;
  
  puVar1 = PTR_DAT_ffff0000be0ef240 + 7;
  PTR_DAT_ffff0000be0ef240 =
       (undefined *)(((ulong)puVar1 & 0xfffffffffffffff8) + (param_1 + 7U & 0xfffffffffffffff8));
  return (ulong)puVar1 & 0xfffffffffffffff8;
}


