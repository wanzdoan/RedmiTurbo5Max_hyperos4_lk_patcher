// ffff00009077ad3c  size=204  FUN_ffff00009077ad3c

void FUN_ffff00009077ad3c(undefined8 param_1,undefined8 param_2)

{
  undefined **ppuVar1;
  undefined *puVar2;
  long lVar3;
  undefined **ppuVar4;
  undefined *puVar5;
  undefined8 local_18;
  
  if ((int)param_1 == 1) {
    puVar2 = (undefined *)FUN_ffff00009077a9c4();
    if (puVar2 != (undefined *)0x0) {
      ppuVar4 = (undefined **)PTR_LOOP_ffff000090832838;
      if ((undefined **)PTR_LOOP_ffff000090832838 != &PTR_LOOP_ffff000090832830) {
        do {
          puVar5 = ppuVar4[7];
          if ((puVar5 <= puVar2) && (puVar2 < puVar5 + ((ulong)ppuVar4[5] >> 0xc) * 0x18)) {
            puVar2 = ppuVar4[4] + ((ulong)((long)puVar2 - (long)puVar5) / 0x18) * 0x1000;
            goto LAB_ffff00009077adf8;
          }
          ppuVar1 = ppuVar4 + 1;
          ppuVar4 = (undefined **)*ppuVar1;
        } while ((undefined **)*ppuVar1 != &PTR_LOOP_ffff000090832830);
      }
      puVar2 = (undefined *)0xffffffffffffffff;
LAB_ffff00009077adf8:
      FUN_ffff00009077b004(puVar2);
      return;
    }
  }
  else {
    local_18 = 0;
    lVar3 = FUN_ffff00009077ae08(param_1,0xc,&local_18,param_2);
    if (lVar3 != 0) {
      FUN_ffff00009077b004(local_18);
    }
  }
  return;
}


