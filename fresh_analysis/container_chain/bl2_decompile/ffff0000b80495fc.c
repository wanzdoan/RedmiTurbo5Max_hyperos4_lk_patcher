
ulong FUN_ffff0000b80495fc(long param_1)

{
  undefined *puVar1;
  
  puVar1 = PTR_DAT_ffff0000b8102020 + 7;
  PTR_DAT_ffff0000b8102020 =
       (undefined *)(((ulong)puVar1 & 0xfffffffffffffff8) + (param_1 + 7U & 0xfffffffffffffff8));
  return (ulong)puVar1 & 0xfffffffffffffff8;
}


