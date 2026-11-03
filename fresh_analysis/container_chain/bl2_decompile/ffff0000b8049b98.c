
void FUN_ffff0000b8049b98(undefined8 param_1,undefined8 param_2)

{
  undefined **ppuVar1;
  undefined *puVar2;
  long lVar3;
  undefined **ppuVar4;
  undefined *puVar5;
  undefined8 local_18;
  
  if ((int)param_1 == 1) {
    puVar2 = (undefined *)FUN_ffff0000b8049820();
    if (puVar2 != (undefined *)0x0) {
      ppuVar4 = (undefined **)PTR_LOOP_ffff0000b8102030;
      if ((undefined **)PTR_LOOP_ffff0000b8102030 != &PTR_LOOP_ffff0000b8102028) {
        do {
          puVar5 = ppuVar4[7];
          if ((puVar5 <= puVar2) && (puVar2 < puVar5 + ((ulong)ppuVar4[5] >> 0xc) * 0x18)) {
            puVar2 = ppuVar4[4] + ((ulong)((long)puVar2 - (long)puVar5) / 0x18) * 0x1000;
            goto LAB_ffff0000b8049c54;
          }
          ppuVar1 = ppuVar4 + 1;
          ppuVar4 = (undefined **)*ppuVar1;
        } while ((undefined **)*ppuVar1 != &PTR_LOOP_ffff0000b8102028);
      }
      puVar2 = (undefined *)0xffffffffffffffff;
LAB_ffff0000b8049c54:
      FUN_ffff0000b8049e60(puVar2);
      return;
    }
  }
  else {
    local_18 = 0;
    lVar3 = FUN_ffff0000b8049c64(param_1,0xc,&local_18,param_2);
    if (lVar3 != 0) {
      FUN_ffff0000b8049e60(local_18);
    }
  }
  return;
}


