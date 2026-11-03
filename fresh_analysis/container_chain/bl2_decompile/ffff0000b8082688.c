
void FUN_ffff0000b8082688(ulong param_1,long param_2)

{
  long lVar1;
  bool bVar2;
  ulong uVar3;
  
  FUN_ffff0000b800ad58(&DAT_ffff0000b81b9b08);
  bVar2 = (param_1 & 7) != 0;
  uVar3 = param_1 + 7 & 0xfffffffffffffff8;
  DAT_ffff0000b81b9ae8 = param_1;
  if (bVar2) {
    DAT_ffff0000b81b9ae8 = uVar3;
  }
  lVar1 = 0;
  if (bVar2) {
    lVar1 = param_1 - uVar3;
  }
  DAT_ffff0000b81b9af8 = 0;
  DAT_ffff0000b81b9b00 = 0;
  DAT_ffff0000b81b9af0 = lVar1 + param_2;
  DAT_ffff0000b81b9b40 = &DAT_ffff0000b81b9b40;
  DAT_ffff0000b81b9b48 = &DAT_ffff0000b81b9b40;
  if (DAT_ffff0000b81b9af0 != 0) {
    *(long *)(DAT_ffff0000b81b9ae8 + 0x10) = DAT_ffff0000b81b9af0;
    FUN_ffff0000b808257c();
    return;
  }
  return;
}


