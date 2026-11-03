
void FUN_ffff0000b80435a8(code *param_1)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  ulong uVar5;
  long lVar6;
  long lVar7;
  undefined4 *puVar8;
  
  lVar3 = FUN_ffff0000b804bccc(0x1000);
  if (lVar3 != 0) {
    lVar7 = 0;
    lVar6 = 0;
    puVar8 = (undefined4 *)&DAT_ffff000000117c00;
    do {
      iVar2 = FUN_ffff0000b804d74c(lVar3 + lVar6,0x1000 - lVar6,&DAT_ffff0000b808875a,*puVar8);
      uVar1 = (uint)lVar7 & 0xff;
      lVar6 = lVar6 + iVar2;
      if ((uVar1 * -0x55555555 >> 2 | uVar1 * -0x40000000) < 0x15555556) {
        iVar2 = FUN_ffff0000b804d74c(lVar3 + lVar6,0x1000 - lVar6,&DAT_ffff0000b809b862);
        lVar6 = lVar6 + iVar2;
      }
      lVar7 = lVar7 + 1;
      puVar8 = puVar8 + 1;
    } while (lVar7 != 0x100);
    uVar4 = FUN_ffff0000b8049ec8(lVar3);
    uVar5 = (*param_1)(uVar4,lVar6);
    if ((uVar5 & 1) == 0) {
      FUN_ffff0000b804c33c(s__ThermalStat_thermal_log_dump_fa_ffff0000b8093c04);
    }
    thunk_FUN_ffff0000b8082558(lVar3);
    return;
  }
  return;
}


